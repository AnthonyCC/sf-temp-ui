package com.freshdirect.erp.ejb;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.productpromotion.FDProductPromotionFactory;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDProductPromotionManager {

	private static Category LOGGER = LoggerFactory.getInstance(FDProductPromotionManager.class);
	private static FDProductPromotionManager instance = new FDProductPromotionManager();
	
	private FDProductPromotionManager(){
		
	}
	
	public static FDProductPromotionManager getInstance(){
		return instance;
	}
	
	public static Map<ZoneInfo,List<FDProductPromotionInfo>> getProductPromotion(String ppType) throws FDResourceException{
		return FDProductPromotionFactory.getInstance().getProductPromotion(ppType);
	}
	
	public static List<FDProductPromotionInfo> getProductPromotion(String ppType,String zoneId) throws FDResourceException{
		return FDProductPromotionFactory.getInstance().getProductPromotion(ppType,zoneId);
	}
	//TODO missing methods. Suggestions from Tamas: 
	//When the configuration changes getProductPromotionByType and getSkus should return new objects,
	//because the previous objects are referenced by CMS and we don't want the referenced objects to change there.
	//The new objects will be obtained by the CMS periodically.
	//
	//When a new configuration is activated the references that are returned by getProductPromotionByType
	//and getSkus should be changed in a synchronized(this) block. The CMS will call getSkus and getProductPromotionByType
	//from a synchronized (FDProductPromotionManager.getInstance()) block which ensure that the references don't loose
	//integrity when applying a new configuration.
	
	/*public Map<String, Map<String, List<FDProductPromotionSku>>> populatePromotionPageProducts(FDProductPromotion fdProductPromotion,boolean preview) {
		return FDProductPromotionFactory.getInstance().populatePromotionPageProducts(fdProductPromotion,preview);
	}*/
}
