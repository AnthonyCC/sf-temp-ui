package com.freshdirect.mobileapi.catalog.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Lookup;
import com.freshdirect.mobileapi.controller.data.response.FilterGroup;
import com.freshdirect.mobileapi.util.SortType;

public class SortOptionInfo {
	
	private List<SortType> sortOptions = new ArrayList<SortType>();
	private List<String> nutritionSortOptions = new ArrayList<String>();
	private List<Lookup> sortOptionsLookup = new ArrayList<Lookup>();
	private List<FilterGroup> filterGroups = new ArrayList<FilterGroup>();
	
	
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
	public List<Lookup> getSortOptionsLookup() {
		return sortOptionsLookup;
	}
	public void setSortOptionsLookup(List<Lookup> sortOptionsLookup) {
		this.sortOptionsLookup = sortOptionsLookup;
	}
	
	public List<FilterGroup> getFilterGroups() {
		return filterGroups;
	}
	public void setFilterGroups(List<FilterGroup> filterGroups) {
		this.filterGroups = filterGroups;
	}
	
	public void addFilterGroup(FilterGroup fg){
		this.filterGroups.add(fg);
	}
	
	
	
}
