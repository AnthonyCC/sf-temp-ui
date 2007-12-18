package com.freshdirect.fdstore.promotion;

import java.io.Serializable;

public class HeaderDiscountRule implements Serializable {

	private final double minSubtotal;
	private final double maxAmount;

	public HeaderDiscountRule(double minSubtotal, double maxAmount) {
		this.minSubtotal = minSubtotal;
		this.maxAmount = maxAmount;
	}

	public double getMinSubtotal() {
		return minSubtotal;
	}

	public double getMaxAmount() {
		return maxAmount;
	}

	public String toString() {
		return "DiscountRule[minSubtotal=" + this.getMinSubtotal() + " maxAmount=" + this.getMaxAmount() + "]";
	}

}
