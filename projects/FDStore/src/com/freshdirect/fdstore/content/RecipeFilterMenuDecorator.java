package com.freshdirect.fdstore.content;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.storeapi.content.Domain;
import com.freshdirect.storeapi.content.DomainValue;
import com.freshdirect.storeapi.content.EnumSearchFilteringValue;
import com.freshdirect.storeapi.content.FilteringMenuItem;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.FilteringValue;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.storeapi.content.RecipeSearchPage;

public class RecipeFilterMenuDecorator extends GenericFilterDecorator<FilteringSortingItem<Recipe>>{

	public RecipeFilterMenuDecorator(Set<FilteringValue> filters) {
		super(filters);
	}

	@Override
	public void decorateItem(FilteringSortingItem<Recipe> item) {
		
		Recipe recipe=item.getNode();
		
		for (FilteringValue filterSource : filters) {
			
			if(!(filterSource instanceof EnumSearchFilteringValue)){
				throw new IllegalArgumentException("Only EnumSearchFilteringValue allowed here.");
			}
			
			EnumSearchFilteringValue filter = (EnumSearchFilteringValue) filterSource;
			
			FilteringMenuItem menu = new FilteringMenuItem();
			Set<FilteringMenuItem> menus = new HashSet<FilteringMenuItem>();
			
			switch (filter) {
			case RECIPE_CLASSIFICATION: {
				RecipeSearchPage recipeSearchPage = RecipeSearchPage.getDefault();
				List<Domain> classificationDomains = recipeSearchPage.getFilterByDomains();
				for(DomainValue dv: recipe.getClassifications()){
					if(classificationDomains.contains(dv.getDomain())){
						menu.setName(dv.getLabel());
						menu.setFilteringUrlValue(dv.getContentKey().getId());
						menu.setFilter(filter);
						menus.add(menu);
						menu=new FilteringMenuItem();						
					}
				}
				item.putMenuValue(EnumSearchFilteringValue.RECIPE_CLASSIFICATION, menus);
			}
			}
		}
		
	}

}
