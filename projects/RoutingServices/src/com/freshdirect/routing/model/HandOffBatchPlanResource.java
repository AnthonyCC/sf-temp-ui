package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingStringUtil;

public class HandOffBatchPlanResource implements IHandOffBatchDispatchResource  {
	
	private String planId;
	private String resourceId;
	private String employeeRoleType;
	private Date adjustmentTime;
		
	public HandOffBatchPlanResource(String planId, String resourceId,
			String employeeRoleType, Date adjustmentTime) {
		super();
		this.planId = planId;
		this.resourceId = resourceId;
		this.employeeRoleType = employeeRoleType;
		this.adjustmentTime = adjustmentTime;
	}
	public HandOffBatchPlanResource() {
		// TODO Auto-generated constructor stub
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
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
		HandOffBatchPlanResource other = (HandOffBatchPlanResource) obj;
		if (resourceId == null) {
			if (other.resourceId != null)
				return false;
		} else if (!resourceId.equals(other.resourceId))
			return false;
		return true;
	}
}
