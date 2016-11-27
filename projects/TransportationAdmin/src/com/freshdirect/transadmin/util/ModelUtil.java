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
import com.freshdirect.framework.util.DateRange;
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
import com.freshdirect.transadmin.model.EmployeeSupervisor;
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
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZonetypeResource;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getTrnAdminEmployeeList(Map kronoList, List roleList, List supervisorList) {

		if (kronoList == null)
			return new ArrayList();

		List finalList = new ArrayList();

		Iterator it = kronoList.values().iterator();		
		while (it.hasNext()) {
			EmployeeInfo info = (EmployeeInfo) it.next();
			List empRoleList = new ArrayList();
			if (roleList != null && roleList.size() > 0) {
				for (int j = 0; j < roleList.size(); j++) {
					EmployeeRole tmpRole = (EmployeeRole) roleList.get(j);
					if (tmpRole.getId().getKronosId().equals(info.getEmployeeId())) {
						tmpRole.migrate();
						empRoleList.add(tmpRole);
					}
				}
			}
			EmployeeSupervisor _supervisor = null;
			if (supervisorList != null && supervisorList.size() > 0) {
				for (int k = 0; k < supervisorList.size(); k++) {
					EmployeeSupervisor tmpSup = (EmployeeSupervisor) supervisorList.get(k);
					if (tmpSup.getId().getKronosId().equals(info.getEmployeeId())) {						
						_supervisor = tmpSup;
						EmployeeInfo supervisorInfo = (EmployeeInfo) kronoList.get(_supervisor.getId().getSupervisorId());
						if(supervisorInfo != null) {
							_supervisor.getId().setSupervisorName(supervisorInfo.getLastName()+", "+supervisorInfo.getFirstName());
						}
					}
				}
			}
			
			WebEmployeeInfo webInfo = new WebEmployeeInfo(info, empRoleList, _supervisor);

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
			Collection<WebEmployeeInfo> activeEmployees) {
		Collection<EmployeeInfo> result = new ArrayList<EmployeeInfo>();
		if (activeEmployees != null) {
			for (WebEmployeeInfo emp : activeEmployees) {
				result.add(emp.getEmpInfo());
			}
		}
		return result;
	}

}
