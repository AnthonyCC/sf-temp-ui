/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.pricing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing a discount.
 *
 * @version $Revision$
 * @author $Author$
 */
public class Discount implements java.io.Serializable {
	
	private static final long serialVersionUID = -6822842931883415109L;
	private final String promotionCode;
	private final EnumDiscountType discountType;
	private final double amount;
	private String promotionDescription=" ";
	private int skuLimit=0;
	private double maxPercentageDiscount=0.0;

	@JsonCreator
	public Discount(@JsonProperty("promotionCode") String promotionCode,
			@JsonProperty("promotionType") EnumDiscountType promotionType, @JsonProperty("amount") double amount) {
		this.promotionCode = promotionCode;
		this.discountType = promotionType;
		this.amount = amount;
	}

	public String getPromotionCode() {
		return this.promotionCode;
	}

	public EnumDiscountType getDiscountType() {
		return this.discountType;
	}

	public double getAmount() {
		return this.amount;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("Discount[");
		buf.append(this.promotionCode).append(' ');
		buf.append(this.amount).append(' ');
		buf.append(this.discountType.toString());
		buf.append(']');
		return buf.toString();
	}

	public String getPromotionDescription() {
		return promotionDescription;
	}

	public void setPromotionDescription(String promotionDescription) {
		this.promotionDescription = promotionDescription;
	}

	public void setSkuLimit(int skuLimit) {
		this.skuLimit = skuLimit;
	}

	public int getSkuLimit() {
		return skuLimit;
	}
	
	public void setMaxPercentageDiscount(double maxDiscount) {
		this.maxPercentageDiscount = maxDiscount;
	}
	
	public double getMaxPercentageDiscount() {
		return maxPercentageDiscount;
	}

}