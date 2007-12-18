package com.freshdirect.fdstore.promotion;

public class RedemptionCodeStrategy implements PromotionStrategyI {

	private String redemptionCode;

	public RedemptionCodeStrategy(String redemptionCode) {
		this.redemptionCode = redemptionCode;
	}

	public String getRedemptionCode() {
		return this.redemptionCode;
	}

	public int evaluate(String promotionCode, PromotionContextI context) {
		PromotionI redeemedPromo = context.getRedeemedPromotion();
		if (redeemedPromo == null || !promotionCode.equals(redeemedPromo.getPromotionCode())) {
			return DENY;
		}

		return ALLOW;
	}

	public int getPrecedence() {
		return 500;
	}

	public String toString() {
		return "RedemptionCodeStrategy[" + this.redemptionCode + "]";
	}

}
