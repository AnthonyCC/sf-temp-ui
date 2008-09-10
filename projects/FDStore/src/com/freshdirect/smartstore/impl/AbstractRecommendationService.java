package com.freshdirect.smartstore.impl;

import java.util.List;

import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;

/**
 * Simple abstract implementation of recommendation service
 * It does nothing but store it's variant ID
 * 
 * Subclasses must implement recommend() method.
 * 
 * @author segabor
 *
 */
public abstract class AbstractRecommendationService implements RecommendationService {
	protected Variant variant;
	
	public AbstractRecommendationService(Variant variant) {
		// TODO: not-null check
		this.variant = variant;
	}
	
	public Variant getVariant() {
		return this.variant;
	}

	abstract public List recommend(int max, SessionInput input);


	public String toString() {
		return variant.getSiteFeature().getName() + "/" + variant.getId();
	}
}
