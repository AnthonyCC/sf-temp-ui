package com.freshdirect.routing.model;

import java.util.Date;

public class HandOffDispatchResource implements IHandOffDispatchResource  {
	
	private String dispatchId;
	private String resourceId;
	private String employeeRoleType;
	private Date adjustmentTime;
		
	public HandOffDispatchResource(String dispatchId, String resourceId,
			String employeeRoleType, Date adjustmentTime) {
		super();
		this.dispatchId = dispatchId;
		this.resourceId = resourceId;
		this.employeeRoleType = employeeRoleType;
		this.adjustmentTime = adjustmentTime;
	}
	public HandOffDispatchResource() {
		// TODO Auto-generated constructor stub
	}
	public String getDispatchId() {
		return dispatchId;
	}
	public void setDispatchId(String dispatchId) {
		this.dispatchId = dispatchId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getEmployeeRoleType() {
		return employeeRoleType;
	}
	public void setEmployeeRoleType(String employeeRoleType) {
		this.employeeRoleType = employeeRoleType;
	}
	public Date getAdjustmentTime() {
		return adjustmentTime;
	}
	public void setAdjustmentTime(Date adjustmentTime) {
		this.adjustmentTime = adjustmentTime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dispatchId == null) ? 0 : dispatchId.hashCode());
		result = prime * result
				+ ((resourceId == null) ? 0 : resourceId.hashCode());
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
		HandOffDispatchResource other = (HandOffDispatchResource) obj;
		if (dispatchId == null) {
			if (other.dispatchId != null)
				return false;
		} else if (!dispatchId.equals(other.dispatchId))
			return false;
		if (resourceId == null) {
			if (other.resourceId != null)
				return false;
		} else if (!resourceId.equals(other.resourceId))
			return false;
		return true;
	}
}
