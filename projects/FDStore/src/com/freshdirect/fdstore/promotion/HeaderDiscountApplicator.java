package com.freshdirect.fdstore.promotion;



import java.util.List;

import com.freshdirect.customer.ErpDiscountLineModel;


public class HeaderDiscountApplicator implements PromotionApplicatorI {

	private final HeaderDiscountRule discountRule;

	/**
	 * minSubTotal > amount
	 */
	public HeaderDiscountApplicator(HeaderDiscountRule discountRule) {
		this.discountRule = discountRule;
	}

	public boolean apply(String promoCode, PromotionContextI context) {
		if (context.getPreDeductionTotal() < this.discountRule.getMinSubtotal()) {
			return false;
		}
       
		double amount = Math.min(context.getPreDeductionTotal(), this.discountRule.getMaxAmount());
		PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
		if(!EnumPromotionType.WINDOW_STEERING.equals(promo.getPromotionType())) {
			return context.applyHeaderDiscount(promoCode, amount, EnumPromotionType.REDEMPTION);
		} else {
			//return context.applyHeaderDiscount(promoCode, amount, EnumPromotionType.WINDOW_STEERING);
			List<ErpDiscountLineModel>  discountModels=context.getUser().getShoppingCart().getDiscounts();
			for (ErpDiscountLineModel discountModel : discountModels) {
				if(promoCode.equals(discountModel.getDiscount().getPromotionCode()))
					return false;
			}
			return true; 
		}
	}

	public HeaderDiscountRule getDiscountRule() {
		return this.discountRule;
	}

	public String toString() {
		return "HeaderDiscountApplicator[" + this.discountRule + "]";
	}

}
