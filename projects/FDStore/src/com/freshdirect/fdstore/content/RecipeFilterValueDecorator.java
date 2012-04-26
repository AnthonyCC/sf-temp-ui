package com.freshdirect.fdstore.content;

import java.util.HashSet;
import java.util.Set;

public class RecipeFilterValueDecorator extends GenericFilterDecorator<FilteringSortingItem<Recipe>> {

	public RecipeFilterValueDecorator(Set<EnumFilteringValue> filters) {
		super(filters);
	}

	@Override
	public void decorateItem(FilteringSortingItem<Recipe> item) {
		
		Recipe recipe=item.getNode();
		
		for (EnumFilteringValue filter : filters) {
			switch (filter) {
			case RECIPE_CLASSIFICATION: {
				Set<String> cl=new HashSet<String>();
				for(DomainValue dv: recipe.getClassifications()){
					cl.add(dv.getContentKey().getId());
				}
				item.putFilteringValue(filter, cl);
			}
			}
		}
		
	}

}
