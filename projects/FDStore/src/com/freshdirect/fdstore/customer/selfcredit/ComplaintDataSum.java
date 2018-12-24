package com.freshdirect.fdstore.customer.selfcredit;

public class ComplaintDataSum {

	private String customerId;
	private Double amountSum;
	private int quantitySum;
	
	public ComplaintDataSum(String customerId) {
		this.customerId = customerId;
		this.amountSum = 0d;
		this.quantitySum = 0;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Double getAmountSum() {
		return amountSum;
	}

	public void setAmountSum(Double amountSum) {
		this.amountSum = amountSum;
	}

	public int getQuantitySum() {
		return quantitySum;
	}

	public void setQuantitySum(int quantitySum) {
		this.quantitySum = quantitySum;
	}

	public void increaseQuantity() {
		this.quantitySum++;
	}
	
	public void increaseAmount(Double amount) {
		this.amountSum += amount;
	}
}
