package com.freshdirect.fdstore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.routing.ejb.RoutingGatewayHome;
import com.freshdirect.delivery.routing.ejb.RoutingGatewaySB;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.service.proxy.PlantServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class RoutingUtil {

	private static RoutingUtil _instance = null;
	private RoutingGatewayHome home=null;
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

	public static RoutingUtil getInstance() {
		if (_instance == null) {
			_instance = new RoutingUtil();
		}
		return _instance;
	}


	public static  IDeliveryReservation reserveTimeslot(DlvReservationModel reservation, IOrderModel order , FDTimeslot timeslot) throws RoutingServiceException {
		
		IDeliverySlot reservedSlot=null;
		DeliveryServiceProxy dlvService = new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(getLocation(order));
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(timeslot.getZoneCode()));
		order.getDeliveryInfo().setDeliveryDate(timeslot.getBaseDate());
		
		if(reservation.getOrderSize() != null && reservation.getOrderSize() > 0) {
			reservation.setReservedOrderSize(reservation.getOrderSize());
		}
		if(reservation.getServiceTime() != null && reservation.getServiceTime() > 0) {
			reservation.setReservedServiceTime(reservation.getServiceTime());
		}
		if(reservation.getReservedOrderSize() == null || reservation.getReservedOrderSize() == 0) {
			IPackagingModel historyPackageInfo = getHistoricOrderSize(order);
			IServiceTimeScenarioModel srvScenario = getRoutingScenario(order.getDeliveryInfo().getDeliveryDate());
			order.getDeliveryInfo().setPackagingInfo(estimateOrderSize(order, srvScenario, historyPackageInfo));
			order.getDeliveryInfo().setServiceTime(dlvService.estimateOrderServiceTime(order,srvScenario));
		} else {
			order.getDeliveryInfo().setOrderSize(reservation.getReservedOrderSize());
			order.getDeliveryInfo().setServiceTime(reservation.getReservedServiceTime());
		}
		
		reservedSlot = getDeliverySlot(timeslot.getDlvTimeslot());
		
		return schedulerReserveOrder(order,reservedSlot );
	}
	
	public  static void confirmTimeslot(DlvReservationModel reservation,IOrderModel order) throws RoutingServiceException {
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(getLocation(order));
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(reservation.getZoneCode()));
		order.getDeliveryInfo().setDeliveryDate(reservation.getDeliveryDate());
		//schedulerUpdateOrderNo(order);
		schedulerConfirmOrder(order, reservation);
	}

	public static void cancelTimeslot(DlvReservationModel reservation,IOrderModel order) throws RoutingServiceException {
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(getLocation(order));
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(reservation.getZoneCode()));
		order.getDeliveryInfo().setDeliveryDate(reservation.getDeliveryDate());
		schedulerCancelOrder(order,reservation);
	}
	
	public static IOrderModel updateReservationStatus(DlvReservationModel reservation, IOrderModel order, String erpOrderId) throws RoutingServiceException {
		
		PlantServiceProxy proxy = new PlantServiceProxy();
		RoutingEngineServiceProxy engineProxy = new RoutingEngineServiceProxy();
		
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(getLocation(order));
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(reservation.getZoneCode()));
		order.getDeliveryInfo().setDeliveryDate(reservation.getDeliveryDate());
		IServiceTimeScenarioModel srvScenario = getRoutingScenario(order.getDeliveryInfo().getDeliveryDate());
		
		IPackagingModel packageModel = proxy.estimateOrderSize(erpOrderId, srvScenario);
		if(packageModel != null) {
			order.getDeliveryInfo().setPackagingInfo(packageModel);
			order.setUnassignedOrderSize(packageModel.getTotalSize1());
			
			order.getDeliveryInfo().setServiceTime(dlvService.estimateOrderServiceTime(order, srvScenario));
			order.setUnassignedServiceTime(order.getDeliveryInfo().getServiceTime());
		}
			
		return order;
	}

	public static boolean updateReservation(DlvReservationModel reservation, IOrderModel order) throws RoutingServiceException {
		
		PlantServiceProxy proxy = new PlantServiceProxy();
		RoutingEngineServiceProxy engineProxy = new RoutingEngineServiceProxy();
		
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(getLocation(order));
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
	
	public static List<java.util.List<IDeliverySlot>> getDeliverySlots(FDReservation reservation,DlvTimeslotModel timeslotModel) {
		List<IDeliverySlot> _slots=new ArrayList<IDeliverySlot>();
		IDeliverySlot slot=getDeliverySlot(timeslotModel);
		_slots.add(slot);
		List<java.util.List<IDeliverySlot>> slots =new ArrayList<java.util.List<IDeliverySlot>>();
		slots.add(_slots);
		return slots;
	}

	public static String getOrderNo(ContactAddressModel address) {
		return address.getId()!=null ? new StringBuilder("T").append(address.getId()).toString():new StringBuilder("T").append((int)(Math.random()/0.00001)).toString();
	}

	public static IOrderModel getOrderModel(ContactAddressModel address, String orderNumber, String reservationId) {

		IOrderModel order= new OrderModel();
		order.setDeliveryInfo(getDeliveryModel(address, reservationId));
		order.setCustomerName(new StringBuffer(100).append(address.getLastName()).append(", ").append(address.getFirstName()).toString());
		order.setCustomerNumber(address.getCustomerId());
		order.setOrderNumber(orderNumber);
		return order;
	}


	public static List<FDTimeslot> getTimeslotForDateRangeAndZone(List<FDTimeslot> _timeSlots, ContactAddressModel address) throws RoutingServiceException {
		
		if(_timeSlots==null || _timeSlots.isEmpty() || address==null)
			return _timeSlots;
		
		DeliveryServiceProxy dlvService = new DeliveryServiceProxy();
		
		
		IOrderModel order = getOrderModel(address, getOrderNo(address));				
		
		RoutingAnalyzerContext context = new RoutingAnalyzerContext();
		context.setDlvTimeSlots(_timeSlots);
			
		ILocationModel locModel = getLocation(order);
		
		IPackagingModel historyPackageInfo = getHistoricOrderSize(order);	
		context.setHistoryPackageInfo(historyPackageInfo);
					
		Map<java.util.Date, RoutingAnalyzerCommand> analyzerCommands = getAnalyzerCommand(_timeSlots, address, context, locModel);
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(_timeSlots.get(0).getZoneCode()));
		schedulerSaveLocation(order);				
		
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

	public   boolean sendTimeslotReservationRequest(DlvReservationModel reservation, ContactAddressModel address, FDTimeslot timeslot)  {

		try {
				RoutingGatewaySB routingSB = getRoutingGatewayHome().create();
				routingSB.sendReserveTimeslotRequest(reservation,address, timeslot);
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

	public boolean sendCommitReservationRequest(DlvReservationModel reservation,ContactAddressModel address) {

		try {
			RoutingGatewaySB routingSB = getRoutingGatewayHome().create();
			routingSB.sendCommitReservationRequest(reservation, address);
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

	public boolean sendReleaseReservationRequest(DlvReservationModel reservation,ContactAddressModel address) {

		try {
			RoutingGatewaySB routingSB = getRoutingGatewayHome().create();
			routingSB.sendReleaseReservationRequest(reservation,address);
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
	
	private static void schedulerConfirmOrder(IOrderModel orderModel,DlvReservationModel reservation) throws RoutingServiceException {

		RoutingEngineServiceProxy routingService=new RoutingEngineServiceProxy();
		orderModel.setOrderNumber(reservation.getOrderId());
		orderModel.getDeliveryInfo().setReservationId(reservation.getId());		
		routingService.schedulerConfirmOrder(orderModel);
	}

	private static boolean schedulerUpdateOrderNo(IOrderModel orderModel) throws RoutingServiceException {

		RoutingEngineServiceProxy routingService=new RoutingEngineServiceProxy();        
        return routingService.schedulerUpdateOrderNo(orderModel);        
	}

	private static void schedulerCancelOrder(IOrderModel orderModel,DlvReservationModel reservation) throws RoutingServiceException {


		RoutingEngineServiceProxy routingService=new RoutingEngineServiceProxy();
		orderModel.setOrderNumber( reservation.getOrderId());
		routingService.schedulerCancelOrder(orderModel);
	}
	private static IDeliveryReservation schedulerReserveOrder(IOrderModel orderModel, IDeliverySlot slot) throws RoutingServiceException {

		IDeliveryReservation reservation=new RoutingEngineServiceProxy().schedulerReserveOrder(orderModel, slot, RoutingServicesProperties.getDefaultLocationType(), RoutingServicesProperties.getDefaultOrderType());
		LOGGER.info("schedulerReserveOrder():: DeliveryReservation:"+reservation.isReserved());
		return reservation;
	}

	

	private static void schedulerSaveLocation(IOrderModel orderModel) throws RoutingServiceException {

		new RoutingEngineServiceProxy().schedulerSaveLocation(orderModel, RoutingServicesProperties.getDefaultLocationType());
	}



	private static IOrderModel getOrderModel(ContactAddressModel address, String orderNum) {

		return getOrderModel(address,orderNum, orderNum);
	}

	private static ILocationModel getLocation(ContactAddressModel address) {

		ILocationModel loc= new LocationModel();
		loc.setStreetAddress1(address.getAddress1());
		loc.setStreetAddress2(address.getAddress2());
		loc.setApartmentNumber(address.getApartment());
		loc.setCity(address.getCity());
		loc.setCountry(address.getCountry());
		loc.setState(address.getState());
		loc.setZipCode(address.getZipCode());
		return loc;
	}

	

	private static IDeliverySlot getDeliverySlot(DlvTimeslotModel timeSlot) {

		IDeliverySlot deliverySlot=new DeliverySlot();
		IRoutingSchedulerIdentity identity=new RoutingSchedulerIdentity();
		identity.setDeliveryDate(timeSlot.getBaseDate());
		deliverySlot.setSchedulerId(identity);
		deliverySlot.setStartTime(timeSlot.getStartTimeAsDate());
		deliverySlot.setStopTime(timeSlot.getEndTimeAsDate());
		deliverySlot.setWaveCode(getHourAMPM(timeSlot.getCutoffTimeAsDate()));
		return deliverySlot;
	}

	private static ILocationModel getLocation(IOrderModel order) throws RoutingServiceException  {
		GeographyServiceProxy geoSrv = new GeographyServiceProxy();
		return geoSrv.locateOrder(order);
	}


	private static IDeliveryModel getDeliveryModel(ContactAddressModel address, String reservationId) {
		IDeliveryModel dlvInfo=new DeliveryModel();
		dlvInfo.setDeliveryLocation(getLocation(address));
		dlvInfo.setReservationId(reservationId);
		return dlvInfo;
	}

	private static String getHourAMPM(Date date) {
		try {
			return  DateUtil.formatTimeAMPM(date);
		} catch (ParseException pe) {
			return null;
		}
	}

	private  RoutingGatewayHome getRoutingGatewayHome() throws FDResourceException {
			lookupRoutingGatewayHome();
			return home;

	}

	protected static IServiceTimeScenarioModel getRoutingScenario(Date dlvDate) throws RoutingServiceException {
		return new RoutingInfoServiceProxy().getRoutingScenario(dlvDate);
	}

	protected static IPackagingModel estimateOrderSize(IOrderModel order, IServiceTimeScenarioModel scenario, IPackagingModel historyInfo) throws RoutingServiceException {
		return new PlantServiceProxy().estimateOrderSize(order, scenario, historyInfo);
	}

	protected static IPackagingModel getHistoricOrderSize(IOrderModel order) throws RoutingServiceException {
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
