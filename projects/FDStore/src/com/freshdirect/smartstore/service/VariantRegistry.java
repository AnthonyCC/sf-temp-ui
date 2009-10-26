package com.freshdirect.smartstore.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

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
		return variantMap.get(variantId);
	}

	public synchronized Map<String, Variant> getServices(
			EnumSiteFeature siteFeature) {
		if (variantMap == null)
			load();
		Map<String, Variant> variants = new HashMap<String, Variant>();
		for (Variant v : variantMap.values()) {
			if (v.getSiteFeature().equals(siteFeature))
				variants.put(v.getId(), v);
		}
		return variants;
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
		} catch (Exception e) {
			LOGGER.error("SmartStore Service Configuration", e);
		}
	}

	public synchronized void reload() {
		load();
	}
}
