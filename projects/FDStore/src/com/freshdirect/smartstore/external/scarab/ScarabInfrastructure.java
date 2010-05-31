package com.freshdirect.smartstore.external.scarab;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.external.ExternalRecommenderRegistry;
import com.freshdirect.smartstore.external.ExternalRecommenderType;
import com.freshdirect.smartstore.external.NoSuchExternalRecommenderException;
import com.scarabresearch.recommendation.api.ScarabService;
import com.scarabresearch.recommendation.api.ScarabServiceCreationException;
import com.scarabresearch.recommendation.api.ScarabServiceFactory;

public final class ScarabInfrastructure {
	private final static Logger LOG = LoggerFactory.getInstance(ScarabInfrastructure.class);
	
	private final static Object sync = new Object();

	private static ScarabService theOnlyService;

        private static ScarabService getService() throws ScarabServiceCreationException {
            synchronized (sync) {
                if (theOnlyService == null) {
                    final String scarabServiceString = ScarabProperties.getScarabServiceString();
                    if (scarabServiceString != null && scarabServiceString.trim().length() > 0) {
                        theOnlyService = ScarabServiceFactory.createService(Arrays.asList(scarabServiceString.split(",")), ScarabProperties
                                .getScarabServiceTimeout());
                        LOG.info("Scarab Service was initialized with : " + scarabServiceString);
                    }
                }
                return theOnlyService;
            }
        }
	
	private static void resetService() {
		synchronized (sync) {
			try {
				getService().close();
				theOnlyService = null;
				LOG.info("Scarab Service was reset");
			} catch (ScarabServiceCreationException e) {
			}
		}
	}

	private static void load() {
		try {
			ExternalRecommenderRegistry.registerRecommender("scarab1", ExternalRecommenderType.PERSONALIZED, // cart & tabs
					new ScarabPersonalRecommender(getService()), 100, 10 * 60 * 1000 /* 10mins */);
			ExternalRecommenderRegistry.registerRecommender("scarab1", ExternalRecommenderType.RELATED, // ymal
					new ScarabRelatedRecommender(getService()));
			ExternalRecommenderRegistry.registerRecommender("scarabCart1", ExternalRecommenderType.RELATED, // cart & tabs
					new ScarabCartRecommender(getService()));
			LOG.info("Scarab recommenders were configured");
		} catch (IllegalArgumentException e) {
			LOG.error("illegal argument while creating/registering scarab external recommenders", e);
		} catch (ScarabServiceCreationException e) {
			LOG.error("scarab service creation error while creating/registering scarab external recommenders", e);
		}
	}

	private static void unload() {
		ExternalRecommenderRegistry.unregisterRecommender("scarab1", ExternalRecommenderType.PERSONALIZED);
		ExternalRecommenderRegistry.unregisterRecommender("scarab1", ExternalRecommenderType.RELATED);
		ExternalRecommenderRegistry.unregisterRecommender("scarabCart1", ExternalRecommenderType.RELATED);
		LOG.info("Scarab recommenders were unloaded");
	}

	public static boolean isLoaded() {
		try {
			return ExternalRecommenderRegistry.getInstance("scarab1", ExternalRecommenderType.PERSONALIZED) != null
					&& ExternalRecommenderRegistry.getInstance("scarab1", ExternalRecommenderType.RELATED) != null
					&& ExternalRecommenderRegistry.getInstance("scarabCart1", ExternalRecommenderType.RELATED) != null;
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
			LOG.info("forcing reload of Scarab Infrastructure");
			resetService();
			unload();
			load();
		} else {
			if (!isLoaded())
				load();
		}
	}

	private ScarabInfrastructure() {

	}
}
