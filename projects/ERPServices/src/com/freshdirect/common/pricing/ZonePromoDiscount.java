package com.freshdirect.common.pricing;

public class ZonePromoDiscount extends Discount 
{
	public ZonePromoDiscount(String promotionCode, EnumDiscountType promotionType, double amount) 
	{
		super(promotionCode,promotionType,amount);
	}

}
