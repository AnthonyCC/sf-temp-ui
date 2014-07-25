package com.freshdirect.smartstore.external.certona;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.external.ExternalRecommenderRegistry;
import com.freshdirect.smartstore.external.ExternalRecommenderType;
import com.freshdirect.smartstore.external.NoSuchExternalRecommenderException;

public class CertonaInfrastructure {

	private final static Logger LOG = LoggerFactory.getInstance(CertonaInfrastructure.class);
	private static void load() {
		try {
			ExternalRecommenderRegistry.registerRecommender("certonaDepartment1rr", ExternalRecommenderType.PERSONALIZED, new CertonaRecommender("department1_rr"), 100, 10 * 60 * 1000 /* 10mins */);
			ExternalRecommenderRegistry.registerRecommender("certonaProduct1rr", ExternalRecommenderType.RELATED, new CertonaRecommender("product1_rr"), 100, 10 * 60 * 1000 /* 10mins */);
			ExternalRecommenderRegistry.registerRecommender("certonaCartConfirm1rr", ExternalRecommenderType.RELATED, new CertonaRecommender("cartconfirm1_rr"), 100, 10 * 60 * 1000 /* 10mins */);
			ExternalRecommenderRegistry.registerRecommender("certonaSearch1rr", ExternalRecommenderType.PERSONALIZED, new CertonaRecommender("search1_rr"), 100, 10 * 60 * 1000 /* 10mins */);
			ExternalRecommenderRegistry.registerRecommender("certonaQuickshop1rr", ExternalRecommenderType.PERSONALIZED, new CertonaRecommender("quickshop1_rr"), 100, 10 * 60 * 1000 /* 10mins */);
			ExternalRecommenderRegistry.registerRecommender("certonaCategory1rr", ExternalRecommenderType.PERSONALIZED, new CertonaRecommender("category1_rr"), 100, 10 * 60 * 1000 /* 10mins */);
			ExternalRecommenderRegistry.registerRecommender("certonaSubCategory1rr", ExternalRecommenderType.PERSONALIZED, new CertonaRecommender("subcategory1_rr"), 100, 10 * 60 * 1000 /* 10mins */);
			
			LOG.info("Certona recommenders were configured");
		} catch (IllegalArgumentException e) {
			LOG.error("illegal argument while creating/registering Certona external recommenders", e);
		}
	}

	private static void unload() {
		ExternalRecommenderRegistry.unregisterRecommender("certonaDepartment1rr", ExternalRecommenderType.PERSONALIZED);
		ExternalRecommenderRegistry.unregisterRecommender("certonaProduct1rr", ExternalRecommenderType.RELATED);
		ExternalRecommenderRegistry.unregisterRecommender("certonaCartConfirm1rr", ExternalRecommenderType.RELATED);
		ExternalRecommenderRegistry.unregisterRecommender("certonaSearch1rr", ExternalRecommenderType.PERSONALIZED);
		ExternalRecommenderRegistry.unregisterRecommender("certonaQuickshop1rr", ExternalRecommenderType.PERSONALIZED);
		ExternalRecommenderRegistry.unregisterRecommender("certonaCategory1rr", ExternalRecommenderType.PERSONALIZED);
		ExternalRecommenderRegistry.unregisterRecommender("certonaSubCategory1rr", ExternalRecommenderType.PERSONALIZED);
		
		LOG.info("Certona recommenders were unloaded");
	}

	public static boolean isLoaded() {
		try {
			return ExternalRecommenderRegistry.getInstance("certonaDepartment1rr", ExternalRecommenderType.PERSONALIZED) != null
					&& ExternalRecommenderRegistry.getInstance("certonaProduct1rr", ExternalRecommenderType.RELATED) != null
					&& ExternalRecommenderRegistry.getInstance("certonaCartConfirm1rr", ExternalRecommenderType.RELATED) != null
					&& ExternalRecommenderRegistry.getInstance("certonaSearch1rr", ExternalRecommenderType.PERSONALIZED) != null
					&& ExternalRecommenderRegistry.getInstance("certonaQuickshop1rr", ExternalRecommenderType.PERSONALIZED) != null					
					&& ExternalRecommenderRegistry.getInstance("certonaCategory1rr", ExternalRecommenderType.PERSONALIZED) != null					
					&& ExternalRecommenderRegistry.getInstance("certonaSubCategory1rr", ExternalRecommenderType.PERSONALIZED) != null;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (NoSuchExternalRecommenderException e) {
			return false;
		}
	}

	public static void reload() {
		reload(false);
	}

	public static void reload(boolean force) {
		if (force) {
			LOG.info("forcing reload of Certona Infrastructure");
			unload();
			load();
		} else {
			if (!isLoaded())
				load();
		}
	}

	private CertonaInfrastructure() {

	}
}
