package com.freshdirect.dataloader.addressscrubbing;

import java.util.Calendar;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.addressscrubbing.ejb.AddressScrubberLoaderSB;
import com.freshdirect.dataloader.addressscrubbing.ejb.AddressScrubbingLoaderHome;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author Aniwesh Vatsal
 *
 */
public class ExceptionAddressVerifyLoader {

	private static final Category LOGGER = LoggerFactory.getInstance(ExceptionAddressVerifyLoader.class);
	
	public static void main(String arg[]){
		Context ctx = null;
		try {
			ctx = getInitialContext();
			AddressScrubbingLoaderHome home=getAddressScrubbingLoaderHome(ctx);
			AddressScrubberLoaderSB sb = home.create();	
			System.out.println("Start Time "+Calendar.getInstance().getTime());
			sb.verifyExceptionAddress();
			System.out.println("END Time "+Calendar.getInstance().getTime());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn(e.getMessage());			
		}
		finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				ne.printStackTrace();
			}
		}	
	}
	
	 public static Context getInitialContext() throws NamingException {
			Hashtable<String, String> h = new Hashtable<String, String>();
			h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
			return new InitialContext(h);
		}	
	 
	 
	public static AddressScrubbingLoaderHome getAddressScrubbingLoaderHome(
			Context ctx) {

		AddressScrubbingLoaderHome home = null;
		try {

			home = (AddressScrubbingLoaderHome) ctx.lookup("freshdirect.dataloader.AddressScrubbingLoader");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn(e.getMessage());
		}

		return home;
	}

}
