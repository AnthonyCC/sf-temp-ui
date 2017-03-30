package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class ProductPromotionInfoManager {

	private static Category LOGGER = LoggerFactory.getInstance(ProductPromotionInfoManager.class);
	
	private static ErpProductPromotionInfoHome managerHome = null;
	
	public static Map<ZoneInfo,List<FDProductPromotionInfo>> getAllProductsByType(String ppType) throws FDResourceException{
		lookupManagerHome();
		Map<ZoneInfo,List<FDProductPromotionInfo>> productPromoInfoMap=null;
		try {
			if(FDStoreProperties.isStorefront2_0Enabled()){
			productPromoInfoMap=FDECommerceService.getInstance().getAllProductsByType(ppType);
			}else{
				ErpProductPromotionInfoSB sb = managerHome.create();
				productPromoInfoMap =sb.getAllProductsByType(ppType);
			}

			return productPromoInfoMap;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}		
	}
	
	public static Map<ZoneInfo,List<FDProductPromotionInfo>> getAllProductsByType(String ppType, Date lastPublished) throws FDResourceException{
		lookupManagerHome();

		try {
			ErpProductPromotionInfoSB sb = managerHome.create();
			Map<ZoneInfo,List<FDProductPromotionInfo>> productPromoInfoMap =sb.getAllProductsByType(ppType,lastPublished);
			return productPromoInfoMap;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}		
	}
	
	private static void lookupManagerHome() throws FDResourceException {
		if (managerHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			managerHome = (ErpProductPromotionInfoHome) ctx.lookup(FDStoreProperties.getProductPromotionInfoHome());
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
	
	public static List<ErpZoneMasterInfo> getAllZoneInfoDetails() throws FDResourceException {
	     
		
		List<ErpZoneMasterInfo> zoneInfo=null;
		try{
			ErpZoneInfoHome home=getErpZoneInfoHome();
			ErpZoneInfoSB remote= home.create();
			zoneInfo=remote.getAllZoneInfoDetails();
		}catch(CreateException sqle){
			invalidateManagerHome();
			LOGGER.error("Unable to load all getAllZoneInfoDetails " , sqle);			
			throw new FDResourceException(sqle);
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return zoneInfo;
				
	}	
	
	public static ErpProductPromotionPreviewInfo getProductPromotionPreviewInfo(String ppPreviewId)throws FDResourceException{
		lookupManagerHome();

		try {
			ErpProductPromotionInfoSB sb = managerHome.create();
			ErpProductPromotionPreviewInfo erpProductPromotionPreviewInfo =sb.getProductPromotionPreviewInfo(ppPreviewId);
			return erpProductPromotionPreviewInfo;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}	
	}
		
	private static void invalidateManagerHome() {
		managerHome = null;
	}
	
	private static ErpZoneInfoHome getErpZoneInfoHome() {
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			return (ErpZoneInfoHome) ctx.lookup("freshdirect.erp.ZoneInfoManager");
		} catch (NamingException ne) {
			throw new EJBException(ne);
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
		
	public static Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> getAllPromotionsByType(String ppType,Date lastPublishedDate) throws FDResourceException{
		lookupManagerHome();

		try {
			ErpProductPromotionInfoSB sb = managerHome.create();
			Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> productPromoInfoMap =sb.getAllPromotionsByType(ppType,lastPublishedDate);
			return productPromoInfoMap;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}		
	}
}
