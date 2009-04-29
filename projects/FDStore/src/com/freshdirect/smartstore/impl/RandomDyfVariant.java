package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.ProductStatisticsProvider;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.sampling.RankedContent;


/**
 * Recommend randomly from purchase history.
 * 
 * The service keeps a {@link SessionCache session cache}, to spare
 * frequent database lookups.
 *  
 * @author istvan
 */
public class RandomDyfVariant extends DYFService {
	
	private static final Category LOGGER = LoggerFactory.getInstance(RandomDyfVariant.class);

	public RandomDyfVariant(Variant variant, ImpressionSampler sampler,
    		boolean catAggr, boolean includeCartItems) {
		super(variant, sampler, catAggr, includeCartItems);
	}

	/**
	 * Randomly selects keys.
	 *
	 */
	public List recommend(SessionInput input) {
	    int max = 10;
		// see if product history in cache
		
		SessionCache.TimedEntry shoppingHistory = (SessionCache.TimedEntry)cache.get(input.getCustomerId());
		
		if (shoppingHistory == null || shoppingHistory.expired()) {
			LOGGER.debug("Loading order history for " + input.getCustomerId() + (shoppingHistory != null ? " (EXPIRED)" : ""));
			
			// if not, retrieve history
			Set products = ProductStatisticsProvider.getInstance().getProducts(input.getCustomerId());
			
			if (products == null) return Collections.EMPTY_LIST;
			
			List productList = new ArrayList(products.size());
			productList.addAll(products);
			
			shoppingHistory = new SessionCache.TimedEntry(productList, 10 * 60 * 1000);
			cache.put(input.getCustomerId(), shoppingHistory);
		}
		
		// select randomly
		
		List productList = (List)shoppingHistory.getPayload();
		
		// so we have enough products for sure
		int size = productList.size();
                int toSelect = Math.min(Math.min(input.getCartContents().size() + 2 * max, size), 20);
		List rankedContents = new ArrayList(size);
		for (int i = 0; i < size; i++) {
		    ContentKey key = (ContentKey) productList.get(i);
		    rankedContents.add(new RankedContent.Single(key, size-i));
		}
		return RankedContent.getKeys(getSampler(input).sample(rankedContents,
				includeCartItems ? Collections.EMPTY_SET : input.getCartContents(), toSelect));
	}
}
