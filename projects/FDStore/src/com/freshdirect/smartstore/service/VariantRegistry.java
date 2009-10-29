package com.freshdirect.smartstore.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationHome;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationSB;
import com.freshdirect.smartstore.fdstore.FactorRequirer;
import com.freshdirect.smartstore.fdstore.ScoreProvider;

final public class VariantRegistry {
	private static final Logger LOGGER = LoggerFactory
			.getInstance(VariantRegistry.class);
	private static VariantRegistry instance;

	private Map<String, Variant> variantMap;
	private Map<EnumSiteFeature, Map<String, Variant>> siteFeatureMap;

	private VariantRegistry() {
	}

	public static synchronized VariantRegistry getInstance() {
		if (instance == null) {
			instance = new VariantRegistry();
		}
		return instance;
	}

	public synchronized Variant getService(String variantId) {
		if (variantMap == null)
			load();
		if (variantMap == null)
			throw new FDRuntimeException("failed to load variant map, see log for details");
		return variantMap.get(variantId);
	}

	/**
	 * THIS IS A MOCK METHOD, STRICTLY FOR TESTING PURPOSES !!!
	 * @param variant
	 */
	public synchronized void addService(Variant variant) {
		if (variantMap == null) {
			variantMap = new HashMap<String, Variant>();
			siteFeatureMap = new HashMap<EnumSiteFeature, Map<String,Variant>>();
		}
		variantMap.put(variant.getId(), variant);
		if (!siteFeatureMap.containsKey(variant.getSiteFeature()))
			siteFeatureMap.put(variant.getSiteFeature(), new HashMap<String, Variant>());
		siteFeatureMap.get(variant.getSiteFeature()).put(variant.getId(), variant);
	}
	
	public synchronized Map<String, Variant> getServices(
			EnumSiteFeature siteFeature) {
		if (variantMap == null)
			load();
		if (variantMap == null)
			throw new FDRuntimeException("failed to load variant map, see log for details");
		return siteFeatureMap.get(siteFeature);
	}

	private SmartStoreServiceConfigurationHome getServiceConfigurationHome()
			throws NamingException {
		return (SmartStoreServiceConfigurationHome) new ServiceLocator(
				FDStoreProperties.getInitialContext()).getRemoteHome(
				"freshdirect.smartstore.SmartStoreServiceConfiguration",
				SmartStoreServiceConfigurationHome.class);
	}

	private void load() {
		try {
			SmartStoreServiceConfigurationSB sb;

			sb = getServiceConfigurationHome().create();
			Collection<Variant> variants = sb.getVariants(null);

			LOGGER.info("loading variants:" + variants);

			Map<String, Variant> variantMapTmp = new HashMap<String, Variant>(
					variants.size());
			Map<EnumSiteFeature, Map<String, Variant>> siteFeatureMapTmp = 
					new HashMap<EnumSiteFeature, Map<String,Variant>>();

			Set factors = new HashSet();

			for (Variant variant : variants) {
				try {
					RecommendationService rs = RecommendationServiceFactory
							.configure(variant);
					if (rs instanceof FactorRequirer) {
						((FactorRequirer) rs).collectFactors(factors);
					}

					variant.setRecommender(rs);
					variantMapTmp.put(variant.getId(), variant);
					if (!siteFeatureMapTmp.containsKey(variant.getSiteFeature()))
						siteFeatureMapTmp.put(variant.getSiteFeature(), new HashMap<String, Variant>());
					siteFeatureMapTmp.get(variant.getSiteFeature()).put(variant.getId(), variant);
				} catch (Exception e) {
					LOGGER.warn("failed to configure variant: "
							+ variant.getId(), e);
					continue;
				}
			}

			LOGGER.info("needed factors :" + factors);
			ScoreProvider.getInstance().acquireFactors(factors);
			LOGGER.info("configured variants:" + variantMapTmp.keySet());

			variantMap = variantMapTmp;
			siteFeatureMap = siteFeatureMapTmp;
		} catch (Exception e) {
			LOGGER.error("SmartStore Service Configuration", e);
		}
	}

	public synchronized void reload() {
		load();
	}
}
