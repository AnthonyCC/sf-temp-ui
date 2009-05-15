package com.freshdirect.transadmin.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.RoutingStopModel;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.Route;
import com.freshdirect.transadmin.model.RouteDecorator;
import com.freshdirect.transadmin.model.RouteInfo;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class ModelUtil {

	private static final DateFormat DATE_FORMAT=new SimpleDateFormat("MM/dd/yyyy H:m:s a");

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
//						System.out.println("tmpRole:"+tmpRole.getEmployeeRoleType());
						empRoleList.add(tmpRole);
					}
				 }

			}
			WebEmployeeInfo webInfo=new WebEmployeeInfo(info,empRoleList);

			finalList.add(webInfo);


		}

		return finalList;
	}

	public static String getRegionCode(Collection zones,String zoneCode)
	{
		String result="";
		Iterator zoneIterator=zones.iterator();
		while(zoneIterator.hasNext())
		{
			Zone zone=(Zone)zoneIterator.next();
			if(zone!=null&&zone.getZoneCode().equals(zoneCode))
			{
				return zone.getRegion().getCode();
			}
		}
		
		return result;
	}
	public static Zone getZone(Collection zones,String zoneCode)
	{		
		Iterator zoneIterator=zones.iterator();
		while(zoneIterator.hasNext())
		{
			Zone zone=(Zone)zoneIterator.next();
			if(zone!=null&&zone.getZoneCode().equals(zoneCode))
			{
				return zone;
			}
		}		
		return null;
	}
	public static List getRoute(List routeDecor)
	{		
		List result=new ArrayList();
		for(int i=0,n=routeDecor.size();i<n;i++)
		{
			RouteDecorator decor=(RouteDecorator)routeDecor.get(i);
			result.add(decor.getRoute());
		}
		return result;
	}
	//region wise auto dispatch
	public static List constructDispatchModel(Collection planList,Collection routeList,Collection zones)
	{
		Iterator planIterator=planList.iterator();
		Iterator routeIterator=routeList.iterator();
		
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
			String regionCode=getRegionCode(zones,zone.getZoneCode());
			List planTmpList=(List)planMap.get(regionCode);
			if(planTmpList==null){
				planTmpList=new ArrayList();
				planMap.put(regionCode,planTmpList);
			}
			planTmpList.add(plan);
		}

		Set keySet=planMap.keySet();
		Iterator keyIterator=keySet.iterator();
		while(keyIterator.hasNext()){
			String regionKey=(String)keyIterator.next();
			List newPlanList=(List)planMap.get(regionKey);
			Collections.sort(newPlanList,PLAN_REGION_COMPARATOR);

		}


		while(routeIterator.hasNext())
		{
			ErpRouteMasterInfo route=(ErpRouteMasterInfo)routeIterator.next();
			String zoneCode=route.getZoneNumber();
			String regionCode=getRegionCode(zones,zoneCode);
			Zone zone=getZone(zones, zoneCode);
			RouteDecorator routeDecor=new RouteDecorator(route,zone);
			List routeTmpList=(List)routeMap.get(regionCode);
			if(routeTmpList==null){
				routeTmpList=new ArrayList();
				routeMap.put(regionCode,routeTmpList);
			}
			routeTmpList.add(routeDecor);
		}

		Set routeSet=routeMap.keySet();
		Iterator routeTmpIterator=routeSet.iterator();
		while(routeTmpIterator.hasNext()){
			String routeKey=(String)routeTmpIterator.next();
			List newPlanList=(List)routeMap.get(routeKey);
			Collections.sort(newPlanList,ROUTE_REGION_COMPARATOR);
		}

		// for the dispatch object from the above lists
		List dispatchList=new ArrayList();
		
		Set finalSet=planMap.keySet();
	    Iterator finalIterator=finalSet.iterator();
		while(finalIterator.hasNext())
		{
			String regionCode=(String)finalIterator.next();			
			List routeLst=(List)routeMap.get(regionCode);
			if(routeLst==null) routeLst=Collections.EMPTY_LIST;
			else routeLst=getRoute(routeLst);
			constructDispatchModelList(dispatchList,(List)planMap.get(regionCode),routeLst);

		}

		
		constructDispatchModelList(dispatchList,noZonePlanList,Collections.EMPTY_LIST);
		return dispatchList;
	}

	public static List constructDispatchModel(Collection planList,Collection routeList){
		// lot of crap stuff to do
		// form the hashMap with zone as key and list contaiining plan(sort the plan based on sequence number)
		// form the hashMap with zone as key and list containing route(sort by route number)
		// form the hashMap with zone as key and list containing truck(sort by route number)
		// construct Dispatch list from plan list merged with route List and truck List
		// return the Dispatch List

		Iterator planIterator=planList.iterator();
		Iterator routeIterator=routeList.iterator();


		//System.out.println("planList before:"+planList.size());

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
			//Collections.sort(newPlanList,PLAN_COMPARATOR);

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
		//int start_index=0;
		//int end_index=0;
		Set finalSet=planMap.keySet();
	    Iterator finalIterator=finalSet.iterator();
		while(finalIterator.hasNext()){
			String zoneCode=(String)finalIterator.next();
			//start_index=end_index;
			//end_index=end_index+((List)planMap.get(zoneCode)).size();
			List routeLst=(List)routeMap.get(zoneCode);
			if(routeLst==null) routeLst=Collections.EMPTY_LIST;
			constructDispatchModelList(dispatchList,(List)planMap.get(zoneCode),routeLst);

		}

		//start_index=end_index;
		//end_index=end_index+noZonePlanList.size();
		constructDispatchModelList(dispatchList,noZonePlanList,Collections.EMPTY_LIST);
		return dispatchList;
	}

	
	
	private static void  constructDispatchModelList(List dispatchList,List planList,List routeList){

		Iterator ite1=routeList.iterator();
		ErpRouteMasterInfo r = null;
		Date firstDlvTime = null;

		for(int i=0;i<planList.size();i++)
		{
			Plan p=(Plan)planList.get(i);
			Dispatch d=new Dispatch();
			d.setPlanId(p.getPlanId());
			d.setDispatchDate(p.getPlanDate());
			d.setStartTime(p.getStartTime());
			d.setSupervisorId(p.getSupervisorId());
			d.setDispatchResources(convertPlnToDispatchResource(p.getPlanResources(),d));
			d.setFirstDlvTime(p.getFirstDeliveryTime());
			//System.out.println("setting the zone :"+p.getZone());
			d.setZone(p.getZone());
			//System.out.println("p.getRegion() :"+p.getRegion());
			d.setBullPen("Y".equalsIgnoreCase(p.getIsBullpen())?Boolean.TRUE:Boolean.FALSE);
			d.setRegion(p.getRegion());

			List routeMatch = matchRoute(p, routeList);

			if(routeMatch != null) {
				r = (ErpRouteMasterInfo)routeMatch.get(0);
				firstDlvTime = (Date)routeMatch.get(1);
				d.setRoute(r.getRouteNumber());
				d.setTruck(r.getTruckNumber());
				d.setFirstDlvTime(firstDlvTime);
				r = null;
				firstDlvTime = null;
			}
			dispatchList.add(d);
		}
	}

	private static List matchRoute(Plan p, List routeList) {
		List result = null;
		if(routeList != null && p != null) {
			Iterator _iterator = routeList.iterator();
			ErpRouteMasterInfo _tmpInfo = null;
			while(_iterator.hasNext()) {
				_tmpInfo = (ErpRouteMasterInfo)_iterator.next();
				try {
					if(_tmpInfo.getFirstDlvTime() != null && _tmpInfo.getFirstDlvTime().trim().length() > 0){
						Date firstDlvTime = DATE_FORMAT.parse("01/01/1970 "+_tmpInfo.getFirstDlvTime()+" "+_tmpInfo.getRouteTime());
						Date firstDlvTime1 = DATE_FORMAT.parse("01/01/1970 "+"12:00:00"+" "+"PM");
						//if(firstDlvTime != null && firstDlvTime.equals(p.getFirstDeliveryTime()))
						long l1=firstDlvTime.getTime();
						long l2=p.getFirstDeliveryTime().getTime();
						long l3=firstDlvTime1.getTime();
						if(firstDlvTime != null &&p.getFirstDeliveryTime()!=null&& firstDlvTime.getTime()==p.getFirstDeliveryTime().getTime()) 
						{
							result = new ArrayList();
							result.add(_tmpInfo);
							result.add(firstDlvTime);
							_iterator.remove();
							break;
						}
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
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
	
	public static IRoutingStopModel getStop(int id, String line1, String city, String state, String zipCode,
			String latitude, String longitude, boolean isDepot) {

		IRoutingStopModel _stop = new RoutingStopModel(id);
		_stop.setDepot(isDepot);

		ILocationModel _locModel = new LocationModel();

		_locModel.setStreetAddress1(line1);
		_locModel.setCity(city); 
		_locModel.setState(state);
		_locModel.setZipCode(zipCode);

		_stop.setLocation(_locModel);

		IGeographicLocation _geoLocModel = new GeographicLocation();
		_geoLocModel.setLatitude(latitude);
		_geoLocModel.setLongitude(longitude);

		_locModel.setGeographicLocation(_geoLocModel);

		return _stop;
	}
	
	public static PlanComparator PLAN_COMPARATOR=new PlanComparator();

	public static RouteComparator ROUTE_COMPARATOR=new RouteComparator();
	
	public static PlanRegionComparator PLAN_REGION_COMPARATOR=new PlanRegionComparator();

	public static RouteRegionComparator ROUTE_REGION_COMPARATOR=new RouteRegionComparator();

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
	
	private static class PlanRegionComparator implements Comparator{

		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			if(o1 instanceof Plan && o2 instanceof Plan)
			{
				Plan p1=(Plan)o1;
				Plan p2=(Plan)o2;
				if(p1.getZone().getPriority().intValue()==-1) return 1;
				if(p2.getZone().getPriority().intValue()==-1) return -1;
				return p1.getZone().getPriority().intValue()-p2.getZone().getPriority().intValue();
			}
			return 0;
		}

	}


	private static class RouteRegionComparator implements Comparator
	{

		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			if(o1 instanceof RouteDecorator && o2 instanceof RouteDecorator)
			{
				RouteDecorator r1=(RouteDecorator)o1;
				RouteDecorator r2=(RouteDecorator)o2;
				if(r1.getZone().getPriority().intValue()==-1) return 1;
				if(r2.getZone().getPriority().intValue()==-1) return -1;
				return r1.getZone().getPriority().intValue()-r2.getZone().getPriority().intValue();

			}
			return 0;
		}

	}	
	public static List mapToList(Map input) {
		
		List output = new ArrayList();
				
		if(input != null) {
			
			Iterator iterator = input.values().iterator();			
			while(iterator.hasNext()) {				
				output.addAll((List)iterator.next());
			}
		}
		return output;
	}

}
