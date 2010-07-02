package com.freshdirect.dataloader.reservation;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.fdstore.FDStoreProperties;

public class BaseCapacityCronRunner {
	
	public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		System.out.println("FDStoreProperties.getRoutingProviderURL() :"+FDStoreProperties.getRoutingProviderURL());
		h.put(Context.PROVIDER_URL, FDStoreProperties.getRoutingProviderURL());
		return new InitialContext(h);
	}
}
