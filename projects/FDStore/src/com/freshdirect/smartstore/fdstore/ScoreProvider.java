package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.impl.CandidateProductRecommendationService;
import com.freshdirect.smartstore.impl.FeaturedItemsRecommendationService;
import com.freshdirect.smartstore.impl.SessionCache;
import com.freshdirect.smartstore.scoring.DataAccess;

/**
 * 
 * This class is a Singleton.
 * 
 * It implements a caching strategy for personalized scores while caches all global scores at the product level.
 * Some factors come from CMS, these are looked up.
 * 
 * The following is needed to extend the factors.
 * <ul>
 * 	<li>In case of a database factor, add a new instance of {@link FactorRangeConverter}</li>
 * 	<li>In case of a CMS factor, add a new instance of {@link StoreLookup}</li> 
 * </ul>
 * 
 * These go into the {@link #ScoreProvider() constructor}.
 * 
 * Only those factors will be calculated which are necessarily 
 * needed (i.e. {@link #acquireFactors(Collection) explicitly acquired}).
 * 
 * All global (acquired) factors are cached. The personalized (acquired) factors are 
 * stored in an {@link SessionCache LRU cache}.
 * 
 * @author istvan
 *
 */
public class ScoreProvider implements DataAccess {
	
	private static final String[] DATASOURCE_NAMES = new String[] { "FeaturedItems", "CandidateLists", "PurchaseHistory" };
	
    // LOGGER
	private static Category LOGGER = LoggerFactory.getInstance(ScoreProvider.class);
	
	/**
	 * Database factors.
	 */
	protected abstract class DatabaseScoreRangeProvider implements ScoreRangeProvider {
	
		private Map factorIndexes = new HashMap();
		private List factors;
		
		// products in a particular order
		private List products = null;
		
		// double arrays in the same order as factorIndexes.values() 
		// thus factors
		// values in the same order as products
		// List<double[]>
		private List values = null;
		
		
		public void purge() {
			products = null;
			values = null;
		}
		
		protected List getProductNames() {
			return products;
		}
		
		protected boolean inCache() {
			return products != null && values != null;
		}
	 
		/**
		 * 
		 * @param dbProductScores assumed to contain only the scores in good order Map<ProductId:String,Scores:doube[]>
 		 */
		protected void reCache(Map dbProductScores) {
			
			List newProducts = new ArrayList(dbProductScores.size());
			List newValues = new ArrayList(factorIndexes.size());

			for(int i = 0; i<  factorIndexes.size(); ++i) {
				newValues.add(new double[dbProductScores.size()]);
			}
			
				
			int r = 0;
			for(Iterator i = dbProductScores.entrySet().iterator(); i.hasNext(); ++r) {
				Map.Entry entry = (Map.Entry)i.next();
				newProducts.add(entry.getKey());
				int c = 0;
				for(Iterator j = factors.iterator(); j.hasNext(); ++c) {
						
					String factor = j.next().toString();
					int idx = ((Number)factorIndexes.get(factor)).intValue();
					double[] scores = (double[])entry.getValue();
					double[] range = (double[])newValues.get(c);
					range[r] = scores[idx];
				}
			}
	
			synchronized (this) {
				products = newProducts;
				values = newValues;
			}
		}
		
		protected DatabaseScoreRangeProvider(List factors) {
			this.factors = factors;
			int idx = 0;
			for(Iterator i = factors.iterator(); i.hasNext();++idx) {
				factorIndexes.put(i.next(), new Integer(idx));
			}
		}
		
		protected List getFactorNames() {
			return factors;
		}
		
		protected int getFactorIndex(String factor) {
			return ((Number)factorIndexes.get(factor)).intValue();
		}
		
		protected double[] getCachedRange(String factor) {
			return (double[])values.get(((Number)factorIndexes.get(factor)).intValue());
		}
	}
	
	/**
	 * Global database factors.
	 */
	protected class GlobalScoreRangeProvider extends DatabaseScoreRangeProvider {

		public void cache(String userId) {
			if (!inCache()) {
				reCache(DatabaseScoreFactorProvider.getInstance().getGlobalFactors(getFactorNames()));
			}
		}
					
		protected GlobalScoreRangeProvider(List factors) {
			super(factors);
		}
		

		public List products(String userId) {
			cache(userId);
			return getProductNames();
		}

		public double[] getRange(String userId, String factor) {
			cache(userId);
			return getCachedRange(factor);
		}
	}
	
	/**
	 * Personalized database factors.
	 */
	protected class PersonalizedScoreRangeProvider extends DatabaseScoreRangeProvider {
		
		private String userId = null;
		
		protected PersonalizedScoreRangeProvider(List factors) {
			super(factors);
		}
		
		public void cache(String userId) {
			if (!inCache() || this.userId == null || !this.userId.equals(userId)) {
				synchronized (this) {
					this.userId = userId;
					reCache(DatabaseScoreFactorProvider.getInstance().getPersonalizedFactors(this.userId,getFactorNames()));
				}
			}
		}
		
		public void purge() {
			userId = null;
			super.purge();
		}

		public double[] getRange(String userId, String factor) {
			cache(userId);
			return getCachedRange(factor);
		}
		
		public ScoreRangeProvider replicate() {
			return new PersonalizedScoreRangeProvider(getFactorNames());
		}

		public List products(String userId) {
			cache(userId);
			return getProductNames();
		}
	}
	
	/**
	 * Information for factors;
	 */
	protected class FactorInfo {
		private Comparator lowerCaseComparator = new Comparator() {

			public int compare(Object s1, Object s2) {
				return s1.toString().compareToIgnoreCase(s2.toString());
			}
		};
		
		private SortedSet globalDBFactors = new TreeSet(lowerCaseComparator);
		private SortedSet personalizedDBFactors = new TreeSet(lowerCaseComparator);
		
		private FactorInfo() {	
			reloadNames();
		}
		
		public synchronized void reloadNames() {
			globalDBFactors.clear();
			personalizedDBFactors.clear();
			DatabaseScoreFactorProvider dbProvider = DatabaseScoreFactorProvider.getInstance();
			globalDBFactors.addAll(dbProvider.getGlobalFactorNames());
			personalizedDBFactors.addAll(dbProvider.getPersonalizedFactorNames());			
		}

		public boolean isGlobal(String name) {
			return isOnline(name) || globalDBFactors.contains(name);
		}

		public boolean isOffline(String name) {
			return globalDBFactors.contains(name) || personalizedDBFactors.contains(name);
		}

		public boolean isPersonalized(String name) {
			return personalizedDBFactors.contains(name);
		}

		public boolean isOnline(String name) {
			return storeLookups.containsKey(name);
		}
	}
	
	
	private FactorInfo factorInfo = null;
	private ScoreRangeProvider personalizedScoreRangeProvider;
	private ScoreRangeProvider globalScoreRangeProvider;
	
	private static ScoreProvider instance = null;
	
	/**
	 * Get instance.
	 * 
	 * @return the only thread local instance
	 */
	public static synchronized ScoreProvider getInstance() {
		if (instance == null) {
			instance = new ScoreProvider();
		}
		return instance;
	}
	
	/**
	 * Just for testing purposes, do not use!
	 * @param ins
	 */
	public static synchronized void setInstance(ScoreProvider ins) {
	    instance = ins;
	}
	
	
	/**
	 * Get available factors.
	 * 
	 * @return Set<String> name of the factors which have associated handlers
	 */
	public Set getAvailableFactors() {
		Set result = new HashSet();
		result.addAll(storeLookups.keySet());
		result.addAll(rangeConverters.keySet());
		return result;
	}
	
	// Map<Factor:String,IndexInDoubleArray:Integer> score index
	// Score indexes tell what position the score is stored in globalScores or personalizesScores
	// in the double array
	private Map globalIndexes = new TreeMap();
	private Map personalizedIndexes = new TreeMap();
	
	
	// Map<ContentKey, double[]>
	private Map globalScores = new HashMap();
	
	// SessionCache<UserId:String, SessionCache.TimedEntry<Map<ContentKey,double[]>>>
	private SessionCache personalizedScores = new SessionCache(FDStoreProperties.getSmartstorePersonalizedScoresCacheEntries(),0.75f);
	
	protected Map storePersonalizedScores(String userId) {
		SessionCache.TimedEntry entry = (SessionCache.TimedEntry)personalizedScores.get(userId);
		if (entry == null || entry.expired()) {
			
			try {
				entry = new SessionCache.TimedEntry(
						loadPersonalizedDBScores(userId),
						1000*FDStoreProperties.getSmartstorePersonalizedScoresCacheTimeout()
				);
			} catch (Exception e) {
				LOGGER.debug("Could not load personalized scores for " + userId, e);
				return Collections.EMPTY_MAP;
			}
			LOGGER.info("Caching personalized scores for " + userId);
			personalizedScores.put(userId,entry);
		}
		return (Map)entry.getPayload();
	}
	
	protected void purgePersonalizedScores(String userId) {
		personalizedScores.remove(userId);
	}
	
	private double getPersonalizedScore(String userId, ContentKey key, int idx) {
		if (userId == null) {
			return 0;
		}
		Map userScores = storePersonalizedScores(userId);
		double[] scores = (double[])userScores.get(key);
		return scores == null ? 0 : scores[idx];
	}
	
	private double getGlobalScore(ContentKey key, int idx) {
		double[] scores = (double[])globalScores.get(key);
		return scores == null ? 0 : scores[idx];
	}
	
	/**
	 * Get the scores for the requested content key.
	 * 
	 * @param userId customer id, may be null for non-personalized factors
	 * @param contentKey 
	 * @param variables requested variables
	 * @return scores in the order of variables
	 */
	public double[] getVariables(String userId, ContentKey contentKey, String[] variables) {
		return getVariables(userId, ContentFactory.getInstance().getContentNodeByKey(contentKey),variables);
	}
	
	/**
	 * Get the scores for the requested content node.
	 * 
	 * @param userId customerId, may be null for non-personalized factors
	 * @param contentNode 
	 * @param variables requested variables
	 * @return scores in the order of variables  
	 */
	public double[] getVariables(String userId, ContentNodeModel contentNode, String[] variables) {
		double[] result = new double[variables.length];
		
		for(int i = 0; i < variables.length; ++i) {
			String var = variables[i];
			if (contentNode == null) {
				result[i] = 0;
			} else if (personalizedIndexes.containsKey(var)) { // personalized factor
				result[i] = getPersonalizedScore(userId,contentNode.getContentKey(),((Number)personalizedIndexes.get(var)).intValue());
			} else if (globalIndexes.containsKey(var)) {
				result[i] = getGlobalScore(contentNode.getContentKey(),((Number)globalIndexes.get(var)).intValue());
			} else if (storeLookups.containsKey(var)) {
				result[i] = ((StoreLookup)storeLookups.get(var)).getVariable(contentNode);
			} else {
				LOGGER.debug("Unknown variable " + var);
				result[i] = 0;
			}
		}
		return result;	
	}
	
	/**
	 * Test if factor is global.
	 * 
	 * @param factor factor name
	 * @return whether factor is global
	 */
	public boolean isGlobal(String factor) {
		return globalIndexes.containsKey(factor) || storeLookups.containsKey(factor);
	}
	
	/**
	 * Test if factor is personalized.
	 * @param factor factor name.
	 * @return whether factor is personalized
	 */
	public boolean isPersonalized(String factor) {
		return personalizedIndexes.containsKey(factor);
	}

	/**
	 * Get content associated with data source.
	 * 
	 * @param name of data source
	 * @param input session input
	 * @return List<{@link ContentNodeModel>}
	 */
	public List getDatasource(SessionInput input, String name) {
		if ("FeaturedItems".equals(name)) {
			if (input.getCurrentNode() != null) {
				return FeaturedItemsRecommendationService.getFeaturedItems(input.getCurrentNode());
			}
		} else if ("CandidateLists".equals(name)) {
			if (input.getCurrentNode() instanceof CategoryModel) {
				return CandidateProductRecommendationService.collectCandidateProductsNodes((CategoryModel)input.getCurrentNode());
			}
		} else if ("PurchaseHistory".equals(name)) {
			if (input.getCustomerId() != null) {
				try {
					Map scores = storePersonalizedScores(input.getCustomerId());
					List result = new ArrayList(scores.size());
					for(Iterator i = scores.keySet().iterator(); i.hasNext();) {
						ContentKey key = (ContentKey)i.next();
						try {
						    ContentNodeModel nodeModel = ContentFactory.getInstance().getContentNodeByKey(key);
						    if (nodeModel!=null) {
						        result.add(nodeModel);
						    }
						} catch (Exception e) {
							LOGGER.debug("Problem with " + key,e);
						}
					}
					return result;
				} catch (Exception e) {
					LOGGER.debug("Could not load history for " + input.getCustomerId());
				}
			}
	    }
	    return Collections.EMPTY_LIST;
	}
	
	public String[] getDatasourceNames() {
	    return DATASOURCE_NAMES;
	}
	
	
	/**
	 * Acquire all available factors.
	 * 
	 * @return actually acquired factors
	 */
	public List acquireAllFactors() {
		return acquireFactors(getAvailableFactors());
	}
	
	/**
	 * Acquire the given factors.
	 * 
	 * All of the following will happen:
	 * <ol>
	 * 	<li>The database columns will be read</li>
	 * 	<li>All global factors will be (re)cached</li>
	 * 	<li>Available personalized factors will be validated</li>
	 * </ol>
	 *   
	 * @param names Collection<{@link String}>
	 * @return the names of actually acquired factors
	 */
	public synchronized List acquireFactors(Collection names) {
		
		factorInfo.reloadNames();
	
		Set personalizedFactors = new HashSet();

		Set globalDBFactors = new HashSet();
		
		Set rawPersonalizedFactors = new HashSet();
		Set rawGlobalFactors = new HashSet();
		
		List result = new ArrayList();
		
		NEXT_FACTOR: for(Iterator i = names.iterator(); i.hasNext();) {
			String name = i.next().toString();
			if (rangeConverters.containsKey(name)) {
				FactorRangeConverter converter = (FactorRangeConverter)rangeConverters.get(name);
				for (Iterator j = converter.requiresGlobalDatabaseColumns().iterator(); j.hasNext();) {
					String column = j.next().toString();
					if (!factorInfo.isOffline(column) || !factorInfo.isGlobal(column)) {
						LOGGER.warn("No column " + column + " in global factors");
						continue NEXT_FACTOR;
					}
					rawGlobalFactors.add(column);
				}
				for(Iterator j = converter.requiresPersonalizedDatabaseColumns().iterator(); j.hasNext();) {
					String column = j.next().toString();
					if (!factorInfo.isOffline(column) && !factorInfo.isPersonalized(column)) {
						LOGGER.warn("No column " + column + " in personalized factors");
						continue NEXT_FACTOR;
					}
					rawPersonalizedFactors.add(column);
				}
				if (converter.isPersonalized()) {
					personalizedFactors.add(name);
				} else {
					globalDBFactors.add(name);
				}
				result.add(name);
			} else if (storeLookups.containsKey(name)) {
				result.add(name);
			} else {
				LOGGER.warn("Neither database nor CMS source");
			} 
		}
		
		globalScoreRangeProvider = new GlobalScoreRangeProvider(new ArrayList(rawGlobalFactors));
		personalizedScoreRangeProvider = new PersonalizedScoreRangeProvider(new ArrayList(rawPersonalizedFactors));
		
		personalizedIndexes.clear();
		for(Iterator i = personalizedFactors.iterator(); i.hasNext();) {
			String factor = i.next().toString();
			personalizedIndexes.put(factor,new Integer(personalizedIndexes.size()));
		}
		
		globalIndexes.clear();
		for(Iterator i = globalDBFactors.iterator(); i.hasNext();) {
			String factor = i.next().toString();
			globalIndexes.put(factor, new Integer(globalIndexes.size()));
		}
		
		try {
			globalScores = loadGlobalDBScores();
			LOGGER.info("Caching global scores");
		} catch (Exception e) {
			LOGGER.debug("Could not cache global scores");
			e.printStackTrace();
			throw new FDRuntimeException(e);
		}
		
		return result;
	}
	
	private Map loadGlobalDBScores() throws Exception {
		
		
		List products = globalScoreRangeProvider.products(null);
		
		Map result = new HashMap(5*products.size()/3+1,0.75f);
		
		for(Iterator i = products.iterator(); i.hasNext();) {
			result.put(new ContentKey(FDContentTypes.PRODUCT,i.next().toString()), new double[globalIndexes.size()]);
		}
		
		for (Iterator i = globalIndexes.entrySet().iterator(); i.hasNext();) {
			// Map.Entry<String,Number>
			Map.Entry entry = (Map.Entry)i.next();
			FactorRangeConverter converter = (FactorRangeConverter)rangeConverters.get(entry.getKey().toString());
			double[] values = converter.map(null,globalScoreRangeProvider);
			
			if (values.length != products.size()) {
				throw new FDRuntimeException(
					"Product list length and range values size differ: " + values.length + " and " + products.size());
			}
			for(int j=0; j< values.length; ++j) {
				double[] productScores = ((double[])result.get(new ContentKey(FDContentTypes.PRODUCT,products.get(j).toString())));
				productScores[((Number)entry.getValue()).intValue()] = values[j];
			}
		}
		globalScoreRangeProvider.purge();
		return result;
	}
	
	
	private Map loadPersonalizedDBScores(String erpCustomerId) throws Exception {
		
		ScoreRangeProvider personalScores = ((PersonalizedScoreRangeProvider)personalizedScoreRangeProvider).replicate();
		
		List products = personalScores.products(erpCustomerId);
		
		Map result = new HashMap(5*products.size()/3+1,0.75f);
		
		for(Iterator i = products.iterator(); i.hasNext();) {
			result.put(new ContentKey(FDContentTypes.PRODUCT,i.next().toString()), new double[personalizedIndexes.size()]);
		}
		
		for (Iterator i = personalizedIndexes.entrySet().iterator(); i.hasNext();) {
			// Map.Entry<String,Number>
			Map.Entry entry = (Map.Entry)i.next();
			FactorRangeConverter converter = (FactorRangeConverter)rangeConverters.get(entry.getKey().toString());
			double[] values = converter.map(erpCustomerId,personalScores);
			
			if (values.length != products.size()) {
				throw new FDRuntimeException(
					"Product list length and range values size differ: " + values.length + " and " + products.size());
			}
			for(int j=0; j< values.length; ++j) {
				double[] productScores = ((double[])result.get(new ContentKey(FDContentTypes.PRODUCT,products.get(j).toString())));
				productScores[((Number)entry.getValue()).intValue()] = values[j];
			}
		}
		return result;
	}
	
	/**
	 * Get all scores for the given users.
	 * 
	 * @param userIds customer id, if null, all available global scores are returned
	 * @return scores in a table
	 * @throws Exception
	 */
	public ScoresTable getAllScores(List userIds) throws Exception {
		
		
		if (userIds != null) { // personalized scores
			final String[] factors = new String[personalizedIndexes.size() + globalIndexes.size() + storeLookups.size()];
			
			int j = 0;
			for(Iterator i = personalizedIndexes.keySet().iterator(); i.hasNext();++j) {
				String factor = i.next().toString();
				factors[j] = factor;
			}
			
			for(Iterator i = globalIndexes.keySet().iterator(); i.hasNext(); ++j) {
				String factor = i.next().toString();
				factors[j] = factor;
				
			}
			
			for(Iterator i = storeLookups.keySet().iterator(); i.hasNext();++j) {
				String factor = i.next().toString();
				factors[j] = factor;
			}
			
			final List values = new ArrayList();
			
			for(Iterator i = userIds.iterator(); i.hasNext();) {
				String userId = i.next().toString();
				Set productIds = DatabaseScoreFactorProvider.getInstance().getPersonalizedProducts(userId);
				
				for(Iterator pi = productIds.iterator(); pi.hasNext();) {
					String productId = pi.next().toString();
					List row = new ArrayList(factors.length + 2);
					row.add(userId);
					row.add(productId);
					
					double[] scores = getVariables(userId,new ContentKey(FDContentTypes.PRODUCT, productId), factors);
					for(int s = 0; s < scores.length; ++s) {
						row.add(new Double(scores[s]));
					}
					values.add(row);
				}
			}
			
			return new ScoresTable() {				
				
				private static final long serialVersionUID = 4253324339031347406L;

				protected void init() {
					addColumn("CUSTOMER_ID", String.class);
					addColumn("PRODUCT_ID", ContentKey.class);
					
					for(int i = 0; i < factors.length; ++i) {
						addColumn(factors[i], Number.class);
					}
				}

				public Iterator getRows() {
					return values.iterator();
				}
			};
		} else {
			final String[] factors = new String[globalIndexes.size() + storeLookups.size()];
			
			int j = 0;
			
			for(Iterator i = globalIndexes.keySet().iterator(); i.hasNext(); ++j) {
				String factor = i.next().toString();
				factors[j] = factor;
				
			}
			
			for(Iterator i = storeLookups.keySet().iterator(); i.hasNext();++j) {
				String factor = i.next().toString();
				factors[j] = factor;
			}
			
			final List values = new ArrayList();
			
			Set productIds = DatabaseScoreFactorProvider.getInstance().getGlobalProducts();
			
			for(Iterator pi = productIds.iterator(); pi.hasNext();) {
				String productId = pi.next().toString();
				List row = new ArrayList(factors.length + 1);
				row.add(productId);
				
				double[] scores = getVariables(null,new ContentKey(FDContentTypes.PRODUCT, productId), factors);
				for(int s = 0; s < scores.length; ++s) {
					row.add(new Double(scores[s]));
				}
				values.add(row);
			}
			
			return new ScoresTable() {

				private static final long serialVersionUID = -3700648053073552014L;
				
				protected void init() {
					addColumn("PRODUCT_ID", ContentKey.class);
					
					for(int i = 0; i < factors.length; ++i) {
						addColumn(factors[i], Number.class);
					}
				}

				public Iterator getRows() {
					return values.iterator();
				}
				
			};
		}
		
	}
	
	
	// Map<Factor:String,FactorRangeConverter>
	private Map rangeConverters = new HashMap();
	
	// Map<Factor:String,StoreLookup>
	private Map storeLookups = new HashMap();
	
	protected ScoreProvider() {	
		LOGGER.info("Personalized cache entries: " + FDStoreProperties.getSmartstorePersonalizedScoresCacheEntries());
		LOGGER.info("Personalized cache timeout (seconds): " + FDStoreProperties.getSmartstorePersonalizedScoresCacheTimeout());
		
		factorInfo = new FactorInfo();
		
		reloadFactorHandlers();
	}
	
	public void reloadFactorHandlers() {
		
		globalScores.clear();
		personalizedScores.clear();
		
		// Store lookups
		storeLookups.put(
			"DealsPercentage", 
			FactorUtil.getDealsPercentageLookup()
		);
		
		storeLookups.put(
			"DealsPercentage_Discretized",
			FactorUtil.getDealsPercentageDiscretized()
		);
		
		storeLookups.put(
			"ExpertWeight",
			FactorUtil.getExpertWeightLookup()
		);
		
		storeLookups.put(
			"ExpertWeight_Normalized",
			FactorUtil.getNormalizedExpertWeightLookup()
		);
		
		storeLookups.put(
			"ProduceRating",
			FactorUtil.getProduceRatingLookup()
		);
		
		storeLookups.put(
			"ProduceRating_Normalized",
			FactorUtil.getNormalizedProduceRatingLookup()
		);
		
		storeLookups.put(
			"ProduceRating_Discretized1",
			FactorUtil.getDescretizedProduceRatingLookup1()
		);
		
		storeLookups.put(
			"ProduceRating_Discretized2",
			FactorUtil.getDescretizedProduceRatingLookup2()
		);
		// Database scores
		
		
		// FREQUENCY 
		rangeConverters.put(
			"Frequency",
			FactorRangeConverter.getRawPersonalizedScores(FactorUtil.PERSONALIZED_FREQUENCY_COLUMN)
		);
		
		rangeConverters.put(
			"Frequency_Normalized",
			FactorUtil.getMaxNormalizedPersonalConverter(FactorUtil.PERSONALIZED_FREQUENCY_COLUMN)
		);
			
		rangeConverters.put(
			"Frequency_Discretized",
			FactorUtil.getLogDiscretizedPersonalConverter(FactorUtil.PERSONALIZED_FREQUENCY_COLUMN, 2)
		);
		
		
		// GLOBAL POPULARITY
		rangeConverters.put(
			"Popularity",
			FactorRangeConverter.getRawGlobalScores(FactorUtil.GLOBAL_POPULARITY_COLUMN)
		);
			
		rangeConverters.put(
			"Popularity_Normalized",
			FactorUtil.getMaxNormalizedGlobalConvereter(FactorUtil.GLOBAL_POPULARITY_COLUMN)
		);
		
		rangeConverters.put(
			"Popularity_NormalizedDepartment",
			FactorUtil.getDepartmentMaxNormalizedGlobalConverter(FactorUtil.GLOBAL_POPULARITY_COLUMN)
		);
		
		rangeConverters.put(
			"Popularity_Discretized",
			FactorUtil.getLogDiscretizedGlobalConverter(FactorUtil.GLOBAL_POPULARITY_COLUMN, 2)
		);
		
		
		// QUANTITY
		rangeConverters.put(
			"Quantity",
			FactorRangeConverter.getRawPersonalizedScores(FactorUtil.PERSONALIZED_QUANTITY_COLUMN)
		);
		
		rangeConverters.put(
			"Quantity_Normalized",
			FactorUtil.getMaxNormalizedPersonalConverter(FactorUtil.PERSONALIZED_QUANTITY_COLUMN)
		);
		
		
		rangeConverters.put(
			"Quantity_Discretized",
			FactorUtil.getLogDiscretizedPersonalConverter(FactorUtil.PERSONALIZED_QUANTITY_COLUMN, 2)
		);
		
		// AMOUNT SPENT
		rangeConverters.put(
			"AmountSpent",
			FactorRangeConverter.getRawPersonalizedScores(FactorUtil.PERSONALIZED_AMOUNT_COLUMN)
		);
		
		rangeConverters.put(
				"AmountSpent_Normalized",
				FactorUtil.getMaxNormalizedPersonalConverter(FactorUtil.PERSONALIZED_AMOUNT_COLUMN)
			);
		
		rangeConverters.put(
			"AmountSpent_Discretized",
			FactorUtil.getLogDiscretizedPersonalConverter(FactorUtil.PERSONALIZED_AMOUNT_COLUMN, 2)
		);
		
		// RECENCY
		rangeConverters.put(
			"Recency",
			FactorRangeConverter.getRawPersonalizedScores(FactorUtil.PERSONALIZED_RECENT_FREQUENCY_COLUMN)
		);
		
		rangeConverters.put(
			"Recency_Normalized",
			FactorUtil.getMaxNormalizedPersonalConverter(FactorUtil.PERSONALIZED_RECENT_FREQUENCY_COLUMN)
		);
		
		rangeConverters.put(
			"Recency_Discretized",
			FactorUtil.getLogDiscretizedPersonalConverter(FactorUtil.PERSONALIZED_RECENT_FREQUENCY_COLUMN, 3)
		);
			
		
		// REORDER RATE	
		rangeConverters.put(
			"ReorderRate",
			FactorUtil.getReorderRateConverter(66)
		);
		
		rangeConverters.put(
			"ReorderRate_Normalized",
			FactorUtil.getNormalizedReorderRateConverter(66)
		);
		
		rangeConverters.put(
			"ReorderRate_DepartmentNormalized",
			FactorUtil.getDepartmentNormalizedReorderRateConverter(66)
		);
		
		rangeConverters.put(
			"ReorderRate_Discretized",
			FactorUtil.getDiscretizedReorderRateConverter(2)
		);
		
		// SEASONALITY
		rangeConverters.put(
			"Seasonality",
			FactorUtil.getSeasonalityConverter(3.)
		);
		
		rangeConverters.put(
			"Seasonality_Discretized",
			FactorUtil.getDiscretizedSeasonality()
		);
		
		// ORIGINAL SCORES
		rangeConverters.put(
			"OriginalScores_Personalized",
			FactorRangeConverter.getRawPersonalizedScores("Score")
		);
		
		rangeConverters.put(
			"OriginalScores_Global",
			FactorRangeConverter.getRawGlobalScores("Score")
		);
		
		
		/* These are for testing "graceful" handling of nonexisting factors
		rangeConverters.put(
			"NincsOttGlobal",
			FactorRangeConverter.getRawGlobalScores("GlobalNincsOszlop")
		);
		
		rangeConverters.put(
			"NincsOttPersonalized",
			FactorRangeConverter.getRawPersonalizedScores("PersonalizedNincsOszlop")
		);
		*/
		
	}
	
}
