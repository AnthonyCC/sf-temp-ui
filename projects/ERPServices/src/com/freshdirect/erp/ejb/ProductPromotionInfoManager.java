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
import com.freshdirect.fdstore.FDEcommProperties;
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
			long startTime;
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpProductPromotionInfoSB)){
				startTime=  startIntervalTimer();
				productPromoInfoMap=FDECommerceService.getInstance().getAllProductsByType(ppType);
				logTimeInterval(startTime,"FDECommerceService.getAllProductsByType" );
			}else{
				startTime=  startIntervalTimer();
				ErpProductPromotionInfoSB sb = managerHome.create();
				productPromoInfoMap =sb.getAllProductsByType(ppType);
				logTimeInterval(startTime,"ErpProductPromotionInfoSB.getAllProductsByType" );
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
		Map<ZoneInfo,List<FDProductPromotionInfo>> productPromoInfoMap= null;
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpProductPromotionInfoSB)){
				productPromoInfoMap= FDECommerceService.getInstance().getAllProductsByType(ppType,lastPublished);
			}else{
			ErpProductPromotionInfoSB sb = managerHome.create();
			productPromoInfoMap =sb.getAllProductsByType(ppType,lastPublished);
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
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpZoneInfoSB)){
				zoneInfo= FDECommerceService.getInstance().getAllZoneInfoDetails();
			}else{
			zoneInfo=remote.getAllZoneInfoDetails();
			}
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
//		lookupManagerHome();
		ErpProductPromotionPreviewInfo erpProductPromotionPreviewInfo;
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpProductPromotionInfoSB)){
				erpProductPromotionPreviewInfo=	FDECommerceService.getInstance().getProductPromotionPreviewInfo(ppPreviewId);
			}else{
			ErpProductPromotionInfoSB sb = managerHome.create();
			erpProductPromotionPreviewInfo =sb.getProductPromotionPreviewInfo(ppPreviewId);
			}
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
//		lookupManagerHome();
		Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> productPromoInfoMap;
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpProductPromotionInfoSB)){
			productPromoInfoMap= FDECommerceService.getInstance().getAllPromotionsByType(ppType,lastPublishedDate);
			}else{
			ErpProductPromotionInfoSB sb = managerHome.create();
			productPromoInfoMap =sb.getAllPromotionsByType(ppType,lastPublishedDate);
			
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
	
	public static long startIntervalTimer(){
		
		 return System.currentTimeMillis();
	}
	/**
	 * returns the difference between the start time and the current time in Mills
	 * @param startTime
	 * @return
	 */
	public static long diffIntervalTimer(long startTime){
		
		 return (System.currentTimeMillis() -startTime ) ;
	}
	/**
	 * Logs the elapsed time of an operation
	 * @param startTime the start time in ms
	 * @param methodName, indicating sb or rst call.
	 */
	public static  void logTimeInterval(long startTime, String methodName){
		
		long totalElapsedTime = diffIntervalTimer(startTime);
		LOGGER.info(methodName+  "Elapsed Time = "+totalElapsedTime+" ms" );
		
	}
	
}
