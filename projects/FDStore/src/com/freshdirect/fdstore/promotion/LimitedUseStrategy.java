package com.freshdirect.fdstore.promotion;

public class LimitedUseStrategy implements PromotionStrategyI {

	private int maxUsage; //is per customer
	
	public LimitedUseStrategy(int maxUsage) {
		this.maxUsage = maxUsage;
	}


	public LimitedUseStrategy() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getMaxUsage() {
		return this.maxUsage;
	}

	@Override
	public int evaluate(String promotionCode, PromotionContextI context) {
		int usage = context.getPromotionUsageCount(promotionCode);
		if (usage >= this.maxUsage) {
			if(this.maxUsage == 1){
				context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.ERROR_USAGE_LIMIT_ONE_EXCEEDED.getErrorCode());
			}
			else{
				context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.ERROR_USAGE_LIMIT_MORE_EXCEEDED.getErrorCode());
			}
			return DENY;
		}		
		return ALLOW;
	}

	@Override
	public int getPrecedence() {
		return 10;
	}

	public String toString() {
		return "LimitedUseStrategy[" + this.maxUsage + "]";
	}

	@Override
	public boolean isStoreRequired() {
		return false;
	}


	public void setMaxUsage(int maxUsage) {
		this.maxUsage = maxUsage;
	}
}
