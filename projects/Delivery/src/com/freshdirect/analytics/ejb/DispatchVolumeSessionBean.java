package com.freshdirect.analytics.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
			List<Date> deliveryDates = routingInfoProxy.getDeliveryDates();
			
			for(Date deliveryDate : deliveryDates){
			Map<IAreaModel, List<IRouteModel>> map = new HashMap<IAreaModel, List<IRouteModel>>();
			List<IRouteModel> routes = null;
			IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
			String dayOfWeek = DateUtil.formatDayOfWk(deliveryDate);
			Map<String, Map<RoutingTimeOfDay, Set<IHandOffBatchDepotScheduleEx>>> depotScheduleEx = 
					handoffProxy.getHandOffBatchDepotSchedulesEx(dayOfWeek);
			List<IWaveInstance> wavesByDeliveryDate = routingInfoProxy.getWavesByDispatchTime(deliveryDate);
			
			Map<String , IWaveInstance> wavesById = new HashMap<String, IWaveInstance>();
			for(IWaveInstance _waveInstance: wavesByDeliveryDate){
				wavesById.put(_waveInstance.getRoutingWaveInstanceId(), _waveInstance);
			}
			Map<RoutingTimeOfDay, Integer> plantCapacity = routingInfoProxy.getPlantCapacityByDispatchTime(deliveryDate);
			Map<RoutingTimeOfDay, RoutingTimeOfDay> dispatchMapping = routingInfoProxy.getPlantDispatchMapping();
			
			Set<IAreaModel> areas = getAreas(wavesByDeliveryDate);
			Map<String, Set<RoutingTimeOfDay>> cutoffByArea = getCutoffsByArea(wavesByDeliveryDate);
			Map<String, Map<RoutingTimeOfDay, List<IRouteModel>>> staticRoutes = routingInfoProxy.getStaticRoutesByArea(deliveryDate);
			
			for(IAreaModel area: areas){
				schedulerId.setArea(area);
				schedulerId.setDeliveryDate(deliveryDate);
				if(area.isDepot()){
					if(cutoffByArea.get(area.getAreaCode())!=null){
						List<IRouteModel> tmpRoutes = new ArrayList<IRouteModel>();
						for(RoutingTimeOfDay cutoff: cutoffByArea.get(area.getAreaCode())){
							if(depotScheduleEx.get(area.getAreaCode())!=null && depotScheduleEx.get(area.getAreaCode()).get(cutoff)!=null){
								
								if(staticRoutes.containsKey(area.getAreaCode())){
									if(staticRoutes.get(area.getAreaCode()).containsKey(cutoff)){
										routes = staticRoutes.get(area.getAreaCode()).get(cutoff);
										routes = convertDptToRegOrders(area.getAreaCode(), routes, depotScheduleEx.get(area.getAreaCode()).get(cutoff), false);
										
									}
								}else{		
									routes = proxy.getRoutesByCriteria(schedulerId, RoutingDateUtil.getWaveCode(cutoff.getAsDate()));
									routes = convertDptToRegOrders(area.getAreaCode(), routes, depotScheduleEx.get(area.getAreaCode()).get(cutoff), true);
									
								}
								if(routes!=null)
								tmpRoutes.addAll(routes);
								
								
							}else{
								LOGGER.info("Depot-"+area.getAreaCode()+" cutoff ->"+cutoff+" for deliveryDate "+deliveryDate+" is missing in the depot schedule.");
							}
						}
						routes = tmpRoutes;
						if(routes!=null)
							for(IRouteModel route:routes)
							{
								LOGGER.info("DPT " +schedulerId.getArea().getAreaCode()+" Stops: "+ route.getStops().size()+" Allocated: "+ route.getAllocatedStops() != null ? route.getAllocatedStops().size() : 0);
							}
					}else{
						LOGGER.info("Depot-"+area.getAreaCode()+" is missing active waves for delivery date ->"+deliveryDate);
					}
				}
				else{ 
					routes = proxy.getRoutesByCriteria(schedulerId, null);
					if(routes!=null)
						for(IRouteModel route:routes)
						{
							LOGGER.info(schedulerId.getArea().getAreaCode()+" "+ route.getStops().size());
						}
				}
				
				if(routes!=null && routes.size()>0)
				{
					map.put(area, routes);
				}
			}
			assignDispatchTime(map, wavesById);
			
			Map<RoutingTimeOfDay, DispatchVolumeModel> dispatchMap = 
					groupbyDispatch(map, wavesByDeliveryDate,plantCapacity,snapshotTime,deliveryDate,dispatchMapping);
			
			printDispatch(dispatchMap);
			saveDispatch(dispatchMap);
			}
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

	private Map<String, Set<RoutingTimeOfDay>> getCutoffsByArea(
			List<IWaveInstance> wavesByDeliveryDate) {
		Map<String, Set<RoutingTimeOfDay>> cutoffs = new HashMap<String, Set<RoutingTimeOfDay>>();
		if(wavesByDeliveryDate!=null)
		{
			for(IWaveInstance waveInstance : wavesByDeliveryDate)
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
			List<IWaveInstance> wavesByDeliveryDate) {
		Set<IAreaModel> areas = new HashSet<IAreaModel>();
		if(wavesByDeliveryDate!=null)
		{
			for(IWaveInstance waveInstance : wavesByDeliveryDate)
			{
				areas.add(waveInstance.getArea());
			}
		}
		return areas;
	}

	private void printDispatch(Map<RoutingTimeOfDay, DispatchVolumeModel> dispatchMap){
		
		
		for(Entry<RoutingTimeOfDay, DispatchVolumeModel> dispatchEntry: dispatchMap.entrySet())
		{
			LOGGER.info(dispatchEntry.getKey()+ " "+dispatchEntry.getValue());
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
				if(route.getWaveId()!=null && wavesByDispatchTime.get(route.getWaveId())!=null)
					route.setDispatchTime(wavesByDispatchTime.get(route.getWaveId()).getDispatchTime());
				else{
					LOGGER.info("wave is missing in transapp reference id->"+route.getWaveId()+" routeNo->"+route.getRouteId()+" start time->"+route.getStartTime());
					if(route.getStops()!=null)
						LOGGER.info("Stop Cnt: "+route.getStops().size());
				}
					
			}
		}
	}
	
	private Map<RoutingTimeOfDay, DispatchVolumeModel> groupbyDispatch(Map<IAreaModel, List<IRouteModel>> map, List<IWaveInstance> wavesByDispatchTime,Map<RoutingTimeOfDay, Integer> plantCapacity, Date snapshotTime, Date deliveryDate, Map<RoutingTimeOfDay, RoutingTimeOfDay> plantDispatchMap){
		Map<RoutingTimeOfDay, DispatchVolumeModel> dispatchMap = new HashMap<RoutingTimeOfDay, DispatchVolumeModel>();
		RoutingTimeOfDay plantDispatch = null;
		for(Map.Entry<IAreaModel, List<IRouteModel>> entry: map.entrySet()){
			for(IRouteModel route: entry.getValue()){
				plantDispatch = getPlantDispatch(plantDispatchMap, route.getDispatchTime());
				if(plantDispatch!=null){
					if(!dispatchMap.containsKey(plantDispatch)){ dispatchMap.put(plantDispatch, new DispatchVolumeModel()); }
					if(entry.getKey()!=null && entry.getKey().isDepot())
						dispatchMap.get(plantDispatch).setNoOftrucks(dispatchMap.get(plantDispatch).getNoOftrucks()+1);
					dispatchMap.get(plantDispatch).setPlantCapacity((plantCapacity!=null && plantCapacity.get(plantDispatch)!=null)?plantCapacity.get(plantDispatch):0);
					dispatchMap.get(plantDispatch).setOrderCount(dispatchMap.get(plantDispatch).getOrderCount()+route.getStops().size());
					dispatchMap.get(plantDispatch).setAllocatedOrderCnt(dispatchMap.get(plantDispatch).getAllocatedOrderCnt() + route.getAllocatedStops().size());
					dispatchMap.get(plantDispatch).setDispatchTime(plantDispatch);
					dispatchMap.get(plantDispatch).setDispatchDate(deliveryDate);
					dispatchMap.get(plantDispatch).setSnapshotTime(snapshotTime);
				}
				
			}
		}
		
		for(IWaveInstance waveInstance : wavesByDispatchTime){
			plantDispatch = getPlantDispatch(plantDispatchMap, waveInstance.getDispatchTime());
			
			if(!dispatchMap.containsKey(plantDispatch)){ dispatchMap.put(plantDispatch, new DispatchVolumeModel()); }
			if(!waveInstance.getArea().isDepot())
				dispatchMap.get(plantDispatch).setNoOftrucks(dispatchMap.get(plantDispatch).getNoOftrucks()+waveInstance.getNoOfResources());
			dispatchMap.get(plantDispatch).setPlantCapacity((plantCapacity!=null && plantCapacity.get(plantDispatch)!=null)?plantCapacity.get(plantDispatch):0);
			dispatchMap.get(plantDispatch).setPlannedCapacity(dispatchMap.get(plantDispatch).getPlannedCapacity()+calculatePlannedCapacity(waveInstance));
			dispatchMap.get(plantDispatch).setDispatchTime(plantDispatch);
			dispatchMap.get(plantDispatch).setDispatchDate(waveInstance.getDeliveryDate());
			dispatchMap.get(plantDispatch).setSnapshotTime(snapshotTime);
			dispatchMap.get(plantDispatch).setShift(waveInstance.getShift());
			
		}
		
		return dispatchMap;
	}
	
	private RoutingTimeOfDay getPlantDispatch(Map<RoutingTimeOfDay, RoutingTimeOfDay> plantDispatchMap, RoutingTimeOfDay dispatchTime ){
		if(plantDispatchMap.containsKey(dispatchTime))
			return plantDispatchMap.get(dispatchTime);
		else
			return dispatchTime;
	}
	private double calculatePlannedCapacity(IWaveInstance waveInstance){
		double capacity = ((double)waveInstance.getMaxRunTime()/3600) * waveInstance.getNoOfResources() * waveInstance.getArea().getDeliveryRate();
		return capacity;
	}
	private class CustomTruckScheduleInfo extends HandOffBatchDepotScheduleEx {
		
		List orders = new ArrayList();
		List allocatedOrder = new ArrayList();
				
		public CustomTruckScheduleInfo(IHandOffBatchDepotScheduleEx info) {
			super();
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
		public List getAllocatedOrder() {
			return allocatedOrder;
		}

		public void setAllocatedOrder(List allocatedOrder) {
			this.allocatedOrder = allocatedOrder;
		}

		public void addAllocatedOrder(Object order) {
			this.allocatedOrder.add(order);
		}
		public String toString() {
			return this.getDepotArrivalTime()+ " "+this.getOriginId();
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
			Set<IHandOffBatchDepotScheduleEx> depotScheduleEx, boolean isDynamic) {
		
		List<CustomTruckScheduleInfo> groupSchedule = new ArrayList<CustomTruckScheduleInfo>();
		
		if(dptRoutes != null && depotScheduleEx != null) 
		{
			List<IRouteModel> newRoutes = new ArrayList<IRouteModel>();
			
			for(IHandOffBatchDepotScheduleEx _schInfo : depotScheduleEx){
				groupSchedule.add(new CustomTruckScheduleInfo(_schInfo));
			}
			Collections.sort(groupSchedule, new TruckScheduleComparator());
					
			if(isDynamic){
				doDynamicRouteAssignment(area, dptRoutes,groupSchedule);
			}
			else{
				doStaticRouteAssignement(area, dptRoutes,groupSchedule);
			}
		return updateDptRouteStop(area, groupSchedule);
			
		}
		return null;
		}
		
	private void doStaticRouteAssignement(String area, List<IRouteModel> dptRoutes, List<CustomTruckScheduleInfo> groupSchedule){
		for(IRouteModel route : dptRoutes) {
			Iterator itr = route.getStops().iterator();
			IRoutingStopModel _order = null;
			CustomTruckScheduleInfo _matchSchedule = null;
			
			while(itr.hasNext()) {
				_order = (IRoutingStopModel)itr.next();
				_matchSchedule = matchSchedule(_order
										, groupSchedule);
				if(_matchSchedule != null) {
				_matchSchedule.addOrder(_order);
				} else {
				LOGGER.info("Invalid Depot Truck Schedule File : Order No:" + _order.getOrderNumber()+ " Stop D-Time->"+_order.getStopDepartureTime()+ " Schedule-> "+groupSchedule);
				}
			}
			
			Iterator itr1 = route.getAllocatedStops().iterator();
			while(itr1.hasNext()) {
				_order = (IRoutingStopModel)itr1.next();
				_matchSchedule = matchSchedule(_order
										, groupSchedule);
				if(_matchSchedule != null) {
					_matchSchedule.addAllocatedOrder(_order);
				} else {
					LOGGER.info("Invalid Depot Truck Schedule File : Allocated Order No:" + _order.getOrderNumber()+ " Allocated Stop D-Time->"+_order.getStopDepartureTime()+ " Schedule-> "+groupSchedule);
				}
			}
			
		}
	}
	private void doDynamicRouteAssignment(String area, List<IRouteModel> dptRoutes, List<CustomTruckScheduleInfo> groupSchedule){
			int allowedDepartTimeDiff = RoutingServicesProperties.getDepotDepartTimeDiff();
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
						_order.setRoutingRouteId(routeID);
						_matchSchedule.addOrder(_order);
						
					} else {
						LOGGER.info("Invalid Depot Truck Schedule File : Order No:" + _order.getOrderNumber()+ " Stop D-Time->"+_order.getStopDepartureTime()+ " Schedule-> "+groupSchedule);
					}
				}
				
				// Allocated orders
				Iterator itr1 = route.getAllocatedStops().iterator();
				while(itr1.hasNext()) {
					_order = (IRoutingStopModel)itr1.next();
					
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
						_order.setRoutingRouteId(routeID);
						_matchSchedule.addAllocatedOrder(_order);
						
					} else {
						LOGGER.info("Invalid Depot Truck Schedule File : Allocated Order No:" + _order.getOrderNumber()+ " Allocated Stop D-Time->"+_order.getStopDepartureTime()+ " Schedule-> "+groupSchedule);
					}
				}
			}
		
		}
		private List<IRouteModel> updateDptRouteStop(String area, List<CustomTruckScheduleInfo> groupSchedule) {
			
			List<IRouteModel> result = new ArrayList<IRouteModel>();			
			int intRountCount = 0;					
				for(CustomTruckScheduleInfo route : groupSchedule) {
					
					intRountCount++;
					IRouteModel newRoute = new RouteModel(); 
					
					newRoute.setRouteId(area+"-"+intRountCount);
					newRoute.setStartTime(route.getTruckDepartureTime());
					newRoute.setDispatchTime(new RoutingTimeOfDay(route.getTruckDepartureTime()));
					newRoute.setCompletionTime(route.getDepotArrivalTime());
					newRoute.setOriginId(route.getOriginId());
					newRoute.setStops(new TreeSet());
					newRoute.setAllocatedStops(new TreeSet());
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
					
					List allocatedOrders = route.getAllocatedOrder();
					
					Iterator itr1 = allocatedOrders.iterator();
					
					intStopCount = 0;
					
					while (itr1.hasNext()) {
						_order = (IRoutingStopModel) itr1.next();
						intStopCount++;
						_order.setStopNo(intStopCount);
						newRoute.getAllocatedStops().add(_order);
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
						if(order.getStopDepartureTime() != null && _schInfo.getDepotArrivalTime().after(order.getStopDepartureTime())) {
							break;
						} else {
							_preSchInfo = _schInfo;
						}
					}
				}
			}
			return _preSchInfo;
		}
		private CustomTruckScheduleInfo matchSchedule(IRoutingStopModel order, List scheduleLst) {
			
			CustomTruckScheduleInfo _schInfo = null;
			CustomTruckScheduleInfo _preSchInfo = null;
			
			if(scheduleLst != null) {
				
				Iterator _itrSchedule = scheduleLst.iterator();
										
				while(_itrSchedule.hasNext()) {
					_schInfo = (CustomTruckScheduleInfo)_itrSchedule.next();
						if(order.getStopDepartureTime() != null && _schInfo.getDepotArrivalTime().after(order.getStopDepartureTime())) {
							break;
						} else {
							_preSchInfo = _schInfo;
						}
				}
			}
			return _preSchInfo;
		}

}