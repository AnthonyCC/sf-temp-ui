package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.FilterCacheStrategy;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.ProductFilterModel;
import com.freshdirect.fdstore.customer.FDUserI;

public class SustainabilityRatingFilter extends AbstractRangeFilter {

	private FDUserI user;
	public SustainabilityRatingFilter(ProductFilterModel model, String parentId, FDUserI user) {
		super(model, parentId);
		this.user = user;
	}

	@Override
	public boolean apply(FilteringProductItem productItem) throws FDResourceException {
		if (productItem == null || productItem.getProductModel() == null || productItem.getProductModel().isUnavailable()) {
			return false;
		}
        
		FDProductInfo productInfo = productItem.getFdProductInfo();
		EnumSustainabilityRating sustainabilityRating = productInfo.getSustainabilityRating(user.getUserContext().getFulfillmentContext().getPlantId());
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
