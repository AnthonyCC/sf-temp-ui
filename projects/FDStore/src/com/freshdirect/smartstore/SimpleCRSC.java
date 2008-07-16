package com.freshdirect.smartstore;

import java.io.Serializable;




/**
 * Simple realization of abstract {@link CompositeRecommendationServiceConfig} class
 * 
 * @author segabor
 *
 */
public class SimpleCRSC extends CompositeRecommendationServiceConfig implements Serializable {
	private static final long serialVersionUID = 8786111758591787466L;

	public SimpleCRSC(String name) {
		super(name);
	}


	protected void init() {
		// does nothing
		// to add parts call addPart() externally 
	}

	public void addPart(RecommendationServiceConfig partConfig, int frequency) {
		super.addPart(partConfig, frequency);
	}
}
