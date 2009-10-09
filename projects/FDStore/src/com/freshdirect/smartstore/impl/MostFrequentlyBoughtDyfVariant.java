package com.freshdirect.smartstore.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.sampling.RankedContent;

/**
 * A SmartStore variant that offers the most frequently bought items.
 * 
 * The instance keeps a session cache to spare frequent database
 * lookups.
 * 
 * @author istvan
 *
 */
public class MostFrequentlyBoughtDyfVariant extends DYFService {
	private static final Category LOGGER = LoggerFactory.getInstance(MostFrequentlyBoughtDyfVariant.class);
	
	public MostFrequentlyBoughtDyfVariant(Variant variant, ImpressionSampler sampler,
    		boolean catAggr, boolean includeCartItems) {
		super(variant, sampler, catAggr, includeCartItems);
	}

	/**
	 * Recommend the most frequently bought products.
	 * @param max maximum number of products to produce
	 * @param input session input
	 */
	public List recommend(SessionInput input) {
		// see if list in cache
		// List<ContentKey>
		SessionCache.TimedEntry cachedSortedAggregates = (SessionCache.TimedEntry)cache.get(input.getCustomerId());
		
		if (cachedSortedAggregates == null ||  cachedSortedAggregates.expired()) {
			LOGGER.debug("Loading order history for " + input.getCustomerId() + (cachedSortedAggregates != null ? " (EXPIRED)" : ""));

			
			final Map productFrequencies = ScoreProvider.getInstance().getUserProductScores(input.getCustomerId());
			if (productFrequencies == null) {
				return Collections.EMPTY_LIST;
			}
			
			List sortedAggregates = createSortedRankedContentList(productFrequencies, aggregateAtCategoryLevel);			
			cachedSortedAggregates = new SessionCache.TimedEntry(sortedAggregates,60*10*1000);
			cache.put(input.getCustomerId(),cachedSortedAggregates);
		} 
		
		return RankedContent.getKeys(getSampler(input).sample((List)cachedSortedAggregates.getPayload(),
				input.isIncludeCartItems() ? Collections.EMPTY_SET : input.getCartContents(), 20));
	}
}
