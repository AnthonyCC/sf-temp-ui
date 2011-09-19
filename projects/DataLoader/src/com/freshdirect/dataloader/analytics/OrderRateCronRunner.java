package com.freshdirect.dataloader.analytics;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.analytics.ejb.OrderRateHome;
import com.freshdirect.analytics.ejb.OrderRateSB;
import com.freshdirect.dataloader.payment.SaleCronRunner;
import com.freshdirect.framework.util.log.LoggerFactory;

public class OrderRateCronRunner {

	/**
	 * @param args
	 */

	private final static Category LOGGER = LoggerFactory.getInstance(OrderRateCronRunner.class);

	public static void main(String[] args) {
		
		Context ctx = null;
		try 
		{
			ctx = getInitialContext();
			OrderRateHome home = (OrderRateHome) ctx.lookup("freshdirect.analytics.OrderRate");
			OrderRateSB sb = home.create();
			sb.getOrderRate();
		}
	catch (Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));			
		LOGGER.info(new StringBuilder("OrderRateCronRunner failed with Exception...").append(sw.toString()).toString());
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
