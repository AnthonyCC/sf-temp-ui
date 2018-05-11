package com.freshdirect.fdstore.promotion;

public class ActiveInactiveStrategy implements PromotionStrategyI {
	
	private boolean active;
	
	public ActiveInactiveStrategy(boolean active){
		this.active = active;
	}

	public ActiveInactiveStrategy() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getPrecedence() {
		return 30;
	}

	@Override
	public int evaluate(String promotionCode, PromotionContextI context) {
		PromotionI promo = PromotionFactory.getInstance().getPromotion(promotionCode);
		
		if(active || (null != promo.getOfferType() && promo.getOfferType().equals(EnumOfferType.WINDOW_STEERING) && context.isAlreadyRedeemedPromotion(promotionCode))){
			return ALLOW;
		}
		return DENY;
	}
	
	public boolean isActive() {
		return this.active;
	}

	@Override
	public boolean isStoreRequired() {
		return false;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
