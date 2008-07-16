package com.freshdirect.smartstore;

import java.util.List;

import com.freshdirect.cms.ContentKey;


/**
 * Recommendation service provider interface.
 * @author istvan
 */
public interface RecommendationService {
	
	
	/**
	 * Get the variant representing this service.
	 * @return variant
	 */
	public Variant getVariant();
	
	/**
	 * Recommend a list of {@link ContentKey}s.
	 * 
	 * 
	 * @param input session information.
	 * @param max the maximum number of recommendations to be produced
	 * @return a List<{@link ContentKey}> of recommendations, expected to be sorted by relevance
	 */
	public List recommend(int max, SessionInput input);

}
