package com.freshdirect.fdstore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	
	private static RoutingUtil _instance = new RoutingUtil();
	private RoutingGatewayHome home=null;
	private Context routingProvider=null;
	private Context altProvider=null;
	private boolean useAltProvider=false;
	
	

	private synchronized void setUseAltProvider(boolean useAltProvider) {
		this.useAltProvider = useAltProvider;
	}

	public RoutingGatewayHome getHome() {
		return home;
	}

	private RoutingUtil() {
		try {
			this.routingProvider = FDStoreProperties.getRoutingInitialContext();
		} catch (NamingException e) {
			useAltProvider=true;
			e.printStackTrace();
			//throw new RuntimeException("Unable to get InitialContext "+e.getMessage());
		}
		try {
			this.altProvider=FDStoreProperties.getInitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	
	protected  void lookupRoutingGatewayHome() throws FDResourceException {
		
		try {
			if(home==null) {
				if(useAltProvider)
					home = (RoutingGatewayHome) altProvider.lookup( FDStoreProperties.getRoutingGatewayHome());
				else 
					home = (RoutingGatewayHome) routingProvider.lookup( FDStoreProperties.getRoutingGatewayHome());
			}
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		}
	}
	
	public static RoutingUtil getInstance() {
		
		return _instance;
	}
	
	
	public static  IDeliveryReservation reserveTimeslot(IOrderModel order , FDTimeslot timeslot) throws RoutingServiceException {
		
		IDeliverySlot reservedSlot=null;
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(getLocation(order));
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(timeslot.getZoneCode()));
		order=setDeliveryInfo(order,timeslot.getBaseDate());
		reservedSlot = getDeliverySlot(timeslot.getDlvTimeslot());
		return schedulerReserveOrder(order,reservedSlot );
	}
	public  static void confirmTimeslot(DlvReservationModel reservation,IOrderModel order) throws RoutingServiceException {
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(getLocation(order));
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(reservation.getZoneCode()));
		order.getDeliveryInfo().setDeliveryDate(reservation.getDeliveryDate());
		schedulerConfirmOrder(order, reservation);
	}
	
	public static void cancelTimeslot(DlvReservationModel reservation,IOrderModel order) throws RoutingServiceException {
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(getLocation(order));
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(reservation.getZoneCode()));
		order.getDeliveryInfo().setDeliveryDate(reservation.getDeliveryDate());
		schedulerCancelOrder(order,reservation);
	}
	public static boolean updateReservation(DlvReservationModel reservation,IOrderModel order) throws RoutingServiceException {
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryLocation(getLocation(order));
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(reservation.getZoneCode()));
		order.getDeliveryInfo().setDeliveryDate(reservation.getDeliveryDate());
		//this.schedulerUpdateOrder(order, reservation,reservation.getRoutingOrderId());
		return false;
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


	public static List<java.util.List<IDeliverySlot>> getTimeslotForDateRangeAndZone(List<FDTimeslot> _timeSlots, ContactAddressModel address) throws RoutingServiceException {
		
		List<java.util.List<IDeliverySlot>> timeSlots=new ArrayList<java.util.List<IDeliverySlot>>();
		
		IOrderModel order=getOrderModel(address,getOrderNo(address));
			if(_timeSlots==null ||_timeSlots.isEmpty()|| address==null)
				return timeSlots;
			DeliveryServiceProxy dlvService = new DeliveryServiceProxy();
			order.getDeliveryInfo().setDeliveryLocation(getLocation(order));
			String zoneCode = null;
			Map<java.util.Date,java.util.List<IDeliverySlot>> routingTimeSlots=getDeliverySlot(_timeSlots);
			//LOGGER.info("getTimeslotForDateRangeAndZoneEx():: DeliverySlot Map:"+routingTimeSlots);
			Iterator<java.util.Date> it = routingTimeSlots.keySet().iterator();
			java.util.Date _date = null;
			List<IDeliverySlot> _tmpSlots = null;
			boolean isLocationSaved = false;
			IPackagingModel historyPackageInfo = getHistoricOrderSize(order);
			
			while(it.hasNext()) {
				_date = it.next();
				_tmpSlots = routingTimeSlots.get(_date);
				if(_tmpSlots != null && _tmpSlots.size() > 0) {
					zoneCode = _tmpSlots.get(0).getZoneCode();
					order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(zoneCode));
					order.getDeliveryInfo().setDeliveryDate(_date);
					IServiceTimeScenarioModel srvScenario=getRoutingScenario(order.getDeliveryInfo().getDeliveryDate());
					order.getDeliveryInfo().setPackagingInfo(estimateOrderSize(order, srvScenario, historyPackageInfo));
					order.getDeliveryInfo().setServiceTime(dlvService.estimateOrderServiceTime(order, srvScenario));
					if(!isLocationSaved) {
						isLocationSaved = true;
						schedulerSaveLocation(order);
					}
					timeSlots.add(schedulerAnalyzeOrder(order, _date, 1, _tmpSlots));
				}
				//LOGGER.info("getTimeslotForDateRangeAndZoneEx():: Date:" +RoutingDateUtil.formatDateTime(_date)+" DeliverySlot:"+timeSlots);
			}
			return timeSlots;
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
		//schedulerUpdateOrder(orderModel,reservation, previousOrderId);
		routingService.schedulerConfirmOrder(orderModel);
	}
	
	private boolean schedulerUpdateOrder(IOrderModel orderModel,DlvReservationModel reservation, String previousOrderId) throws RoutingServiceException {
		
		RoutingEngineServiceProxy routingService=new RoutingEngineServiceProxy();
        orderModel.setOrderNumber(reservation.getOrderId());
        boolean isUpdated = true;
        if(orderModel.getOrderNumber() != null && !orderModel.getOrderNumber().equalsIgnoreCase(previousOrderId)) {
             isUpdated=routingService.schedulerUpdateOrder(orderModel, previousOrderId);
             //LOGGER.debug("routingService.schedulerUpdateOrder() :"+isUpdated);
        }  
        return isUpdated;
        
	}
	
	private static void schedulerCancelOrder(IOrderModel orderModel,DlvReservationModel reservation) throws RoutingServiceException {
		
		
		RoutingEngineServiceProxy routingService=new RoutingEngineServiceProxy();
		orderModel.setOrderNumber( reservation.getOrderId());
		routingService.schedulerCancelOrder(orderModel);
	}
	private static IDeliveryReservation schedulerReserveOrder(IOrderModel orderModel, IDeliverySlot slot) throws RoutingServiceException {
		
		IDeliveryReservation reservation=new RoutingEngineServiceProxy().schedulerReserveOrder(orderModel, slot, RoutingServicesProperties.getDefaultLocationType(), RoutingServicesProperties.getDefaultOrderType());
		//LOGGER.info("schedulerReserveOrder():: DeliveryReservation:"+reservation.isReserved());
		return reservation;
	}

	private static List<IDeliverySlot> schedulerAnalyzeOrder(IOrderModel orderModel, Date startDate, int noOfDays, List<IDeliverySlot> slots) throws RoutingServiceException {
		
		return new RoutingEngineServiceProxy().schedulerAnalyzeOrder(orderModel, RoutingServicesProperties.getDefaultLocationType(), RoutingServicesProperties.getDefaultOrderType(), startDate, noOfDays, slots);
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
	
	private static Map<java.util.Date,java.util.List<IDeliverySlot>>  getDeliverySlot(List<FDTimeslot> dlvTimeSlots ) {
		
		Map<java.util.Date,java.util.List<IDeliverySlot>> data=new HashMap<java.util.Date,java.util.List<IDeliverySlot>>();
		
		for(int i=0;i<dlvTimeSlots.size();i++) {
			IDeliverySlot routeDlvTimeslot=new DeliverySlot();
			FDTimeslot dlvTimeSlot= dlvTimeSlots.get(i);
			
			routeDlvTimeslot.setStartTime(dlvTimeSlot.getBegDateTime());
			routeDlvTimeslot.setStopTime(dlvTimeSlot.getEndDateTime());
			routeDlvTimeslot.setWaveCode(getHourAMPM(dlvTimeSlot.getCutoffDateTime()));
			routeDlvTimeslot.setZoneCode(dlvTimeSlot.getZoneCode());
			
			if(data.containsKey(dlvTimeSlot.getBaseDate())) {
				List<IDeliverySlot> _timeSlots=data.get(dlvTimeSlot.getBaseDate());
				_timeSlots.add(routeDlvTimeslot);
				data.put(dlvTimeSlot.getBaseDate(), _timeSlots);
			}
			else {
				List<IDeliverySlot> _timeSlots=new ArrayList<IDeliverySlot>();
				_timeSlots.add(routeDlvTimeslot);
				data.put(dlvTimeSlot.getBaseDate(), _timeSlots);
			}
		}
		
		return data;
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

	

	
	private static IServiceTimeScenarioModel getRoutingScenario(Date dlvDate) throws RoutingServiceException {
		return new RoutingInfoServiceProxy().getRoutingScenario(dlvDate);
	}
	
	private static IPackagingModel estimateOrderSize(IOrderModel order, IServiceTimeScenarioModel scenario, IPackagingModel historyInfo) throws RoutingServiceException {
		return new PlantServiceProxy().estimateOrderSize(order, scenario, historyInfo);
	}
	
	private static IPackagingModel getHistoricOrderSize(IOrderModel order) throws RoutingServiceException {
		return new PlantServiceProxy().getHistoricOrderSize(order);
	}
	
	
	private static IOrderModel setDeliveryInfo(IOrderModel order, Date deliveryDate) throws RoutingServiceException {
		
		DeliveryServiceProxy dlvService=new DeliveryServiceProxy();
		order.getDeliveryInfo().setDeliveryDate(deliveryDate);
		IPackagingModel historyPackageInfo = getHistoricOrderSize(order);
		IServiceTimeScenarioModel srvScenario=getRoutingScenario(order.getDeliveryInfo().getDeliveryDate());
		order.getDeliveryInfo().setPackagingInfo(estimateOrderSize(order, srvScenario, historyPackageInfo));
		order.getDeliveryInfo().setServiceTime(dlvService.estimateOrderServiceTime(order,srvScenario));
		return order;
	}

	

	
	
	
}
