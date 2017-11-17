package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.ProductFilterModel;

public class AllergenFilter extends NutritionInfoFilter {

	public AllergenFilter(ProductFilterModel model, String parentId) {
		super(model, parentId);
	}

	@Override
	protected ErpNutritionInfoType getType() {
		return ErpNutritionInfoType.ALLERGEN;
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.ERPS;
	}

}
