package com.freshdirect.fdstore.promotion;

import java.util.Set;

public class OrderTypeStrategy implements PromotionStrategyI {

	private final Set allowedOrderTypes;

	/**
	 * @param allowedOrderTypes Set of EnumOrderType
	 */
	public OrderTypeStrategy(Set allowedOrderTypes) {
		this.allowedOrderTypes = allowedOrderTypes;
	}

	public int evaluate(String promotionCode, PromotionContextI context) {
		return allowedOrderTypes.contains(context.getOrderType()) ? ALLOW : DENY;
	}

	public int getPrecedence() {
		return 200;
	}

	public String toString() {
		return "OrderTypeStrategy[" + allowedOrderTypes + "]";
	}

}