package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.ConfigurationException;
import com.freshdirect.smartstore.ConfigurationStatus;
import com.freshdirect.smartstore.EnumConfigurationState;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationHome;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationSB;
import com.freshdirect.smartstore.impl.AllProductInCategoryRecommendationService;
import com.freshdirect.smartstore.impl.COSFilter;
import com.freshdirect.smartstore.impl.CandidateProductRecommendationService;
import com.freshdirect.smartstore.impl.ClassicYMALRecommendationService;
import com.freshdirect.smartstore.impl.FavoritesRecommendationService;
import com.freshdirect.smartstore.impl.FeaturedItemsRecommendationService;
import com.freshdirect.smartstore.impl.GlobalCompiler;
import com.freshdirect.smartstore.impl.ManualOverrideRecommendationService;
import com.freshdirect.smartstore.impl.MostFrequentlyBoughtDyfVariant;
import com.freshdirect.smartstore.impl.NullRecommendationService;
import com.freshdirect.smartstore.impl.RandomDyfVariant;
import com.freshdirect.smartstore.impl.ScriptedRecommendationService;
import com.freshdirect.smartstore.impl.SmartSavingRecommendationService;
import com.freshdirect.smartstore.impl.SmartYMALRecommendationService;
import com.freshdirect.smartstore.impl.YmalYfRecommendationService;
import com.freshdirect.smartstore.impl.YourFavoritesInCategoryRecommendationService;
import com.freshdirect.smartstore.sampling.ComplicatedImpressionSampler;
import com.freshdirect.smartstore.sampling.ConfiguredImpressionSampler;
import com.freshdirect.smartstore.sampling.ContentSampler;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.sampling.ListSampler;
import com.freshdirect.smartstore.sampling.SimpleLimit;

/**
 * Configures SmartStore services.
 * @author istvan
 *
 */
public class SmartStoreServiceConfiguration {
	// logger instance
	private static Category LOGGER = LoggerFactory.getInstance(SmartStoreServiceConfiguration.class);
	
	public static final String CKEY_SAMPLING_STRATEGY = "sampling_strat";
	public static final String CKEY_TOP_PERC = "top_perc";
	public static final String CKEY_TOP_N = "top_n";
	public static final String CKEY_EXPONENT = "exponent";

	public static final String CKEY_PREZ_DESC = "prez_desc";
	public static final String CKEY_PREZ_TITLE = "prez_title";
	public static final String CKEY_FI_LABEL = "fi_label";

	public static final String CKEY_CAT_AGGR = "cat_aggr";
	public static final String CKEY_INCLUDE_CART_ITEMS = "include_cart_items";

	public static final String CKEY_SMART_SAVE = "smart_saving";
	public static final String CKEY_COS_FILTER = "cos_filter";

	public static final String CKEY_FAVORITE_LIST_ID = "favorite_list_id";

	public final static String CKEY_GENERATOR = "generator";
    public final static String CKEY_SCORING = "scoring";
    
    public final static Map configDesc = new HashMap();
    
    static {
    	configDesc.put(CKEY_SAMPLING_STRATEGY, "Sampling Strategy");
    	configDesc.put(CKEY_TOP_PERC, "Top %");
    	configDesc.put(CKEY_TOP_N, "Top N");
    	configDesc.put(CKEY_EXPONENT, "Exponent");
    	configDesc.put(CKEY_PREZ_DESC, "Presentation Description");
    	configDesc.put(CKEY_PREZ_TITLE, "Presentation Title");
    	configDesc.put(CKEY_FI_LABEL, "Featured Items Label");
    	configDesc.put(CKEY_CAT_AGGR, "Category Aggregation");
    	configDesc.put(CKEY_INCLUDE_CART_ITEMS, "Include Cart Items");
    	configDesc.put(CKEY_SMART_SAVE, "Smart Savings");
    	configDesc.put(CKEY_COS_FILTER, "COS Filter");
    	configDesc.put(CKEY_FAVORITE_LIST_ID, "Favorite List Id");
    	configDesc.put(CKEY_GENERATOR, "Generator Function");
    	configDesc.put(CKEY_SCORING, "Scoring Function");
    }
    
    public final static int DEFAULT_TOP_N = 20;
    public final static double DEFAULT_TOP_P = 20.0;
    public final static String DEFAULT_SAMPLING_STRATEGY = "deterministic";
    public final static double DEFAULT_EXPONENT = 0.66;
    
	public static final String DEFAULT_FI_LABEL = "Our Favorites";

	public final static boolean DEFAULT_CAT_AGGR = false;
    public final static boolean DEFAULT_INCLUDE_CART_ITEMS = false;
    
    public static final boolean DEFAULT_SMART_SAVE = false;
    public static final String DEFAULT_COS_FILTER = null;

	public static final String DEFAULT_FAVORITE_LIST_ID = "fd_favorites";
    
	// Valid sampler names
	public static final String SAMPLERS[] = { "deterministic", "uniform",
			"linear", "quadratic", "cubic", "harmonic", "sqrt", "power",
			"complicated" };

	// static instance
	private static SmartStoreServiceConfiguration instance = null;
	
	// service locator
	private ServiceLocator serviceLocator;
    
	// Map<EumSiteFeature,Map<String,RecommendationService> >
	private Map siteFeatureServices = null;
	
	// private constructor
	private SmartStoreServiceConfiguration() throws NamingException {
		serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
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
	
	// get service configuration home bean
	private SmartStoreServiceConfigurationHome getServiceConfigurationHome() {
		try {
			return (SmartStoreServiceConfigurationHome) serviceLocator.getRemoteHome(
				"freshdirect.smartstore.SmartStoreServiceConfiguration", SmartStoreServiceConfigurationHome.class);
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
	}


    private Map loadVariants() {
        Map services = new HashMap();
        try {
            SmartStoreServiceConfigurationSB sb;

            sb = getServiceConfigurationHome().create();
            Collection variants = sb.getVariants(null);

            LOGGER.info("loading variants:" +variants);
            
            GlobalCompiler.getInstance().loadFactorNames();

            Set factors = new HashSet();

            for (Iterator i = variants.iterator(); i.hasNext();) {
                Variant variant = (Variant) i.next();
                try {
                    RecommendationService rs = configure(variant);
                    if (rs instanceof FactorRequirer) {
                        ((FactorRequirer)rs).collectFactors(factors);
                    }
                    Map siteFeatureSpecMap = (Map) services.get(variant.getSiteFeature());
                    if (siteFeatureSpecMap==null) {
                        siteFeatureSpecMap = new HashMap();
                        services.put(variant.getSiteFeature(), siteFeatureSpecMap);
                    }
                    
                    siteFeatureSpecMap.put(variant.getId(), rs);
                } catch (Exception e) {
                	LOGGER.warn("failed to configure variant: " + variant.getId(), e);
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
	
	/**
	 * Configure a service for the variant.
	 * 
	 * Returns the appropriate {@link RecommendationService} configured or throws
	 * a runtime exception.
	 * 
	 * @param variant variant
	 * @throws CompileException 
	 */
    public static RecommendationService configure(Variant variant) {
    	RecommendationService service;
    	RecommendationServiceConfig config = variant.getServiceConfig();
        RecommendationServiceType serviceType = config.getType();
        boolean catAggr = DEFAULT_CAT_AGGR;
        boolean includeCartItems = DEFAULT_INCLUDE_CART_ITEMS;
        boolean smartSave = DEFAULT_SMART_SAVE;
        String cosFilter = DEFAULT_COS_FILTER;
        
        SortedMap statuses = variant.getServiceConfig().getConfigStatus();
        if (statuses == null)
        	variant.getServiceConfig().setConfigStatus(statuses = new TreeMap());
        
        if (!RecommendationServiceType.TAB_STRATEGY.equals(serviceType)) {
        	smartSave = extractSmartSave(config, statuses, variant.getSiteFeature());
        	// if smart saving used, we will return items from the cart.
            includeCartItems = extractIncludeCartItems(config, statuses, smartSave);
        	cosFilter = extractCosFilter(config, statuses);
        	extractCartPresentation(config, statuses);
        }
        
        if (EnumSiteFeature.FEATURED_ITEMS.equals(variant.getSiteFeature())) {
        	extractFeaturedPresentation(config, statuses);
        }
        
    	String favoriteListId = extractFavoriteListId(config, statuses);

        ImpressionSampler sampler = null;
        if (!RecommendationServiceType.NIL.equals(serviceType)
        		&& !RecommendationServiceType.TAB_STRATEGY.equals(serviceType)
        		&& !RecommendationServiceType.ALL_PRODUCT_IN_CATEGORY.equals(serviceType)
        		&& !RecommendationServiceType.CLASSIC_YMAL.equals(serviceType)
        		&& !RecommendationServiceType.SMART_YMAL.equals(serviceType)
        		&& !RecommendationServiceType.YMAL_YF.equals(serviceType)) {
        	try {
        		sampler = configureSampler(variant.getServiceConfig(), statuses);
        	} catch (ConfigurationException e) {
        		LOGGER.error("unable to configure sampler", e);
        		sampler = null;
        	}
        	catAggr = extractCategoryAggregation(config, statuses);
        }

		String generator = null;
    	String scoring = null;
    	if (RecommendationServiceType.SCRIPTED.equals(serviceType)) {
    		generator = config.get(CKEY_GENERATOR);
    		if (generator != null)
    			statuses.put(CKEY_GENERATOR, new ConfigurationStatus(CKEY_GENERATOR, generator,
    					EnumConfigurationState.CONFIGURED_OK));
    		else
    			statuses.put(CKEY_GENERATOR, new ConfigurationStatus(CKEY_GENERATOR, null,
    					EnumConfigurationState.UNCONFIGURED_WRONG,
    					"Mandatory parameter!"));
    		scoring = config.get(CKEY_SCORING);
    		if (scoring != null)
    			statuses.put(CKEY_SCORING, new ConfigurationStatus(CKEY_SCORING, scoring,
    					EnumConfigurationState.CONFIGURED_OK));
    		else
    			statuses.put(CKEY_SCORING, new ConfigurationStatus(CKEY_SCORING, null,
    					EnumConfigurationState.UNCONFIGURED_DEFAULT).setWarning(
						"You might have forgotten to set this value."));
    	}
    	
        Set unused = new HashSet(config.keys());
        unused.removeAll(statuses.keySet());
        Iterator it = unused.iterator();
        while (it.hasNext()) {
        	String param = (String) it.next();
        	statuses.put(param, new ConfigurationStatus(param, config.get(param),
        			EnumConfigurationState.CONFIGURED_UNUSED)
        			.setWarning("Unused parameter. Maybe mistyped?!?"));
        }

        if (!RecommendationServiceType.NIL.equals(serviceType)
        		&& !RecommendationServiceType.TAB_STRATEGY.equals(serviceType)
        		&& !RecommendationServiceType.ALL_PRODUCT_IN_CATEGORY.equals(serviceType)
        		&& !RecommendationServiceType.CLASSIC_YMAL.equals(serviceType)
        		&& !RecommendationServiceType.SMART_YMAL.equals(serviceType)
        		&& !RecommendationServiceType.YMAL_YF.equals(serviceType)) {
	    	if (sampler == null)
	    		return new NullRecommendationService(variant);
        }
        
        // If composite
        if (RecommendationServiceType.FREQUENTLY_BOUGHT_DYF.equals(serviceType)) {
            service = new MostFrequentlyBoughtDyfVariant(variant, sampler, catAggr, includeCartItems);
        } else if (RecommendationServiceType.RANDOM_DYF.equals(serviceType)) {
        	service = new RandomDyfVariant(variant, sampler, catAggr, includeCartItems);
        } else if (RecommendationServiceType.NIL.equals(serviceType)
        		|| RecommendationServiceType.TAB_STRATEGY.equals(serviceType)) {
        	service = new NullRecommendationService(variant);
        } else if (RecommendationServiceType.FEATURED_ITEMS.equals(serviceType)) {
        	service = new FeaturedItemsRecommendationService(variant, sampler, catAggr, includeCartItems);
        } else if (RecommendationServiceType.ALL_PRODUCT_IN_CATEGORY.equals(serviceType)) {
        	service = new AllProductInCategoryRecommendationService(variant, null, catAggr, includeCartItems);
        } else if (RecommendationServiceType.FAVORITES.equals(serviceType)) {
			service = new FavoritesRecommendationService(variant, sampler, catAggr, includeCartItems, favoriteListId);
        } else if (RecommendationServiceType.CANDIDATE_LIST.equals(serviceType)) {
        	service = new CandidateProductRecommendationService(variant, sampler, catAggr, includeCartItems);
        } else if (RecommendationServiceType.YOUR_FAVORITES_IN_FEATURED_ITEMS.equals(serviceType)) {
        	service = new YourFavoritesInCategoryRecommendationService(variant, sampler, catAggr, includeCartItems);
        } else if (RecommendationServiceType.MANUAL_OVERRIDE.equals(serviceType)) {
        	service = new ManualOverrideRecommendationService(variant, sampler, catAggr, includeCartItems);
        } else if (RecommendationServiceType.SCRIPTED.equals(serviceType)) {
			try {
				service = new ScriptedRecommendationService(variant, sampler, catAggr, includeCartItems, generator, scoring);
			} catch (CompileException e) {
				LOGGER.error("cannot instantiate script recommender - compile error (fall back to NIL): " + variant.getId(), e);
				return new NullRecommendationService(variant);
			} catch (NullPointerException e) {
				LOGGER.error("cannot instantiate script recommender - generator null (fall back to NIL): " + variant.getId(), e);
				return new NullRecommendationService(variant);
			}
        } else if (RecommendationServiceType.CLASSIC_YMAL.equals(serviceType)) {
        	service = new ClassicYMALRecommendationService(variant, null, catAggr, includeCartItems);
        } else if (RecommendationServiceType.SMART_YMAL.equals(serviceType)) {
        	service = new SmartYMALRecommendationService(variant, null, catAggr, includeCartItems);
        } else if (RecommendationServiceType.YMAL_YF.equals(serviceType)) {
			try {
	        	service = new YmalYfRecommendationService(variant, null, catAggr, includeCartItems);
			} catch (CompileException e) {
				LOGGER.error("cannot instantiate script recommender (fall back to NIL): " + variant.getId(), e);
				return new NullRecommendationService(variant);
			}
        } else {
        	service = new NullRecommendationService(variant);
        }
        
        if (smartSave)
        	service = new SmartSavingRecommendationService(service);
        
        if (cosFilter != null)
        	service = new COSFilter(service, cosFilter);
        
        return service;
    }

	private static boolean extractCategoryAggregation(RecommendationServiceConfig config,
			Map statuses) {
		boolean catAggr = DEFAULT_CAT_AGGR;
		String catAggrStr = config.get(CKEY_CAT_AGGR);
		if (catAggrStr != null) {
			if (catAggrStr.equalsIgnoreCase("yes")
					|| catAggrStr.equalsIgnoreCase("true")
					|| catAggrStr.equals("1")) {
				catAggr = true;
				statuses.put(CKEY_CAT_AGGR, new ConfigurationStatus(CKEY_CAT_AGGR, catAggrStr,
						Boolean.toString(catAggr), EnumConfigurationState.CONFIGURED_OK));
			} else
				statuses.put(CKEY_CAT_AGGR, new ConfigurationStatus(CKEY_CAT_AGGR, catAggrStr,
						Boolean.toString(DEFAULT_CAT_AGGR), EnumConfigurationState.CONFIGURED_WRONG_DEFAULT,
						"Unrecognized value defaulting to " + Boolean.toString(DEFAULT_CAT_AGGR)));
		} else
			statuses.put(CKEY_CAT_AGGR, new ConfigurationStatus(CKEY_CAT_AGGR, null,
					Boolean.toString(DEFAULT_CAT_AGGR), EnumConfigurationState.UNCONFIGURED_DEFAULT));
		return catAggr;
	}

	private static boolean extractIncludeCartItems(RecommendationServiceConfig config,
			Map statuses, boolean smartSave) {
		boolean includeCartItems = DEFAULT_INCLUDE_CART_ITEMS;
		String iciStr = config.get(CKEY_INCLUDE_CART_ITEMS);
		if (smartSave) {
			includeCartItems = true;
			if (iciStr != null)
				statuses.put(CKEY_INCLUDE_CART_ITEMS, new ConfigurationStatus(CKEY_INCLUDE_CART_ITEMS, iciStr,
						Boolean.toString(includeCartItems), EnumConfigurationState.CONFIGURED_OVERRIDDEN)
						.setWarning("Variant belongs to Smart Savings enabled Site Feature therefore automatically turned on"));
			else
				statuses.put(CKEY_INCLUDE_CART_ITEMS, new ConfigurationStatus(CKEY_INCLUDE_CART_ITEMS, null,
						Boolean.toString(includeCartItems), EnumConfigurationState.UNCONFIGURED_OVERRIDDEN)
						.setWarning("Variant belongs to Smart Savings enabled Site Feature therefore automatically turned on"));
			return includeCartItems;
		}
		if (iciStr != null) {
			if (iciStr.equalsIgnoreCase("yes")
					|| iciStr.equalsIgnoreCase("true")
					|| iciStr.equals("1")) {
				includeCartItems = true;
				statuses.put(CKEY_INCLUDE_CART_ITEMS, new ConfigurationStatus(CKEY_INCLUDE_CART_ITEMS, iciStr,
						Boolean.toString(includeCartItems), EnumConfigurationState.CONFIGURED_OK));
			} else
				statuses.put(CKEY_INCLUDE_CART_ITEMS, new ConfigurationStatus(CKEY_INCLUDE_CART_ITEMS, iciStr,
						Boolean.toString(DEFAULT_INCLUDE_CART_ITEMS), EnumConfigurationState.CONFIGURED_WRONG_DEFAULT,
						"Unrecognized value defaulting to " + Boolean.toString(DEFAULT_INCLUDE_CART_ITEMS)));
		} else
			statuses.put(CKEY_INCLUDE_CART_ITEMS, new ConfigurationStatus(CKEY_INCLUDE_CART_ITEMS, null,
					Boolean.toString(DEFAULT_INCLUDE_CART_ITEMS), EnumConfigurationState.UNCONFIGURED_DEFAULT));
		return includeCartItems;
	}
	
	private static boolean extractSmartSave(RecommendationServiceConfig config,
			Map statuses, EnumSiteFeature siteFeature) {
		boolean smartSave = DEFAULT_SMART_SAVE;
		String smartSaveStr = config.get(CKEY_SMART_SAVE);
		if (siteFeature.isSmartSavings()) {
			smartSave = true;
			if (smartSaveStr != null)
				statuses.put(CKEY_SMART_SAVE, new ConfigurationStatus(CKEY_SMART_SAVE, smartSaveStr,
						Boolean.toString(smartSave), EnumConfigurationState.CONFIGURED_OVERRIDDEN)
						.setWarning("Variant belongs to Smart Savings enabled Site Feature therefore automatically turned on"));
			else
				statuses.put(CKEY_SMART_SAVE, new ConfigurationStatus(CKEY_SMART_SAVE, null,
						Boolean.toString(smartSave), EnumConfigurationState.UNCONFIGURED_OVERRIDDEN)
						.setWarning("Variant belongs to Smart Savings enabled Site Feature therefore automatically turned on"));
			return smartSave;
		}
		if (smartSaveStr != null) {
			if (smartSaveStr.equalsIgnoreCase("yes")
					|| smartSaveStr.equalsIgnoreCase("true")
					|| smartSaveStr.equals("1")) {
				smartSave = true;
				statuses.put(CKEY_SMART_SAVE, new ConfigurationStatus(CKEY_SMART_SAVE, smartSaveStr,
						Boolean.toString(smartSave), EnumConfigurationState.CONFIGURED_OK));
			} else
				statuses.put(CKEY_SMART_SAVE, new ConfigurationStatus(CKEY_SMART_SAVE, smartSaveStr,
						Boolean.toString(DEFAULT_SMART_SAVE), EnumConfigurationState.CONFIGURED_WRONG_DEFAULT,
						"Unrecognized value defaulting to " + Boolean.toString(DEFAULT_SMART_SAVE)));
		} else
			statuses.put(CKEY_SMART_SAVE, new ConfigurationStatus(CKEY_SMART_SAVE, null,
					Boolean.toString(DEFAULT_SMART_SAVE), EnumConfigurationState.UNCONFIGURED_DEFAULT));
		return smartSave;
	}

	private static String extractCosFilter(RecommendationServiceConfig config,
			Map statuses) {
		String cosFilter = DEFAULT_COS_FILTER;
		String cosFilterStr = config.get(CKEY_COS_FILTER);
		if (cosFilterStr != null) {
			if (cosFilterStr.equalsIgnoreCase("corporate")
					|| cosFilterStr.equalsIgnoreCase("residential")
					|| cosFilterStr.equalsIgnoreCase("home")) {
				cosFilter = cosFilterStr;
				statuses.put(CKEY_COS_FILTER, new ConfigurationStatus(CKEY_COS_FILTER,
						cosFilter.toUpperCase(), EnumConfigurationState.CONFIGURED_OK));
			} else
				statuses.put(CKEY_COS_FILTER, new ConfigurationStatus(CKEY_COS_FILTER, cosFilterStr,
						null, EnumConfigurationState.CONFIGURED_WRONG_DEFAULT,
						"Unrecognized value defaulting to " + DEFAULT_COS_FILTER));
		} else
			statuses.put(CKEY_COS_FILTER, new ConfigurationStatus(CKEY_COS_FILTER, null,
					EnumConfigurationState.UNCONFIGURED_DEFAULT));
		return cosFilter;
	}

	private static void extractCartPresentation(RecommendationServiceConfig config,
			Map statuses) {
		ConfigurationStatus status;
		if (config.get(CKEY_PREZ_TITLE) != null)
			status = new ConfigurationStatus(CKEY_PREZ_TITLE, config.get(CKEY_PREZ_TITLE), EnumConfigurationState.CONFIGURED_OK);
		else
			status = new ConfigurationStatus(CKEY_PREZ_TITLE, null, EnumConfigurationState.UNCONFIGURED_OK).setWarning(
					"Missing value may cause visual issues.");
		
		statuses.put(CKEY_PREZ_TITLE, status);
		if (config.get(CKEY_PREZ_DESC) != null)
			status = new ConfigurationStatus(CKEY_PREZ_DESC, config.get(CKEY_PREZ_DESC), EnumConfigurationState.CONFIGURED_OK);
		else
			status = new ConfigurationStatus(CKEY_PREZ_DESC, null, EnumConfigurationState.UNCONFIGURED_OK).setWarning(
					"Missing value may cause visual issues.");
		statuses.put(CKEY_PREZ_DESC, status);
	}

	private static void extractFeaturedPresentation(
			RecommendationServiceConfig config, Map statuses) {
		if (config.get(CKEY_FI_LABEL) != null) {
			statuses.put(CKEY_FI_LABEL, new ConfigurationStatus(CKEY_FI_LABEL, config.get(CKEY_FI_LABEL), EnumConfigurationState.CONFIGURED_OK));
		} else {
			statuses.put(CKEY_FI_LABEL, new ConfigurationStatus(CKEY_FI_LABEL, null, DEFAULT_FI_LABEL, EnumConfigurationState.UNCONFIGURED_DEFAULT)
					.setWarning("Using default but may be inappropriate."));
			config.set(CKEY_FI_LABEL, DEFAULT_FI_LABEL);
		}
	}

	private static String extractFavoriteListId(
			RecommendationServiceConfig config, Map statuses) {
		String favoriteListId = DEFAULT_FAVORITE_LIST_ID;
        if (RecommendationServiceType.FAVORITES.equals(config.getType())) {
        	ConfigurationStatus status;
        	if (config.get(CKEY_FAVORITE_LIST_ID) != null) {
        		favoriteListId = config.get(CKEY_FAVORITE_LIST_ID);
        		status = new ConfigurationStatus(CKEY_FAVORITE_LIST_ID, favoriteListId, EnumConfigurationState.CONFIGURED_OK);
        	} else
        		status = new ConfigurationStatus(CKEY_FAVORITE_LIST_ID, favoriteListId, EnumConfigurationState.UNCONFIGURED_DEFAULT);
        	statuses.put(CKEY_FAVORITE_LIST_ID, status);
        }
		return favoriteListId;
	}

	public static ImpressionSampler configureSampler(RecommendationServiceConfig config, Map statuses) {
		ImpressionSampler sampler;
		Random R = new Random();

		// log configuration
		LOGGER.debug("configuration=" + config.toString());

		int topN = extractTopN(config, statuses);
		double topP = extractTopPercentage(config, statuses);

		final ContentSampler.ConsiderationLimit cl = new SimpleLimit(topP, topN);

		String samplingStrategy = extractSamplingStrategy(config, statuses);
		double exponent = extractExponent(config, statuses, samplingStrategy);

		if ("deterministic".equals(samplingStrategy)) {
			sampler = new ConfiguredImpressionSampler(cl, ListSampler.ZERO);
		} else if ("uniform".equals(samplingStrategy)) {
			sampler = new ConfiguredImpressionSampler(cl,
					new ListSampler.Uniform(R));
		} else if ("linear".equals(samplingStrategy)) {
			sampler = new ConfiguredImpressionSampler(cl,
					new ListSampler.Linear(R));
		} else if ("quadratic".equals(samplingStrategy)) {
			sampler = new ConfiguredImpressionSampler(cl,
					new ListSampler.Quadratic(R));
		} else if ("cubic".equals(samplingStrategy)) {
			sampler = new ConfiguredImpressionSampler(cl,
					new ListSampler.Cubic(R));
		} else if ("harmonic".equals(samplingStrategy)) {
			sampler = new ConfiguredImpressionSampler(cl,
					new ListSampler.Harmonic(R));
		} else if ("sqrt".equals(samplingStrategy)) {
			sampler = new ConfiguredImpressionSampler(cl,
					new ListSampler.SquareRootCDF(R));
		} else if ("power".equals(samplingStrategy)) {
			sampler = new ConfiguredImpressionSampler(cl,
					new ListSampler.PowerCDF(R, exponent));
		} else if ("complicated".equals(samplingStrategy)) {
			sampler = new ComplicatedImpressionSampler(cl);
		} else {
			LOGGER.warn("Invalid strategy: " + samplingStrategy);
			ConfigurationStatus status = (ConfigurationStatus) statuses.get(CKEY_SAMPLING_STRATEGY);
			throw new ConfigurationException(CKEY_SAMPLING_STRATEGY, status.getState());
		}

		// log sampler
		LOGGER.debug("Configured sampler: " + sampler);

		return sampler;
	}

	private static int extractTopN(RecommendationServiceConfig config,
			Map statuses) {
		int topN = DEFAULT_TOP_N;
		ConfigurationStatus status;
		try {
			topN = Integer.parseInt(config.get(CKEY_TOP_N, Integer.toString(DEFAULT_TOP_N)));
			if (config.get(CKEY_TOP_N) != null) {
				if (topN == DEFAULT_TOP_N)
					status = new ConfigurationStatus(CKEY_TOP_N, Integer.toString(DEFAULT_TOP_N), EnumConfigurationState.CONFIGURED_DEFAULT);
				else
					status = new ConfigurationStatus(CKEY_TOP_N, Integer.toString(topN), EnumConfigurationState.CONFIGURED_OK);
			} else {
				status = new ConfigurationStatus(CKEY_TOP_N, null, Integer.toString(DEFAULT_TOP_N), EnumConfigurationState.UNCONFIGURED_DEFAULT);			
			}
		} catch (NumberFormatException e) {
			status = new ConfigurationStatus(CKEY_TOP_N, config.get(CKEY_TOP_N), Integer.toString(DEFAULT_TOP_N),
					EnumConfigurationState.CONFIGURED_WRONG_DEFAULT).setWarning("Integer cannot be parsed, using default value");
		}
		statuses.put(CKEY_TOP_N, status);	
		LOGGER.debug("  TOP N: " + topN);
		return topN;
	}

	private static double extractTopPercentage(
			RecommendationServiceConfig config, Map statuses) {
		double topP = DEFAULT_TOP_P;
		ConfigurationStatus status;
		try {
			topP = Double.parseDouble(config.get(CKEY_TOP_PERC, Double.toString(DEFAULT_TOP_P)));
			if (config.get(CKEY_TOP_PERC) != null) {
				if (topP == DEFAULT_TOP_P)
					status = new ConfigurationStatus(CKEY_TOP_PERC, Double.toString(DEFAULT_TOP_P), EnumConfigurationState.CONFIGURED_DEFAULT);
				else
					status = new ConfigurationStatus(CKEY_TOP_PERC, Double.toString(topP), EnumConfigurationState.CONFIGURED_OK);
			} else {
				status = new ConfigurationStatus(CKEY_TOP_PERC, Double.toString(DEFAULT_TOP_P), EnumConfigurationState.UNCONFIGURED_DEFAULT);			
			}
		} catch (NumberFormatException e) {
			status = new ConfigurationStatus(CKEY_TOP_PERC, config.get(CKEY_TOP_PERC), Double.toString(DEFAULT_TOP_P),
					EnumConfigurationState.CONFIGURED_WRONG_DEFAULT).setWarning("Float cannot be parsed, using default value");
		}
		statuses.put(CKEY_TOP_PERC, status);	
		LOGGER.debug("  TOP %: " + topP);
		return topP;
	}

	private static String extractSamplingStrategy(RecommendationServiceConfig config, Map statuses) {
		ConfigurationStatus status;
		String samplingStrategy = config.get(CKEY_SAMPLING_STRATEGY);
		if (samplingStrategy == null) {
			if (RecommendationServiceType.RANDOM_DYF.equals(config.getType())) {
				samplingStrategy = "uniform";
				status = new ConfigurationStatus(CKEY_SAMPLING_STRATEGY, null, samplingStrategy, EnumConfigurationState.UNCONFIGURED_OVERRIDDEN);			
			} else if (RecommendationServiceType.FAVORITES.equals(config.getType())) {
				samplingStrategy = "deterministic";
				status = new ConfigurationStatus(CKEY_SAMPLING_STRATEGY, null, samplingStrategy, EnumConfigurationState.UNCONFIGURED_OVERRIDDEN);			
			} else {
				samplingStrategy = DEFAULT_SAMPLING_STRATEGY;
				status = new ConfigurationStatus(CKEY_SAMPLING_STRATEGY, null, samplingStrategy, EnumConfigurationState.UNCONFIGURED_DEFAULT);			
			}
		} else {
			samplingStrategy = samplingStrategy.toLowerCase();
			if (RecommendationServiceType.RANDOM_DYF.equals(config.getType())) {
				status = new ConfigurationStatus(CKEY_SAMPLING_STRATEGY, samplingStrategy, "uniform", EnumConfigurationState.CONFIGURED_OVERRIDDEN);			
				samplingStrategy = "uniform";
			} if (RecommendationServiceType.FAVORITES.equals(config.getType())) {
				status = new ConfigurationStatus(CKEY_SAMPLING_STRATEGY, samplingStrategy, "deterministic", EnumConfigurationState.CONFIGURED_OVERRIDDEN);			
				samplingStrategy = "deterministic";
			} else {
				boolean found = false;
				for (int i = 0; i < SAMPLERS.length; i++)
					if (samplingStrategy.equals(SAMPLERS[i])) {
						found = true;
						break;
					}
				if (found)
					status = new ConfigurationStatus(CKEY_SAMPLING_STRATEGY, samplingStrategy, EnumConfigurationState.CONFIGURED_OK);
				else
					status = new ConfigurationStatus(CKEY_SAMPLING_STRATEGY, samplingStrategy, EnumConfigurationState.CONFIGURED_WRONG,
							"Possible values are: " + ArrayUtils.toString(SAMPLERS));
			}
		}
		statuses.put(CKEY_SAMPLING_STRATEGY, status);	
		LOGGER.debug("  Sampling Strategy: " + samplingStrategy);
		return samplingStrategy;
	}
	
	private static double extractExponent(RecommendationServiceConfig config, Map statuses, String samplingStrategy) {
		String exponentStr = config.get(CKEY_EXPONENT);
		double exponent = DEFAULT_EXPONENT;
		ConfigurationStatus status = null;
		if ("power".equals(samplingStrategy)) {
			if (exponentStr != null)
				try {
					exponent = Double.parseDouble(exponentStr);
					if (exponent == DEFAULT_EXPONENT)
						status = new ConfigurationStatus(CKEY_EXPONENT, Double.toString(exponent), EnumConfigurationState.CONFIGURED_DEFAULT);
					else
						status = new ConfigurationStatus(CKEY_EXPONENT, Double.toString(exponent), EnumConfigurationState.CONFIGURED_OK);
				} catch (NumberFormatException e) {
					status = new ConfigurationStatus(CKEY_EXPONENT, config.get(CKEY_EXPONENT), Double.toString(DEFAULT_EXPONENT),
							EnumConfigurationState.CONFIGURED_WRONG_DEFAULT).setWarning("Float cannot be parsed, using default value");
				}
			else
				status = new ConfigurationStatus(CKEY_EXPONENT, null, Double.toString(DEFAULT_EXPONENT), EnumConfigurationState.UNCONFIGURED_DEFAULT);
			
			LOGGER.debug("  Exponent: " + exponent);
		} else {
			if (exponentStr != null)
				status = new ConfigurationStatus(CKEY_EXPONENT, exponentStr, EnumConfigurationState.CONFIGURED_UNUSED);
		}
		if (status != null)
			statuses.put(CKEY_EXPONENT, status);
		return exponent;
	}

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
	
            return services == null ? Collections.EMPTY_MAP : services;
	}
	
	public synchronized void refresh() {
	    siteFeatureServices = null;	    
	}

	public synchronized void refreshAll() {
		EnumSiteFeature.refresh();
		VariantSelectorFactory.refresh();
		refresh();
	}
	
	public Collection loadSiteFeatures() {
        try {
            SmartStoreServiceConfigurationSB sb;
			sb = getServiceConfigurationHome().create();
	        return sb.getSiteFeatures();
		} catch (RemoteException e) {
            LOGGER.warn("SmartStore Service Configuration", e);
            return Collections.EMPTY_LIST;
		} catch (CreateException e) {
            LOGGER.warn("SmartStore Service Configuration", e);
            return Collections.EMPTY_LIST;
		} catch (SQLException e) {
            LOGGER.warn("SmartStore Service Configuration", e);
            return Collections.EMPTY_LIST;
		}
	}
}
