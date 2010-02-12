package com.freshdirect.fdstore.zone;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.ejb.ErpZoneInfoHome;
import com.freshdirect.erp.ejb.ErpZoneInfoSB;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.zone.ejb.FDZoneInfoHome;
import com.freshdirect.fdstore.zone.ejb.FDZoneInfoSB;
import com.freshdirect.fdstore.zone.ejb.FDZoneInfoSessionBean;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDZoneInfoManager {
	
	
	  
	    private static Category LOGGER = LoggerFactory.getInstance( FDZoneInfoSessionBean.class );

		private final static ServiceLocator LOCATOR = new ServiceLocator();
		
		private static FDZoneInfoHome managerHome = null;
		
		
		public static ErpZoneMasterInfo findZoneInfoMaster(String zoneId) throws FDResourceException {		     						
			
			
			try {
				lookupManagerHome();
				FDZoneInfoSB sb = managerHome.create();
				return sb.findZoneInfoMaster(zoneId);
			} catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
			
			
			
					
		}
	    
	    public static Collection loadAllZoneInfoMaster() throws FDResourceException{
	    	
	    	
	    	 Collection zoneInfo=null;
	 		try{
	 			lookupManagerHome();
	 			FDZoneInfoSB sb = managerHome.create();	 			
	 			zoneInfo=sb.loadAllZoneInfoMaster();
	 		}catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
	 		return zoneInfo;
	    }
	    
	    public  static String findZoneId(String serviceType,String zipCode) throws FDResourceException{
	   	 String zoneId=null;
	 		try{
	 			lookupManagerHome();
	 			FDZoneInfoSB sb = managerHome.create();	 		
	 			LOGGER.debug("Service Type:"+serviceType+" ZipCode is:"+zipCode);
	 			zoneId=sb.findZoneId(serviceType, zipCode);
	 			LOGGER.debug("zoneId found is :"+zoneId);
	 			invalidateManagerHome();				
	 		}	catch (CreateException ce) {
				invalidateManagerHome();
				throw new FDResourceException(ce, "Error creating session bean");
				
			} catch (RemoteException re) {
				invalidateManagerHome();
				throw new FDResourceException(re, "Error talking to session bean");
			}
	 		return zoneId;
	    }
	    
	    
	  
	    
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

}
