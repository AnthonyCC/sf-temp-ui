/**
 * 
 */
package com.freshdirect.smartstore.sampling;

import java.util.List;
import java.util.Set;

import com.freshdirect.smartstore.sampling.ContentSampler.ConsiderationLimit;


public class ComplicatedImpressionSampler implements
		ImpressionSampler {
	private final ConsiderationLimit cl;

	public ComplicatedImpressionSampler(ConsiderationLimit cl) {
		this.cl = cl;
	}

	public List sample(List sortedRankedContent, Set reserved, int k) {
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