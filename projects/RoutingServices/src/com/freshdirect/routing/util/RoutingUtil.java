package com.freshdirect.routing.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.RoutingActivityType;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.OrderEstimationResult;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.PackagingModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.model.ZoneModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.service.proxy.PlantServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;

public class RoutingUtil {
	
	private static final Category LOGGER = LoggerFactory.getInstance(RoutingUtil.class);
	
	public static String standardizeStreetAddress(String s1, String s2) {
		String streetAddressResult = null;
		//String oldStreetAddress = address.getStreetAddress1();
		try {
			streetAddressResult = AddressScrubber.standardizeForGeocode(s1);
			//streetAddress = AddressScrubber.standardizeForGeocode(address.getAddress1());
		} catch (RoutingServiceException iae1) {
			try {
				streetAddressResult = AddressScrubber.standardizeForGeocode(s2);
			} catch (RoutingServiceException iae2) {
				iae2.printStackTrace();
			}
		}
		return streetAddressResult;
	}
	
	public static boolean isGeocodeAcceptable(String confidence, String quality) {
		return confidence != null && confidence.trim().length() > 0;// Commented for APPDEV-2132 EnumGeocodeConfidenceType.HIGH.getName().equals(confidence);
	}
	
	public static List getZipCodes(String zipCode) {
		
		if(zipCode != null && zipCode.trim().length() != 0) {
			StringBuffer tmpBufZipCode = new StringBuffer();
			tmpBufZipCode.append(zipCode);
			String tmpZipCode = FDStoreProperties.getAlternateZipcodeForGeocode(zipCode);
	    	if(!StringUtil.isEmpty(tmpZipCode)) {
	    		tmpBufZipCode.append(",").append(tmpZipCode);
	    	}
	    	return Arrays.asList(StringUtil.decodeStrings(tmpBufZipCode.toString()));	    	
		} 
		return null;    	
	}
	
	public static String getQueryParam(List lstZipCode) {
		
		if(lstZipCode != null) {			
	    	if(lstZipCode.size() > 1) {
	    		Iterator iterator = lstZipCode.iterator();
	    		int intCount = 0;
	    		StringBuffer strBuf = new StringBuffer();	    		
	        	while(iterator.hasNext()) {
	        		intCount++;
	        		strBuf.append("'").append(iterator.next()).append("'");
	        		if(intCount != lstZipCode.size()) {
	        			strBuf.append(",");
	        		}
	        	}
	        	return "in ("+strBuf.toString()+")";
	    	} else if (lstZipCode.size() == 1){
	    		return "= '"+lstZipCode.get(0)+"'";
	    	}
		} 
		return null;
    	
	}
	
	public static String getRegion(IAreaModel model) {
			return model.getRegion().getRegionCode();
	}
	
	public static double getDouble(String input) {
		try {
			return Double.parseDouble(input);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}
	
	public static String[] toStringArray(List<String> source) {
		if(source != null) {
			String[] result = new String[source.size()];
			int intCount = 0;
			for(String unit : source) {
				result[intCount++] = unit;
			}
			return result;
		}
		return null;
	}
	
	public static List<List<?>> splitList(List<?> list, int maxListSize) {
        List<List<?>> splittedList = new ArrayList<List<?>>();
        int itemsRemaining = list.size();
        int start = 0;

        while (itemsRemaining != 0) {
        	int end = itemsRemaining >= maxListSize ? (start + maxListSize) : (start + itemsRemaining);

            splittedList.add(list.subList(start, end));

            int sizeOfFinalList = end - start;
            itemsRemaining = itemsRemaining - sizeOfFinalList;
            start = start + sizeOfFinalList;
        }

        return splittedList;
    }
	
	public static String splitValueByUnderscore(String codedValue) {
		String[] dataLst = StringUtils.split(codedValue, "_");
		if(dataLst != null && dataLst.length > 1) {
			return dataLst[1];
		} else {
			return "000";
		}
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
	private static IDeliveryModel getDeliveryModel(ContactAddressModel address, String reservationId) {
		IDeliveryModel dlvInfo=new DeliveryModel();
		dlvInfo.setDeliveryLocation(getLocation(address));
		dlvInfo.setReservationId(reservationId);
		return dlvInfo;
	}
	private static ILocationModel getLocation(ContactAddressModel address) {
		
		IBuildingModel building = new BuildingModel();
		
		building.setStreetAddress1(address.getAddress1());
		building.setStreetAddress2(address.getAddress2());
		
		building.setCity(address.getCity());
		building.setCountry(address.getCountry());
		building.setState(address.getState());
		building.setZipCode(address.getZipCode());
		
		ILocationModel loc= new LocationModel(building);
		loc.setApartmentNumber(address.getApartment());
		return loc;
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

	private static void schedulerCancelOrder(IOrderModel orderModel,DlvReservationModel reservation) throws RoutingServiceException {


		RoutingEngineServiceProxy routingService=new RoutingEngineServiceProxy();
		orderModel.setOrderNumber( reservation.getOrderId());
		routingService.schedulerCancelOrder(orderModel);
	}
	
	private static ILocationModel locateOrder(IOrderModel order) throws RoutingServiceException  {
		GeographyServiceProxy geoSrv = new GeographyServiceProxy();
		
		ILocationModel location = geoSrv.locateOrder(order);
		if(location != null && order != null && order.getDeliveryInfo() != null) {
			order.getDeliveryInfo().setDeliveryLocation(location);
		}
		return location;
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
	
	protected static IServiceTimeScenarioModel getRoutingScenario(Date dlvDate) throws RoutingServiceException {
		return new RoutingInfoServiceProxy().getRoutingScenarioByDate(dlvDate);
	}
	
	public static IServiceTimeScenarioModel getRoutingScenarioEx(Date dlvDate, Date cutoff, Date startTime, Date endTime) throws RoutingServiceException {
		return new RoutingInfoServiceProxy().getRoutingScenarioEx(dlvDate,cutoff,startTime,endTime);
	}
	
	protected static OrderEstimationResult estimateOrderSize(IOrderModel order, IServiceTimeScenarioModel scenario, IPackagingModel historyInfo) throws RoutingServiceException {
		return new PlantServiceProxy().estimateOrderSize(order, scenario, historyInfo);
	}

	protected static IPackagingModel getHistoricOrderSize(IOrderModel order) throws RoutingServiceException {
		return new PlantServiceProxy().getHistoricOrderSize(order);
	}
	
	public static String getOrderNo(ContactAddressModel address) {
		return address.getId()!=null ? new StringBuilder("T").append(address.getId()).toString():new StringBuilder("T").append((int)(Math.random()/0.00001)).toString();
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
	
	order.getDeliveryInfo().setCalculatedServiceTime(dlvService.getServiceTime(order,srvScenario, RoutingActivityType.UPDATE_TIMESLOT));
		
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

	
	public static DateRange getStopETAWindow(Date deliveryDate, Date stopArrivalTime, int windowETAInterval, DateRange routingWindow) {
		routingWindow = new DateRange(RoutingDateUtil.getNormalDate(deliveryDate, routingWindow.getStartDate()), 
				RoutingDateUtil.getNormalDate(deliveryDate, routingWindow.getEndDate()));
		SortedSet<DateRange> _tempETAWindows = getStopETAWindows(windowETAInterval, routingWindow);
		if(_tempETAWindows != null && _tempETAWindows.size() > 0) {
			return getStopETAWindow(stopArrivalTime, _tempETAWindows, routingWindow);
		}
		return null;
	}
	
	/**
	 * Returns a list of date ranges (sorted by start date) representing the
	 * ETA date ranges determined for stop delivery window.
	 * @param timeWindowInterval.
	 * @param routingWindow (DateRange).
	 * @return The date ranges.
	 */
	private static SortedSet<DateRange> getStopETAWindows(int timeWindowInterval, DateRange routingWindow) {
		SortedSet<DateRange> result = new TreeSet<DateRange>(
				new Comparator<DateRange>() {
					@Override
					public int compare(DateRange obj1, DateRange obj2) {
						Date d1 = ((DateRange) obj1).getStartDate();
						Date d2 = ((DateRange) obj2).getStartDate();
						return d1.compareTo(d2);
					}
				});
		Calendar cal = Calendar.getInstance();
		Date _tempStartDate = null;
		if (routingWindow != null) {
			Date _startWindowTime = routingWindow.getStartDate();
			Date _endWindowTime = routingWindow.getEndDate();
			_tempStartDate = _startWindowTime;
			boolean done = false;
			while (!done) {
					cal.setTime(_tempStartDate);
					cal.add(Calendar.MINUTE, timeWindowInterval);
	
					if (cal.getTime().before(_endWindowTime)) {
				
						result.add(new DateRange(_tempStartDate, cal.getTime()));
						
						_tempStartDate = cal.getTime();
					
					} else {
						
						cal.setTime(_endWindowTime);
						
						cal.add(Calendar.MINUTE, -timeWindowInterval);

						if(cal.getTime().after(_startWindowTime))
						{
							result.add(new DateRange(cal.getTime(), _endWindowTime));
						} else {
							result.add(new DateRange(_startWindowTime, _endWindowTime));
						}
						
						_tempStartDate = _endWindowTime;
					}
					if(_tempStartDate.equals(_endWindowTime))
					{
						done = true;
					}
			}
		}
		return result;
	}
	
	private static DateRange getStopETAWindow(Date stopArrivalTime, SortedSet<DateRange> etaTimeWindows, DateRange routingDlvWindow) {
		
		if(etaTimeWindows != null) {		
			/*
			 * Stop Arrival Time is after the end of the Routing Delivery Time Window,
			 * then the order will be assigned to the last ETA Time Window defined for that Routing Delivery Time Window
			*/
			if(etaTimeWindows != null && etaTimeWindows .size() > 0 
					&& routingDlvWindow != null && stopArrivalTime != null 
						&& stopArrivalTime.after(routingDlvWindow.getEndDate())) {
				return new DateRange(etaTimeWindows.last().getStartDate(), etaTimeWindows.last().getEndDate());			
			}
			
			/*
			 * Stop Arrival Time is before the beginning of the Routing Delivery Time Window,
			 * then the order will be assigned to the first ETA Time Window defined for that Routing Delivery Time Window 
			*/
			if(etaTimeWindows != null && etaTimeWindows .size() > 0 
					&& routingDlvWindow != null && stopArrivalTime != null 
						&& stopArrivalTime.before(routingDlvWindow.getStartDate())) {
				return new DateRange(etaTimeWindows.first().getStartDate(), etaTimeWindows.first().getEndDate());	
			}
			
			/* 
			 * If a Stop Arrival Time is within two or more ETA Time Windows, the order will be assigned to the ETA Time Window with the earliest start time
			 */
			SortedSet<DateRange> tempRange = new TreeSet<DateRange>(new Comparator<DateRange>() {
				@Override
				public int compare(DateRange obj1, DateRange obj2) {
					Date d1 = ((DateRange) obj1).getStartDate();
					Date d2 = ((DateRange) obj2).getStartDate();
					return d1.compareTo(d2);
				}
			});
			for (Iterator<DateRange> i = etaTimeWindows.iterator(); i.hasNext();) {
				DateRange _range = i.next();
				if(_range.containsEx(stopArrivalTime)) {
					tempRange.add(_range);
				}
			}
			if(tempRange.size() > 0) {
				return new DateRange(tempRange.first().getStartDate(), tempRange.first().getEndDate());
			}
		}
		return null;
	}
	
	public static IOrderModel schedulerRetrieveOrder(IOrderModel orderModel) throws RoutingServiceException {

		RoutingEngineServiceProxy routingService=new RoutingEngineServiceProxy();        
        return routingService.schedulerRetrieveOrder(orderModel);        
	}

	public static IOrderModel getOrderModel(IHandOffBatchStop stop, Map<String, IAreaModel> areaLookup) {
		
		IOrderModel order= new OrderModel();
		order.setOrderNumber(stop.getOrderNumber());
		IDeliveryModel dModel = new DeliveryModel();
		dModel.setDeliveryDate(stop.getDeliveryInfo().getDeliveryDate());
		dModel.setReservationId(stop.getDeliveryInfo().getReservationId());
		IZoneModel zModel = new ZoneModel();
		IAreaModel aModel = areaLookup.get(stop.getDeliveryInfo().getDeliveryZone().getZoneNumber());
		zModel.setArea(aModel);
		dModel.setDeliveryZone(zModel);
    	order.setDeliveryInfo(dModel);
    	return order;
    	
	}
}


