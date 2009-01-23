package com.freshdirect.smartstore.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.ProductStatisticsProvider;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;

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
	
	public MostFrequentlyBoughtDyfVariant(Variant variant) {
		super(variant);
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

			
			final Map productFrequencies = prefersDB ? getItemsFromAnalysis(input.getCustomerId()) : getItemsFromEIEO(input.getCustomerId());
			if (productFrequencies == null) {
				return Collections.EMPTY_LIST;
			}
			
			List sortedAggregates = createSortedRankedContentList(productFrequencies, aggregateAtCategoryLevel);			
			cachedSortedAggregates = new SessionCache.TimedEntry(sortedAggregates,60*10*1000);
			cache.put(input.getCustomerId(),cachedSortedAggregates);
		} 
		
		return getSampler(input).sample((List)cachedSortedAggregates.getPayload(), includeCartItems ? Collections.EMPTY_SET : input.getCartContents(), 20);
	}



	private Map getItemsFromEIEO(String customerId) {
		// get line items
		List lineItems;
		try {
			lineItems = FDListManager.getEveryItemEverOrdered(new FDIdentity(customerId /*input.getCustomerId()*/));
		} catch (FDResourceException e) {
			e.printStackTrace();
			return null;
		}
		
		
		// Map<ContentKey,Integer>
		final Map productFrequencies = new HashMap(3*lineItems.size()/2+1,0.75f);
		
		// aggregate frequencies at product level
		for(Iterator i = lineItems.iterator(); i.hasNext();) {
			FDProductSelectionI selection = (FDProductSelectionI)i.next();
			ContentKey productKey = SmartStoreUtil.getProductContentKey(selection.getSkuCode());
			if (productKey == null) continue;
			Integer sofarObj = (Integer)productFrequencies.get(productKey);
			int sofarFreq = sofarObj == null ? 0 : sofarObj.intValue();
			productFrequencies.put(productKey, new Integer(selection.getStatistics().getFrequency() + sofarFreq));
		}

		return productFrequencies;
	}


	/**
	 * Returns analyzed product list from database
	 * 
	 * @param max
	 * @param input
	 * @return
	 */
	private Map getItemsFromAnalysis(String customerId) {
	    return ProductStatisticsProvider.getInstance().getUserProductScores(customerId);
	}
}
