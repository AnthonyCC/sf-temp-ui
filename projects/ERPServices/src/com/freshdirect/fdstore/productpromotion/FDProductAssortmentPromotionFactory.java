package com.freshdirect.fdstore.productpromotion;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.erp.EnumProductPromotionType;
import com.freshdirect.erp.ejb.ProductPromotionInfoManager;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.ExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDProductAssortmentPromotionFactory {
	
	private final static Object lock = new Object();
	private static FDProductAssortmentPromotionFactory sharedInstance = null;
	
	private static final Category LOGGER = LoggerFactory.getInstance(FDProductAssortmentPromotionFactory.class);	
	
	private Map<String, Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>>> promotionMap = new LinkedHashMap<String, Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>>>();
	private Date lastPublished;
	
	
	
	private ExpiringReference< Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>>> productsAssortmentPromotion = new ExpiringReference<Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>>>(5 * 60 * 1000) {
		protected Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> load() {
			try {
				LOGGER.info("REFRESHING PRODUCTS ASSORTMENT PROMOTION FOR ANY NEW PROMOTIONS FROM LAST MODIFIED TIME "+lastPublished);				
				Date currentTime = new Date();
				Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> productPromoInfoMap = null;
				if(null !=lastPublished){
					productPromoInfoMap = ProductPromotionInfoManager.getAllPromotionsByType(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getName(),lastPublished);					
				}else{
					loadPromotions();
				}
				lastPublished = currentTime;
				LOGGER.info("REFRESHED PROMOTION MAP FOR ANY NEW PRODUCTS ASSORTMENT PROMOTIONS.");
				return productPromoInfoMap;
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}
	};
	
	private FDProductAssortmentPromotionFactory() {
		
		loadPromotions();
	}
	
	private void loadPromotions(){
		try {			
			Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> promoInfos = ProductPromotionInfoManager.getAllPromotionsByType(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getName(),null);
			if(null !=promoInfos && !promoInfos.isEmpty()){
				this.promotionMap.put(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getName(),promoInfos);
			}
		} catch (FDResourceException ex) {
			LOGGER.error("Failed to load product promotions", ex);
		}
	}
	

	public static FDProductAssortmentPromotionFactory getInstance() {
		synchronized(lock) {
			if (sharedInstance == null) {
				sharedInstance = new FDProductAssortmentPromotionFactory();
			}
		}
		
		return sharedInstance;
	}

	protected synchronized Map<String,Map<String, Map<ZoneInfo,List<FDProductPromotionInfo>>>> getPromotionMap() {
		
		Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> promoInfos = this.productsAssortmentPromotion.get();
		if(null !=promoInfos && !promoInfos.isEmpty()){
			Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> lPromos =this.promotionMap.get(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getName());
			if(null ==lPromos){
				lPromos = new HashMap<String, Map<ZoneInfo,List<FDProductPromotionInfo>>>();
			}
			lPromos.putAll(promoInfos);
			this.promotionMap.put(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getName(),lPromos);
		}
		return this.promotionMap;
	}
	
	
	
	public 	Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> getProductPromotion(String type) {
		return this.getPromotionMap().get(type);
	}
	
	public Map<ZoneInfo,List<FDProductPromotionInfo>> getProductPromotion(String type,String ppId) {
		Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> map =getProductPromotion(type);
		Map<ZoneInfo,List<FDProductPromotionInfo>> promotionZoneProducts= null;
		if(null != map){
			promotionZoneProducts = map.get(ppId);
		}
		/*if(null != promotionZoneProducts){
			if(null == zoneId){
				promotionProducts = promotionZoneProducts.get(ZonePriceListing.MASTER_DEFAULT_ZONE);
			}else{
				promotionProducts = promotionZoneProducts.get(zoneId);
			}
		}*/
		return promotionZoneProducts;
	}
	
	
	public void forceRefresh(String type) {
		if(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.equals(type)){
			productsAssortmentPromotion.forceRefresh();
		}
	}	
	
	
	

	
	
	

}
