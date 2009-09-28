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
import com.freshdirect.giftcard.ErpRecipentModel;

/**
 * @stereotype fd-model
 */
public abstract class ErpAbstractOrderModel extends ErpTransactionModel {

	/**
     * @associates <{ErpOrderLineModel}>
     * @link aggregationByValue
     */
    private List orderLines;
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
	//List of ErpGiftCardI
    private List appliedCredits;

    /**
     * @supplierCardinality 1
     * @clientCardinality 1
     */
    private ErpDeliveryInfoModel deliveryInfo;

	private List charges = null;

	private List discounts = null;

    public List getRecepientsList() {
		return recepientsList;
	}

	//List of Gift cards used for this order. Not persisted
	private List selectedGiftCards;
	
	//usage details for each gift card applied on this order.
	private List appliedGiftcards;
	
	public void setRecepientsList(List recepientsList) {
		this.recepientsList = recepientsList;
	}

	private List recepientsList=null;

	public ErpAbstractOrderModel(EnumTransactionType transType) {
        super(transType);
		appliedCredits = new ArrayList();
		charges = new ArrayList();
		discounts = new ArrayList();
		selectedGiftCards =  new ArrayList();
		appliedGiftcards = new ArrayList();
		recepientsList = new ArrayList();
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
			this.setRecepientsList(order.getRecepientsList());
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
		List charges = getCharges();
		if (charges != null && charges.size() > 0) {
			Iterator iter = charges.iterator();
			while (iter.hasNext()) {
				ErpChargeLineModel chargeLine = (ErpChargeLineModel) iter.next();
				chargeLine.setPK(null);
			}
		}
		// null out all order line primary keys
		List orderLines = getOrderLines();
		if (orderLines != null && orderLines.size() > 0) {
			Iterator iter = orderLines.iterator();
			while (iter.hasNext()) {
				ErpOrderLineModel orderLine = (ErpOrderLineModel) iter.next();
				orderLine.setPK(null);
			}
		}
		// null out all applied credit primary keys
		List credits = getAppliedCredits();
		if (credits != null && credits.size() > 0) {
			Iterator iter = credits.iterator();
			while (iter.hasNext()) {
				ErpAppliedCreditModel credit = (ErpAppliedCreditModel) iter.next();
				credit.setPK(null);
			}
		}
		
		// null out all promotion line primary keys
		List discounts = getDiscounts();
		if (discounts != null && discounts.size() > 0) {
			Iterator iter = discounts.iterator();
			while (iter.hasNext()) {
				ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
				discountLine.setPK(null);
			}
		}
		
		// null out all receipents
		List recepients = getRecepientsList();
		if (recepients != null && recepients.size() > 0) {
			Iterator iter = recepients.iterator();
			while (iter.hasNext()) {
				ErpRecipentModel recepientsLine = (ErpRecipentModel) iter.next();
				recepientsLine.setPK(null);
			}
		}
		/*
		System.out.println("getGiftcardPaymentMethods in ab model "+ getGiftcardPaymentMethods().size());
		// null out all used gift cards primary keys
		List gcPMList = getGiftcardPaymentMethods();
		if (gcPMList != null && gcPMList.size() > 0) {
			Iterator iter = gcPMList.iterator();
			while (iter.hasNext()) {
				ErpPaymentMethodModel gc = (ErpPaymentMethodModel) iter.next();
				gc.setPK(null);
			}
		}
		*/
		// null out all applied credit primary keys
		List appliedGiftcards = getAppliedGiftcards();
		if (appliedGiftcards != null && appliedGiftcards.size() > 0) {
			Iterator iter = appliedGiftcards.iterator();
			while (iter.hasNext()) {
				ErpAppliedGiftCardModel gc = (ErpAppliedGiftCardModel) iter.next();
				gc.setPK(null);
			}
		}

	}
    public List getOrderLines(){ return orderLines; }

    public void setOrderLines(List orderLines){ this.orderLines = orderLines; }

	public TreeSet getOrderLineDepartments(){

    	TreeSet orderLineDepartments = new TreeSet();

		for (java.util.Iterator i= orderLines.iterator(); i.hasNext();) {
		    ErpOrderLineModel ol = (ErpOrderLineModel) i.next();
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

    public List getAppliedCredits() { return appliedCredits; }

	public void setAppliedCredits(List appliedCredits){ this.appliedCredits = appliedCredits; }

    public ErpDeliveryInfoModel getDeliveryInfo() { return deliveryInfo; }

    public void setDeliveryInfo(ErpDeliveryInfoModel deliveryInfo) { this.deliveryInfo = deliveryInfo; }

	public void setSubTotal(double subTotal) { this.subTotal = subTotal; }

	public double getSubTotal() { return this.subTotal; }

	public double getTax() { return this.tax; }

	public void setTax(double tax) { this.tax = tax; }
	
	public ErpOrderLineModel getOrderLine(String lineNumber){
		
		ErpOrderLineModel foundLine = null;
		for(Iterator i = this.orderLines.iterator(); i.hasNext(); ){
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
		for(Iterator i = this.orderLines.iterator(); i.hasNext(); ){
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
		for (Iterator i=this.orderLines.iterator(); i.hasNext(); ) {
			deposit += ((ErpOrderLineModel)i.next()).getDepositValue();
		}
		return deposit;
	}


	public double getAmount() {
        double amount = 0.0;

        // add up orderline prices
        for (Iterator i=this.orderLines.iterator(); i.hasNext(); ) {
        	double price = ((ErpOrderLineModel)i.next()).getPrice();
			System.out.println("Amount $$$$$$$$$$$$ "+price);
			amount += price;
        }

        // subtract credits
        if (this.appliedCredits!=null) {
	        for (Iterator i=this.appliedCredits.iterator(); i.hasNext(); ) {
				amount -= ((ErpAppliedCreditModel)i.next()).getAmount();
	        }
        }
        
        if (this.discounts != null && this.discounts.size() > 0) {
        	for (Iterator iter = this.discounts.iterator(); iter.hasNext();) {
            	ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
            	amount -= discountLine.getDiscount().getAmount();
        	}
        } else if(this.discount != null){  // this is to be backward compatible
			amount -= this.discount.getAmount();
		}

        // add charges (with their discounts applied)
        for (Iterator i=this.charges.iterator(); i.hasNext(); ) {
        	amount += ((ErpChargeLineModel)i.next()).getTotalAmount();
        }

        // add tax
		amount += this.tax;
		
		amount += this.getDepositValue();

        return MathUtil.roundDecimal(amount);
	}

    public List getCharges() { return this.charges; }
    public void setCharges(List l) { this.charges = l; }
    public void addCharge(ErpChargeLineModel charge) { this.charges.add(charge); }
    public void addCharges(List l) { this.charges.addAll(l); }

	public ErpChargeLineModel getCharge(EnumChargeType chargeType) {
        for (Iterator i=this.charges.iterator(); i.hasNext(); ) {
        	ErpChargeLineModel curr = (ErpChargeLineModel)i.next();
        	if (chargeType.equals( curr.getType() )) {
				return curr;
        	}
        }
        return null;
	}

	public List getCharges(EnumChargeType chargeType) {
		List chargeList = new ArrayList();
        for (Iterator i=this.charges.iterator(); i.hasNext(); ) {
        	ErpChargeLineModel curr = (ErpChargeLineModel)i.next();
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
    
    public List getDiscounts() { return this.discounts; }
    public void setDiscounts(List l) { this.discounts = l; }
    public void addDiscount(ErpDiscountLineModel discount) { this.discounts.add(discount); }
    public void addDiscounts(List l) { this.discounts.addAll(l); }

	public ErpDiscountLineModel getDiscount(EnumDiscountType discountType) {
        for (Iterator i=this.discounts.iterator(); i.hasNext(); ) {
        	ErpDiscountLineModel curr = (ErpDiscountLineModel)i.next();
        	Discount discount = curr.getDiscount();
        	if (discount != null && discountType.equals( discount.getDiscountType() )) {
				return curr;
        	}
        }
        return null;
	}

	public List getDiscounts(EnumDiscountType discountType) {
		List discountList = new ArrayList();
        for (Iterator i=this.discounts.iterator(); i.hasNext(); ) {
        	ErpDiscountLineModel curr = (ErpDiscountLineModel)i.next();
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

	public List getSelectedGiftCards() {
		return selectedGiftCards;
	}

	public void setSelectedGiftCards(List selectedGiftCards) {
		this.selectedGiftCards = selectedGiftCards;
	}

	public List getAppliedGiftcards() {
		return appliedGiftcards;
	}

	public void setAppliedGiftcards(List appliedGiftcards) {
		this.appliedGiftcards = appliedGiftcards;
	}
	
	public double getAppliedGiftCardAmount() {
		double amount = 0.0;
        for (Iterator i=this.appliedGiftcards.iterator(); i.hasNext(); ) {
        	ErpAppliedGiftCardModel curr = (ErpAppliedGiftCardModel)i.next();
        	amount += curr.getAmount();
        }
		return amount;
	}
	
	public ErpOrderLineModel getOrderLineByOrderLineNumber(String id){
		ErpOrderLineModel foundLine = null;
		for(Iterator i = this.orderLines.iterator(); i.hasNext(); ){
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
