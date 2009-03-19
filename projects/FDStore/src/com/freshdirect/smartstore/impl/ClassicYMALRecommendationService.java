package com.freshdirect.smartstore.impl;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Trigger;
import com.freshdirect.smartstore.Variant;

/**
 * @author csongor
 */
public class ClassicYMALRecommendationService extends AbstractRecommendationService {
	private static final Category LOGGER = LoggerFactory.getInstance(ClassicYMALRecommendationService.class);
	
	public ClassicYMALRecommendationService(Variant variant) {
		super(variant);
	}

	/**
	 * Recommends products for the current node
	 */
	public List recommendNodes(Trigger trigger, SessionInput input) {
		List prodList;

		if (input.getYmalSource() != null) {
			prodList = input.getYmalSource().getYmalProducts();
			clearConfiguredProductCache();
			prodList = addConfiguredProductToCache(prodList);
		} else {
			prodList = Collections.EMPTY_LIST;
			LOGGER.info("ymal source is null: returning empty list");
		}

		return prodList;
	}
}
