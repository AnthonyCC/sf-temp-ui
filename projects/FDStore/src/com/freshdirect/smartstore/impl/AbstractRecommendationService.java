package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.sampling.ContentSampler;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.sampling.ListSampler;
import com.freshdirect.smartstore.sampling.RankedContent;

/**
 * Simple abstract implementation of recommendation service
 * It does nothing but store it's variant ID
 * 
 * Subclasses must implement recommendNodes() method.
 * 
 * @author segabor
 *
 */
public abstract class AbstractRecommendationService implements RecommendationService {
    public static final String SAMPLING_STRATEGY = "sampling_strat";

    public static final String CAT_AGGR = "cat_aggr";

    /**
     * Config key to switch on/off the automatic removal of cart items. 
     */
    public static final String INCLUDE_CART_ITEMS = "include_cart_items";

    protected Variant variant;
	
    // sampler used in strategy 
    private ImpressionSampler sampler;
	
	
	
    protected boolean           aggregateAtCategoryLevel = false;

    protected boolean           includeCartItems         = false;
	
    protected static ImpressionSampler DETERMINISTIC_SAMPLER = new ConfiguredImpressionSampler(new SimpleLimit(100, 100), ListSampler.ZERO);

    private final static class SimpleLimit implements ContentSampler.ConsiderationLimit {
        private final double topP;
        private final int    topN;

        private SimpleLimit(double topP, int topN) {
            this.topP = topP;
            this.topN = topN;
        }

        public int max(List rankedItems) {
        	return Math.max((int)((topP*rankedItems.size())/100.0), topN);
        }

        public String toString() {
            return "topN:"+topN+",percent:"+topP;
        }
    }

    private static class ConfiguredImpressionSampler implements ImpressionSampler {
		
		private ContentSampler.ConsiderationLimit cl;
		private ListSampler listSampler;
		
		private ConfiguredImpressionSampler(ContentSampler.ConsiderationLimit cl, ListSampler listSampler) {
			this.cl = cl;
			this.listSampler = listSampler;
		}
		
		public List sample(List sortedRankedContent, Set reserved, int k) {
			return ContentSampler.drawWithoutReplacement(sortedRankedContent, reserved, cl, k, listSampler);
		}
		
		public String toString() {
		    return "limit:"+cl+",list:"+listSampler;
		}
	};
	
	protected void configureSampler(Random R) {
		
		RecommendationServiceConfig config = variant.getServiceConfig();

		final int topN = Integer.parseInt(NVL.apply(config.get("top_n"),"20").toString());
		final double topP = Double.parseDouble(NVL.apply(config.get("top_perc"),"20").toString());
		final ContentSampler.ConsiderationLimit cl = new SimpleLimit(topP, topN);
		
		aggregateAtCategoryLevel = "true".equals(config.get(CAT_AGGR));
		
		String samplingStrategy = config.get(SAMPLING_STRATEGY);
		
		if (samplingStrategy == null) {
			sampler = new ConfiguredImpressionSampler(cl, new ListSampler.Uniform(R));
		} else {		
			samplingStrategy = samplingStrategy.toLowerCase();		
			
			if ("deterministic".equals(samplingStrategy)) {
				sampler = new ConfiguredImpressionSampler(cl,ListSampler.ZERO);
			} else if ("uniform".equals(samplingStrategy)) {
				sampler = new ConfiguredImpressionSampler(cl,new ListSampler.Uniform(R));
			} else if ("linear".equals(samplingStrategy)) {
				sampler = new ConfiguredImpressionSampler(cl,new ListSampler.Linear(R));
			} else if ("quadratic".equals(samplingStrategy)) {
				sampler = new ConfiguredImpressionSampler(cl,new ListSampler.Quadratic(R));
			} else if ("cubic".equals(samplingStrategy)) {
				sampler = new ConfiguredImpressionSampler(cl, new ListSampler.Cubic(R));
			} else if ("harmonic".equals(samplingStrategy)) {
				sampler = new ConfiguredImpressionSampler(cl, new ListSampler.Harmonic(R));
			} else if ("sqrt".equals(samplingStrategy)) {
				sampler = new ConfiguredImpressionSampler(cl, new ListSampler.SquareRootCDF(R));
			} else if ("power".equals(samplingStrategy)) {
				sampler = new ConfiguredImpressionSampler(cl, 
					new ListSampler.PowerCDF(R,Double.parseDouble(NVL.apply(config.get("exponent"),"0.66").toString())));
			} else if ("complicated".equals(samplingStrategy)) {
				sampler = new ImpressionSampler() {
					public List sample(List sortedRankedContent, Set reserved, int k) {
						return ContentSampler.drawWithoutReplacement(sortedRankedContent, reserved, cl, k);
					}
					
					public String toString() {
					    return "complicated";
					}
				};
			}
		}
	}
	
	/**
	 * Get the highest node the content key is aggregated at.
	 * 
	 * The label is a string. If the product or sku is not aggregated,
	 * then the product key is returned (unless it is orphan). If it is
	 * aggregated, then the highest level category is returned.
	 * @param key
	 * @return key of aggregation level
	 */
	protected ContentKey getAggregationKey(ContentKey key) {
		// this looks like it could be refactored as a class,
		// but it has no other purpose than calculating that string, so 
		// just leave it here
		// it actually returns either the ContentKey's id, or the highest
		// level it was aggregated at the category level
		return new Object() {
			
			ContentKey getKey(ProductModel product) {
				if (product == null) return null; // in case argument is a getParent of a sku
				CategoryModel parent = (CategoryModel)product.getParentNode();
				if (parent == null) return null; // orphan
				ContentKey cat = getKey(parent);
				return cat == null ? product.getContentKey() : cat;
			}
			
			ContentKey getKey(CategoryModel category) {
				ContentKey cat = null;

				while(category != null) {
					if (category.isDYFAggregated()) cat = category.getContentKey();
					if (category.getParentNode() instanceof CategoryModel) {
						category = (CategoryModel) category.getParentNode();
					} else break;
				}	
				return cat;
			}
			
			ContentKey getKey(ContentKey key) {
				
				ContentNodeModel model =ContentFactory.getInstance().getContentNodeByKey(key);
				if (model == null) {
					return null;
				} else if (model instanceof SkuModel) {
					return getKey((ProductModel)model.getParentNode());
				} else if (model instanceof ProductModel) {
					return getKey((ProductModel)model);
				} else if (model instanceof CategoryModel) {
					return getKey((CategoryModel)model);
				} else return key;
			}
		}.getKey(key);
	}
	
	/**
	 * Create ranked content list.
	 * 
	 * This function does the following:
	 * <ol>
	 *    <li>Aggregates the content keys at category level, if <tt>aggregate</tt> is true</li>
	 *    <li>Wraps all content keys into {@link RankedContent} objects that will be sampled</li>
	 *    <li>Sorts the ranked contents in decreasing score order
	 * </ol>
	 * 
	 * @param scores Map<{@link ContentKey},{@link Number}>
	 * @return List<@link {@link RankedContent}>
	 */
	protected List createSortedRankedContentList(Map scores, final boolean aggregate) {
		
		
		List result = new ArrayList();
		
		/** Map<String,RankedContent> */
		Map aggregateMap = new HashMap();
		
		for(Iterator i = scores.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry e = (Map.Entry)i.next();
			final ContentKey key = (ContentKey)e.getKey();
			
			String label = null;
			if (aggregate) {
				ContentKey aggregationLevelKey = getAggregationKey(key);
				if (aggregationLevelKey != null) {
					label = aggregationLevelKey.getId();
				}
			} else {
				label = key.getId();
			}
			
			if (label == null) continue;
			Number score = (Number)e.getValue();
			
			if (score.doubleValue() == 0) continue;
			
			
			if (aggregate) {
				RankedContent aggregateContent = (RankedContent)aggregateMap.get(label);
				if (aggregateContent == null) {
					aggregateContent = new RankedContent.Aggregate(label);
					aggregateMap.put(label, aggregateContent);
				}
				((RankedContent.Aggregate)aggregateContent).add(new RankedContent.Single(key,score.doubleValue()));
			} else { // no aggregation
				result.add(new RankedContent.Single(key,score.doubleValue()));
			}
		}
		
		if (aggregate) {
			for(Iterator i = aggregateMap.entrySet().iterator(); i.hasNext();) {
				// Map.Entry<String,RankedContent>
				Map.Entry e = (Map.Entry)i.next();
				RankedContent.Aggregate agg = (RankedContent.Aggregate)e.getValue();
				if (agg.getCount() == 1) { // not aggregate
					result.add(agg.take());
				} else {
					result.add(agg);
				}
			}
		}
		
		Collections.sort(result, new Comparator() {

			public int compare(Object o1, Object o2) {
				RankedContent c1 = (RankedContent)o1;
				RankedContent c2 = (RankedContent)o2;
				
				return c1.getScore() < c2.getScore() ? +1 : c1.getScore() > c2.getScore() ? -1 : 0;
			}		
		});
		
		return result;
	}
	
	public AbstractRecommendationService(Variant variant) {
		// TODO: not-null check
		this.variant = variant;
                includeCartItems = Boolean.valueOf(variant.getServiceConfig().get(INCLUDE_CART_ITEMS)).booleanValue();
		configureSampler(new Random());
	}
	
	public Variant getVariant() {
		return this.variant;
	}

	abstract public List recommendNodes(SessionInput input);


	public String getDescription() {
	    return "";
	}

	protected ImpressionSampler getSampler(SessionInput input) {
	    return input.isNoShuffle() ? DETERMINISTIC_SAMPLER : sampler;
	}
	
	public String toString() {
		return "Service(feature:"+variant.getSiteFeature().getName() +
		    ",variant:" + variant.getId()+
		    ",class:"+StringUtil.getSimpleName(this.getClass())+
		    ','+CAT_AGGR + ':' + aggregateAtCategoryLevel+
		    ','+INCLUDE_CART_ITEMS + ':' + includeCartItems + 
		    ",sampler:("+sampler+"),"+getDescription()+")";
	}
}
