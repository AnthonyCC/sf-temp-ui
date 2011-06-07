package com.freshdirect.routing.model;

import java.util.Date;
import java.util.Set;

import com.freshdirect.sap.bapi.BapiSendHandOff;

public interface IHandOffBatchPlan {	

	public String getPlanId();

	public void setPlanId(String planId);

	public String getZoneCode();

	public void setZoneCode(String zoneCode);

	public String getRegion();

	public void setRegion(String region);

	public Date getPlanDate();

	public void setPlanDate(Date planDate);

	public Date getFirstDeliveryTime();

	public void setFirstDeliveryTime(Date firstDeliveryTime);

	public Date getStartTime();

	public void setStartTime(Date startTime);

	public int getSequence();

	public void setSequence(int sequence);
	
	public String getIsBullpen();

	public void setIsBullpen(String isBullpen);

	public Set getBatchPlanResources();

	public void setBatchPlanResources(Set batchPlanResources);

	public String getSupervisorId();

	public void setSupervisorId(String supervisorId);

	public String getUserId();

	public void setUserId(String userId);

	public Date getMaxTime();

	public void setMaxTime(Date maxTime);

	public String getIsOpen();

	public void setIsOpen(String isOpen);

	public Boolean getIsTeamOverride();

	public void setIsTeamOverride(Boolean isTeamOverride);

	public Date getLastDeliveryTime(); 

	public void setLastDeliveryTime(Date lastDeliveryTime); 

	public Date getCutOffTime();
	
	public void setCutOffTime(Date cutOffTime);
		
}
