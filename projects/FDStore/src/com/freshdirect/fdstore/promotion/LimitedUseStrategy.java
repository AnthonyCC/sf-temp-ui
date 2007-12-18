package com.freshdirect.fdstore.promotion;

public class LimitedUseStrategy implements PromotionStrategyI {

	private final int maxUsage;

	public LimitedUseStrategy(int maxUsage) {
		this.maxUsage = maxUsage;
	}

	public int getMaxUsage() {
		return this.maxUsage;
	}

	public int evaluate(String promotionCode, PromotionContextI context) {
		int usage = context.getPromotionUsageCount(promotionCode);
		if (usage >= this.maxUsage) {
			return DENY;
		}

		return ALLOW;
	}

	public int getPrecedence() {
		return 10;
	}

	public String toString() {
		return "LimitedUseStrategy[" + this.maxUsage + "]";
	}

}
