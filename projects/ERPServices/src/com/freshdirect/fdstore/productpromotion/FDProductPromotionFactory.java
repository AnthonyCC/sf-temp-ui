package com.freshdirect.fdstore.productpromotion;

import java.text.SimpleDateFormat;
import java.util.Date;
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

public class FDProductPromotionFactory {
	
	private final static Object lock = new Object();
	private static FDProductPromotionFactory sharedInstance = null;
	
	private static final Category LOGGER = LoggerFactory.getInstance(FDProductPromotionFactory.class);	
	private static SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

	
	private Map<String, Map<ZoneInfo,List<FDProductPromotionInfo>>> promotionMap = new LinkedHashMap<String, Map<ZoneInfo,List<FDProductPromotionInfo>>>();
	private Date presPickLastPublished;
//	private Date productsAssortmentLastPublished;
//	private Date currentTime;
	
	private ExpiringReference< Map<ZoneInfo,List<FDProductPromotionInfo>>> presPickPromotion = new ExpiringReference<Map<ZoneInfo,List<FDProductPromotionInfo>>>(5 * 60 * 1000) {
		protected Map<ZoneInfo,List<FDProductPromotionInfo>> load() {
			try {
				LOGGER.info("REFRESHING PRESIDENT'S PICK PRODUCT PROMOTION FOR ANY NEW PROMOTIONS FROM LAST MODIFIED TIME "+presPickLastPublished);
				/*try {
					currentTime = format.parse(format.format(new Date()));
				} catch (ParseException e) {
				}*/
				Date currentTime = new Date();
				Map<ZoneInfo,List<FDProductPromotionInfo>> productPromoInfoMap = null;
				if(null !=presPickLastPublished){
					productPromoInfoMap = ProductPromotionInfoManager.getAllProductsByType(EnumProductPromotionType.PRESIDENTS_PICKS.getName(),presPickLastPublished);					
				}else{
					loadPromotions();
				}
				presPickLastPublished = currentTime;
				LOGGER.info("REFRESHED PROMOTION MAP FOR ANY NEW PROMOTIONS.");
				return productPromoInfoMap;
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}
	};
	
	/*private ExpiringReference< Map<String,List<FDProductPromotionInfo>>> productsAssortmentPromotion = new ExpiringReference<Map<String,List<FDProductPromotionInfo>>>(1 * 60 * 1000) {
		protected Map<String,List<FDProductPromotionInfo>> load() {
			try {
				LOGGER.info("REFRESHING PRODUCTS ASSORTMENT PROMOTION FOR ANY NEW PROMOTIONS FROM LAST MODIFIED TIME "+productsAssortmentLastPublished);				
				Date currentTime = new Date();
				Map<String,List<FDProductPromotionInfo>> productPromoInfoMap = null;
				if(null !=productsAssortmentLastPublished){
					productPromoInfoMap = ProductPromotionInfoManager.getAllProductsByType(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getName(),productsAssortmentLastPublished);					
				}else{
					loadPromotions();
				}
				productsAssortmentLastPublished = currentTime;
				LOGGER.info("REFRESHED PROMOTION MAP FOR ANY NEW PRODUCTS ASSORTMENT PROMOTIONS.");
				return productPromoInfoMap;
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}
	};*/
	
	private FDProductPromotionFactory() {
		
		loadPromotions();
	}
	
	private void loadPromotions(){
		try {
//			List<FDProductPromotion> promoList = ProductPromotionManager.getProductPromotionsRT(EnumProductPromotionType.PRESIDENTS_PICKS.getName());
//			for ( FDProductPromotion promo : promoList ) {
			Map<ZoneInfo,List<FDProductPromotionInfo>> promoInfos = ProductPromotionInfoManager.getAllProductsByType(EnumProductPromotionType.PRESIDENTS_PICKS.getName());
			if(null !=promoInfos && !promoInfos.isEmpty()){
				this.promotionMap.put(EnumProductPromotionType.PRESIDENTS_PICKS.getName(),promoInfos);
			}
			/*promoInfos = ProductPromotionInfoManager.getAllProductsByType(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getName());
			if(null !=promoInfos && !promoInfos.isEmpty()){
				this.promotionMap.put(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getName(),promoInfos);
			}*/
		} catch (FDResourceException ex) {
			LOGGER.error("Failed to load product promotions", ex);
		}
	}
	

	public static FDProductPromotionFactory getInstance() {
		synchronized(lock) {
			if (sharedInstance == null) {
				sharedInstance = new FDProductPromotionFactory();
			}
		}
		
		return sharedInstance;
	}

	protected synchronized Map<String, Map<ZoneInfo,List<FDProductPromotionInfo>>> getPromotionMap() {
		Map<ZoneInfo,List<FDProductPromotionInfo>> promoInfos = this.presPickPromotion.get();
		if(null !=promoInfos && !promoInfos.isEmpty()){
			this.promotionMap.put(EnumProductPromotionType.PRESIDENTS_PICKS.getName(),promoInfos);
		}
		/*promoInfos = this.productsAssortmentPromotion.get();
		if(null !=promoInfos && !promoInfos.isEmpty()){
			this.promotionMap.put(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getName(),promoInfos);
		}*/
		return this.promotionMap;
	}
	
	
	
	public Map<ZoneInfo,List<FDProductPromotionInfo>> getProductPromotion(String type) {
		return this.getPromotionMap().get(type);
	}
	
	public List<FDProductPromotionInfo> getProductPromotion(String type,String zoneId) {
		Map<ZoneInfo,List<FDProductPromotionInfo>> map =getProductPromotion(type);
		List<FDProductPromotionInfo> promotionProducts = null;
		if(null != map){
			promotionProducts = map.get(zoneId);
		}
		return promotionProducts;
	}
	
	public void forceRefresh(String type) {
		if(EnumProductPromotionType.PRESIDENTS_PICKS.equals(type)){
			presPickPromotion.forceRefresh();
		}
		/*if(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.equals(type)){
			productsAssortmentPromotion.forceRefresh();
		}*/
	}	
	
	/*public void populateSkus(FDProductPromotion fdProductPromotion){
		if(null !=fdProductPromotion){
			Map<String,Map<String,Set<FDProductPromotionSku>>> featuredSkus = new HashMap<String,Map<String,Set<FDProductPromotionSku>>>();
			Map<String,Map<String,Set<FDProductPromotionSku>>> nonFeaturedSkus= new HashMap<String,Map<String,Set<FDProductPromotionSku>>>();
			fdProductPromotion.setFeaturedSkus(featuredSkus);
			fdProductPromotion.setNonFeaturedSkus(nonFeaturedSkus);
			Map<String, List<FDProductPromotionSku>> zoneSkusMap =fdProductPromotion.getZoneSkus();
			Set<String> zones =zoneSkusMap.keySet();
			for (String zoneId : zones) {
				List<FDProductPromotionSku> skusForZone = zoneSkusMap.get(zoneId);
				//group the skus by dept, featured, priority and the dept criteria.
				for (Iterator iterator = skusForZone.iterator(); iterator.hasNext();) {
					FDProductPromotionSku productPromotionSku = (FDProductPromotionSku) iterator.next();
					String dept = productPromotionSku.getDeptCode();
					Map<String,Set<FDProductPromotionSku>> deptSkus = null;
					deptSkus =nonFeaturedSkus.get(zoneId);
					if(productPromotionSku.isFeatured()){
						deptSkus =featuredSkus.get(zoneId);						
					}
					if(null == deptSkus){
						deptSkus = new HashMap<String,Set<FDProductPromotionSku>>();
					}
					
					Set<FDProductPromotionSku> skus = deptSkus.get(dept);
					if(null == skus){
						skus= new TreeSet<FDProductPromotionSku>();
					}
					skus.add(productPromotionSku);
					deptSkus.put(dept, skus);
					
					if(productPromotionSku.isFeatured()){
						featuredSkus.put(zoneId, deptSkus);
					}else{
						nonFeaturedSkus.put(zoneId, deptSkus);
					}					
				}				
			}			
		}
		
	}*/
	
	/*public List<FDProductPromotionSku> getSkus(String promotionType, String pricingZoneId){
		List<FDProductPromotionSku> list = new ArrayList<FDProductPromotionSku>();
		FDProductPromotion promotion = getProductPromotion(promotionType);
		if(null != promotion && null !=promotion.getZoneSkus()){
			list = promotion.getZoneSkus().get(pricingZoneId);
			if(null == list || !list.isEmpty()){
				list = promotion.getZoneSkus().get(FDProductPromotion.DEFAULT_ZONE);//DEFAULT PRICING ZONE
			}
		}		
		return list;
	}*/
	

	
	
	

}
