package com.freshdirect.delivery.ejb;

/**
 *
 * @author  tbalumuri
 * @version 
 */
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Category;

import com.freshdirect.analytics.EventType;
import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.SectorVO;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.GeographyRestriction;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.RoutingAnalyzerCommand;
import com.freshdirect.fdstore.RoutingAnalyzerContext;
import com.freshdirect.fdstore.RoutingUtil;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.PackagingModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class CapacitySnapshotSessionBean extends SessionBeanSupport {
	
	private static final long serialVersionUID = 7626801598033245149L;
	
	private static final Category LOGGER = LoggerFactory.getInstance(CapacitySnapshotSessionBean.class);
	
	
	public static String getOrderNo(ILocationModel location) {
		return location.getBuilding().getBuildingId()!=null ? new StringBuilder("T").append(location.getBuilding().getBuildingId()).toString():new StringBuilder("T").append((int)(Math.random()/0.00001)).toString();
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
	
public static IOrderModel getOrderModel(ILocationModel location, String orderNumber) {
		
	IDeliveryModel dlvInfo = new DeliveryModel();
	dlvInfo.setDeliveryLocation(location);
	dlvInfo.setReservationId(orderNumber);		
	
	IOrderModel order= new OrderModel();
	order.setDeliveryInfo(dlvInfo);
	IPackagingModel pModel = new PackagingModel();
	order.getDeliveryInfo().setPackagingDetail(pModel);			
			
	order.setCustomerName(new StringBuffer(100).append("UPS SNAPSHOT:").append(location.getBuilding().getBuildingId()).toString());
	order.setCustomerNumber("UPS SNAPSHOT:"+location.getBuilding().getBuildingId());
	order.setOrderNumber(orderNumber);
	return order;
	}
	
	private ErpAddressModel performCosResidentialMerge(ErpAddressModel address)
			throws FDResourceException {
		ErpAddressModel timeslotAddress=address;
		if(address!=null){
			if(EnumServiceType.CORPORATE.equals(address.getServiceType())){
				try{
					DlvServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().checkAddress(address);
			 		EnumDeliveryStatus status = serviceResult.getServiceStatus(address.getServiceType());
			 		if(EnumDeliveryStatus.COS_ENABLED.equals(status)){	
			 			//Clone the address model object
			 			timeslotAddress=cloneAddress(address);
			 			timeslotAddress.setServiceType(EnumServiceType.HOME);
			 		}
			 		
				}catch (FDInvalidAddressException iae) {
					LOGGER
					.warn("GEOCODE FAILED FOR ADDRESS setRegularDeliveryAddress  FDInvalidAddressException :"
							+ address + "EXCEPTION :" + iae);
				}
			}
		}
		return timeslotAddress;
	}
	
	private ErpAddressModel cloneAddress(ErpAddressModel address) {
		ErpAddressModel model=new ErpAddressModel(address);
		return model;
	}

    public void capture(Date startDate, Date endDate) throws RemoteException{
    
    	try {
    		
    		GeographyServiceProxy geoProxy = new GeographyServiceProxy();
    		List<ILocationModel> locations = geoProxy.getLocationsForSnapshot();
			
			DlvManagerSB dlvSB = FDDeliveryManager.getInstance().getDlvManagerHome().create();
			IOrderModel order = null;
			TimeslotEventModel event = new TimeslotEventModel(EnumTransactionSource.SYSTEM.getCode(),false,0.00, false, false, null);
			int locationsProcessedCnt = 0;
				
			for(ILocationModel location : locations) {
				
				if(++locationsProcessedCnt == 5) 
				{
					Thread.sleep(5000);
					locationsProcessedCnt =0;
				}
				
				ContactAddressModel address = getAddressModel(location);
				
				ErpAddressModel erpAddress = new ErpAddressModel(address);
				
				ErpAddressModel address1 = performCosResidentialMerge(erpAddress);
				
				
				List<DlvTimeslotModel> dlvtimeslots = dlvSB.getTimeslotForDateRangeAndZone(startDate, endDate, address1);
				
				if(dlvtimeslots!=null && dlvtimeslots.size()>0)
				{
					event.setZoneCtActive(FDDeliveryManager.getInstance().findZoneById(dlvtimeslots.get(0).getZoneId()).isCtActive());
					
					List<FDTimeslot> fdtimeslots = new ArrayList<FDTimeslot>();
					
					for ( DlvTimeslotModel timeslot : dlvtimeslots ) {
						fdtimeslots.add(new FDTimeslot(timeslot));
					}
					List<GeographyRestriction> geoRestrictions = FDDeliveryManager.getInstance().getGeographicDlvRestrictions(address1);
					DateRange geoRestrictionRange = getStandardRange();
					DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
					
					fdtimeslots = applyRestrictions(fdtimeslots,geoRestrictions,geoRestrictionRange,restrictions);
					
					if(RoutingUtil.hasAnyDynamicEnabled(fdtimeslots)) {
						try {
							long startTime = System.currentTimeMillis();
							fdtimeslots = getTimeslotForDateRangeAndZone(fdtimeslots, location);
							long endTime = System.currentTimeMillis();	
							order = getOrderModel(location, getOrderNo(location));
							dlvSB.buildEvent(fdtimeslots, event, null, order, address1, EventType.GET_TIMESLOT, (int) (endTime-startTime));
							SectorVO sectorInfo = FDDeliveryManager.getInstance().getSectorInfo(address1);
							if(event!=null && event.getId()!=null && sectorInfo != null){
								event.setSector(sectorInfo.getName());
							}
							if(event!=null && event.getId()!=null)
								dlvSB.logTimeslots(event);
						
							
							
						} catch (Exception e) {
								event = dlvSB.buildEvent(null, event, null, null,address1,(event!=null)?event.getEventType():EventType.GET_TIMESLOT, 0);
								if(event!=null && event.getId()!=null)
									dlvSB.logTimeslots(event);
								e.printStackTrace();
								LOGGER.debug("Exception in getTimeslotForDateRangeAndZoneEx():"+e.toString());
								
						}
					}
				}
			}					
			
    	} 
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
    	
    	
	}  
   
    private DateRange getStandardRange() {
		Calendar begCal = Calendar.getInstance();
		begCal.add(Calendar.DATE, 1);
		begCal = DateUtil.truncate(begCal);

		Calendar endCal = Calendar.getInstance();
		endCal.add(Calendar.DATE, 7);
		endCal = DateUtil.truncate(endCal);

		return new DateRange(begCal.getTime(), endCal.getTime());
	}

    private static List<FDTimeslot> applyRestrictions(List<FDTimeslot> fdtimeslots, List<GeographyRestriction> geoRestrictions, DateRange geoRestrictionRange, DlvRestrictionsList restrictions )
    {
    	Date start = geoRestrictionRange.getStartDate();
    	Date end = geoRestrictionRange.getEndDate();
    	Calendar cal = Calendar.getInstance();
    	
    	List<Date> holidays = new ArrayList<Date>();
    	while(start.before(end))
    	{
    		if(restrictions.isRestricted( EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.CLOSED, start))
    			holidays.add(start);
    		cal.setTime(start);
    		cal.add(Calendar.DATE, 1);
    		start = cal.getTime();
    	}
    	for(FDTimeslot timeslot:fdtimeslots)
    	{
    		timeslot.setStoreFrontAvailable("A");
    		DlvTimeslotModel ts = timeslot.getDlvTimeslot();
    		boolean geoRestricted = GeographyRestriction.isTimeSlotGeoRestricted(geoRestrictions,
					timeslot, new ArrayList<String>(), geoRestrictionRange,null);
    		timeslot.setGeoRestricted(geoRestricted);
			if(ts.getCapacity() <= 0 || geoRestricted)
				timeslot.setStoreFrontAvailable("H");
			
			if(!"H".equals(timeslot.getStoreFrontAvailable()) && !timeslot.hasAvailCTCapacity() 
							&& ( (timeslot.getDlvTimeslot() != null && timeslot.getDlvTimeslot().getRoutingSlot() != null
								&& (timeslot.getDlvTimeslot().getRoutingSlot().isManuallyClosed()
									|| !timeslot.getDlvTimeslot().getRoutingSlot().isDynamicActive()))))
				timeslot.setStoreFrontAvailable("S");
			
			if(holidays.contains(timeslot.getBaseDate()))
			{
				timeslot.setHolidayRestricted(true);
				timeslot.setStoreFrontAvailable("R");
			}
    	}
		return fdtimeslots;
			
    }
    
    private static ContactAddressModel getAddressModel(ILocationModel location)
    {
    	ContactAddressModel address = new ContactAddressModel();
    	address.setId(location.getBuilding().getBuildingId());
		address.setAddress1(location.getBuilding().getStreetAddress1());
		address.setCity(location.getBuilding().getCity());
		address.setState(location.getBuilding().getState());
		address.setZipCode(location.getBuilding().getZipCode());
		address.setCustomerId("UPS SNAPSHOT");
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setLatitude(Double.parseDouble(location.getBuilding().getGeographicLocation().getLatitude()));
		addressInfo.setLongitude(Double.parseDouble(location.getBuilding().getGeographicLocation().getLongitude()));
		address.setServiceType(EnumServiceType.getEnum(location.getServiceType()));
		address.setAddressInfo(addressInfo);
		address.setApartment(location.getApartmentNumber());
		return address;
    }

	public static List<FDTimeslot> getTimeslotForDateRangeAndZone(List<FDTimeslot> _timeSlots, ILocationModel location) throws RoutingServiceException {
		
		if(_timeSlots==null || _timeSlots.isEmpty() || location==null)
			return _timeSlots;
		
		DeliveryServiceProxy dlvService = new DeliveryServiceProxy();
		RoutingInfoServiceProxy routingInfoproxy = new RoutingInfoServiceProxy();
		
		IOrderModel order = getOrderModel(location, getOrderNo(location));				
		
		RoutingAnalyzerContext context = new RoutingAnalyzerContext();
		context.setDlvTimeSlots(_timeSlots);
		context.setServiceTimeTypes(routingInfoproxy.getRoutingServiceTimeTypes());
		
		context.setHistoryPackageInfo(null);
				
		Map<java.util.Date, RoutingAnalyzerCommand> analyzerCommands = getAnalyzerCommand(_timeSlots, order, context);
		order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(_timeSlots.get(0).getZoneCode()));
		
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
																									,IOrderModel order
																									, RoutingAnalyzerContext context) {

		Map<java.util.Date, RoutingAnalyzerCommand> data = new HashMap<java.util.Date, RoutingAnalyzerCommand>();				
		RoutingAnalyzerCommand tmpCommand = null;
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
					
					tmpCommand = new RoutingAnalyzerCommand();
					tmpCommand.setOrder(order);
					tmpCommand.setRoutingTimeSlots(_timeSlots);
					tmpCommand.setContext(context);
					tmpCommand.setDeliveryDate(dlvTimeSlot.getBaseDate());
					_timeSlots.add(routeDlvTimeslot);
					data.put(dlvTimeSlot.getBaseDate(), tmpCommand);
				}
			}
		}

		return data;
	}
	
}
