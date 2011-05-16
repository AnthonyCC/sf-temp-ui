package com.freshdirect.fdstore.promotion;

public class ActiveInactiveStrategy implements PromotionStrategyI {
	
	private final boolean active;
	
	public ActiveInactiveStrategy(boolean active){
		this.active = active;
	}

	public int getPrecedence() {
		// TODO Auto-generated method stub
		return 30;
	}

	public int evaluate(String promotionCode, PromotionContextI context) {
		PromotionI promo = PromotionFactory.getInstance().getPromotion(promotionCode);
		
		if(active || (promo.getOfferType().equals(EnumOfferType.WINDOW_STEERING) && context.isAlreadyRedeemedPromotion(promotionCode))){
			return ALLOW;
		}
		return DENY;
	}
	
	public boolean isActive() {
		return this.active;
	}
}
