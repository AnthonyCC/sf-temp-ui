package com.freshdirect.smartstore;

import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.content.ContentNodeModel;


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
	 * Recommend a list of {@link ContentNodeModel}s.
	 * @param input session information.
	 * 
	 * 
	 * @return a List<{@link ContentNodeModel}> of recommendations, expected to be sorted by relevance
	 */
	public List recommendNodes(SessionInput input);
	
	public String getDescription();
	
	/**
	 * Returns the active configuration of the service
	 */
	public Map getConfiguration();
}
