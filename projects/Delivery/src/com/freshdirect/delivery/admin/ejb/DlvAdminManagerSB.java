package com.freshdirect.delivery.admin.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.planning.DlvPlanModel;
import com.freshdirect.delivery.planning.DlvResourceModel;
import com.freshdirect.framework.core.PrimaryKey;

public interface DlvAdminManagerSB extends EJBObject {
	
	public void addPlan(DlvPlanModel planModel)  throws DlvResourceException, RemoteException;
	
	public List getPlansForRegion(String regionId) throws DlvResourceException, RemoteException;
	
	public void deletePlan(String planId) throws DlvResourceException, RemoteException;
	
	public DlvPlanModel getPlan(String planId) throws DlvResourceException, RemoteException;
	
	public List getResourcesForDateRange(String regionName, String zoneCode, Date[] days) throws DlvResourceException, RemoteException;
	
	public List getResourcesForDate(String regionName, String zoneCode, Date day) throws DlvResourceException, RemoteException;
	
	public Map getResourcesForRegionAndDateRange(String regionId, Date[] days) throws DlvResourceException, RemoteException;
	
	public DlvResourceModel getResourceById(String resourceId) throws DlvResourceException, RemoteException; 
	
	public void updatePlan(DlvPlanModel planModel) throws DlvResourceException, RemoteException;
	
	public PrimaryKey updatePlanningResource(String regionName, String zoneCode, DlvResourceModel resourceModel) throws DlvResourceException, RemoteException;
	
	public void updateZones(String regionName, List zones, Date startDate) throws DlvResourceException, RemoteException;
	
	public void updateChefsTableZone(String zoneCode,  boolean ctActive, int ctReleaseTime) throws DlvResourceException, RemoteException;
	
	public void updateZoneUnattendedDeliveryStatus(String zoneCode, boolean unattended) throws DlvResourceException, RemoteException;
	
	public void updateRegionData(String regionDataId, Date startDate, double dlvCharge) throws DlvResourceException, RemoteException;
	
	public List getEarlyWarningData(Date day) throws DlvResourceException, RemoteException;

}
