package com.freshdirect.webapp.taglib.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.fdstore.content.EnumFilteringValue;
import com.freshdirect.fdstore.content.FilteringComparatorUtil;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringSortingItemFilter;
import com.freshdirect.fdstore.content.FilteringSortingMenuBuilder;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.content.GenericFilter;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.GenericFilterValueDecoder;
import com.freshdirect.fdstore.content.GenericFilteringMenuBuilder;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeFilterMenuDecorator;
import com.freshdirect.fdstore.content.RecipeFilterValueDecorator;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.UrlFilterValueDecoder;
import com.freshdirect.fdstore.util.FilteringNavigator;

public class SearchRecipeTag extends FilteringFlow<FilteringSortingItem<Recipe>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5809709785695385027L;
	
	private SearchResults results;
	protected FilteringNavigator nav;
	private Set<EnumFilteringValue> filters;
	{
		filters=new HashSet<EnumFilteringValue>();
		filters.add(EnumFilteringValue.RECIPE_CLASSIFICATION);
	}

	@Override
	protected GenericFilterValueDecoder createDecoder() {
		return new UrlFilterValueDecoder(filters);
	}

	@Override
	protected GenericFilterDecorator<FilteringSortingItem<Recipe>> createFilterValueDecorator() {
		return new RecipeFilterValueDecorator(filters);
	}

	@Override
	protected GenericFilterDecorator<FilteringSortingItem<Recipe>> createMenuValueDecorator() {
		return new RecipeFilterMenuDecorator(filters);
	}

	@Override
	protected GenericFilter createFilter(Map<EnumFilteringValue, List<Object>> filterValues) {
		return new FilteringSortingItemFilter(filterValues, filters);
	}

	@Override
	protected GenericFilteringMenuBuilder createMenuBuilder(Map<EnumFilteringValue, List<Object>> filterValues) {
		return new FilteringSortingMenuBuilder(filterValues, filters);
	}

	@Override
	protected Comparator<FilteringSortingItem<Recipe>> createComparator(List<FilteringSortingItem<Recipe>> items) {
		if(nav.isOrderAscending()){
			return FilteringComparatorUtil.RECIPE_SORT_BY_NAME_ASC;
		}else{
			return FilteringComparatorUtil.RECIPE_SORT_BY_NAME_DESC;
		}
	}

	@Override
	protected List<FilteringSortingItem<Recipe>> getItems() {
		return new ArrayList<FilteringSortingItem<Recipe>>(results.getRecipes());
	}

	@Override
	protected Set<EnumFilteringValue> getFilterEnums() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void postProcess(List<FilteringSortingItem<Recipe>> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void preProcess(List<FilteringSortingItem<Recipe>> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getPageSize() {
		return nav.getPageSize();
	}

	@Override
	protected int getPageOffset() {
		return nav.getPageOffset();
	}

	@Override
	protected int getPageNumber() {
		return nav.getPageNumber();
	}

	public void setResults(SearchResults results) {
		this.results = results;
	}

	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
	}

}
