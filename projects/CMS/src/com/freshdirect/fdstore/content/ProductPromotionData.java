package com.freshdirect.fdstore.content;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	
}
