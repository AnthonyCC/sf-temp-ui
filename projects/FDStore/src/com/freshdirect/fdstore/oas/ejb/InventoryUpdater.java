/**
 * @author ekracoff
 * Created on Aug 9, 2005*/

package com.freshdirect.fdstore.oas.ejb;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;


public class InventoryUpdater {
	private final static Category LOGGER = LoggerFactory.getInstance(InventoryUpdater.class);

	public static void main(String[] args) {
		Context ctx = null;
		try {
			ctx = getInitialContext();
			AdServerGatewayHome home = (AdServerGatewayHome) ctx.lookup("freshdirect.fdstore.AdServerGateway");
			AdServerGatewaySB sb = home.create();
			
			long startTime = System.currentTimeMillis();
			LOGGER.info("-- Starting Inventory Update -- ");
			sb.run();
			LOGGER.info("-- Finished -- process took " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds");
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
