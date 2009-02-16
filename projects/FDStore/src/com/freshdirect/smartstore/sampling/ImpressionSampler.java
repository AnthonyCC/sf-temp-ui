package com.freshdirect.smartstore.sampling;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.smartstore.impl.IConfigurable;

/**
 * Sampler that produces the Content Keys for impressions.
 * 
 * @author istvan
 *
 */
public interface ImpressionSampler extends IConfigurable {
	
	/**
	 * Produce content keys for impressions by sampling.
	 * 
	 * @param sortedRankedContent choose from these (List<{@link RankedContent}>)
	 * @param reserved these cannot be chosen (e.g. cart), type Set<{@link ContentKey}> 
	 * @param k number of impressions to produce
	 * @return List<{@link ContentKey}>
	 */
	public List sample(List sortedRankedContent, Set reserved, int k);

	/**
	 * Append active configuration entries to the given map
	 * @param configMap
	 * @return
	 */
	public Map appendConfiguration(Map configMap);
}
