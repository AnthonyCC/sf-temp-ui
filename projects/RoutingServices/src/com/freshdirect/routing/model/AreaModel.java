package com.freshdirect.routing.model;

public class AreaModel extends BaseModel implements IAreaModel  {
	
	private String areaCode;
	private double loadBalanceFactor;
	
	private String balanceBy;
	
	private boolean needsLoadBalance;
	
	private boolean isDepot;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((areaCode == null) ? 0 : areaCode.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AreaModel other = (AreaModel) obj;
		if (areaCode == null) {
			if (other.areaCode != null)
				return false;
		} else if (!areaCode.equals(other.areaCode))
			return false;
		return true;
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

	public boolean isDepot() {
		return isDepot;
	}

	public void setDepot(boolean isDepot) {
		this.isDepot = isDepot;
	}

	
}
