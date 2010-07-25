package com.freshdirect.fdstore.promotion;

import com.freshdirect.fdstore.FDStoreProperties;

public class MaxRedemptionStrategy implements PromotionStrategyI {
	
	private final int maxRedemptions;
	
	public MaxRedemptionStrategy(int maxRedemptions){
		this.maxRedemptions = maxRedemptions;
	}

	public int getPrecedence() {
		// TODO Auto-generated method stub
		return 30;
	}

	public int evaluate(String promotionCode, PromotionContextI context) {
		if(context.getUser().getAllAppliedPromos().contains(promotionCode)){
			//Promotion already applied. Give it to the customer.
			return ALLOW;
		} else {
			Integer redeemCount = PromotionFactory.getInstance().getRedemptions(promotionCode);
			if(redeemCount != null && redeemCount.intValue() < this.maxRedemptions)
				return ALLOW;
		}
		context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.ERROR_REDEMPTION_EXCEEDED.getErrorCode());
		return DENY;
	}
	
	public int getMaxRedemptions() {
		return this.maxRedemptions;
	}
}
