/**
 * 
 */
package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ecomm.gateway.StandingOrder3CronService;
import com.freshdirect.fdstore.ecomm.gateway.StandingOrder3CronServiceI;
import com.freshdirect.framework.util.log.LoggerFactory;

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
			LOGGER.info("Fetching non activated SO to be cleaned up");
			
				LOGGER.info("Fetching non activated SO to be cleaned up, using SF 2.0 Service..");
				StandingOrder3CronServiceI so3CronService = StandingOrder3CronService.getInstance();
				soIdList = so3CronService.queryForDeactivatingTimeslotEligible();
				if(null != soIdList && !soIdList.isEmpty()){
					LOGGER.info("Non Activated SOs :"+soIdList);
					so3CronService.removeSOfromLogistics(soIdList);
					so3CronService.removeTimeSlotInfoFromSO(soIdList);
					
				}
			
		} catch (FDResourceException e) {
			LOGGER.error("FDResourceException in StandingOrder3CancelReservationCron: "+e);
		} catch (RemoteException e) {
			LOGGER.error("RemoteException in StandingOrder3CancelReservationCron: "+e);
		}

	}

}
