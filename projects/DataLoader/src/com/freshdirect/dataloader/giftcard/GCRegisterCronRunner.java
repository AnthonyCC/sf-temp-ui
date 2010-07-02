/*
 * 
 * SaleCronRunner.java
 * Date: Jul 5, 2002 Time: 5:53:45 PM
 */

package com.freshdirect.dataloader.giftcard;

/**
 * 
 * @author skrishnasamy
 */
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.payment.ejb.SaleCronHome;
import com.freshdirect.dataloader.payment.ejb.SaleCronSB;
import com.freshdirect.framework.util.log.LoggerFactory;

public class GCRegisterCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(GCRegisterCronRunner.class);

	public static void main(String[] args) {
		
		long registerTimeout;
		if (args.length >= 1) {
			int minutes = 50;
			try {
				// first parameter is for captureTimeout
				minutes = Integer.parseInt(args[0]);
			} catch (NumberFormatException ne) {
				LOGGER.warn("Passed in param is not an int", ne);
			}

			registerTimeout = minutes * 60 * 1000;
		}else{
			LOGGER.warn("NO timeout was passed for Register Transactions defaulting to 20 mins");
			registerTimeout = 20 * 60 * 1000;
		}

		Context ctx = null;
		try {
			LOGGER.info("GCRegisterCron started");
			ctx = getInitialContext();
			SaleCronHome home = (SaleCronHome) ctx.lookup("freshdirect.dataloader.SaleCron");

			SaleCronSB sb = home.create();
			sb.registerGiftCards(registerTimeout);
			LOGGER.info("GCRegisterCron finished");
		} catch (Exception e) {
			LOGGER.error(e);
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
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}

}
