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

	public static void main(String[] args) {
		
		Context ctx = null;
		try {
			ctx = getInitialContext();
			
			List<DlvReservationModel> unassignedReservations=null;
			DlvManagerSB dlvManager =null;
			try {
				DlvManagerHome dlh =(DlvManagerHome) ctx.lookup("freshdirect.delivery.DeliveryManager");
				dlvManager = dlh.create();
				unassignedReservations=dlvManager.getUnassignedReservations();
			} catch (Exception e) {
				LOGGER.info(new StringBuilder("UnassignedReservationCronRunner failed with exception : ").append(e.toString()).toString());
			}
			if(unassignedReservations==null || unassignedReservations.isEmpty())
				return;
			for (DlvReservationModel reservation : unassignedReservations) {
				reassignReservation(dlvManager,reservation);
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
		
    private static void reassignReservation(DlvManagerSB dlvManager,DlvReservationModel reservation) {
    	try {
    		FDTimeslot f=null;
			 FDIdentity identity=getIdentity(reservation.getCustomerId());
			 ContactAddressModel address= FDCustomerManager.getAddress(identity, reservation.getAddressId());
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