/**
 * 
 */
package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.ejb.SapException;

/**
 * @author ksriram
 *
 */
public class StandingOrder3CancelReservationCron {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(StandingOrder3CancelReservationCron.class);
	private static ThreadLocal<StandingOrder3CronHome> so3CronHome = new ThreadLocal<StandingOrder3CronHome>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			List<String> soIdList = null;
			if (args.length >= 1) {
				for (String arg : args) {
					try { 
						if (arg.startsWith("orders=")) {
							String orders=arg.substring("orders=".length());
							if(null !=orders && !orders.trim().equalsIgnoreCase("")){
								String[] order = orders.split(",");								
								Set<String> soSet = new HashSet<String>(Arrays.asList(order));
								soIdList = new ArrayList<String>(soSet);
								
							}
						} 
					} catch (Exception e) {
						System.err.println("Usage: java com.freshdirect.fdstore.standingorders.service.StandingOrder3CancelReservationCron [orders=(,) separated}]");
						System.exit(-1);
					}
				}
			}
			lookupSO3CronHome();
			StandingOrder3CronSB sb = so3CronHome.get().create();
			LOGGER.info("Fetching non activated SO to be cleaned up");
			if(null == soIdList){
				soIdList = sb.queryForDeactivatingTimeslotEligible();
			}
			if(null != soIdList && !soIdList.isEmpty()){
				LOGGER.info("Non Activated SOs :"+soIdList);
				sb.removeSOfromLogistics(soIdList);
				sb.removeTimeSlotInfoFromSO(soIdList);
				
			}
			
		} catch (FDResourceException e) {
			invalidateSOSHome();
			LOGGER.error("FDResourceException in StandingOrder3CancelReservationCron: "+e);
		} catch (RemoteException e) {
			invalidateSOSHome();
			LOGGER.error("RemoteException in StandingOrder3CancelReservationCron: "+e);
		} catch (CreateException e) {
			invalidateSOSHome();
			LOGGER.error("CreateException in StandingOrder3CancelReservationCron: "+e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.error("SQLException in StandingOrder3CancelReservationCron: "+e);

		} catch (ErpTransactionException e) {
			// TODO Auto-generated catch block
			LOGGER.error("ErpTransactionException in StandingOrder3CancelReservationCron: "+e);
		} catch (SapException e) {
			// TODO Auto-generated catch block
			LOGGER.error("SapException in StandingOrder3CancelReservationCron: "+e);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			LOGGER.error("FinderException in StandingOrder3CancelReservationCron: "+e);
		}

	}

	private static void lookupSO3CronHome() throws FDResourceException {
		if ( so3CronHome.get() != null ) {
			return;
		}
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			so3CronHome.set( (StandingOrder3CronHome)ctx.lookup( "freshdirect.fdstore.SO3Cron" ) );
		} catch (NamingException ne) {
			throw new FDResourceException(ne);			
		} finally {
			try {
				if ( ctx != null ) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("cannot close Context while trying to cleanup", ne);				
			}
		}
	}
	
	private static void invalidateSOSHome() {
		so3CronHome.set( null );
	}
}
