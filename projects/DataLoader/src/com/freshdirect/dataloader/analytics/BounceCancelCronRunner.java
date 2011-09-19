package com.freshdirect.dataloader.analytics;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.analytics.ejb.BounceCancelHome;
import com.freshdirect.dataloader.analytics.ejb.BounceCancelSB;
import com.freshdirect.framework.util.log.LoggerFactory;


public class BounceCancelCronRunner {

	/**
	 * @param args
	 */

	private final static Category LOGGER = LoggerFactory.getInstance(OrderRateCronRunner.class);

	public static void main(String[] args) {
		
		Context ctx = null;
		try 
		{
			ctx = getInitialContext();
			BounceCancelHome  home = (BounceCancelHome) ctx.lookup("freshdirect.dataloader.BounceCancelCron");

			BounceCancelSB sb = home.create();
			sb.cancelBounce();
		}
	catch (Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));			
		LOGGER.info(new StringBuilder("BounceCancelCronRunner failed with Exception...").append(sw.toString()).toString());
		LOGGER.error(sw.toString());
	//	email(Calendar.getInstance().getTime(), sw.getBuffer().toString());		
	} finally {
		try {
			if (ctx != null) {
				ctx.close();
				ctx = null;
			}
		} catch (NamingException ne) {
			StringWriter sw = new StringWriter();
			ne.printStackTrace(new PrintWriter(sw));	
		//	email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
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
