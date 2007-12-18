/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.pricing;

/**
 * Class representing a discount.
 *
 * @version $Revision$
 * @author $Author$
 */
public class Discount implements java.io.Serializable {

	private final String promotionCode;
	private final EnumDiscountType discountType;
	private final double amount;

	public Discount(String promotionCode, EnumDiscountType promotionType, double amount) {
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

}