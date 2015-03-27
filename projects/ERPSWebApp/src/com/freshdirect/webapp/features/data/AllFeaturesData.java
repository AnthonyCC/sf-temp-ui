package com.freshdirect.webapp.features.data;

import java.util.Collection;
import java.util.Map;

public class AllFeaturesData {

	private Map<String, Collection<FeatureData>> features;

	public Map<String, Collection<FeatureData>> getVersions() {
		return features;
	}

	public void setVersions(Map<String, Collection<FeatureData>> features) {
		this.features = features;
	}
}