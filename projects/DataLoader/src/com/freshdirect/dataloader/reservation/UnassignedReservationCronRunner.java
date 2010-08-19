package com.freshdirect.dataloader.reservation;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.routing.ejb.RoutingActivityType;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
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
				email(Calendar.getInstance().getTime(),e.toString());
				return ;
			}
			
			try {
				DlvManagerHome dlh =(DlvManagerHome) ctx.lookup("freshdirect.delivery.DeliveryManager");
				dlvManager = dlh.create();
				FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");

				custManager = home.create();
				cron.processReRouteReservation(dlvManager, custManager, dlvManager.getReRouteReservations());
				
				List<IRoutingNotificationModel> notifications = dlvManager.retrieveNotifications();
				List<IRoutingNotificationModel> cancelledNotifications = new ArrayList<IRoutingNotificationModel>();
				List<IRoutingNotificationModel> unUsedNotifications = new ArrayList<IRoutingNotificationModel>();
				
				if(notifications != null) {
					for (IRoutingNotificationModel notification : notifications) {
						if(notification.getNotificationType() != null 
								&& notification.getNotificationType().equals(EnumRoutingNotification.SchedulerOrdersCanceledNotification)) {
							cancelledNotifications.add(notification);
						} else {
							unUsedNotifications.add(notification);
						}
					}
				}
				System.out.println("Total no of notifications processed :"+cancelledNotifications.size()+ " , Unused:"+unUsedNotifications.size());
				if(cancelledNotifications.size() > 0 || unUsedNotifications.size() > 0) {
					dlvManager.processCancelNotifications(cancelledNotifications, unUsedNotifications);
				}
				
				if(hasArg) {
					
					List<DlvReservationModel> _unassignedReservations = dlvManager.getUnassignedReservations(startDate.getTime());
					
					if(_unassignedReservations!=null && !_unassignedReservations.isEmpty()) {
						
						unassignedReservations.addAll(_unassignedReservations);
					}
				} else {
					
					for(int i=0; i<DEFAULT_DAYS; i++) {
						
						List<DlvReservationModel> _unassignedReservations = dlvManager.getUnassignedReservations(startDate.getTime());
						System.out.println("Total unassigned reservations for :"+startDate.getTime()+"->"+(_unassignedReservations!=null ? _unassignedReservations.size() : 0));
						if(_unassignedReservations!=null && !_unassignedReservations.isEmpty()) {
							unassignedReservations.addAll(_unassignedReservations);
						}
						startDate.add(Calendar.DATE, 1);
					}
				}
				
				if(unassignedReservations != null && unassignedReservations.size() > 0) {			
					
					for (DlvReservationModel reservation : unassignedReservations) {
						cron.processReservation(dlvManager,custManager,reservation);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info(new StringBuilder("UnassignedReservationCronRunner failed with exception : ").append(e.toString()).toString());
				email(Calendar.getInstance().getTime(),e.toString());
			}
			
			LOGGER.info("UnassignedReservationCronRunner finished");
		}catch (NamingException e) {
				e.printStackTrace();
				LOGGER.info(new StringBuilder("UnassignedReservationCronRunner failed with exception : ").append(e.toString()).toString());
				email(Calendar.getInstance().getTime(),e.toString());
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
	
	private void processReRouteReservation(DlvManagerSB dlvManager, FDCustomerManagerSB sb
												, List<DlvReservationModel> reRouteReservations) {
		System.out.println("Total no of reroute reservations processed :"+ (reRouteReservations != null ? reRouteReservations.size() : 0));
		if(reRouteReservations != null && reRouteReservations.size() > 0) {			
			
			for (DlvReservationModel reservation : reRouteReservations) {
				FDIdentity identity = getIdentity(reservation.getCustomerId());
				ContactAddressModel address;
				try {
					address = sb.getAddress(identity, reservation.getAddressId());
					dlvManager.cancelRoutingReservation(reservation, address);
				} catch (FDResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			try {
				dlvManager.clearReRouteReservations();
			} catch (DlvResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
    @Override
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
			email(Calendar.getInstance().getTime(),e.toString());
		}
    }
    
    private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="UnassignedReservationCron:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");			
			
			if(exceptionMsg != null) {
				buff.append("b").append(exceptionMsg).append("/b");
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");
			
		}catch (MessagingException e) {
			LOGGER.warn("Error Sending UnassignedReservationCron report email: ", e);
		}
		
	}
	

}