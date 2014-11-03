package com.freshdirect.transadmin.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DispatchManagerDaoI;
import com.freshdirect.transadmin.dao.RouteManagerDaoI;
import com.freshdirect.transadmin.dao.SpatialManagerDaoI;
import com.freshdirect.transadmin.exception.TransAdminApplicationException;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PunchInfoI;
import com.freshdirect.transadmin.model.ResourceI;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ScribLabel;
import com.freshdirect.transadmin.model.UPSRouteInfo;
import com.freshdirect.transadmin.model.WaveInstance;
import com.freshdirect.transadmin.model.WaveInstancePublish;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LogManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransAdminCacheManager;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.util.WaveUtil;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class DispatchManagerImpl extends BaseManagerImpl implements DispatchManagerI {
	
	/*
	 * These constants are used by Muni Meter update method
	 * @updateMuniMeterCardDetails(String flag, String value, String status, String dispatchId)
	 */
	private final static String DISPATCH_FLAG="dispatch";
	private final static String CHECKIN_FLAG="checkin";
	
	private DispatchManagerDaoI dispatchManagerDao = null;
	
	private RouteManagerDaoI routeManagerDao = null;
	
	private DomainManagerI domainManagerService; 
	
	private EmployeeManagerI employeeManagerService;
	
	private SpatialManagerDaoI spatialManagerDao;
	
	private LogManagerI logManager; 
	
	
	public DispatchManagerDaoI getDispatchManagerDao() {
		return dispatchManagerDao;
	}

	public void setDispatchManagerDao(DispatchManagerDaoI dispatchManagerDao) {
		this.dispatchManagerDao = dispatchManagerDao;
	}
	
	protected BaseManagerDaoI getBaseManageDao() {
		return getDispatchManagerDao();
	}
	
	public List matchCommunity(double latitiude, double longitude, String deliveryModel) {
		
		return getSpatialManagerDao().matchCommunity(latitiude, longitude, deliveryModel); 
	}	
		
	public Collection getPlan() {
		
		return getDispatchManagerDao().getPlan();
	}
	
	public Collection getPlanEntry(String dateRange, String zoneLst, String facilityLocation) {
		return getDispatchManagerDao().getPlanEntry(dateRange, zoneLst, facilityLocation);
	}
	
	public Collection<Plan> getPlanList(String date) {
		return getDispatchManagerDao().getPlanList(date);
	}
	
	public Collection getPlanList(String date, String region) {
		return getDispatchManagerDao().getPlanList(date, region);
	}
	
	public Collection getPlanForResource(String date, String resourceId) {
		return getDispatchManagerDao().getPlanForResource(date, resourceId);
	}
	
	public Collection getDispatchForResource(String date, String resourceId) {
		return getDispatchManagerDao().getDispatchForResource(date, resourceId);
	}
	
	public Collection getPlan(String day, String zone, String date) {
		
		return getDispatchManagerDao().getPlan(day, zone, date);
	}
	
	public Dispatch getDispatch(String dispatchId) {
		
		return getDispatchManagerDao().getDispatch(dispatchId);
	}
	
	public Plan getPlan(String id)  {
		
		return getDispatchManagerDao().getPlan(id);	
	}
	
			
	public void copyPlan(Collection addPlanList, Collection removePlanList) {
		this.removeEntity(removePlanList);
		//this.saveEntityList(addPlanList);
		Iterator it=addPlanList.iterator();
		while(it.hasNext()) {
			getDispatchManagerDao().savePlan((Plan)it.next());
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection getDispatchList(String date, String facilityLocation, String zone, String region) {
		Collection coll = getDispatchManagerDao().getDispatchList(date, facilityLocation, zone, region);

		return coll;
	}
	
	public List refreshRoute(Date requestedDate) {
		List changeList = new ArrayList();
		try{
			//Get the dispatch list for the day from DB.
			String dateString = TransStringUtil.getServerDate(requestedDate);
			Collection coll = getDispatchList(dateString, null, null, null);
			
			Iterator iter = coll.iterator();
			while(iter.hasNext()){
				boolean changed = false;
				Dispatch dispatchObj = (Dispatch) iter.next();
				FDRouteMasterInfo routeInfo = getDomainManagerService().getRouteMasterInfo(dispatchObj.getRoute(), requestedDate);
				String sapTruckNum = routeInfo != null ? routeInfo.getTruckNumber() : "";
				if(sapTruckNum != null && sapTruckNum.length() > 0 &&!sapTruckNum.equals(dispatchObj.getTruck())) {
					//Route/Truck assignment has changed in SAP. So update truck number and add it to the changeList.
					dispatchObj.setTruck(routeInfo.getTruckNumber());
					changed =  true;
					
				}
				/*String firstDlvTimeFromSAP = routeInfo != null ? routeInfo.getFirstDlvTime() : "";
				String firstDlvTime = TransStringUtil.getServerTime(dispatchObj.getFirstDlvTime());
				if(firstDlvTime != null && firstDlvTime.length() > 0 && firstDlvTime.equals(firstDlvTimeFromSAP)) {
					dispatchObj.setFirstDlvTime(TransStringUtil.getServerTime(firstDlvTimeFromSAP));
					changed = true;
					
				}*/
				if(changed)
					changeList.add(dispatchObj);
			}
			if(changeList.size() > 0){
				//There are changes to dispatches. update dispatch table.
				//saveEntityList(changeList);
				getRouteManagerDao().updateTruckInfo(changeList);
				return changeList;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException("Exception Occurred while refreshing Route information for requested date "+requestedDate);
		}
		return changeList;
	}

	public void saveDispatch(Dispatch dispatch) throws TransAdminApplicationException {
		// Check if a route is already is assigned to a dispatch before you save the dispatch		
		validateDispatchRoute(dispatch);
		getDispatchManagerDao().saveDispatch(dispatch);
	}
	
	private void validateDispatchRoute(Dispatch dispatch) throws TransAdminApplicationException {
		try{
			boolean routeChanged = false;
			Collection assignedRoutes = getAssignedRoutes(TransStringUtil.getServerDate(dispatch.getDispatchDate()));
			if(!TransStringUtil.isEmpty(dispatch.getDispatchId())){
				Dispatch currDispatch = getDispatchManagerDao().getDispatch(dispatch.getDispatchId());
				if(!dispatch.getRoute().equals(currDispatch.getRoute()))
					routeChanged = true;
			}else{
				routeChanged = true;
			}

			if(routeChanged && assignedRoutes.contains(dispatch.getRoute())){
				throw new TransAdminApplicationException("135", new String[]{dispatch.getRoute()});
			}
		}catch(ParseException exp){
			//Ignore it
		}
	}

	public void saveDispatch(Dispatch dispatch, String referenceContextId) throws TransAdminApplicationException  {
		validateDispatchRoute(dispatch);
		boolean isNew = TransStringUtil.isEmpty(dispatch.getDispatchId());
		getDispatchManagerDao().saveDispatch(dispatch);
		
		if(isNew && !TransStringUtil.isEmpty(referenceContextId)) {
			String[] referenceDispatchIds = StringUtil.decodeStrings(referenceContextId);
			if(referenceDispatchIds != null) {
				for(String tmpDispatchId : referenceDispatchIds) {
					Dispatch referenceDispatch = this.getDispatch(tmpDispatchId);
					
					if(referenceDispatch.getDispatchResources() != null) {
						referenceDispatch.getDispatchResources().clear();
					} else {
						referenceDispatch.setDispatchResources(new HashSet(0));
					}
					
					referenceDispatch.setConfirmed(false);
					this.getDispatchManagerDao().saveDispatch(referenceDispatch);
				}
			}
			
		}
		
	}
	
	public Collection getAssignedTrucks(String date) {
		return getDispatchManagerDao().getAssignedTrucks(date);
	}
	
	public Collection getAssignedRoutes(String date) {
		return getDispatchManagerDao().getAssignedRoutes(date);
	}
	
	public Collection getDispatchTrucks(String date) {
		Set trucks = new HashSet();
		Collection routes = getDomainManagerService().getRoutes(date);
		Iterator iter = routes.iterator();
		while(iter.hasNext()){
			ErpRouteMasterInfo routeInfo = (ErpRouteMasterInfo) iter.next();
			trucks.add(routeInfo.getTruckNumber());
		}
		return trucks;
	}
	
	public Collection getAvailableTrucks(String date) {
		Collection assignedTrucks =  getAssignedTrucks(date);
		Collection dispatchTrucks = getDispatchTrucks(date);
		Collection allTrucks = getDomainManagerService().getTruckNumbers();
		assignedTrucks.addAll(dispatchTrucks);
		//allTrucks.removeAll(assignedTrucks);
		return allTrucks;
	}
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
		
	public void savePlan(Plan	plan) {
		getDispatchManagerDao().savePlan(plan);
	}
	
	public void savePlan(Plan plan, String referencePlanId) {
		if(TransStringUtil.isEmpty(plan.getPlanId()) && !TransStringUtil.isEmpty(referencePlanId)) {
			String[] referencePlanIds = StringUtil.decodeStrings(referencePlanId);
			if(referencePlanIds != null) {
				for(String tmpPlanId : referencePlanIds) {
					Plan referencePlan = this.getPlan(tmpPlanId);
					if(referencePlan.getPlanResources() != null) {
						referencePlan.getPlanResources().clear();
					} else {
						referencePlan.setPlanResources(new HashSet(0));
					}
					referencePlan.setOpen("Y");
					this.getDispatchManagerDao().savePlan(referencePlan);
				}
			}
			
		}
		Plan previousModel = getPlan(plan.getPlanId());
		getDispatchManagerDao().savePlan(plan);		
	}
		
	
	public Collection getUnusedDispatchRoutes(String dispatchDate) {
		
		//  get dispatch for the day
		Collection routeList=this.dispatchManagerDao.getAssignedRoutes(dispatchDate);
		Collection sapRouteList=this.domainManagerService.getRoutes(dispatchDate);
		//  this contains the Route model extract the String
		Set unusedRouteNumList=new HashSet();
		if(sapRouteList!=null && !sapRouteList.isEmpty())
		{
			Iterator iterator=sapRouteList.iterator();
			while(iterator.hasNext()){
				ErpRouteMasterInfo route=(ErpRouteMasterInfo)iterator.next();
				if(!routeList.contains(route.getRouteNumber())){
					unusedRouteNumList.add(route);
				}
			}
		}						
		return unusedRouteNumList;
	}
	
	public int updateRouteMapping(Date routeDate, String cutOffId, String sessionId, boolean isDepot) {
		return getRouteManagerDao().updateRouteMapping(routeDate, cutOffId, sessionId, isDepot);
	}
	
	public Map getRouteNumberGroup(String date, String cutOff, String groupCode) {
		return getRouteManagerDao().getRouteNumberGroup(date, cutOff, groupCode);
	}

	public RouteManagerDaoI getRouteManagerDao() {
		return routeManagerDao;
	}

	public void setRouteManagerDao(RouteManagerDaoI routeManagerDao) {
		this.routeManagerDao = routeManagerDao;
	}
	
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}
	
	public Collection getUnassignedActiveEmployees() {
		
		try {
			Set unassignedPunchedInEmployees=new HashSet();
			String date=TransStringUtil.getCurrentServerDate();
			String day=TransStringUtil.getServerDay(TransStringUtil.getServerDateString(date)).toUpperCase();

			Collection punchInfo = TransAdminCacheManager.getInstance().getPunchInfo(date, employeeManagerService);
			if(punchInfo == null || punchInfo.isEmpty())
				return unassignedPunchedInEmployees;
			
			Collection dispList=getDispatchList(date,null,null,null);
			Set dispatchResources=new HashSet();
			if (dispList != null && dispList.size() > 0)
			{						
				  Iterator iterator=dispList.iterator();
				  while(iterator.hasNext()){							  
					  Dispatch disp=(Dispatch)iterator.next();
					  if(disp.getBullPen()==null || (disp.getBullPen().booleanValue()==false && disp.getZone()!=null ))
						  dispatchResources.addAll(disp.getDispatchResources());
				  }
			}
			Iterator it=punchInfo.iterator();
			PunchInfoI _punchInfo=null;
			while(it.hasNext()) {
				_punchInfo=(PunchInfoI)it.next();
				if(_punchInfo.getInPunchDTM()!=null &&_punchInfo.getOutPunchDTM()==null) {// add check for inPunch!=null
					if(!isDispatchAssigned(_punchInfo.getEmployeeId(),dispatchResources)) {
						WebEmployeeInfo webEmpInfo=employeeManagerService.getEmployee(_punchInfo.getEmployeeId());
						if(webEmpInfo!=null && webEmpInfo.getEmpInfo()!=null && !webEmpInfo.getEmpRole().isEmpty() )
						{
							if(DispatchPlanUtil.isEligibleForUnassignedEmployees(domainManagerService.getEmployeeRole(_punchInfo.getEmployeeId())))
							{
								ScheduleEmployee s = employeeManagerService.getSchedule(_punchInfo.getEmployeeId()
																	, TransStringUtil.getServerDate(TransStringUtil.getWeekOf(date)), day);	
								if(s!=null)
								webEmpInfo.setRegion(s.getRegionS());
								for(Iterator i=dispList.iterator();i.hasNext();)
								{
									Dispatch disp=(Dispatch)i.next();
									Set r=disp.getDispatchResources();
									for(Iterator j=r.iterator();j.hasNext();)
									{
										ResourceI resource=(ResourceI)j.next();
										if(_punchInfo.getEmployeeId().equals(resource.getId().getResourceId()))
										{
											if(disp.getBullPen()!=null&&disp.getBullPen().booleanValue()==true)
											{
												webEmpInfo.setBullpen(true);
											}
										}
												
									}
									
								}
								unassignedPunchedInEmployees.add(webEmpInfo);
							}
						}
					}
				}
			}			
			return unassignedPunchedInEmployees;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	 
	
	private boolean isDispatchAssigned(String employeeId, Set dispatchResources) {
		
		if(dispatchResources==null)
			return false;
		
		boolean assigned=false;
		Iterator it=dispatchResources.iterator();
		while(!assigned && it.hasNext()) {
			ResourceI resource=(ResourceI)it.next();
			if(employeeId.equals(resource.getId().getResourceId()))
				assigned=true;
		}
		return assigned;
	}
	public void evictDispatch(Dispatch dispatch)
	{
		this.dispatchManagerDao.evictDispatch(dispatch);
	}
	
	public Map getHTInScan(Date routeDate) 
	{
		try {
			if(!TransportationAdminProperties.isAirclicBlackhole()) {
				return routeManagerDao.getHTInScan(routeDate);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Map getHTOutScan(Date routeDate) 
	{
		try {
			if(!TransportationAdminProperties.isAirclicBlackhole()) {
				return routeManagerDao.getHTOutScan(routeDate);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//added new code Appdev 808
	
	public List getHTOutScanAsset(Date routeDate) 
	{
		try {
			if(!TransportationAdminProperties.isAirclicBlackhole()) {
				return routeManagerDao.getHTOutScanAsset(routeDate);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List getResourcesWorkedForSixConsecutiveDays(Date date) {
		return routeManagerDao.getResourcesWorkedForSixConsecutiveDays(date);
	}
	
	public List getDispatchTeamResourcesChanged(Date date, String type, String field) {
		return routeManagerDao.getDispatchTeamResourcesChanged(date,type,field);
	}

	public void setLogManager(LogManagerI logManager) {
		this.logManager = logManager;
	}

	public SpatialManagerDaoI getSpatialManagerDao() {
		return spatialManagerDao;
	}

	public void setSpatialManagerDao(SpatialManagerDaoI spatialManagerDao) {
		this.spatialManagerDao = spatialManagerDao;
	}
	
	public Collection getScribList(String date) {
		return getDispatchManagerDao().getScribList(date);
	}
	
	public Collection getScribList(String date, String region) {
		return getDispatchManagerDao().getScribList(date, region);
	}
		
	public Scrib getScrib(String id) {
		return getDispatchManagerDao().getScrib(id);
	}
	
	public Collection getUserPref(String userId)
	{
		return getDispatchManagerDao().getUserPref(userId);
	}

	public Collection getDispatchReasons(boolean active) {
		return getDispatchManagerDao().getDispatchReasons(active);
	}
	
	public int addScenarioDayMapping(DlvScenarioDay scenarioDay) {
		return getDispatchManagerDao().addScenarioDayMapping(scenarioDay);
	}
	public void deleteDefaultScenarioDay(String sDate, String sDay){
		getDispatchManagerDao().deleteDefaultScenarioDay(sDate,sDay);
	}
	
	public ScribLabel getScribLabelByDate(String date){
		return getDispatchManagerDao().getScribLabelByDate(date);
	}
	public Collection getDatesByScribLabel(String slabel){
		return getDispatchManagerDao().getDatesByScribLabel(slabel);
	}
	
	public Collection getDispatchForGPS(Date dispatchDate, String assetId) {
		return getDispatchManagerDao().getDispatchForGPS(dispatchDate, assetId);
	}
	
	public Collection getDispatchForEZPass(Date dispatchDate, String assetId) {
		return getDispatchManagerDao().getDispatchForEZPass(dispatchDate, assetId);
	}
	
	public Collection getDispatchForMotKit(Date dispatchDate, String assetId) {
		return getDispatchManagerDao().getDispatchForMotKit(dispatchDate, assetId);
	}
	
	public Collection getPlan(Date planDate, String zone) {
		return getDispatchManagerDao().getPlan(planDate, zone);
	}
	
	public Collection getScrib(Date scribDate, String zone) {
		return getDispatchManagerDao().getScrib(scribDate, zone);
	}
	
	public Collection getWaveInstancePublish(Date deliveryDate) {
		return getDispatchManagerDao().getWaveInstancePublish(deliveryDate);
	}
	
	public Collection getWaveInstance(Date deliveryDate, String area) {
		return getDispatchManagerDao().getWaveInstance(deliveryDate, area);
	}
	
	public Collection getWaveInstance(Date cutOff) {
		return getDispatchManagerDao().getWaveInstance(cutOff);
	}
	
	public Collection getWaveInstance(Date deliveryDate, Date cutOff) {
		return getDispatchManagerDao().getWaveInstance(deliveryDate, cutOff);
	}
	
	public WaveInstance getWaveInstance(String waveInstanceId)  {
		return getDispatchManagerDao().getWaveInstance(waveInstanceId);
	}
			
	public void saveWaveInstances(List<WaveInstance> saveWaveInstances, List<WaveInstance> deleteWaveInstances) {
		if(deleteWaveInstances != null && deleteWaveInstances.size() > 0) {
			this.getDispatchManagerDao().removeEntity(deleteWaveInstances);
		}
		if(saveWaveInstances != null && saveWaveInstances.size() > 0) {
			this.getDispatchManagerDao().saveEntityList(saveWaveInstances);
		}
	}
		
	public void publishWaveInstance(Date deliveryDate, String actionBy, EnumWaveInstancePublishSrc source) {
		
		List<List<WaveInstance>> waveInstancesResult = new ArrayList<List<WaveInstance>>();
		Collection wavePublish = this.getWaveInstancePublish(deliveryDate);
		WaveInstancePublish publish = null;
		if(wavePublish != null && wavePublish.size() > 0) {
			publish = (WaveInstancePublish)wavePublish.iterator().next();
		} else {
			publish = new WaveInstancePublish();			
		}
		publish.setDeliveryDate(deliveryDate);
		publish.setPublishedAt(new Date());
		publish.setPublishedBy(actionBy);
		publish.setSource(source);
		
		if(deliveryDate != null) {
			Collection sourceData = null;			
			if(EnumWaveInstancePublishSrc.SCRIB.equals(source)) {
				sourceData = this.getScrib(deliveryDate, null);				
			} else {
				sourceData = this.getPlan(deliveryDate, null);		
			}
			waveInstancesResult = WaveUtil.getWavesForPublish(sourceData, deliveryDate, actionBy, source, this);
		}
		this.getDispatchManagerDao().saveEntity(publish);
		
		if(waveInstancesResult.get(1).size() > 0) {
			this.getDispatchManagerDao().removeEntity(waveInstancesResult.get(1));
		}
		if(waveInstancesResult.get(0).size() > 0) {
			this.getDispatchManagerDao().saveEntityList(waveInstancesResult.get(0));
		}
	}
	
	public void uploadScrib(Set<Date> scribDates, List<Scrib> toSaveScribs) {
		for(Date scribDate : scribDates) {
			this.getDispatchManagerDao().removeEntity(this.getDispatchManagerDao().getScrib(scribDate, null));
		}
		if(toSaveScribs != null && toSaveScribs.size() > 0) {
			this.getDispatchManagerDao().saveEntityList(toSaveScribs);
		}
	}
	
	public List<UPSRouteInfo> getUPSRouteInfo(Date deliveryDate){
		return this.getRouteManagerDao().getUPSRouteInfo(deliveryDate);
	}
	
	public Collection getDispatchForRoute(Date dispatchDate, String routeNo){
		return this.getDispatchManagerDao().getDispatchForRoute(dispatchDate, routeNo);
	}
	
	public Collection getTrnFacilitys(){
		return this.getDispatchManagerDao().getTrnFacilitys();
	}
	
	public int updateOrderUnassignedInfo(String orderNo) {
		return this.getRouteManagerDao().updateOrderUnassignedInfo(orderNo);
	}
	
	public boolean updateMuniMeterCardDetails(String flag, String value, String status, String dispatchId, String userId){
		//Need to get the userId Important
		
		double cardValue=0.00;
		// Get the current dispatch record
		Dispatch tmpDispatch = getDispatch(dispatchId);
		tmpDispatch.setUserId(userId);
		if(value!=null && !value.isEmpty()){
			try {
				cardValue = Double.parseDouble(value);
				if(cardValue > TransportationAdminProperties.getMuniMeterMaxValue()){
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if(status.equals("N")){
			status=null;
		}
		// Based on flag update the values of munimeter
		if(flag.equalsIgnoreCase(DISPATCH_FLAG)){
			tmpDispatch.setMuniMeterValueAssigned(!TransStringUtil.isEmpty(value)?Double.parseDouble(value):null);
			tmpDispatch.setMuniMeterCardNotAssigned(status);
		} else if(flag.equalsIgnoreCase(CHECKIN_FLAG)){
			if(!TransStringUtil.isEmpty(value) && tmpDispatch.getMuniMeterValueAssigned()< Double.parseDouble(value)){
				return false;
			}
			tmpDispatch.setMuniMeterValueReturned(!TransStringUtil.isEmpty(value)?Double.parseDouble(value):null);
			tmpDispatch.setMuniMeterCardNotReturned(status);
		} else {
			return false;
		}
		//call dispatchermanagerDAOIMPL with the dispatch 
		try {
			this.getDispatchManagerDao().saveDispatch(tmpDispatch);
			return true;
		} catch (DataAccessException e) {
			e.printStackTrace();
			return false;
		}
		//return muniMeterDao.updateMuniMeterCardDetails(flag, value, status, dispatchId);
	}
	
	public String getDialogDisplayFlag(String dialogFlag, String dispatchId){
		String displayFlag="NONE";
		String muniMeterAssignedValue="0";
		// Get the current dispatch record
		Dispatch dispatch = getDispatch(dispatchId);
		//get MuniMeter flag
		String muniMeterFlag=dispatch.getRegion().getMuniMeterEnabled();
		if(dispatch.getMuniMeterValueAssigned()!=null){
			muniMeterAssignedValue = Double.toString(dispatch.getMuniMeterValueAssigned());
		}
		
		if(!(TransStringUtil.isEmpty(muniMeterFlag))){
			if(dialogFlag.equalsIgnoreCase(DISPATCH_FLAG) && muniMeterFlag.equals("X")){
				displayFlag=DISPATCH_FLAG;
			} else if(dialogFlag.equalsIgnoreCase(CHECKIN_FLAG) && muniMeterFlag.equals("X") 
					&& !TransStringUtil.isEmpty(dispatch.getMuniMeterCardNotAssigned())
					&& "X".equals(dispatch.getMuniMeterCardNotAssigned())){
				
					displayFlag="NONE";
			} else if(dialogFlag.equalsIgnoreCase(CHECKIN_FLAG) && muniMeterFlag.equals("X")
					&& TransStringUtil.isEmpty(dispatch.getMuniMeterCardNotAssigned()) ){
				if(dispatch.getMuniMeterValueAssigned()!=null){
					displayFlag=CHECKIN_FLAG+"_"+muniMeterAssignedValue;
				}
				
			}
		}
		return displayFlag;
	}
	
}
