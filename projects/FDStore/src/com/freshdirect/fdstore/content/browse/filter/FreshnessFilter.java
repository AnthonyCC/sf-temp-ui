package com.freshdirect.fdstore.content.browse.filter;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductFilterModel;
import com.freshdirect.storeapi.util.ProductInfoUtil;

public class FreshnessFilter extends AbstractRangeFilter {
	private static final Logger LOGGER = LoggerFactory.getInstance( FreshnessFilter.class ); 

	public FreshnessFilter(ProductFilterModel model, String parentId) {
		super(model, parentId);
	}

	@Override
	public boolean apply(FilteringProductItem productItem) throws FDResourceException {
		if (productItem == null || productItem.getProductModel() == null || productItem.getProductModel().isUnavailable()) {
			return false;
		}
		//String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
		String plantID=ProductInfoUtil.getPickingPlantId(productItem.getFdProductInfo());
		// Perishable product - freshness warranty
		FDProductInfo productInfo = productItem.getFdProductInfo();
		if (productInfo.getFreshness(plantID) != null) {
			// method above returns either a positive integer encoded in string
			// or null
			try {
				final double value = Integer.parseInt(productInfo.getFreshness(plantID));
				return invertChecker(isWithinRange(value));
			} catch (NumberFormatException exc) {
				LOGGER.error("Failed to parse freshness value " + productInfo.getFreshness(plantID), exc);
				return false;
			}
		}

		return invertChecker(false);
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.ERPS;
	}

}
