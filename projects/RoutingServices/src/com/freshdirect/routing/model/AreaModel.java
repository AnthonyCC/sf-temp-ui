package com.freshdirect.routing.model;

public class AreaModel extends BaseModel implements IAreaModel  {
	
	private String areaCode;
	private double loadBalanceFactor;
	
	private String balanceBy;
	
	private boolean needsLoadBalance;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getBalanceBy() {
		return balanceBy;
	}

	public void setBalanceBy(String balanceBy) {
		this.balanceBy = balanceBy;
	}

	public double getLoadBalanceFactor() {
		return loadBalanceFactor;
	}

	public void setLoadBalanceFactor(double loadBalanceFactor) {
		this.loadBalanceFactor = loadBalanceFactor;
	}

	public boolean getNeedsLoadBalance() {
		return needsLoadBalance;
	}

	public void setNeedsLoadBalance(boolean needsLoadBalance) {
		this.needsLoadBalance = needsLoadBalance;
	}
}
