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
import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.RoutingStopModel;
import com.freshdirect.transadmin.constants.EnumDispatchType;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeTeam;
import com.freshdirect.transadmin.model.EmployeeTruckPreference;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.Route;
import com.freshdirect.transadmin.model.RouteDecorator;
import com.freshdirect.transadmin.model.RouteInfo;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class ModelUtil {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy H:m:s a");

	public static List getDeliveryLocations(List lstDlvLocation) {

		List result = new ArrayList();
		ILocationModel locationModel = null;
		IBuildingModel building = null;
		
		DlvLocation dlvLocationModel = null;
		if(lstDlvLocation != null) {
			Iterator iterator = lstDlvLocation.iterator();
			while(iterator.hasNext()) {
				
				dlvLocationModel = (DlvLocation)iterator.next();
				
				building = new BuildingModel();
								
				building.setCity(dlvLocationModel.getBuilding().getCity());
				building.setState(dlvLocationModel.getBuilding().getState());
				building.setSrubbedStreet(dlvLocationModel.getBuilding().getSrubbedStreet());
				building.setStreetAddress1(dlvLocationModel.getBuilding().getSrubbedStreet());
				building.setStreetAddress2(dlvLocationModel.getBuilding().getSrubbedStreet());
				building.setZipCode(dlvLocationModel.getBuilding().getZip());
				building.setCountry(dlvLocationModel.getBuilding().getCountry());
				locationModel = new LocationModel(building);
				locationModel.setApartmentNumber(dlvLocationModel.getApartment());
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
		IBuildingModel building = null;
		DlvBuilding dlvLocationModel = null;
		
		if(lstDlvLocation != null) {
			Iterator iterator = lstDlvLocation.iterator();
			while(iterator.hasNext()) {
				
				dlvLocationModel = (DlvBuilding)iterator.next();
				
				building = new BuildingModel();
				building.setSrubbedStreet(dlvLocationModel.getSrubbedStreet());
				building.setStreetAddress1(dlvLocationModel.getSrubbedStreet());
				building.setStreetAddress2(dlvLocationModel.getSrubbedStreet());
				building.setZipCode(dlvLocationModel.getZip());
				building.setCountry(dlvLocationModel.getCountry());
								
				locationModel = new LocationModel(building);
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
						tmpRole.migrate();
						empRoleList.add(tmpRole);
					}
				 }

			}
			WebEmployeeInfo webInfo=new WebEmployeeInfo(info,empRoleList);

			finalList.add(webInfo);


		}

		return finalList;
	}
	
	public static List getTrnAdminEmployeeList(List empByRoleList, List roleList, List truckPrefList){

		if (empByRoleList == null)
			return new ArrayList();

		List empTruckPrefInfoList = new ArrayList();

		for (int i = 0; i < empByRoleList.size(); i++) {
			EmployeeInfo info = (EmployeeInfo) empByRoleList.get(i);

			List empRoleList = new ArrayList();
			if (roleList != null && roleList.size() > 0) {
				for (int j = 0; j < roleList.size(); j++) {
					EmployeeRole tmpRole = (EmployeeRole) roleList.get(j);
					if (tmpRole.getId().getKronosId().equals(
							info.getEmployeeId())) {
						tmpRole.migrate();
						empRoleList.add(tmpRole);
					}
				}
			}

			Map empTruckPrefMap = new HashMap();
			if (truckPrefList != null && truckPrefList.size() > 0) {
				for (int j = 0; j < truckPrefList.size(); j++) {
					EmployeeTruckPreference tmpPref = (EmployeeTruckPreference) truckPrefList
							.get(j);
					if (tmpPref.getId().getKronosId().equals(
							info.getEmployeeId())) {

						empTruckPrefMap.put(tmpPref.getId().getPrefKey(),
								tmpPref.getTruckNumber());
					}
				}
			}
			WebEmployeeInfo webInfo = new WebEmployeeInfo(info, empRoleList,
					empTruckPrefMap);

			empTruckPrefInfoList.add(webInfo);
		}

		return empTruckPrefInfoList;
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
	@SuppressWarnings("unchecked")
	public static List constructDispatchModel(Collection planList, Collection routeList) {
		
		List<Dispatch> dispatchLst = new ArrayList<Dispatch>();
		List<Plan> bullpens = new ArrayList<Plan>();
	
		Map<String, List<Plan>> planMapping = new HashMap<String, List<Plan>>();
		Map<String, List<ErpRouteMasterInfo>> routeMapping = new HashMap<String, List<ErpRouteMasterInfo>>();
		
		Iterator<Plan> planItr = planList.iterator();
		while(planItr.hasNext()){
			Plan _plan = planItr.next();
			if(_plan.getZone() == null && "Y".equalsIgnoreCase(_plan.getIsBullpen())){
				bullpens.add(_plan);
				continue;
			}
			if(_plan.getZone() == null && _plan.getDestinationFacility() != null
					&& EnumTransportationFacilitySrc.CROSSDOCK.getName().equalsIgnoreCase(_plan.getDestinationFacility().getTrnFacilityType().getName())){
				if(!planMapping.containsKey(_plan.getDestinationFacility().getRoutingCode())){
					planMapping.put(_plan.getDestinationFacility().getRoutingCode(), new ArrayList<Plan>());
				}
				planMapping.get(_plan.getDestinationFacility().getRoutingCode()).add(_plan);
				continue;
			}
			if(!planMapping.containsKey(_plan.getZoneCode())){
				planMapping.put(_plan.getZoneCode(), new ArrayList<Plan>());
			}
			planMapping.get(_plan.getZoneCode()).add(_plan);		
		}
		
		Iterator<ErpRouteMasterInfo> routeItr = routeList.iterator();
		while(routeItr.hasNext()){
			ErpRouteMasterInfo _route = routeItr.next();
			if(!TransStringUtil.isEmpty(_route.getZoneNumber())){
				if(!routeMapping.containsKey(_route.getZoneNumber())){				
					routeMapping.put(_route.getZoneNumber(), new ArrayList<ErpRouteMasterInfo>());				
				}
				routeMapping.get(_route.getZoneNumber()).add(_route);	
			} else {
				if(!routeMapping.containsKey(_route.getRouteNumber().substring(1,4))){
					routeMapping.put(_route.getRouteNumber().substring(1,4), new ArrayList<ErpRouteMasterInfo>());
				}
				routeMapping.get(_route.getRouteNumber().substring(1,4)).add(_route);	
			}					
		}		
		
		Set finalKeySet = planMapping.keySet();
		Iterator<String> finalBatchPlanItr = finalKeySet.iterator();
		while(finalBatchPlanItr.hasNext()){
			String zone = finalBatchPlanItr.next();
			List<ErpRouteMasterInfo> zoneRouteList = routeMapping.get(zone);
			if(zoneRouteList == null) 
				zoneRouteList = Collections.EMPTY_LIST;
			constructDispatchModelList(dispatchLst, (List<Plan>)planMapping.get(zone), zoneRouteList);
		}
				
		//Bull-pen Plan List
		constructDispatchModelList(dispatchLst, bullpens, Collections.EMPTY_LIST);
							
		return dispatchLst;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Plan> getRunnerPlans(List<Plan> zoneRouteList){
		List<Plan> runnerPlans = new ArrayList<Plan>();
		if(zoneRouteList!=null && zoneRouteList.size() > 0) {
			for (Iterator<Plan> k = zoneRouteList.iterator(); k.hasNext();) {
				Plan p = k.next();
				if(p.getOriginFacility().getTrnFacilityType().getName().equals(EnumTransportationFacilitySrc.DEPOTDELIVERY.getName())){
					runnerPlans.add(p);
					k.remove();
				}
			}
		}
		return runnerPlans;
	}
	
	@SuppressWarnings("unchecked")
	private static void constructDispatchModelList(
			List<Dispatch> dispatchLst,
			List<Plan> zonePlanList, List<ErpRouteMasterInfo> zoneRouteList) {
		
		ErpRouteMasterInfo r = null;
		Date firstDlvTime = null;
		
		List<Plan> runnerPlans = getRunnerPlans(zonePlanList);

		Iterator<Plan> planItr = zonePlanList.iterator();
		while (planItr.hasNext()) {
			boolean isTrailerPlan = false;
			Plan p = planItr.next();

			Dispatch d = new Dispatch();
			d.setPlanId(p.getPlanId());
			d.setDispatchDate(p.getPlanDate());
			d.setOriginFacility(p.getOriginFacility());
			d.setDestinationFacility(p.getDestinationFacility());
			d.setStartTime(p.getStartTime());
			d.setSupervisorId(p.getSupervisorId());
			d.setDispatchResources(convertPlnToDispatchResource(p.getPlanResources(),d));
			d.setFirstDlvTime(p.getFirstDeliveryTime());
			d.setBullPen("Y".equalsIgnoreCase(p.getIsBullpen())? Boolean.TRUE:Boolean.FALSE);
			d.setRegion(p.getRegion());
			d.setDispatchType(EnumDispatchType.ROUTEDISPATCH.getName());
			if(p.getZone() == null && p.getDestinationFacility() != null
					&& EnumTransportationFacilitySrc.CROSSDOCK.getName().equalsIgnoreCase(p.getDestinationFacility().getTrnFacilityType().getName())){
				isTrailerPlan = true;
			}
			if(p.getZone()!=null && p.getZone().getArea()!=null && "X".equals(p.getZone().getArea().getIsDepot()) && 
					p.getDestinationFacility()!=null && p.getDestinationFacility().getTrnFacilityType()!=null &&
					 p.getDestinationFacility().getTrnFacilityType().getName().equals(EnumTransportationFacilitySrc.DEPOTDELIVERY.getName()))
			{
				int runnerCount = 0;
				for ( Iterator<Plan> k = runnerPlans.iterator(); k.hasNext()&& runnerCount<6;) 
					{
						Plan runnerPlan = k.next();
						if(runnerPlan.getOriginFacility().equals(p.getDestinationFacility()) && 
								(runnerPlan.getFirstDeliveryTime().after(p.getFirstDeliveryTime()) || runnerPlan.getFirstDeliveryTime().equals(p.getFirstDeliveryTime())) &&  
								(runnerPlan.getLastDeliveryTime().before(p.getLastDeliveryTime())) && 
								runnerPlan.getPlanResources()!=null && runnerPlan.getPlanResources().size()>0)
						{
							
							d.getDispatchResources().addAll(convertPlnToDispatchResource(runnerPlan.getPlanResources(),d));
							k.remove();
							runnerCount++;
						}
					}
			}
			List routeMatch = matchRoute(p, zoneRouteList, isTrailerPlan);
			if(routeMatch != null) {
				r = (ErpRouteMasterInfo)routeMatch.get(0);
				firstDlvTime = (Date)routeMatch.get(1);
				d.setRoute(r.getRouteNumber());
				d.setTruck(r.getTruckNumber());
				d.setFirstDlvTime(firstDlvTime);
				d.setZone(new Zone());
				d.getZone().setZoneCode(r.getZoneNumber());
				r = null;
				firstDlvTime = null;
			}
			dispatchLst.add(d);
		}
	}

	public static List constructDispatchModelOld(Collection planList,Collection routeList){
		// lot of crap stuff to do
		// form the hashMap with zone as key and list contaiining plan(sort the plan based on sequence number)
		// form the hashMap with zone as key and list containing route(sort by route number)
		// form the hashMap with zone as key and list containing truck(sort by route number)
		// construct Dispatch list from plan list merged with route List and truck List
		// return the Dispatch List

		Iterator<Plan> planIterator = planList.iterator();
		Iterator<ErpRouteMasterInfo> routeIterator = routeList.iterator();		

		Map planMap=new HashMap();
		Map routeMap=new HashMap();

		List<Plan> noZonePlanList = new ArrayList<Plan>();

		while(planIterator.hasNext()){
			Plan plan = planIterator.next();
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
		}


		while(routeIterator.hasNext()){
			ErpRouteMasterInfo route = routeIterator.next();
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

		Set finalSet=planMap.keySet();
	    Iterator finalIterator=finalSet.iterator();
		while(finalIterator.hasNext()){
			String zoneCode=(String)finalIterator.next();
			List routeLst=(List)routeMap.get(zoneCode);
			if (routeLst == null)
				routeLst = Collections.EMPTY_LIST;
			constructDispatchModelListOld(dispatchList
											, (List) planMap.get(zoneCode)
											, routeLst);
		}

		constructDispatchModelListOld(dispatchList
											,noZonePlanList
												,Collections.EMPTY_LIST);
		return dispatchList;
	}

	
	
	@SuppressWarnings("unchecked")
	private static void  constructDispatchModelListOld(List dispatchList,List planList,List routeList){

		Iterator ite1=routeList.iterator();
		ErpRouteMasterInfo r = null;
		Date firstDlvTime = null;

		for(int i=0;i<planList.size();i++)
		{
			Plan p=(Plan)planList.get(i);
			Dispatch d=new Dispatch();
			d.setPlanId(p.getPlanId());
			d.setDispatchDate(p.getPlanDate());
			d.setOriginFacility(p.getOriginFacility());
			d.setDestinationFacility(p.getDestinationFacility());
			d.setStartTime(p.getStartTime());
			d.setSupervisorId(p.getSupervisorId());
			d.setDispatchResources(convertPlnToDispatchResource(p.getPlanResources(),d));
			d.setFirstDlvTime(p.getFirstDeliveryTime());
			//System.out.println("setting the zone :"+p.getZone());
			
			//System.out.println("p.getRegion() :"+p.getRegion());
			d.setBullPen("Y".equalsIgnoreCase(p.getIsBullpen())?Boolean.TRUE:Boolean.FALSE);
			d.setRegion(p.getRegion());

			List routeMatch = matchRoute(p, routeList, false);

			if(routeMatch != null) {
				r = (ErpRouteMasterInfo)routeMatch.get(0);
				firstDlvTime = (Date)routeMatch.get(1);
				d.setRoute(r.getRouteNumber());
				d.setTruck(r.getTruckNumber());
				d.setFirstDlvTime(firstDlvTime);
				d.setZone(new Zone());
				d.getZone().setZoneCode(r.getZoneNumber());
				r = null;
				firstDlvTime = null;
			}
			dispatchList.add(d);
		}
	}

	private static List matchRoute(Plan p, List routeList, boolean isTrailerPlan) {
		List result = null;
		if(routeList != null && p != null) {
			Iterator _iterator = routeList.iterator();
			ErpRouteMasterInfo _tmpInfo = null;
			while(_iterator.hasNext()) {
				_tmpInfo = (ErpRouteMasterInfo)_iterator.next();
				try {
					if (_tmpInfo.getFirstDlvTime() != null
							&& _tmpInfo.getFirstDlvTime().trim().length() > 0) {
						Date routeFirstDlvTime = DATE_FORMAT
								.parse("01/01/1970 "+ _tmpInfo.getFirstDlvTime() + " " + _tmpInfo.getRouteTime());
						Date planFirstDlvTime = isTrailerPlan ? p.getStartTime() : p.getFirstDeliveryTime();
						if (routeFirstDlvTime != null
								&& planFirstDlvTime != null
								&& routeFirstDlvTime.getTime() == planFirstDlvTime.getTime()) {
							result = new ArrayList();
							result.add(_tmpInfo);
							result.add(routeFirstDlvTime);
							_iterator.remove();
							break;
						}
					}
				} catch (ParseException e) {
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
			routeInfo.setTruckNumber(erpRouteInfo.getTruckNumber());
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
		
		IGeographicLocation _geoLocModel = new GeographicLocation();
		_geoLocModel.setLatitude(latitude);
		_geoLocModel.setLongitude(longitude);
		
		
		IBuildingModel bmodel = new BuildingModel();
		bmodel.setGeographicLocation(_geoLocModel);
		bmodel.setStreetAddress1(line1);
		bmodel.setCity(city); 
		bmodel.setState(state);
		bmodel.setZipCode(zipCode);
		
		ILocationModel _locModel = new LocationModel(bmodel);
		
		DeliveryModel deliveryInfo = new DeliveryModel();
		deliveryInfo.setDeliveryLocation(_locModel);
		_stop.setDeliveryInfo(deliveryInfo);
		
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
				if(r1.getZone()!=null && r2.getZone()!=null){
					if(r1.getZone().getPriority().intValue()==-1) return 1;
					if(r2.getZone().getPriority().intValue()==-1) return -1;
					return r1.getZone().getPriority().intValue()-r2.getZone().getPriority().intValue();
				}
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
	
	public static Map<String, EmployeeInfo> getIdMappedEmployees(
			Collection<WebEmployeeInfo> activeEmployees,
			Collection<WebEmployeeInfo> terminatedEmployees) {
		Map<String, EmployeeInfo> result = new HashMap<String, EmployeeInfo>();
		if (activeEmployees != null) {
			for (WebEmployeeInfo emp : activeEmployees) {
				result.put(emp.getEmployeeId(), emp.getEmpInfo());
			}
		}
		if (terminatedEmployees != null) {
			for (WebEmployeeInfo emp : terminatedEmployees) {
				result.put(emp.getEmployeeId(), emp.getEmpInfo());
			}
		}
		return result;
	}
	
	public static Map<String, String> getIdMappedTeam(Collection<EmployeeTeam> teamInfos) {
		Map<String, String> result = new HashMap<String, String>();
		if (teamInfos != null) {
			for (EmployeeTeam emp : teamInfos) {
				result.put(emp.getKronosId(), emp.getLeadKronosId());
			}
		}
		return result;
	}
	
	public static Map<String, ScheduleEmployeeInfo> getIdMappedSchedule(Collection<ScheduleEmployeeInfo> schedule) {
		Map<String, ScheduleEmployeeInfo> result = new HashMap<String, ScheduleEmployeeInfo>();
		if (schedule != null) {
			for (ScheduleEmployeeInfo sch : schedule) {
				result.put(sch.getEmployeeId(), sch);
			}
		}
		return result;
	}

	public static Collection<EmployeeInfo> getEmployees(
			Collection<WebEmployeeInfo> activeEmployees,
			Collection<WebEmployeeInfo> terminatedEmployees) {
		Collection<EmployeeInfo> result = new ArrayList<EmployeeInfo>();
		if (activeEmployees != null) {
			for (WebEmployeeInfo emp : activeEmployees) {
				result.add(emp.getEmpInfo());
			}
		}
		if (terminatedEmployees != null) {
			for (WebEmployeeInfo emp : terminatedEmployees) {
				result.add(emp.getEmpInfo());
			}
		}
		return result;
	}

}
