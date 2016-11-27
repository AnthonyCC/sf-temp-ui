package com.freshdirect.webapp.features.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.features.data.AllFeaturesData;
import com.freshdirect.webapp.features.data.FeatureData;

public class FeaturesService {

	private static final Logger LOGGER = LoggerFactory.getInstance(FeaturesService.class);

	private static final FeaturesService INSTANCE = new FeaturesService();

	private static final String FEATURES_COOKIE_NAME = "features";

	private FeaturesService() {
	}

	public static FeaturesService defaultService() {
		return INSTANCE;
	}

	public Map<String, String> getActiveFeaturesMapped(Cookie[] cookies, FDUserI user) {
		Set<EnumRolloutFeature> result = getActiveFeatures(cookies, user);
		return getFeatures(result);
	}

	public AllFeaturesData getAllFeatures(Cookie[] cookies, FDUserI user) {
		Map<String, Collection<FeatureData>> allFeatures = new HashMap<String, Collection<FeatureData>>();
		for (EnumRolloutFeature feature : EnumRolloutFeature.values()) {
			FeatureData featureData = createFeatureData(feature, cookies, user);
			Set<FeatureData> featureVersions = (Set<FeatureData>) allFeatures.get(featureData.getName());
			if (featureVersions == null) {
				featureVersions = new HashSet<FeatureData>();
				allFeatures.put(featureData.getName(), featureVersions);
			}
			featureVersions.add(featureData);
		}
		AllFeaturesData allFeaturesData = new AllFeaturesData();
		allFeaturesData.setVersions(allFeatures);
		return allFeaturesData;
	}

	public boolean isFeatureActive(EnumRolloutFeature feature, Cookie[] cookies, FDUserI user) {
		boolean result = false;
		Set<EnumRolloutFeature> activeFeatures = getActiveFeatures(cookies, user);
		if (activeFeatures != null) {
			result = activeFeatures.contains(feature);
		}
		return result;
	}

	public EnumRolloutFeature parseFeatureName(String featureName) {
		EnumRolloutFeature result = null;
		try {
			result = EnumRolloutFeature.valueOf(featureName);
		} catch (Exception exception) {
			LOGGER.warn("Invalid feature name: " + featureName);
		}
		return result;
	}

	private FeatureData createFeatureData(EnumRolloutFeature feature, Cookie[] cookies, FDUserI user) {
		String featureName = feature.getCookieName();
		String featureVersion = feature.getCookieVersion();
		Set<EnumRolloutFeature> featuresFromCookie = loadFeaturesFromCookie(cookies);
		boolean enabledInProperty = FeatureRolloutArbiter.isFeatureRolledOut(feature, user);
		boolean enabledInCookie = featuresFromCookie.contains(feature);
		boolean active = isFeatureActive(feature, cookies, user);
		FeatureData featureData = new FeatureData();
		featureData.setName(featureName);
		featureData.setVersion(featureVersion);
		featureData.setEnabledInProperty(enabledInProperty);
		featureData.setEnabledInCookie(enabledInCookie);
		featureData.setActive(active);
		return featureData;
	}

	private Set<EnumRolloutFeature> getActiveFeatures(Cookie[] cookies, FDUserI user) {
		Set<EnumRolloutFeature> result = getActiveFeaturesFromCookie(cookies, user);
		Set<EnumRolloutFeature> featuresEnabledInProperty = new HashSet<EnumRolloutFeature>();
		for (EnumRolloutFeature feature : EnumRolloutFeature.values()) {
			boolean featureHasForcedParentOrChild = false;
			for (EnumRolloutFeature cookieForcedFeature : result) {
				if (cookieForcedFeature.featureIsChild(feature) || feature.featureIsChild(cookieForcedFeature)) {
					featureHasForcedParentOrChild = true;
					break;
				}
			}
			if (!featureHasForcedParentOrChild && FeatureRolloutArbiter.isFeatureRolledOut(feature, user)) {
				featuresEnabledInProperty.add(feature);
			}
		}
		removeUnusedParentFeature(featuresEnabledInProperty, user);
		result.addAll(featuresEnabledInProperty);
		return result;
	}

	private Set<EnumRolloutFeature> getActiveFeaturesFromCookie(Cookie[] cookies, FDUserI user) {
		Set<EnumRolloutFeature> result = loadFeaturesFromCookie(cookies);
		return result;
	}

	private Map<String, String> getFeatures(Set<EnumRolloutFeature> features) {
		Map<String, String> result = new HashMap<String, String>();
		for (EnumRolloutFeature feature : features) {
			result.put(feature.getCookieName(), feature.getCookieVersion());
		}
		return result;
	}

	private Set<EnumRolloutFeature> loadFeaturesFromCookie(Cookie[] cookies) {
		String features = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(FEATURES_COOKIE_NAME)) {
					features = cookie.getValue();
					break;
				}
			}
		}
		return parseFeatures(features);
	}

	private Set<EnumRolloutFeature> parseFeatures(String features) {
		Set<EnumRolloutFeature> result = new HashSet<EnumRolloutFeature>();
		if (features != null && 0 < features.length()) {
			String[] featureWords = features.split("\\|");
			for (String word : featureWords) {
				if (word != null && word.contains(":")) {
					String[] wordParts = word.split(":");
					String featureName = wordParts[0];
					String featureVersion = wordParts[1];
					if (wordParts != null && featureName != null && featureVersion != null) {
						EnumRolloutFeature feature = parseFeatureName(featureName + featureVersion);
						if (feature != null) {
							result.add(feature);
						}
					}
				}
			}
		}
		return result;
	}

	private void removeUnusedParentFeature(Set<EnumRolloutFeature> features, FDUserI user) {
		Iterator<EnumRolloutFeature> iterator = features.iterator();
		while (iterator.hasNext()) {
			EnumRolloutFeature feature = iterator.next();
			EnumRolloutFeature childFeature = feature.getChild();
			boolean removeFeature = false;
			while (childFeature != null) {
				if (FeatureRolloutArbiter.isFeatureRolledOut(childFeature, user)) {
					removeFeature = true;
					break;
				}
				childFeature = childFeature.getChild();
			}
			if (removeFeature) {
				iterator.remove();
			}
		}
	}

}
