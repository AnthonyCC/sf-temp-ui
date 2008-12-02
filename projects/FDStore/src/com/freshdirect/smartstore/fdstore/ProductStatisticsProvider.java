package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.ejb.DyfModelHome;
import com.freshdirect.smartstore.ejb.DyfModelSB;
import com.freshdirect.smartstore.impl.SessionCache;

/**
 * Product statistics provider.
 * @author istvan
 */
public class ProductStatisticsProvider {
	
	
	private static final Category LOGGER = LoggerFactory.getInstance(ProductStatisticsProvider.class);
	
	private static ProductStatisticsProvider instance;
	
        /**
         * Service locator.
         */
        protected ServiceLocator serviceLocator;
        
        /**
         * Global product frequency map, Map<@link {@link ContentKey},Float>.
         */
        protected Map globalProductScores = new HashMap();

        /**
         * The user score cache.
         */
        SessionCache userScoreCache = new SessionCache(1000, 0.75f);
	
	
	/**
	 * Get provider instance.
	 * @return instance (may throw a runtime exception if the instance cannot be created, and it was not cached)
	 */
	public synchronized static ProductStatisticsProvider getInstance() {
		if (instance == null) {
			try {
				instance = new ProductStatisticsProvider();
			} catch (NamingException e) {
				LOGGER.error("could not initialize service", e);
				throw new RuntimeException("could not initialize service",e);
			} catch (RemoteException e) {
				LOGGER.error("could not cache global product scores",e);
			} catch (CreateException e) {
				LOGGER.error("could not create SS session bean",e);
			}
		}
		return instance;
	}
	
	
	/**
	 * Private constructor.
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws CreateException
	 */
	private ProductStatisticsProvider() throws NamingException, RemoteException, CreateException {
            serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
            DyfModelSB source = getModelHome().create();
            globalProductScores = source.getGlobalProductScores();
        }
	
	/**
	 * Get user specific product scores.
	 * 
	 * The higher the score, the more the user likes.
	 * @return Map<{@link ContentKey},{@link Float}> productId->Score, never null
	 */
	public Map getUserProductScores(String erpCustomerId) {
            try {
                Map productFrequencies = (Map) userScoreCache.get(erpCustomerId);
                if (productFrequencies == null) {
                    productFrequencies = getModel().getProductFrequencies(erpCustomerId);
                    userScoreCache.put(erpCustomerId, productFrequencies);
                }
                return productFrequencies;
            } catch (RemoteException e) {
                LOGGER.error("remote exception", e);
                return Collections.EMPTY_MAP;
            }
        }
 	
	/**
	 * Get global product score.
	 * 
	 * @param key product content key
	 * @return global popularity, -1
	 */
	public float getGlobalProductScore(ContentKey key) {
		Float score = (Float)globalProductScores.get(key);
		return score == null ? -1 : score.floatValue();
	}
    	
    	public Set getProducts(String customerID) {
            try {
                return getModel().getProducts(customerID);
            } catch (RemoteException e) {
                throw new FDRuntimeException(e);
            }
        }

        protected DyfModelHome getModelHome() {
            try {
                return (DyfModelHome) serviceLocator.getRemoteHome("freshdirect.smartstore.DyfModelHome", DyfModelHome.class);
            } catch (NamingException e) {
                throw new FDRuntimeException(e);
            }
        }
        
        protected DyfModelSB getModel() {
            try {
                return getModelHome().create();
            } catch (RemoteException e) {
                throw new FDRuntimeException(e);
            } catch (CreateException e) {
                throw new FDRuntimeException(e);
            }
        }

}
