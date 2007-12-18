package com.freshdirect.delivery.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.planning.DlvPlanModel;
import com.freshdirect.delivery.planning.DlvResourceModel;
import com.freshdirect.framework.core.PrimaryKey;

public interface DlvAdminI {
	
	public void addPlan(DlvPlanModel planModel) throws DlvResourceException;
	
	public List getPlansForRegion(String regionId) throws DlvResourceException;
	
	public void deletePlan(String planId) throws DlvResourceException;
	
	public DlvPlanModel getPlan(String planId) throws DlvResourceException;
	
	public List getResourcesForDateRange(String regionName, String zoneCode, Date[] days) throws DlvResourceException;
	
	public List getResourcesForDate(String regionName, String zoneCode, Date day) throws DlvResourceException;
	
	public Map getResourcesForRegionAndDateRange(String regionId, Date[] days) throws DlvResourceException;
	
	public DlvResourceModel getResourceById(String resourceId) throws DlvResourceException;
	
	public void updatePlan(DlvPlanModel planModel) throws DlvResourceException;
	
	public PrimaryKey updatePlanningResource(String regionName, String zoneCode, DlvResourceModel resourceModel) throws DlvResourceException;
	
	public void updateZones(String regionName, List zones, Date startDate) throws DlvResourceException;
	
	public void updateChefsTableZone(String zoneCode, boolean ctActive, int ctReleaseTime) throws DlvResourceException;
	
	public void updateZoneUnattendedDeliveryStatus(String zoneCode, boolean unattended) throws DlvResourceException;
	
	public void updateRegionData(String regionDataId, Date startDate, double dlvCharge) throws DlvResourceException;
	
	public List getMapLayersForRegion() throws DlvResourceException;
	
	public List getEarlyWarningData(Date day) throws DlvResourceException;
	
}
