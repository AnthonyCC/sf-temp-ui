package com.freshdirect.fdstore.promotion;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface PromotionI extends Serializable {

	public String getPromotionCode();

	public boolean evaluate(PromotionContextI context);
	
	//public boolean evaluateLineItemPromo(PromotionContextI context);

	public boolean apply(PromotionContextI context);
	
	//public boolean applyLineItem(PromotionContextI context);
	
	public String getDescription();

	public Date getExpirationDate();

	/** @return List of HeaderDiscountRule */
	public List getHeaderDiscountRules();

	/** @return total amount of header discount rules */
	public double getHeaderDiscountTotal();
	
	public boolean isSampleItem();
	
	public double getMinSubtotal();
	
	public EnumPromotionType getPromotionType();
	
	public Timestamp getModifyDate();
	//The following methods were added as part of Category Discount Implementation.
	public boolean isRedemption();
	
	public boolean isWaiveCharge();
	
	public boolean isHeaderDiscount();
	
	public boolean isSignupDiscount();
	
	public boolean isCategoryDiscount();
	
	public boolean isLineItemDiscount();
	
	public boolean isRecommendedItemsOnly();
	
	public boolean isAllowHeaderDiscount();
	
	public double getLineItemDiscountPercentOff();
}