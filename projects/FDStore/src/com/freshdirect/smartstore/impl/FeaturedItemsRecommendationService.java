/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.Collections;
import java.util.List;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.scoring.HelperFunctions;

/**
 * 
 * @author zsombor
 */
public class FeaturedItemsRecommendationService extends AbstractRecommendationService {
	/**
	 * @param variant
	 */
	public FeaturedItemsRecommendationService(Variant variant, ImpressionSampler sampler, boolean includeCartItems) {
		super(variant, sampler, includeCartItems);
	}

	/**
	 * 
	 * @param input
	 * @return a List<{@link ContentNodeModel}> of recommendations
	 * 
	 */
	public List doRecommendNodes(SessionInput input) {
		List featuredNodes = Collections.EMPTY_LIST;
		if (input.getCurrentNode() != null) {
			ContentNodeModel model = input.getCurrentNode();

			featuredNodes = getFeaturedItems(model);
			featuredNodes = sample(input, rankListByOrder(featuredNodes), false);
		}

		return featuredNodes;
	}

	/**
	 * Return a list of featured items.
	 * 
	 * @param model
	 * @return
	 */
	public static List getFeaturedItems(ContentNodeModel model) {
		return HelperFunctions.getFeaturedItems(model);
	}
}
