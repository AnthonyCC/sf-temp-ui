package com.freshdirect.fdstore.promotion;

public class ProfileAttributeStrategy implements PromotionStrategyI {

	private String attributeName;
	private String desiredValue;

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setDesiredValue(String desiredValue) {
		this.desiredValue = desiredValue;
	}

	public String getDesiredValue() {
		return this.desiredValue;
	}

	public int evaluate(String promotionCode, PromotionContextI context) {

		if (attributeName == null) {
			return DENY;
		}

		if (context.hasProfileAttribute(attributeName, desiredValue)) {
			return ALLOW;
		}

		return DENY;
	}

	public int getPrecedence() {
		return 1000;
	}

	public String toString() {
		return "ProfileAttributeStrategy[...]";
	}

}
