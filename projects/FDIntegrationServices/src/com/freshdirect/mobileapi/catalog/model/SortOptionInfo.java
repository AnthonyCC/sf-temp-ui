package com.freshdirect.mobileapi.catalog.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.mobileapi.util.SortType;

public class SortOptionInfo {
	
	private List<SortType> sortOptions = new ArrayList<SortType>();
	private List<String> nutritionSortOptions = new ArrayList<String>();
	
	public List<SortType> getSortOptions() {
		return sortOptions;
	}
	public void setSortOptions(List<SortType> sortOptions) {
		this.sortOptions = sortOptions;
	}
	public List<String> getNutritionSortOptions() {
		return nutritionSortOptions;
	}
	public void setNutritionSortOptions(List<String> list) {
		this.nutritionSortOptions = list;
	}
	
	
	
}
