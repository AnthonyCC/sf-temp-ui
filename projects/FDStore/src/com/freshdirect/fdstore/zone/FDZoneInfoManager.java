package com.freshdirect.fdstore.zone;

import java.rmi.RemoteException;
import java.util.Collection;

import org.apache.log4j.Category;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class FDZoneInfoManager {
	private final static Category LOGGER = LoggerFactory.getInstance(FDZoneInfoManager.class);


    public static ErpZoneMasterInfo findZoneInfoMaster(String zoneId) throws FDResourceException {
        try {

        	if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDZoneInfoSB)){
        	return FDECommerceService.getInstance().findZoneInfoMaster(zoneId);
        	}else {
            return FDServiceLocator.getInstance().getFDZoneInfoSessionBean().findZoneInfoMaster(zoneId);
        	}
        } catch (RemoteException re) {
            throw new FDResourceException(re, "Error talking to session bean");
        }
    }

    public static Collection loadAllZoneInfoMaster() throws FDResourceException {

        Collection zoneInfo = null;
        try {

        	if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDZoneInfoSB)){
        		 zoneInfo = FDECommerceService.getInstance().loadAllZoneInfoMaster();
            	}else {
            zoneInfo = FDServiceLocator.getInstance().getFDZoneInfoSessionBean().loadAllZoneInfoMaster();
            	}
        } catch (RemoteException re) {
            throw new FDResourceException(re, "Error talking to session bean");
        }
        return zoneInfo;
    }

	public static String findZoneId(String serviceType, String zipCode) throws FDResourceException {
		String zoneId = null;
		try {
			String cacheKey = serviceType + "," + zipCode;
			String cachedZoneId = CmsServiceLocator.ehCacheUtil().getObjectFromCache(CmsCaches.FD_ZONE_ID_CACHE_NAME.cacheName, cacheKey);
			if (cachedZoneId != null) {
				return cachedZoneId;
			}
			LOGGER.debug("Service Type:" + serviceType + " ZipCode is:" + zipCode);
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDZoneInfoSB)) {
				zoneId = FDECommerceService.getInstance().findZoneId(serviceType, zipCode);
			} else {
				zoneId = FDServiceLocator.getInstance().getFDZoneInfoSessionBean().findZoneId(serviceType, zipCode);
			}
			LOGGER.debug("zoneId found is :" + zoneId);
			if (zoneId == null) {
				throw new FDResourceException(
						"Zone ID not found for serviceType:" + serviceType + ", zipCode:" + zipCode);
			}
			CmsServiceLocator.ehCacheUtil().putObjectToCache(CmsCaches.FD_ZONE_ID_CACHE_NAME.cacheName, cacheKey, zoneId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return zoneId;

	}

    public static String findZoneId(String serviceType, String zipCode, boolean isPickupOnlyORNotServiceble) throws FDResourceException {
        String zoneId = null;
        try {
            LOGGER.debug("Service Type:" + serviceType + " ZipCode is:" + zipCode);
            if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDZoneInfoSB)){
            	zoneId = FDECommerceService.getInstance().findZoneId(serviceType, zipCode, isPickupOnlyORNotServiceble);
           	}else {
            zoneId = FDServiceLocator.getInstance().getFDZoneInfoSessionBean().findZoneId(serviceType, zipCode, isPickupOnlyORNotServiceble);
           	}
            LOGGER.debug("zoneId found is :" + zoneId);
            if (zoneId == null) {
                throw new FDResourceException("Zone ID not found for serviceType:" + serviceType + ", zipCode:" + zipCode);
            }
        } catch (RemoteException re) {
            throw new FDResourceException(re, "Error talking to session bean");
        }
        return zoneId;

    }

    /*public static PlantSalesArea getPlantInfo() {
    	SalesArea sa=new SalesArea("SO1","DC1","DIV1");
    	SalesArea dsa=new SalesArea("SO","DC","DIV");

    	PlantSalesArea psa=new PlantSalesArea();
    	psa.setCode("1000");//Plant ID
    	psa.setDefaultSalesArea(dsa);
    	psa.setSalesArea(sa);
    	return psa;
    }*/

}