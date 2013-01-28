package com.freshdirect.fdstore.promotion;

import java.util.Map;

import com.freshdirect.fdstore.customer.FDCartLineI;

public class SkuLimitStrategy implements LineItemStrategyI {
	
	private static final long serialVersionUID = 1L;
	private int skuLimit=0;
	
	public int getPrecedence() {
		return 100;
	}

	
	public SkuLimitStrategy(int skuLimit) { 
		this.skuLimit=skuLimit;
	}
	
	public int evaluate(FDCartLineI lineItem, String promotionCode, PromotionContextI context) {
		Map<String,Integer> skuCountMap = context.getShoppingCart().getSkuCount();
		Integer skuCount = skuCountMap.get(promotionCode);
		System.out.println("=============Evaluating the SkuLimitStrategy, skuCount= " + skuCount + "|skuLimit=" + skuLimit);
		if(null==skuCount || skuCount < skuLimit){
			return ALLOW;
		}		
		return DENY;
	}
}



