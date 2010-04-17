package com.freshdirect.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.ErpRecipentModel;

/**
 * @stereotype fd-model
 */
public abstract class ErpAbstractOrderModel extends ErpTransactionModel {
	private static final long serialVersionUID = 2941997132034110838L;

	/**
     * @associates <{ErpOrderLineModel}>
     * @link aggregationByValue
     */
    private List<ErpOrderLineModel> orderLines;
    private Date requestedDate;
    private Discount discount;
    private Date pricingDate;
    private ErpPaymentMethodI paymentMethod;
	private double subTotal;
	private double tax;
	private String customerServiceMessage;
	private String marketingMessage;
	private String glCode;
	//This attribute is to hold the count of deliverypasses this cart holds.
	private int deliveryPassCount = 0;

	//This attribute flag is to denote whether a delivery pass was applied to this cart.
	private boolean dlvPassApplied;
	
	//This attribute flag is to denote whether a delivery promotion was applied to this cart.
	private boolean dlvPromotionApplied;
	
	private double bufferAmt;

    /**
     * @associates <{ErpAppliedCreditModel}>
     * @link aggregationByValue
     * @clientCardinality 1
     * @supplierCardinality 0..*
     */
    private List<ErpAppliedCreditModel> appliedCredits;

    /**
     * @supplierCardinality 1
     * @clientCardinality 1
     */
    private ErpDeliveryInfoModel deliveryInfo;

	private List<ErpChargeLineModel> charges = null;

	private List<ErpDiscountLineModel> discounts = null;

    public List<ErpRecipentModel> getRecipientsList() {
		return recipientsList;
	}

	//List of Gift cards used for this order. Not persisted
	private List<ErpGiftCardModel> selectedGiftCards;
	
	//usage details for each gift card applied on this order.
	private List<ErpAppliedGiftCardModel> appliedGiftcards;
	
	public void setRecepientsList(List<ErpRecipentModel> recepientsList) {
		this.recipientsList = recepientsList;
	}

	private List<ErpRecipentModel> recipientsList = null;

	public ErpAbstractOrderModel(EnumTransactionType transType) {
        super(transType);

		appliedCredits = new ArrayList<ErpAppliedCreditModel>();
		charges = new ArrayList<ErpChargeLineModel>();
		discounts = new ArrayList<ErpDiscountLineModel>();
		selectedGiftCards =  new ArrayList<ErpGiftCardModel>();
		appliedGiftcards = new ArrayList<ErpAppliedGiftCardModel>();
		recipientsList = new ArrayList<ErpRecipentModel>();
    }

	public void set(ErpAbstractOrderModel order, boolean isNewObject) {
	
		if (order != null) {
			this.setAppliedCredits(order.getAppliedCredits());
			this.setCharges(order.getCharges());
			this.setCustomerServiceMessage(order.getCustomerServiceMessage());
			this.setDeliveryInfo(order.getDeliveryInfo());
			this.setDiscounts(order.getDiscounts());
			this.setMarketingMessage(order.getMarketingMessage());
			this.setOrderLines(order.getOrderLines());			
			this.setPaymentMethod(order.getPaymentMethod());
			this.setPK(order.getPK());
			this.setPricingDate(order.getPricingDate());
			this.setRequestedDate(order.getRequestedDate());
			this.setSubTotal(order.getSubTotal());
			this.setTax(order.getTax());
			this.setTransactionDate(order.getTransactionDate());
			this.setTransactionInitiator(order.getTransactionInitiator());
			this.setTransactionSource(order.getTransactionSource());
			this.setRecepientsList(order.getRecipientsList());
			this.setSelectedGiftCards(order.getSelectedGiftCards());
			this.setAppliedGiftcards(order.getAppliedGiftcards());
			this.setBufferAmt(order.getBufferAmt());
			if (isNewObject) {
				clearPK();
			}

		}
	}
	
	private void clearPK () {
		System.out.println("clear PK called $$$$$$$$$$$$$$$ ");
		setPK(null);

		// null out all charge line primary keys
		List<ErpChargeLineModel> charges = getCharges();
		if (charges != null && charges.size() > 0) {
			Iterator<ErpChargeLineModel> iter = charges.iterator();
			while (iter.hasNext()) {
				ErpChargeLineModel chargeLine = iter.next();
				chargeLine.setPK(null);
			}
		}
		// null out all order line primary keys
		List<ErpOrderLineModel> orderLines = getOrderLines();
		if (orderLines != null && orderLines.size() > 0) {
			Iterator<ErpOrderLineModel> iter = orderLines.iterator();
			while (iter.hasNext()) {
				ErpOrderLineModel orderLine = (ErpOrderLineModel) iter.next();
				orderLine.setPK(null);
			}
		}
		// null out all applied credit primary keys
		List<ErpAppliedCreditModel> credits = getAppliedCredits();
		if (credits != null && credits.size() > 0) {
			for ( ErpAppliedCreditModel credit : credits ) {
				credit.setPK(null);
			}
		}
		
		// null out all promotion line primary keys
		List<ErpDiscountLineModel> discounts = getDiscounts();
		if (discounts != null && discounts.size() > 0) {
			Iterator<ErpDiscountLineModel> iter = discounts.iterator();
			while (iter.hasNext()) {
				ErpDiscountLineModel discountLine = iter.next();
				discountLine.setPK(null);
			}
		}
		
		// null out all receipents
		List<ErpRecipentModel> recepients = getRecipientsList();
		if (recepients != null && recepients.size() > 0) {
			Iterator<ErpRecipentModel> iter = recepients.iterator();
			while (iter.hasNext()) {
				ErpRecipentModel recepientsLine = (ErpRecipentModel) iter.next();
				recepientsLine.setPK(null);
			}
		}

		// null out all applied credit primary keys
		List<ErpAppliedGiftCardModel> appliedGiftcards = getAppliedGiftcards();
		if (appliedGiftcards != null && appliedGiftcards.size() > 0) {
			Iterator<ErpAppliedGiftCardModel> iter = appliedGiftcards.iterator();
			while (iter.hasNext()) {
				ErpAppliedGiftCardModel gc = (ErpAppliedGiftCardModel) iter.next();
				gc.setPK(null);
			}
		}

	}
    public List<ErpOrderLineModel> getOrderLines(){ return orderLines; }

    public void setOrderLines(List<ErpOrderLineModel> orderLines){ this.orderLines = orderLines; }

	public TreeSet<String> getOrderLineDepartments(){

    	TreeSet<String> orderLineDepartments = new TreeSet<String>();

		for ( ErpOrderLineModel ol : orderLines ) {
		    orderLineDepartments.add(ol.getDepartmentDesc());
		}
		return orderLineDepartments;
	}

    public Date getRequestedDate(){ return requestedDate; }

    public void setRequestedDate(Date requestedDate){ this.requestedDate = requestedDate; }

    public Date getPricingDate(){ return pricingDate; }

    public void setPricingDate(Date pricingDate){ this.pricingDate = pricingDate; }

    public ErpPaymentMethodI getPaymentMethod(){ return paymentMethod; }

    public void setPaymentMethod(ErpPaymentMethodI paymentMethod){ this.paymentMethod = paymentMethod; }

    public List<ErpAppliedCreditModel> getAppliedCredits() { return appliedCredits; }

	public void setAppliedCredits(List<ErpAppliedCreditModel> appliedCredits){ this.appliedCredits = appliedCredits; }

    public ErpDeliveryInfoModel getDeliveryInfo() { return deliveryInfo; }

    public void setDeliveryInfo(ErpDeliveryInfoModel deliveryInfo) { this.deliveryInfo = deliveryInfo; }

	public void setSubTotal(double subTotal) { this.subTotal = subTotal; }

	public double getSubTotal() { return this.subTotal; }

	public double getTax() { return this.tax; }

	public void setTax(double tax) { this.tax = tax; }
	
	public ErpOrderLineModel getOrderLine(String lineNumber){
		
		ErpOrderLineModel foundLine = null;
		for(Iterator<ErpOrderLineModel> i = this.orderLines.iterator(); i.hasNext(); ){
			ErpOrderLineModel orderLine = (ErpOrderLineModel)i.next(); 
			if(orderLine.getOrderLineNumber().equals(lineNumber)){
				foundLine = orderLine;
				break;
			}
		}
		return foundLine;
	}
	
	public ErpOrderLineModel getOrderLineByPK(String id){
		ErpOrderLineModel foundLine = null;
		for(Iterator<ErpOrderLineModel> i = this.orderLines.iterator(); i.hasNext(); ){
			ErpOrderLineModel ol = (ErpOrderLineModel) i.next();
			if(ol.getPK().getId().equals(id)){
				foundLine = ol;
				break;
			}
		}
		return foundLine;
	}


	public double getDepositValue() {
		double deposit = 0;
		for (Iterator<ErpOrderLineModel> i=this.orderLines.iterator(); i.hasNext(); ) {
			deposit += ((ErpOrderLineModel)i.next()).getDepositValue();
		}
		return deposit;
	}


	public double getAmount() {
        double amount = 0.0;

        // add up orderline prices
        for (Iterator<ErpOrderLineModel> i=this.orderLines.iterator(); i.hasNext(); ) {
        	double price = ((ErpOrderLineModel)i.next()).getPrice();
			amount += price;
        }

        // subtract credits
        if (this.appliedCredits!=null) {
	        for (Iterator<ErpAppliedCreditModel> i=this.appliedCredits.iterator(); i.hasNext(); ) {
				amount -= i.next().getAmount();
	        }
        }
        
        if (this.discounts != null && this.discounts.size() > 0) {
        	for (Iterator<ErpDiscountLineModel> iter = this.discounts.iterator(); iter.hasNext();) {
            	ErpDiscountLineModel discountLine = iter.next();
            	amount -= discountLine.getDiscount().getAmount();
        	}
        } else if(this.discount != null){  // this is to be backward compatible
			amount -= this.discount.getAmount();
		}

        // add charges (with their discounts applied)
        for (Iterator<ErpChargeLineModel> i=this.charges.iterator(); i.hasNext(); ) {
        	amount += i.next().getTotalAmount();
        }

        // add tax
		amount += this.tax;
		
		amount += this.getDepositValue();

        return MathUtil.roundDecimal(amount);
	}

    public List<ErpChargeLineModel> getCharges() { return this.charges; }
    public void setCharges(List<ErpChargeLineModel> l) { this.charges = l; }
    public void addCharge(ErpChargeLineModel charge) { this.charges.add(charge); }
    public void addCharges(List<ErpChargeLineModel> l) { this.charges.addAll(l); }

	public ErpChargeLineModel getCharge(EnumChargeType chargeType) {
        for (Iterator<ErpChargeLineModel> i=this.charges.iterator(); i.hasNext(); ) {
        	ErpChargeLineModel curr = i.next();
        	if (chargeType.equals( curr.getType() )) {
				return curr;
        	}
        }
        return null;
	}

	public List<ErpChargeLineModel> getCharges(EnumChargeType chargeType) {
		List<ErpChargeLineModel> chargeList = new ArrayList<ErpChargeLineModel>();
        for (Iterator<ErpChargeLineModel> i=this.charges.iterator(); i.hasNext(); ) {
        	ErpChargeLineModel curr = i.next();
        	if (chargeType.equals( curr.getType() )) {
        		chargeList.add( curr );
        	}
        }
		return chargeList;
	}

	public double getDeliverySurcharge() {
		ErpChargeLineModel charge = getCharge(EnumChargeType.DELIVERY);
		return charge==null ? 0.0 : charge.getTotalAmount();
	}

	public double getPhoneSurcharge() {
		ErpChargeLineModel charge = getCharge(EnumChargeType.PHONE);
		return charge==null ? 0.0 : charge.getTotalAmount();
	}

    public void setCustomerServiceMessage(String s) { this.customerServiceMessage = s; }
    public String getCustomerServiceMessage() { return this.customerServiceMessage; }
    public void setMarketingMessage(String s) { this.marketingMessage = s; }
    public String getMarketingMessage() { return this.marketingMessage; }
    public void setGlCode(String s) { this.glCode = s; }
    public String getGlCode(){ return this.glCode; }
    
    public List<ErpDiscountLineModel> getDiscounts() { return this.discounts; }
    public void setDiscounts(List<ErpDiscountLineModel> l) { this.discounts = l; }
    public void addDiscount(ErpDiscountLineModel discount) { this.discounts.add(discount); }
    public void addDiscounts(List<ErpDiscountLineModel> l) { this.discounts.addAll(l); }

	public ErpDiscountLineModel getDiscount(EnumDiscountType discountType) {
        for (Iterator<ErpDiscountLineModel> i=this.discounts.iterator(); i.hasNext(); ) {
        	ErpDiscountLineModel curr = i.next();
        	Discount discount = curr.getDiscount();
        	if (discount != null && discountType.equals( discount.getDiscountType() )) {
				return curr;
        	}
        }
        return null;
	}

	public List<ErpDiscountLineModel> getDiscounts(EnumDiscountType discountType) {
		List<ErpDiscountLineModel> discountList = new ArrayList<ErpDiscountLineModel>();
        for (Iterator<ErpDiscountLineModel> i=this.discounts.iterator(); i.hasNext(); ) {
        	ErpDiscountLineModel curr = i.next();
        	Discount discount = curr.getDiscount();
        	if (discount != null && discountType.equals( discount.getDiscountType() )) {
        		discountList.add( curr );
        	}
        }
		return discountList;
	}

	public int getDeliveryPassCount() {
		return deliveryPassCount;
	}

	public void setDeliveryPassCount(int deliveryPassCount) {
		this.deliveryPassCount = deliveryPassCount;
	}

	public boolean isDlvPassApplied() {
		return dlvPassApplied;
	}

	public void setDlvPassApplied(boolean dlvPassApplied) {
		this.dlvPassApplied = dlvPassApplied;
	}

	public boolean isDlvPromotionApplied() {
		return dlvPromotionApplied;
	}

	public void setDlvPromotionApplied(boolean dlvPromotionApplied) {
		this.dlvPromotionApplied = dlvPromotionApplied;
	}

	public List<ErpGiftCardModel> getSelectedGiftCards() {
		return selectedGiftCards;
	}

	public void setSelectedGiftCards(List<ErpGiftCardModel> selectedGiftCards) {
		this.selectedGiftCards = selectedGiftCards;
	}

	public List<ErpAppliedGiftCardModel> getAppliedGiftcards() {
		return appliedGiftcards;
	}

	public void setAppliedGiftcards(List<ErpAppliedGiftCardModel> appliedGiftcards) {
		this.appliedGiftcards = appliedGiftcards;
	}
	
	public double getAppliedGiftCardAmount() {
		double amount = 0.0;
        for (Iterator<ErpAppliedGiftCardModel> i=this.appliedGiftcards.iterator(); i.hasNext(); ) {
        	ErpAppliedGiftCardModel curr = (ErpAppliedGiftCardModel)i.next();
        	amount += curr.getAmount();
        }
		return amount;
	}
	
	public ErpOrderLineModel getOrderLineByOrderLineNumber(String id){
		ErpOrderLineModel foundLine = null;
		for(Iterator<ErpOrderLineModel> i = this.orderLines.iterator(); i.hasNext(); ){
			ErpOrderLineModel ol = (ErpOrderLineModel) i.next();
			if(ol.getOrderLineNumber().equals(id)){
				foundLine = ol;
				break;
			}
		}
		return foundLine;
	}

	/**
	 * @return the bufferAmt
	 */
	public double getBufferAmt() {
		return bufferAmt;
	}

	/**
	 * @param bufferAmt the bufferAmt to set
	 */
	public void setBufferAmt(double bufferAmt) {
		this.bufferAmt = bufferAmt;
	}
}
