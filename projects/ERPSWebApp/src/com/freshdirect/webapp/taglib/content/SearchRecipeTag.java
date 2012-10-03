package com.freshdirect.webapp.taglib.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.content.EnumFilteringValue;
import com.freshdirect.fdstore.content.FilteringComparatorUtil;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeFilterMenuDecorator;
import com.freshdirect.fdstore.content.RecipeFilterValueDecorator;
import com.freshdirect.fdstore.content.SearchResults;

public class SearchRecipeTag extends FilteringFlow<Recipe>{

	private static final long serialVersionUID = 5809709785695385027L;
	
	private SearchResults results;
	private Set<EnumFilteringValue> filters;
	{
		filters=new HashSet<EnumFilteringValue>();
		filters.add(EnumFilteringValue.RECIPE_CLASSIFICATION);
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

	public void setResults(SearchResults results) {
		this.results = results;
	}

	@Override
	protected Set<EnumFilteringValue> getFilters() {
		return filters;
	}

}
