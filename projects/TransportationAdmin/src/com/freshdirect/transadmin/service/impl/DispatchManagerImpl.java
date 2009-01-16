package com.freshdirect.transadmin.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DispatchManagerDaoI;
import com.freshdirect.transadmin.exception.TransAdminApplicationException;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.TrnDispatch;
import com.freshdirect.transadmin.model.TrnDispatchPlan;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DispatchManagerImpl extends BaseManagerImpl implements DispatchManagerI {
	
	private DispatchManagerDaoI dispatchManagerDao = null;
	
	private DomainManagerI domainManagerService; 
	
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
						  Set disResList=disp.getDispatchResources();
						  removeEntity(disResList);							  							  
				  }
				  removeEntity(dispList);			
		}		  		  				
						
		Collection planList=getPlanList(date);		
		Collection routeList=getDomainManagerService().getRoutes(date);
		Collection truckList=getDomainManagerService().getTrucks();
		
		Collection dispatchList=ModelUtil.constructDispatchModel(planList,routeList,truckList);
		
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
	
	public void saveDispatch(Dispatch dispatch) throws TransAdminApplicationException{
		/* Check if a route is already is assigned to a dispatch before you save the dispatch */
		try{
			boolean routeChanged = false;
			Collection assignedRoutes = getAssignedRoutes(TransStringUtil.getServerDate(dispatch.getDispatchDate()));
			if(!TransStringUtil.isEmpty(dispatch.getDispatchId())){
				Dispatch currDispatch = getDispatchManagerDao().getDispatch(dispatch.getDispatchId());
				if(!dispatch.getRoute().equals(currDispatch.getRoute()))
					routeChanged = true;
			}

			if(routeChanged && assignedRoutes.contains(dispatch.getRoute())){
				throw new TransAdminApplicationException("135", new String[]{dispatch.getRoute()});
			}
		}catch(ParseException exp){
			//Ignore it
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
		allTrucks.removeAll(assignedTrucks);
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
				if(!sapRouteList.contains(route.getRouteNumber())){
					unusedRouteNumList.add(route);
				}
			}
		}						
		return unusedRouteNumList;
	}
}
