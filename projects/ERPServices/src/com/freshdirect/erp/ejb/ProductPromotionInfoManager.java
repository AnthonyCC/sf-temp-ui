package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class ProductPromotionInfoManager {

	private static Category LOGGER = LoggerFactory.getInstance(ProductPromotionInfoManager.class);

	public static Map<ZoneInfo, List<FDProductPromotionInfo>> getAllProductsByType(String ppType)
			throws FDResourceException {
		Map<ZoneInfo, List<FDProductPromotionInfo>> productPromoInfoMap = null;

		long startTime = startIntervalTimer();
		productPromoInfoMap = FDECommerceService.getInstance().getAllProductsByType(ppType);
		logTimeInterval(startTime, "FDECommerceService.getAllProductsByType");

		return productPromoInfoMap;

	}

	public static Map<ZoneInfo, List<FDProductPromotionInfo>> getAllProductsByType(String ppType, Date lastPublished)
			throws FDResourceException {

		Map<ZoneInfo, List<FDProductPromotionInfo>> productPromoInfoMap = null;

		long startTime = startIntervalTimer();

		productPromoInfoMap = FDECommerceService.getInstance().getAllProductsByType(ppType, lastPublished);
		logTimeInterval(startTime, "FDECommerceService.getAllProductsByType-date");

		return productPromoInfoMap;

	}

	public static List<ErpZoneMasterInfo> getAllZoneInfoDetails() throws FDResourceException {

		List<ErpZoneMasterInfo> zoneInfo = null;
		try {

			long startTime = startIntervalTimer();
			zoneInfo = FDECommerceService.getInstance().getAllZoneInfoDetails();
			logTimeInterval(startTime, "FDECommerceService.getAllZoneInfoDetails");

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
		return zoneInfo;

	}

	public static ErpProductPromotionPreviewInfo getProductPromotionPreviewInfo(String ppPreviewId)
			throws FDResourceException {

		ErpProductPromotionPreviewInfo erpProductPromotionPreviewInfo;

		long startTime = startIntervalTimer();

		erpProductPromotionPreviewInfo = FDECommerceService.getInstance().getProductPromotionPreviewInfo(ppPreviewId);
		logTimeInterval(startTime, "FDECommerceService.getProductPromotionPreviewInfo");

		return erpProductPromotionPreviewInfo;

	}

	public static Map<String, Map<ZoneInfo, List<FDProductPromotionInfo>>> getAllPromotionsByType(String ppType,
			Date lastPublishedDate) throws FDResourceException {
		Map<String, Map<ZoneInfo, List<FDProductPromotionInfo>>> productPromoInfoMap;

		long startTime = startIntervalTimer();

		productPromoInfoMap = FDECommerceService.getInstance().getAllPromotionsByType(ppType, lastPublishedDate);
		logTimeInterval(startTime, "FDECommerceService.getAllPromotionsByType");

		return productPromoInfoMap;

	}

	public static long startIntervalTimer() {

		return System.currentTimeMillis();
	}

	/**
	 * returns the difference between the start time and the current time in Mills
	 * 
	 * @param startTime
	 * @return
	 */
	public static long diffIntervalTimer(long startTime) {

		return (System.currentTimeMillis() - startTime);
	}

	/**
	 * Logs the elapsed time of an operation
	 * 
	 * @param startTime the start time in ms
	 * @param           methodName, indicating sb or rst call.
	 */
	public static void logTimeInterval(long startTime, String methodName) {

		long totalElapsedTime = diffIntervalTimer(startTime);
		LOGGER.info(methodName + "Elapsed Time = " + totalElapsedTime + " ms");

	}

}
