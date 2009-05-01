package com.freshdirect.fdstore.promotion;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;

public class RecommendedLineItemStrategy implements LineItemStrategyI {
	
	public int getPrecedence() {
		return 200;
	}

	public RecommendedLineItemStrategy(){
		
	}
	public int evaluate(FDCartLineI lineItem, String promotionCode, PromotionContextI context) {
		// TODO Auto-generated method stub
			String savingsId = lineItem.getSavingsId();
			boolean eligibleLine = false;
			if(savingsId != null) {
				PromoVariantModel eligiblePV = (PromoVariantModel) context.getUser().getPromoVariant(savingsId);
				eligibleLine = (eligiblePV != null && eligiblePV.getAssignedPromotion().getPromotionCode().equals(promotionCode));
				if(eligibleLine){
					return ALLOW;
				}
			} 
			if(!eligibleLine) {
				//If savingsId is null check if the variant id is not null.
				savingsId  = lineItem.getVariantId();
				PromoVariantModel eligiblePV = (PromoVariantModel) context.getUser().getPromoVariant(savingsId);
				eligibleLine = (eligiblePV != null && eligiblePV.getAssignedPromotion().getPromotionCode().equals(promotionCode));
				if(eligibleLine){
					return ALLOW;
				}
			}
		return DENY;
	}
}



