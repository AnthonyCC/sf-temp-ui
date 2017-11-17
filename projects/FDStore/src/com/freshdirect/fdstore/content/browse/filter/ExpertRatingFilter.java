package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductFilterModel;
import com.freshdirect.storeapi.content.ProductModel;

public class ExpertRatingFilter extends AbstractRangeFilter {

	public ExpertRatingFilter(ProductFilterModel model, String parentId) {
		super(model, parentId);
	}

	@Override
	public boolean apply(FilteringProductItem ctx) throws FDResourceException {
		if (ctx == null || ctx.getProductModel() == null) {
			return false;
		}

		ProductModel node = ctx.getProductModel();
		EnumOrderLineRating rating = node.getProductRatingEnum();
		if (rating.getValue() > 0) {
			final double val = rating.getValue()/2.0;

			return invertChecker(isWithinRange(val));
		}

				
		return invertChecker(false);
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.ERPS;
	}

}
