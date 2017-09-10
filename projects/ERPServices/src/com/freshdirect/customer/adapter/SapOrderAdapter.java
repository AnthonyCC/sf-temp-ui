/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.address.BasicContactAddressI;
import com.freshdirect.common.pricing.ConfiguredPrice;
import com.freshdirect.common.pricing.CreditMemo;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumTaxationType;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.PricingEngine;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.BillingRegionInfo;
import com.freshdirect.sap.SapChargeLineI;
import com.freshdirect.sap.SapCustomerI;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.SapOrderLineI;

/**
 * Adapts an ErpAbstractOrderModel to SapOrderI.
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapOrderAdapter implements SapOrderI {

	private ErpAbstractOrderModel erpOrder;
	private SapCustomerI customer;
	private SapOrderLineAdapter[] orderLines;
	private String webOrderNumber;
	private CustomerRatingI custRatingAdaptor;

	/**
	 * Creates new SapOrderAdapter
	 */
	public SapOrderAdapter(ErpAbstractOrderModel erpOrder, ErpCustomerModel erpCustomer, CustomerRatingI rating) throws FDResourceException {
		this.erpOrder = erpOrder;
		this.webOrderNumber = null;

		this.custRatingAdaptor = rating;

		boolean phonePrivate = rating == null ? false : rating.isPhonePrivate();

		ErpAddressModel shipTo = this.erpOrder.getDeliveryInfo().getDeliveryAddress();
		ErpPaymentMethodI paymentMethod = this.erpOrder.getPaymentMethod();
		/*if(paymentMethod!=null) {
			BillingCountryInfo bc=BillingCountryInfo.getEnum(paymentMethod.getCountry());
			if(bc!=null) {
				BillingRegionInfo br =bc.getRegion(paymentMethod.getState());
				paymentMethod.setState(br.getCodeExt());
			}
		}*/
				
		if (paymentMethod != null) {
			this.customer = new CustomerAdapter(phonePrivate, erpCustomer, shipTo, paymentMethod, this.getAlternateDeliveryAddress());
		} else {
			this.customer = new CustomerAdapter(phonePrivate, erpCustomer, shipTo, null);
		}

		this.createOrderLines();
	}
	
	/**
	 * private method to create SapOrderLineAdapter from ErpOrderLines
	 */
	private void createOrderLines() throws FDResourceException {
		List erpOrderLines = this.erpOrder.getOrderLines();
		this.orderLines = new SapOrderLineAdapter[erpOrderLines.size()];

		for (int i = 0; i < this.orderLines.length; i++) {
			ErpOrderLineModel erpOrderLine = (ErpOrderLineModel) erpOrderLines.get(i);

			// look up FDProduct
			FDProduct fdProduct;
			try {
				fdProduct = FDCachedFactory.getProduct(erpOrderLine.getSku());
			} catch (FDSkuNotFoundException ex) {
				throw new FDResourceException(ex);
			}
			this.orderLines[i] = new SapOrderLineAdapter(fdProduct, erpOrderLine,erpOrder.getDeliveryInfo().getDeliveryPlantInfo());
		}
	}

	/**
	 * Get the optional order-level promotion to apply.
	 *FIXME impedance mismatch: SAP only supports a single header level promoti
	 *
	 * @return Discount object, null if none
	 */
	public Discount getDiscount() {
		List l = this.erpOrder.getDiscounts();
		if (l == null || l.isEmpty()) {
			return null;
		}
		if (l.size() == 1) {
			return ((ErpDiscountLineModel) l.iterator().next()).getDiscount();
		}
		throw new UnsupportedOperationException("Mapping multiple discountlines to SAP not supported yet");
	}

	public SapChargeLineI[] getChargeLines() {
		List<ErpChargeLineModel> erpCharges = this.erpOrder.getCharges();
		
		List<SapChargeLineI> sapCharges = new ArrayList<SapChargeLineI>();
		for (ErpChargeLineModel charge : erpCharges) {
				sapCharges.add(new SapChargeLineAdapter(charge));
			}
		return (SapChargeLineI[]) sapCharges.toArray(new SapChargeLineI[sapCharges.size()]);
	}

	public String getDeliveryZone() {
		return this.erpOrder.getDeliveryInfo().getDeliveryZone();
	}

	public String getDeliveryRegionId() {
		return this.erpOrder.getDeliveryInfo().getDeliveryRegionId();
	}

	/**
	 * Get the customer associated with this order
	 *
	 * @return Customer object
	 */
	public SapCustomerI getCustomer() {
		return this.customer;
	}

	public String getCompanyName() {
		return NVL.apply(erpOrder.getDeliveryInfo().getDeliveryAddress().getCompanyName(), "");
	}
	
	private BasicContactAddressI getAlternateDeliveryAddress() {
		ErpAddressModel adr = this.erpOrder.getDeliveryInfo().getDeliveryAddress();
		return adr.getAlternateAddress();
	}

	private EnumDeliveryType getDeliveryType() {
		return this.erpOrder.getDeliveryInfo().getDeliveryType();
	}

	public String getDeliveryModel() {
		return this.getDeliveryType().getDeliveryModel();
	}

	public Date getDeliveryStartTime() {
		Date startTime = this.erpOrder.getDeliveryInfo().getDeliveryStartTime();
		return startTime;
	}

	public Date getDeliveryEndTime() {
		Date endTime = this.erpOrder.getDeliveryInfo().getDeliveryEndTime();
		return endTime;
	}

	public Date getCutoffTime() {
		return this.erpOrder.getDeliveryInfo().getDeliveryCutoffTime();
	}

	/**
	 * Get the web order ID for this order.
	 */
	public String getWebOrderNumber() {
		if (this.webOrderNumber == null) {
			return this.erpOrder.getPK().getId();
		} else {
			return this.webOrderNumber;
		}
	}

	public List getDiscounts() {
		return this.erpOrder.getDiscounts();
	}

	public void setWebOrderNumber(String webOrderNumber) {
		this.webOrderNumber = webOrderNumber;
	}

	/**
	 * Get the credit memos to apply for this order.
	 *
	 * @return array of CreditMemo objects
	 */
	public CreditMemo[] getCreditMemos() {
		List<ErpAppliedCreditModel> appliedCredits = erpOrder.getAppliedCredits();
		CreditMemo[] creditMemos = new CreditMemo[appliedCredits.size()];
		CreditMemo memo = null;
		int index = 0;
		
		for ( ErpAppliedCreditModel appliedCredit : appliedCredits ) {
			memo = new CreditMemo(
				appliedCredit.getPK().getId(),
				"",
				"",
				appliedCredit.getDepartment(),
				appliedCredit.getAmount(),
				index + 1,
				appliedCredit.getAffiliate());
			;
			creditMemos[index] = memo;

			index++;
		}
		return creditMemos;
	}

	/**
	 * Get the date the order was requested for.
	 *
	 * @return requested date
	 */
	public Date getRequestedDate() {
		return this.erpOrder.getRequestedDate();
	}

	/**
	 * Get an order line by ordinal number.
	 *
	 * @param ordinal number of order line
	 *
	 * @return SapOrderLineI
	 *
	 * @throws IndexOutOfBoundsException
	 */
	public SapOrderLineI getOrderLine(int num) {
		return this.orderLines[num];
	}

	/**
	 * Get the number of order lines.
	 *
	 * @return number of SapOrderLines
	 */
	public int numberOfOrderLines() {
		return this.orderLines.length;
	}

	public String getMarketingMessage() {
		String marketingMessage = this.erpOrder.getMarketingMessage();
		return marketingMessage == null ? "" : marketingMessage;
	}

	public String getCustomerServiceMessage() {
		String csMessage = this.erpOrder.getCustomerServiceMessage();
		return csMessage == null ? "" : csMessage;
	}
	
	public ErpAddressModel getDeliveryAddress(){
		return this.erpOrder.getDeliveryInfo().getDeliveryAddress(); 
		 
	}

	public String getDeliveryInstructions() {
		ErpAddressModel address = this.erpOrder.getDeliveryInfo().getDeliveryAddress(); 
		String instructions = address.getInstructions();

		// !!! HACK: prepend the welcome msg
		String mktgMsg = this.erpOrder.getMarketingMessage();
		if (mktgMsg != null && mktgMsg.startsWith("Welcome!")) {
			instructions = instructions == null ? "Welcome!" : "Welcome! " + instructions;
		}
		
		// !!! prepend Alt Contact Phone number for an address 
		// requirement for Hamptons orders.
		if(address.getAltContactPhone() != null) {
			instructions = instructions == null ? "ALT # " + address.getAltContactPhone() : "ALT # " + address.getAltContactPhone() + " " + instructions;
		}
		
		if (EnumUnattendedDeliveryFlag.OPT_IN.equals(address.getUnattendedDeliveryFlag())) {
			String unattendedInstructions = address.getUnattendedDeliveryInstructions();
			instructions = NVL.apply(instructions,"") + " UNATTENDED_DELIVERY: " + NVL.apply(unattendedInstructions,"OK");
		} else if (EnumUnattendedDeliveryFlag.OPT_OUT.equals(address.getUnattendedDeliveryFlag())) {
			instructions = NVL.apply(instructions,"") + " DO_NOT_LEAVE_UNATTENDED";
		} 

		return NVL.apply(instructions,"");
	}

	public String getCustomerStarRating() {
		return this.custRatingAdaptor == null || this.custRatingAdaptor.getCustomerStarRating() == 0
			? null
			: ("" + this.custRatingAdaptor.getCustomerStarRating());
	}

	public String getChefsTableMembership() {
		return (this.custRatingAdaptor == null || !this.custRatingAdaptor.isChefsTableMember()) ? null : "001";
	}

	public String getProfileSegment() {
		if (this.custRatingAdaptor == null)
			return null;
		String profileSegment = this.custRatingAdaptor.getMetalCategory();
		if ("0".equals(profileSegment))
			return null;
		return ((profileSegment.length() < 3 ? "000".substring(profileSegment.length()) : "") + profileSegment);
	}
	
	public String getBillingRef() {
		return this.erpOrder.getPaymentMethod().getBillingRef();
	}
	
	public boolean isFreeOrder(){
		EnumPaymentType pt = this.erpOrder.getPaymentMethod().getPaymentType();
		return pt.equals(EnumPaymentType.MAKE_GOOD);
	}
	
	public boolean isRecipeOrder() {
		for (int i = 0; i < this.orderLines.length; i++) {
			if (this.orderLines[i].isRecipeItem()) {
				return true;
			}
		}
		return false;
	}

	public String getGlCode() {
		return this.erpOrder.getGlCode();
	}
	
	private static class SapChargeLineAdapter implements SapChargeLineI {

		private static final long	serialVersionUID	= 9206363545163217301L;
		
		private final ErpChargeLineModel charge;

		public SapChargeLineAdapter(ErpChargeLineModel charge) {
			this.charge = charge;
		}
		
		public EnumChargeType getType(){
			return this.charge.getType();
		}

		public double getAmount() {
			return this.charge.getAmount();
		}

		public String getMaterialNumber() {
			return this.charge.getType().getMaterialNumber();
		}

		public Discount getDiscount() {
			return this.charge.getDiscount();
		}
		
		public double getTaxRate(){
			return this.charge.getTaxRate();
		}
	}

	private static class SapOrderLineAdapter implements SapOrderLineI {

		private static final long	serialVersionUID	= 6640596050684635082L;
		
		private final ErpOrderLineModel orderLine;
		private final FDProduct fdProduct;

		/** List of ErpInventoryModel */
		private List<ErpInventoryModel> inventories = Collections.<ErpInventoryModel>emptyList();
		private ErpDeliveryPlantInfoModel plantInfo;

		/**
		 * constructor
		 * @param plantInfo 
		 */
		public SapOrderLineAdapter(FDProduct fdProduct, ErpOrderLineModel orderLine, ErpDeliveryPlantInfoModel plantInfo) {
			this.orderLine = orderLine;
			this.fdProduct = fdProduct;
			this.plantInfo=plantInfo;
		}

		/**
		 * Get the material pricing condition (PR00) to be used.
		 *
		 * @return pricing condition 
		 */
		public MaterialPrice getPricingCondition() {
			try {
				Pricing pricing = fdProduct.getPricing();
				FDConfigurableI prConf = new FDConfiguration(orderLine.getQuantity(), orderLine.getSalesUnit(), orderLine
					.getOptions());
				ConfiguredPrice confPrice = PricingEngine.getConfiguredPrice(pricing, prConf, orderLine.getUserContext().getPricingContext(), orderLine.getFDGroup(), orderLine.getGroupQuantity(),orderLine.getScaleQuantity());
				return confPrice.getPricingCondition();
			} catch (PricingException e) {
				throw new IllegalStateException("Unable to determine pricing condition");
			}
		}

		public boolean isZeroBasePrice() {
			// components of a composite product
			return false;
			//return !"".equals(NVL.apply(this.orderLine.getComponentGroup(), ""));
		}
		
		public boolean isRecipeItem() {
			return orderLine.getRecipeSourceId() != null;
		}

		/**
		 * Get optional orderline discount.
		 */
		public Discount getDiscount() {
			return this.orderLine.getDiscount();
		}

		/**
		 * Get tax rate as percentage (0.0825 means 8.25%).
		 */
		public double getTaxRate() {
			return this.orderLine.getTaxRate();
		}

		public double getDepositValue() {
			return this.orderLine.getDepositValue();
		}

		public ErpAffiliate getAffiliate() {
			return this.orderLine.getAffiliate();
		}

		/**
		 * Get the SAP material number.
		 */
		public String getMaterialNumber() {
			return this.fdProduct.getMaterial().getMaterialNumber();
		}

		/**
		 * Get orderline description.
		 */
		public String getDescription() {
			// return the labelname attrib if there's one
			return this.fdProduct.getAttribute(EnumAttributeName.LABEL_NAME.getName(), this.orderLine.getDescription());
		}

		/**
		 * Get the name of the characteristic to put the ordered quantity in.
		 *
		 * @return null or empty String if none
		 */
		public String getQuantityCharacteristic() {
			return this.fdProduct.getMaterial().getQuantityCharacteristic();
		}

		/**
		 * Get the delivery group.
		 */
		public int getDeliveryGroup() {
			//return this.orderLine.getDeliveryGroup();
			return 0;
		}

		/**
		 * Get orderline department description.
		 */
		public String getDepartmentDesc() {
			return this.orderLine.getDepartmentDesc();
		}

		/**
		 * Get description of ingredients.
		 */
		public String getIngredientsDesc() {
			return this.fdProduct.getIngredients();
		}

		/**
		 * Get the name of the characteristic to put the selected sales unit in.
		 *
		 * @return null or empty String if none
		 */
		public String getSalesUnitCharacteristic() {
			return this.fdProduct.getMaterial().getSalesUnitCharacteristic();
		}

		/**
		 * Get orderline configuration description.
		 */
		public String getConfigurationDesc() {
			return this.orderLine.getConfigurationDesc();
		}

		/**
		 * Get quantity ordered.
		 */
		public double getQuantity() {
			return this.orderLine.getQuantity();
		}

		public String getLineNumber() {
			return this.orderLine.getOrderLineNumber();
		}

		/**
		 * Get selected variation-variation option pairs.
		 */
		public Map<String,String> getOptions() {
			return this.orderLine.getOptions();
		}

		/**
		 * Get selected sales unit.
		 */
		public String getSalesUnit() {
			return this.orderLine.getSalesUnit();
		}

		public List<ErpInventoryModel> getInventories() {
			return this.inventories;
		}

		public void setInventories(List<ErpInventoryModel> inventories) {
			this.inventories = inventories;
		}

		/**
		 * Get the availability check rule.
		 */
		public EnumATPRule getAtpRule() {
			
			//return this.fdProduct.getMaterial().getAtpRule();
			return this.fdProduct.getMaterial().getMaterialPlants().get(getPickingPlantId()).getAtpRule();
		}

		public double getFixedPrice() {
			if(FDStoreProperties.getGiftcardSkucode().equalsIgnoreCase(orderLine.getSku().getSkuCode()))
				return orderLine.getPrice();			
			return 0;
		}
		
		public String getPricingZoneId() {
			//Ignore begining zeroes. 0000100000 --> 100000
			return orderLine.getPricingZoneId();
		}
		
		public ErpCouponDiscountLineModel getCouponDiscount(){
			return orderLine.getCouponDiscount();
		}

		@Override
		public EnumTaxationType getTaxationType() {
			return orderLine.getTaxationType();
		}

		@Override
		public String getPickingPlantId() {
			return orderLine.getPlantID();
		}
	}

	@Override
	public ErpChargeLineModel getCharge(EnumChargeType chargeType) {
		return erpOrder.getCharge(chargeType);
	}
	
	public double getGcAmount(){
		return erpOrder.getAppliedGiftCardAmount();
	}

	@Override
	public String getPlant() {
		return erpOrder.getDeliveryInfo().getDeliveryPlantInfo().getPlantId();
	}

	@Override
	public String getSalesOrg() {
		return erpOrder.getDeliveryInfo().getDeliveryPlantInfo().getSalesOrg();
	}

	@Override
	public String getDistributionChannel() {
		return erpOrder.getDeliveryInfo().getDeliveryPlantInfo().getDistChannel();
	}
	
	public boolean isAddOnOrder(){
		EnumPaymentType pt = this.erpOrder.getPaymentMethod().getPaymentType();
		return pt.equals(EnumPaymentType.ADD_ON_ORDER);
		
	}
	
	public String getReferenceOrderId(){
		
		return  this.erpOrder.getPaymentMethod().getReferencedOrder();
		
	}
// introduced for storefront2.0
	/**
	 * @return the erpOrder
	 */
	public ErpAbstractOrderModel getErpOrder() {
		return erpOrder;
	}
	

}