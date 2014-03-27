package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.FilterCacheStrategy;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.ProductFilterModel;

public class SustainabilityRatingFilter extends AbstractRangeFilter {

	public SustainabilityRatingFilter(ProductFilterModel model, String parentId) {
		super(model, parentId);
	}

	@Override
	public boolean apply(FilteringProductItem ctx) throws FDResourceException {
		if (ctx == null || ctx.getProductModel() == null) {
			return false;
		}

		FDProductInfo productInfo = ctx.getFdProductInfo();
		EnumSustainabilityRating sustainabilityRating = productInfo.getSustainabilityRating();
		if (sustainabilityRating != null && sustainabilityRating.getId() >= 2) {
			return invertChecker(isWithinRange( sustainabilityRating.getId() ));
		}
		
		
		return invertChecker(false);
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.ERPS;
	}

}
