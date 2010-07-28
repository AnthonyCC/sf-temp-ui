package com.freshdirect.fdstore.promotion.management;

import com.freshdirect.fdstore.FDResourceException;

public class FDPromotionUtils {
	public static String generateCloneCode(String promotionCode) throws FDResourceException {
		if (!FDPromotionNewManager.lookupPromotion(promotionCode))
			return promotionCode;

		for (int i = 0; i < Math.pow(36, 15); i++) {
			String postFix = Integer.toString(i, 36);
			String candidate = promotionCode.substring(0, Math.min(promotionCode.length(), 16 - postFix.length() - 1));
			candidate += "_" + postFix;
			if (!FDPromotionNewManager.lookupPromotion(candidate))
				return candidate;
		}
		throw new FDResourceException("cannot generate clone code -- left out possibilities");
	}
}
