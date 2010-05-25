package com.freshdirect.transadmin.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;

import weblogic.auddi.util.Logger;

import com.freshdirect.content.attributes.AttributeComparator.Selected;
import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumBalanceBy;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.dao.ZoneExpansionDaoI;
import com.freshdirect.transadmin.datamanager.model.WorkTableModel;
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
import com.freshdirect.transadmin.web.util.ZoneWorkTableUtil;

public class DomainManagerImpl  
		extends BaseManagerImpl  implements DomainManagerI {
	
	private final static Category LOGGER = LoggerFactory.getInstance(DomainManagerImpl.class);
	
	private DomainManagerDaoI domainManagerDao = null;
	
	private ZoneExpansionDaoI zoneExpansionDao = null;		
	
	public ZoneExpansionDaoI getZoneExpansionDao() {	
		return zoneExpansionDao;
	}

	public void setZoneExpansionDao(ZoneExpansionDaoI zoneExpansionDao) {
		this.zoneExpansionDao = zoneExpansionDao;
	}

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
	
	public ErpTruckMasterInfo getERPTruck(String truckId)
	{
		return TransAdminCacheManager.getInstance().getTruckMasterInfo(truckId);
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
		Set truckNumbers = new TreeSet();
		Collection routes = getTrucks();
		Iterator iter = routes.iterator();
		while(iter.hasNext()){
			ErpTruckMasterInfo truckInfo = (ErpTruckMasterInfo) iter.next();
			truckNumbers.add(truckInfo.getTruckNumber());
		}
		return truckNumbers;
	}

	public Collection getActiveZones() {
		// TODO Auto-generated method stub
		return getDomainManagerDao().getActiveZones();
	}
	
	public void saveRouteNumberGroup(Map routeMapping) {
		getDomainManagerDao().saveRouteNumberGroup(routeMapping);
	}
	
	public Collection getRouteMapping(String routeDate, String routeId) {
		return getDomainManagerDao().getRouteMapping(routeDate, routeId);
	}
	
	public Collection getRouteMappingByCutOff(String routeDate, String cutOff) {
		return getDomainManagerDao().getRouteMappingByCutOff(routeDate, cutOff);
}

	public Collection getUPSRouteInfo(String routeDate) {
		// TODO Auto-generated method stub
		return getDomainManagerDao().getUPSRouteInfo(routeDate);
	}

	public Collection getEmployeeRole(String empId) {
		return getDomainManagerDao().getEmployeeRole(empId);
	}
	
	public Collection getZoneWorkTableInfo(String worktable, String regionId){		
		return this.compareAndaddZones(worktable, regionId);
	}
	
	public Collection checkPolygons(){
		return getZoneExpansionDao().checkPolygons();
	}
	
	public String getDeliveryCharge(String regionId){
		return getZoneExpansionDao().getDeliveryCharge(regionId);
	}
	
	private Collection compareAndaddZones(String worktable, String regionId){
	
		Set<WorkTableModel> workTableList =(Set)getZoneExpansionDao().getZoneWorkTableInfo(worktable);
		Set<WorkTableModel> zoneList =(Set)getZoneExpansionDao().getZoneRegionInfo(regionId);	
		Set result=new HashSet();	
		
		for(WorkTableModel model: workTableList){
			if(zoneList.contains(model)){
				model.setCommonInboth("X");
			}else{
				model.setNewZone("X");
			}			
			result.add(model);
		}
		
		for(WorkTableModel model: zoneList){
			if(workTableList.contains(model)){
				//model.setCommonInBoth(true);
			}else{
				model.setZoneTblOnly("X");
			    result.add(model);
			}
		}
		return result;
	}
	
	
	public void refreshDev(String worktable){		
		getZoneExpansionDao().refreshDev(worktable);
	}
	
	public void refreshProd(String worktable){		
		getZoneExpansionDao().refreshProd(worktable);
	}

	public void doZoneExpansion(String worktable, String zone[][], String regionId, String deliveryFee, String expansionType) throws DataAccessException{	
		if("rollout".equalsIgnoreCase(expansionType)){	
			if(zone.length > 0 && deliveryFee!=null){
				List selZoneList=new ArrayList();
				for(int i=0;i<zone.length;i++){
					String[] zoneSel=zone[i];
					selZoneList.add(zoneSel[0]);
				}

				Set<WorkTableModel> TotalList=(Set)this.compareAndaddZones(worktable, regionId);
				Set<WorkTableModel> workTblList=(Set)this.getOnlyWorkTableList(worktable, regionId);
				Set<WorkTableModel> commonList =(Set)this.getCommonList(worktable, regionId);
				Set<WorkTableModel> zonetblList=(Set)this.getOnlyZoneList(worktable, regionId);
				boolean newRegionId=false;

				for (Iterator iterator = selZoneList.iterator(); iterator.hasNext();) {
					String zoneCode = (String) iterator.next();
					boolean isMatched= false;
					//Iterate through worktable list and compare with this id
					for (Iterator iterator2 = workTblList.iterator(); iterator2.hasNext();) {
						WorkTableModel workTableModel = (WorkTableModel) iterator2.next();	
						if(workTableModel.getCode().equalsIgnoreCase(zoneCode)){
							isMatched = true;
							break;
						}
						}//End workTblList loop
						if(isMatched){
							if(!newRegionId){
								getZoneExpansionDao().insertNewRegionDataId(regionId, deliveryFee);
								LOGGER.debug("Inserting new Region_data_ID for "+regionId+" is successfull!!");
								newRegionId=true;
							}
							//execute the corresponding query
							getZoneExpansionDao().insertNewZone(worktable, regionId, zoneCode);
							LOGGER.debug(zoneCode + " is inserted into ZONE table.");
							getZoneExpansionDao().deleteFromZoneDesc(zoneCode);
							LOGGER.debug(zoneCode + " is deleted from ZONE_DESC");
							getZoneExpansionDao().insertIntoZoneDesc(zoneCode);
							LOGGER.debug(zoneCode + " is inserted into ZONE_DESC");
							getZoneExpansionDao().deleteFromTranspZone(zoneCode);
							LOGGER.debug(zoneCode + " is deleted from TRANSP_ZONE table");
							getZoneExpansionDao().insertIntoTranspZone(zoneCode, worktable);
							LOGGER.debug(zoneCode + " is inserted into TRANSP_ZONE table");							
						}else{
							//Get the next list and iterate through it as above.
							for (Iterator iterator2 = commonList.iterator(); iterator2.hasNext();) {		
								WorkTableModel workTableModel = (WorkTableModel) iterator2.next();	
								if(workTableModel.getCode().equals(zoneCode)){
									isMatched=true;
									break;
								}
							}//End commonList loop
							if(isMatched){
								if(!newRegionId){
									getZoneExpansionDao().insertNewRegionDataId(regionId, deliveryFee);
									LOGGER.debug("Inserting new Region_data_ID for "+regionId+" is successfull!!");
									newRegionId=true;
								}
								//execute the corresponding query
								getZoneExpansionDao().insertCommonZoneSelected(worktable, regionId, zoneCode);
								LOGGER.debug(zoneCode + " is inserted from WORKTABLE into ZONE (Common) table");			
							} else {
								//Get the next list and iterate through it as above.
								for (Iterator iterator2 = zonetblList.iterator(); iterator2.hasNext();) {
									WorkTableModel workTableModel = (WorkTableModel) iterator2.next();
									if(workTableModel.getCode().equals(zoneCode)){
										isMatched=true;
										break;
									}
								}//End zonetblList loop
									if (isMatched) {
										//discard the zone
									}
								}
							}
				}//end For(selZoneList) loop
				
				//Non selected zones.
				for (Iterator iterator = TotalList.iterator(); iterator.hasNext();) {
					WorkTableModel workTableModel = (WorkTableModel) iterator.next();
					boolean isMatched= false;
					for (Iterator itr = selZoneList.iterator(); itr.hasNext();) {
						String zoneCode = (String) itr.next();
						if(workTableModel.getCode().equals(zoneCode)){
							isMatched = true;
							break;	
						}
					}//End (selZoneList) loop	
					if(!isMatched && !workTblList.contains(workTableModel)){	
						if(!newRegionId){
							getZoneExpansionDao().insertNewRegionDataId(regionId, deliveryFee);
							LOGGER.debug("Inserting new Region_data_ID for "+regionId+" is successfull!!");
							newRegionId=true;
						}
						//execute queries for unchecked zones except New zones unchecked
 						getZoneExpansionDao().insertUncheckedZones(workTableModel.getCode(), regionId);
 						LOGGER.debug(workTableModel.getCode() + "is copied from ZONE table into ZONE table with new REGION_DATA_ID");			
					}
				}//end (TotalList) for loop
			}//End main if
			LOGGER.debug("8 Day rollout for zones chosen is successfull");
			getZoneExpansionDao().updateMultipleDays(regionId);
			
		}else if("zExpansion".equalsIgnoreCase(expansionType)){
				if(zone.length > 0){
					List selZoneList=new ArrayList();
					for(int i=0;i<zone.length;i++){
						String[] zoneSel=zone[i];
						selZoneList.add(zoneSel[0]);
					}
					Set<WorkTableModel> commonList =(Set)this.getCommonList(worktable, regionId);
					for (Iterator iterator = selZoneList.iterator(); iterator.hasNext();) {
						String zoneCode = (String) iterator.next();
						boolean isMatched= false;	
						for (Iterator itr = commonList.iterator(); itr.hasNext();) {
							WorkTableModel workTableModel = (WorkTableModel) itr.next();
							if(workTableModel.getCode().equals(zoneCode)){
								isMatched=true;
								break;
							}
						}
						if(isMatched){
							getZoneExpansionDao().doExpansion(worktable,regionId,zoneCode);
							LOGGER.debug("For >> "+zoneCode + "geoloc is copied from WORKTABLE table into ZONE table");
						}
					}
			        }//end if
				LOGGER.debug("Zone Expansion for zones chosen is successfull");
		}//end else if block
	}
	
	public Collection getCommonList(String worktable, String regionId){
		Set<WorkTableModel> workTableList =(Set)getZoneExpansionDao().getZoneWorkTableInfo(worktable);
		Set<WorkTableModel> zoneList =(Set)getZoneExpansionDao().getZoneRegionInfo(regionId);
		Set commonList=new HashSet();
		for(WorkTableModel model: workTableList){
			if(zoneList.contains(model)){
				//model.setCommonInBoth(true);
				model.setCommonInboth("X");
				commonList.add(model);
			}
		}
		return commonList;
	}

	private Collection getOnlyWorkTableList(String worktable, String regionId){
		Set<WorkTableModel> workTableList =(Set)getZoneExpansionDao().getZoneWorkTableInfo(worktable);
		Set<WorkTableModel> zoneList =(Set)getZoneExpansionDao().getZoneRegionInfo(regionId);
		Set workTblList=new HashSet();
		for(WorkTableModel model: workTableList){
			if(zoneList.contains(model)){
			
			}else{
				model.setNewZone("X");
				workTblList.add(model);	
			}
		}
		return workTblList;
	}

	private Collection getOnlyZoneList(String worktable, String regionId){
		Set<WorkTableModel> workTableList =(Set)getZoneExpansionDao().getZoneWorkTableInfo(worktable);
		Set<WorkTableModel> zoneList =(Set)getZoneExpansionDao().getZoneRegionInfo(regionId);
		Set zoneTblList=new HashSet();	

		for(WorkTableModel model: zoneList){
			if(workTableList.contains(model)){
				//model.setCommonInBoth(true);
			}else{
				model.setZoneTblOnly("X");
				zoneTblList.add(model);
			}
		}
		return zoneTblList;
	}

	public void rollbackTimeslots(String zone[][]){

		if(zone.length > 0){
			List selZoneList=new ArrayList();
			for(int i=0;i<zone.length;i++){
				String[] zoneSel=zone[i];
				selZoneList.add(zoneSel[0]);
			}

			for (Iterator iterator = selZoneList.iterator(); iterator.hasNext();) {
				String zoneCode = (String) iterator.next();
				//Avoid duplicate timslot
				getZoneExpansionDao().deleteTimeslot(zoneCode);
				LOGGER.debug("PRIOR TIMESLOTS for  "+zoneCode+" are deleted");
				getZoneExpansionDao().deleteTrunkResource(zoneCode);				
				getZoneExpansionDao().deletePlanningResource(zoneCode);
				//Rollback timeslot
				getZoneExpansionDao().updatePlanningResource(zoneCode);
				getZoneExpansionDao().updateTimeslot(zoneCode);
				LOGGER.debug("ROLLING BACK timeslots to current date for "+zoneCode+" is Successfull");
			}
		}	
	}
	
	public List getStartDateForRegion(String regionId){
		 return getZoneExpansionDao().getStartDateForRegion(regionId);
	}
	
	public void updateStartDate(String regionId){
		 getZoneExpansionDao().updateStartDate(regionId);
	}
	
	public void makeDevLive(String regionId){
		 getZoneExpansionDao().makeDevLive(regionId);
		 LOGGER.debug("Making DEV live for testing!!");
	}
	
	
	//Geo Restrictions
	
	public Collection getGeoRestrictions(){
		
		Set<WorkTableModel> workTableList =(Set)getZoneExpansionDao().getGeoRestrictionWorkTableInfo();
		Set<WorkTableModel> boudaryList =(Set)getZoneExpansionDao().getGeoRestrictionBoundaryInfo();	
		Set result=new HashSet();	
		
		for(WorkTableModel model: workTableList){
			if(boudaryList.contains(model)){
				model.setCommonInboth("X");
			}else{
				model.setNewRestriction("X");
			}			
			result.add(model);
		}
		
		for(WorkTableModel model: boudaryList){
			if(workTableList.contains(model)){
				//model.setCommonInboth("X");
			}else{
				model.setBoundaryTblOnly("X");
				result.add(model);
			}			
		}
		
		return result;
	}
	
	public void doGeoRestriction(String zone[][]) throws DataAccessException{	
			
			if(zone.length > 0){
				List selZoneList=new ArrayList();
				for(int i=0;i<zone.length;i++){
					String[] zoneSel=zone[i];
					selZoneList.add(zoneSel[0]);
				}

				Set<WorkTableModel> workTblList=(Set)this.getGeoRestrictionsWorkTableList();
				Set<WorkTableModel> commonList =(Set)this.getCommonGeoRestrictionsList();
				Set<WorkTableModel> zonetblList=(Set)this.getGeoRestrictionsBoundaryList();
				
				for (Iterator iterator = selZoneList.iterator(); iterator.hasNext();) {
					String zoneCode = (String) iterator.next();
					boolean isMatched= false;
					//Iterate through worktable list and compare with this id
					for (Iterator iterator2 = workTblList.iterator(); iterator2.hasNext();) {
						WorkTableModel workTableModel = (WorkTableModel) iterator2.next();	
						if(workTableModel.getCode().equalsIgnoreCase(zoneCode)){
							isMatched = true;
							break;
						}
						}//End workTblList loop
						if(isMatched){
							
							//execute the corresponding query
							getZoneExpansionDao().insertNewGeoRestriction(zoneCode);
							LOGGER.debug("Inserting new new geoRestriction for "+zoneCode+" is successfull!!");
						}else{
							//Get the next list and iterate through it as above.
							for (Iterator iterator2 = commonList.iterator(); iterator2.hasNext();) {		
								WorkTableModel workTableModel = (WorkTableModel) iterator2.next();	
								if(workTableModel.getCode().equals(zoneCode)){
									isMatched=true;
									break;
								}
							}//End commonList loop
							if(isMatched){
								
								//execute the corresponding query
								getZoneExpansionDao().updateGeoRestriction(zoneCode);
								LOGGER.debug("Updating new goeRestriction for "+zoneCode+" is successfull!!");
							} else {
								//Get the next list and iterate through it as above.
								for (Iterator iterator2 = zonetblList.iterator(); iterator2.hasNext();) {
									WorkTableModel workTableModel = (WorkTableModel) iterator2.next();
									if(workTableModel.getCode().equals(zoneCode)){
										isMatched=true;
										break;
									}
								}//End zonetblList loop
									if (isMatched) {
										//discard the zone
									}
								}
						}//end else block
				}//end For(selZoneList) loop
				LOGGER.debug("Adding or updating Geo Restrictions are successfull");
			}//End main if
    }
	
	
	private Collection getCommonGeoRestrictionsList(){
		Set<WorkTableModel> workTableList =(Set)getZoneExpansionDao().getGeoRestrictionWorkTableInfo();
		Set<WorkTableModel> boudaryList =(Set)getZoneExpansionDao().getGeoRestrictionBoundaryInfo();	
		Set commonList=new HashSet();
		for(WorkTableModel model: workTableList){
			if(boudaryList.contains(model)){
				//model.setCommonInBoth(true);
				model.setCommonInboth("X");
				commonList.add(model);
			}
		}
		return commonList;
	}

	private Collection getGeoRestrictionsWorkTableList(){
		Set<WorkTableModel> workTableList =(Set)getZoneExpansionDao().getGeoRestrictionWorkTableInfo();
		Set<WorkTableModel> boudaryList =(Set)getZoneExpansionDao().getGeoRestrictionBoundaryInfo();	
		
		Set workTblList=new HashSet();
		for(WorkTableModel model: workTableList){
			if(boudaryList.contains(model)){
			
			}else{
				model.setNewRestriction("X");
				workTblList.add(model);	
			}
		}
		return workTblList;
	}

	private Collection getGeoRestrictionsBoundaryList(){
		Set<WorkTableModel> workTableList =(Set)getZoneExpansionDao().getGeoRestrictionWorkTableInfo();
		Set<WorkTableModel> boudaryList =(Set)getZoneExpansionDao().getGeoRestrictionBoundaryInfo();	
		
		Set zoneTblList=new HashSet();	

		for(WorkTableModel model: boudaryList){
			if(workTableList.contains(model)){
				//model.setCommonInBoth(true);
			}else{
				model.setBoundaryTblOnly("X");
				zoneTblList.add(model);
			}
		}
		return zoneTblList;
	}
	
	public void refreshGeoRestrictionWorktable(){
		String worktable="DLV.GEO_RESTRICTION_WORKTAB";
		getZoneExpansionDao().refreshGeoRestrictionWorktable(worktable);
	}

	
}

