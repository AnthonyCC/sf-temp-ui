package com.freshdirect.fdstore.ecoupon.model;

import java.io.Serializable;

public class FDCouponUPCInfo implements Serializable, Comparable<FDCouponUPCInfo>{

	private String fdCouponId;
	private String couponId;
	private String upc;
	private String upcDescription;
	private boolean isRequired;
	public String getFdCouponId() {
		return fdCouponId;
	}
	public void setFdCouponId(String fdCouponId) {
		this.fdCouponId = fdCouponId;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public String getUpcDescription() {
		return upcDescription;
	}
	public void setUpcDescription(String upcDescription) {
		this.upcDescription = upcDescription;
	}
	public boolean isRequired() {
		return isRequired;
	}
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	@Override
	public int compareTo(FDCouponUPCInfo couponUpcInfo) {
		if(!this.equals(couponUpcInfo)){
			return couponUpcInfo.getFdCouponId().compareTo(this.getFdCouponId());
		}
		return 0;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((couponId == null) ? 0 : couponId.hashCode());
		result = prime * result
				+ ((fdCouponId == null) ? 0 : fdCouponId.hashCode());
		result = prime * result + ((upc == null) ? 0 : upc.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FDCouponUPCInfo other = (FDCouponUPCInfo) obj;
		if (couponId == null) {
			if (other.couponId != null)
				return false;
		} else if (!couponId.equals(other.couponId))
			return false;
		if (fdCouponId == null) {
			if (other.fdCouponId != null)
				return false;
		} else if (!fdCouponId.equals(other.fdCouponId))
			return false;
		if (upc == null) {
			if (other.upc != null)
				return false;
		} else if (!upc.equals(other.upc))
			return false;		
		return true;
	}
	@Override
	public String toString() {
		return "FDCouponUPCInfo [fdCouponId=" + fdCouponId + ", couponId="
				+ couponId + ", upc=" + upc + ", upcDescription="
				+ upcDescription + ", isRequired=" + isRequired + "]";
	}
	
	
}
