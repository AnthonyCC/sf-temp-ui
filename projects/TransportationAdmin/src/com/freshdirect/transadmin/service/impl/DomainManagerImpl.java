package com.freshdirect.transadmin.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumBalanceBy;
import com.freshdirect.transadmin.constants.EnumIssueStatus;
import com.freshdirect.transadmin.constants.EnumServiceStatus;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.dao.ZoneExpansionDaoI;
import com.freshdirect.transadmin.datamanager.model.WorkTableModel;
import com.freshdirect.transadmin.model.CapacitySnapshot;
import com.freshdirect.transadmin.model.CapacitySnapshotModel;
import com.freshdirect.transadmin.model.DispositionType;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.IssueLog;
import com.freshdirect.transadmin.model.IssueSubType;
import com.freshdirect.transadmin.model.IssueType;
import com.freshdirect.transadmin.model.MaintenanceIssue;
import com.freshdirect.transadmin.model.SectorZipcode;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
//import com.freshdirect.transadmin.model.TrnPlantCapacity;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.VIRRecord;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.comparator.AlphaNumericComparator;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.EnumCachedDataType;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransAdminCacheManager;
import com.freshdirect.transadmin.util.TransStringUtil;

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
		Collection adHocRoutes = getDomainManagerDao().getAdHocRoutes();
		Collections.sort((List)adHocRoutes, new AlphaNumericComparator());
		return adHocRoutes; 
	}
	
	public Collection getSnapshotLocations() {
		Collection<Object[]> locations = getDomainManagerDao().getSnapshotLocations();
		List<CapacitySnapshotModel> snapshotLocations = new ArrayList<CapacitySnapshotModel>();
		for(Object[] obj: locations)
		{
			if(obj!=null)
			{
				if(obj[0]!=null && obj[0] instanceof CapacitySnapshot && obj[1]!=null && obj[1] instanceof DlvBuilding)
				{
					CapacitySnapshotModel model = new CapacitySnapshotModel();
					
					CapacitySnapshot snapshot = (CapacitySnapshot)obj[0];
					DlvBuilding building = (DlvBuilding)obj[1];
					model.setId(snapshot.getBuildingId()+"$"+snapshot.getServicetype());
					model.setBuildingId(snapshot.getBuildingId());
					model.setServicetype(snapshot.getServicetype());
					model.setSrubbedStreet(building.getSrubbedStreet());
					model.setCity(building.getCity());
					model.setState(building.getState());
					model.setZip(building.getZip());
					model.setCountry(building.getCountry());
					snapshotLocations.add(model);
				}
				
				
			}
		}
		
		return snapshotLocations; 
	}
	
	
	public Map getTrucks() {
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
	
	public Zone getZone(String id)  {
		return getDomainManagerDao().getZone(id);	
	}
	
	public Map getZoneByIDs(Set ids)  {
		return getDomainManagerDao().findByIDs(ids);	
	}
	
	public TrnAdHocRoute getAdHocRoute(String id)  {
		return getDomainManagerDao().getAdHocRoute(id);	
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
	
/*	public TrnPlantCapacity getPlantCapacity(String id){
		return getDomainManagerDao().getPlantCapacity(id);
	}
	
	public Collection getPlantCapacities(Date dispatchDate) {
		return getDomainManagerDao().getPlantCapacities(dispatchDate);
	}*/
	
	public Collection getDeliveryModels() {
		return getDomainManagerDao().getDeliveryModels();
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
	
	public Collection getLightDutyRegions() {		
		return getDomainManagerDao().getLightDutyRegions();
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
		Map routes = getTrucks();
		truckNumbers = routes.keySet();
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
	public Collection checkGeoRestrictionPolygons(){
		return getZoneExpansionDao().checkGeoRestrictionPolygons();
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
 						LOGGER.debug(workTableModel.getCode() + " is copied from ZONE table to ZONE table with new REGION_DATA_ID");			
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
	
	public void updateDisassociatedTimeslots(){
		try{
			LOGGER.debug("checking disassociated timeslots if Any");
			getZoneExpansionDao().updateDisassociatedTimeslots();
			LOGGER.debug("Fixing disassociated timeslots completed!!");
		}catch(Exception e){
			e.printStackTrace();
		}
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
	
	
	public void saveScheduleGroup(Collection _masterSchedule, String[] employeeIds, Date weekOf) {
		
				
		String strWeekOf = getParsedDate(weekOf);
		if(employeeIds != null && _masterSchedule != null 
										&& _masterSchedule.size() > 0 && strWeekOf != null) {
			Collection<ScheduleEmployee> _clonedSchedule = new ArrayList<ScheduleEmployee>(); 
			Iterator _itr = _masterSchedule.iterator();
			while(_itr.hasNext()) {
				ScheduleEmployee _cloneSch = (ScheduleEmployee)_itr.next();
				try {
					_clonedSchedule.add((ScheduleEmployee)_cloneSch.clone());
				} catch (CloneNotSupportedException notSupported) {
					// No Supposed to Happen
					notSupported.printStackTrace();
				}
			}
			for(String empId : employeeIds) {
				Collection schedules = getDomainManagerDao().getScheduleEmployee(empId, strWeekOf);
				if(schedules != null && schedules.size() > 0) {
					this.removeEntity(schedules);
				}
				for(ScheduleEmployee _schEmp : _clonedSchedule) {
					_schEmp.setEmployeeId(empId);
					_schEmp.setScheduleId(null);
				}
				this.saveEntityList(_clonedSchedule);
			}
		}		
	}
	
	public void saveOrUpdateEmployeeSchedule(List<ScheduleEmployee> scheduleEmp) {
		List<ScheduleEmployee> sList=new ArrayList<ScheduleEmployee>();
		for (Iterator<ScheduleEmployee> iterator = scheduleEmp.iterator(); iterator.hasNext();) {
			ScheduleEmployee _scheduleEmp = iterator.next();
			if(_scheduleEmp.getDepotZone()!=null){
				int _codelength = _scheduleEmp.getDepotZone().getZoneCode().length();				
				if(_codelength < 3) {
					StringBuffer strBuf = new StringBuffer();
					while(3 - _codelength > 0) {
						strBuf.append("0");
						_codelength++;
					}
					_scheduleEmp.setDepotZoneS(strBuf.toString() + _scheduleEmp.getDepotZoneS());
				}
			}
		    _scheduleEmp.setWeekOf(TransStringUtil.getWeekOf(_scheduleEmp.getDate()));
		   
			if(_scheduleEmp != null){
				String empId = _scheduleEmp.getEmployeeId();
				Date weekOf = TransStringUtil.getWeekOf(_scheduleEmp.getDate());
				String day = null;
				String strWeekOf = getParsedDate(weekOf);
				String DAY[] = new String[] { "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN" };
				try{
					day = TransStringUtil.getDayofWeek(_scheduleEmp.getDate());
					if ("Monday".equalsIgnoreCase(day))
						day = DAY[0];
					else if ("Tuesday".equalsIgnoreCase(day))
						day = DAY[1];
					else if ("Wednesday".equalsIgnoreCase(day))
						day = DAY[2];
					else if ("Thursday".equalsIgnoreCase(day))
						day = DAY[3];
					else if ("Friday".equalsIgnoreCase(day))
						day = DAY[4];
					else if ("Saturday".equalsIgnoreCase(day))
						day = DAY[5];
					else if ("Sunday".equalsIgnoreCase(day))
						day = DAY[6];
					 _scheduleEmp.setDay(day);
					if(strWeekOf!=null && day!= null){
						Collection schedules = getDomainManagerDao().getScheduleEmployee(empId, strWeekOf, day);
						if(schedules != null && schedules.size() > 0) {
							this.removeEntity(schedules);
						}
						sList.add(_scheduleEmp);						
					}				
				}catch(ParseException ex){
					ex.printStackTrace();
				}
			 }	
		}
		if(sList.size()>0){
			getDomainManagerDao().saveEntityList(sList);
		}
	}
	
	
	public void copyScheduleGroup(String[] employeeIds, Date sourceWeekOf, Date destinationWeekOf, String day) {
		
		Collection<ScheduleEmployee> _toDeleteSchedule = new ArrayList<ScheduleEmployee>();
		Collection<ScheduleEmployee> _toSaveSchedule = new ArrayList<ScheduleEmployee>();
		
		String strSourceWeekOf = getParsedDate(sourceWeekOf);
		String strDestinationWeekOf = getParsedDate(destinationWeekOf);
		if(employeeIds != null) {
			for(String empId : employeeIds) {
				if(day != null && !day.equalsIgnoreCase("ALL")) {
					_toDeleteSchedule.addAll(getDomainManagerDao().getScheduleEmployee(empId, strDestinationWeekOf, day));
				} else {
					_toDeleteSchedule.addAll(getDomainManagerDao().getScheduleEmployee(empId, strDestinationWeekOf));
				}
			}
			if(_toDeleteSchedule.size() > 0) {
				this.removeEntity(_toDeleteSchedule);
			}
			for(String empId : employeeIds) {
				Collection<ScheduleEmployee> _masterSchedule = null;getDomainManagerDao().getScheduleEmployee(empId, strSourceWeekOf);
				if(day != null && !day.equalsIgnoreCase("ALL")) {
					_masterSchedule = getDomainManagerDao().getScheduleEmployee(empId, strSourceWeekOf, day);
				} else {
					_masterSchedule = getDomainManagerDao().getScheduleEmployee(empId, strSourceWeekOf);
				}
				ScheduleEmployee _tmpSchedule = null;
				if(_masterSchedule != null) {
					for(ScheduleEmployee _cloneSch : _masterSchedule) {
						try {
							_tmpSchedule = (ScheduleEmployee)_cloneSch.clone();
							_tmpSchedule.setWeekOf(destinationWeekOf);
							_tmpSchedule.setScheduleId(null);
							_toSaveSchedule.add(_tmpSchedule);
							
						} catch (CloneNotSupportedException notSupported) {
							// No Supposed to Happen
						}
					}
				}
			}
			if(_toSaveSchedule.size() > 0) {
				this.saveEntityList(_toSaveSchedule);
			}
		}
	}
	
	public Collection getScheduleEmployee(String employeeId, String weekOf, String day) throws DataAccessException {
		return getDomainManagerDao().getScheduleEmployee(employeeId, weekOf, day);
	}
	
	public Collection getScheduleEmployee(String employeeId, String weekOf) throws DataAccessException {
		return getDomainManagerDao().getScheduleEmployee(employeeId, weekOf);
	}
	
	public Collection getTeamInfo() {
		return getDomainManagerDao().getTeamInfo();
	}
	
	public Collection getDeliveryGroups(){
		return getDomainManagerDao().getDeliveryGroups();
	}
	
	public Collection getIssueTypes(){
		return getDomainManagerDao().getIssueTypes();
	}
	
	public IssueType getIssueType(String name){
		return getDomainManagerDao().getIssueType(name);
	}
	
	public IssueType getIssueTypeById(String id){
		return getDomainManagerDao().getIssueTypeById(id);
	}
	
	public IssueSubType getIssueSubType(String issueSubTypeName){
		return getDomainManagerDao().getIssueSubType(issueSubTypeName);
	}
	
	public Collection getIssueSubTypes(){
		return getDomainManagerDao().getIssueSubTypes();
	}
	
	public Collection getVIRRecords(){
		return getDomainManagerDao().getVIRRecords();
	}
	
	public Collection getVIRRecords(Date createDate, String truckNumber){
		return getDomainManagerDao().getVIRRecords(createDate, truckNumber);
	}
	
	public VIRRecord getVIRRecord(String id){
		return getDomainManagerDao().getVIRRecord(id);
	}
	
	public Collection getMaintenanceIssue(String truckNumber, String issueTypeId, String issueSubTypeId){
		return getDomainManagerDao().getMaintenanceIssue(truckNumber, issueTypeId, issueSubTypeId);
	}
	
	public Collection getMaintenanceIssue(IssueType issueTypeId, IssueSubType issueSubTypeId){
		return getDomainManagerDao().getMaintenanceIssue(issueTypeId, issueSubTypeId);
	}
	
	public MaintenanceIssue getMaintenanceIssue(String id){
		return getDomainManagerDao().getMaintenanceIssue(id);
	}
	
	public Collection getMaintenanceIssues(){
		return getDomainManagerDao().getMaintenanceIssues();
	}
	
	public Collection getMaintenanceIssues(String issueStatus, String serviceStatus) {
		return getDomainManagerDao().getMaintenanceIssues(issueStatus, serviceStatus);
	}
	
	public Collection getMaintenanceIssues(String serviceStatus) {
		return getDomainManagerDao().getMaintenanceIssues(serviceStatus);
	}
	
	public void saveMaintenanceIssue(MaintenanceIssue model){
	    getDomainManagerDao().saveMaintenanceIssue(model);
	}
	
	public String saveVIRRecord(String createDate, String truckNumber, String vendor
			, String driver, String createdBy
			, String[][] recordIssues){
		String recordId = null;
		try{
			VIRRecord virRecord = new VIRRecord();
			virRecord.setTruckNumber(truckNumber);
			virRecord.setDriver(driver);
			virRecord.setCreateDate(new Timestamp(System.currentTimeMillis()));
			virRecord.setCreatedBy(createdBy);
			virRecord.setVendor(vendor);
			
			Set<IssueLog> issueLogList = new HashSet<IssueLog>();
			
			if (recordIssues != null && recordIssues.length > 0) {
				MaintenanceIssue _mIssue = null;
				IssueLog _issueLog = null;
				for(int count = 0; count < recordIssues.length; count++){
					_issueLog = new IssueLog();
					_issueLog.setIssueType(recordIssues[count][0]);
					_issueLog.setIssueSubType(recordIssues[count][1]);
					_issueLog.setDamageLocation(recordIssues[count][2]);
					_issueLog.setIssueSide(recordIssues[count][3]);
					_issueLog.setComments(TransStringUtil.isEmpty(recordIssues[count][4])? null : recordIssues[count][4]);
					
					if(_issueLog.getIssueType()!=null && _issueLog.getIssueSubType() != null){
						if("No Issue".equalsIgnoreCase(_issueLog.getIssueType()) 
								&& "No Issue".equalsIgnoreCase(_issueLog.getIssueSubType())){
							//No maintenance Issue is logged
						}else{
							// Verify if any 'OPEN' Maintenance issue exists with the same
							// truckNumber,IssueType and IssueSubType
							Collection maintenanceIssues = domainManagerDao
																.getMaintenanceIssue(truckNumber,_issueLog.getIssueType(),_issueLog.getIssueSubType());
							
							if (maintenanceIssues != null && maintenanceIssues.size() > 0) {
								for (Iterator<MaintenanceIssue> it = maintenanceIssues.iterator(); it.hasNext();) {
									 _mIssue = it.next();
								}							
							} else {
								_mIssue = new MaintenanceIssue();
	
								_mIssue.setTruckNumber(truckNumber);
								_mIssue.setIssueType(_issueLog.getIssueType());
								_mIssue.setIssueSubType(_issueLog.getIssueSubType());
								_mIssue.setCreateDate(new Timestamp(System.currentTimeMillis()));
								_mIssue.setCreatedBy(virRecord.getCreatedBy());
								_mIssue.setComments(TransStringUtil.isEmpty(_issueLog.getComments())? null : _issueLog.getComments());
								_mIssue.setDamageLocation(_issueLog.getDamageLocation());
								_mIssue.setIssueSide(_issueLog.getIssueSide());
								_mIssue.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								_mIssue.setVendor(virRecord.getVendor());
								_mIssue.setServiceStatus(EnumServiceStatus.INSERVICE.getDescription());
								_mIssue.setIssueStatus(EnumIssueStatus.OPEN.getName());
								//Create new maintenance log
								domainManagerDao.saveMaintenanceIssue(_mIssue);								
							}
						}						
						_issueLog.setMaintenanceIssue(_mIssue);					
						_issueLog.setVirRecord(virRecord);
					}
					issueLogList.add(_issueLog);					
				}
			}
			virRecord.setVirRecordIssues(issueLogList);
			if(virRecord != null){
				recordId = domainManagerDao.saveVIRRecord(virRecord);
			}
		}catch(DataIntegrityViolationException ex){
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return recordId;
	}
	
	public Collection getSector() {
		return getDomainManagerDao().getSector();
	}
	
	public Collection getActiveSector() {
		return getDomainManagerDao().getActiveSector();
	}
	public SectorZipcode getSectorZipCode(String zipCode){
		return getDomainManagerDao().getSectorZipCode(zipCode);
	}

	@Override
	public boolean addToSnapshot(String servicetypes,
			String buildings) {
	
		String[] buildingList = buildings.split(",");
		String[] serviceList = servicetypes.split(",");
		
		List<CapacitySnapshot> snapshots = new ArrayList<CapacitySnapshot>();
		for(int i=0;i<buildingList.length;i++)
		{
			for(int j=0;j<serviceList.length;j++)
			{
				CapacitySnapshot snapshot = new CapacitySnapshot();
				snapshot.setBuildingId(buildingList[i]);
				snapshot.setServicetype(serviceList[j].toUpperCase());
				snapshots.add(snapshot);
			}
		}
		return domainManagerDao.saveToSnapshot(snapshots);
		
	}

	@Override
	public Object getSnapshotLocation(String buildingId,
			String serviceType) {
		return getDomainManagerDao().getSnapshotLocation( buildingId,
				 serviceType);
	}
}

