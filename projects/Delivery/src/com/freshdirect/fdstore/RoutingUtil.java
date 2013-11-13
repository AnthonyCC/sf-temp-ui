package com.freshdirect.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.routing.ejb.RoutingGatewayHome;
import com.freshdirect.delivery.routing.ejb.RoutingGatewaySB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.constants.RoutingActivityType;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IRoutingDepotId;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.OrderEstimationResult;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.PackagingModel;
import com.freshdirect.routing.model.RoutingDepotId;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.model.TrnFacilityType;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.CapacityEngineServiceProxy;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.service.proxy.PlantServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public class RoutingUtil {

	private static RoutingUtil _instance = null;
	private RoutingGatewayHome home = null;
	private DlvManagerHome dlvHome = null;
	private Context routingProvider=null;
	private Context altProvider=null;
	private boolean useAltProvider=false;
	private static final Category LOGGER = LoggerFactory.getInstance(RoutingUtil.class);
	private static long lastRefresh = 0;
	private final static long REFRESH_PERIOD = 30 * 60 * 1000;
	
	private  void refresh() {
		refresh(false);
	}

	private  synchronized  void refresh(boolean force) {
		long t = System.currentTimeMillis();
		if (force || (t - lastRefresh > REFRESH_PERIOD)) {
			close(routingProvider);
			close(altProvider);
			lastRefresh = t;
			LOGGER.info("Reloading contexts in RoutingUtil. ");
			setUseAltProvider(false);
			this.routingProvider =getRoutingContext();
			this.altProvider=getAltContext();
		}
	}
	
	private Context getContext() {
		refresh();
		return useAltProvider? altProvider:routingProvider;
	}
	private synchronized void setUseAltProvider(boolean useAltProvider) {
		this.useAltProvider = useAltProvider;
	}

	public RoutingGatewayHome getHome() {
		return home;
	}

	private RoutingUtil() {
		
		this.routingProvider =getRoutingContext();
		this.altProvider=getAltContext();
	}
	
	private Context getRoutingContext() {
		try {
			return FDStoreProperties.getRoutingInitialContext();
		} catch (NamingException e) {
			setUseAltProvider(true);
			e.printStackTrace();
			return null;
		}
	}
	
	private Context getAltContext() {
		try {
			return FDStoreProperties.getInitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	protected  void lookupRoutingGatewayHome() throws FDResourceException {

		try {
			if(home==null) {
				home = (RoutingGatewayHome) getContext().lookup( FDStoreProperties.getRoutingGatewayHome());
			}
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		}
	}
	
	public void lookupDlvManagerHome() throws FDResourceException {

		try {
			if(dlvHome==null) {
				dlvHome = (DlvManagerHome) getContext().lookup( FDStoreProperties.getDeliveryManagerHome());
			}
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		}
	}

	public static RoutingUtil getInstance() {
		if (_instance == null) {
			_instance = new RoutingUtil();
		}
		return _instance;
	}

	public static IOrderModel calculateReservationSize(DlvReservationModel reservation, IOrderModel order
															, FDTimeslot timeslot) throws RoutingServiceException {
		
		RoutingInfoServiceProxy routingInfoproxy = new RoutingInfoServiceProxy();
		DeliveryServiceProxy dlvService = new DeliveryServiceProxy();
		
		IZoneModel zoneModel = dlvService.getDeliveryZone(timeslot.getZoneCode());
		order.getDeliveryInfo().setDeliveryZone(zoneModel);
		order.getDeliveryInfo().setDeliveryDate(timeslot.getBaseDate());
		
		Map<String, IServiceTimeTypeModel> serviceTimeTypeMapping = routingInfoproxy.getRoutingServiceTimeTypes();
		order.getDeliveryInfo().setDeliveryLocation(locateOrder(order));
		
		IServiceTimeScenarioModel srvScenario = getRoutingScenarioEx(order.getDeliveryInfo().getDeliveryDate(), 
				timeslot.getCutoffNormalDateTime(), timeslot.getBegTime(), timeslot.getEndTime()); // this method uses the handoff/timeslot specific scenario for that date or day. This logic is only invoked by the unassigned cron job.
		OrderEstimationResult calculatedSize = estimateOrderSize(order, srvScenario, order.getDeliveryInfo().getPackagingDetail());
		order.getDeliveryInfo().setPackagingDetail(calculatedSize.getPackagingModel());
		order.getDeliveryInfo().setCalculatedOrderSize(calculatedSize.getCalculatedOrderSize());
		
		srvScenario.setZoneConfiguration(routingInfoproxy.getRoutingScenarioMapping(srvScenario.getCode()));
		if(zoneModel.getServiceTimeType().getCode() != null) {
			zoneModel.setServiceTimeType(serviceTimeTypeMapping.get(zoneModel.getServiceTimeType().getCode()));
		} else {
			zoneModel.setServiceTimeType(null);
		}
		if(order.getDeliveryInfo().getDeliveryLocation().getServiceTimeType() != null) {
			order.getDeliveryInfo().getDeliveryLocation().setServiceTimeType(serviceTimeTypeMapping
														.get(order.getDeliveryInfo().getDeliveryLocation().getServiceTimeType().getCode()));
		} else {
			order.getDeliveryInfo().getDeliveryLocation().setServiceTimeType(null);
		}
		if(order.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
				&& order.getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType() != null) {
			order.getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(serviceTimeTypeMapping
					.get(order.getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType().getCode()));
		} else {
			order.getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(null);
		}
		order.getDeliveryInfo().setReservedOrdersAtBuilding(reservation.getReservedOrdersAtBuilding());
		order.getDeliveryInfo().setCalculatedServiceTime(dlvService.getServiceTime(order,srvScenario,RoutingActivityType.UPDATE_TIMESLOT));
			
		return order;
	}

	public static  IDeliveryReservation reserveTimeslot(DlvReservationModel reservation, IOrderModel order 
															, FDTimeslot timeslot) throws RoutingServiceException {
		
		IDeliverySlot reservedSlot = null;
		DeliveryServiceProxy dlvService = new DeliveryServiceProxy();
		RoutingInfoServiceProxy routingInfoproxy = new RoutingInfoServiceProxy();
		
		IZoneModel zoneModel = dlvService.getDeliveryZone(timeslot.getZoneCode());
		order.getDeliveryInfo().setDeliveryZone(zoneModel);
		order.getDeliveryInfo().setDeliveryDate(timeslot.getBaseDate());
		
		if(reservation.getOverrideOrderSize() != null) {
			IPackagingModel pModel = new PackagingModel();
			pModel.setNoOfCartons(reservation.getOverrideOrderSize().longValue());			
			order.getDeliveryInfo().setPackagingDetail(pModel);

		} else if(reservation.getMetricsSource() != null) {
			IPackagingModel pModel = new PackagingModel();
			pModel.setNoOfCartons(reservation.getNoOfCartons().longValue());	
			pModel.setNoOfCases(reservation.getNoOfCases().longValue());
			pModel.setNoOfFreezers(reservation.getNoOfFreezers().longValue());
			pModel.setSource(reservation.getMetricsSource());
			order.getDeliveryInfo().setPackagingDetail(pModel);
			
		} else {
			IPackagingModel historyPackageInfo = getHistoricOrderSize(order);
			order.getDeliveryInfo().setPackagingDetail(historyPackageInfo);
		}
					
		Map<String, IServiceTimeTypeModel> serviceTimeTypeMapping = routingInfoproxy.getRoutingServiceTimeTypes();
		order.getDeliveryInfo().setDeliveryLocation(locateOrder(order));
		
		IServiceTimeScenarioModel srvScenario = getRoutingScenario(order.getDeliveryInfo().getDeliveryDate());
		OrderEstimationResult calculatedSize = estimateOrderSize(order, srvScenario, order.getDeliveryInfo().getPackagingDetail());
		order.getDeliveryInfo().setCalculatedOrderSize(calculatedSize.getCalculatedOrderSize());
		order.getDeliveryInfo().setPackagingDetail(calculatedSize.getPackagingModel());
		
		srvScenario.setZoneConfiguration(routingInfoproxy.getRoutingScenarioMapping(srvScenario.getCode()));
		if(zoneModel.getServiceTimeType().getCode() != null) {
			zoneModel.setServiceTimeType(serviceTimeTypeMapping.get(zoneModel.getServiceTimeType().getCode()));
		} else {
			zoneModel.setServiceTimeType(null);
		}
		if(order.getDeliveryInfo().getDeliveryLocation().getServiceTimeType() != null) {
			order.getDeliveryInfo().getDeliveryLocation().setServiceTimeType(serviceTimeTypeMapping
														.get(order.getDeliveryInfo().getDeliveryLocation().getServiceTimeType().getCode()));
		} else {
			order.getDeliveryInfo().getDeliveryLocation().setServiceTimeType(null);
		}
		if(order.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
				&& order.getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType() != null) {
			order.getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(serviceTimeTypeMapping
					.get(order.getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType().getCode()));
		} else {
			order.getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(null);
		}
		order.getDeliveryInfo().setReservedOrdersAtBuilding(reservation.getReservedOrdersAtBuilding());
		
		order.getDeliveryInfo().setCalculatedServiceTime(dlvService.getServiceTime(order,srvScenario, RoutingActivityType.RESERVE_TIMESLOT));
				
		reservedSlot = getDeliverySlot(timeslot.getDlvTimeslot());
		
		return schedulerReserveOrder(order, reservedSlot );
	}
	
	public  static void confirmTimeslot(DlvReservationModel reservation,IOrderModel order) throws RoutingServiceException {
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(locateOrder(order));
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(reservation.getZoneCode()));
		order.getDeliveryInfo().setDeliveryDate(reservation.getDeliveryDate());
		//schedulerUpdateOrderNo(order);
		schedulerConfirmOrder(order, reservation);
	}

	public static void cancelTimeslot(DlvReservationModel reservation, IOrderModel order) throws RoutingServiceException {
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(locateOrder(order));
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(reservation.getZoneCode()));
		order.getDeliveryInfo().setDeliveryDate(reservation.getDeliveryDate());
		schedulerCancelOrder(order,reservation);
	}
	
	public static IOrderModel calculateReservationSize(DlvReservationModel reservation, IOrderModel order, String erpOrderId) throws RoutingServiceException {
		
		PlantServiceProxy proxy = new PlantServiceProxy();
		
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(locateOrder(order));
		IZoneModel zoneModel = dlvService.getDeliveryZone(reservation.getZoneCode());
		order.getDeliveryInfo().setDeliveryZone(zoneModel);		
		order.getDeliveryInfo().setDeliveryDate(reservation.getDeliveryDate());
		IServiceTimeScenarioModel srvScenario = getRoutingScenario(order.getDeliveryInfo().getDeliveryDate());
		
		OrderEstimationResult result = proxy.estimateOrderSize(erpOrderId, srvScenario);
		if(result != null && result.getPackagingModel() != null) {
			order.getDeliveryInfo().setPackagingDetail(result.getPackagingModel());	
			order.getDeliveryInfo().setCalculatedOrderSize(result.getCalculatedOrderSize());
		}
			
		return order;
	}

	public static boolean updateReservation(DlvReservationModel reservation, IOrderModel order) throws RoutingServiceException {
				
		RoutingEngineServiceProxy engineProxy = new RoutingEngineServiceProxy();
		
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(locateOrder(order));
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(reservation.getZoneCode()));
		order.getDeliveryInfo().setDeliveryDate(reservation.getDeliveryDate());
		
		return engineProxy.schedulerUpdateOrder(order);		
	}
	
	public static  List<java.util.List<IDeliverySlot>> getDeliverySlots(DlvTimeslotModel timeslotModel) {
		List<IDeliverySlot> _slots=new ArrayList<IDeliverySlot>();
		IDeliverySlot slot=getDeliverySlot(timeslotModel);
		_slots.add(slot);
		List<java.util.List<IDeliverySlot>> slots =new ArrayList<java.util.List<IDeliverySlot>>();
		slots.add(_slots);
		return slots;
	}
	
	public static IDeliverySlot getDeliverySlots(FDReservation reservation,DlvTimeslotModel timeslotModel) {
		IDeliverySlot slot=getDeliverySlot(timeslotModel);
		return slot;
	}

	public static String getOrderNo(ContactAddressModel address) {
		return address.getId()!=null ? new StringBuilder("T").append(address.getId()).toString():new StringBuilder("T").append((int)(Math.random()/0.00001)).toString();
	}

	public static IOrderModel getOrderModel(DlvReservationModel reservation, ContactAddressModel address
														, String orderNumber, String reservationId) {

		IOrderModel order= new OrderModel();
		order.setDeliveryInfo(getDeliveryModel(address, reservationId));
		if(reservation != null) {
			if(reservation.getReservedOrderSize() != null) {
				order.setReservedOrderSize(reservation.getReservedOrderSize());
			}
			if(reservation.getReservedServiceTime() != null) {
				order.setReservedServiceTime(reservation.getReservedServiceTime());
			}
			if(reservation.getOverrideOrderSize() != null) {
				order.setOverrideOrderSize(reservation.getOverrideOrderSize());
			}
			if(reservation.getOverrideServiceTime() != null) {
				order.setOverrideServiceTime(reservation.getOverrideServiceTime());
			}
			
			IPackagingModel pModel = new PackagingModel();
			if(reservation.getNoOfCartons() != null) {
					pModel.setNoOfCartons(reservation.getNoOfCartons());
			}
			if(reservation.getNoOfCases() != null) {
				pModel.setNoOfCases(reservation.getNoOfCases());
			}
			if(reservation.getNoOfFreezers() != null) {
				pModel.setNoOfFreezers(reservation.getNoOfFreezers());
			}
			order.getDeliveryInfo().setPackagingDetail(pModel);

		}
		order.setCustomerName(new StringBuffer(100).append(address.getLastName()).append(", ").append(address.getFirstName()).toString());
		order.setCustomerNumber(address.getCustomerId());
		order.setOrderNumber(orderNumber);
		return order;
	}


	public static List<FDTimeslot> getTimeslotForDateRangeAndZone(List<FDTimeslot> _timeSlots
																	, ContactAddressModel address, RoutingActivityType routingType) throws RoutingServiceException {
		
		if(_timeSlots==null || _timeSlots.isEmpty() || address==null)
			return _timeSlots;
		
		DeliveryServiceProxy dlvService = new DeliveryServiceProxy();
		RoutingInfoServiceProxy routingInfoproxy = new RoutingInfoServiceProxy();
		
		IOrderModel order = getOrderModel(address, getOrderNo(address));				
		
		RoutingAnalyzerContext context = new RoutingAnalyzerContext();
		context.setDlvTimeSlots(_timeSlots);
		context.setServiceTimeTypes(routingInfoproxy.getRoutingServiceTimeTypes());
		context.setRoutingType(routingType);
		ILocationModel locModel = locateOrder(order);
		
		IPackagingModel historyPackageInfo = getHistoricOrderSize(order);	
		context.setHistoryPackageInfo(historyPackageInfo);
				
		Map<java.util.Date, RoutingAnalyzerCommand> analyzerCommands = getAnalyzerCommand(_timeSlots, address, context, locModel);
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(_timeSlots.get(0).getZoneCode()));
		
		//schedulerSaveLocation(order);				
		
		RoutingAnalyzerCommand tmpCommand = null;
		Iterator<java.util.Date> itr = analyzerCommands.keySet().iterator();
		java.util.Date _date = null;
		
		if(RoutingServicesProperties.isAnalyzeMultiThread()) {
			
			ExecutorService es = Executors.newFixedThreadPool(analyzerCommands.keySet().size());
		    List<Future<RoutingAnalyzerCommand>> tasks = new ArrayList<Future<RoutingAnalyzerCommand>>();
		    
			while(itr.hasNext()) {
				_date = itr.next();
				tmpCommand = analyzerCommands.get(_date);
				Future<RoutingAnalyzerCommand> future = es.submit(tmpCommand, tmpCommand);
			    tasks.add(future);
			}   			
		    try {
		      for (Future<RoutingAnalyzerCommand> future : tasks) {
		    	  RoutingAnalyzerCommand e = future.get();
		    	  LOGGER.info(" [Analyze Thread Completed ]: " + e.getException());
		      }
		      es.shutdown();
		    } catch (ExecutionException e) {
		    	e.printStackTrace();
		      //throw new RoutingServiceException();
		    } catch (InterruptedException ie) {
		    	ie.printStackTrace();
		      //throw new RoutingServiceException();
		    }
		    
		} else {
			
			while(itr.hasNext()) {
				_date = itr.next();
				tmpCommand = analyzerCommands.get(_date);	
				tmpCommand.execute();
			}
		}
		return context.getDlvTimeSlots();
	}
	
	private static Map<java.util.Date, RoutingAnalyzerCommand> getAnalyzerCommand(List<FDTimeslot> dlvTimeSlots 
																									, ContactAddressModel address
																									, RoutingAnalyzerContext context
																									, ILocationModel locModel) {

		Map<java.util.Date, RoutingAnalyzerCommand> data = new HashMap<java.util.Date, RoutingAnalyzerCommand>();				
		RoutingAnalyzerCommand tmpCommand = null;					
		IOrderModel order = null;
		List<IDeliverySlot> _routingTimeSlots = null;
		
		for(int i=0; i<dlvTimeSlots.size(); i++) {
			
			FDTimeslot dlvTimeSlot = dlvTimeSlots.get(i);
			IDeliverySlot routeDlvTimeslot = dlvTimeSlot.getDlvTimeslot().getRoutingSlot();
			if(routeDlvTimeslot != null && routeDlvTimeslot.isDynamicActive()) {
				if(data.containsKey(dlvTimeSlot.getBaseDate())) {
					tmpCommand = data.get(dlvTimeSlot.getBaseDate());
					_routingTimeSlots = tmpCommand.getRoutingTimeSlots();
					_routingTimeSlots.add(routeDlvTimeslot);
					data.put(dlvTimeSlot.getBaseDate(), tmpCommand);
				} else {
					List<IDeliverySlot> _timeSlots = new ArrayList<IDeliverySlot>();
					order = getOrderModel(address, getOrderNo(address));
					
					tmpCommand = new RoutingAnalyzerCommand();
					tmpCommand.setOrder(order);
					tmpCommand.setRoutingTimeSlots(_timeSlots);
					tmpCommand.setContext(context);
					tmpCommand.setDeliveryDate(dlvTimeSlot.getBaseDate());
					order.getDeliveryInfo().setDeliveryLocation(locModel);
					
					_timeSlots.add(routeDlvTimeslot);
					data.put(dlvTimeSlot.getBaseDate(), tmpCommand);
				}
			}
		}

		return data;
	}
	
	public FDDynamicTimeslotList getTimeslotsForDateRangeAndZoneEx(List<FDTimeslot> timeSlots, TimeslotEventModel event, ContactAddressModel address) {
		
		try {
				DlvManagerSB sb = getDlvManagerHome().create();
				return sb.getTimeslotForDateRangeAndZoneEx(timeSlots, event,address,RoutingActivityType.GET_TIMESLOT);

			} catch (Exception ce) {
				dlvHome = null;
				/*if(useAltProvider==false) {
					setUseAltProvider(true);
				}*/
				ce.printStackTrace();

			}
			FDDynamicTimeslotList dynamicTimeslots = new FDDynamicTimeslotList();
			dynamicTimeslots.setTimeslots(new ArrayList<FDTimeslot>());
			return dynamicTimeslots;
	}
	
	public   void sendDateRangeAndZoneForTimeslots(List<FDTimeslot> timeSlots, ContactAddressModel address) {

		try {
				RoutingGatewaySB routingSB = getRoutingGatewayHome().create();
				routingSB.sendDateRangeAndZoneForTimeslots(timeSlots, address);

			} catch (Exception ce) {
				home=null;
				if(useAltProvider==false) {
					setUseAltProvider(true);
				}
				ce.printStackTrace();

			}
	}

	public   boolean sendTimeslotReservationRequest(DlvReservationModel reservation, ContactAddressModel address, FDTimeslot timeslot, TimeslotEventModel event)  {

		try {
				RoutingGatewaySB routingSB = getRoutingGatewayHome().create();
				routingSB.sendReserveTimeslotRequest(reservation,address, timeslot, event);
				return true;

			} catch (Exception ce) {
				home=null;
				if(useAltProvider==false) {
					setUseAltProvider(true);
				}
				ce.printStackTrace();
				return false;

			}
	}

	public boolean sendCommitReservationRequest(DlvReservationModel reservation,ContactAddressModel address, TimeslotEventModel event) {

		try {
			RoutingGatewaySB routingSB = getRoutingGatewayHome().create();
			routingSB.sendCommitReservationRequest(reservation, address,event);
			return true;

		} catch (Exception ce) {
			home=null;
			if(useAltProvider==false) {
				setUseAltProvider(true);
			}
			ce.printStackTrace();
			return false;

		}

	}

	public boolean sendReleaseReservationRequest(DlvReservationModel reservation,ContactAddressModel address, TimeslotEventModel event) {

		try {
			RoutingGatewaySB routingSB = getRoutingGatewayHome().create();
			routingSB.sendReleaseReservationRequest(reservation,address,event);
			return true;

		} catch (Exception ce) {
			home=null;
			if(useAltProvider==false) {
				setUseAltProvider(true);
			}
			ce.printStackTrace();
			return false;
		}

	}
	
		
	private static boolean needsPurge(Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> srcCutOffInstance) {
		if(srcCutOffInstance != null) {
			Collection<Map<RoutingTimeOfDay, List<IWaveInstance>>> _tmpCutOffMpp = srcCutOffInstance.values();
			for(Map<RoutingTimeOfDay, List<IWaveInstance>> _tmpMpp : _tmpCutOffMpp) {
				for(Map.Entry<RoutingTimeOfDay, List<IWaveInstance>> _tmpInnerMpp : _tmpMpp.entrySet()) {					
					for(IWaveInstance _srcWaveInst : _tmpInnerMpp.getValue()) {
						if(_srcWaveInst != null && _srcWaveInst.getRoutingWaveInstanceId() != null
								&& _srcWaveInst.getRoutingWaveInstanceId().length() > 0) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	public static List<IWaveInstance> synchronizeWaveInstance(IRoutingSchedulerIdentity schedulerId
								, Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> waveInstanceTree
								, Set<String> inSyncZones, Map<String, TrnFacilityType> routingLocationMap, Set inSyncRoutingWaveInstIds) {
		
		List<IWaveInstance> waveInstancesResult = new ArrayList<IWaveInstance>();
		CapacityEngineServiceProxy capacityProxy = new CapacityEngineServiceProxy();
		RoutingEngineServiceProxy routingProxy = new RoutingEngineServiceProxy();
		
		//Dispatch-> CutOff ->WaveInstance
		Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> srcCutOffInstance = waveInstanceTree.get(schedulerId.getDeliveryDate())
																						.get(schedulerId.getArea().getAreaCode());
		if(srcCutOffInstance != null) {
			List<IWaveInstance> destinationInstances = null;
			try {
				//if(needsPurge(srcCutOffInstance)) {
				if(!inSyncZones.contains(schedulerId.getArea().getAreaCode())) {
					routingProxy.purgeOrders(schedulerId, true);
				}
				destinationInstances = capacityProxy.retrieveWaveInstances(schedulerId);
			} catch(RoutingServiceException e) {
				//e.printStackTrace();
				LOGGER.info(" Unable to retrieve Wave for sync : " + schedulerId);
			}
			if(destinationInstances != null) {
				//First Collect Wave Instance Data from transp and group them by cutoff
				Collection<Map<RoutingTimeOfDay, List<IWaveInstance>>> _tmpCutOffMpp = srcCutOffInstance.values();
				//CutOff to Wave Instance Listing
				Map<RoutingTimeOfDay, List<IWaveInstance>> toSyncWaveMpp = new TreeMap<RoutingTimeOfDay, List<IWaveInstance>>();
				
				for(Map<RoutingTimeOfDay, List<IWaveInstance>> _tmpMpp : _tmpCutOffMpp) {
					for(Map.Entry<RoutingTimeOfDay, List<IWaveInstance>> _tmpInnerMpp : _tmpMpp.entrySet()) {
						if(!toSyncWaveMpp.containsKey(_tmpInnerMpp.getKey())) {
							toSyncWaveMpp.put(_tmpInnerMpp.getKey(), new ArrayList<IWaveInstance>());
						}
						for(IWaveInstance _srcWaveInst : _tmpInnerMpp.getValue()) {
							toSyncWaveMpp.get(_tmpInnerMpp.getKey()).add(_srcWaveInst);
						}											
					}										
				}
				
				Map<RoutingTimeOfDay, List<IWaveInstance>> blankWaveMpp = new TreeMap<RoutingTimeOfDay, List<IWaveInstance>>();
				Map<RoutingTimeOfDay, List<IWaveInstance>> nonBlankWaveMpp = new TreeMap<RoutingTimeOfDay, List<IWaveInstance>>();
				Map<String, IWaveInstance> waveIdMap = new HashMap<String, IWaveInstance>();
				
				for(IWaveInstance _destInst : destinationInstances) {
					if(_destInst.getCutOffTime() != null) {
						waveIdMap.put(_destInst.getRoutingWaveInstanceId(), _destInst);
						if(_destInst.getNoOfResources() == 0 
										|| !inSyncRoutingWaveInstIds.contains(_destInst.getRoutingWaveInstanceId())) {
							if(!blankWaveMpp.containsKey(_destInst.getCutOffTime())) {
								blankWaveMpp.put(_destInst.getCutOffTime(), new ArrayList<IWaveInstance>());
							}
							blankWaveMpp.get(_destInst.getCutOffTime()).add(_destInst);
						} else {
							if(!nonBlankWaveMpp.containsKey(_destInst.getCutOffTime())) {
								nonBlankWaveMpp.put(_destInst.getCutOffTime(), new ArrayList<IWaveInstance>());
							}
							nonBlankWaveMpp.get(_destInst.getCutOffTime()).add(_destInst);
						}
					}
				}
				
				
				for(Map.Entry<RoutingTimeOfDay, List<IWaveInstance>> _tmpMpp : toSyncWaveMpp.entrySet()) {
					IWaveInstance syncWaveInstance = null;
					for(IWaveInstance _syncWaveInst : _tmpMpp.getValue()) {
						if(_syncWaveInst.getRoutingWaveInstanceId() == null
								|| waveIdMap.get(_syncWaveInst.getRoutingWaveInstanceId()) == null) {
							if(blankWaveMpp.containsKey(_tmpMpp.getKey())) {
								List<IWaveInstance> blankWaveLst = blankWaveMpp.get(_tmpMpp.getKey());
								if(blankWaveLst != null && blankWaveLst.size() > 0) {
									syncWaveInstance = _syncWaveInst;
									syncWaveInstance.setRoutingWaveInstanceId(blankWaveLst.remove(0).getRoutingWaveInstanceId());
								}
							}
						} else {
							syncWaveInstance = _syncWaveInst;
						}
												
						if(syncWaveInstance == null) {
							System.out.println("UNABLE TO FIND WAVE TO SYNCHRONIZATION :"+schedulerId+"->"+_syncWaveInst+"\n");
							System.out.println("destinationInstances :"+schedulerId+"->"+destinationInstances);
							
							_syncWaveInst.setNotificationMessage("WAVE TEMPLATE ERROR");
							_syncWaveInst.setStatus(EnumWaveInstanceStatus.NOTSYNCHRONIZED);
							waveInstancesResult.add(_syncWaveInst);
						} else if (waveIdMap.get(syncWaveInstance.getRoutingWaveInstanceId()) != null) {
							System.out.println("SYNCHRONIZING WAVE :"+schedulerId+"->"+_syncWaveInst);
							syncWaveInstance.copyBaseAttributes(waveIdMap.get(syncWaveInstance.getRoutingWaveInstanceId()));
							
							IRoutingDepotId routingDepotId = new RoutingDepotId();
							routingDepotId.setLocationId(syncWaveInstance.getOriginFacility());
							routingDepotId.setRegionID(schedulerId.getRegionId());
							/*if(syncWaveInstance.getOriginFacility() != null)
								routingDepotId.setLocationType(routingLocationMap.get(syncWaveInstance.getOriginFacility()).getName());*/
							routingDepotId.setLocationType(RoutingServicesProperties.getDefaultUpsLocationType());
							syncWaveInstance.setDepotId(routingDepotId);

							List<String> unassignedOrder = null;
							try {
								unassignedOrder = capacityProxy.saveWaveInstances(schedulerId, syncWaveInstance, syncWaveInstance.isForce());
							
								syncWaveInstance.setNotificationMessage(null);
								syncWaveInstance.setStatus(EnumWaveInstanceStatus.SYNCHRONIZED);
								if(unassignedOrder != null && unassignedOrder.size() > 0) {
									if(!syncWaveInstance.isForce()) {
										syncWaveInstance.setStatus(EnumWaveInstanceStatus.NOTSYNCHRONIZED);
										syncWaveInstance.setNotificationMessage(unassignedOrder.size() + " orders will be unassigned");
									} else {
										syncWaveInstance.setForce(false);
									}
								}
								waveInstancesResult.add(syncWaveInstance);								
							} catch(RoutingServiceException e) {
								//e.printStackTrace();
								LOGGER.info("FAILED to SYNCHRONIZE WAVE INSTANCE : " + schedulerId+" : "+syncWaveInstance);
							}
						} else {
							System.out.println("UNABLE TO FIND WAVE TO SYNCHRONIZATION :"+schedulerId+"->"+_syncWaveInst+"\n");
							System.out.println("destinationInstances :"+schedulerId+"->"+destinationInstances);
						}
					}										
				}
			}								
		}
		return waveInstancesResult;
	}
	
	private static void schedulerConfirmOrder(IOrderModel orderModel,DlvReservationModel reservation) throws RoutingServiceException {

		RoutingEngineServiceProxy routingService=new RoutingEngineServiceProxy();
		orderModel.setOrderNumber(reservation.getOrderId());
		orderModel.getDeliveryInfo().setReservationId(reservation.getId());		
		routingService.schedulerConfirmOrder(orderModel);
		schedulerUpdateOrderNo(orderModel);
	}

	private static boolean schedulerUpdateOrderNo(IOrderModel orderModel) throws RoutingServiceException {

		RoutingEngineServiceProxy routingService=new RoutingEngineServiceProxy();        
        return routingService.schedulerUpdateOrderNo(orderModel);        
	}

	public static IOrderModel schedulerRetrieveOrder(IOrderModel orderModel) throws RoutingServiceException {

		RoutingEngineServiceProxy routingService=new RoutingEngineServiceProxy();        
        return routingService.schedulerRetrieveOrder(orderModel);        
	}
	private static void schedulerCancelOrder(IOrderModel orderModel,DlvReservationModel reservation) throws RoutingServiceException {


		RoutingEngineServiceProxy routingService=new RoutingEngineServiceProxy();
		orderModel.setOrderNumber( reservation.getOrderId());
		routingService.schedulerCancelOrder(orderModel);
	}
	
	private static IDeliveryReservation schedulerReserveOrder(IOrderModel orderModel, IDeliverySlot slot) throws RoutingServiceException {
		
		schedulerSaveLocation(orderModel);
		IDeliveryReservation reservation=new RoutingEngineServiceProxy().schedulerReserveOrder(orderModel, slot
											, RoutingServicesProperties.getDefaultLocationType(), RoutingServicesProperties.getDefaultOrderType());
		LOGGER.info("schedulerReserveOrder():: DeliveryReservation:"+reservation.isReserved());
		return reservation;
	}

	

	private static void schedulerSaveLocation(IOrderModel orderModel) throws RoutingServiceException {

		new RoutingEngineServiceProxy().schedulerSaveLocation(orderModel, RoutingServicesProperties.getDefaultLocationType());
	}


	private static IOrderModel getOrderModel(ContactAddressModel address, String orderNum) {

		return getOrderModel(null, address, orderNum, orderNum);
	}

	private static ILocationModel getLocation(ContactAddressModel address) {
		
		IBuildingModel building = new BuildingModel();
		
		building.setStreetAddress1(address.getAddress1());
		building.setStreetAddress2(address.getAddress2());
		
		building.setCity(address.getCity());
		building.setCountry(address.getCountry());
		building.setState(address.getState());
		building.setZipCode(address.getZipCode());
		building.setBuildingId(address.getBuildingId());
		ILocationModel loc= new LocationModel(building);
		loc.setApartmentNumber(address.getApartment());
		return loc;
	}

	public static IDeliverySlot getDeliverySlot(DlvTimeslotModel timeSlot) {

		IDeliverySlot deliverySlot=new DeliverySlot();
		
		IRoutingSchedulerIdentity identity=new RoutingSchedulerIdentity();
		identity.setDeliveryDate(timeSlot.getBaseDate());
		deliverySlot.setZoneCode(timeSlot.getZoneCode());
		deliverySlot.setSchedulerId(identity);
		deliverySlot.setStartTime(timeSlot.getRoutingStartTimeAsDate());
		deliverySlot.setStopTime(timeSlot.getRoutingEndTimeAsDate());
		deliverySlot.setWaveCode(timeSlot.getRoutingSlot().getWaveCode()); 
		return deliverySlot;
	}

	private static ILocationModel locateOrder(IOrderModel order) throws RoutingServiceException  {
		GeographyServiceProxy geoSrv = new GeographyServiceProxy();
		
		ILocationModel location = geoSrv.locateOrder(order);
		if(location != null && order != null && order.getDeliveryInfo() != null) {
			order.getDeliveryInfo().setDeliveryLocation(location);
		}
		return location;
	}


	private static IDeliveryModel getDeliveryModel(ContactAddressModel address, String reservationId) {
		IDeliveryModel dlvInfo=new DeliveryModel();
		dlvInfo.setDeliveryLocation(getLocation(address));
		dlvInfo.setReservationId(reservationId);
		return dlvInfo;
	}

	
	private  RoutingGatewayHome getRoutingGatewayHome() throws FDResourceException {
			lookupRoutingGatewayHome();
			return home;

	}
	
	private  DlvManagerHome getDlvManagerHome() throws FDResourceException {
		lookupDlvManagerHome();
		return dlvHome;

	}

	public static IServiceTimeScenarioModel getRoutingScenario(Date dlvDate) throws RoutingServiceException {
		return new RoutingInfoServiceProxy().getRoutingScenarioByDate(dlvDate);
	}
	
	public static IServiceTimeScenarioModel getRoutingScenarioEx(Date dlvDate, Date cutoff, Date startTime, Date endTime) throws RoutingServiceException {
		return new RoutingInfoServiceProxy().getRoutingScenarioEx(dlvDate,cutoff,startTime,endTime);
	}

	protected static OrderEstimationResult estimateOrderSize(IOrderModel order, IServiceTimeScenarioModel scenario, IPackagingModel historyInfo) throws RoutingServiceException {
		return new PlantServiceProxy().estimateOrderSize(order, scenario, historyInfo);
	}

	public static IPackagingModel getHistoricOrderSize(IOrderModel order) throws RoutingServiceException {
		return new PlantServiceProxy().getHistoricOrderSize(order);
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
	
	public static boolean hasAnyDynamicEnabled(List<FDTimeslot> timeSlots) {
		
		boolean result = false;
		if(timeSlots != null) {
			for (FDTimeslot slot : timeSlots) {
				if(slot != null && slot.getDlvTimeslot() != null && slot.getDlvTimeslot().getRoutingSlot() != null) {		    	
					result = result || slot.getDlvTimeslot().getRoutingSlot().isDynamicActive();
				}
			}
		}
		return result;
	}
	
	public void finalize() {
		close(routingProvider);
		close(altProvider);
	}
		
	private static void close(Context ctx) {
		if(ctx!=null) {
			try {
				ctx.close();
			}catch(Exception e) {
			}
		}
	}

}
