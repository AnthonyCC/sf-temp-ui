package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.content.FilterCacheStrategy;
import com.freshdirect.fdstore.content.ProductFilterModel;

public class OrganicFilter extends NutritionInfoFilter {

	public OrganicFilter(ProductFilterModel model, String parentId) {
		super(model, parentId);
	}

	@Override
	protected ErpNutritionInfoType getType() {
		return ErpNutritionInfoType.ORGANIC;
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.ERPS;
	}

}
