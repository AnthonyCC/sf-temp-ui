package com.freshdirect.fdstore.promotion;

public class SignupDiscountRule extends HeaderDiscountRule {
	private double maxAmountPerSku;

	public SignupDiscountRule(double minSubtotal, double maxAmount, double maxAmountPerSku) {
		super(minSubtotal, maxAmount);
		this.maxAmountPerSku = maxAmountPerSku;
	}

	public SignupDiscountRule() {
		super();
		// TODO Auto-generated constructor stub
	}

	public double getMaxAmountPerSku() {
		return maxAmountPerSku;
	}

	public void setMaxAmountPerSku(double maxAmountPerSku) {
		this.maxAmountPerSku = maxAmountPerSku;
	}

	public String toString() {
		return "SignupDiscountRule[minSubtotal="
			+ this.getMinSubtotal()
			+ " maxAmount="
			+ this.getMaxAmount()
			+ " maxAmountPerSku="
			+ this.maxAmountPerSku
			+ "]";
	}

}