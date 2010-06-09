package com.freshdirect.dataloader.reservation;

/**
 * 
 * @author knadeem
 */
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSessionBean.ReservationInfo;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ReservationCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(ReservationCronRunner.class);
		
	public static void main(String[] args) {
		Context ctx = null;
		try {
			LOGGER.info("ReservationCron started");
			ctx = getInitialContext();
			FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");

			FDCustomerManagerSB sb = home.create();
		
			List rsvInfo = new ArrayList();
			if (args.length >= 1) {
				int day_of_week = Integer.parseInt(args[0]);
				rsvInfo = sb.getRecurringReservationList(day_of_week);
			}else{		
				rsvInfo = sb.getRecurringReservationList();
			}
			
			DlvManagerHome dlh =(DlvManagerHome) ctx.lookup("freshdirect.delivery.DeliveryManager");
			DlvManagerSB dsb = dlh.create();
			
			for (Iterator i = rsvInfo.iterator(); i.hasNext();) {
				ReservationInfo info = (ReservationInfo) i.next();
				FDIdentity Identity=new FDIdentity(info.getCustomerId(), info.getFdCustomerId());
				FDUserI user=sb.recognize(Identity);
				try {
					dsb.makeRecurringReservation(info.getCustomerId(), info.getDayOfWeek(), info.getStartTime()
															, info.getEndTime(), info.getAddress(), user.isChefsTable());
					
				} catch(Exception e) {
					LOGGER.warn("Could not Reserve a Weekly recurring timeslot "+info.getCustomerId(), e);			
				}
				
			}
			
			LOGGER.info("ReservationCron finished");
		} catch (Exception e) {
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
	
	
	static public Context getInitialContext() throws NamingException {
		Hashtable h = new Hashtable();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
}
