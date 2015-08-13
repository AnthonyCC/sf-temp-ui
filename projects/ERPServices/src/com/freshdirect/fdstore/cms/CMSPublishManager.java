package com.freshdirect.fdstore.cms;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.cms.ejb.CMSManagerHome;
import com.freshdirect.fdstore.cms.ejb.CMSManagerSB;

public class CMSPublishManager {
	
	private static CMSManagerHome managerHome = null;
	private static final Logger LOGGER = Logger.getLogger(CMSPublishManager.class);
	
	public static void createFeed(String feedId, String storeId, String feedData) throws FDResourceException {
		CMSManagerSB sb = getCMSManagerSB();
		try {
			sb.createFeed(feedId, storeId, feedData);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static String getLatestFeed(String storeId) throws FDResourceException {
		String response = null;
		CMSManagerSB sb = getCMSManagerSB();
		try {
			response = sb.getLatestFeed(storeId);
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return response;
	}
	
	private static CMSManagerSB getCMSManagerSB()  throws FDResourceException {
		lookupManagerHome();
		CMSManagerSB sb = null;
		try {
			sb = managerHome.create();
		} catch (CreateException ce) {
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return sb;
		
	}
	
	private static void lookupManagerHome() throws FDResourceException {
		if (managerHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			managerHome = (CMSManagerHome) ctx.lookup("freshdirect.cms.CMSManager");
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
}