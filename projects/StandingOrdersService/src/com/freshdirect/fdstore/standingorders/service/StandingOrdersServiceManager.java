package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class StandingOrdersServiceManager {
	
	private final static Category LOGGER = LoggerFactory.getInstance(StandingOrdersServiceManager.class);

	private static StandingOrdersServiceHome soHome = null;
	
	private static void lookupServiceHome() throws FDResourceException {
		if(soHome != null){
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			soHome = (StandingOrdersServiceHome) ctx.lookup(StandingOrdersServiceHome.JNDI_HOME);
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}
	
	private static void invalidateHome() {
		soHome = null;
	}
	
	private static StandingOrdersServiceManager sharedInstance;

	protected StandingOrdersServiceManager() {}
	
	public static synchronized StandingOrdersServiceManager getInstance() {
		if (sharedInstance == null) {
			sharedInstance = new StandingOrdersServiceManager();
		}
		return sharedInstance;
	}
	
	public void placeStandingOrders(Collection<String> soList, StandingOrdersJobConfig jobConfig) throws FDResourceException {
		lookupServiceHome();
		try {
			StandingOrdersServiceSB sb = soHome.create();			
			sb.placeStandingOrders(soList, jobConfig);
		} catch (CreateException ce) {
			invalidateHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

}
