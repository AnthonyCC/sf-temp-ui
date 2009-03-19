package com.freshdirect.smartstore.impl;

import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.ejb.DyfModelHome;

/**
 * Base class of DYF Recommendation Services
 *  * 
 * @author segabor
 *
 */
public abstract class DYFService extends BaseContentKeyRecommendationService {
	private static final Category LOGGER = LoggerFactory.getInstance(AbstractRecommendationService.class);

	// cache the most recently accessed order histories
	protected SessionCache cache = new SessionCache();

	protected ServiceLocator serviceLocator;

	protected SessionCache getCache() {
		return this.cache;
	}
	


	protected DyfModelHome getModelHome() {
		try {
			return (DyfModelHome) serviceLocator.getRemoteHome(
				"freshdirect.smartstore.DyfModelHome", DyfModelHome.class);
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
	}

	

	public DYFService(Variant variant) {
		super(variant);

		// connect to database
		try {
			this.serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
		} catch (NamingException e) {
			LOGGER.error("Failed to instantiate MostFrequentlyBoughtVariant", e);
		}
	}
}
