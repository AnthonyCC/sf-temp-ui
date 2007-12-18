package com.freshdirect.fdstore.customer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;
import javax.servlet.jsp.JspException;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDConfiguredPrice;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.sap.PosexUtil;
import com.sap.mw.jco.JCO.Exception;

public class ErpSaleModelFactory {

	
	public static ErpSaleModel createErpSaleModel(String orderType) throws FDResourceException, ErpFraudException, ErpAuthorizationException, ReservationException, DeliveryPassException, FDInvalidConfigurationException, JspException, FDAuthenticationException, RemoteException, CreateException
	{
		String erpCustomerId="516479246";  // customer having 300$ credit
		String fdCustomerId="516479248";
		//String redemptionCode="USRJO2";
		
		FDIdentity identity = new FDIdentity(erpCustomerId,fdCustomerId);
		FDCartModel cart=makeFDCartModel(identity,1,orderType);
		
		if (cart == null)
			return null;

		
		// applying the discount
		cart.addDiscount(createDiscount());
		// applying the applied credit
		ArrayList list=new ArrayList();
		list.add(createAppliedCreaditModel());
		cart.setCustomerCredits(list);		  		
		
		ArrayList crgList=new ArrayList();
		crgList.add(createChargeLineModel());
		cart.setCharges(crgList);		
		
		ErpCreateOrderModel createModel=new ErpCreateOrderModel();		
		createModel.setAppliedCredits(cart.getCustomerCredits());
		createModel.setDiscounts(cart.getDiscounts());	
		createModel.setDeliveryInfo(getDeliveryInfoModel(cart));
		createModel.setPaymentMethod(cart.getPaymentMethod());
		createModel.setSubTotal(cart.getSubTotal());
        createModel.setTax(cart.getTaxValue());
        createModel.setOrderLines(getOrderLines(cart));
        
        List cList = new ArrayList();
		for(Iterator i = cart.getCharges().iterator(); i.hasNext(); ) {
			cList.add(new ErpChargeLineModel((ErpChargeLineModel) i.next()));
		}
				
		createModel.setCharges(cList);                
        Set promoSet=new HashSet();               
        promoSet.add("15PRCNTUSRJGRP2");                
        ErpSaleModel model=new ErpSaleModel(new PrimaryKey(erpCustomerId),createModel,promoSet,null);
        //ErpSaleModel model=new ErpSaleModel(new PrimaryKey(erpCustomerId),status ,transactions,new ArrayList(),"1002828", null,promoSet,null,null);        
        model.setPK(new PrimaryKey("PK1"));
        return model;        
	}				
 
	
	public static ErpAuthorizationModel createErpAuthorizationModel(ErpSaleModel saleModel,String orderType)
	{	
		ErpAbstractOrderModel order = saleModel.getCurrentOrder();
		ErpAuthorizationModel authModel=new ErpAuthorizationModel();				
		if("FDONLY".equalsIgnoreCase(orderType))
		{
		   authModel.setAffiliate(ErpAffiliate.getEnum(ErpAffiliate.CODE_FD));
		   authModel.setAmount(12.43);
		}
		else
		{
		   authModel.setAffiliate(ErpAffiliate.getEnum(ErpAffiliate.CODE_BC));
		   authModel.setAmount(49.85);
		}

		authModel.setAuthCode("718694");			
		ErpPaymentMethodI payment=order.getPaymentMethod();
		authModel.setAbaRouteNumber(payment.getAbaRouteNumber());
		authModel.setBankAccountType(payment.getBankAccountType());
		authModel.setCardType(payment.getCardType());
		authModel.setCcNumLast4(payment.getAccountNumber());
		authModel.setDescription("some desc");
		authModel.setPaymentMethodType(payment.getPaymentMethodType());
		authModel.setResponseCode(EnumPaymentResponse.APPROVED);	
		authModel.setAvs("Y");
		return authModel;
	}
	
	public static ErpInvoiceModel createErpInvoiceModel(ErpSaleModel saleModel)
	{		
		ErpAbstractOrderModel order = saleModel.getCurrentOrder();
		ErpInvoiceModel invModel=new ErpInvoiceModel();
		invModel.setAmount(order.getAmount());
		invModel.setDiscounts(order.getDiscounts());
		invModel.setAppliedCredits(order.getAppliedCredits());
		invModel.setSubTotal(order.getSubTotal());  //reduce it so that capture fails 
		invModel.setCharges(order.getCharges());
		invModel.setInvoiceLines(createErpInvoiceLineModel(order.getOrderLines()));
		invModel.setPK(new PrimaryKey("PK1"));
		invModel.setTax(order.getTax());
		invModel.setTransactionDate(new Date());
		invModel.setTransactionInitiator("System");		
        return invModel;		
		
	}
	
	
	public static List createErpInvoiceLineModel(List orderLineList)
	{
		List invLineList=new ArrayList();	
		for(int i=0;i<orderLineList.size();i++)
		{
		 ErpOrderLineModel line=(ErpOrderLineModel)orderLineList.get(i);
		 ErpInvoiceLineModel model= new ErpInvoiceLineModel();
		 model.setActualCost(line.getPrice());
		 model.setDepositValue(line.getDepositValue());
		 model.setMaterialNumber(line.getMaterialNumber());
		 model.setOrderLineNumber(line.getOrderLineNumber());
		 model.setPK(line.getPK());
		 model.setPrice(line.getPrice()); //reduce the price so that capture strategy will fail 
		 //model.setPrice(35.6);
		 model.setQuantity(line.getQuantity());
		 model.setTaxValue(line.getPrice()*line.getTaxRate());
		 invLineList.add(model);
		}
		return invLineList;
	}
	
	public static ErpDeliveryInfoModel getDeliveryInfoModel(FDCartModel cart)
	{
			FDReservation deliveryReservation = cart.getDeliveryReservation();
			ErpDeliveryInfoModel deliveryInfo = new ErpDeliveryInfoModel();
			deliveryInfo.setDeliveryReservationId(deliveryReservation.getPK().getId());
			deliveryInfo.setDeliveryAddress(cart.getDeliveryAddress());
			deliveryInfo.setDeliveryStartTime(deliveryReservation.getStartTime());
			deliveryInfo.setDeliveryEndTime(deliveryReservation.getEndTime());
			deliveryInfo.setDeliveryCutoffTime(deliveryReservation.getCutoffTime());
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
				if(EnumServiceType.CORPORATE.equals(address.getServiceType())){
					deliveryInfo.setDeliveryType(EnumDeliveryType.CORPORATE);
				} else {
					deliveryInfo.setDeliveryType(EnumDeliveryType.HOME);
				}
			}
			return deliveryInfo;		
	}
				
	
	public static List getOrderLines(FDCartModel cart) throws FDResourceException, FDInvalidConfigurationException
	{
		
		List orderLines = new ArrayList();
		int num = 0;
		for (Iterator i = cart.getOrderLines().iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			List erpLines = buildErpOrderLines(num,line);
			orderLines.addAll(erpLines);
			num += erpLines.size();
		}
		return orderLines;		
	}
	
	
	public static List buildErpOrderLines(int num,FDCartLineI model)
	{
		ErpOrderLineModel  ol = new ErpOrderLineModel();
		ol.setAffiliate(model.getAffiliate());
		ol.setAlcohol(model.isAlcohol());
		ol.setCartlineId(model.getCartlineId());
		ol.setConfiguration((FDConfiguration)model.getConfiguration());
		ol.setConfigurationDesc(model.getConfigurationDesc());
		ol.setDeliveryPass(false);
		ol.setDepartmentDesc(model.getDepartmentDesc());
		ol.setDepositValue(model.getDepositValue());
		ol.setDescription(model.getDescription());
		ol.setDiscount(model.getDiscount());
		ol.setMaterialNumber(model.getMaterialNumber());
		ol.setOrderLineNumber(PosexUtil.getPosex(num));
		ol.setPerishable(model.isPerishable());
		ol.setPK(new PrimaryKey("PK1"));
		ol.setPrice(model.getPrice());
		ol.setRecipeSourceId(model.getRecipeSourceId());
		ol.setRequestNotification(model.isRequestNotification());
		ol.setSku(model.getSku());
		ol.setTaxRate(model.getTaxRate());
		List ols = new ArrayList(1);
		ols.add(ol);
		return ols;
		
	}
	
	
	public static FDCartModel makeFDCartModel(FDIdentity identity, int orderSize,String orderType) throws JspException, FDResourceException, FDInvalidConfigurationException {

		FDCartModel cart = new FDCartModel();
	try{
		for (int i = 0; i < orderSize; i++) {
			if("FDONLY".equalsIgnoreCase(orderType))
			{
			  cart.addOrderLine(createFDCartLine(100));
			}
			else if("BCONLY".equalsIgnoreCase(orderType))
			{
				cart.addOrderLine(createBCCartLine(101));				
			}
			else
			{
				cart.addOrderLine(createFDCartLine(100));
				cart.addOrderLine(createBCCartLine(101));	
			}
		}				
		ErpAddressModel address=new ErpAddressModel();
        address.setAddress1("2330 borden ave");
        address.setCity("Long island city");
        address.setCompanyName("FreshDirect Inc");
        address.setZipCode("11101");
        address.setPK(new PrimaryKey("PK1"));
		address.setState("NY");
		cart.setDeliveryAddress(address);					
		
		Collection ccards = FDCustomerManager.getPaymentMethods(identity);
		cart.setPaymentMethod((ErpPaymentMethodI) ((ccards.toArray())[0]));
		
		Calendar todayCal = new GregorianCalendar();							     															
		Calendar begCal = new GregorianCalendar();		
		begCal.set(Calendar.MONTH, Calendar.JANUARY);
		begCal.set(Calendar.DAY_OF_MONTH,todayCal.get(Calendar.DAY_OF_MONTH) );
		begCal.set(Calendar.HOUR_OF_DAY, 0);
		begCal.set(Calendar.MINUTE, 1);

		Calendar endCal = new GregorianCalendar();
		endCal.set(Calendar.MONTH, Calendar.JANUARY);
		endCal.set(Calendar.DAY_OF_MONTH, todayCal.get(Calendar.DAY_OF_MONTH)+2);
		endCal.set(Calendar.HOUR_OF_DAY, 0);
		endCal.set(Calendar.MINUTE, 1);

		FDDeliveryManager.getInstance().scrubAddress(address);

		System.out.println("-------> find timeslots from " + begCal.getTime() + " to " + endCal.getTime());

		List timeSlots =
			FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(begCal.getTime(), endCal.getTime(), address);

		FDTimeslot slot = null;

		int c = 0;
		while ((timeSlots.size() > 0) && (slot == null) && (c < 15)) {
			FDTimeslot fdts = (FDTimeslot) timeSlots.get((int) (Math.random() * timeSlots.size()));
			if (fdts.getCapacity() > 0) {
				slot = fdts;
				System.out.println("timeslot id is : " + slot.getTimeslotId());
				System.out.println("timeslot is : " + slot.getBegDateTime());
			} else {
				c++;
			}
		}

		if (slot == null) {
			System.out.println("No timeslots available...");
			return null;
		}

		DlvZoneInfoModel zInfo = FDDeliveryManager.getInstance().getZoneInfo(address, new java.util.Date());
		System.out.println("zone id is : " + zInfo.getZoneId());

		FDReservation reservation = FDDeliveryManager.getInstance().reserveTimeslot(slot, identity.getErpCustomerPK(), 1000, EnumReservationType.STANDARD_RESERVATION, address.getPK().getId());
		cart.setDeliveryReservation(reservation);

		//cart.setDeliveryChargeWaived(true);
		cart.setZoneInfo(zInfo);			

	} catch (FDResourceException fdre) {
		fdre.printStackTrace();
		return null;
	} catch (ReservationException re) {
		re.printStackTrace();
		return null;
	} catch (FDInvalidAddressException fdiae) {
		fdiae.printStackTrace();
		return null;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		throw e;
	}
	
		return cart;
  }
	
	
	public static FDCartLineI createFDCartLine(int idx) throws FDResourceException, FDInvalidConfigurationException
	{
		ErpOrderLineModel  ol = new ErpOrderLineModel();
		FDSku sku=new FDSku("TESTFDSKU"+idx,idx);
		//FDSku sku=new FDSku("FRU0059902",29877);		
        ol.setAffiliate(ErpAffiliate.getEnum("FD"));
        ol.setAlcohol(false);
        ol.setConfiguration(createConfiguration(40));
        ol.setConfigurationDesc("Apple to eat");
        ol.setDepartmentDesc("Grocerry");
        ol.setDepositValue(0);
        ol.setDescription("Apple to Eat");
        ol.setSku(sku);                
        ol.setMaterialNumber("000000000200200928");
        ol.setOrderLineNumber(PosexUtil.getPosex(idx++));
        ol.setPerishable(true);
        ol.setPrice(35.6);
        ol.setTaxRate(0.08375);          
        FDCartLineModel model=new FDCartLineModel(ol);        
        MaterialPrice price=new MaterialPrice(35.6,"EA");
        
        FDConfiguredPrice fdPrice=new FDConfiguredPrice(20,0,price);
        model.price=fdPrice;        
        return model;     
   
	}


	public static FDCartLineI createBCCartLine(int idx) throws FDResourceException, FDInvalidConfigurationException
	{
		ErpOrderLineModel  ol = new ErpOrderLineModel();
		FDSku sku=new FDSku("TESTBCSKU"+idx,idx);
        ol.setAffiliate(ErpAffiliate.getEnum("BC"));
        ol.setAlcohol(false);
        ol.setConfiguration(createConfiguration(4));
        ol.setConfigurationDesc("Apple to eat");
        ol.setDepartmentDesc("Grocerry");
        ol.setDepositValue(0);
        ol.setDescription("Apple to Eat");
        ol.setSku(sku);        
        ol.setMaterialNumber("000000000500000434");
        ol.setOrderLineNumber(PosexUtil.getPosex(idx));
        ol.setPerishable(false);
        ol.setPrice(46);
        ol.setTaxRate(0.08375);              
        FDCartLineModel model=new FDCartLineModel(ol);        
        MaterialPrice price=new MaterialPrice(11.5,"EA");
        
        FDConfiguredPrice fdPrice=new FDConfiguredPrice(20,0,price);
        model.price=fdPrice;        
        return model;     
   
	}
	
	public static FDConfiguration createConfiguration(int num)
	{
		return new FDConfiguration(num,"EA");
	}
	
	public static ErpAppliedCreditModel createAppliedCreaditModel()
	{
		ErpAppliedCreditModel model=new ErpAppliedCreditModel();
		model.setAffiliate(ErpAffiliate.getEnum(ErpAffiliate.CODE_BC));
		model.setAmount(34.47);
		model.setCustomerCreditPk(new PrimaryKey("PK1"));
		model.setOriginalAmount(30.0);
		model.setPK(new PrimaryKey("PK1"));
		return model;
	}
	
	 public static ErpChargeLineModel createChargeLineModel() throws RemoteException, CreateException, FDAuthenticationException, FDResourceException
	 {
		 
		 ErpChargeLineModel model=new ErpChargeLineModel(EnumChargeType.DELIVERY,"some reason",4.95,null,0);
		 return model;
        
	 }
	
	
	 public static Discount createDiscount() throws RemoteException, CreateException, FDAuthenticationException, FDResourceException
	 {		 
            Discount discount=new Discount("TEST_PROMO",EnumDiscountType.DOLLAR_OFF,10);
            return discount;
        
	 }	
	
	
}
