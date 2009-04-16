package com.freshdirect.fdstore.promotion;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RecommendationStrategy implements PromotionStrategyI {

	public RecommendationStrategy(){
		
	}

	public int getPrecedence() {
		// TODO Auto-generated method stub
		return 30;
	}

	public int evaluate(String promotionCode, PromotionContextI context) {
		Map promoVariantMap = context.getPromoVariantMap();
		
		if(promoVariantMap != null && promoVariantMap.size() > 0){
			Set keys = promoVariantMap.keySet();
			for(Iterator iter = keys.iterator(); iter.hasNext();){
				PromoVariantModel pvModel = (PromoVariantModel) promoVariantMap.get(iter.next());
				if(pvModel != null) {
					PromotionI promotion = pvModel.getAssignedPromotion();
					if(promotionCode.equals(promotion.getPromotionCode())) 
						return ALLOW;
				}
			}
		}
		return DENY;
	}

}
