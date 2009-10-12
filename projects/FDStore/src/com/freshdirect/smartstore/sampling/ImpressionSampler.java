package com.freshdirect.smartstore.sampling;

import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;

/**
 * Sampler that produces the Content Keys for impressions.
 * 
 * @author istvan
 *
 */
public interface ImpressionSampler {
	
	/**
	 * Produce content keys for impressions by sampling.
	 * 
	 * @param sortedRankedContent choose from these (List<{@link RankedContent}>)
	 * @param reserved these cannot be chosen (e.g. cart), type Set<{@link ContentKey}> 
	 * @param k number of impressions to produce
	 * @return List<{@link ContentKey}>
	 */
	public List<RankedContent.Single> sample(List<RankedContent> sortedRankedContent, Set<ContentKey> reserved, int k);
	
	public boolean isDeterministic();
}
