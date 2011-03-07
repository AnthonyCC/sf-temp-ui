package com.freshdirect.fdstore.zone;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.fdstore.zone.ejb.FDZoneInfoHome;
import com.freshdirect.fdstore.zone.ejb.FDZoneInfoSessionBean;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDZoneInfoManager {

		private final static ServiceLocator LOCATOR = new ServiceLocator();
		private final static Category LOGGER = LoggerFactory.getInstance(FDZoneInfoManager.class);

		
		private static FDZoneInfoHome managerHome = null;
		
		
	   
	   
	    
	    
	  
	    
	    private static void lookupManagerHome() throws FDResourceException {
			if (managerHome != null) {
				return;
			}
			Context ctx = null;
			try {
				ctx = FDStoreProperties.getInitialContext();
				managerHome = (FDZoneInfoHome) ctx.lookup("freshdirect.fdstore.ZoneInfoManager");
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
	    
	    
	    private static void invalidateManagerHome() {
			managerHome = null;
		}

    public static ErpZoneMasterInfo findZoneInfoMaster(String zoneId) throws FDResourceException {
        try {
            return FDServiceLocator.getInstance().getFDZoneInfoSessionBean().findZoneInfoMaster(zoneId);
        } catch (RemoteException re) {
            throw new FDResourceException(re, "Error talking to session bean");
        }
    }

    public static Collection loadAllZoneInfoMaster() throws FDResourceException {

        Collection zoneInfo = null;
        try {
            zoneInfo = FDServiceLocator.getInstance().getFDZoneInfoSessionBean().loadAllZoneInfoMaster();
        } catch (RemoteException re) {
            throw new FDResourceException(re, "Error talking to session bean");
        }
        return zoneInfo;
    }

    public static String findZoneId(String serviceType, String zipCode) throws FDResourceException {
        String zoneId = null;
        try {
            LOGGER.debug("Service Type:" + serviceType + " ZipCode is:" + zipCode);
            zoneId = FDServiceLocator.getInstance().getFDZoneInfoSessionBean().findZoneId(serviceType, zipCode);
            LOGGER.debug("zoneId found is :" + zoneId);
            if (zoneId == null) {
                throw new FDResourceException("Zone ID not found for serviceType:" + serviceType + ", zipCode:" + zipCode);
            }
        } catch (RemoteException re) {
            throw new FDResourceException(re, "Error talking to session bean");
        }
        return zoneId;
    }

}
