package com.freshdirect.cms.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDProductPromotionPreviewInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.ProductModelPromotionAdapter;
import com.freshdirect.fdstore.ZonePriceInfoListing;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentUtil;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductPromotionData;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ProductPromotionUtil {

	private static final Logger LOGGER = LoggerFactory.getInstance(ProductPromotionUtil.class);
	
	public static Map<String, Map<String, List<ProductModel>>> populatePromotionPageProducts(Map<String, Map<String,List<FDProductPromotionInfo>>> zoneSkusMap,Map<String, ProductModel> skuProductMap) {
		
		Map<String, Map<String, List<ProductModel>>> zoneProductModelsMap = new HashMap<String, Map<String, List<ProductModel>>>();
		
//		zoneSkusMap =populatePromotionPageProducts(fdProductPromotion,preview);
//		zoneSkusMap =fdProductPromotion.getZoneSkusMap();
		if(null !=zoneSkusMap){
			for (Iterator iterator = zoneSkusMap.keySet().iterator(); iterator.hasNext();) {
				String zoneId = (String) iterator.next();
				Map<String, List<ProductModel>> productModelsMap = zoneProductModelsMap.get(zoneId);
				if(null == productModelsMap){
					productModelsMap = new HashMap<String, List<ProductModel>>();
					zoneProductModelsMap.put(zoneId, productModelsMap);
				}
				Map<String, List<FDProductPromotionInfo>> skusMap = zoneSkusMap.get(zoneId);
				if(null != skusMap){
					for (Iterator iterator2 = skusMap.keySet().iterator(); iterator2.hasNext();) {
						String type = (String) iterator2.next();
						List<ProductModel> products = productModelsMap.get(type);
						if(null == products){
							products = new ArrayList<ProductModel>();
							productModelsMap.put(type, products);
						}
						List<FDProductPromotionInfo> skusList = skusMap.get(type);
						if(null != skusList){
							for (Iterator iterator3 = skusList.iterator(); iterator3.hasNext();) {
								FDProductPromotionInfo fdProductPromotionSku = (FDProductPromotionInfo) iterator3.next();
								String skuCode = fdProductPromotionSku.getSkuCode();
								if(fdProductPromotionSku.isFeatured()){
									products = productModelsMap.get(fdProductPromotionSku.getFeaturedHeader());
									if(null == products){
										products = new ArrayList<ProductModel>();
										productModelsMap.put(fdProductPromotionSku.getFeaturedHeader(), products);
									}
								}
								ProductModel productModel = null;
								if(null !=skuProductMap){
									productModel = skuProductMap.get(skuCode);
								}
								if(null == productModel){
									try {
										productModel = ((SkuModel) ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey(FDContentTypes.SKU, skuCode))).getProductModel();
										skuProductMap.put(skuCode,productModel);
									} catch (Exception e){
										LOGGER.warn("Failed to get the product for sku: " + skuCode +" in promo products", e);
									}
								}
								if (null !=productModel && !products.contains(productModel)) {
									products.add(productModel);
								}
							}
						}					
					}
				}
			}
		}
		return zoneProductModelsMap;
	}	
	

	public static List<ProductModel> getFeaturedProducts(List<ProductModel> products,boolean isPreview){
		return getFeaturedProducts(products, isPreview, 3);
	}
	
	public static List<ProductModel> getFeaturedProducts(List<ProductModel> products,boolean isPreview, int limit){
		List<ProductModel> featProducts = new ArrayList<ProductModel>();
		if(null != products){
			int i=0;
			for (Iterator iterator = products.iterator(); iterator.hasNext();) {
				ProductModel productModel = (ProductModel) iterator.next();
				if(isPreview ||(!isPreview && !isUnavailable(productModel)))
				if (productModel instanceof ProductModelPromotionAdapter) {
					if(((ProductModelPromotionAdapter)productModel).isFeatured() ){
						featProducts.add(productModel);
						iterator.remove();
						i++;
						if(i>=limit)break;//Need only 3 featured products to show. (that's now default)
					}					
				}				
				
			}
		}
		return featProducts;
	}
	
	/*
	 * Except the first 3 featured products, remaining all are non-featured products.
	 */
	public static List<ProductModel> getNonFeaturedProducts(List<ProductModel> products,boolean isPreview){
		List<ProductModel> nonFeatProducts = new ArrayList<ProductModel>();
		if(null != products){
			int i=0;
			for (Iterator iterator = products.iterator(); iterator.hasNext();) {
				ProductModel productModel = (ProductModel) iterator.next();
				if(isPreview ||(!isPreview && !isUnavailable(productModel))){
					if (productModel instanceof ProductModelPromotionAdapter) {	
						nonFeatProducts.add(productModel);
					}				
				}
				
			}
		}
		return nonFeatProducts;
	}
		
	public static List<ProductModel> getNonFeaturedProducts(List<ProductModel> products, String sortByType,boolean isPreview){
		List<ProductModel> nonFeatProducts =  getNonFeaturedProducts(products, isPreview);
		if(null != sortByType){
			if(sortByType.equalsIgnoreCase(ProductPromotionData.SORT_BY_DEPT_VIEW)){
				Collections.sort(nonFeatProducts, DEPTFULL_COMPARATOR); 
			}else if(sortByType.equalsIgnoreCase(ProductPromotionData.SORT_BY_PRICE_VIEW)){
				Collections.sort(nonFeatProducts, ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR); 
			}else if(sortByType.equalsIgnoreCase(ProductPromotionData.SORT_BY_PRICE_VIEW_INVERSE)){
				Collections.sort(nonFeatProducts, ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR_INVERSE); 
			}					
		}
		return nonFeatProducts;
	}
	
	public static List<ProductModel> getSortedProducts(List<ProductModel> products, String sortByType){
		if(null != sortByType){
			if(sortByType.equalsIgnoreCase(ProductPromotionData.SORT_BY_DEPT_VIEW)){
				Collections.sort(products, DEPTFULL_COMPARATOR); 
			}else if(sortByType.equalsIgnoreCase(ProductPromotionData.SORT_BY_PRICE_VIEW)){
				Collections.sort(products, ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR); 
			}else if(sortByType.equalsIgnoreCase(ProductPromotionData.SORT_BY_PRICE_VIEW_INVERSE)){
				Collections.sort(products, ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR_INVERSE); 
			}					
		}
		return products;
	}
	
	/**
	 * To sort by product's original parent department.
	 */
	public final static Comparator<ProductModel> DEPTFULL_COMPARATOR = new Comparator<ProductModel>() {

		public int compare(ProductModel p1, ProductModel p2) {
			String p1Dept = "";
			String p2Dept = "";
			
			ProductModel prodModel1 = ((SkuModel) ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey(FDContentTypes.SKU, p1.getDefaultSkuCode()))).getProductModel();
			ProductModel prodModel2 = ((SkuModel) ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey(FDContentTypes.SKU, p2.getDefaultSkuCode()))).getProductModel();
			//check that getDepartment != null (fix for new prods in cmsTest)
			p1Dept = (prodModel1.getDepartment() != null) ? NVL.apply(prodModel1.getDepartment().getFullName(), "") : "";
			p2Dept = (prodModel2.getDepartment() != null) ? NVL.apply(prodModel2.getDepartment().getFullName(), "") : "";
			
			int ret = p1Dept.compareTo(p2Dept);

			if (ret == 0) {
				ret = prodModel1.getFullName().compareTo(prodModel2.getFullName());
			}
			
			return ret;
		}
	};
	public static Map<ZoneInfo, List<FDProductPromotionInfo>> formatProductPromotionPreviewInfo(
			ErpProductPromotionPreviewInfo erpProductPromotionPreviewInfo)
			throws FDResourceException {
		Map<ZoneInfo,List<FDProductPromotionInfo>> productPromotionInfoMap = erpProductPromotionPreviewInfo.getProductPromotionInfoMap();
		Map<ZoneInfo,List<FDProductPromotionInfo>> productPromotionPreviewInfoMap = new HashMap<ZoneInfo,List<FDProductPromotionInfo>>();
		Map<String,ErpProductInfoModel> erpProductInfoMap =  erpProductPromotionPreviewInfo.getErpProductInfoMap();
		if(null !=productPromotionInfoMap){
			for(Iterator itr = productPromotionInfoMap.keySet().iterator();itr.hasNext();){
				ZoneInfo zoneInfo =(ZoneInfo)itr.next();
				List<FDProductPromotionInfo> productPromotionInfoList =(List<FDProductPromotionInfo>)productPromotionInfoMap.get(zoneInfo);
				List<FDProductPromotionInfo> productPromotionPreviewInfoList = (List<FDProductPromotionInfo>)productPromotionPreviewInfoMap.get(zoneInfo);
				if(null ==productPromotionPreviewInfoList){
					productPromotionPreviewInfoList = new ArrayList<FDProductPromotionInfo>();
					productPromotionPreviewInfoMap.put(zoneInfo, productPromotionPreviewInfoList);
				}
				if(null !=productPromotionInfoList){
					for (Iterator iterator = productPromotionInfoList.iterator(); iterator.hasNext();) {
						FDProductPromotionInfo productPromotionInfo = (FDProductPromotionInfo) iterator.next();
						ErpProductInfoModel erpProductInfoModel = null !=erpProductInfoMap?erpProductInfoMap.get(productPromotionInfo.getSkuCode()):null;
						FDProductPromotionPreviewInfo productPromotionPreviewInfo = new FDProductPromotionPreviewInfo(productPromotionInfo);
						FDProductInfo fdProductInfo = null;
						String skuCode = productPromotionInfo.getSkuCode();
						try {
							if(null !=erpProductInfoMap && null!=erpProductInfoModel.getMaterialPrices()){
								fdProductInfo = FDFactory.getProductInfo(erpProductInfoModel);
							}else{
								fdProductInfo = FDFactory.getProductInfo(skuCode);
							}
							productPromotionPreviewInfo.setFdProductInfo(fdProductInfo);
							if(null != fdProductInfo){
								ZonePriceInfoListing zonePriceListing = fdProductInfo.getZonePriceInfoList();
							
								FDProductInfo fdCachedProductInfo =FDCachedFactory.getProductInfo(skuCode);
								FDProduct fdCachedProduct = FDCachedFactory.getProduct(fdCachedProductInfo);
								productPromotionPreviewInfo.setFdProduct(fdCachedProduct);
								if(null ==zonePriceListing.getZonePriceInfo(ZonePriceListing.DEFAULT_ZONE_INFO)){	
									zonePriceListing.addZonePriceInfo(ZonePriceListing.DEFAULT_ZONE_INFO,fdCachedProductInfo.getZonePriceInfo(ZonePriceListing.DEFAULT_ZONE_INFO).clone());									
								}
								
							}
						}catch (FDSkuNotFoundException e) {
							LOGGER.warn(skuCode+ " not found.");
						}
						productPromotionPreviewInfoList.add(productPromotionPreviewInfo);
//						}
						
					}
				}
			}
		}
		return productPromotionPreviewInfoMap;
	}
	
	//Origin : [APPDEV-2857] Blocking Alcohol for customers outside of Alcohol Delivery Area
	private static boolean isUnavailable(ProductModel productModel) {
		return productModel.isUnavailable() || !ContentUtil.isAvailableByContext(productModel);
	}
}
