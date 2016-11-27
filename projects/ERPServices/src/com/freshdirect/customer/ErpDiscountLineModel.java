/*
 * Created on Jul 12, 2005
 *
 */
package com.freshdirect.customer;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.framework.core.ModelSupport;

/**
 * @author jng
 *
 */
public class ErpDiscountLineModel extends ModelSupport {

    private Discount discount;

    public ErpDiscountLineModel() {
    	super();
    }

    public ErpDiscountLineModel(Discount discount) {
    	this();
		this.discount = discount;
    }
    
    public ErpDiscountLineModel(ErpDiscountLineModel discountLine) {
    	this();
    	this.discount = discountLine.getDiscount();
    }


	public Discount getDiscount() {
		return this.discount;
	}
	
	public void setDiscount(Discount discount) {
		this.discount = discount;
	}
		
	public String toString(){
		return "[ErpDiscountLineModel discount type: "+discount.getDiscountType()+" discount code: "+discount.getPromotionCode()+" discount amount: "+discount.getAmount()+"]";
	}
	
}
