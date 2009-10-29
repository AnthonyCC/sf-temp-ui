package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.filter.FilterFactory;
import com.freshdirect.smartstore.filter.ProductFilter;
import com.freshdirect.smartstore.sampling.ConfiguredImpressionSampler;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.sampling.ListSampler;
import com.freshdirect.smartstore.sampling.RankedContent;
import com.freshdirect.smartstore.sampling.SimpleLimit;
import com.freshdirect.smartstore.sampling.RankedContent.Single;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;

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
	private static Category LOGGER = LoggerFactory.getInstance(AbstractRecommendationService.class);

    protected Variant variant;
	
    // sampler used in strategy 
    private ImpressionSampler sampler;
	
    protected boolean aggregateAtCategoryLevel;
    private boolean includeCartItems;
  
    /**
     * ThreadLocal<Map<String:ContentKey.id,String:Recommender.id>>
     */
    public static ThreadLocal<Map<String,String>> RECOMMENDER_SERVICE_AUDIT = new ThreadLocal<Map<String,String>>();

    /**
     * ThreadLocal<Map<String:ContentKey.id,String:RecommenderStrategy.id>>
     */
    public static ThreadLocal<Map<String,String>> RECOMMENDER_STRATEGY_SERVICE_AUDIT = new ThreadLocal<Map<String,String>>();

    
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
	protected List<RankedContent> createSortedRankedContentList(Map scores, final boolean aggregate) {
		
		
		List<RankedContent> result = new ArrayList<RankedContent>();
		
		/** Map<String,RankedContent> */
		Map<String, RankedContent> aggregateMap = new HashMap<String, RankedContent>();
		
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
				RankedContent aggregateContent = aggregateMap.get(label);
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
	

	/**
	 * Aggregate RankedContent.Single items into RankedContent.Aggregate /s
	 * @param rankedContentNodes
	 * @return
	 */
	protected List<RankedContent> aggregateContentList(List<RankedContent> rankedContentNodes) {
            List<RankedContent> result = new ArrayList<RankedContent>();
    
            /** Map<ContentKey,RankedContent> */
            Map<ContentKey,RankedContent> aggregateMap = new HashMap<ContentKey,RankedContent>();
    
            for (RankedContent e : rankedContentNodes) {
                if (e instanceof RankedContent.Single) {
                    RankedContent.Single s = (RankedContent.Single) e;
                    ContentKey aggregationLevelKey = getAggregationKey(s.getContentKey());
                    if ((aggregationLevelKey != null) && (!aggregationLevelKey.equals(s.getContentKey()))) {
                        RankedContent aggregateContent = aggregateMap.get(aggregationLevelKey);
                        if (aggregateContent == null) {
                            aggregateContent = new RankedContent.Aggregate(aggregationLevelKey.getId());
                            aggregateMap.put(aggregationLevelKey, aggregateContent);
                        }
                        ((RankedContent.Aggregate) aggregateContent).add(s);
                    } else {
                        result.add(s);
                    }
                } else {
                    result.add(e);
                }
            }

            for (Iterator i = aggregateMap.entrySet().iterator(); i.hasNext();) {
                // Map.Entry<String,RankedContent>
                Map.Entry<ContentKey,RankedContent> e = (Map.Entry<ContentKey,RankedContent>) i.next();
                RankedContent.Aggregate agg = (RankedContent.Aggregate) e.getValue();
                if (agg.getCount() == 1) { // not aggregate
                    result.add(agg.takeFirst());
                } else {
                    result.add(agg);
                }
            }

            Collections.sort(result, new Comparator() {
                public int compare(Object o1, Object o2) {
                    RankedContent c1 = (RankedContent) o1;
                    RankedContent c2 = (RankedContent) o2;
                    return c1.getScore() < c2.getScore() ? +1 : c1.getScore() > c2.getScore() ? -1 : 0;
                }
            });
            return result;
	}
	
	
	public AbstractRecommendationService(Variant variant, ImpressionSampler sampler, boolean aggrCat, boolean includeCartItems) {
		this.variant = variant;
		this.sampler = sampler;
		this.aggregateAtCategoryLevel = aggrCat;
		this.includeCartItems = includeCartItems;
	}
	
	public Variant getVariant() {
		return this.variant;
	}

	final public List<ContentNodeModel> recommendNodes(SessionInput input) {
		if (!input.isIncludeCartItems())
			input.setIncludeCartItems(includeCartItems);
		return doRecommendNodes(input);
	}
	
	protected abstract List<ContentNodeModel> doRecommendNodes(SessionInput input);

	public String getDescription() {
	    return "";
	}

    protected ImpressionSampler getSampler(SessionInput input) {
        return getSampler(input.isNoShuffle());
    }

    protected ImpressionSampler getSampler(boolean noShuffle) {
        return noShuffle ? new ConfiguredImpressionSampler(new SimpleLimit(100, 100), ListSampler.ZERO) : sampler;
    }
        
        
	/**
	 * randomize the list of content nodes 
	 * 
	 * @param input
	 * @param nodes List<ContentNodeModel>
	 * @return List<ContentNodeModel>
	 */
	protected List sampleContentNodeModels(SessionInput input, List nodes) {
            if (!(nodes.isEmpty() || input.isNoShuffle())) {
                int size =nodes.size();
                List<RankedContent> rankedContents = new ArrayList<RankedContent>(size);
                for (int i=0;i<size;i++) {
                    ContentNodeModel node = (ContentNodeModel) nodes.get(i);
                    rankedContents.add(new RankedContent.Single(size - i, node));
                }
                List result = RankedContent.getContentNodeModel(getSampler(input).sample(rankedContents,
                		(Set<ContentKey>) (input.isIncludeCartItems() ? Collections.EMPTY_SET : input.getCartContents()), rankedContents.size()));
                return result;
            }
            return nodes;
	}

	/**
         * randomize the list of RankedContent.Single 
         * 
         * @param input
         * @param nodes List<RankedContent.Single>
         * @return List<ContentNodeModel>
         */
        protected List<ContentNodeModel> sampleRankedContents(SessionInput input, List<RankedContent.Single> nodes, Collection<ContentNodeModel> exclude) {
            if (!(nodes.isEmpty() || input.isNoShuffle())) {
                List<ContentNodeModel> sample = RankedContent.getContentNodeModel(getSampler(input).sample((List<RankedContent>) (List) nodes,
                        (Set<ContentKey>) (input.isIncludeCartItems() ? Collections.EMPTY_SET : input.getCartContents()), nodes.size()));
                List<ContentNodeModel> result = new ArrayList<ContentNodeModel>(sample.size());
                for (int i=0;i<sample.size();i++) {
                    ContentNodeModel model = sample.get(i);
                    if (model!=null && !exclude.contains(model)) {
                        result.add(model);
                    }
                }
                return result;
            } else {
                List<ContentNodeModel> result = new ArrayList<ContentNodeModel>(nodes.size());
				ProductFilter filter = FilterFactory.createStandardFilter(input.isIncludeCartItems() ?
						Collections.EMPTY_LIST : input.getCartContents());                
                for (Iterator<Single> iter = nodes.iterator(); iter.hasNext();) {
                    RankedContent.Single node = iter.next();
                    if (!exclude.contains(node.getModel()) &&
                    		filter.filter((ProductModel) node.getModel()) != null) {
                        result.add(node.getModel());
                    }
                }
                return result;
            }
        }

	
	public String toString() {
		return "Service(feature:"+variant.getSiteFeature().getName() +
		    ",variant:" + variant.getId()+
		    ",class:"+StringUtil.getSimpleName(this.getClass())+
		    ','+RecommendationServiceFactory.CKEY_CAT_AGGR + ':' + aggregateAtCategoryLevel+
		    ','+RecommendationServiceFactory.CKEY_INCLUDE_CART_ITEMS + ':' + includeCartItems + 
		    ",sampler:("+sampler+"),"+getDescription()+")";
	}

	public boolean isIncludeCartItems() {
		return includeCartItems;
	}


	public boolean isSmartSavings() {
		return false;
	}
	
	public boolean isRefreshable() {
		final ImpressionSampler sampler2 = getSampler(false);
		return !(sampler2 == null || sampler2.isDeterministic() );
	}
}
