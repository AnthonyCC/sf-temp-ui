package com.freshdirect.fdstore;

import com.freshdirect.common.pricing.ConfiguredPrice;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.Price;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.PricingEngine;
import com.freshdirect.common.pricing.PricingException;


public class FDPricingEngine {

	private FDPricingEngine() {
	}

	public static FDConfiguredPrice doPricing(FDProduct fdProduct, FDConfigurableI prConf, Discount discount, PricingContext pCtx, FDGroup group, double grpQty, String pricingUnit)
		throws PricingException {

		ConfiguredPrice configuredPrice = PricingEngine.getConfiguredPrice(fdProduct.getPricing(), prConf, pCtx, group, grpQty);

		final double price;
		final double discountValue;

		if (discount == null) {
			price = configuredPrice.getPrice().getPrice();
			discountValue = 0;

		} else {
			if (EnumDiscountType.SAMPLE.equals(discount.getDiscountType())) {
				// samples are corrected to a base price of zero, it's not an amount off...
				price = 0;
				discountValue = 0;

			} else {
				Price discountedPrice = PricingEngine.applyDiscount(configuredPrice.getPrice(), prConf.getQuantity(), discount, pricingUnit);
				price = configuredPrice.getPrice().getPrice();
				discountValue = price - discountedPrice.getPrice();
			}
		}
		
		//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$Pricing for :" + fdProduct.getSkuCode() + " -discountValue: " + discountValue + " -price:" + price + " -condn:" + configuredPrice.getPricingCondition());

		return new FDConfiguredPrice(price, discountValue, configuredPrice.getPricingCondition());
	}

}
