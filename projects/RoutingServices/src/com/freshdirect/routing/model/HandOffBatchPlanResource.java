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
}
