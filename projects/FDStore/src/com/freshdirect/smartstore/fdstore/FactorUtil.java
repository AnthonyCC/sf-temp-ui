package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;


/**
 * Custom {@link FactorRangeConverter} and {@link StoreLookup} implementations.
 * 
 * @author istvan
 *
 */
public class FactorUtil {	
	// GLOBAL FACTORS columns, when moved to JDK 1.5+ these should be enums
	public static String GLOBAL_POPULARITY_COLUMN = "POPULARITY";
	public static String GLOBAL_REORDER_BUYER_COUNT_COLUMN = "REORDERBUYERCOUNT";
	public static String GLOBAL_TOTAL_BUYER_COUNT_COLUMN = "TOTALBUYERCOUNT";
	public static String GLOBAL_WEEKLY_FREQUENCY_COLUMN = "WEEKLYFREQUENCY";
	public static String GLOBAL_AVERAGE_FREQUENCY_COLUMN = "AVERAGEFREQUENCY";
	
	// PERSONALIZED FACTORS columns, when moved to JDK 1.5+ these should be enums
	public static String PERSONALIZED_FREQUENCY_COLUMN = "FREQUENCY";
	public static String PERSONALIZED_RECENT_FREQUENCY_COLUMN = "RECENT_FREQUENCY";
	public static String PERSONALIZED_QUANTITY_COLUMN = "QUANTITY";
	public static String PERSONALIZED_AMOUNT_COLUMN = "AMOUNT";
	
	private static class DealsCache extends OnlineScoreCache {
		public double calculateVariable(ContentNodeModel contentNode) {
			return contentNode instanceof ProductModel ? 
					((double) ((ProductModel) contentNode).getDealPercentage()) : 0.0;
		}
	}

	private static class TieredDealsCache extends OnlineScoreCache {
		public double calculateVariable(ContentNodeModel contentNode) {
			return contentNode instanceof ProductModel ? 
					((double) ((ProductModel) contentNode).getTieredDealPercentage()) : 0.0;
		}
	}

	private static class HighestDealsCache extends OnlineScoreCache {
		public double calculateVariable(ContentNodeModel contentNode) {
			return contentNode instanceof ProductModel ? 
					((double) ((ProductModel) contentNode).getHighestDealPercentage()) : 0.0;
		}
	}

	private static class ProduceRatingCache extends OnlineScoreCache {
		public double calculateVariable(ContentNodeModel contentNode) {
			try {
				return contentNode instanceof ProductModel ?
						((Number) ordinals.get( ((ProductModel) contentNode).getProductRating() )).doubleValue()
						: 0.0;
			} catch (FDResourceException e) {
				return 0.0;
			}
		}
	}
	
	private static OnlineScoreCache dealsCache = new DealsCache();
	private static OnlineScoreCache tieredDealsCache = new TieredDealsCache();
	private static OnlineScoreCache highestDealsCache = new HighestDealsCache();
	private static OnlineScoreCache produceRatingCache = new ProduceRatingCache();
	
	/**
	 * Get a CSM lookup which returns "Deals Percentage".
	 * 
	 * @return StoreLookup
	 */
	public static StoreLookup getDealsPercentageLookup() {
		return new CachingStoreLookup(dealsCache) {
			public double getVariable(ContentNodeModel contentNode) {
				return super.getVariable(contentNode) / 100.0;
			}	
		};
	}
	
	public static StoreLookup getDealsPercentageDiscretized() {
		return new CachingStoreLookup(dealsCache) {
			public double getVariable(ContentNodeModel contentNode) {
				return Math.floor(super.getVariable(contentNode) / 5.0);
			}
		};
	}

	/**
	 * Get a CSM lookup which returns "Tiered Deals Percentage".
	 * 
	 * @return StoreLookup
	 */
	public static StoreLookup getTieredDealsPercentageLookup() {
		return new CachingStoreLookup(tieredDealsCache) {
			public double getVariable(ContentNodeModel contentNode) {
				return super.getVariable(contentNode) / 100.0;
			}	
		};
	}
	
	public static StoreLookup getTieredDealsPercentageDiscretized() {
		return new CachingStoreLookup(tieredDealsCache) {
			public double getVariable(ContentNodeModel contentNode) {
				return Math.floor(super.getVariable(contentNode) / 5.0);
			}
		};
	}
	
	/**
	 * Get a CSM lookup which returns "Highest Deals Percentage".
	 * 
	 * @return StoreLookup
	 */
	public static StoreLookup getHighestDealsPercentageLookup() {
		return new CachingStoreLookup(highestDealsCache) {
			public double getVariable(ContentNodeModel contentNode) {
				return super.getVariable(contentNode) / 100.0;
			}	
		};
	}
	
	public static StoreLookup getHighestDealsPercentageDiscretized() {
		return new CachingStoreLookup(highestDealsCache) {
			public double getVariable(ContentNodeModel contentNode) {
				return Math.floor(super.getVariable(contentNode) / 5.0);
			}
		};
	}
	
	/**
	 * Get CMS lookup which returns "Expert Weight".
	 * @return StoreLookup
	 */
	public static StoreLookup getExpertWeightLookup() {
		return new StoreLookup() {

			public double getVariable(ContentNodeModel contentNode) {
				return 
					contentNode instanceof ProductModel ?
							((ProductModel)contentNode).getExpertWeight() : 0;
			}
			
		};
	}
	
	/**
	 * Get CMS lookup which returns "Expert Weight" on a 0 - 1 scale.
	 * @return StoreLookup
	 */
	public static StoreLookup getNormalizedExpertWeightLookup() {
		return new StoreLookup() {
			public double getVariable(ContentNodeModel contentNode) {
				int ew = 
					contentNode instanceof ProductModel ?
						((ProductModel)contentNode).getExpertWeight() : 0;
				switch(ew) {
					case -5: return 0;
					case -4: return 0.1;
					case -3: return 0.2;
					case -2: return 0.3;
					case -1: return 0.4;
					case 0: return 0.5;
					case 1: return 0.6;
					case 2: return 0.7;
					case 3: return 0.8;
					case 4: return 0.9;
					case 5: return 1;
					default:
						return 0.5;
				}
			}
		};
	}
	
	private final static Map ordinals = new HashMap();
	static {
            ordinals.put(null,new Integer(0));
            ordinals.put("",new Integer(0));
            ordinals.put(EnumOrderLineRating.TERRIBLE.getStatusCodeInDisplayFormat(),new Integer(1));
            ordinals.put(EnumOrderLineRating.BELOW_AVG.getStatusCodeInDisplayFormat(),new Integer(2));
            ordinals.put(EnumOrderLineRating.BELOW_AVG_PLUS.getStatusCodeInDisplayFormat(),new Integer(3));
            ordinals.put(EnumOrderLineRating.AVERAGE.getStatusCodeInDisplayFormat(),new Integer(4));
            ordinals.put(EnumOrderLineRating.NEVER_RATED.getStatusCodeInDisplayFormat(),new Integer(5));
            ordinals.put(EnumOrderLineRating.NO_RATING.getStatusCodeInDisplayFormat(),new Integer(6));
            ordinals.put(EnumOrderLineRating.AVERAGE_PLUS.getStatusCodeInDisplayFormat(),new Integer(7));
            ordinals.put(EnumOrderLineRating.GOOD.getStatusCodeInDisplayFormat(),new Integer(8));
            ordinals.put(EnumOrderLineRating.VERY_GOOD.getStatusCodeInDisplayFormat(),new Integer(9));
            ordinals.put(EnumOrderLineRating.GOOD_PLUS.getStatusCodeInDisplayFormat(),new Integer(10));
            ordinals.put(EnumOrderLineRating.PEAK_PRODUCE_8.getStatusCodeInDisplayFormat(),new Integer(11));
            ordinals.put(EnumOrderLineRating.VERY_GOOD.getStatusCodeInDisplayFormat(),new Integer(12));
            ordinals.put(EnumOrderLineRating.PEAK_PRODUCE_9.getStatusCodeInDisplayFormat(),new Integer(13));
            ordinals.put(EnumOrderLineRating.VERY_GOOD_PLUS.getStatusCodeInDisplayFormat(),new Integer(14));
            ordinals.put(EnumOrderLineRating.PEAK_PRODUCE_10.getStatusCodeInDisplayFormat(),new Integer(15));
            
            ordinals.put(EnumOrderLineRating.TERRIBLE.getStatusCode(),new Integer(1));
            ordinals.put(EnumOrderLineRating.BELOW_AVG.getStatusCode(),new Integer(2));
            ordinals.put(EnumOrderLineRating.BELOW_AVG_PLUS.getStatusCode(),new Integer(3));
            ordinals.put(EnumOrderLineRating.AVERAGE.getStatusCode(),new Integer(4));
            ordinals.put(EnumOrderLineRating.NEVER_RATED.getStatusCode(),new Integer(5));
            ordinals.put(EnumOrderLineRating.NO_RATING.getStatusCode(),new Integer(6));
            ordinals.put(EnumOrderLineRating.AVERAGE_PLUS.getStatusCode(),new Integer(7));
            ordinals.put(EnumOrderLineRating.GOOD.getStatusCode(),new Integer(8));
            ordinals.put(EnumOrderLineRating.VERY_GOOD.getStatusCode(),new Integer(9));
            ordinals.put(EnumOrderLineRating.GOOD_PLUS.getStatusCode(),new Integer(10));
            ordinals.put(EnumOrderLineRating.PEAK_PRODUCE_8.getStatusCode(),new Integer(11));
            ordinals.put(EnumOrderLineRating.VERY_GOOD.getStatusCode(),new Integer(12));
            ordinals.put(EnumOrderLineRating.PEAK_PRODUCE_9.getStatusCode(),new Integer(13));
            ordinals.put(EnumOrderLineRating.VERY_GOOD_PLUS.getStatusCode(),new Integer(14));
            ordinals.put(EnumOrderLineRating.PEAK_PRODUCE_10.getStatusCode(),new Integer(15));
	}
		
	public static StoreLookup getProduceRatingLookup() {
		return new CachingStoreLookup(produceRatingCache) {
			public double getVariable(ContentNodeModel contentNode) {
				return super.getVariable(contentNode);
			}
		};
	}
	
	public static StoreLookup getNormalizedProduceRatingLookup() {
		return new CachingStoreLookup(produceRatingCache) {		
			//   0,    1,    2,    3,    4,   5,   6,    7,   8,   9,  10,  11,  12,  13,  14,  15
			private double[] scores = 
				{0, -0.8, -0.6, -0.4, -0.2, 0.0,  0.0, 0.0, 0.2, 0.4, 0.6, 0.6, 0.8, 0.8, 1.0, 1.0};

			public double getVariable(ContentNodeModel contentNode) {
				return scores[(int) super.getVariable(contentNode)];
			}
			
			
		};
	}
	
	public static StoreLookup getDescretizedProduceRatingLookup1() {
		return new CachingStoreLookup(produceRatingCache) {		
			//   0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15
			private double[] scores = 
				{0,  1,  2,  3,  4,  0,  0,  5,  6,  7,  8,  8,  9,  9, 10, 10};

			public double getVariable(ContentNodeModel contentNode) {
				return scores[(int) super.getVariable(contentNode)];
			}	
		};		
	}
	
	public static StoreLookup getDescretizedProduceRatingLookup2() {
		return new CachingStoreLookup(produceRatingCache) {		
			//   0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15
			private double[] scores = 
				{0, -4, -3, -2, -1,  0,  0,  0,  1,  2,  3,  3,  4,  4,  5,  5};

			public double getVariable(ContentNodeModel contentNode) {
				return scores[(int) super.getVariable(contentNode)];
			}	
		};		
	}
	
	public static StoreLookup getNewnessLookup() {
		return new StoreLookup() {
			public double getVariable(ContentNodeModel contentNode) {
				Number n = null;
				try {
					n = (Number) ContentFactory.getInstance().getProductNewnesses().get(contentNode);
				} catch (FDResourceException e) {
				}
				return n != null ? n.doubleValue() : Integer.MIN_VALUE;
			}
		};
	}
	
	protected static class ReorderRateConverter extends FactorRangeConverter {

		private Set dbColumns =
			new HashSet(Arrays.asList(new String[]{GLOBAL_REORDER_BUYER_COUNT_COLUMN, GLOBAL_TOTAL_BUYER_COUNT_COLUMN}));
		
		private int minPurchases;
		
		protected ReorderRateConverter(int minPurchases) {
			this.minPurchases = minPurchases;
		}
		
		public double[] map(String userId, ScoreRangeProvider provider)
			throws Exception {
			double [] reordersCounts = dup(provider.getRange(userId, GLOBAL_REORDER_BUYER_COUNT_COLUMN));
			double [] totalCounts = provider.getRange(userId, GLOBAL_TOTAL_BUYER_COUNT_COLUMN);
			for(int i = 0; i< reordersCounts.length; ++i) {
				reordersCounts[i] = totalCounts[i] > minPurchases ? reordersCounts[i]/totalCounts[i] : 0;
			}			
			return reordersCounts;
		}
		
		public Set requiresGlobalDatabaseColumns() {
			return dbColumns;
		}
		
		public boolean isPersonalized() {
			return false;
		}
		
		
	}
	
	public static FactorRangeConverter getReorderRateConverter(int minPurchases) {
		return new ReorderRateConverter(minPurchases);
	}
	
	/**
	 * Get factor range converter for reorder rate.
	 * 
	 * Takes the ratio of two database columns: RerorderBuyerCount and TotalBuyerCount.
	 * 
	 * @param minPurchases if purchased less, the score is zero.
	 * @return FactorRangeConverter
	 */
	public static FactorRangeConverter getNormalizedReorderRateConverter(int minPurchases) {
		return new ReorderRateConverter(minPurchases) {
			

			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				double [] reordersCounts = super.map(userId, provider);
				
				divide(reordersCounts,max(reordersCounts));
				return reordersCounts;
			}		
		};
	}
	
	
	/**
	 * 
	 * @param minPurchases
	 * @return
	 */
	public static FactorRangeConverter getDepartmentNormalizedReorderRateConverter(int minPurchases) {
		return new ReorderRateConverter(minPurchases) {
		
			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				final double[] range = super.map(userId, provider);
				
				return new DepartmentSpecificConverter() {

					public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
						return maxNormalize(range, userId, provider);
					}
					
				}.map(userId, provider);
			}
			
		};
	}
	
	public static FactorRangeConverter getDiscretizedReorderRateConverter(double base) {
		return new LogCDFDiscretizingConverter(base) {
			
			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				return discretizeRange(provider.getRange(userId, GLOBAL_POPULARITY_COLUMN));
			}
			
			public boolean isPersonalized() {
				return false;
			}
			
			public Set requiresGlobalDatabaseColumns() {
				return Collections.singleton(GLOBAL_POPULARITY_COLUMN);
			}
		};
	}	

	/**
	 * Frequencies divided by the max frequency a 0 - 1 rate.
	 * 
	 * Frequencies are divided by the max frequency.
	 * @return converter
	 */
	public static FactorRangeConverter getNormalizedFrequencyConverter() {
		return new FactorRangeConverter() {

			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				double [] values = dup(provider.getRange(userId, PERSONALIZED_FREQUENCY_COLUMN));
				double m = max(values);
				if (m > 0) {
					for(int i = 0; i< values.length; ++i) {
						values[i] /= m; 
					}
				}
				return values;
			}
			
			public Set requiresPersonalizedDatabaseColumns() {
				return Collections.singleton(PERSONALIZED_FREQUENCY_COLUMN);
			}
			
			public boolean isPersonalized() {
				return true;
			}
		};
	}
	
	
	/**
	 * Discretizer utility.
	 * 
	 * Discretization consists of:
	 * <ol>
	 *  <li>Calculating the CDF of value occurrences such that
	 *      products are sorted in decreasing value order</li>
	 *  <li>Putting the values into buckets depending where logarithm intersects
	 *      the CDF curve</li> 
	 * </ol>
	 * Thus, it is a relatively expensive process.
	 * 
	 * @author istvan
	 */
	protected static abstract class LogCDFDiscretizingConverter extends FactorRangeConverter {
		
		private double base;
		
		// Frequency bucket
		class Bucket {
			int value = 1; // count
			int cdf = 0; 
			int norm = 0;
			
			public void inc() {
				++value;
			}
			
			public int intValue() {
				return value;
			}
			
			public void accumulate(int c) {
				cdf += c;
			}
			
			public void caculateNorm(double total) {
				double bar = total /= base;
				norm = 1;
				while(cdf < bar) {
					bar /= base;
					++norm;
				}
			}
			
			public int normValue() {
				return norm;
			}
			
		}
		
		protected LogCDFDiscretizingConverter(double base) {
			this.base = base;
		}
		
		protected double[] discretizeRange(double[] range) {
			// store values in decreasing order
			SortedMap histo = new TreeMap(
				new Comparator() {

					public int compare(Object o1, Object o2) {
						double diff = ((Number)o2).doubleValue() - ((Number)o1).doubleValue() ;
						return diff < 0 ? -1 : diff > 0 ? +1 : 0;
					}
				}
			);
					
			// sum up the value occurrences
			for(int i = 0; i< range.length; ++i) {
				Double value = new Double(range[i]);
				Bucket count = (Bucket)histo.get(value);
				if (count == null) {
					histo.put(value,new Bucket());
				} else {
					count.inc();
				}
			}
			
			// calculate the CDF
			int total = 0;
			for(Iterator i = histo.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry entry = (Map.Entry)i.next();
				Bucket bucket = (Bucket)entry.getValue();
				total += bucket.intValue();
				bucket.accumulate(total);
			}
			
			// calculate the norms
			for(Iterator i = histo.values().iterator(); i.hasNext();) {
				Bucket bucket = (Bucket)i.next();
				bucket.caculateNorm(total);
			}
			
			double [] results = new double[range.length];
			
			// assign the values
			for(int i = 0; i< range.length; ++i) {
				results[i] = range[i] == 0 ? 0 : ((Bucket)histo.get(new Double(range[i]))).normValue();
			}
			
			return results;
		}
	}
	
	public static FactorRangeConverter getLogDiscretizedPersonalConverter(final String column, double base) {

		return new LogCDFDiscretizingConverter(base) {
			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				return discretizeRange(provider.getRange(userId, column));
			}
			
			public boolean isPersonalized() {
				return true;
			}
			
			public Set requiresPersonalizedDatabaseColumns() {
				return Collections.singleton(column);
			}
		};
	}
	
	public static FactorRangeConverter getLogDiscretizedGlobalConverter(final String column, double base) {
		
		return new LogCDFDiscretizingConverter(base) {
			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				return discretizeRange(provider.getRange(userId, column));
			}
			
			public boolean isPersonalized() {
				return false;
			}
			
			public Set requiresGlobalDatabaseColumns() {
				return Collections.singleton(column);
			}		
		};
	}

	/**
	 * Get max normalized converter for the global factor with given source column.
	 * 
	 * Adjust the range such that all values are divided by the range max.
	 * 
	 */
	public static FactorRangeConverter getMaxNormalizedGlobalConvereter(final String column) {
		return new FactorRangeConverter() {

			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				double[] values = dup(provider.getRange(null, column));
				divide(values,max(values));
				return values;
			}
			
			public boolean isPersonalized() {
				return false;
			}
			
			public Set requiresGlobalDatabaseColumns() {
				return Collections.singleton(column);
			}
			
		};
	}
	
	/**
	 * Get max normalized personalized converter for the given source column.
	 * 
	 */	
	public static FactorRangeConverter getMaxNormalizedPersonalConverter(final String column) {
		return new FactorRangeConverter() {

			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				double[] values = dup(provider.getRange(userId, column));
				divide(values,max(values));
				return values;
			}
			
			public boolean isPersonalized() {
				return true;
			}
			
			public Set requiresPersonalizedDatabaseColumns() {
				return Collections.singleton(column);
			}
			
		};
	}
	
	
	/**
	 * Department specific statistics.
	 *
	 */
	protected static abstract class DepartmentSpecificConverter extends FactorRangeConverter {
		
		/**
		 * 
		 * @param userId
		 * @param provider
		 * @return Map<DepartmentId:String,List<Index:Integer>>
		 */
		protected static Map departmentMap(String userId, ScoreRangeProvider provider) {
			List products = provider.products(userId);
			Map map = new HashMap();
			for(int i=0; i< products.size(); ++i) {
				String dept = "";
				try {
					ProductModel productNode = 
						(ProductModel)ContentFactory.getInstance().getContentNodeByKey(
								new ContentKey(FDContentTypes.PRODUCT,products.get(i).toString()));
					dept = productNode.getDepartment().getContentKey().getId();
				} catch (Exception e) {
				}
				List indexes = (List)map.get(dept);
				if (indexes == null) {
					indexes = new ArrayList();
					map.put(dept, indexes);
				}
				indexes.add(new Integer(i));
			}
			return map;
		}
		
		protected double[] maxNormalize(double[] values, String userId, ScoreRangeProvider provider) throws Exception {
			Map deptMap = departmentMap(userId, provider);
			for(Iterator i = deptMap.values().iterator(); i.hasNext();) {
				List indexes = (List)i.next();
				double max = Double.MIN_VALUE;
				for(Iterator j = indexes.iterator(); j.hasNext();) {
					int index = ((Integer)j.next()).intValue();
					if (values[index] > max) {
						max = values[index];
					}
				}
				if (max != 0) {
					for(Iterator j = indexes.iterator(); j.hasNext();) {
						int index = ((Integer)j.next()).intValue();
						values[index] /= max;
					}
				}
			}
			return values;
		}
	};
	
	public static FactorRangeConverter getDepartmentMaxNormalizedPersonalizedConverter(final String factor) {
		return new DepartmentSpecificConverter() {

			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				return maxNormalize(dup(provider.getRange(userId, factor)),userId, provider);
			}
			
			public boolean isPersonalized() {
				return true;
			}
			
			public Set requiresPersonalizedDatabaseColumns() {
				return Collections.singleton(factor);
			}
		};
	}
	
	public static FactorRangeConverter getDepartmentMaxNormalizedGlobalConverter(final String factor) {
		return new DepartmentSpecificConverter() {

			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				return maxNormalize(dup(provider.getRange(userId, factor)),userId, provider);
			}
			
			public boolean isPersonalized() {
				return false;
			}
			
			public Set requiresGlobalDatabaseColumns() {
				return Collections.singleton(factor);
			}
		};
	}
	
	public static FactorRangeConverter getSeasonalityConverter(final double asymptote) {
		
		return new FactorRangeConverter() {

			private Set dbColumns =
				new HashSet(Arrays.asList(new String[]{GLOBAL_WEEKLY_FREQUENCY_COLUMN, GLOBAL_AVERAGE_FREQUENCY_COLUMN}));
			
			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				double[] range = dup(provider.getRange(userId, GLOBAL_WEEKLY_FREQUENCY_COLUMN));
				double[] averageFrequencies = provider.getRange(userId, GLOBAL_AVERAGE_FREQUENCY_COLUMN);
				for(int i = 0; i< range.length; ++i) {
					if (averageFrequencies[i] == 0) {
						range[i] = Double.MAX_VALUE;
					} else {
						range[i] /= averageFrequencies[i];
					}
				}
				positiveBiasedSigmoidNormalize(range, asymptote);
				
				return range;
			}
			
			public boolean isPersonalized() {
				return false;
			}
			
			public Set requiresGlobalDatabaseColumns() {
				return dbColumns;
			}
			
		};
	}
	
	
	public static FactorRangeConverter getDiscretizedSeasonality() {
		return new FactorRangeConverter() {
			
			private Set dbColumns =
				new HashSet(Arrays.asList(new String[]{GLOBAL_WEEKLY_FREQUENCY_COLUMN, GLOBAL_AVERAGE_FREQUENCY_COLUMN}));
			
			public boolean isPersonalized() {
				return false;
			}
			
			public Set requiresGlobalDatabaseColumns() {
				return dbColumns;
			}

			public double[] map(String userId, ScoreRangeProvider provider) throws Exception {
				double[] weeks = provider.getRange(userId, GLOBAL_WEEKLY_FREQUENCY_COLUMN);
				double[] averages = provider.getRange(userId, GLOBAL_AVERAGE_FREQUENCY_COLUMN);
				double[] scores = new double[weeks.length];
				 
				for(int i = 0; i< weeks.length; ++i) {
					if (averages[i] > 0) {
						double rat = weeks[i] / averages[i];
						if (rat < 1./3.) {
							scores[i] = 0;
						} else if (rat < 2./3.) {
							scores[i] = 1;
 						} else if (rat < 1.) {
 							scores[i] = 2;
 						} else if (rat < 2.) {
 							scores[i] = 3;
 						} else if (rat < 4.) {
 							scores[i] = 5;
 						}
					} else {
						scores[i] = 0;
					}
				}
				
				return scores;
			}
		};
	}
	
	
}
