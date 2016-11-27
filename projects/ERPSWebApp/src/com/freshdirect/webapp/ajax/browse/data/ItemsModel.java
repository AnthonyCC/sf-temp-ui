package com.freshdirect.webapp.ajax.browse.data;

import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ProductModel;

public class ItemsModel {
	
	public static String DEFAULT_GROUP_NAME = "default";
	
	private List<CategoryModel> regularSubCategories;
	private List<CategoryModel> preferenceSubCategories; 
	private Map<CategoryData,Map<CategoryData,List<ProductModel>>> twoLevelGroupedProducts;
	
	
	public List<CategoryModel> getRegularSubCategories() {
		return regularSubCategories;
	}
	public void setRegularSubCategories(List<CategoryModel> regularSubCategories) {
		this.regularSubCategories = regularSubCategories;
	}
	public List<CategoryModel> getPreferenceSubCategories() {
		return preferenceSubCategories;
	}
	public void setPreferenceSubCategories(
			List<CategoryModel> preferenceSubCategories) {
		this.preferenceSubCategories = preferenceSubCategories;
	}
	public Map<CategoryData, Map<CategoryData, List<ProductModel>>> getTwoLevelGroupedProducts() {
		return twoLevelGroupedProducts;
	}
	public void setTwoLevelGroupedProducts(Map<CategoryData, Map<CategoryData, List<ProductModel>>> twoLevelGroupedProducts) {
		this.twoLevelGroupedProducts = twoLevelGroupedProducts;
	}

}
