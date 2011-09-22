package com.freshdirect.fdstore.promotion;

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
		int skuCount = context.getShoppingCart().getSkuCount();
		System.out.println("=============Evaluating the SkuLimitStrategy, skuCount= " + skuCount + "|skuLimit=" + skuLimit);
		if(skuCount < skuLimit){
			return ALLOW;
		}		
		return DENY;
	}
}



