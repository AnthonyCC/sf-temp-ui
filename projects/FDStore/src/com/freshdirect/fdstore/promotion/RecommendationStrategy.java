package com.freshdirect.fdstore.promotion;

import java.util.Date;

import com.freshdirect.fdstore.FDStoreProperties;

public class RecommendationStrategy implements PromotionStrategyI {

	public int evaluate(String promotionCode, PromotionContextI context) {
		if (FDStoreProperties.isSmartSavingsEnabled()) {
			return ALLOW;
		}

		return DENY;
	}

	public int getPrecedence() {
		return 30;
	}


}
