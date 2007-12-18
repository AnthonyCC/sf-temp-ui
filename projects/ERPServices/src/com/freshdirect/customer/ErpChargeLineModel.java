package com.freshdirect.customer;

import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.framework.core.ModelSupport;

/**
 * @stereotype fd-model
 */
public class ErpChargeLineModel extends ModelSupport {

    private EnumChargeType type	= null;
    private String reasonCode	= null;
    private double amount;
    private Discount discount;
    private double taxRate;

    public ErpChargeLineModel() {
    	super();
    }

    public ErpChargeLineModel(EnumChargeType type, String reason, double amount, Discount discount, double taxRate) {
    	this();
		this.type = type;
		this.reasonCode = reason;
		this.amount = amount;
		this.discount = discount;
		this.taxRate = taxRate;
    }
    
    public ErpChargeLineModel(ErpChargeLineModel charge) {
    	this();
    	this.type = charge.getType();
    	this.reasonCode = charge.getReasonCode();
    	this.amount = charge.getAmount();
    	this.discount = charge.getDiscount();
    	this.taxRate = charge.getTaxRate();
    }

	public double getAmount(){ return this.amount; }
	
	public void setAmount(double amount) {
		this.amount = amount;	
	}

    public String getReasonCode(){ return reasonCode; }

    public void setReasonCode(String reasonCode){ this.reasonCode = reasonCode; }


    public EnumChargeType getType(){ return type; }

    public void setType(EnumChargeType t){ this.type = t; }


	public Discount getDiscount() {
		return this.discount;
	}
	
	public void setDiscount(Discount discount) {
		this.discount = discount;
	}
	
	/** 
	 * Get amount with promotion applied.
	 */
	public double getTotalAmount() {
		if (this.discount==null) {
			return this.amount;	
		}

        EnumDiscountType pt = this.discount.getDiscountType();
        if ( EnumDiscountType.PERCENT_OFF.equals(pt) ) {
             return this.amount * ( 1.0 - this.discount.getAmount() );
        } else if ( EnumDiscountType.DOLLAR_OFF.equals(pt) ) {
            return this.amount - this.discount.getAmount();
        }

		throw new RuntimeException("Unknown promotion: "+this.discount);
	}
	
	public double getTaxRate() {
		return this.taxRate;
	}
	
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
	
	public boolean isTaxed () {
		return this.taxRate > 0;
	}
	
	public String toString(){
		return "[ErpChargeLineModel type: "+type+" reason: "+reasonCode+" amount: "+amount +" taxRate: "+taxRate+"]";
	}

}
