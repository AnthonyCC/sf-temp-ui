package com.freshdirect.smartstore.impl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.framework.util.DiscreteRandomSampler;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;

/**
 * Recommendations service that mixes "other" recommendations.
 * 
 * This class mixes existing recommendation services according
 * to the specified distribution.
 * 
 * Subclasses should set the actual frequencies in the constructor.
 * 
 * @see DiscreteRandomSampler
 * @author istvan
 *
 */
public abstract class CompositeRecommendationService implements RecommendationService {

	// variant id
	private Variant variant;
	
	// random number generator instance
	private Random random = new Random();
	
	// sampler
	private DiscreteRandomSampler sampler = 
		new DiscreteRandomSampler(
			new Comparator() {

				public int compare(Object o1, Object o2) {
					RecommendationService s1 = (RecommendationService)o1;
					RecommendationService s2 = (RecommendationService)o2;
					
					return s1.getVariant().getServiceConfig().getName().compareTo(s2.getVariant().getServiceConfig().getName());
				}
				
			});
	
	/**
	 * Constructor.
	 * 
	 * @param variant
	 */
	protected CompositeRecommendationService(Variant variant) {
		this.variant = variant;
		init();
	}
	
	
	/**
	 * Any special initialization.
	 * Will be invoked by the constructor.
	 */
	protected abstract void init();
	
	
	/**
	 * Set a service's relative frequency.
	 * @param service service used by the composite
	 * @param frequency relative frequency
	 */
	protected void setServiceFrequencty(RecommendationService service, int frequency) {
		sampler.setValue(service, frequency);
	}
	
	/**
	 * Get variant.
	 * @return variant
	 */
	public Variant getVariant() {
		return variant;
	}
	
	/**
	 * Verbose representation.
	 * For debugging.
	 * @return string rep
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.
			append(getVariant().getId()).
			append(':').
			append(getVariant().getServiceConfig().getName()).
			append(" {");
		for(Iterator i = sampler.getValues().iterator(); i.hasNext();) {
			RecommendationService s = (RecommendationService)i.next();
			buffer.append(s.getVariant().getId()).append(':').append(sampler.getFrequency(s));
			if (i.hasNext()) buffer.append(',');
 		}
		buffer.append('}');
		return buffer.toString();
	}

	/**
	 * Provide recommendations.
	 * 
	 * The method will select one of the services with the established 
	 * probability to derive the actual recommendations.
	 * 
	 * @param max maximum recommendations to return
	 * @param input session information
	 * @return a List<{@link ContentKey}> of recommendations
	 * @see #setServiceFrequencty(RecommendationService, int)
	 */
	public List recommend(int max, SessionInput input) {
		RecommendationService service = (RecommendationService)sampler.getRandomValue(random);
		return service.recommend(max, input);
	}
}
