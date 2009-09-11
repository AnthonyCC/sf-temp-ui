package com.freshdirect.dataloader.reservation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.dataloader.payment.ejb.SaleCronHome;
import com.freshdirect.dataloader.payment.ejb.SaleCronSB;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.routing.ejb.RoutingActivityType;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSessionBean.ReservationInfo;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class UnassignedReservationCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(UnassignedReservationCronRunner.class);

	private static final int DEFAULT_DAYS=7;
	public static void main(String[] args) {
		
		Context ctx = null;
		try {
			ctx = getInitialContext();
			
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
					if(_unassignedReservations!=null && !_unassignedReservations.isEmpty())
						unassignedReservations.addAll(_unassignedReservations);
				} else {
					for(int i=0;i<DEFAULT_DAYS;i++) {
						List<DlvReservationModel> _unassignedReservations=dlvManager.getUnassignedReservations(startDate.getTime());
						if(_unassignedReservations!=null && !_unassignedReservations.isEmpty())
							unassignedReservations.addAll(_unassignedReservations);
						startDate.add(Calendar.DATE, 1);
					}
				}
			} catch (Exception e) {
				LOGGER.info(new StringBuilder("UnassignedReservationCronRunner failed with exception : ").append(e.toString()).toString());
			}
			if(unassignedReservations==null || unassignedReservations.isEmpty())
				return;
			for (DlvReservationModel reservation : unassignedReservations) {
				reassignReservation(dlvManager,custManager,reservation);
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
		
    private static void reassignReservation(DlvManagerSB dlvManager,FDCustomerManagerSB sb,DlvReservationModel reservation) {
    	try {
    		
			 FDIdentity identity=getIdentity(reservation.getCustomerId());
			 ContactAddressModel address= sb.getAddress(identity, reservation.getAddressId());
			 RoutingActivityType unassignedAction=reservation.getUnassignedActivityType();
			 if(RoutingActivityType.CANCEL_TIMESLOT.equals(unassignedAction)) {
				 dlvManager.releaseReservationEx(reservation, address);
			 } else if(RoutingActivityType.RESERVE_TIMESLOT.equals(unassignedAction)
					 || RoutingActivityType.CONFIRM_TIMESLOT.equals(unassignedAction)) {
				 FDReservation _reservation = new FDReservation( reservation.getPK(),
						 										 new FDTimeslot(dlvManager.getTimeslotById(reservation.getTimeslotId())),
																 reservation.getExpirationDateTime(),
																 reservation.getReservationType(),
																 reservation.getCustomerId(),
																 address.getPK().getId(),
																 reservation.isChefsTable(),
																 reservation.isUnassigned(),
																 reservation.getOrderId());
				dlvManager.reserveTimeslotEx(_reservation, address);
				if(RoutingActivityType.CONFIRM_TIMESLOT.equals(unassignedAction)) {
					 dlvManager.commitReservationEx(reservation, address, reservation.getOrderId());
				}
			 }
			 
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info(new StringBuilder("UnassignedReservationCronRunner: ").append(" failed to reassign reservation for id ").append(reservation.getId()).toString(),e);
			}
    }
	static public Context getInitialContext() throws NamingException {
		Hashtable h = new Hashtable();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		System.out.println("ErpServicesProperties.getProviderURL() :"+ErpServicesProperties.getProviderURL());
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	private static FDIdentity getIdentity(String erpCustomerId) {
		return new FDIdentity(erpCustomerId);
	}

}