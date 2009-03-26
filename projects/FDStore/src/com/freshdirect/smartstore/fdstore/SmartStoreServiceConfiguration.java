package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.RecommenderStrategy;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationHome;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationSB;
import com.freshdirect.smartstore.impl.AllProductInCategoryRecommendationService;
import com.freshdirect.smartstore.impl.CandidateProductRecommendationService;
import com.freshdirect.smartstore.impl.FavoritesRecommendationService;
import com.freshdirect.smartstore.impl.FeaturedItemsRecommendationService;
import com.freshdirect.smartstore.impl.GlobalCompiler;
import com.freshdirect.smartstore.impl.ManualOverrideRecommendationService;
import com.freshdirect.smartstore.impl.MostFrequentlyBoughtDyfVariant;
import com.freshdirect.smartstore.impl.NullRecommendationService;
import com.freshdirect.smartstore.impl.RandomDyfVariant;
import com.freshdirect.smartstore.impl.ScriptedRecommendationService;
import com.freshdirect.smartstore.impl.ClassicYMALRecommendationService;
import com.freshdirect.smartstore.impl.SmartYMALRecommendationService;
import com.freshdirect.smartstore.impl.YourFavoritesInCategoryRecommendationService;

/**
 * Configures SmartStore services.
 * @author istvan
 *
 */
public class SmartStoreServiceConfiguration {
    
	
	// logger instance
	private static Category LOGGER = LoggerFactory.getInstance(SmartStoreServiceConfiguration.class);
	
	// service locator
	private ServiceLocator serviceLocator;

	// static instance
	private static SmartStoreServiceConfiguration instance = null;
	
	// private constructor
	private SmartStoreServiceConfiguration() throws NamingException {
		serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
	}
	
	// get service configuration home bean
	private SmartStoreServiceConfigurationHome getServiceConfigurationHome() {
		try {
			return (SmartStoreServiceConfigurationHome) serviceLocator.getRemoteHome(
				"freshdirect.smartstore.SmartStoreServiceConfiguration", SmartStoreServiceConfigurationHome.class);
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
	}
	
	/**
	 * Get the unique instance.
	 * 
	 * @return the instance or null
	 */
	public static SmartStoreServiceConfiguration getInstance() {
		if (instance == null) {
			try {
				instance = new SmartStoreServiceConfiguration();
			} catch (NamingException e) {
			}
		}
		return instance;
	}
	
	
	/**
	 * Configure a service for the variant.
	 * 
	 * Returns the appropriate {@link RecommendationService} configured or throws
	 * a runtime exception.
	 * 
	 * @param variant variant
	 */
	private RecommendationService configure(final Variant variant) {
		final RecommendationServiceType serviceType = variant.getServiceConfig().getType();
		
		// If composite 
		if (RecommendationServiceType.FREQUENTLY_BOUGHT_DYF.equals(serviceType)) {
			return new MostFrequentlyBoughtDyfVariant(variant);
		} else if (RecommendationServiceType.RANDOM_DYF.equals(serviceType)) {
			return new RandomDyfVariant(variant);
		} else if (RecommendationServiceType.NIL.equals(serviceType)) {
			return new NullRecommendationService(variant);
		} else if (RecommendationServiceType.FEATURED_ITEMS.equals(serviceType)) {
		    return new FeaturedItemsRecommendationService(variant);
		} else if (RecommendationServiceType.ALL_PRODUCT_IN_CATEGORY.equals(serviceType)) {
		    return new AllProductInCategoryRecommendationService(variant);
		} else if (RecommendationServiceType.FAVORITES.equals(serviceType)) {
		    return new FavoritesRecommendationService(variant);
		} else if (RecommendationServiceType.CANDIDATE_LIST.equals(serviceType)) {
		    return new CandidateProductRecommendationService(variant);
		} else if (RecommendationServiceType.YOUR_FAVORITES_IN_FEATURED_ITEMS.equals(serviceType)) {
		    return new YourFavoritesInCategoryRecommendationService(variant);
		} else if (RecommendationServiceType.MANUAL_OVERRIDE.equals(serviceType)) {
		    return new ManualOverrideRecommendationService(variant);
		} else if (RecommendationServiceType.SCRIPTED.equals(serviceType)) {
		    try {
		        return new ScriptedRecommendationService(variant);
                    } catch (CompileException e) {
                        throw new FDRuntimeException(e, "Compile error " + e.getMessage());
                    }
		} else if (RecommendationServiceType.CLASSIC_YMAL.equals(serviceType)) {
			return new ClassicYMALRecommendationService(variant);
		} else if (RecommendationServiceType.SMART_YMAL.equals(serviceType)) {
			return new SmartYMALRecommendationService(variant);
		} else {
//			throw new FDRuntimeException("Unrecognized variant " + variant);
			return new NullRecommendationService(variant);
		}
	}
	
	// Map<EumSiteFeature,Map<String,RecommendationService> >
	private Map siteFeatureServices = null;
	
	/**
	 * Get the available services corresponding to the requested feature.
	 * If no the services have not been initialized (first call), then
	 * they will be. Otherwise it is a name lookup in the cache.
	 * @param feature
	 * @return Map of service (or the {@link Collections#EMPTY_MAP empty map}
	 */
	public synchronized Map getServices(EnumSiteFeature feature) {
            if (siteFeatureServices == null) {
                siteFeatureServices = loadVariants();
            }
            Map services = (Map)siteFeatureServices.get(feature);
	
            return services;
	}

    private Map loadVariants() {
        Map services = new HashMap();
        try {
            SmartStoreServiceConfigurationSB sb;

            sb = getServiceConfigurationHome().create();
            Collection variants = sb.getVariants(null);

            LOGGER.info("loading variants:" +variants);
            
            GlobalCompiler.getInstance().loadFactorNames();

            List scriptedRecommenders = new ArrayList();
            Set factors = new HashSet();

            for (Iterator i = variants.iterator(); i.hasNext();) {
                Variant variant = (Variant) i.next();
                try {
                    RecommendationService rs = configure(variant);
                    if (rs instanceof ScriptedRecommendationService) {
                        scriptedRecommenders.add(rs);
                        ((ScriptedRecommendationService) rs).collectFactors(factors);
                    }
                    if (rs instanceof SmartYMALRecommendationService) {
                        Set rss = CmsManager.getInstance().getContentKeysByType(ContentType.get("RecommenderStrategy"));
                    	Iterator it = rss.iterator();
                    	while (it.hasNext()) {
                    		ContentKey key = (ContentKey) it.next();
                    		RecommenderStrategy strat = (RecommenderStrategy)
                    				ContentFactory.getInstance().getContentNode(key.getId());
                    		RecommendationService rs1 = 
                    				((SmartYMALRecommendationService) rs).getScriptRecommendationService(strat);
                            ((ScriptedRecommendationService) rs1).collectFactors(factors);
                    	}
                    }
                    Map siteFeatureSpecMap = (Map) services.get(variant.getSiteFeature());
                    if (siteFeatureSpecMap==null) {
                        siteFeatureSpecMap = new HashMap();
                        services.put(variant.getSiteFeature(), siteFeatureSpecMap);
                    }
                    
                    siteFeatureSpecMap.put(variant.getId(), rs);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            
            LOGGER.info("needed factors :" +factors);
            ScoreProvider.getInstance().acquireFactors(factors);
            LOGGER.info("configured services :"+services);
        } catch (RemoteException e) {
            LOGGER.warn("SmartStore Service Configuration", e);
            return Collections.EMPTY_MAP;
        } catch (CreateException e) {
            LOGGER.warn("SmartStore Service Configuration", e);
            return Collections.EMPTY_MAP;
        } catch (SQLException e) {
            LOGGER.warn("SmartStore Service Configuration", e);
            return Collections.EMPTY_MAP;
        }
        return services;
    }
	
	public synchronized void refresh() {
	    siteFeatureServices = null;	    
	}

}
