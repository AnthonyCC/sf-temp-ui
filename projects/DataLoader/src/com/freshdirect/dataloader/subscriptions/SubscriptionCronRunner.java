package com.freshdirect.dataloader.subscriptions;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.payment.ejb.SaleCronHome;
import com.freshdirect.dataloader.payment.ejb.SaleCronSB;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SubscriptionCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(SubscriptionCronRunner.class);

	public static void main(String[] args) {
		long authTimeout;

		if (args.length >= 1) {
			int minutes = 15;
			try {
				// first parameter is for authTimeout
				minutes = Integer.parseInt(args[0]);
			} catch (NumberFormatException ne) {
				LOGGER.warn("Passed in param is not an int", ne);
			}

			authTimeout = minutes * 60 * 1000;
		}else{
			LOGGER.warn("NO timeout was passed for Subscription Authorizations defaulting to 15 mins");
			authTimeout = 15 * 60 * 1000;
		}

		Context ctx = null;
		try {
			ctx = getInitialContext();
			SaleCronHome home = (SaleCronHome) ctx.lookup("freshdirect.dataloader.SaleCron");

			SaleCronSB sb = home.create();
			sb.authorizeSubscriptions(authTimeout);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				//could not do the cleanup
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

