package com.freshdirect.routing.model;


public class AreaModel extends BaseModel implements IAreaModel  {
	
	private String areaCode;
	private double loadBalanceFactor;
	
	private String balanceBy;
	
	private boolean needsLoadBalance;
	
	private boolean isDepot;
	
	private boolean isActive;

	private double deliveryRate;
	
	private int preTripTime;
	
    private int postTripTime;
    
    private IRegionModel region;
    
    //private int maxStemTime;
	
    private String prefix;
    
    private String deliveryModel;
        
	public String getDeliveryModel() {
		return deliveryModel;
	}

	public void setDeliveryModel(String deliveryModel) {
		this.deliveryModel = deliveryModel;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getPreTripTime() {
		return preTripTime;
	}

	public void setPreTripTime(int preTripTime) {
		this.preTripTime = preTripTime;
	}

	public int getPostTripTime() {
		return postTripTime;
	}

	public void setPostTripTime(int postTripTime) {
		this.postTripTime = postTripTime;
	}

	public double getDeliveryRate() {
		return deliveryRate;
	}

	public void setDeliveryRate(double deliveryRate) {
		this.deliveryRate = deliveryRate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	
	/*public int getMaxStemTime() {
		return maxStemTime;
	}

	public void setMaxStemTime(int maxStemTime) {
		this.maxStemTime = maxStemTime;
	}*/

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
		return region != null ? region.isDepot() : false;
	}
	
	public String toString() {
		return areaCode+"|"+isActive+"|"+deliveryRate;
	}

	public IRegionModel getRegion() {
		return region;
	}

	public void setRegion(IRegionModel region) {
		this.region = region;
	}

	
}
