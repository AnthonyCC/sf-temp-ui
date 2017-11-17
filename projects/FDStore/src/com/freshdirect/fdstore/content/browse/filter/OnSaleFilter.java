package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.PriceCalculator;
import com.freshdirect.storeapi.content.ProductFilterModel;

public class OnSaleFilter extends AbstractProductItemFilter {
	public OnSaleFilter(ProductFilterModel model, String parentId) {
		super(model, parentId);
	}

	public OnSaleFilter(String id, String parentId, String name) { //'virtual' onsaleFilter for search page 
		super(id, parentId, name);
	}
	
	@Override
	public boolean apply(FilteringProductItem ctx) throws FDResourceException {
		if (ctx == null || ctx.getProductModel() == null) {
			return false;
		}

		final PriceCalculator pricing = ctx.getProductModel().getPriceCalculator(ContentFactory.getInstance().getCurrentUserContext().getPricingContext());

		if (pricing.getDealPercentage() > 0 || pricing.getTieredDealPercentage() > 0 || pricing.getGroupPrice() != 0.0) {
			return invertChecker(true);
		}
		
		return invertChecker(false);
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.ERPS_PRICING_ZONE;
	}

}
