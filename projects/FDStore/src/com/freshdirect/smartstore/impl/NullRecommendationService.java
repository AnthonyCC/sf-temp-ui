package com.freshdirect.smartstore.impl;

import java.util.Collections;
import java.util.List;

import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;

/**
 * A service that always returns the empty list.

 *
 */
public class NullRecommendationService implements RecommendationService {
	

	private Variant variant;
	
	public Variant getVariant() {
		return variant;
	}
	
	public NullRecommendationService(Variant variant) {
		this.variant = variant;
	}

	public List recommend(int max, SessionInput input) {
		return Collections.EMPTY_LIST;
	}

}
