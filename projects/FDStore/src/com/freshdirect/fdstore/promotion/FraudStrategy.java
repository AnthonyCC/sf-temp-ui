package com.freshdirect.fdstore.promotion;

public class FraudStrategy implements PromotionStrategyI {

	public int getPrecedence() {
		return 110;
	}

	public int evaluate(String promotionCode, PromotionContextI context) {
		if(context.hasProfileAttribute("signup_promo_eligible", "deny")){
			return DENY;
		}
		
		return ALLOW;
	}
}
