package com.freshdirect.customer;

import com.freshdirect.common.pricing.EnumTaxationType;
import com.freshdirect.framework.util.MathUtil;

public class TaxCalculatorUtil {

	public static double getTaxValue(ErpOrderLineModel orderLine) {
		return getTaxValue(orderLine.getActualPrice(), orderLine.getDiscountAmount(), null!=orderLine.getCouponDiscount()?orderLine.getCouponDiscount().getDiscountAmt():0.0, orderLine.getTaxRate(), orderLine.getTaxationType());		
	}
	
	public static double getTaxValue(double originalPrice, double promotionalDiscount, double couponsDiscount, double taxRate, EnumTaxationType taxationType) {
		double taxValue=0.0;
		double taxablePrice =0.0;
		if(null == taxationType || null == EnumTaxationType.getEnum(taxationType.getName())){
			//default
			taxationType = EnumTaxationType.TAX_AFTER_ALL_DISCOUNTS;
		}
		
		if(EnumTaxationType.TAX_AFTER_ALL_DISCOUNTS.equals(taxationType)){
			taxablePrice = originalPrice-promotionalDiscount-couponsDiscount;
		}else if(EnumTaxationType.TAX_AFTER_INTERNAL_DISCOUNTS.equals(taxationType)){
			taxablePrice = originalPrice-promotionalDiscount;
		}/*else if(EnumTaxationType.TAX_ON_ORIGINAL_PRICE.equals(taxationType)){
			taxablePrice = originalPrice;
		}else if(EnumTaxationType.TAX_AFTER_EXTERNAL_DISCOUNTS.equals(taxationType)){
			taxablePrice = originalPrice-couponsDiscount;
		}*/
		
		taxValue = MathUtil.roundDecimal(taxablePrice*taxRate);
		return taxValue > 0 ? taxValue :0;
	}
}
