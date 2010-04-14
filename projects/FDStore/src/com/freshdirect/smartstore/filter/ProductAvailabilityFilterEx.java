package com.freshdirect.smartstore.filter;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Check whether product or one of its alternatives is available
 * 
 * @author segabor
 * 
 */
public class ProductAvailabilityFilterEx extends ProductAvailabilityFilter {
	private static final Logger LOGGER = LoggerFactory.getInstance(ProductAvailabilityFilterEx.class);

	@Override
	public ProductModel filter(ProductModel model) {
		if (model == null || available(model)) {
			return model;
		}

		for (ContentNodeModel alternativeModel : model.getRecommendedAlternatives()) {
			if (alternativeModel instanceof ProductModel && available((ProductModel) alternativeModel)) {
				LOGGER.debug("substituted: " + model.getContentKey());
				return (ProductModel) alternativeModel;
			} else if (alternativeModel instanceof SkuModel) {
				return filter((ProductModel) alternativeModel.getParentNode());
			}
		}
		return null;
	}
}
