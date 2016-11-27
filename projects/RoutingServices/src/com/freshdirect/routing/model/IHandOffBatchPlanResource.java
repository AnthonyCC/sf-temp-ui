package com.freshdirect.routing.model;

import java.util.Date;

public interface IHandOffBatchPlanResource {	

	 String getPlanId();
	 void setPlanId(String planId);
	 String getResourceId();
	 void setResourceId(String resourceId);
	 String getEmployeeRoleType();
	 void setEmployeeRoleType(String employeeRoleType);
	 Date getAdjustmentTime();
	 void setAdjustmentTime(Date adjustmentTime);
		
}
