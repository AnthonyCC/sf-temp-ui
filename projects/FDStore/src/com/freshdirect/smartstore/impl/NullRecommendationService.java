package com.freshdirect.smartstore.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Trigger;
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

	public List recommendNodes(Trigger trigger, SessionInput input) {
		return Collections.EMPTY_LIST;
	}

	public String toString() {
            return "Service(feature:"+variant.getSiteFeature().getName() +
            ",variant:" + variant.getId()+
            ",class:"+StringUtil.getSimpleName(this.getClass())+")";
	}
	
	public String getDescription() {
		return "";
	}

	public Map getConfiguration() {
		return Collections.EMPTY_MAP;
	}
}
