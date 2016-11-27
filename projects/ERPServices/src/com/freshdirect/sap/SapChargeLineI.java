package com.freshdirect.sap;

import java.io.Serializable;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumChargeType;

public interface SapChargeLineI extends Serializable {

	public EnumChargeType getType();
	public double getAmount();
	public String getMaterialNumber();
	public Discount getDiscount();
	public double getTaxRate();
}
