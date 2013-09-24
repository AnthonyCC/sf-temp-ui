package com.freshdirect.webapp.taglib.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.content.EnumSearchFilteringValue;
import com.freshdirect.fdstore.content.FilteringComparatorUtil;
import com.freshdirect.fdstore.content.FilteringFlow;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.GenericFilteringMenuBuilder;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeFilterMenuDecorator;
import com.freshdirect.fdstore.content.RecipeFilterValueDecorator;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.util.FilteringNavigator;

public class RecipeFilterImpl extends FilteringFlow<Recipe>{
	
	private SearchResults results;
	private FilteringNavigator nav;
	
	private Set<FilteringValue> filters;
	{
		filters=new HashSet<FilteringValue>();
		filters.add(EnumSearchFilteringValue.RECIPE_CLASSIFICATION);
	}

	public RecipeFilterImpl(SearchResults results, FilteringNavigator nav) {
		super();
		this.results = results;
		this.nav = nav;
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
	protected Comparator<FilteringSortingItem<Recipe>> createComparator(List<FilteringSortingItem<Recipe>> items) {
		if(nav.isOrderAscending()){
			return FilteringComparatorUtil.RECIPE_SORT_BY_NAME_ASC;
		}else{
			return FilteringComparatorUtil.RECIPE_SORT_BY_NAME_DESC;
		}
	}
	
	protected List<FilteringSortingItem<Recipe>> reOrganizeFavourites(List<FilteringSortingItem<Recipe>> items) {
		return items;//Nothing to do here, only has product filter relevances
	}

	@Override
	protected List<FilteringSortingItem<Recipe>> getItems() {
		return new ArrayList<FilteringSortingItem<Recipe>>(results.getRecipes());
	}

	@Override
	protected Set<FilteringValue> getFilterEnums() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void postProcess(List<FilteringSortingItem<Recipe>> items, GenericFilteringMenuBuilder<FilteringSortingItem<Recipe>> menuBuilder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void preProcess(List<FilteringSortingItem<Recipe>> items) {
		// TODO Auto-generated method stub
		
	}

	public void setResults(SearchResults results) {
		this.results = results;
	}

	@Override
	protected Set<FilteringValue> getFilters() {
		return filters;
	}

	@Override
	protected void midProcess(List<FilteringSortingItem<Recipe>> items) {
		// TODO Auto-generated method stub
		
	}

}
