package com.freshdirect.routing.model;

import java.util.Date;

public class BuildingOperationDetails extends BaseModel implements IBuildingOperationDetails, Comparable<BuildingOperationDetails>  {
	
	private String dayOfWeek;
	
	private Date bldgStartHour;
	private Date bldgEndHour;
	private Date serviceStartHour;
	private Date serviceEndHour;

	private String bldgComments;
	private String serviceComments;
	
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public Date getBldgStartHour() {
		return bldgStartHour;
	}
	public Date getBldgEndHour() {
		return bldgEndHour;
	}
	public Date getServiceStartHour() {
		return serviceStartHour;
	}
	public Date getServiceEndHour() {
		return serviceEndHour;
	}
	public String getBldgComments() {
		return bldgComments;
	}
	public String getServiceComments() {
		return serviceComments;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public void setBldgStartHour(Date bldgStartHour) {
		this.bldgStartHour = bldgStartHour;
	}
	public void setBldgEndHour(Date bldgEndHour) {
		this.bldgEndHour = bldgEndHour;
	}
	public void setServiceStartHour(Date serviceStartHour) {
		this.serviceStartHour = serviceStartHour;
	}
	public void setServiceEndHour(Date serviceEndHour) {
		this.serviceEndHour = serviceEndHour;
	}
	public void setBldgComments(String bldgComments) {
		this.bldgComments = bldgComments;
	}
	public void setServiceComments(String serviceComments) {
		this.serviceComments = serviceComments;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildingOperationDetails other = (BuildingOperationDetails) obj;
		if (dayOfWeek == null) {
			if (other.dayOfWeek != null)
				return false;
		} else if (!dayOfWeek.equals(other.dayOfWeek))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(BuildingOperationDetails o) {
		// TODO Auto-generated method stub
		return this.getDayOfWeek().compareTo(o.getDayOfWeek());
	}	
	
	
	
}
