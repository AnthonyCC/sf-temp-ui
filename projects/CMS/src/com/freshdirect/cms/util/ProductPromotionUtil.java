package com.freshdirect.cms.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
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
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductPromotionData;
import com.freshdirect.fdstore.content.SkuModel;
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
										productModel = ((SkuModel) ContentFactory.getInstance().getContentNodeByKey(new ContentKey(FDContentTypes.SKU, skuCode))).getProductModel();
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
		List<ProductModel> featProducts = new ArrayList<ProductModel>();
		if(null != products){
			for (Iterator iterator = products.iterator(); iterator.hasNext();) {
				ProductModel productModel = (ProductModel) iterator.next();
				if(isPreview ||(!isPreview &&!productModel.isUnavailable()))
				if (productModel instanceof ProductModelPromotionAdapter) {
					if(((ProductModelPromotionAdapter)productModel).isFeatured() ){
						featProducts.add(productModel);
					}					
				}				
				
			}
		}
		return featProducts;
	}
	
	public static List<ProductModel> getNonFeaturedProducts(List<ProductModel> products,boolean isPreview){
		List<ProductModel> nonFeatProducts = new ArrayList<ProductModel>();
		if(null != products){
			for (Iterator iterator = products.iterator(); iterator.hasNext();) {
				ProductModel productModel = (ProductModel) iterator.next();
				if(isPreview ||(!isPreview &&!productModel.isUnavailable())){
					if (productModel instanceof ProductModelPromotionAdapter) {
						if(!((ProductModelPromotionAdapter)productModel).isFeatured() ){
							nonFeatProducts.add(productModel);
						}					
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
				Collections.sort(nonFeatProducts, ProductModel.DEPTFULL_COMPARATOR); 
			}else if(sortByType.equalsIgnoreCase(ProductPromotionData.SORT_BY_PRICE_VIEW)){
				Collections.sort(nonFeatProducts, ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR); 
			}else if(sortByType.equalsIgnoreCase(ProductPromotionData.SORT_BY_PRICE_VIEW_INVERSE)){
				Collections.sort(nonFeatProducts, ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR_INVERSE); 
			}					
		}
		return nonFeatProducts;
	}
	
	public static Map<String, List<FDProductPromotionInfo>> formatProductPromotionPreviewInfo(
			ErpProductPromotionPreviewInfo erpProductPromotionPreviewInfo)
			throws FDResourceException {
		Map<String,List<FDProductPromotionInfo>> productPromotionInfoMap = erpProductPromotionPreviewInfo.getProductPromotionInfoMap();
		Map<String,List<FDProductPromotionInfo>> productPromotionPreviewInfoMap = new HashMap<String,List<FDProductPromotionInfo>>();
		Map<String,ErpProductInfoModel> erpProductInfoMap =  erpProductPromotionPreviewInfo.getErpProductInfoMap();
		if(null !=productPromotionInfoMap && null !=erpProductInfoMap){
			for(Iterator itr = productPromotionInfoMap.keySet().iterator();itr.hasNext();){
				String zoneCode =(String)itr.next();
				List<FDProductPromotionInfo> productPromotionInfoList =(List<FDProductPromotionInfo>)productPromotionInfoMap.get(zoneCode);
				List<FDProductPromotionInfo> productPromotionPreviewInfoList = (List<FDProductPromotionInfo>)productPromotionPreviewInfoMap.get(zoneCode);
				if(null ==productPromotionPreviewInfoList){
					productPromotionPreviewInfoList = new ArrayList<FDProductPromotionInfo>();
					productPromotionPreviewInfoMap.put(zoneCode, productPromotionPreviewInfoList);
				}
				if(null !=productPromotionInfoList){
					for (Iterator iterator = productPromotionInfoList.iterator(); iterator.hasNext();) {
						FDProductPromotionInfo productPromotionInfo = (FDProductPromotionInfo) iterator.next();
						ErpProductInfoModel erpProductInfoModel = erpProductInfoMap.get(productPromotionInfo.getSkuCode());
						FDProductPromotionPreviewInfo productPromotionPreviewInfo = new FDProductPromotionPreviewInfo(productPromotionInfo);
						if(null !=erpProductInfoModel.getMaterialPrices()){
							FDProductInfo fdProductInfo = FDFactory.getProductInfo(erpProductInfoModel);
							productPromotionPreviewInfo.setFdProductInfo(fdProductInfo);
							if(null != fdProductInfo){
								ZonePriceInfoListing zonePriceListing = fdProductInfo.getZonePriceInfoList();
								try {
									FDProductInfo fdCachedProductInfo =FDCachedFactory.getProductInfo(erpProductInfoModel.getSkuCode());
									FDProduct fdCachedProduct = FDCachedFactory.getProduct(fdCachedProductInfo);
									productPromotionPreviewInfo.setFdProduct(fdCachedProduct);
									if(null ==zonePriceListing.getZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE)){	
										zonePriceListing.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE,fdCachedProductInfo.getZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE).clone());									
									}
								}catch (FDSkuNotFoundException e) {
									LOGGER.warn(erpProductInfoModel.getSkuCode()+ " not found.");
								}
							}
							
							productPromotionPreviewInfoList.add(productPromotionPreviewInfo);
						}
						
					}
				}
			}
		}
		return productPromotionPreviewInfoMap;
	}
}
