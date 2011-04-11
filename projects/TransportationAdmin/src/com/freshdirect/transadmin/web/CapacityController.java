package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IUnassignedModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.OrderEstimationResult;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.PackagingModel;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.PlantServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.Capacity;
import com.freshdirect.transadmin.web.model.CapacityAnalyzerCommand;
import com.freshdirect.transadmin.web.model.EarlyWarningCommand;
import com.freshdirect.transadmin.web.model.TimeRange;
import com.freshdirect.transadmin.web.model.UnassignedCommand;
import com.freshdirect.transadmin.web.model.WaveInstanceCommand;

public class CapacityController extends AbstractMultiActionController {
	
	private DomainManagerI domainManagerService;
	
	private ZoneManagerI zoneManagerService;
	
	private DispatchManagerI dispatchManagerService;
	
	private LocationManagerI locationManagerService;
		
	public ZoneManagerI getZoneManagerService() {
		return zoneManagerService;
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	
	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

	/**
	 * Custom handler for early warning
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView earlyWarningHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String rDate = request.getParameter("rDate");
		String cutOff = request.getParameter("cutOff");
		String rType = request.getParameter("rType");
		String condition = request.getParameter("condition");
		
		ModelAndView mav = new ModelAndView("earlyWarningView");
		Map<String, List<TimeRange>> discountMapping = null;
		IServiceTimeScenarioModel srvScenario = null;
		try {
			if(rDate != null) {
				Date reportDate = TransStringUtil.getDate(rDate);
				srvScenario = new RoutingInfoServiceProxy().getRoutingScenarioByDate(reportDate);
				discountMapping = this.getZoneManagerService().getWindowSteeringDiscounts(reportDate);
				
			}			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if("T".equalsIgnoreCase(rType)) {			
			processEarlyWarning(mav, new TimeEarlyWarningFormatter()
												, executeEarlyWarningTime(mav, rDate, cutOff, rType, EnumLogicalOperator.getEnum(condition))
												, discountMapping);
		} else {
			processEarlyWarning(mav, new OrderEarlyWarningFormatter()
												, executeEarlyWarningOrder(mav, rDate, cutOff, rType, EnumLogicalOperator.getEnum(condition))
												, discountMapping);
		}
		
		
		if(TransStringUtil.isEmpty(rDate)) {
			rDate = TransStringUtil.getNextDate();
		}
		
		mav.getModel().put("rDate", rDate);
		mav.getModel().put("cutOff", cutOff);
		mav.getModel().put("rType", rType);
		mav.getModel().put("autorefresh", request.getParameter("autorefresh"));
		mav.getModel().put("cutoffs", domainManagerService.getCutOffs());
		mav.getModel().put("srcscenario", srvScenario);
		mav.getModel().put("conditions", (List) EnumLogicalOperator.getEnumList());
		mav.getModel().put("condition", condition);
		
		return mav;
	}
	
	/**
	 * Custom handler for capacity Analyzer
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView capacityAnalyzerHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		String selectedDate = request.getParameter("selectedDate");
		String cutOff = request.getParameter("cutOff");
		String dlvGroup = request.getParameter("group");
		String serviceType = request.getParameter("serviceType");
		
		ModelAndView mav = new ModelAndView("capacityAnalyzerView");
		
		Set<TimeRange> allWindows = new TreeSet<TimeRange>();		
		Map<DlvBuilding, List<IDeliverySlot>> buildingsMap = new HashMap<DlvBuilding, List<IDeliverySlot>>();  
		List<CapacityAnalyzerCommand> buildings = new ArrayList<CapacityAnalyzerCommand>();
		
		try {
			if(selectedDate != null && dlvGroup != null) {
				Date rDate = TransStringUtil.getDate(selectedDate);
				
				List<DlvLocation> locations = this.getLocationManagerService().getBuildingGroup(dlvGroup);
				DeliveryServiceProxy dlvProxy = new DeliveryServiceProxy();
				RoutingInfoServiceProxy routingInfoProxy = new RoutingInfoServiceProxy();
				PlantServiceProxy plantServiceProxy = new PlantServiceProxy();
				RoutingEngineServiceProxy routingEngine = new RoutingEngineServiceProxy();
				
				Map<String, IServiceTimeTypeModel> serviceTimeTypes = routingInfoProxy.getRoutingServiceTimeTypes();
				IServiceTimeScenarioModel srvScenario = routingInfoProxy.getRoutingScenarioByDate(rDate);
				
				
				if(locations != null) {
					IOrderModel order = null;
					for(DlvLocation location : locations) {
						List<IDeliverySlot> timeslots = dlvProxy.getTimeslots(rDate, this.getCutOffTime(cutOff)
													, location.getLatitude().doubleValue()
													, location.getLongitude().doubleValue()
													, serviceType);
						List<IDeliverySlot> routingSlots = null;
						
						if(timeslots != null && timeslots.size() > 0 && isDynamicEnabled(timeslots)) {
							location.getBuilding().setZoneCode(timeslots.get(0).getZoneCode());							
							order = getOrderModel(location, getOrderNo(location));
							IZoneModel zoneModel = dlvProxy.getDeliveryZone(timeslots.get(0).getZoneCode());
							order.getDeliveryInfo().setDeliveryZone(zoneModel);
							order.getDeliveryInfo().setDeliveryDate(rDate);
							
							OrderEstimationResult calculatedSize = plantServiceProxy.estimateOrderSize(order, srvScenario, null);
							order.getDeliveryInfo().setPackagingDetail(calculatedSize.getPackagingModel());
							order.getDeliveryInfo().setCalculatedOrderSize(calculatedSize.getCalculatedOrderSize());
							
							srvScenario.setZoneConfiguration(routingInfoProxy.getRoutingScenarioMapping(srvScenario.getCode()));
							if(zoneModel.getServiceTimeType().getCode() != null) {
								zoneModel.setServiceTimeType(serviceTimeTypes.get(zoneModel.getServiceTimeType().getCode()));
							} else {
								zoneModel.setServiceTimeType(null);
							}
							if(order.getDeliveryInfo().getDeliveryLocation().getServiceTimeType() != null) {
								order.getDeliveryInfo().getDeliveryLocation().setServiceTimeType(serviceTimeTypes
																			.get(order.getDeliveryInfo().getDeliveryLocation().getServiceTimeType().getCode()));
							} else {
								order.getDeliveryInfo().getDeliveryLocation().setServiceTimeType(null);
							}
							if(order.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
									&& order.getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType() != null) {
								order.getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(serviceTimeTypes
										.get(order.getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType().getCode()));
							} else {
								order.getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(null);
							}
							
							
							order.getDeliveryInfo().setCalculatedServiceTime(dlvProxy.getServiceTime(order, srvScenario));
							
							routingSlots = routingEngine.schedulerAnalyzeOrder(order, 
																RoutingServicesProperties.getDefaultLocationType(), 
																	RoutingServicesProperties.getDefaultOrderType(), 
																	rDate, 1, timeslots);
						}
						if(routingSlots!=null && routingSlots.size() > 0)
							buildingsMap.put(location.getBuilding(), routingSlots);
						else
							buildingsMap.put(location.getBuilding(), new ArrayList<IDeliverySlot>());
					}					
				}
				relateTimeRange(allWindows, buildingsMap);
				processCapacityAnalzyer(buildings,allWindows,buildingsMap);
			}			
		} catch (ParseException e) {			
			e.printStackTrace();
		}
		
		if(TransStringUtil.isEmpty(selectedDate)) {
			selectedDate = TransStringUtil.getNextDate();
		}
		mav.getModel().put("selectedDate", selectedDate);
		mav.getModel().put("cutOff", cutOff);
		mav.getModel().put("group", dlvGroup);
		mav.getModel().put("serviceType", serviceType);
		mav.getModel().put("deliveryGroups",domainManagerService.getDeliveryGroups());
		mav.getModel().put("autorefresh", request.getParameter("autorefresh"));
		mav.getModel().put("cutoffs", domainManagerService.getCutOffs());
		mav.getModel().put("allBuildings", buildings);
		mav.getModel().put("allWindows", allWindows);	
		
		return mav;
	}
	
	private void relateTimeRange(Set<TimeRange> allWindows,
			Map<DlvBuilding, List<IDeliverySlot>> buildingWindows) {

		if (buildingWindows != null) {

			for (Map.Entry<DlvBuilding, List<IDeliverySlot>> slotMapping : buildingWindows
					.entrySet()) {
				if (slotMapping.getValue() != null) {
					for (IDeliverySlot slot : slotMapping.getValue()) {
						allWindows.add(new TimeRange(slot.getStartTime(), slot
								.getStopTime()));
					}
				}
			}
		}
	}
	
	private void processCapacityAnalzyer(List<CapacityAnalyzerCommand> buildings, Set<TimeRange> allWindows,
			Map<DlvBuilding, List<IDeliverySlot>> buildingWindows) {
		
		CapacityAnalyzerCommand _displayCommand = null;
		DlvBuilding _dlvBuilding = null;
		Map<TimeRange, String> slotsMap = null;
		
		if (buildingWindows != null && buildingWindows.size() > 0) {

			for (Map.Entry<DlvBuilding, List<IDeliverySlot>> buildingMapping : buildingWindows.entrySet()) {
				Zone zoneModel = null;
				_displayCommand = new CapacityAnalyzerCommand();
				slotsMap = new HashMap<TimeRange, String>();
				
				_dlvBuilding = buildingMapping.getKey();				
				_displayCommand.setBuildingId(_dlvBuilding.getBuildingId());
				_displayCommand.setZoneCode(_dlvBuilding.getZoneCode());
				_displayCommand.setAddress(_dlvBuilding.getSrubbedStreet()+", "+_dlvBuilding.getCity()+", "+_dlvBuilding.getZip());
				if(_dlvBuilding.getZoneCode()!= null)
					zoneModel = domainManagerService.getZone(_dlvBuilding.getZoneCode());
				_displayCommand.setDescription(zoneModel!=null ? zoneModel.getName():"");
				
				int soldOutCount = 0;				
				if (buildingMapping.getValue() != null) {
					
					TimeRange _slotRange = null;
					for(TimeRange range: allWindows){
						boolean isDynamicCapacityAvailable = false;
						boolean isMatching = false;
						for (IDeliverySlot slot : buildingMapping.getValue()) {							
							_slotRange = new TimeRange(slot.getStartTime(), slot.getStopTime());
							if(range.getTimeRangeString().equals(_slotRange.getTimeRangeString())){
								isMatching = true;
								isDynamicCapacityAvailable = isDynamicCapacityAvailable(slot);
							}
						}
						if(isMatching){
							if(!isDynamicCapacityAvailable)
								soldOutCount++;
							slotsMap.put(range, isDynamicCapacityAvailable ? "Y":"N");
						}else
							slotsMap.put(range, "");
						
					}					
				}
				
				_displayCommand.setSoldOutWindow(soldOutCount);				
				_displayCommand.setTimeslots(slotsMap);
				buildings.add(_displayCommand);
			}
		}
	}
	
	public static String getOrderNo(DlvLocation location) {
		return location.getBuilding().getBuildingId()!=null ? new StringBuilder("T").append(location.getBuilding().getBuildingId()).toString():new StringBuilder("T").append((int)(Math.random()/0.00001)).toString();
	}

	
	public static IOrderModel getOrderModel(DlvLocation location, String orderNumber) {
		
		IBuildingModel building = new BuildingModel();
		
		building.setStreetAddress1(location.getBuilding().getSrubbedStreet());
		building.setStreetAddress2(null);
		
		building.setCity(location.getBuilding().getCity());
		building.setCountry(location.getBuilding().getCountry());
		building.setState(location.getBuilding().getState());
		building.setZipCode(location.getBuilding().getZip());
		
		ILocationModel _locModel= new LocationModel(building);
		_locModel.setApartmentNumber(location.getApartment());
		_locModel.setLocationId(location.getLocationId());
		
		GeographicLocation _geoLocModel = new GeographicLocation();
		_geoLocModel.setLatitude(""+location.getLatitude());
		_geoLocModel.setLongitude(""+location.getLongitude());
						
		
		building.setGeographicLocation(_geoLocModel);
		IDeliveryModel dlvInfo = new DeliveryModel();
		dlvInfo.setDeliveryLocation(_locModel);
		dlvInfo.setReservationId(orderNumber);		
		
		IOrderModel order= new OrderModel();
		order.setDeliveryInfo(dlvInfo);
		IPackagingModel pModel = new PackagingModel();
		order.getDeliveryInfo().setPackagingDetail(pModel);			
				
		order.setCustomerName(new StringBuffer(100).append("SIMCUSTOMERNAME:").append(location.getBuilding().getBuildingId()).toString());
		order.setCustomerNumber("SIMCUSTOMER:"+location.getBuilding().getBuildingId());
		order.setOrderNumber(orderNumber);
		//order.setErpOrderNumber(location.getLocationId());
		return order;
	}
	
	protected static boolean isDynamicEnabled(List<IDeliverySlot> timeSlots) {
		boolean result = true;
		if(timeSlots != null) {
			for (IDeliverySlot slot : timeSlots) {			    	
				result = result && slot.isDynamicActive();
			}
		} else {
			result = false;
		}
		return result;
	}
	
	public boolean isDynamicCapacityAvailable(IDeliverySlot slot) {
		return slot != null && slot.getDeliveryCost() != null && slot.getDeliveryCost().isAvailable();
	}
	
	private IDeliverySlot matchSlotToMetrics(List<IDeliverySlot> slots, IDeliveryWindowMetrics metrics) {
		
		IDeliverySlot result = null;
		Iterator<IDeliverySlot> _itr = slots.iterator();
		IDeliverySlot _tmpSlot = null;
		
		if(slots != null) {
			while(_itr.hasNext()) {
				_tmpSlot = _itr.next();
				
				if(isMatchingTime(_tmpSlot.getStartTime(), metrics.getDeliveryStartTime())
						&& isMatchingTime(_tmpSlot.getStopTime(), metrics.getDeliveryEndTime())) {
					result = _tmpSlot;
					break;
				}
			}
		}
		return result;
	}
	
	private boolean isMatchingTime(Date date1, Date date2) {
		boolean result = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int hour1 = cal.get(Calendar.HOUR_OF_DAY);
		int minute1 = cal.get(Calendar.MINUTE);
		
		cal.setTime(date2);
		int hour2 = cal.get(Calendar.HOUR_OF_DAY);
		int minute2 = cal.get(Calendar.MINUTE);
		
		if(hour1 == hour2 && minute1 == minute2) {
			result = true; 
		}
		return result;
	}
	
	private Map<String, List<Capacity>> executeEarlyWarningOrder(ModelAndView mav, String rDate, String cutOff, String rType, EnumLogicalOperator condition) {
		
		Map<String, List<Capacity>> capacityMapping = new TreeMap<String, List<Capacity>>();
				
		if(!TransStringUtil.isEmpty(rDate)) {
			try {
				Date deliveryDate = TransStringUtil.getDate(rDate);
				
				DeliveryServiceProxy deliveryProxy = new DeliveryServiceProxy();
				
				Map<String, List<IDeliverySlot>> refSlotsByZone = deliveryProxy.getTimeslotsByDate
																	(deliveryDate, getCutOffTime(cutOff), null, condition);
				
				Map<String, List<IDeliveryWindowMetrics>> slotsByZone = deliveryProxy.getTimeslotsByDateEx
																			(deliveryDate, getCutOffTime(cutOff), null, condition);
									
				Iterator<String> _itr = slotsByZone.keySet().iterator();
								
				String _zoneCode = null;
								
				List<IDeliveryWindowMetrics> metrics = null;
				IDeliverySlot _refSlot = null;
				
				while(_itr.hasNext()) {
					_zoneCode = _itr.next();										
					metrics = slotsByZone.get(_zoneCode);
					if(!capacityMapping.containsKey(_zoneCode)) {
						capacityMapping.put(_zoneCode, new ArrayList<Capacity>());
					}
									
					if(metrics != null) {
						
						Iterator<IDeliveryWindowMetrics> _metricsItr = metrics.iterator();						
						IDeliveryWindowMetrics _metrics = null;
						Capacity _capacity = null;
						
						while(_metricsItr.hasNext()) {							
							_metrics = _metricsItr.next();
							
							_capacity = new Capacity();
							capacityMapping.get(_zoneCode).add(_capacity);
							
							_capacity.setDeliveryStartTime(_metrics.getDeliveryStartTime());
							_capacity.setDeliveryEndTime(_metrics.getDeliveryEndTime());
							_capacity.setTotalCapacity(_metrics.getOrderCapacity());
							_capacity.setTotalAllocated(_metrics.getTotalAllocatedOrders());
							_capacity.setTotalConfirmed(_metrics.getTotalConfirmedOrders());
							
							_capacity.setNoOfResources(0);
							
							_refSlot = matchSlotToMetrics(refSlotsByZone.get(_zoneCode), _metrics);
							if(_refSlot != null) {
								_capacity.setManuallyClosed(_refSlot.isManuallyClosed());
								_capacity.setDynamicActive(_refSlot.isDynamicActive());
								_capacity.setReferenceId(_refSlot.getReferenceId());
							}
							
						}						
					}					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		} 
		return capacityMapping;
	}

	private Map<String, List<Capacity>> executeEarlyWarningTime(ModelAndView mav, String rDate, String cutOff, String rType, EnumLogicalOperator condition) {
		
		Map<String, List<Capacity>> capacityMapping = new TreeMap<String, List<Capacity>>();
		
		
		if(!TransStringUtil.isEmpty(rDate)) {
			try {
				Date deliveryDate = TransStringUtil.getDate(rDate);
				
				DeliveryServiceProxy deliveryProxy = new DeliveryServiceProxy();
				RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
				
				Map<String, List<IDeliverySlot>> slotsByZone = deliveryProxy.getTimeslotsByDate
																	(deliveryDate, getCutOffTime(cutOff), null, condition);
								
				Iterator<String> _itr = slotsByZone.keySet().iterator();
												
				String _zoneCode = null;
				List<IDeliverySlot> _slots = null;
				IDeliverySlot _refSlot = null;
				
				while(_itr.hasNext()) {
					
					_zoneCode = _itr.next();
					_slots = slotsByZone.get(_zoneCode);
					List<IDeliveryWindowMetrics> metrics = null;
					IRoutingSchedulerIdentity _schId = null;
					if(_slots.size() > 0) {
						_schId = _slots.get(0).getSchedulerId();
						metrics = proxy.retrieveCapacityMetrics(_schId, _slots);
					}
					
					if(!capacityMapping.containsKey(_zoneCode)) {
						capacityMapping.put(_zoneCode, new ArrayList<Capacity>());
					}	
					
					if(metrics != null) {
						
						Iterator<IDeliveryWindowMetrics> _metricsItr = metrics.iterator();						
						IDeliveryWindowMetrics _metrics = null;
						Capacity _capacity = null;
						
						 
						while(_metricsItr.hasNext()) {							
							_metrics = _metricsItr.next();
							_capacity = new Capacity();
							capacityMapping.get(_zoneCode).add(_capacity);
							
							_capacity.setDeliveryStartTime(_metrics.getDeliveryStartTime());
							_capacity.setDeliveryEndTime(_metrics.getDeliveryEndTime());
							_capacity.setTotalCapacity(_metrics.getTotalCapacityTime());
							_capacity.setTotalAllocated(_metrics.getConfirmedServiceTime() 
															+ _metrics.getConfirmedTravelTime()
															+ _metrics.getReservedServiceTime()
															+ _metrics.getReservedTravelTime());
							_capacity.setTotalConfirmed(_metrics.getConfirmedServiceTime() 
															+ _metrics.getConfirmedTravelTime());	
							_capacity.setNoOfResources(_metrics.getAllocatedVehicles());
							
							_refSlot = matchSlotToMetrics(_slots, _metrics);
							if(_refSlot != null) {
								_capacity.setManuallyClosed(_refSlot.isManuallyClosed());
								_capacity.setDynamicActive(_refSlot.isDynamicActive());
								_capacity.setReferenceId(_refSlot.getReferenceId());
							}
						}						
					}					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}						
		}
		return capacityMapping;
	}
	
	private void processEarlyWarning(ModelAndView mav, EarlyWarningFormatter formatter
										, Map<String, List<Capacity>> capacityMapping
										, Map<String, List<TimeRange>> discountMapping)  {
		
		List<EarlyWarningCommand> capacity = new ArrayList<EarlyWarningCommand>();
		Map<Region, Capacity> regionCapacity = new HashMap<Region, Capacity>();
		List<EarlyWarningCommand> regCapacity = new ArrayList<EarlyWarningCommand>();
		
		Map<String, Zone> zoneMapping = getZoneMapping();
		Iterator<String> _capItr = capacityMapping.keySet().iterator();
		String _zoneCode = null;
		Capacity _capacity = null;
		Capacity _tmpRegCapacity = null;
		try {
			while(_capItr.hasNext()) {
				_zoneCode = _capItr.next();
				List<TimeRange> discountSlots = new ArrayList<TimeRange>();
				
				if(discountMapping != null) {					
					if(discountMapping.containsKey(_zoneCode)) {
						discountSlots.addAll(discountMapping.get(_zoneCode));
					}
					if(discountMapping.containsKey("ALL")) {
						discountSlots.addAll(discountMapping.get("ALL"));
					}
				}
				
				Zone _refZone = zoneMapping.get(_zoneCode);
				Iterator<Capacity> _childItr = capacityMapping.get(_zoneCode).iterator();
				
				double totalCapacity = 0;
				double totalConfirmed = 0;
				double totalAllocated = 0;
				double percentageConfirmed = 0;
				double percentageAllocated = 0;
				
				EarlyWarningCommand _displayCommand = null;
				EarlyWarningCommand _timeslotCommand = null;
				List<EarlyWarningCommand> timeslotDetails = null;
				
				_displayCommand =  new EarlyWarningCommand();
				timeslotDetails = new ArrayList<EarlyWarningCommand>();
				_displayCommand.setTimeslotDetails(timeslotDetails);
				_displayCommand.setCode(_refZone.getArea().getCode());
				_displayCommand.setName(_refZone.getArea().getName());
				
				int openCount = 0;
				int closedCount = 0;
				int dynamicActiveCount = 0;
				int dynamicInActiveCount = 0;
				boolean hasDiscount = false;
				
				while(_childItr.hasNext()) {
					_capacity = _childItr.next();
					
					totalCapacity = totalCapacity + _capacity.getTotalCapacity();
					totalConfirmed = totalConfirmed + _capacity.getTotalConfirmed();							
					totalAllocated = totalAllocated + _capacity.getTotalAllocated();
					
					_timeslotCommand = new EarlyWarningCommand();
					
					if(_capacity.isManuallyClosed()) {
						closedCount++;
					} else {
						openCount++;
					}
					if(_capacity.isDynamicActive()) {
						dynamicActiveCount++;
					} else {
						dynamicInActiveCount++;
					}
					_timeslotCommand.setOpenCount(_capacity.isManuallyClosed() ? 0 : 1);
					_timeslotCommand.setClosedCount(_capacity.isManuallyClosed() ? 1 : 0);
					
					_timeslotCommand.setDynamicActiveCount(_capacity.isDynamicActive() ? 1 : 0);
					_timeslotCommand.setDynamicInActiveCount(_capacity.isDynamicActive() ? 0 : 1);
					
					_timeslotCommand.setReferenceId(_capacity.getReferenceId());
					_timeslotCommand.setNoOfResources(_capacity.getNoOfResources());
					timeslotDetails.add(_timeslotCommand);
					_timeslotCommand.setName(RoutingDateUtil.formatDateTime
							(_capacity.getDeliveryStartTime(), _capacity.getDeliveryEndTime()));
					_timeslotCommand.setTotalCapacity(formatter.formatCapacity(_capacity.getTotalCapacity()));
					_timeslotCommand.setConfirmedCapacity(formatter.formatCapacity(_capacity.getTotalConfirmed()));
					_timeslotCommand.setAllocatedCapacity(formatter.formatCapacity(_capacity.getTotalAllocated()));
					_timeslotCommand.setPercentageConfirmed("0%");
					_timeslotCommand.setPercentageAllocated("0%");
					if(_capacity.getTotalCapacity() > 0) {
						_timeslotCommand.setPercentageConfirmed(""+Math.round((_capacity.getTotalConfirmed()/_capacity.getTotalCapacity())*100.0)+"%");
						_timeslotCommand.setPercentageAllocated(""+Math.round((_capacity.getTotalAllocated()/_capacity.getTotalCapacity())*100.0)+"%");								
					}
					boolean isDiscounted = this.isDiscounted(discountSlots, _zoneCode, _capacity.getDeliveryStartTime(), _capacity.getDeliveryEndTime());
					if(isDiscounted) {
						_timeslotCommand.setDiscounted(isDiscounted);
						hasDiscount = true;
					}
				}
				if(totalCapacity > 0) {
					percentageConfirmed = (totalConfirmed/totalCapacity)*100.0;
					percentageAllocated = (totalAllocated/totalCapacity)*100.0;
				}								
				_displayCommand.setTotalCapacity(formatter.formatCapacity(totalCapacity));
				_displayCommand.setConfirmedCapacity(formatter.formatCapacity(totalConfirmed));
				_displayCommand.setAllocatedCapacity(formatter.formatCapacity(totalAllocated));
				_displayCommand.setPercentageConfirmed(""+Math.round(percentageConfirmed)+"%");
				_displayCommand.setPercentageAllocated(""+Math.round(percentageAllocated)+"%");
				
				_displayCommand.setOpenCount(openCount);
				_displayCommand.setClosedCount(closedCount);				
				_displayCommand.setDynamicActiveCount(dynamicActiveCount);
				_displayCommand.setDynamicInActiveCount(dynamicInActiveCount);
				_displayCommand.setDiscounted(hasDiscount);
				
				capacity.add(_displayCommand);
				
				if(!regionCapacity.containsKey(_refZone.getRegion())) {
					_tmpRegCapacity = new Capacity();
					_tmpRegCapacity.setManuallyClosed(true);
					regionCapacity.put(_refZone.getRegion(), _tmpRegCapacity);
				}
				_tmpRegCapacity  = regionCapacity.get(_refZone.getRegion());
				_tmpRegCapacity.setTotalCapacity(_tmpRegCapacity.getTotalCapacity()+totalCapacity);
				_tmpRegCapacity.setTotalAllocated(_tmpRegCapacity.getTotalAllocated()+totalAllocated);
				_tmpRegCapacity.setTotalConfirmed(_tmpRegCapacity.getTotalConfirmed()+totalConfirmed);
				
				_tmpRegCapacity.setOpenCount(_tmpRegCapacity.getOpenCount() + openCount);
				_tmpRegCapacity.setClosedCount(_tmpRegCapacity.getClosedCount() + closedCount);
				
				_tmpRegCapacity.setDynamicActiveCount(_tmpRegCapacity.getDynamicActiveCount() + dynamicActiveCount);
				_tmpRegCapacity.setDynamicInActiveCount(_tmpRegCapacity.getDynamicInActiveCount() + dynamicInActiveCount);
				if(_displayCommand.isDiscounted()) {
					_tmpRegCapacity.setDiscounted(_displayCommand.isDiscounted());
				}
				
			}
			
			Iterator<Region> _regItr = regionCapacity.keySet().iterator();
			Region _region = null;
			Capacity _regCap = null;
			EarlyWarningCommand _regDisplayCommand = null; 
			
			double totalCapacity = 0;
			double totalConfirmed = 0;
			double totalAllocated = 0;
			double percentageConfirmed = 0;
			double percentageAllocated = 0;
			
			while(_regItr.hasNext()) {
				
				double _regPerConfirmed = 0;
				double _regPerAllocated = 0;
				_region = _regItr.next();
								
				_regCap = regionCapacity.get(_region);
				
				totalCapacity = totalCapacity + _regCap.getTotalCapacity();
				totalConfirmed = totalConfirmed + _regCap.getTotalConfirmed();							
				totalAllocated = totalAllocated + _regCap.getTotalAllocated();
				
				_regDisplayCommand =  new EarlyWarningCommand();
				_regDisplayCommand.setName(_region.getName());
				_regDisplayCommand.setCode(_region.getCode());
				_regDisplayCommand.setTotalCapacity(formatter.formatCapacity(_regCap.getTotalCapacity()));
				_regDisplayCommand.setAllocatedCapacity(formatter.formatCapacity(_regCap.getTotalAllocated()));
				_regDisplayCommand.setConfirmedCapacity(formatter.formatCapacity(_regCap.getTotalConfirmed()));
				_regDisplayCommand.setRegion(true);
				_regDisplayCommand.setOpenCount(_regCap.getOpenCount());
				_regDisplayCommand.setClosedCount(_regCap.getClosedCount());
				_regDisplayCommand.setDynamicActiveCount(_regCap.getDynamicActiveCount());
				_regDisplayCommand.setDynamicInActiveCount(_regCap.getDynamicInActiveCount()); 
				_regDisplayCommand.setDiscounted(_regCap.isDiscounted());
				
				if(_regCap.getTotalCapacity() > 0) {
					_regPerConfirmed = (_regCap.getTotalConfirmed()/_regCap.getTotalCapacity())*100.0;
					_regPerAllocated = (_regCap.getTotalAllocated()/_regCap.getTotalCapacity())*100.0;
				}
				
				_regDisplayCommand.setPercentageConfirmed(""+Math.round(_regPerConfirmed)+"%");
				_regDisplayCommand.setPercentageAllocated(""+Math.round(_regPerAllocated)+"%");
				regCapacity.add(_regDisplayCommand);
			}
			_regDisplayCommand =  new EarlyWarningCommand();
			_regDisplayCommand.setName("");
			_regDisplayCommand.setTotalCapacity(formatter.formatCapacity(totalCapacity));
			_regDisplayCommand.setAllocatedCapacity(formatter.formatCapacity(totalAllocated));
			_regDisplayCommand.setConfirmedCapacity(formatter.formatCapacity(totalConfirmed));
			_regDisplayCommand.setRegion(true);
			
			if(totalCapacity > 0) {
				percentageConfirmed = (totalConfirmed/totalCapacity)*100.0;
				percentageAllocated = (totalAllocated/totalCapacity)*100.0;
			}
			_regDisplayCommand.setPercentageConfirmed(""+Math.round(percentageConfirmed)+"%");
			_regDisplayCommand.setPercentageAllocated(""+Math.round(percentageAllocated)+"%");
			regCapacity.add(_regDisplayCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mav.getModel().put("earlywarnings", capacity );
		mav.getModel().put("earlywarnings_region", regCapacity );
	}
	
	private boolean isDiscounted(List<TimeRange> discountSlots, String zoneCode, Date startTime, Date endTime) {		
		if(discountSlots != null) {			
			for(TimeRange slot : discountSlots) {
				TimeOfDay windowStart = new TimeOfDay(startTime);
				TimeOfDay windowEnd = new TimeOfDay(endTime);
				
				if((slot.getStartTime().equals(windowStart) || windowStart.after(slot.getStartTime())) && 
						(windowEnd.before(slot.getEndTime()) || (slot.getEndTime().equals(windowEnd)))) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Custom handler for early warning
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView unassignedHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String rDate = request.getParameter("rDate");
		String reRouteDate = request.getParameter("reRouteDate");
				
		List<UnassignedCommand> unassigneds = new ArrayList<UnassignedCommand>();
		
		ModelAndView mav = new ModelAndView("unassignedView");
		
		if(TransStringUtil.isEmpty(rDate)) {
			rDate = TransStringUtil.getNextDate();
		}
		if(TransStringUtil.isEmpty(reRouteDate)) {
			reRouteDate = TransStringUtil.getNextDate();
		}
		
		if(!TransStringUtil.isEmpty(rDate)) {
			try {
				Date deliveryDate = TransStringUtil.getDate(rDate);
				
				DeliveryServiceProxy deliveryProxy = new DeliveryServiceProxy();
						
				List<IUnassignedModel> orders = deliveryProxy.getUnassigned(deliveryDate, null, null);
				
				Iterator<IUnassignedModel> _itr = orders.iterator();
				IUnassignedModel _order = null;
				UnassignedCommand _unassigned = null;
				
				while(_itr.hasNext()) {
					_order = _itr.next();
					
					_unassigned = new UnassignedCommand();
					
					_unassigned.setCreateModTime(_order.getOrder().getCreateModifyTime());
					_unassigned.setCustomerId(_order.getOrder().getCustomerNumber());
					_unassigned.setOrderId(_order.getOrder().getOrderNumber());
					_unassigned.setTimeWindow(RoutingDateUtil.formatDateTime(_order.getOrder().getDeliveryInfo().getDeliveryStartTime()
								, _order.getOrder().getDeliveryInfo().getDeliveryEndTime()));
					_unassigned.setUnassignedTime(_order.getOrder().getUnassignedTime());
					_unassigned.setUnassignedAction(_order.getOrder().getUnassignedAction());
					
					_unassigned.setReservationId(_order.getOrder().getDeliveryInfo().getReservationId());
					_unassigned.setZone(_order.getOrder().getDeliveryInfo().getDeliveryZone().getZoneNumber());
					
					_unassigned.setOrderSize(_order.getOrder().getReservedOrderSize());
					_unassigned.setServiceTime(_order.getOrder().getReservedServiceTime());
					
					_unassigned.setOverrideOrderSize(_order.getOrder().getOverrideOrderSize());
					_unassigned.setOverrideServiceTime(_order.getOrder().getOverrideServiceTime());
					
					if(_order.getOrder() != null && _order.getOrder().getDeliveryInfo() != null
														&& _order.getOrder().getDeliveryInfo().getPackagingDetail() != null) {
						_unassigned.setPackageInfo(_order.getOrder().getDeliveryInfo().getPackagingDetail().getNoOfCartons()
														+","+_order.getOrder().getDeliveryInfo().getPackagingDetail().getNoOfCases()
														+","+_order.getOrder().getDeliveryInfo().getPackagingDetail().getNoOfFreezers());
					}
										
					_unassigned.setUpdateStatus(_order.getOrder().getUpdateStatus());
					
					_unassigned.setManuallyClosed(_order.getSlot().isManuallyClosed());
					_unassigned.setDynamicActive(_order.getSlot().isDynamicActive());
					
					_unassigned.setIsChefsTable(_order.getIsChefsTable());
					_unassigned.setIsForced(_order.getIsForced());
					unassigneds.add(_unassigned);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		mav.getModel().put("rDate", rDate);
		mav.getModel().put("reRouteDate", reRouteDate);
		mav.getModel().put("autorefresh", request.getParameter("autorefresh"));
		mav.getModel().put("unassigneds", unassigneds );
		mav.getModel().put("zones", domainManagerService.getZones());
		return mav;
	}
	
	/**
	 * Custom handler for early warning
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView waveMonitorHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String rDate = request.getParameter("rDate");
		String cutOff = request.getParameter("cutOff");
		EnumWaveInstanceStatus waveStatus = EnumWaveInstanceStatus.getEnum(request.getParameter("waveStatus"));
		
		ModelAndView mav = new ModelAndView("waveMonitorView");
		RoutingInfoServiceProxy proxy = new RoutingInfoServiceProxy();
		Collection gridResult = new ArrayList<WaveInstanceCommand>();
		
		Map<Date, List<String>> dynamicEnabledZoneMpp = proxy.getDynamicEnabledZoneMapping();
		
		try {
			Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> result = null;
			List<IWaveInstance> waveInstances = new ArrayList<IWaveInstance>();
			
			if (rDate != null && rDate.trim().length() > 0) {
				result = proxy.getWaveInstanceTree(TransStringUtil.getDate(rDate), waveStatus);
			} else {
				result = proxy.getWaveInstanceTree(null, waveStatus);
			}
			RoutingTimeOfDay rCutOff = null;
			if(cutOff != null && cutOff.trim().length() > 0) {
				rCutOff = new RoutingTimeOfDay(getCutOffTime(cutOff));
			}
			if(result != null) {
				for(Map.Entry<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> _tmpDlvDateMpp : result.entrySet()) {
					for(Map.Entry<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> _tmpZoneMpp : _tmpDlvDateMpp.getValue().entrySet()) {
						for(Map.Entry<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> _tmpDispatchTimeMpp : _tmpZoneMpp.getValue().entrySet()) {
							for(Map.Entry<RoutingTimeOfDay, List<IWaveInstance>> _tmpCutOffMpp : _tmpDispatchTimeMpp.getValue().entrySet()) {
								if(rCutOff != null && !rCutOff.equals(_tmpCutOffMpp.getKey())) {
									continue;
								} else {
									waveInstances.addAll(_tmpCutOffMpp.getValue());
								}
							}
						}
					}
				}
				
			}
			WaveInstanceCommand _tmpWaveCmd = null;
			for(IWaveInstance _inst : waveInstances) {
				_tmpWaveCmd = new WaveInstanceCommand(_inst);
				if(dynamicEnabledZoneMpp != null && _inst.getArea() != null && dynamicEnabledZoneMpp.containsKey(_inst.getDeliveryDate())
						&& dynamicEnabledZoneMpp.get(_inst.getDeliveryDate()).contains(_inst.getArea().getAreaCode())) {
					_tmpWaveCmd.setIsInValid(false);					
				}
				gridResult.add(_tmpWaveCmd);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		mav.getModel().put("rDate", rDate);
		mav.getModel().put("cutOff", cutOff);
		mav.getModel().put("waveinstances", gridResult );
		mav.getModel().put("waveStatus", waveStatus != null ? waveStatus.getName() : "" );		
		
		//mav.getModel().put("erroronly", errorOnly);
		mav.getModel().put("cutoffs", domainManagerService.getCutOffs());
		
		return mav;
	}
	
	interface EarlyWarningFormatter {
		
		String formatCapacity(double value);
	}
	
	class TimeEarlyWarningFormatter implements EarlyWarningFormatter{
		
		public String formatCapacity(double value) {
			return ""+TransStringUtil.formatIntoHHMM(value);
		}
	}
	
	class OrderEarlyWarningFormatter implements EarlyWarningFormatter{
		
		public String formatCapacity(double value) {
			return ""+Math.round(value);
		}
	}
	
	protected Map getZoneMapping() {
		
		Map<String, Zone> result = new HashMap<String, Zone>();
		Collection zones = domainManagerService.getZones();
		Zone tmpZone = null;
		if(zones != null) {
			Iterator<Zone> iterator = zones.iterator();
			while(iterator.hasNext()) {
				tmpZone = (Zone)iterator.next();				
				if(tmpZone != null && tmpZone.getArea() != null) {
					result.put(tmpZone.getZoneCode(), tmpZone);
				}
			}
		}
		return result;
	}
	
	protected Date getCutOffTime(String cutOff) {
		TrnCutOff tmpModel = getDomainManagerService().getCutOff(cutOff);
		if(tmpModel != null) {
			return tmpModel.getCutOffTime().getAsDate();
		}
		return null;
	}
	
}
