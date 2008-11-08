package com.freshdirect.transadmin.model;

import java.util.Date;

import com.freshdirect.framework.util.DateUtil;

public class TrnRouteNumberId implements java.io.Serializable {

	private Date routeDate;

	private String cutOffId;
	
	private String areaCode;

	public TrnRouteNumberId() {
	}

	public TrnRouteNumberId(Date routeDate, String cutOffId, String areaCode) {
		this.routeDate = routeDate;
		this.cutOffId = cutOffId;
		this.areaCode = areaCode;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TrnRouteNumberId))
			return false;
		TrnRouteNumberId castOther = (TrnRouteNumberId) other;

		return ((this.getAreaCode() == castOther.getAreaCode()) 
						|| (this.getAreaCode() != null && castOther.getAreaCode() != null 
										&& this.getAreaCode().equals(castOther.getAreaCode())))
				&& ((this.getCutOffId() == castOther.getCutOffId()) 
						|| (this.getCutOffId() != null	&& castOther.getCutOffId() != null 
								&& this.getCutOffId().equals(castOther.getCutOffId())))
				&& ((this.getRouteDate() == castOther.getRouteDate()) 
						|| (this.getRouteDate() != null	&& castOther.getRouteDate() != null 
								&& DateUtil.truncate(this.getRouteDate()).equals(DateUtil.truncate(castOther.getRouteDate()))));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getAreaCode() == null ? 0 : this.getAreaCode()
						.hashCode());
		result = 37
				* result
				+ (getCutOffId() == null ? 0 : this.getCutOffId()
						.hashCode());
		
		result = 37
		* result
		+ (getRouteDate() == null ? 0 : this.getRouteDate()
				.hashCode());
		return result;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCutOffId() {
		return cutOffId;
	}

	public void setCutOffId(String cutOffId) {
		this.cutOffId = cutOffId;
	}

	public Date getRouteDate() {
		return routeDate;
	}

	public void setRouteDate(Date routeDate) {
		this.routeDate = routeDate;
	}

	public String toString() {
		return routeDate.toString()+areaCode+cutOffId;
	}

}