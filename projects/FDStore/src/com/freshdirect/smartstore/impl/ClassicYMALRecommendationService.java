package com.freshdirect.smartstore.impl;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;
import com.freshdirect.smartstore.sampling.ImpressionSampler;

/**
 * @author csongor
 */
public class ClassicYMALRecommendationService extends AbstractRecommendationService {
	private static final Category LOGGER = LoggerFactory.getInstance(ClassicYMALRecommendationService.class);
	
	public ClassicYMALRecommendationService(Variant variant, ImpressionSampler sampler,
    		boolean catAggr, boolean includeCartItems) {
		super(variant, sampler, catAggr, includeCartItems);
	}

	/**
	 * Recommends products for the current node
	 */
	public List recommendNodes(SessionInput input) {
		List prodList;

		if (input.getYmalSource() != null) {
			prodList = input.getYmalSource().getYmalProducts();
			SmartStoreUtil.clearConfiguredProductCache();
			prodList = SmartStoreUtil.addConfiguredProductToCache(prodList);
		} else {
			prodList = Collections.EMPTY_LIST;
			LOGGER.info("ymal source is null: returning empty list");
		}

		return prodList;
	}
}
