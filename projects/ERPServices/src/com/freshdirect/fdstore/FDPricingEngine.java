package com.freshdirect.fdstore;

import com.freshdirect.common.pricing.ConfiguredPrice;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.Price;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.PricingEngine;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.customer.ErpCouponDiscountLineModel;


public class FDPricingEngine {

	private FDPricingEngine() {
	}

	public static FDConfiguredPrice doPricing(FDProduct fdProduct, FDConfigurableI prConf, Discount discount, PricingContext pCtx, FDGroup group, double grpQty, String pricingUnit, ErpCouponDiscountLineModel couponDiscount, Double scaleQuantity)
		throws PricingException {

		ConfiguredPrice configuredPrice = PricingEngine.getConfiguredPrice(fdProduct.getPricing(), prConf, pCtx, group, grpQty,scaleQuantity);

		final double price;
		final double discountValue;
		final double couponDiscountValue;
		FDConfiguredPrice fdConfiguredPrice=null;
		Price discountedPrice =null;
		if (discount == null) {
			price = configuredPrice.getPrice().getPrice();
			discountValue = 0;

		} else {
			if (EnumDiscountType.SAMPLE.equals(discount.getDiscountType()) || EnumDiscountType.FREE.equals(discount.getDiscountType())) {
				// samples are corrected to a base price of zero, it's not an amount off...
				price = 0;
				discountValue = 0;

			} else {
				discountedPrice = PricingEngine.applyDiscount(configuredPrice.getPrice(), prConf.getQuantity(), discount, pricingUnit);
				price = configuredPrice.getPrice().getPrice();
				discountValue = price - discountedPrice.getPrice();
			}
		}
		fdConfiguredPrice =new FDConfiguredPrice(price, discountValue, configuredPrice.getPricingCondition(),configuredPrice.getZoneInfo());
		discountedPrice =null!=discountedPrice?discountedPrice:configuredPrice.getPrice();
		//Apply coupon discount if any, after applying line-item discounts.
		if(null != couponDiscount){
			Price couponDiscountedPrice = PricingEngine.applyCouponDiscount(discountedPrice, 1, couponDiscount, pricingUnit);
			couponDiscountValue =discountedPrice.getPrice()-couponDiscountedPrice.getPrice(); 
			fdConfiguredPrice.setCouponDiscountValue(couponDiscountValue);
		}
		
		return fdConfiguredPrice;
	}

}
