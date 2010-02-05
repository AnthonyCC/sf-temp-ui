package com.freshdirect.dataloader.reservation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.routing.ejb.RoutingActivityType;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumRoutingNotification;
import com.freshdirect.routing.model.IRoutingNotificationModel;

public class UnassignedReservationCronRunner extends BaseReservationCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(UnassignedReservationCronRunner.class);

	private static final int DEFAULT_DAYS=7;
	
	public static void main(String[] args) {
		
		if(!FDStoreProperties.isDynamicRoutingEnabled()) {
			return;
		}
		UnassignedReservationCronRunner cron=new UnassignedReservationCronRunner();
		Context ctx = null;
		try {
			ctx = cron.getInitialContext();
			
			List<DlvReservationModel> unassignedReservations=new ArrayList<DlvReservationModel>();
			DlvManagerSB dlvManager =null;
			FDCustomerManagerSB custManager=null;
			Calendar startDate=null;//Calendar.getInstance().getTime();
			
			
			boolean hasArg=false;
			try {
				 if (args.length >=1) {
					startDate=DateUtil.truncate(DateUtil.toCalendar(DateUtil.parse(args[0])));
					Calendar now=Calendar.getInstance();
			        now=DateUtil.truncate(now);
					now.add(Calendar.DATE, 1);
					if(startDate.before(now)) {
						throw new Exception("Invalid date argument. Accepted Format is [yyyy-MM-dd]" );
					}
					hasArg=true;
				} else {
					startDate=DateUtil.truncate(Calendar.getInstance());
					startDate.add(Calendar.DATE, 1);
				}
			} catch (Exception e) {
			
				e.printStackTrace();
				LOGGER.info(new StringBuilder("UnassignedReservationCronRunner failed with exception : ").append(e.toString()).toString());
				LOGGER.error("Invalid date argument. Accepted Format is [yyyy-MM-dd]");
				LOGGER.error(e);
				return ;
			}
			
			try {
				DlvManagerHome dlh =(DlvManagerHome) ctx.lookup("freshdirect.delivery.DeliveryManager");
				dlvManager = dlh.create();
				FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");

				custManager = home.create();
				
				if(hasArg) {
					
					List<DlvReservationModel> _unassignedReservations=dlvManager.getUnassignedReservations(startDate.getTime());
					
					if(_unassignedReservations!=null && !_unassignedReservations.isEmpty()) {
						
						unassignedReservations.addAll(_unassignedReservations);
					}
				} else {
					
					for(int i=0; i<DEFAULT_DAYS; i++) {
						
						List<DlvReservationModel> _unassignedReservations=dlvManager.getUnassignedReservations(startDate.getTime());
						System.out.println("Total unassigned reservations for :"+startDate.getTime()+"->"+(_unassignedReservations!=null ? _unassignedReservations.size() : 0));
						if(_unassignedReservations!=null && !_unassignedReservations.isEmpty())
							unassignedReservations.addAll(_unassignedReservations);
						startDate.add(Calendar.DATE, 1);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info(new StringBuilder("UnassignedReservationCronRunner failed with exception : ").append(e.toString()).toString());
			}
			if(unassignedReservations==null || unassignedReservations.isEmpty())
				return;
			
			
			for (DlvReservationModel reservation : unassignedReservations) {
				cron.processReservation(dlvManager,custManager,reservation);
			}
			
			try {
				List<IRoutingNotificationModel> notifications = dlvManager.retrieveNotifications();
				List<IRoutingNotificationModel> cancelledNotifications = new ArrayList<IRoutingNotificationModel>();
				
				for (IRoutingNotificationModel notification : notifications) {
					if(notification.getNotificationType().equals(EnumRoutingNotification.SchedulerOrdersCanceledNotification)) {
						cancelledNotifications.add(notification);
					}
				}
				System.out.println("Total no of notifications processed :"+cancelledNotifications.size());
				if(cancelledNotifications.size() > 0) {
					dlvManager.processCancelNotifications(cancelledNotifications);
				}
			} catch (Exception e) {
				LOGGER.info(new StringBuilder("UnassignedReservationCronRunner failed with notification exception : ").append(e.toString()).toString());
			}	
			LOGGER.info("UnassignedReservationCronRunner finished");
		}catch (NamingException e) {
				e.printStackTrace();
				LOGGER.info(new StringBuilder("UnassignedReservationCronRunner failed with exception : ").append(e.toString()).toString());
				LOGGER.error(e);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException e) {
				LOGGER.warn("Could not close CTX due to following NamingException", e);
			}
		}
	}
	
    public void processReservation(DlvManagerSB dlvManager,FDCustomerManagerSB sb,DlvReservationModel reservation) {
    	try {
    		
    		FDIdentity identity = getIdentity(reservation.getCustomerId());
    		ContactAddressModel address = sb.getAddress(identity, reservation.getAddressId());
    		
    		if(address == null) {//Depot

    			String locationId = reservation.getAddressId();
    			DlvDepotModel depot = FDDepotManager.getInstance().getDepotByLocationId(locationId);
    			if(depot != null) {
	    			DlvLocationModel location = depot.getLocation(locationId);
	    			address=new ErpDepotAddressModel(location.getAddress());  
    			}
    		}

    		if(address != null) {
	    		RoutingActivityType unassignedAction = reservation.getUnassignedActivityType();
	    		FDTimeslot _timeslot = new FDTimeslot(dlvManager.getTimeslotById(reservation.getTimeslotId()));
	    		if(unassignedAction != null) {
		    		if(RoutingActivityType.RESERVE_TIMESLOT.equals(unassignedAction)) {
		    			if(_timeslot != null) {
		    				dlvManager.reserveTimeslotEx(reservation, address, _timeslot);
		    			}
		    		} else {
		
		    			dlvManager.commitReservationEx(reservation, address);  
		    			if(reservation.getUpdateStatus() != null) {
		    				dlvManager.updateReservationEx(reservation, address, _timeslot);
		    			}
					}
	    		} else if(reservation.getUpdateStatus() != null) {
	    			dlvManager.updateReservationEx(reservation, address, _timeslot);
	    		}
    		} else {
    			LOGGER.info(new StringBuilder("UnassignedReservationCronRunner: ").append(" failed to fetch address reservation for id ").append(reservation.getId()).toString());
    		}
    		
			 
			 
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(new StringBuilder("UnassignedReservationCronRunner: ").append(" failed to reassign reservation for id ").append(reservation.getId()).toString(),e);
		}
    }
	

}