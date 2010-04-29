package com.freshdirect.smartstore.filter;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * check availabilities light version (no available alternatives check)
 * 
 * @author zsombor
 * 
 */
public class ProductAvailabilityFilter extends ContentFilter {
	private static final Logger LOGGER = LoggerFactory.getInstance(ProductAvailabilityFilter.class);

	/**
	 * It calls {@link #filter(ProductModel)}.
	 * 
	 * Not overridable.
	 * 
	 * @param key
	 * @return null if filter applies, <i>key</i> otherwise
	 */
	final public ContentKey filter(ContentKey key) {
		ProductModel model = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(key);

		ProductModel filtered = filter(model);
		if (filtered != null) {
			return filtered.getContentKey();
		} else {
			LOGGER.debug("not available: " + key);
			return null;
		}
	}

	/**
	 * Override this!
	 * 
	 * @param model
	 *            can be null
	 * @return null if filter applies, <i>model</i> otherwise
	 */
	protected ProductModel filter(ProductModel model) {
		if (model == null || available(model)) {
			return model;
		} else {
			return null;
		}
	}

	/**
	 * @param model
	 * @return if the product is available and not excluded from recommendations
	 */
	final protected boolean available(ProductModel model) {
		return model.isDisplayable() && !model.isExcludedRecommendation();
	}
}
