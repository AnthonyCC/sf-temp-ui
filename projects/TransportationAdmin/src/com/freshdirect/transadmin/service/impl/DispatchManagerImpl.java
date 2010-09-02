package com.freshdirect.transadmin.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DispatchManagerDaoI;
import com.freshdirect.transadmin.dao.RouteManagerDaoI;
import com.freshdirect.transadmin.dao.SpatialManagerDaoI;
import com.freshdirect.transadmin.exception.TransAdminApplicationException;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.PunchInfoI;
import com.freshdirect.transadmin.model.ResourceI;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ScribLabel;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LogManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class DispatchManagerImpl extends BaseManagerImpl implements DispatchManagerI {
	
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

	public Collection getDrivers() {
					
		return getDispatchManagerDao().getDrivers(); 
	}
	
	public List matchCommunity(double latitiude, double longitude, String deliveryModel) {
		
		return getSpatialManagerDao().matchCommunity(latitiude, longitude, deliveryModel); 
	}
	
	public Collection getHelpers() {
		
		return getDispatchManagerDao().getHelpers();
	}	
		
	public Collection getPlan() {
		
		return getDispatchManagerDao().getPlan();
	}
	
	public Collection getPlan(String dateRange, String zoneLst) {
		return getDispatchManagerDao().getPlan(dateRange, zoneLst);
	}
	
	public Collection getPlanList(String date) {
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

	public void autoDisptch(String date) {
		// TODO Auto-generated method stub
		
		// get the plan for the dispatch Date order by sequence number 
		// get the route and truck for the date order by route number
		// create the dispatch model from above data
		// insert the data in dispatch table		
		Collection dispList=getDispatchList(date,null,null);	
		
		if(dispList!=null || dispList.size()>0){						
			  Iterator iterator=dispList.iterator();
			  while(iterator.hasNext()){							  
						  Dispatch disp=(Dispatch)iterator.next();
						  disp.setUserId("AUTO-DISPATCH");
						  Set disResList=disp.getDispatchResources();
						  removeEntity(disResList);							  							  
				  }
				  removeEntity(dispList);			
		}		  		  				
						
		Collection planList=getPlanList(date);		
		Collection routeList=getDomainManagerService().getRoutes(date);
		Collection truckList=getDomainManagerService().getTrucks();
		
		Collection dispatchList=ModelUtil.constructDispatchModel(planList,routeList);
		
		Map childMap=new HashMap();
		Iterator iterator=dispatchList.iterator();
		while(iterator.hasNext()){
			Dispatch dis=(Dispatch)iterator.next();
			Set res=dis.getDispatchResources();
			childMap.put(dis.getPlanId(),res);
			dis.setDispatchResources(null);
		}
		
		// first save the parent 
		getDispatchManagerDao().saveEntityList(dispatchList);
		
		Iterator resIterator=dispatchList.iterator();
		while(resIterator.hasNext()){
			Dispatch dis=(Dispatch)resIterator.next();
			Set disResource=(Set)childMap.get(dis.getPlanId());
			if(disResource!=null)
			{ 
				ModelUtil.assosiateDispatchToResource(disResource,dis);
				getDispatchManagerDao().saveEntityList(disResource);
			}
			
		}
		
		// now save the child
		
//		for(int i=0;i<childList.size();i++){
//			Set resSet=(Set)childList.get(i);
//			getDispatchManagerDao().saveEntityList(resSet);
//		}
	}

	
	public void autoDisptchRegion(String date) {
		
		Collection dispList=getDispatchList(date,null,null);	
		
		if(dispList!=null || dispList.size()>0){						
			  Iterator iterator=dispList.iterator();
			  while(iterator.hasNext()){							  
						  Dispatch disp=(Dispatch)iterator.next();
						  Object[] param=new Object[]{disp.getDispatchId(),"DELETED","",""};
						  logManager.log("AUTO-DISPATCH", 3, param);
						  Set disResList=disp.getDispatchResources();
						  removeEntity(disResList);							  							  
				  }
				  removeEntity(dispList);			
		}		  		  				
						
		Collection planList=getPlanList(date);		
		Collection routeList=getDomainManagerService().getRoutes(date);		
		Collection zones=getDomainManagerService().getZones();	
		Collection dispatchList=ModelUtil.constructDispatchModel(planList,routeList,zones);
		
		Map childMap=new HashMap();
		Iterator iterator=dispatchList.iterator();
		while(iterator.hasNext()){
			Dispatch dis=(Dispatch)iterator.next();
			Set res=dis.getDispatchResources();
			childMap.put(dis.getPlanId(),res);
			dis.setDispatchResources(null);
		}
		
		// first save the parent 
		getDispatchManagerDao().saveEntityList(dispatchList);
		
		Iterator resIterator=dispatchList.iterator();
		while(resIterator.hasNext()){
			Dispatch dis=(Dispatch)resIterator.next();
			Set disResource=(Set)childMap.get(dis.getPlanId());
			if(disResource!=null)
			{ 
				ModelUtil.assosiateDispatchToResource(disResource,dis);
				getDispatchManagerDao().saveEntityList(disResource);
			}
			
		}

	}
	
	public Collection getDispatchList(String date, String zone, String region) {
		Collection coll = getDispatchManagerDao().getDispatchList(date, zone, region);
		if(coll.size() > 0){
			Dispatch dispatch = (Dispatch) coll.iterator().next();
		}
		return coll;
	}
	
	public boolean refreshRoute(Date requestedDate) {
		try{
			//Get the dispatch list for the day from DB.
			String dateString = TransStringUtil.getServerDate(requestedDate);
			Collection coll = getDispatchList(dateString, null, null);
			List changeList = new ArrayList();
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
				String firstDlvTimeFromSAP = routeInfo != null ? routeInfo.getFirstDlvTime() : "";
				String firstDlvTime = TransStringUtil.getServerTime(dispatchObj.getFirstDlvTime());
				if(firstDlvTime != null && firstDlvTime.length() > 0 && firstDlvTime.equals(firstDlvTimeFromSAP)) {
					dispatchObj.setFirstDlvTime(TransStringUtil.getServerTime(firstDlvTimeFromSAP));
					changed = true;
					
				}
				if(changed)
					changeList.add(dispatchObj);
			}
			if(changeList.size() > 0){
				//There are changes to dispatches. update dispatch table.
				saveEntityList(changeList);
				return true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException("Exception Occurred while refreshing Route information for requested date "+requestedDate);
		}
		return false;
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
		if (!TransStringUtil.isEmpty(dispatch.getDispatchId())) {
			
			Dispatch referenceDispatch = this.getDispatch(referenceContextId);
			Dispatch currentDispatch = this.getDispatch(dispatch.getDispatchId());
			if(referenceDispatch != null && currentDispatch != null) {
				Collection bullpens = this.getDispatchManagerDao().getDispatch(currentDispatch.getDispatchDate(), 
																		currentDispatch.getStartTime(), 
																		true);
				Dispatch bullPen = bullPen = new Dispatch();
				
				bullPen.setDispatchDate(currentDispatch.getDispatchDate());

				bullPen.setRegion(currentDispatch.getRegion());
				bullPen.setStartTime(currentDispatch.getStartTime());
				bullPen.setFirstDlvTime(currentDispatch.getFirstDlvTime());
				bullPen.setBullPen(true);
				for (Iterator i = currentDispatch.getDispatchResources().iterator(); i.hasNext();) {
					DispatchResource _oldResource = (DispatchResource)i.next();
					DispatchResource dispatchResource = new DispatchResource();
					ResourceId resource = new ResourceId();
					resource.setResourceId(_oldResource.getId().getResourceId());	
					resource.setAdjustmentTime(_oldResource.getId().getAdjustmentTime());
					dispatchResource.setEmployeeRoleType(_oldResource.getEmployeeRoleType());
					dispatchResource.setId(resource);
					dispatchResource.setNextTelNo(_oldResource.getNextTelNo());
					dispatchResource.setDispatch(bullPen);
					bullPen.getDispatchResources().add(dispatchResource);
				}
				this.getDispatchManagerDao().removeEntityEx(referenceDispatch);
				getDispatchManagerDao().saveDispatch(bullPen);
			}
		} else if(!TransStringUtil.isEmpty(referenceContextId)) {
			Dispatch referenceDispatch = this.getDispatch(referenceContextId);
			if(referenceDispatch.getDispatchResources() != null) {
				referenceDispatch.getDispatchResources().clear();
			}
			this.getDispatchManagerDao().saveDispatch(referenceDispatch);
		}
		getDispatchManagerDao().saveDispatch(dispatch);
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
	
	
	public static void main(String args[]){
		
		DispatchManagerImpl impl=new DispatchManagerImpl();
		impl.autoDisptch("11/26/2008");
	}
	
	public void savePlan(Plan	plan) {
		getDispatchManagerDao().savePlan(plan);
	}
	
	public void savePlan(Plan plan, String referencePlanId) {
		if (!TransStringUtil.isEmpty(plan.getPlanId())) {
			
			Plan referencePlan = this.getPlan(referencePlanId);
			Plan currentPlan = this.getPlan(plan.getPlanId());
			if(referencePlan != null && currentPlan != null) {
				Collection bullpens = this.getDispatchManagerDao().getPlan(currentPlan.getPlanDate(), 
																		currentPlan.getStartTime(), 
																		true);
				Plan bullPen = bullPen = new Plan();
				
				bullPen.setPlanDate(currentPlan.getPlanDate());

				bullPen.setRegion(currentPlan.getRegion());
				bullPen.setStartTime(currentPlan.getStartTime());
				bullPen.setFirstDeliveryTime(currentPlan.getFirstDeliveryTime());
				bullPen.setIsBullpen("Y");
				for (Iterator i = currentPlan.getPlanResources().iterator(); i.hasNext();) {
					PlanResource _oldResource = (PlanResource)i.next();
					PlanResource planResource = new PlanResource();
					ResourceId resource = new ResourceId();
					resource.setResourceId(_oldResource.getId().getResourceId());						
					planResource.setEmployeeRoleType(_oldResource.getEmployeeRoleType());
					planResource.setId(resource);
					bullPen.getPlanResources().add(planResource);
				}
				
				this.getDispatchManagerDao().removeEntityEx(referencePlan);
				getDispatchManagerDao().savePlan(bullPen);
			}
		} else if(!TransStringUtil.isEmpty(referencePlanId)) {
			Plan referencePlan = this.getPlan(referencePlanId);
			if(referencePlan.getPlanResources() != null) {
				referencePlan.getPlanResources().clear();
			}
			this.getDispatchManagerDao().savePlan(referencePlan);
		}
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
			//String date="12-Apr-2009";
			Collection punchInfo=employeeManagerService.getPunchInfo(date);
			if(punchInfo==null || punchInfo.isEmpty())
				return unassignedPunchedInEmployees;
			
			Collection dispList=getDispatchList(date,null,null);	
			Set dispatchResources=new HashSet();
			if(dispList!=null || dispList.size()>0)
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
	
	public boolean addScribLabel(ScribLabel sLabel){
		return getDispatchManagerDao().addScribLabel(sLabel);
	}
	
	public ScribLabel getScribLabelByDate(String date){
		return getDispatchManagerDao().getScribLabelByDate(date);
	}
	
}
