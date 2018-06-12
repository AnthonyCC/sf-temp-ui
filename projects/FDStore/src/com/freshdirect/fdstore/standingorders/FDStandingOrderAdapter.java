package com.freshdirect.fdstore.standingorders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.CatalogKey;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpReturnLineModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.customer.FDBulkRecipientList;
import com.freshdirect.fdstore.customer.FDBulkRecipientModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCartonInfo;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDOrderTranslator;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.WebOrderViewFactory;
import com.freshdirect.fdstore.customer.WebOrderViewI;
import com.freshdirect.fdstore.customer.adapter.DiscountLineModelAdaptor;
import com.freshdirect.fdstore.customer.adapter.FDInvoiceAdapter;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.customer.util.FDCartUtil;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.rules.FDRuleContextI;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.fdstore.services.tax.TaxFactory;
import com.freshdirect.fdstore.services.tax.TaxFactoryImpl;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardDlvConfirmModel;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.payment.EnumPaymentMethodType;

public class FDStandingOrderAdapter  implements FDOrderI{

	private static final long serialVersionUID = -7702575123652018830L;

	private static Category LOGGER = LoggerFactory.getInstance(FDOrderAdapter.class);

	private ErpAbstractOrderModel erpOrder;
	private FDInvoiceAdapter firstInvoice;
	private FDInvoiceAdapter lastInvoice;
	private ErpReturnOrderModel returnOrder;
	private ErpRedeliveryModel redeliveryModel;
	private List<FDCartLineI> orderLines;
	private List<FDCartLineI> sampleLines;
	private FDReservation deliveryReservation;
	protected List<FDCartonInfo> cartonInfo;
	protected Map<String, Integer> cartonMetrics;

	private EnumNotificationType taxationtype;
	
	FDStandingOrder so ;
	
	
	public FDStandingOrderAdapter() {
		orderLines = new ArrayList<FDCartLineI>();
	}

	public FDStandingOrderAdapter(FDCartModel cart,FDStandingOrder so) {
		init(cart, so);
	}
	
	

	/** Adapt a sale
	 * @param saleModel Sale object
	 * @param lazy if true, initialize cartline items from orderlines as much as possible
	 * avoiding using product models. Retained for FDX CRM. 
	 */
	protected void init(FDCartModel cart,FDStandingOrder so) {
		
		/* Frequesncy  so.fullFrequencyDescription
		 
		 Date    so.deliveryDay
		 
		 deliveryTime   so.deliveryTime */
		 
		try {
		ErpCreateOrderModel orderModel=FDOrderTranslator.getErpCreateOrderModel(cart);
		erpOrder = orderModel;
		erpOrder.seteStoreId(orderModel.geteStoreId());
        this.so=so;
		

						EnumPaymentType origPaymentType = erpOrder.getPaymentMethod().getPaymentType();
						String origMakeGoodRefOrder = erpOrder.getPaymentMethod().getReferencedOrder();
						String origBillingRef = erpOrder.getPaymentMethod().getBillingRef();
						//TODO Need to verify whether payment is being returned from cart 
						//erpOrder.setPaymentMethod(custPayment);
						
						erpOrder.getPaymentMethod().setPaymentType(origPaymentType);
						erpOrder.getPaymentMethod().setReferencedOrder(origMakeGoodRefOrder);
						erpOrder.getPaymentMethod().setBillingRef(origBillingRef);

		} catch (FDResourceException e1) {
			LOGGER.info("payment method not found"+e1);
		}		
		  
		
		orderLines = new ArrayList<FDCartLineI>();
		sampleLines = new ArrayList<FDCartLineI>();
		ErpDeliveryPlantInfoModel dpi=erpOrder.getDeliveryInfo().getDeliveryPlantInfo();

		List<ErpOrderLineModel> erpLines = erpOrder.getOrderLines();
		for (int i = 0; i < erpLines.size(); i++) {
			ErpOrderLineModel ol = erpLines.get(i);
			if(null == ol.getPlantID()){
				ol.setPlantID(dpi.getPlantId());
			}
			
			String olNum = ol.getOrderLineNumber();
			ErpInvoiceLineI firstInvoiceLine = getFirstInvoiceLine(olNum);
			ErpInvoiceLineI lastInvoiceLine = getLastInvoiceLine(olNum);
			ErpReturnLineModel returnLine = getReturnLine(olNum);


			FDCartLineI cartLine;
			cartLine = new FDCartLineModel(ol, firstInvoiceLine, lastInvoiceLine, returnLine, false);
			cartLine.setCouponDiscount(ol.getCouponDiscount());
			cartLine.setEStoreId(erpOrder.geteStoreId());
			cartLine.getUserContext().setStoreContext(StoreContext.createStoreContext(erpOrder.geteStoreId()));
			//if(dpi!=null && !StringUtil.isEmpty(dpi.getSalesOrg()) && !StringUtil.isEmpty(ol.getSalesOrg())  && !ol.getSalesOrg().equals(dpi.getSalesOrg())) {
			if(dpi!=null && !StringUtil.isEmpty(dpi.getSalesOrg()) && !StringUtil.isEmpty(ol.getSalesOrg())) {				
				CatalogKey catalogKey = dpi.getCatalogKey();
				if(null !=catalogKey && null != catalogKey.getPricingZone()){
					cartLine.getUserContext().setPricingContext(new PricingContext(catalogKey.getPricingZone()));
				}else{
					//--Hack for APPDEV-4726 FoodKick pricing errors in order receipt and order details
					if(!ol.getSalesOrg().equals(dpi.getSalesOrg())) {
										
						ZoneInfo z=new ZoneInfo(ol.getPricingZoneId(), dpi.getSalesOrg(), dpi.getDistChannel(), ZoneInfo.PricingIndicator.BASE, new ZoneInfo(ol.getPricingZoneId(), ol.getSalesOrg(), ol.getDistChannel()));
						cartLine.getUserContext().setPricingContext(new PricingContext(z));
					} else if (!"0001".equals(ol.getSalesOrg())){//for group scale
						ZoneInfo z=new ZoneInfo(ol.getPricingZoneId(), dpi.getSalesOrg(), dpi.getDistChannel(), ZoneInfo.PricingIndicator.BASE, new ZoneInfo(ol.getPricingZoneId(), "0001", "01"));
						cartLine.getUserContext().setPricingContext(new PricingContext(z));
					}
				}
			}
			
			//If gift card sku load the fixed frice into cartline.
			if(FDStoreProperties.getGiftcardSkucode().equalsIgnoreCase(ol.getSku().getSkuCode()) || FDStoreProperties.getRobinHoodSkucode().equalsIgnoreCase(ol.getSku().getSkuCode())){
				cartLine.setFixedPrice(ol.getPrice());
			}

			
			
			try {
				if(cartLine.lookupFDProduct()!=null){
					cartLine.refreshConfiguration();					
				}

			} catch (FDInvalidConfigurationException e) {
				LOGGER.warn("Difficulty recreating orderline " + i , e);

				// salvage original descriptions
				cartLine.setDepartmentDesc(ol.getDepartmentDesc());
				cartLine.setDescription(ol.getDescription());
				cartLine.setConfigurationDesc(ol.getConfigurationDesc());				
			} catch (FDResourceException e) {
				throw new FDRuntimeException(e, "Difficulty recreating orderline " + i);
			}
			
				
			if (cartLine.isSample()) {
				sampleLines.add(cartLine);
			} else {
				orderLines.add(cartLine);
			}

		}
		Collections.sort(orderLines, FDCartModel.LINE_COMPARATOR);

		// Now set up carton content info
		cartonInfo = new ArrayList<FDCartonInfo>(); 

		
		//set the carton metrics
	}

	



	private ErpInvoiceLineI getFirstInvoiceLine(String orderLineNumber) {
		return firstInvoice == null ? null : firstInvoice.getInvoiceLine(orderLineNumber);
	}

	private ErpInvoiceLineI getLastInvoiceLine(String orderLineNumber) {
		return lastInvoice == null ? null : lastInvoice.getInvoiceLine(orderLineNumber);
	}
	
	public ErpInvoiceLineI getInvoiceLine(String orderLineNumber) {
		return getLastInvoiceLine(orderLineNumber);
	}

	private ErpReturnLineModel getReturnLine(String orderLineNumber) {
		if (hasReturn()) {
			// FIXME types are mixed up a little here! ( ErpInvoiceLineModel <-> ErpReturnLineModel )
			// getInvoiceLines() should return ErpInvoiceLineModels-s, 
			// why are there ErpReturnLineModel-s in the list??
			// code is not type-safe this way!
			List returnLines = returnOrder.getInvoiceLines();
			for (int i = 0, size = returnLines.size(); i < size; i++) {
				ErpReturnLineModel returnLine = (ErpReturnLineModel) returnLines.get(i);
				if (orderLineNumber.equalsIgnoreCase(returnLine.getLineNumber())) {
					return returnLine;
				}
			}
		}
		return null;
	}




	@Override
    public Date getPricingDate() {
		return erpOrder.getPricingDate();
	}


	public static final int IC_FREE2GROUP = -1;
	public static final int IC_GROUP_BY_DEPTS = 1;
	public static final int IC_GROUP_BY_CARTONS = 2;

	
	@Override
    public Collection<ErpChargeLineModel> getCharges() {
		return Collections.unmodifiableCollection(erpOrder.getCharges());
	}




	/**
	 * Retrieves the list of applied credits recorded for a given order.
	 * @return read-only Collection of ErpAppliedCreditModel objects.
	 */
	@Override
    public Collection<ErpAppliedCreditModel> getAppliedCredits() {
		return erpOrder.getAppliedCredits();
	}

	@Override
    public double getCustomerCreditsValue() {
		Collection<ErpAppliedCreditModel> appliedCredits = getAppliedCredits();
		double creditValue = 0;

		for ( ErpAppliedCreditModel ac : appliedCredits ) {
			creditValue += ac.getAmount();
		}
		return creditValue;
	}

	@Override
    public Collection<ErpAppliedCreditModel> getActualAppliedCredits() {
		return hasInvoice() ? lastInvoice.getAppliedCredits() : erpOrder.getAppliedCredits();
	}

	@Override
    public double getActualCustomerCreditsValue() {
		return lastInvoice.getCustomerCreditsValue();
	}

	@Override
    public ErpAddressModel getDeliveryAddress() {
		return erpOrder.getDeliveryInfo().getDeliveryAddress();
	}

	/**
	 * !!! ugly impedance mismatch: depot/pickup addresses are not mapped properly on backend...
	 * some properties (eg. alt dlv on depot) would still not be mapped back. 
	 */
	public ErpAddressModel getCorrectedDeliveryAddress() {
		ErpDeliveryInfoModel dlvInfo = erpOrder.getDeliveryInfo();
		ErpAddressModel origAddress = dlvInfo.getDeliveryAddress();
		/*
		 * The following block was added to fix COS Promo Bug.
		 * The modified order delivery info object contains the attribute
		 * EnumDeliveryType which defines the service type used fo that
		 * order. But the view cart page itself uses AddressModel.serviceType
		 * which in this case would be NULL, to decide if it was a HOME or 
		 * CORPORATE order. So the following black sets the AddressModel.serviceType
		 * based on the DeliveryInfo.deliveryType so that the system knows
		 * the serviceType way in advance before you go to delivery address page
		 * and come back.
		 *  
		 */
		//BEGIN FIX
		EnumDeliveryType dlvType = dlvInfo.getDeliveryType();
		if(EnumDeliveryType.HOME.equals(dlvType)){
			origAddress.setServiceType(EnumServiceType.HOME);
		} else if (EnumDeliveryType.CORPORATE.equals(dlvType)){
			origAddress.setServiceType(EnumServiceType.CORPORATE);
		} else if (EnumDeliveryType.PICKUP.equals(dlvType)){
			origAddress.setServiceType(EnumServiceType.PICKUP);
		} else if (EnumDeliveryType.DEPOT.equals(dlvType)){
			origAddress.setServiceType(EnumServiceType.DEPOT);
		}
		//END FIX.
		
		if (EnumDeliveryType.DEPOT.equals(dlvType) || EnumDeliveryType.PICKUP.equals(dlvType)) {
			ErpDepotAddressModel depotAddress = new ErpDepotAddressModel(origAddress);
			depotAddress.setFacility(getDepotFacility());
			depotAddress.setPickup(EnumDeliveryType.PICKUP.equals(dlvType));
			depotAddress.setLocationId(dlvInfo.getDepotLocationId());
			depotAddress.setInstructions(origAddress.getInstructions());
			depotAddress.setZoneCode(dlvInfo.getDeliveryZone()); 
			return depotAddress;
		}
		return origAddress;
	}

	@Override
    public EnumDeliveryType getDeliveryType() {
		return erpOrder.getDeliveryInfo().getDeliveryType();
	}

	@Override
    public ErpDeliveryInfoModel getDeliveryInfo() {
		return erpOrder.getDeliveryInfo();
	}

	@Override
    public String getDepotFacility() {
		String depotLocationId = erpOrder.getDeliveryInfo().getDepotLocationId();
		if (depotLocationId == null || "".equals(depotLocationId)) {
			return "";
		}
		try {
			return FDDeliveryManager.getInstance().getDepotFacility(depotLocationId);
		} catch (FDResourceException fdre) {
			LOGGER.error("Unable to look up a depot location", fdre);
		}
		return "";
	}



	@Override
    public ErpPaymentMethodI getPaymentMethod() {
		return erpOrder.getPaymentMethod();
	}

	public String getDeliveryReservationId() {
		return erpOrder.getDeliveryInfo().getDeliveryReservationId();
	}

	@Override
    public Date getRequestedDate() {
		return erpOrder.getRequestedDate();
	}



	@Override
    public List<FDCartLineI> getOrderLines() {
		return orderLines;
	}



	@Override
    public String getDiscountDescription() {
		String desc = "";
		List<ErpDiscountLineModel> discounts = erpOrder.getDiscounts();
		if ( discounts != null && discounts.size() > 0) {
			for ( ErpDiscountLineModel model : discounts ) {
				Discount discount =  model.getDiscount();
				String code = discount.getPromotionCode();
				PromotionI promotion = PromotionFactory.getInstance().getPromotion(code);
				if (promotion != null) {
					if (EnumPromotionType.SIGNUP.equals(promotion.getPromotionType())) {
						desc = "FREE FOOD";						
					} else {
						desc = promotion.getDescription();
					}
				}
			}
		}
		return desc;
	}

	@Override
    public String getRedeemedSampleDescription() {
		String desc = "NONE";
		//Show any redeemed sample line if any.
		if ( sampleLines != null && sampleLines.size() > 0) {
			for ( FDCartLineI cartLine : sampleLines ) {
				Discount discount =  cartLine.getDiscount();
				String code = discount.getPromotionCode();
				PromotionI promotion = PromotionFactory.getInstance().getPromotion(code);
				if (promotion != null && promotion.isRedemption()) {
					desc = promotion.getDescription();
				}
			}
		}
		return desc;
	}
	
	public String getRedeemedExtendDPDescription() {
		String desc = "NONE";
		
		//Show any redeemed sample line if any.
		if ( sampleLines != null && sampleLines.size() > 0) {
			for ( FDCartLineI cartLine : sampleLines ) {
				Discount discount =  cartLine.getDiscount();
				String code = discount.getPromotionCode();
				PromotionI promotion = PromotionFactory.getInstance().getPromotion(code);
				if (promotion != null && promotion.isRedemption()) {
					desc = promotion.getDescription();
				}
			}
		}
		return desc;
	}
	@Override
    public String getDeliveryZone() {
		return erpOrder.getDeliveryInfo().getDeliveryZone();
	}

	@Override
    public double getDeliverySurcharge() {
		return this.getChargeAmount(EnumChargeType.DELIVERY)+this.getChargeAmount(EnumChargeType.DLVPREMIUM);
	}
	
	@Override
    public double getDeliveryPremium() {
		ErpChargeLineModel charge = erpOrder.getCharge(EnumChargeType.DLVPREMIUM);
		return charge == null ? 0.0 : charge.getAmount();
	}
	
	@Override
    public double getInvoicedDeliveryCharge(){
		return lastInvoice.getInvoicedDeliveryCharge();
	}
	
	@Override
    public double getInvoicedDeliverySurcharge(){
		return lastInvoice.getInvoicedDeliverySurcharge();
	}
	@Override
    public double getInvoicedDeliveryPremium(){
		return lastInvoice.getInvoicedDeliveryPremium();
	}
	

	@Override
    public boolean isDeliveryChargeWaived() {
		return this.isChargeWaived(EnumChargeType.DELIVERY) && (erpOrder.getCharge(EnumChargeType.DLVPREMIUM)==null || this.isChargeWaived(EnumChargeType.DLVPREMIUM));
	}
	
	@Override
    public boolean isDeliverySurChargeWaived() {
		return isChargeWaived(EnumChargeType.MISCELLANEOUS);
	}
	
	@Override
    public boolean isDeliveryChargeTaxable() {
		return this.isChargeTaxable(EnumChargeType.DELIVERY) || this.isChargeTaxable(EnumChargeType.DLVPREMIUM);
	}

	@Override
    public boolean isChargeWaived(EnumChargeType chargeType) {
		ErpChargeLineModel charge = erpOrder.getCharge(chargeType);
		return charge == null ? false : charge.getDiscount() != null;
	}

	@Override
    public boolean isChargeTaxable(EnumChargeType chargeType) {
		ErpChargeLineModel charge = erpOrder.getCharge(chargeType);
		return charge == null ? false : charge.getTaxRate() > 0;
	}
	
	@Override
    public boolean isEstimatedPrice() {

		return true;
	}

	@Override
    public double getTotal() {
		return erpOrder.getAmount();
	}

	@Override
    public double getSubTotal() {
		return FDCartUtil.getSubTotal(orderLines);
	}

	/**
	 * @return the amount you have saved by item promotion value and coupon
	 */
	@Override
	public double getSaveAmount(boolean includeDiscountSavings) {
		return FDCartUtil.getSaveAmount(orderLines) + (includeDiscountSavings? getTotalDiscountValue() : 0);
	}
	
	@Override
    public double getTaxValue() {
		return erpOrder.getTax();
	}

	@Override
    public double getDepositValue() {
		return erpOrder.getDepositValue();
	}

	@Override
    public int numberOfOrderLines() {
		return orderLines.size();
	}

	@Override
    public FDCartLineI getOrderLine(int idx) {
		return orderLines.get(idx);
	}
	
	public ErpOrderLineModel getOrderLine(String orderlineId) {
		return erpOrder.getOrderLineByPK(orderlineId);
	}

	@Override
    public List<FDCartLineI> getSampleLines() {
		return sampleLines;
	}

	@Override
    public String getCustomerServiceMessage() {
		return erpOrder.getCustomerServiceMessage();
	}

	@Override
    public String getMarketingMessage() {
		return erpOrder.getMarketingMessage();
	}

	@Override
    public String getDeliveryInstructions() {
		return erpOrder.getDeliveryInfo().getDeliveryAddress().getInstructions();
	}

	@Override
    public double getChargeAmount(EnumChargeType type) {
		ErpChargeLineModel charge = hasInvoice() ? lastInvoice.getCharge(type) : erpOrder.getCharge(type);
		return charge == null ? 0.0 : charge.getAmount();
	}

	private double getChargeAmountOnReturn(EnumChargeType type) {
		ErpChargeLineModel charge = returnOrder.getCharge(type);
		return charge == null ? 0.0 : charge.getAmount();
	}
	
	@Override
    public double getPhoneCharge() {
		return getChargeAmount(EnumChargeType.PHONE);
	}

	@Override
    public double getMiscellaneousCharge() {
		return getChargeAmount(EnumChargeType.MISCELLANEOUS);
	}

	@Override
    public boolean isMiscellaneousChargeWaived() {
		return isChargeWaived(EnumChargeType.MISCELLANEOUS);
	}
	
	@Override
    public boolean isMiscellaneousChargeTaxable() {
		return isChargeTaxable(EnumChargeType.MISCELLANEOUS);
	}

	@Override
    public double getRestockingCharges() {
		double charge = 0;
		charge += getChargeAmount(EnumChargeType.FD_RESTOCKING_FEE);
		charge += getChargeAmount(EnumChargeType.WBL_RESTOCKING_FEE);
		charge += getChargeAmount(EnumChargeType.BC_RESTOCKING_FEE);
		charge += getChargeAmount(EnumChargeType.USQ_RESTOCKING_FEE);
		charge += getChargeAmount(EnumChargeType.FDW_RESTOCKING_FEE);
		return charge;
	}

	@Override
    public double getRestockingCharges(ErpAffiliate affiliate) {

		double charge = 0.0;
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_FD).equals(affiliate)) {
			charge = getChargeAmount(EnumChargeType.FD_RESTOCKING_FEE);
		}
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_WBL).equals(affiliate)) {
			charge = getChargeAmount(EnumChargeType.WBL_RESTOCKING_FEE);
		}
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_BC).equals(affiliate)) {
			charge = getChargeAmount(EnumChargeType.BC_RESTOCKING_FEE);
		}
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ).equals(affiliate)) {
			charge = getChargeAmount(EnumChargeType.USQ_RESTOCKING_FEE);
		}
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_FDW).equals(affiliate)) {
			charge = getChargeAmount(EnumChargeType.FDW_RESTOCKING_FEE);
		}
		return charge;
	}

	@Override
    public double getFDRestockingCharges() {
		return getChargeAmount(EnumChargeType.FD_RESTOCKING_FEE);
	}

	@Override
    public double getWBLRestockingCharges() {
		return getChargeAmount(EnumChargeType.WBL_RESTOCKING_FEE);
	}

	@Override
    public double getCCDeclinedCharge() {
		return getChargeAmount(EnumChargeType.CC_DECLINED);
	}





	@Override
    public boolean containsAlcohol() {
		for ( FDCartLineI line : orderLines ) {
			if (line.isAlcohol()) {
				return true;
			}
		}
		return false;
	}

	@Override
    public boolean hasInvoice() {
		return firstInvoice != null;
	}

	@Override
    public boolean hasReturn() {
		return returnOrder != null;
	}

	@Override
    public boolean hasSettledReturn() {
		return returnOrder != null && firstInvoice != lastInvoice;
	}

	public double getReturnSubTotal() {
		return returnOrder.getSubTotal();
	}

	public double getReturnTotal() {
		return returnOrder.getAmount();
	}

	public List<ErpChargeLineModel> getReturnCharges() {
		return returnOrder.getCharges();
	}

	@Override
    public boolean hasRedelivery() {
		return redeliveryModel != null;
	}

	@Override
    public Date getRedeliveryStartTime() {
		return redeliveryModel.getDeliveryInfo().getDeliveryStartTime();
	}

	@Override
    public Date getRedeliveryEndTime() {
		return redeliveryModel.getDeliveryInfo().getDeliveryEndTime();
	}


	@Override
    public double getInvoicedTotal() {
		return lastInvoice.getInvoicedTotal();
	}

	@Override
    public double getInvoicedSubTotal() {
		return lastInvoice.getInvoicedSubTotal();
	}

	@Override
    public double getInvoicedTaxValue() {
		return lastInvoice.getInvoicedTaxValue();
	}

	@Override
    public double getInvoicedDepositValue() {
		return lastInvoice.getInvoicedDepositValue();
	}

	@Override
    public List<ErpChargeLineModel> getInvoicedCharges() {
		return lastInvoice.getInvoicedCharges();
	}

	@Override
    public double getActualDiscountValue() {
		return lastInvoice.getActualDiscountValue();
	}

	@Override
    public boolean isChargedWaivedForReturn(EnumChargeType type) {
		if (returnOrder == null) {
			return false;
		}
		List<ErpChargeLineModel> waivedCharges = returnOrder.getCharges();
		if (waivedCharges == null) {
			return false;
		}
		boolean isWaived = false;
		for ( ErpChargeLineModel charge : waivedCharges ) {
			if (charge.getType().equals(type) && charge.getDiscount() != null) {
				if(type == EnumChargeType.DELIVERY || type == EnumChargeType.MISCELLANEOUS){
					//if delivery charge or msc charges
					String promoCode = charge.getDiscount().getPromotionCode();
					//Not waived due to a Delivery Pass .
					isWaived = !(promoCode != null && promoCode.equals(DlvPassConstants.PROMO_CODE));
				} else {
					//any other charges
					isWaived = true;
				}
				break;
			}
		}
		return isWaived;
	}

	@Override
    public boolean isChargeWaivedByCSROnReturn(EnumChargeType type) {
		if (returnOrder == null) {
			return false;
		}
		List<ErpChargeLineModel> waivedCharges = returnOrder.getCharges();
		if (waivedCharges == null) {
			return false;
		}
		boolean isCsrWaived = false;
		for ( ErpChargeLineModel charge : waivedCharges ) {
			if (charge.getType().equals(type) && charge.getDiscount() != null) {
				if(type == EnumChargeType.DELIVERY || type == EnumChargeType.MISCELLANEOUS){
					//if delivery charge or msc charges
					String promoCode = charge.getDiscount().getPromotionCode();
					isCsrWaived = (promoCode != null && promoCode.equals("DELIVERY") ? true : false);
				} else {
					//any other charges
					isCsrWaived = true;
				}
				break;
			}
		}
		return isCsrWaived;
	}

	@Override
    public WebOrderViewI getOrderView(ErpAffiliate affiliate) {
		return WebOrderViewFactory.getOrderView(orderLines, affiliate, false);
	}

	@Override
    public List<WebOrderViewI> getOrderViews() {
		return WebOrderViewFactory.getOrderViews(orderLines, false);
	}
	
	@Override
    public WebOrderViewI getInvoicedOrderView(ErpAffiliate affiliate) {
		return WebOrderViewFactory.getInvoicedOrderView(orderLines, getSampleLines(), affiliate);
	}

	@Override
    public List<WebOrderViewI> getInvoicedOrderViews() {
		return WebOrderViewFactory.getInvoicedOrderViews(orderLines, getSampleLines());
	}

	@Override
    public FDReservation getDeliveryReservation() {
		return deliveryReservation;
	}

	public String getBillingRef() {
		return getPaymentMethod().getBillingRef();
	}

	@Override
    public List<FDCartLineI> getShortedItems() {
		List<FDCartLineI> shortedItems = new ArrayList<FDCartLineI>();

		for ( FDCartLineI line : orderLines ) {
			if (line.getDeliveredQuantity() != null && !"".equals(line.getDeliveredQuantity())) {				
				if (line.isPricedByLb()) {				
					if (new Double(line.getDeliveredQuantity()).doubleValue() == 0) {
						shortedItems.add(line);
					}				
				} else if (new Double(line.getDeliveredQuantity()).doubleValue() < line.getQuantity()) {					
					shortedItems.add(line);					
				}				
			}
		}
		return shortedItems;
	}



	@Override
    public boolean isPhoneChargeWaived() {
		return isChargeWaived(EnumChargeType.PHONE);
	}

	@Override
    public boolean isPhoneChargeTaxable() {
		return isChargeTaxable(EnumChargeType.PHONE);
	}



	public List<FDCartonInfo> getCartonContents(String orderlineNumber) {
		List<FDCartonInfo> retList = new ArrayList<FDCartonInfo>();
		for ( FDCartonInfo cInf : cartonInfo ) {
			if(cInf.containsCartonInfo(orderlineNumber) != null) {
				retList.add(cInf);
			}
		}
		return retList;
	}
	
	@Override
    public Map<String, Integer> getCartonMetrics() {
		return cartonMetrics;
	}


	

	@Override
    public List<ErpDiscountLineModel> getDiscounts() { 
		return erpOrder.getDiscounts(); 
	}

	public List<DiscountLineModelAdaptor> getHeaderDiscounts() {
		if ( erpOrder.getDiscounts() != null && erpOrder.getDiscounts().size() > 0 ) {
			List<DiscountLineModelAdaptor> result = new ArrayList<DiscountLineModelAdaptor>();
			for ( ErpDiscountLineModel discountLine : erpOrder.getDiscounts() ) {
				result.add( new DiscountLineModelAdaptor( discountLine ) );
			}
			return result;
		}
		return null;
	}
	
	@Override
    public double getTotalDiscountValue() {
		double totalDiscountAmount = 0.0;
		if ( erpOrder.getDiscounts() != null && erpOrder.getDiscounts().size() > 0 ) {
			for ( ErpDiscountLineModel discountLine : erpOrder.getDiscounts() ) {
				totalDiscountAmount += discountLine.getDiscount().getAmount();
			}
		}
		return MathUtil.roundDecimal(totalDiscountAmount);
	}

	@Override
    public List<ErpDiscountLineModel> getActualDiscounts() { 
		return lastInvoice.getActualDiscounts(); 
	}
	

	



	
	@Override
    public double getDeliverySurchargeOnReturn() {
		return this.getChargeAmountOnReturn(EnumChargeType.DELIVERY) + this.getChargeAmountOnReturn(EnumChargeType.DLVPREMIUM);
	}
	
	@Override
    public double getDeliveryChargeOnReturn() {
		return this.getChargeAmountDiscAppliedOnReturn(EnumChargeType.DELIVERY) + this.getChargeAmountDiscAppliedOnReturn(EnumChargeType.DLVPREMIUM);
	}

	@Override
    public boolean containsDeliveryPass() {
		boolean deliveryPass = false;
		for ( FDCartLineI line : orderLines ) {
			if (line.lookupFDProduct().isDeliveryPass()) {
				deliveryPass = true;
				break;
			}
		}
		return deliveryPass;		
	}

	/*
	 * new API which gives the total without and discounts and applied credit 
	*/ 		
	@Override
    public double getPreDeductionTotal() {
		double preTotal = 0.0;
		preTotal += MathUtil.roundDecimal( getSubTotal() );
		preTotal += MathUtil.roundDecimal( getTaxValue() );
		preTotal += MathUtil.roundDecimal( getDepositValue() );

		// apply charges
		for ( ErpChargeLineModel m : getCharges() ) {
			preTotal += MathUtil.roundDecimal( m.getTotalAmount() );
		}
		return MathUtil.roundDecimal( preTotal );
	}
	

	
	@Override
    public int getLineItemDiscountCount(String promoCode){
		Set<String> uniqueDiscountedProducts =new HashSet<String>(); 
		for ( FDCartLineI cartLine : orderLines ) {
			if(cartLine.hasDiscount(promoCode)) {
				uniqueDiscountedProducts.add(cartLine.getProductRef().getContentKey().getId());
			}
		}
		return uniqueDiscountedProducts.size();
	}

	@Override
    public double getTotalLineItemsDiscountAmount() {
		double discountAmt=0;
		for ( FDCartLineI cartLine : orderLines ) {
			if(cartLine.getDiscount() !=  null){
				discountAmt+=cartLine.getDiscountAmount();
			}
		}
        return discountAmt;
	}
	
	//Gift cards
	@Override
    public List<ErpGiftCardModel> getGiftcardPaymentMethods() {
		return erpOrder.getSelectedGiftCards();
	}
	

	


	
	@Override
    public FDRecipientList getGiftCardRecipients() {
		return new FDRecipientList(erpOrder.getRecipientsList());
	}


	

	
	public FDBulkRecipientList getGiftCardBulkRecipients(){
		List<ErpRecipentModel> recipientList = erpOrder.getRecipientsList();
		Collections.sort(recipientList, new ErpRecipientModelTemplateComparator());
		FDBulkRecipientModel bulkModel = new FDBulkRecipientModel();
		List<FDBulkRecipientModel> recipients = new ArrayList<FDBulkRecipientModel>();
		
		for ( ErpRecipentModel erpRecipentModel : recipientList ) {
			if ( null == bulkModel.getTemplateId() || 
					( !bulkModel.getTemplateId().equals( erpRecipentModel.getTemplateId() ) ) || 
					( bulkModel.getTemplateId().equals( erpRecipentModel.getTemplateId() ) && bulkModel.getAmount() != erpRecipentModel.getAmount() ) ) {
				bulkModel = new FDBulkRecipientModel();
				bulkModel.setQuantity( "1" );// Initial qty.
				bulkModel.setAmount( erpRecipentModel.getAmount() );
				bulkModel.setTemplateId( erpRecipentModel.getTemplateId() );
				recipients.add( bulkModel );
			} else {
				Integer qty = Integer.parseInt( bulkModel.getQuantity() );
				bulkModel.setQuantity( ( ++qty ).toString() ); // Increase the qty by one.
			}
		}
		FDBulkRecipientList bulkList = new FDBulkRecipientList(recipients);	
		return bulkList;
	}
	
	private class ErpRecipientModelTemplateComparator implements Comparator<ErpRecipentModel>{

		@Override
		public int compare(ErpRecipentModel recp1, ErpRecipentModel recp2) {
			if(recp1.getTemplateId().compareToIgnoreCase(recp2.getTemplateId())==0){
				return Double.compare(recp1.getAmount(),recp2.getAmount());
			}else{		
				return recp1.getTemplateId().compareToIgnoreCase(recp2.getTemplateId());			
			}
		}
		
	}

	@Override
    public ErpOrderLineModel getOrderLineByNumber(String orderlineNumber) {
		return erpOrder.getOrderLineByOrderLineNumber(orderlineNumber);
	}
	

	
	@Override
    public double getBufferAmt() {
		return erpOrder.getBufferAmt();
	}
	

	
	@Override
    public boolean isDiscountInCart(String promoCode) {
		for ( FDCartLineI cartLine : orderLines ) {
			if(cartLine.getDiscount() !=  null){
				String discountCode = cartLine.getDiscount().getPromotionCode();
				if(discountCode.equals(promoCode)) return true;
			}
		}
        return false;
	}
	
	@Override
    public double getLineItemDiscountAmount(String promoCode){
		double discountAmt=0;
		for (Iterator<FDCartLineI> i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI cartLine = i.next();
			if(cartLine.getDiscount() !=  null && cartLine.getDiscount().getPromotionCode().equals(promoCode)){
				discountAmt+=cartLine.getDiscountAmount();
			}
		}
        return discountAmt;
	}
	
	@Override
    public double getDiscountValue(String promoCode) {
		double totalDiscountAmount = 0.0;
		if ( erpOrder.getDiscounts() != null && erpOrder.getDiscounts().size() > 0 ) {
			for ( ErpDiscountLineModel discountLine : erpOrder.getDiscounts() ) {
				if(promoCode.equals(discountLine.getDiscount().getPromotionCode())) {
					totalDiscountAmount += discountLine.getDiscount().getAmount();
				}
			}
		}
		return MathUtil.roundDecimal(totalDiscountAmount);
	}
	



	@Override
	public boolean hasClientCodes() {
		for (FDCartLineI cl : orderLines)
			if (!cl.getClientCodes().isEmpty())
				return true;
		return false;
	}
	


	@Override
    public int getLineCnt() {
		return orderLines == null ? 0 : orderLines.size();
	}

	@Override
	public boolean isDlvPassPremiumAllowedTC() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getDeliveryCharge() {
		return this.getChargeAmountDiscountApplied(EnumChargeType.DELIVERY)+this.getChargeAmountDiscountApplied(EnumChargeType.DLVPREMIUM);
	}
	@Override
    public double getChargeAmountDiscountApplied(EnumChargeType chargeType) {
		ErpChargeLineModel charge = erpOrder.getCharge(chargeType);
		return charge == null ? 0.0 : charge.getTotalAmount();
	}
	@Override
    public double getChargeAmountDiscAppliedOnReturn(EnumChargeType chargeType) {
		ErpChargeLineModel charge = returnOrder.getCharge(chargeType);
		return charge == null ? 0.0 : charge.getTotalAmount();
	}

	@Override
	public double getPremiumFee(FDRuleContextI ctx) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
    public Double getEbtPurchaseAmount() {
	
		Double result = null;
		if(getPaymentMethod() != null && EnumPaymentMethodType.EBT.equals(getPaymentMethod().getPaymentMethodType())
				&& hasInvoice()) {
			result = (getInvoicedTotal() <= getTotal()) ? getInvoicedTotal(): getTotal();
		}
		return result;
	}
	
	public boolean hasCouponDiscounts(){
		return erpOrder.hasCouponDiscounts();
	}



	@Override
	public Map<String, FDAvailabilityInfo> getUnavailabilityMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FDCartLineI getOrderLineById(int parseInt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOrderLineIndex(int parseInt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeOrderLine(int cartIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DlvPassAvailabilityInfo> getUnavailablePasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshAll(boolean b) {

		
	}

	@Override
	public double getTip() {
		return this.getChargeAmountDiscountApplied(EnumChargeType.TIP);
}





	@Override
	public ErpDeliveryPlantInfoModel getDeliveryPlantInfo() {
		return erpOrder.getDeliveryInfo().getDeliveryPlantInfo();
	}

	@Override
	public boolean isCustomTip() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCustomTip(boolean isCustomTip) {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public double getAvalaraTaxValue(AvalaraContext avalaraContext) {
		if(FDStoreProperties.getAvalaraTaxEnabled()){
			TaxFactory taxFactory = new TaxFactoryImpl();
			taxFactory.getTax(avalaraContext);
		}		
		return getTaxValue();
	}

	@Override
	public boolean isTipApplied() {
		// TODO Auto-generated method stub
		return erpOrder.getTip()>=0.00?true:false;
	}



	
	@Override
    public EnumNotificationType getTaxationType(){
		return this.taxationtype;
	}
	
	@Override
    public void setTaxationType(EnumNotificationType taxationType){
		this.taxationtype = taxationType;
	}


	

	/**
	 * @return the so
	 */
	public FDStandingOrder getSo() {
		return so;
	}


	/**
	 * @param so the so to set
	 */
	public void setSo(FDStandingOrder so) {
		this.so = so;
	}


	@Override
	public String getExtendDPDiscountDescription() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public EnumEStoreId getEStoreId() {
		// TODO Auto-generated method stub
		return erpOrder.geteStoreId();
	}


	@Override
	public double getTotalAppliedGCAmount() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public double getCCPaymentAmount() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setTip(double tip) {
		
	}


	@Override
	public void setTipApplied(boolean tipApplied) {

		
	}

	@Override
	public String getErpSalesId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnumSaleStatus getOrderStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnumTransactionSource getOrderSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPending() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Date getDatePlaced() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnumTransactionSource getOrderSource(String criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTransactionInitiator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTransactionInitiator(String criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLastModifiedDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isModifiedOrder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCustomerId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hasCreditIssued() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<ErpComplaintModel> getComplaints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getFailedAuthorizations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAuthorizations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRefusedDelivery() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ErpShippingInfo getShippingInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSapOrderId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FDCartonInfo> getCartonContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChargeInvoice() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getChargeInvoiceTotal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDeliveryPassId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDlvPassApplied() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDlvPassAppliedOnReturn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EnumSaleType getOrderType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAppliedGiftCards() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getAppliedAmount(String certificateNum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ErpGiftCardDlvConfirmModel getGCDeliveryInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErpRecipentModel getGCResendInfoFor(String giftCardId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErpGCDlvInformationHolder getGCDlvInformationHolder(String givexNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStandingOrderId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasSignature() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMakeGood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAuthFailDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStandingOrderName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSODeliveryDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIn_modify() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLock_timestamp() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public int modifyOrderCount() {
        // TODO Auto-generated method stub
        return 0;
    }

	@Override
	public boolean containsDlvPassOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChargeWaivedByDlvPass(EnumChargeType chargeType) {
		// TODO Auto-generated method stub
		return false;
	}

}
