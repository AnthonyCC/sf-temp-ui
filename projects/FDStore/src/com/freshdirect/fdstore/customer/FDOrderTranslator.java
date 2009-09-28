/*
 * $Workfile:FDOrderTranslator.java$
 *
 * $Date:5/15/2003 5:34:55 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.customer.EnumWebServiceType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;

/**
 * Translates an FDOrder into an ErpOrder.
 *
 * @version $Revision:16$
 * @author $Author:Mike Rose$
 */
public class FDOrderTranslator {

	public static ErpCreateOrderModel getErpCreateOrderModel(FDCartModel cart, EnumSaleType saleType) throws FDResourceException {
		return getErpCreateOrderModel(cart,saleType, false);
	}

	public static ErpCreateOrderModel getErpCreateOrderModel(FDCartModel cart) throws FDResourceException {
		return getErpCreateOrderModel(cart,false);
	}
	public static ErpCreateOrderModel getErpCreateOrderModel(FDCartModel cart, boolean skipModifyLines) throws FDResourceException {
		ErpCreateOrderModel createOrder = new ErpCreateOrderModel();
		translateOrder(cart, createOrder, skipModifyLines);
		return createOrder;
	}

	public static ErpModifyOrderModel getErpModifyOrderModel(FDCartModel cart) throws FDResourceException {
		ErpModifyOrderModel modifyOrder = new ErpModifyOrderModel();
		translateOrder(cart, modifyOrder, false);
		return modifyOrder;
	}
	
	public static ErpCreateOrderModel getErpCreateOrderModel(FDCartModel cart,EnumSaleType saleType, boolean skipModifyLines) throws FDResourceException {
		ErpCreateOrderModel createOrder = new ErpCreateOrderModel();
		//createOrder.setSaleType(saleType);
		//if(EnumSaleType.REGULAR.equals(saleType.getSaleType())) {
			translateOrder(cart, createOrder, skipModifyLines);
		//}
		/*else if(EnumSaleType.SUBSCRIPTION.equals(saleType)) {
			translateSubscriptionOrder(cart, createOrder, skipModifyLines);
		}*/
		return createOrder;
	}

	private static void translateOrder(FDCartModel cart, ErpAbstractOrderModel order, boolean skipModifyLines) throws FDResourceException {
		try {
			order.setPaymentMethod(cart.getPaymentMethod());
			//System.out.println("Selected gift cards "+cart.getSelectedGiftCards() != null ? cart.getSelectedGiftCards().size() : 0);
			order.setSelectedGiftCards(cart.getSelectedGiftCards());
			FDReservation deliveryReservation = cart.getDeliveryReservation();
			ErpDeliveryInfoModel deliveryInfo = new ErpDeliveryInfoModel();
			deliveryInfo.setDeliveryReservationId(deliveryReservation.getPK().getId());
			deliveryInfo.setDeliveryAddress(cart.getDeliveryAddress());
			if(deliveryReservation.getTimeslot()!=null) {
				deliveryInfo.setDeliveryStartTime(deliveryReservation.getStartTime());
				deliveryInfo.setDeliveryEndTime(deliveryReservation.getEndTime());
				deliveryInfo.setDeliveryCutoffTime(deliveryReservation.getCutoffTime());
				order.setRequestedDate(deliveryReservation.getStartTime());
			}
			else {
				Calendar startTime=Calendar.getInstance();
				Calendar endTime=Calendar.getInstance();
				Calendar cutOffTime=Calendar.getInstance();
				cutOffTime.add(Calendar.DATE, -1);
				endTime.add(Calendar.HOUR, 2);
				deliveryInfo.setDeliveryStartTime(startTime.getTime());
				deliveryInfo.setDeliveryEndTime(endTime.getTime());
				order.setRequestedDate(startTime.getTime());
				deliveryInfo.setDeliveryCutoffTime(cutOffTime.getTime());
			}
			deliveryInfo.setDeliveryZone(cart.getZoneInfo().getZoneCode());
			deliveryInfo.setDeliveryRegionId(cart.getZoneInfo().getRegionId());
			if (cart.getDeliveryAddress() instanceof ErpDepotAddressModel) {
				ErpDepotAddressModel depotAddress = (ErpDepotAddressModel) cart.getDeliveryAddress();
				deliveryInfo.setDepotLocationId(depotAddress.getLocationId());
				if (depotAddress.isPickup()) {
					deliveryInfo.setDeliveryType(EnumDeliveryType.PICKUP);
				}else{
					deliveryInfo.setDeliveryType(EnumDeliveryType.DEPOT);
				}
			} else {
				ErpAddressModel address = cart.getDeliveryAddress();
				if(EnumServiceType.WEB.equals(address.getServiceType())){
					EnumWebServiceType webServiceType = address.getWebServiceType();
					deliveryInfo.setDeliveryType(EnumDeliveryType.getDeliveryType(webServiceType.getName()));
				} else if(EnumServiceType.CORPORATE.equals(address.getServiceType())){
					deliveryInfo.setDeliveryType(EnumDeliveryType.CORPORATE);
				} else {
					deliveryInfo.setDeliveryType(EnumDeliveryType.HOME);
				}
			}
			order.setDeliveryInfo(deliveryInfo);
			order.setPricingDate(Calendar.getInstance().getTime());
			
			order.setTax(cart.getTaxValue());
			
			// instead we add the actual subtotal which is subtotal + disount amount for the order 
			//order.setSubTotal(cart.getSubTotal());
			order.setSubTotal(cart.getActualSubTotal());
			
			
			order.setGlCode(lookupGLCode(cart.getDeliveryAddress()));

			List orderLines = new ArrayList();
			int num = 0;
			for (Iterator i = cart.getOrderLines().iterator(); i.hasNext();) {
				FDCartLineI line = (FDCartLineI) i.next();
				if (skipModifyLines && (line instanceof FDModifyCartLineI)) {
					continue;
				}
				num += addTranslatedLine(num, line, orderLines);
			}
			for (Iterator i = cart.getSampleLines().iterator(); i.hasNext();) {
				num += addTranslatedLine(num, (FDCartLineI) i.next(), orderLines);
			}
			order.setOrderLines(orderLines);

			//
			// Convert cart's customer credits to applied credits for the order
			// We cannot just take credits from the original order and set them on 
			// modify order as they will have the PK which is not correct
			List aList = new ArrayList();
			for(Iterator i = cart.getCustomerCredits().iterator(); i.hasNext(); ){
				aList.add(new ErpAppliedCreditModel((ErpAppliedCreditModel)i.next()));
			}
			order.setAppliedCredits(aList);

			//
			// Transfer cart charges to order model
			// We cannot just take charges from the original order and set them on 
			// modify order as they will have the PK which is not correct
			List cList = new ArrayList();
			for(Iterator i = cart.getCharges().iterator(); i.hasNext(); ) {
				cList.add(new ErpChargeLineModel((ErpChargeLineModel) i.next()));
			}
			order.setCharges(cList);

			// add discount to promotion list
			if (cart.getDiscounts() != null && cart.getDiscounts().size() > 0) {
				order.setDiscounts(cart.getDiscounts());				
			}
			
			//
			// Set miscellaneous messages
			//
			order.setCustomerServiceMessage(cart.getCustomerServiceMessage() == null ? "" : cart.getCustomerServiceMessage());
			order.setMarketingMessage(cart.getMarketingMessage() == null ? "" : cart.getMarketingMessage());
			//set the delivery pass info.
			order.setDeliveryPassCount(cart.getDeliveryPassCount());
			order.setDlvPassApplied(cart.isDlvPassApplied());
			order.setDlvPromotionApplied(cart.isDlvPromotionApplied());
			
			order.setBufferAmt(cart.getBufferAmt());
			
		} catch (FDInvalidConfigurationException ex) {
			throw new FDResourceException(ex, "Invalid configuration encountered");
		}
	}

	
	public static void translateSubscriptionOrder(FDCartModel cart, ErpAbstractOrderModel order, boolean skipModifyLines) throws FDResourceException {
		try {
			order.setPaymentMethod(cart.getPaymentMethod());
			
			order.setPricingDate(Calendar.getInstance().getTime());
			order.setRequestedDate(order.getPricingDate());
			order.setTax(cart.getTaxValue());
			order.setSubTotal(cart.getSubTotal());
			

			List orderLines = new ArrayList();
			int num = 0;
			for (Iterator i = cart.getOrderLines().iterator(); i.hasNext();) {
				FDCartLineI line = (FDCartLineI) i.next();
				if (skipModifyLines && (line instanceof FDModifyCartLineI)) {
					continue;
				}
				num += addTranslatedLine(num, line, orderLines);
			}
			for (Iterator i = cart.getSampleLines().iterator(); i.hasNext();) {
				num += addTranslatedLine(num, (FDCartLineI) i.next(), orderLines);
			}
			order.setOrderLines(orderLines);


			//
			// Transfer cart charges to order model
			// We cannot just take charges from the original order and set them on 
			// modify order as they will have the PK which is not correct
			List cList = new ArrayList();
			for(Iterator i = cart.getCharges().iterator(); i.hasNext(); ) {
				cList.add(new ErpChargeLineModel((ErpChargeLineModel) i.next()));
			}
			order.setCharges(cList);

			// add discount to promotion list
			if (cart.getDiscounts() != null && cart.getDiscounts().size() > 0) {
				order.setDiscounts(cart.getDiscounts());				
			}
			
			//
			// Set miscellaneous messages
			//
			order.setCustomerServiceMessage(cart.getCustomerServiceMessage() == null ? "" : cart.getCustomerServiceMessage());
			order.setMarketingMessage(cart.getMarketingMessage() == null ? "" : cart.getMarketingMessage());
			//set the delivery pass info.
			order.setDeliveryPassCount(cart.getDeliveryPassCount());
		} catch (FDInvalidConfigurationException ex) {
			throw new FDResourceException(ex, "Invalid configuration encountered");
		}
	}

	/** @return number of lines added */
	private static int addTranslatedLine(int baseLineNumber, FDCartLineI cartLine, List orderLines) throws FDResourceException, FDInvalidConfigurationException {
		List erpLines = cartLine.buildErpOrderLines(baseLineNumber);
		orderLines.addAll(erpLines);
		return erpLines.size();
	}
	
	private static String lookupGLCode(AddressModel address) throws FDResourceException {
		FDDeliveryManager dlvMan = FDDeliveryManager.getInstance();
		return dlvMan.getMunicipalityInfos().getGlCode(address.getState(), dlvMan.getCounty(address), address.getCity());
	}

}