/**
 * 
 */
package com.freshdirect.smartstore.sampling;

import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.smartstore.sampling.ContentSampler.ConsiderationLimit;


public class ComplicatedImpressionSampler implements
		ImpressionSampler {
	private final ConsiderationLimit cl;

	public ComplicatedImpressionSampler(ConsiderationLimit cl) {
		this.cl = cl;
	}

	
	@Override
	public List<RankedContent.Single> sample(List<RankedContent> sortedRankedContent, Set<ContentKey> reserved, int k) {
            return ContentSampler.drawWithoutReplacement(
                    sortedRankedContent, reserved, cl, k);
	}

	public String toString() {
		return "complicated";
	}
	
	public boolean isDeterministic() {
		return false;
	}
}