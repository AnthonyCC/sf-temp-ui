package com.freshdirect.dataloader.reservation;

import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CancelReservationCronRunner extends BaseReservationCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(CancelReservationCronRunner.class);

	public static void main(String[] args) {
		
		CancelReservationCronRunner cron=new CancelReservationCronRunner();
		Context ctx = null;
		try {
			ctx = cron.getInitialContext();
			
			DlvManagerSB dlvManager =null;
			FDCustomerManagerSB custManager=null;
			List<DlvReservationModel> expiredReservations=null;
			try {
				DlvManagerHome dlh =(DlvManagerHome) ctx.lookup("freshdirect.delivery.DeliveryManager");
				dlvManager = dlh.create();
				FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");

				custManager = home.create();
				expiredReservations=dlvManager.getExpiredReservations();
				System.out.println("Total expired reservations  ->"+(expiredReservations!=null ? expiredReservations.size() : 0));
				dlvManager.expireReservations();
				
			} catch (Exception e) {
				LOGGER.info(new StringBuilder("CancelReservationCronRunner failed with exception : ").append(e.toString()).toString());
				return;
			}
			if(expiredReservations==null || expiredReservations.isEmpty() || !FDStoreProperties.isDynamicRoutingEnabled())
				return;
			
			
			for (DlvReservationModel reservation : expiredReservations) {
				cron.processReservation(dlvManager,custManager,reservation);
			 }
			LOGGER.info("CancelReservationCronRunner finished");
		}catch (NamingException e) {
				e.printStackTrace();
				LOGGER.info(new StringBuilder("CancelReservationCronRunner failed with exception : ").append(e.toString()).toString());
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
		
    
    	
	
	@Override
    protected void processReservation(DlvManagerSB dlvManager,
			FDCustomerManagerSB sb, DlvReservationModel reservation) {
		try {
    		
			 FDIdentity identity=getIdentity(reservation.getCustomerId());
			 ContactAddressModel address= sb.getAddress(identity, reservation.getAddressId());
			 if(reservation.isInUPS()) {
				 dlvManager.releaseReservationEx(reservation, address);
			 }
			 
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info(new StringBuilder("CancelReservationCronRunner: ").append(" failed to reassign reservation for id ").append(reservation.getId()).toString(),e);
			}
	}

}
