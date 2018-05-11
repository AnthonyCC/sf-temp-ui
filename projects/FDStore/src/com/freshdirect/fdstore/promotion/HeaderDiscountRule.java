package com.freshdirect.fdstore.promotion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.promotion.management.FDPromoDollarDiscount;

public class HeaderDiscountRule implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private double minSubtotal;
	private double maxAmount;
	private List<FDPromoDollarDiscount> dollarList = new ArrayList<FDPromoDollarDiscount>();

	public HeaderDiscountRule(double minSubtotal, double maxAmount) {
		this.minSubtotal = minSubtotal;
		this.maxAmount = maxAmount;
	}
	
	public HeaderDiscountRule(List<FDPromoDollarDiscount> dollarList) {
		this.dollarList = dollarList;
	}

	public HeaderDiscountRule() {
		super();
		// TODO Auto-generated constructor stub
	}

	public double getMinSubtotal() {
		return minSubtotal;
	}

	public double getMaxAmount() {
		return maxAmount;
	}
	
	public List<FDPromoDollarDiscount> getDollarList() {
		return dollarList;
	}

	public void setMinSubtotal(double minSubtotal) {
		this.minSubtotal = minSubtotal;
	}

	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
	}

	public void setDollarList(List<FDPromoDollarDiscount> dollarList) {
		this.dollarList = dollarList;
	}

	public String toString() {
		return "DiscountRule[minSubtotal=" + this.getMinSubtotal() + " maxAmount=" + this.getMaxAmount() + "]";
	}

}
