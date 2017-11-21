package com.freshdirect.storeapi.content;

import java.util.List;
import java.util.Map;

public class ProductPromotionData {
	
	public static final String SORT_BY_FAV_VIEW="SORT_BY_FAV_VIEW";
	public static final String SORT_BY_DEPT_VIEW="SORT_BY_DEPT_VIEW";
	public static final String SORT_BY_PRICE_VIEW="SORT_BY_PRICE_VIEW";
	public static final String SORT_BY_PRICE_VIEW_INVERSE="SORT_BY_PRICE_VIEW_INVERSE";
	public static final String FEATURED="FEATURED";
	public static final String NON_FEATURED="NON_FEATURED";
	public static final String DEFAULT_ZONE="0000100000";

	private List<ProductModel> productModels;
	private Map<String,Map<String, List<ProductModel>>> zoneProductModelsMap;
	private Map<String,ProductModel> skuProductMap;
	private String productPromotitonType;
	private String promotionId;
//	private Set<String> skus;
	

//	public Set<String> getSkus() {
//		return skus;
//	}
//	public void setSkus(Set<String> skus) {
//		this.skus = skus;
//	}
	public List<ProductModel> getProductModels() {
		return productModels;
	}
	public void setProductModels(List<ProductModel> productModels) {
		this.productModels = productModels;
	}
	public Map<String, ProductModel> getSkuProductMap() {
		return skuProductMap;
	}
	public void setSkuProductMap(Map<String, ProductModel> skuProductMap) {
		this.skuProductMap = skuProductMap;
	}
	
	public Map<String, Map<String, List<ProductModel>>> getZoneProductModelsMap() {
		return zoneProductModelsMap;
	}
	public void setZoneProductModelsMap(
			Map<String, Map<String, List<ProductModel>>> zoneProductModelsMap) {
		this.zoneProductModelsMap = zoneProductModelsMap;
	}
	public String getProductPromotitonType() {
		return productPromotitonType;
	}
	public void setProductPromotitonType(String productPromotitonType) {
		this.productPromotitonType = productPromotitonType;
	}
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	
	
	
}
