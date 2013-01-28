package com.freshdirect.analytics.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Category;

import com.freshdirect.analytics.DispatchVolumeModel;
import com.freshdirect.delivery.dao.DispatchVolumeDAO;
import com.freshdirect.delivery.dao.IDispatchVolumeDAO;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.HandOffBatchDepotScheduleEx;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IHandOffBatchDepotScheduleEx;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.RouteModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public class DispatchVolumeSessionBean extends SessionBeanSupport {

	private static final Category LOGGER = LoggerFactory.getInstance(DispatchVolumeSessionBean.class);
	
	
	
	public void captureDispatchVolume(Date snapshotTime){
		
		Connection conn = null;
		try 
		{
			RoutingInfoServiceProxy routingInfoProxy = new RoutingInfoServiceProxy();
			RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
			HandOffServiceProxy handoffProxy = new HandOffServiceProxy();
			Map<IAreaModel, List<IRouteModel>> map = new HashMap<IAreaModel, List<IRouteModel>>();
			List<IRouteModel> routes = null;
			Calendar cal = Calendar.getInstance();
			cal = DateUtil.truncate(cal);
			cal.add(Calendar.DATE, 1);
			IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
			String dayOfWeek = DateUtil.formatDayOfWk(cal.getTime());
			Map<String, Map<RoutingTimeOfDay, Set<IHandOffBatchDepotScheduleEx>>> depotScheduleEx = 
					handoffProxy.getHandOffBatchDepotSchedulesEx(dayOfWeek);
			Map<String, IWaveInstance> wavesByDispatchTime = routingInfoProxy.getWavesByDispatchTime(cal.getTime());
			Map<RoutingTimeOfDay, Integer> plantCapacity = routingInfoProxy.getPlantCapacityByDispatchTime(cal.getTime());
			
			Set<IAreaModel> areas = getAreas(wavesByDispatchTime);
			Map<String, Set<RoutingTimeOfDay>> dptCutoff = getDptCutoff(wavesByDispatchTime);
			
			for(IAreaModel area: areas)
			{
				schedulerId.setArea(area);
				schedulerId.setDeliveryDate(cal.getTime());
				if(area.isDepot())
				{
					if(dptCutoff.get(area.getAreaCode())!=null)
					{
						List<IRouteModel> tmpRoutes = new ArrayList<IRouteModel>();
						for(RoutingTimeOfDay cutoff: dptCutoff.get(area.getAreaCode()))
						{
							if(depotScheduleEx.get(area.getAreaCode())!=null && depotScheduleEx.get(area.getAreaCode()).get(cutoff)!=null)
							{
								routes = proxy.getRoutesByCriteria(schedulerId, RoutingDateUtil.getWaveCode(cutoff.getAsDate()));
								routes = convertDptToRegOrders(area.getAreaCode(), routes, depotScheduleEx.get(area.getAreaCode()).get(cutoff));
								if(routes!=null)
								tmpRoutes.addAll(routes);
							}
						}
						routes = tmpRoutes;
					}
				}
				else{ 
					routes = proxy.getRoutesByCriteria(schedulerId, null);
				}
				
				if(routes!=null && routes.size()>0)
				{
					map.put(area, routes);
				}
			}
			assignDispatchTime(map, wavesByDispatchTime);
			
			Map<RoutingTimeOfDay, DispatchVolumeModel> dispatchMap = groupbyDispatch(map, wavesByDispatchTime,plantCapacity,snapshotTime,cal.getTime());
			
			printDispatch(dispatchMap);
			saveDispatch(dispatchMap);
		}
		catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (conn != null) {
						conn.close();
						conn = null;
					}
				} catch (SQLException se) {
					LOGGER.warn("SQLException while cleaning up", se);
				}
			}
			
	}

	private Map<String, Set<RoutingTimeOfDay>> getDptCutoff(
			Map<String, IWaveInstance> wavesByDispatchTime) {
		Map<String, Set<RoutingTimeOfDay>> cutoffs = new HashMap<String, Set<RoutingTimeOfDay>>();
		if(wavesByDispatchTime!=null && wavesByDispatchTime.values()!=null)
		{
			for(IWaveInstance waveInstance : wavesByDispatchTime.values())
			{
				if(waveInstance.getArea().isDepot())
				{
					if(!cutoffs.containsKey(waveInstance.getArea().getAreaCode()))
							cutoffs.put(waveInstance.getArea().getAreaCode(), new HashSet<RoutingTimeOfDay>());
					cutoffs.get(waveInstance.getArea().getAreaCode()).add(waveInstance.getCutOffTime());
				}
			}
		}
		return cutoffs;
	}

	private Set<IAreaModel> getAreas(
			Map<String, IWaveInstance> wavesByDispatchTime) {
		Set<IAreaModel> areas = new HashSet<IAreaModel>();
		if(wavesByDispatchTime!=null && wavesByDispatchTime.values()!=null)
		{
			for(IWaveInstance waveInstance : wavesByDispatchTime.values())
			{
				areas.add(waveInstance.getArea());
			}
		}
		return areas;
	}

	private void printDispatch(Map<RoutingTimeOfDay, DispatchVolumeModel> dispatchMap){
		
		
		for(Entry<RoutingTimeOfDay, DispatchVolumeModel> dispatchEntry: dispatchMap.entrySet())
		{
			System.out.println(dispatchEntry.getKey()+ " "+dispatchEntry.getValue());
		}
	}
	
	private void saveDispatch(Map<RoutingTimeOfDay, DispatchVolumeModel> dispatchMap){
		IDispatchVolumeDAO dispatchVolumeDAO = new DispatchVolumeDAO();
		Connection conn = null;
		try {
			conn = getConnection();
			dispatchVolumeDAO.saveDispatch(conn, dispatchMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(conn!=null)
				conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
	}
	private void assignDispatchTime(Map<IAreaModel, List<IRouteModel>> map,  Map<String, IWaveInstance> wavesByDispatchTime)
	{
		for(List<IRouteModel> routes: map.values())
		{
			for(IRouteModel route: routes)
			{
				if(wavesByDispatchTime.get(route.getWaveId())!=null) route.setDispatchTime(wavesByDispatchTime.get(route.getWaveId()).getDispatchTime());
			}
		}
	}
	
	private Map<RoutingTimeOfDay, DispatchVolumeModel> groupbyDispatch(Map<IAreaModel, List<IRouteModel>> map, Map<String, IWaveInstance> wavesByDispatchTime,Map<RoutingTimeOfDay, Integer> plantCapacity, Date snapshotTime, Date deliveryDate){
		Map<RoutingTimeOfDay, DispatchVolumeModel> dispatchMap = new HashMap<RoutingTimeOfDay, DispatchVolumeModel>();
		
		for(List<IRouteModel> routes: map.values())
		{
			for(IRouteModel route: routes)
			{
				if(route.getDispatchTime()!=null)
				{
					if(!dispatchMap.containsKey(route.getDispatchTime())){ dispatchMap.put(route.getDispatchTime(), new DispatchVolumeModel()); }
					dispatchMap.get(route.getDispatchTime()).setOrderCount(dispatchMap.get(route.getDispatchTime()).getOrderCount()+route.getStops().size());
					dispatchMap.get(route.getDispatchTime()).setDispatchTime(route.getDispatchTime());
					dispatchMap.get(route.getDispatchTime()).setDispatchDate(deliveryDate);
					dispatchMap.get(route.getDispatchTime()).setSnapshotTime(snapshotTime);
				}
				
			}
		}
		
		for(IWaveInstance waveInstance : wavesByDispatchTime.values()){
			if(!dispatchMap.containsKey(waveInstance.getDispatchTime())){ dispatchMap.put(waveInstance.getDispatchTime(), new DispatchVolumeModel()); }
			
			dispatchMap.get(waveInstance.getDispatchTime()).setNoOftrucks(dispatchMap.get(waveInstance.getDispatchTime()).getNoOftrucks()+waveInstance.getNoOfResources());
			dispatchMap.get(waveInstance.getDispatchTime()).setPlantCapacity((plantCapacity!=null && plantCapacity.get(waveInstance.getDispatchTime())!=null)?plantCapacity.get(waveInstance.getDispatchTime()):0);
			dispatchMap.get(waveInstance.getDispatchTime()).setPlannedCapacity(dispatchMap.get(waveInstance.getDispatchTime()).getPlannedCapacity()+calculatePlannedCapacity(waveInstance));
			dispatchMap.get(waveInstance.getDispatchTime()).setDispatchTime(waveInstance.getDispatchTime());
			dispatchMap.get(waveInstance.getDispatchTime()).setDispatchDate(waveInstance.getDeliveryDate());
			dispatchMap.get(waveInstance.getDispatchTime()).setSnapshotTime(snapshotTime);
			
		}
		
		return dispatchMap;
	}
	
	private int calculatePlannedCapacity(IWaveInstance waveInstance){
		int capacity = (int) Math.round(((double)waveInstance.getMaxRunTime()/3600) * waveInstance.getNoOfResources() * waveInstance.getArea().getDeliveryRate());
		return capacity;
	}
	private class CustomTruckScheduleInfo extends HandOffBatchDepotScheduleEx {
		
		List orders = null;
				
		public CustomTruckScheduleInfo(List orders, IHandOffBatchDepotScheduleEx info) {
			super();
			this.orders = orders;
			this.setArea(info.getArea());
			this.setDepotArrivalTime(info.getDepotArrivalTime());
			this.setTruckDepartureTime(info.getTruckDepartureTime());
			this.setOriginId(info.getOriginId());
		}
				
		public List getOrders() {
			return orders;
		}
		
		public void setOrders(List orders) {
			this.orders = orders;
		}
		
		public void addOrder(Object order) {
			this.orders.add(order);
		}
		
		public String toString() {
			//return orders.toString()+","+super.toString()+"\n";
			return super.toString()+"\n";
		}
	}
		
	
	private class TruckScheduleComparator implements Comparator {		
		
		public int compare(Object obj1, Object obj2){

			Date arrivalData1 = ( (IHandOffBatchDepotScheduleEx) obj1).getDepotArrivalTime();
			Date arrivalData2 = ( (IHandOffBatchDepotScheduleEx) obj2).getDepotArrivalTime();			
			return arrivalData1.compareTo(arrivalData2);
		}		
	}
	private List<IRouteModel> convertDptToRegOrders(String area, List<IRouteModel> dptRoutes, 
			Set<IHandOffBatchDepotScheduleEx> depotScheduleEx) {
		
		int allowedDepartTimeDiff = RoutingServicesProperties.getDepotDepartTimeDiff();
		List<CustomTruckScheduleInfo> groupSchedule = new ArrayList<CustomTruckScheduleInfo>();
		
		if(dptRoutes != null && depotScheduleEx != null) 
		{
			List<IRouteModel> newRoutes = new ArrayList<IRouteModel>();
			
					for(IHandOffBatchDepotScheduleEx _schInfo : depotScheduleEx) 
					{
						groupSchedule.add(new CustomTruckScheduleInfo(new ArrayList(),_schInfo));
					}
					Collections.sort(groupSchedule, new TruckScheduleComparator());
					
					Date currDepotDeparture = null;
					String currRouteId = null;
						for(IRouteModel route : dptRoutes) {
							Iterator itr = route.getStops().iterator();
							IRoutingStopModel _order = null;
							String routeID = route.getRouteId();
							CustomTruckScheduleInfo _matchSchedule = null;
							
							while(itr.hasNext()) {
								_order = (IRoutingStopModel)itr.next();
								
								if(currRouteId == null) {
									currRouteId = routeID;
								}
							
								if(_order.getOrderNumber() == null || _order.getOrderNumber().trim().length() == 0
										//|| IRoutingStopModel.DEPOT_STOPNO.equalsIgnoreCase(_order.getOrderNumber())) {
										|| _order.getOrderNumber().startsWith(IRoutingStopModel.DEPOT_STOPNO)) {
									currDepotDeparture = _order.getStopArrivalTime();
									continue;
								} else if(currRouteId != null && !currRouteId.equalsIgnoreCase(routeID)) {
									currDepotDeparture = null;
									currRouteId = routeID;
								}
								if(currDepotDeparture == null) {
									if(allowedDepartTimeDiff != 0 
												&& RoutingDateUtil.getDiffInHours(_order.getDeliveryInfo().getDeliveryStartTime()
																						, route.getStartTime())
												> allowedDepartTimeDiff) {
										_order.setStopDepartureTime(_order.getDeliveryInfo().getDeliveryStartTime());
									} else {
										_order.setStopDepartureTime(route.getStartTime());
									}
								} else {
									_order.setStopDepartureTime(currDepotDeparture);
								}
								_matchSchedule = matchSchedule(_order
																, groupSchedule
																, route.getOriginId());
								if(_matchSchedule != null) {
									_order.setRoutingRouteId(routeID); //_order.setRouteStartTime(_matchSchedule.getTruckDepartureTime());
									_matchSchedule.addOrder(_order);
								} else {
									//throw new RoutingServiceException("Invalid Depot Truck Schedule File : SAP Order No:" + _order.getOrderNumber()
											//, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
									
								}
							}
						}
						return updateDptRouteStop(area, groupSchedule);
			
		}
		return null;
		}
		private List<IRouteModel> updateDptRouteStop(String area, List<CustomTruckScheduleInfo> groupSchedule) {
			
			List<IRouteModel> result = new ArrayList<IRouteModel>();			
			int intRountCount = 0;					
				for(CustomTruckScheduleInfo route : groupSchedule) {
					
					intRountCount++;
					IRouteModel newRoute = new RouteModel(); 
					
					newRoute.setRouteId(area+"-"+intRountCount);
					newRoute.setStartTime(route.getTruckDepartureTime());
					newRoute.setCompletionTime(route.getDepotArrivalTime());
					newRoute.setOriginId(route.getOriginId());
					newRoute.setStops(new TreeSet());
					newRoute.setDispatchTime(new RoutingTimeOfDay(route.getTruckDepartureTime()));
					result.add(newRoute);
					
					List orders = route.getOrders();
					
					Iterator itr = orders.iterator();
					IRoutingStopModel _order = null;
					
					int intStopCount = 0;
					
					while(itr.hasNext()) {
						_order = (IRoutingStopModel)itr.next();
						
						intStopCount++;
						_order.setStopNo(intStopCount);
						
						newRoute.appendRoutingRoute(_order.getRoutingRouteId());
						newRoute.getStops().add(_order);
						
					}
				}
			return result;
		}
		private CustomTruckScheduleInfo matchSchedule(IRoutingStopModel order, List scheduleLst, String routeOriginId) {
			
			CustomTruckScheduleInfo _schInfo = null;
			CustomTruckScheduleInfo _preSchInfo = null;
			
			if(scheduleLst != null) {
				
				Iterator _itrSchedule = scheduleLst.iterator();
										
				while(_itrSchedule.hasNext()) {
					_schInfo = (CustomTruckScheduleInfo)_itrSchedule.next();
					
					if(_schInfo.getOriginId() != null &&
							routeOriginId != null && routeOriginId.equalsIgnoreCase(_schInfo.getOriginId())) {
						if(_schInfo.getDepotArrivalTime().after(order.getStopDepartureTime())) {
							break;
						} else {
							_preSchInfo = _schInfo;
						}
					}
				}
			}
			return _preSchInfo;
		}
	

}