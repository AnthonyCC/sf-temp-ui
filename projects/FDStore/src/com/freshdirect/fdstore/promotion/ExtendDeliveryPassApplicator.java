package com.freshdirect.fdstore.promotion;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;

/**
 * Header-level percent off order subtotal.
 */
public class ExtendDeliveryPassApplicator implements PromotionApplicatorI {

	private final int extendDays;
	private DlvZoneStrategy zoneStrategy;

	/**
	 * @param percentOff between 0 and 1
	 */
	public ExtendDeliveryPassApplicator(int extendDays) {
		this.extendDays = extendDays;
	}


	public boolean apply(String promoCode, PromotionContextI context) {
		//If delivery zone strategy is applicable please evaluate before applying the promotion.
		int e = zoneStrategy != null ? zoneStrategy.evaluate(promoCode, context) : PromotionStrategyI.ALLOW;
		if(e == PromotionStrategyI.DENY) return false;
		
		if(context.getUser().getDeliveryPassStatus().equals(EnumDlvPassStatus.ACTIVE)){
			//Only if user has active delivery pass.
			context.getShoppingCart().setDlvPassExtendDays(extendDays);
			return true;
		}
		return false;
	}


	public int getExtendDays() {
		return extendDays;
	}
	public void setZoneStrategy(DlvZoneStrategy zoneStrategy) {
		this.zoneStrategy = zoneStrategy;
	}

	public DlvZoneStrategy getDlvZoneStrategy() {
		return this.zoneStrategy;
	}
}
