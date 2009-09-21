package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.routing.ejb.RoutingGatewayHome;
import com.freshdirect.delivery.routing.ejb.RoutingGatewaySB;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;

public class RoutingUtil {
	
	private static RoutingUtil _instance = null;
	private RoutingGatewayHome home=null;
	
	
	
	
	
	public RoutingGatewayHome getHome() {
		return home;
	}

	private RoutingUtil() {
		
	}
	
	private Context getInitialContext() throws NamingException {
		Hashtable h = new Hashtable();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		System.out.println("FDStoreProperties.getRoutingProviderURL( :"+FDStoreProperties.getRoutingProviderURL());
		h.put(Context.PROVIDER_URL, FDStoreProperties.getRoutingProviderURL());
		//PROP_PROVIDER_URL, 	"t3://app01.stdev01.nyc1.freshdirect.com:7001");
		return new InitialContext(h);
	}
	
	
	
	protected  void lookupRoutingGatewayHome() throws FDResourceException {
		Context ctx = null;
		try {
			if(home==null) {
				ctx = getInitialContext();
				home = (RoutingGatewayHome) ctx.lookup( FDStoreProperties.getRoutingGatewayHome());
			}
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if(ctx != null) {
					ctx.close();
				}
			} catch (NamingException e) {}
		}
	}
	
	public static RoutingUtil getInstance() {
		if (_instance == null) {
			_instance = new RoutingUtil();
		}
		return _instance;
	}
	
	public   void sendDateRangeAndZoneForTimeslots(List<FDTimeslot> timeSlots, ContactAddressModel address) throws FDResourceException {

		try {
				RoutingGatewaySB routingSB = getRoutingGatewayHome().create();
				routingSB.sendDateRangeAndZoneForTimeslots(timeSlots, address);

			} catch (CreateException ce) {
				home=null;
				throw new FDResourceException(ce);
				
			} catch (RemoteException re) {
				home=null;
				throw new FDResourceException(re);
			}
	}
	
	public   void sendTimeslotReservationRequest(DlvReservationModel reservation, ContactAddressModel address, FDTimeslot timeslot) throws FDResourceException {

		try {
				RoutingGatewaySB routingSB = getRoutingGatewayHome().create();
				routingSB.sendReserveTimeslotRequest(reservation,address, timeslot);

			} catch (CreateException ce) {
				home=null;
				throw new FDResourceException(ce);
			} catch (RemoteException re) {
				home=null;
				throw new FDResourceException(re);
			}
	}
	
	public void sendCommitReservationRequest(DlvReservationModel reservation,ContactAddressModel address, String previousOrderId) throws FDResourceException{
		
		try {
			RoutingGatewaySB routingSB = getRoutingGatewayHome().create();
			routingSB.sendCommitReservationRequest(reservation,address, previousOrderId);

		} catch (CreateException ce) {
			home=null;
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			home=null;
			throw new FDResourceException(re);
		}
		
	}

	public void sendReleaseReservationRequest(DlvReservationModel reservation,ContactAddressModel address) throws FDResourceException{
		
		try {
			RoutingGatewaySB routingSB = getRoutingGatewayHome().create();
			routingSB.sendReleaseReservationRequest(reservation,address);

		} catch (CreateException ce) {
			home=null;
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			home=null;
			throw new FDResourceException(re);
		}
		
	}
	public IOrderModel getOrderModel(ContactAddressModel address) {
		
		return getOrderModel(address,"T"+address.getId());
	}
	
	public IOrderModel getOrderModel(ContactAddressModel address, String orderNumber) {
		
		IOrderModel order= new OrderModel();
		order.setDeliveryInfo(getDeliveryModel(address));
		order.setCustomerName(new StringBuffer(100).append(address.getLastName()).append(", ").append(address.getFirstName()).toString());
		order.setCustomerNumber(address.getCustomerId());
		order.setOrderNumber(orderNumber);
		return order;
	}
	
		
	public IDeliverySlot getDeliverySlot(DlvTimeslotModel timeSlot) {
		
		IDeliverySlot deliverySlot=new DeliverySlot();
		IRoutingSchedulerIdentity identity=new RoutingSchedulerIdentity();
		identity.setDeliveryDate(timeSlot.getBaseDate());
		deliverySlot.setSchedulerId(identity);
		deliverySlot.setStartTime(timeSlot.getStartTimeAsDate());
		deliverySlot.setStopTime(timeSlot.getEndTimeAsDate());
		deliverySlot.setWaveCode(getHourAMPM(timeSlot.getCutoffTimeAsDate()));
		return deliverySlot;
	}

	public Map<java.util.Date,java.util.List<IDeliverySlot>>  getDeliverySlot(List<FDTimeslot> dlvTimeSlots ) {
		
		Map<java.util.Date,java.util.List<IDeliverySlot>> data=new HashMap<java.util.Date,java.util.List<IDeliverySlot>>();
		
		for(int i=0;i<dlvTimeSlots.size();i++) {
			IDeliverySlot routeDlvTimeslot=new DeliverySlot();
			FDTimeslot dlvTimeSlot= dlvTimeSlots.get(i);
			
			routeDlvTimeslot.setStartTime(dlvTimeSlot.getBegDateTime());
			routeDlvTimeslot.setStopTime(dlvTimeSlot.getEndDateTime());
			routeDlvTimeslot.setWaveCode(getHourAMPM(dlvTimeSlot.getCutoffDateTime()));
			
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
	
	
	/*public List filter(List dlvTimeSlots, Date _date ) {
		Filter filter=new Filter();
		filter.addCriteria(new DateCriteria(_date));
		return filter.filter(dlvTimeSlots);
	}*/
	
	private ILocationModel getLocation(ContactAddressModel address) {
		
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
	
	private IDeliveryModel getDeliveryModel(ContactAddressModel address) {
		IDeliveryModel dlvInfo=new DeliveryModel();
		dlvInfo.setDeliveryLocation(getLocation(address));
		return dlvInfo;
	}
	
	private String getHourAMPM(Date date) {
		try {
			return  DateUtil.formatTimeAMPM(date);
		} catch (ParseException pe) {
			return null;
		}
	}
	
	private  RoutingGatewayHome getRoutingGatewayHome() throws FDResourceException {
			lookupRoutingGatewayHome();
			return home;
			//(RoutingGatewayHome) //serviceLocator.getRemoteHome(FDStoreProperties.getRoutingGatewayHome(), RoutingGatewayHome.class);
		
	}
	
	/*
	public static class DateCriteria implements Criteria {
		private Date _date=null;
		public DateCriteria(Date _date) {
			this._date=_date;
		}
		public boolean passes(Object o) {
			DlvTimeslotModel ts=(DlvTimeslotModel)o;
			return DateUtil.isSameDay(_date, ts.getBaseDate());
		}
	}
	
	public static class Filter implements java.io.Serializable {

	    private List allCriteria = new ArrayList();
	    
	    public void addCriteria(Criteria criteria){
	    	allCriteria.add(criteria);
	    }
	    
	    public List filter(List list){
            
	    	List out=new ArrayList();
	        if(list != null){
	            Iterator iter = list.iterator();
	            while(iter.hasNext()){
	                Object o = iter.next();
	                
	                if(passesAllCriteria(o))
	                	out.add(0);
	            }
	        }
	        return out;
	    }


	    private boolean passesAllCriteria(Object o){
	        for(int i = 0; i < allCriteria.size(); i ++){
	            Criteria criteria = (Criteria)allCriteria.get(i);
	            if(!criteria.passes(o)){
	                return false;
	            }
	        }
	        return true;
	    }
    }
	public static interface Criteria {
    	boolean passes(Object o);
    }
    */
	
	
	
}
