package com.freshdirect.customer;


import java.util.*;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.framework.util.MathUtil;

public abstract class ErpAbstractInvoiceModel extends ErpTransactionModel {
    
	private static final long	serialVersionUID	= -3874831690840201472L;
	
	/**
     * @link aggregationByValue
     * @clientCardinality 1..*
     * @supplierCardinality 1..* 
     */
	private double tax;
	private double subtotal;
	private double amount;
	private List<ErpChargeLineModel> charges;
	private String invoiceNumber;

    /**
     * @associates <{ErpInvoiceLineModel}>
     * @link aggregationByValue
     */
    private List<ErpInvoiceLineModel> invoiceLines;

	public ErpAbstractInvoiceModel(EnumTransactionType transType) {
        super(transType);
        this.charges = new ArrayList<ErpChargeLineModel>();
    }

	public double getTax(){
		return this.tax;
	}
		
	public void setTax(double tax){
		this.tax = tax;
	}

	public double getDepositValue() {
		double deposit = 0.0;
		for ( ErpInvoiceLineModel invoiceLine : invoiceLines ) {
			deposit += invoiceLine.getDepositValue();
		}
		return deposit;
	} 

	public double getSubTotal(){		
		return this.subtotal;
	}
	
	
	public double getActualSubTotal(){		
		double subTotal = 0.0;
		if ( invoiceLines == null ) 
			return 0; 
		for ( ErpInvoiceLineModel invoiceLine : invoiceLines ) {
			subTotal += MathUtil.roundDecimal(invoiceLine.getPrice()+invoiceLine.getActualDiscountAmount());
		}
		return MathUtil.roundDecimal(subTotal);		
	}
			

	public void setSubTotal(double subTotal){
		this.subtotal = subTotal;
	}

		

	public List<ErpInvoiceLineModel> getInvoiceLines(){
		return invoiceLines;
	}
	
	public ErpInvoiceLineModel getInvoiceLine(String lineNumber){
		ErpInvoiceLineModel foundInvoiceLine = null;
		for ( ErpInvoiceLineModel invoiceLine : invoiceLines ) {
			if(invoiceLine.getOrderLineNumber().equals(lineNumber)){
				foundInvoiceLine = invoiceLine;
				break;
			}
		}
		return foundInvoiceLine;
	}
			

	public void setInvoiceLines( List<ErpInvoiceLineModel> lines ) {
		this.invoiceLines = lines;
	}

    public double getAmount() {
        return this.amount;
    }
	public void setAmount(double amount){
		this.amount = amount;
	}
	
	public double getPreTaxAmount(){
		return this.amount - this.tax;
	}
	
    public List<ErpChargeLineModel> getCharges() { 
    	return this.charges; 
    }
    public void setCharges(List<ErpChargeLineModel> charges) { 
    	this.charges = charges; 
    }
    public void addCharge(ErpChargeLineModel charge) { 
    	this.charges.add(charge); 
    }
    public void addCharges(List<ErpChargeLineModel> charges) { 
    	this.charges.addAll(charges); 
    }

	public ErpChargeLineModel getCharge(EnumChargeType chargeType) {
		ErpChargeLineModel charge = null;
        for ( ErpChargeLineModel curr : charges ) {
        	if (chargeType.equals( curr.getType() )) {
        		if (charge!=null) {
        			throw new RuntimeException("Multiple charges present of type "+chargeType);
        		}
        		charge = curr;
        	}
        }
        return charge;
	}

	public List<ErpChargeLineModel> getCharges(EnumChargeType chargeType) {
		List<ErpChargeLineModel> chargeList = new ArrayList<ErpChargeLineModel>();
        for ( ErpChargeLineModel curr  : charges ) {
        	if (chargeType.equals( curr.getType() )) {
        		chargeList.add( curr );
        	}
        }
		return chargeList;
	}
 
	public double getDeliverySurcharge() {
		ErpChargeLineModel charge = getCharge(EnumChargeType.DELIVERY);
		return charge==null ? 0.0 : charge.getAmount();
	}
	
	public double getRestockingFee(ErpAffiliate affiliate){
		ErpChargeLineModel charge = null;
		
		if(ErpAffiliate.getEnum(ErpAffiliate.CODE_FD).equals(affiliate)){
			charge = this.getCharge(EnumChargeType.FD_RESTOCKING_FEE);
		}
		if(ErpAffiliate.getEnum(ErpAffiliate.CODE_WBL).equals(affiliate)){
			charge = this.getCharge(EnumChargeType.WBL_RESTOCKING_FEE);
		}
		if(ErpAffiliate.getEnum(ErpAffiliate.CODE_BC).equals(affiliate)){
			charge = this.getCharge(EnumChargeType.BC_RESTOCKING_FEE);
		}
		if(ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ).equals(affiliate)){
			charge = this.getCharge(EnumChargeType.USQ_RESTOCKING_FEE);
		}

		return charge == null ? 0.0 : charge.getAmount();
	}
	
	public double getFDRestockingFee(){
		ErpChargeLineModel charge = this.getCharge(EnumChargeType.FD_RESTOCKING_FEE);
		return charge==null ? 0.0 : charge.getAmount();
	}
	
	public double getWBLRestockingFee(){
		ErpChargeLineModel charge = this.getCharge(EnumChargeType.WBL_RESTOCKING_FEE);
		return charge==null ? 0.0 : charge.getAmount();
	}
	
	public double getPhoneCharge(){
		ErpChargeLineModel charge = this.getCharge(EnumChargeType.PHONE);
		return charge==null ? 0.0 : charge.getAmount();
	}

    public String getInvoiceNumber(){
		return this.invoiceNumber;
	}

    public void setInvoiceNumber(String invoiceNum){
		this.invoiceNumber = invoiceNum;
	}
	
}
