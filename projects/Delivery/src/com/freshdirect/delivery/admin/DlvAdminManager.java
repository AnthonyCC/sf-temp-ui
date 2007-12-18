package com.freshdirect.delivery.admin;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.admin.ejb.DlvAdminManagerHome;
import com.freshdirect.delivery.admin.ejb.DlvAdminManagerSB;
import com.freshdirect.delivery.map.ejb.DlvMapperHome;
import com.freshdirect.delivery.map.ejb.DlvMapperSB;
import com.freshdirect.delivery.planning.DlvPlanModel;
import com.freshdirect.delivery.planning.DlvResourceModel;
import com.freshdirect.framework.core.PrimaryKey;

public class DlvAdminManager implements DlvAdminI {
	
	private final static DlvAdminManager instance = new DlvAdminManager();
	
	private DlvAdminManagerHome adminManagerHome = null;
	private DlvMapperHome mapperHome = null;
	
	/** Creates new DlvTemplateManager */
	private DlvAdminManager() {
		//private constructor so that we have a single instance of this class in jvm
	}
	
	public static DlvAdminI getInstance() {
		return instance;
		//return new DlvAdminManagerMock();
	}
	
	public void addPlan(DlvPlanModel planModel) throws DlvResourceException {
		
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			sb.addPlan(planModel);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot Create DlvAdminManagerSessionBean");
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot Talk to DlvAdminManagerSessionBean");
		}
	}
	
	public List getPlansForRegion(String regionId) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			return sb.getPlansForRegion(regionId);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot Create DlvAdminManagerSessionBean");
		}catch(RemoteException re){ 
			throw new DlvResourceException(re, "Cannot Talk to DlvAdminManagerSessionBean");
		}
	}
	
	public void deletePlan(String planId) throws DlvResourceException {
		
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			sb.deletePlan(planId);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot Create DlvAdminManagerSessionBean");
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot Talk to DlvAdminManagerSessionBean");
		}
	}
	
	public DlvPlanModel getPlan(String planId) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			return sb.getPlan(planId);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create DlvAdminManagerSessionBean");
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to DlvAdminManagerSessionBean");
		}
	}
	
	public void updatePlan(DlvPlanModel planModel) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = this.adminManagerHome.create();
			sb.updatePlan(planModel);
			
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create DlvAdminManagerSessionBean");
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to DlvAdminManagerSessionBean");
		}
	}
	
	public List getResourcesForDateRange(String regionName, String zoneCode, Date[] days) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			return sb.getResourcesForDateRange(regionName, zoneCode, days);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create DlvAdminManagerSessionBean");
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to DlvAdminManagerSessionBean");
		}
	}
	
	public Map getResourcesForRegionAndDateRange(String regionId, Date[] days) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			return sb.getResourcesForRegionAndDateRange(regionId, days);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create DlvAdminManagerSessionBean");
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to DlvAdminManagerSessionBean");
		}
	}
	
	public List getResourcesForDate(String regionName, String zoneCode, Date day) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			return sb.getResourcesForDate(regionName, zoneCode, day);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create DlvAdminManagerSessionBean");
		}catch(RemoteException re){
			re.printStackTrace();
			throw new DlvResourceException(re, "Cannot talk to DlvAdminManagerSessionBean");
		}

	}
	
	public DlvResourceModel getResourceById(String resourceId) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			return sb.getResourceById(resourceId);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create DlvAdminManagerSessionBean");
		}catch(RemoteException re){
			re.printStackTrace();
			throw new DlvResourceException(re, "Cannot talk to DlvAdminManagerSessionBean");
		}
	}
	
	public PrimaryKey updatePlanningResource(String regionName, String zoneCode, DlvResourceModel resourceModel) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			return sb.updatePlanningResource(regionName, zoneCode, resourceModel);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create DlvAdminManagerSessionBean"); 
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to DlvAdminManagerSessionBean");
		}
	}
	
	public void updateZones(String regionName, List zones, Date startDate) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			sb.updateZones(regionName, zones, startDate);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create DlvAdminManagerSessionBean"); 
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to DlvAdminManagerSessionBean");
		}
	}

	public void updateChefsTableZone(String zoneCode, boolean ctActive, int ctReleaseTime) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			sb.updateChefsTableZone(zoneCode, ctActive, ctReleaseTime);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create DlvAdminManagerSessionBean"); 
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to DlvAdminManagerSessionBean");
		}		
	}
	
	public void updateZoneUnattendedDeliveryStatus(String zoneCode, boolean unattended) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			sb.updateZoneUnattendedDeliveryStatus(zoneCode, unattended);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create DlvAdminManagerSessionBean"); 
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to DlvAdminManagerSessionBean");
		}		
	}
	
	public void updateRegionData(String regionDataId, Date startDate, double dlvCharge) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = adminManagerHome.create();
			sb.updateRegionData(regionDataId, startDate, dlvCharge);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create DlvAdminManagerSessionBean"); 
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to DlvAdminManagerSessionBean");
		}
	}
	
	public List getMapLayersForRegion() throws DlvResourceException {
		if(this.mapperHome == null){
			this.lookupMapperHome();
		}
		try{
			DlvMapperSB sb = this.mapperHome.create();
			return sb.getMapLayersForRegion();
		}catch(CreateException ce){
			throw new DlvResourceException(ce);
		}catch(RemoteException re){
			throw new DlvResourceException(re);
		}
	}
	
	public List getEarlyWarningData(Date day) throws DlvResourceException {
		if(this.adminManagerHome == null){
			this.lookupAdminManagerHome();
		}
		try{
			DlvAdminManagerSB sb = this.adminManagerHome.create();
			return sb.getEarlyWarningData(day);
		}catch(CreateException ce){
			throw new DlvResourceException(ce, "Cannot create ReportSessionBean");
		}catch(RemoteException re){
			re.printStackTrace();
			throw new DlvResourceException(re, "Cannot talk to ReportSessionBean");
		}
	}
	
	protected void lookupAdminManagerHome() throws DlvResourceException {
		Context ctx = null;
		try {
			ctx = DlvProperties.getInitialContext();
			this.adminManagerHome = (DlvAdminManagerHome) ctx.lookup( DlvProperties.getDlvAdminManagerHome() );
		} catch (NamingException ne) {
			throw new DlvResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {}
		}
	}
	
	protected void lookupMapperHome() throws DlvResourceException {
		Context ctx = null;
		try{
			ctx = DlvProperties.getInitialContext();
			this.mapperHome = (DlvMapperHome)ctx.lookup(DlvProperties.getMapperHome());
		}catch(NamingException ne) {
			throw new DlvResourceException(ne);
		}finally {
			try{
				ctx.close();
			}catch(NamingException e){}
		}
	}
	
}
