package com.freshdirect.transadmin.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.customer.ErpRouteMasterInfo;

import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.routing.constants.EnumBalanceBy;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.model.DispositionType;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.EnumCachedDataType;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransAdminCacheManager;

public class DomainManagerImpl  
		extends BaseManagerImpl  implements DomainManagerI {
	
	private DomainManagerDaoI domainManagerDao = null;
	
	public DomainManagerDaoI getDomainManagerDao() {
		return domainManagerDao;
	}

	public void setDomainManagerDao(DomainManagerDaoI domainManagerDao) {
		this.domainManagerDao = domainManagerDao;
	}
	
	protected BaseManagerDaoI getBaseManageDao() {
		return getDomainManagerDao();
	}


	public Collection getZones() {
		
		return getDomainManagerDao().getZones();
	}
	
	public Collection getAreas() {
		
		return getDomainManagerDao().getAreas();
	}
	
	public Collection getMarkedAreas() {
		return getDomainManagerDao().getMarkedAreas();
	}
	
	public Collection getAdHocRoutes() {
		
		return getDomainManagerDao().getAdHocRoutes();
	}
	
	public Collection getRouteForZone(String zoneId) {
		return getDomainManagerDao().getRouteForZone(zoneId);
	}
	
	public Collection getTrucks() {
		// get the data from CacheManager
		// if data does not exist then get the data from sap and add it in cache
		
		return TransAdminCacheManager.getInstance().getAllTruckMasterInfo(); 
	}
	

	
	public Collection getTimeSlots() {
		return getDomainManagerDao().getTimeSlots();
	}
	
	public String[] getDays() {
		return getDomainManagerDao().getDays();
	}
	
	public String[] getTimings() {
		return getDomainManagerDao().getTimings();
	}
	
	public String[] getTruckTypes() {
		return getDomainManagerDao().getTruckTypes();
	}
	
	public Collection getSupervisors() {
		return getDomainManagerDao().getSupervisors(); 
	}
		
	
	
	
	public Zone getZone(String id)  {
		return getDomainManagerDao().getZone(id);	
	}
	
	public TrnAdHocRoute getAdHocRoute(String id)  {
		return getDomainManagerDao().getAdHocRoute(id);	
	}
	
	public TrnTruck getTruck(String id)  {
		return getDomainManagerDao().getTruck(id);	
	}
	
	public TrnArea getArea(String id)  {
		return getDomainManagerDao().getArea(id);	
	}
	
	public Collection getZoneTypes() {
		return getDomainManagerDao().getZoneTypes();
	}
	
	
	public TrnZoneType getZoneType(String id) {
		return getDomainManagerDao().getZoneType(id);
	}
	
	public TrnCutOff getCutOff(String id) {
		return getDomainManagerDao().getCutOff(id);
	}
	
	public Collection getCutOffs() {
		return getDomainManagerDao().getCutOffs();
	}
	
	public Collection getRouteNumberGroup(String date, String cutOff, String area) {
		return getDomainManagerDao().getRouteNumberGroup(date, cutOff, area);
	}
	
	public Collection getDeliveryModels() {
		return getDomainManagerDao().getDeliveryModels();
	}

	public Collection getRoutes() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Collection getBalanceBys() {
		return EnumBalanceBy.getEnumList();
	}
		
	
	
	public Collection getZonetypeResources(String zoneTypeId) {
		return getDomainManagerDao().getZonetypeResources(zoneTypeId);
	}

	public Collection getRegions() {
		// TODO Auto-generated method stub
		return getDomainManagerDao().getRegions();
	}

	public Collection getDispositionTypes() {
		// TODO Auto-generated method stub
		return getDomainManagerDao().getDispositionTypes();
	}
	
	public DispositionType getDispositionType(String code) {
		return getDomainManagerDao().getDispositionType(code);
	}
	
	public Region getRegion(String code) {
		// TODO Auto-generated method stub
		return getDomainManagerDao().getRegion(code);
	}

	public void refreshCachedData(EnumCachedDataType dataType) {
		// TODO Auto-generated method stub
		TransAdminCacheManager.getInstance().refreshCacheData(dataType); 
		
	}

	public Collection getRoutes(String requestedDate) {
		// TODO Auto-generated method stub
		return TransAdminCacheManager.getInstance().getAllRouteMasterInfo(requestedDate); 
	}

	public Collection getEmployeeJobType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveZoneType(TrnZoneType zoneType) {
		getDomainManagerDao().saveZoneType(zoneType);
		
	}
	public FDRouteMasterInfo getRouteMasterInfo(String routeNo, Date requestedDate){
		ErpRouteMasterInfo routeInfo = TransAdminCacheManager.getInstance().getRouteMasterInfo(routeNo, requestedDate);
		if (routeInfo != null){
			return new FDRouteMasterInfo(routeInfo);
		}else {
			return null;
		}
		
	}

	public Collection getAllRoutes(String requestedDate) {
		
		Collection validRoutes= getRoutes(requestedDate);
		Collection adHocRoutes = getAdHocRoutes();
		return ModelUtil.mergeRoutes(validRoutes, adHocRoutes);
	}
	
	public Collection getTruckNumbers() {
		Set truckNumbers = new HashSet();
		Collection routes = getTrucks();
		Iterator iter = routes.iterator();
		while(iter.hasNext()){
			ErpTruckMasterInfo truckInfo = (ErpTruckMasterInfo) iter.next();
			truckNumbers.add(truckInfo.getTruckNumber());
		}
		return truckNumbers;
	} 
}
