package com.freshdirect.fdstore.content;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeFilterMenuDecorator extends GenericFilterDecorator<FilteringSortingItem<Recipe>>{

	public RecipeFilterMenuDecorator(Set<EnumFilteringValue> filters) {
		super(filters);
	}

	@Override
	public void decorateItem(FilteringSortingItem<Recipe> item) {
		
		Recipe recipe=item.getNode();
		
		for (EnumFilteringValue filter : filters) {
			
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
				item.putMenuValue(EnumFilteringValue.RECIPE_CLASSIFICATION, menus);
			}
			}
		}
		
	}

}
