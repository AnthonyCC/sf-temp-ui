package com.freshdirect.transadmin.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.DispatchResourceId;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.PlanResourceId;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.Route;
import com.freshdirect.transadmin.model.RouteInfo;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class ModelUtil {
	
	private static final DateFormat DATE_FORMAT=new SimpleDateFormat("MM/dd/yyyy h:m:s");
	
	public static List getDeliveryLocations(List lstDlvLocation) {
		
		List result = new ArrayList();
		ILocationModel locationModel = null;
		DlvLocation dlvLocationModel = null;
		if(lstDlvLocation != null) {
			Iterator iterator = lstDlvLocation.iterator();
			while(iterator.hasNext()) {
				locationModel = new LocationModel();
				dlvLocationModel = (DlvLocation)iterator.next();
				locationModel.setApartmentNumber(dlvLocationModel.getApartment());
				locationModel.setCity(dlvLocationModel.getBuilding().getCity());		
				locationModel.setState(dlvLocationModel.getBuilding().getState());		
				locationModel.setStreetAddress1(dlvLocationModel.getBuilding().getSrubbedStreet());
				locationModel.setStreetAddress2(dlvLocationModel.getBuilding().getSrubbedStreet());
				locationModel.setZipCode(dlvLocationModel.getBuilding().getZip());
				locationModel.setCountry(dlvLocationModel.getBuilding().getCountry());
				result.add(locationModel);
			}
		}
		return result;
	}
	
	public static void updateGeographyLocation(List lstDlvLocation, List geographyLocation) {
				
		IGeographicLocation locationModel = null;
		DlvLocation dlvLocationModel = null;
		if(lstDlvLocation != null) {
			Iterator iterator = lstDlvLocation.iterator();
			int intCount = 0;
			while(iterator.hasNext()) {
				locationModel = (IGeographicLocation)geographyLocation.get(intCount++);
				dlvLocationModel = (DlvLocation)iterator.next();
				dlvLocationModel.setLatitude(new BigDecimal(locationModel.getLatitude()));
				dlvLocationModel.setLongitude(new BigDecimal(locationModel.getLongitude()));
				dlvLocationModel.setGeocodeConfidence(locationModel.getConfidence());
				dlvLocationModel.setGeocodeQuality(locationModel.getQuality());
			}
		}
	}
	
	public static List getDeliveryBuildings(List lstDlvLocation) {
		
		List result = new ArrayList();
		ILocationModel locationModel = null;
		DlvBuilding dlvLocationModel = null;
		if(lstDlvLocation != null) {
			Iterator iterator = lstDlvLocation.iterator();
			while(iterator.hasNext()) {
				locationModel = new LocationModel();
				dlvLocationModel = (DlvBuilding)iterator.next();
					
				locationModel.setStreetAddress1(dlvLocationModel.getSrubbedStreet());
				locationModel.setStreetAddress2(dlvLocationModel.getSrubbedStreet());
				locationModel.setZipCode(dlvLocationModel.getZip());
				locationModel.setCountry(dlvLocationModel.getCountry());
				result.add(locationModel);
			}
		}
		return result;
	}
	
	public static List getTrnAdminEmployeeList(List kronoList,List roleList){
		
		if(kronoList==null) return new ArrayList();
		
		List finalList=new ArrayList();
		
		for(int i=0;i<kronoList.size();i++){
			EmployeeInfo info=(EmployeeInfo)kronoList.get(i);
						
			List empRoleList=new ArrayList();
			if(roleList!=null && roleList.size()>0)
			{
				 for(int j=0;j<roleList.size();j++){
					EmployeeRole tmpRole=(EmployeeRole)roleList.get(j);
					if(tmpRole.getId().getKronosId().equals(info.getEmployeeId())){
						System.out.println("tmpRole:"+tmpRole.getEmployeeRoleType());
						empRoleList.add(tmpRole);
					}
				 }
				
			}
			WebEmployeeInfo webInfo=new WebEmployeeInfo(info,empRoleList);
			
			finalList.add(webInfo);
			
			
		}
		
		return finalList;
	}
	
	
	public static List constructDispatchModel(Collection planList,Collection routeList,Collection truckList){
		// lot of crap stuff to do
		// form the hashMap with zone as key and list contaiining plan(sort the plan based on sequence number)
		// form the hashMap with zone as key and list containing route(sort by route number)
		// form the hashMap with zone as key and list containing truck(sort by route number)
		// construct Dispatch list from plan list merged with route List and truck List 
		// return the Dispatch List 
		
		Iterator planIterator=planList.iterator();
		Iterator routeIterator=routeList.iterator();
		
		
		System.out.println("planList before:"+planList.size());
		
		Map planMap=new HashMap();
		Map routeMap=new HashMap();	
		// for null zone
		List noZonePlanList=new ArrayList();
		
		while(planIterator.hasNext()){
			Plan plan=(Plan)planIterator.next();
			Zone zone=plan.getZone();
			if(zone==null){
				noZonePlanList.add(plan);
				continue;
			}
			List planTmpList=(List)planMap.get(zone.getZoneCode());
			if(planTmpList==null){ 
				planTmpList=new ArrayList();
				planMap.put(zone.getZoneCode(),planTmpList);
			}
			planTmpList.add(plan);						
		}
						
		Set keySet=planMap.keySet();
		Iterator keyIterator=keySet.iterator();
		while(keyIterator.hasNext()){
			String planKey=(String)keyIterator.next();
			List newPlanList=(List)planMap.get(planKey);
			Collections.sort(newPlanList,PLAN_COMPARATOR);
			
		}
		
		
		while(routeIterator.hasNext()){
			ErpRouteMasterInfo route=(ErpRouteMasterInfo)routeIterator.next();
			String zoneCode=route.getZoneNumber();
			List routeTmpList=(List)routeMap.get(zoneCode);
			if(routeTmpList==null){ 
				routeTmpList=new ArrayList();
				routeMap.put(zoneCode,routeTmpList);
			}
			routeTmpList.add(route);			
		}
		
		Set routeSet=routeMap.keySet();
		Iterator routeTmpIterator=routeSet.iterator();
		while(routeTmpIterator.hasNext()){
			String routeKey=(String)routeTmpIterator.next();
			List newPlanList=(List)routeMap.get(routeKey);
			Collections.sort(newPlanList,ROUTE_COMPARATOR);
		}
		
		// for the dispatch object from the above lists
		List dispatchList=new ArrayList();
		int start_index=0;
		int end_index=0;
		Set finalSet=planMap.keySet();
	    Iterator finalIterator=finalSet.iterator(); 
		while(finalIterator.hasNext()){
			String zoneCode=(String)finalIterator.next();
			start_index=end_index;
			end_index=end_index+((List)planMap.get(zoneCode)).size();
			List routeLst=(List)routeMap.get(zoneCode);
			if(routeLst==null) routeLst=Collections.EMPTY_LIST;
			constructDispatchModelList(dispatchList,(List)planMap.get(zoneCode),routeLst,((List)truckList).subList(start_index,end_index));			
			
		}
		
		start_index=end_index;
		end_index=end_index+noZonePlanList.size();
		constructDispatchModelList(dispatchList,noZonePlanList,Collections.EMPTY_LIST,((List)truckList).subList(start_index,end_index));	    
		return dispatchList;
	}
	
	private static void  constructDispatchModelList(List dispatchList,List planList,List routeList,List truckList){
				
		Iterator ite1=routeList.iterator();
		Iterator trkIterator=truckList.iterator();
		for(int i=0;i<planList.size();i++)
		{
			Plan p=(Plan)planList.get(i);
			Dispatch d=new Dispatch();
			d.setPlanId(p.getPlanId());
			d.setDispatchDate(p.getPlanDate());
			d.setStartTime(p.getStartTime());
			d.setDispatchResources(convertPlnToDispatchResource(p.getPlanResources(),d));						
			d.setFirstDlvTime(p.getFirstDeliveryTime());
			System.out.println("setting the zone :"+p.getZone());			
			d.setZone(p.getZone());
			System.out.println("p.getRegion() :"+p.getRegion());
			d.setBullPen("Y".equalsIgnoreCase(p.getIsBullpen())?Boolean.TRUE:Boolean.FALSE);
			d.setRegion(p.getRegion());
			if(ite1.hasNext()){
				ErpRouteMasterInfo r=(ErpRouteMasterInfo)ite1.next();
				d.setRoute(r.getRouteNumber());
				d.setTruck(r.getTruckNumber());
				System.out.println("r.getRouteNumber() :"+r.getRouteNumber());
				if(r.getFirstDlvTime()!=null && r.getFirstDlvTime().trim().length()>0){					
					try {
						d.setFirstDlvTime(DATE_FORMAT.parse("01/01/1970 "+r.getFirstDlvTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			}			
//			if(trkIterator.hasNext()){
//				ErpTruckMasterInfo trkMaster=(ErpTruckMasterInfo)trkIterator.next();
//				d.setTruck(trkMaster.getTruckNumber());
//			}				  				
				dispatchList.add(d);										
		}		
	}
	
	public static Set convertPlnToDispatchResource(Set planResourceList,Dispatch dispatch){
		Set dispatchResourceList=new HashSet();
		if(planResourceList!=null && planResourceList.size()>0){
			
			Iterator iterator=planResourceList.iterator();
			while(iterator.hasNext()){			
			   PlanResource resource=(PlanResource)iterator.next();
			   DispatchResource dispResource=new DispatchResource();
			   
			   ResourceId id=new ResourceId();			  
			   id.setResourceId(resource.getId().getResourceId());
			   
			   dispResource.setId(id);
			   dispResource.setEmployeeRoleType(resource.getEmployeeRoleType());
			   dispResource.setDispatch(dispatch);
			   dispatchResourceList.add(dispResource);
			}
			
		}
		return dispatchResourceList;
	}
	
	public static void assosiateDispatchToResource(Set dispatchResourceSet,Dispatch dispatch){
		Set dispatchResourceList=new HashSet();
		if(dispatchResourceSet!=null && dispatchResourceSet.size()>0){
			
			Iterator iterator=dispatchResourceSet.iterator();
			while(iterator.hasNext()){			
				DispatchResource resource=(DispatchResource)iterator.next();			   			   
			    resource.getId().setContextId(dispatch.getDispatchId());			    			   
			    resource.setDispatch(dispatch);
			   //dispatchResourceList.add(resource);
			}			
		}
				
	}
	
	public static List mergeRoutes(Collection validRoutes, Collection adHocRoutes){
		List routes = new ArrayList();
		Iterator iter = validRoutes.iterator();
		while(iter.hasNext()){
			ErpRouteMasterInfo erpRouteInfo = (ErpRouteMasterInfo) iter.next();
			RouteInfo routeInfo = new RouteInfo();
			routeInfo.setRouteNumber(erpRouteInfo.getRouteNumber());
			routeInfo.setZoneNumber(erpRouteInfo.getZoneNumber());
			routeInfo.setAdHoc(false);
			routes.add(routeInfo);
		}
		iter = adHocRoutes.iterator();
		while(iter.hasNext()){
			TrnAdHocRoute adHocRouteInfo = (TrnAdHocRoute) iter.next();
			RouteInfo routeInfo = new RouteInfo();
			routeInfo.setRouteNumber(adHocRouteInfo.getRouteNumber());
			routeInfo.setAdHoc(true);
			routes.add(routeInfo);
		}

		return routes;
	}
	public static PlanComparator PLAN_COMPARATOR=new PlanComparator();
	
	public static RouteComparator ROUTE_COMPARATOR=new RouteComparator();
	
	private static class PlanComparator implements Comparator{

		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			if(o1 instanceof Plan && o2 instanceof Plan)
			{
				Plan p1=(Plan)o1;
				Plan p2=(Plan)o2;
				
				if(p1.getSequence()>p2.getSequence()) return 1;
				else if(p1.getSequence()<p2.getSequence()) return -1;
				else return 0;
				
			}
			return 0;
		}
				
	}
	
	
	private static class RouteComparator implements Comparator{

		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			if(o1 instanceof Route && o2 instanceof Route)
			{
				ErpRouteMasterInfo p1=(ErpRouteMasterInfo)o1;
				ErpRouteMasterInfo p2=(ErpRouteMasterInfo)o2;
				
				if(p1.getRouteNumber().compareTo(p2.getRouteNumber())>0) return 1;
				if(p1.getRouteNumber().compareTo(p2.getRouteNumber())<0) return -1;
				else return 0;
				
			}
			return 0;
		}
				
	}

}
