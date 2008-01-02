package com.freshdirect.fdstore.promotion;

import java.util.Set;

/**
 * Strategy to restrict usage to a single order (for any cust). Keeps set of saleIds where it was applied before. 
 */
public class UniqueUseStrategy implements PromotionStrategyI {

	private final Set usedSaleIds;

	public UniqueUseStrategy(Set usedSaleIds) {
		this.usedSaleIds = usedSaleIds;
	}

	public Set getUsedSaleIds() {
		return this.usedSaleIds;
	}

	public int evaluate(String promotionCode, PromotionContextI context) {
		System.out.println("Inside unique use evaluate ");
		System.out.println("usedSaleIds "+usedSaleIds);
		if (usedSaleIds.isEmpty()) {
			return ALLOW;
		}

		// also allow if modifying that order
		String saleId = context.getModifiedSaleId();
		System.out.println("Modify Sale id "+saleId);
		if (saleId != null) {
			return usedSaleIds.contains(saleId) ? ALLOW : DENY;
		}

		return DENY;
	}

	public int getPrecedence() {
		return 1000;
	}

	public String toString() {
		return "UniqueUseStrategy[" + this.usedSaleIds + "]";
	}

}
