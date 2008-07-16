package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
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
public class MostFrequentlyBoughtDyfVariant implements RecommendationService {

	
	private Variant variant;
	

	// cache the most recently accessed order histories
	private SessionCache cache = new SessionCache();
	
	public Variant getVariant() {
		return variant;
	}
	
	
	public MostFrequentlyBoughtDyfVariant(Variant variant) {
		this.variant = variant;
	}


	/**
	 * Recommend the most frequently bought products.
	 * @param max maximum number of products to produce
	 * @param input session input
	 */
	public List recommend(int max, SessionInput input) {
		
		
		// see if list in cache
		// List<ContentKey>
		List sortedProductList = (List)cache.get(input.getCustomerId());
		
		if (sortedProductList == null) {
			// if not in cache, calculate it

			// get line items
			List lineItems;
			try {
				lineItems = FDListManager.getEveryItemEverOrdered(new FDIdentity(input.getCustomerId()));
			} catch (FDResourceException e) {
				e.printStackTrace();
				return Collections.EMPTY_LIST;
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
			
			
			sortedProductList = new ArrayList(productFrequencies.size());
			sortedProductList.addAll(productFrequencies.keySet());
			
			// sort by frequency
			Collections.sort(
				sortedProductList, 
				new Comparator() {
					public int compare(Object o1, Object o2) {
						return 
							((Integer)productFrequencies.get(o2)).intValue() -
							((Integer)productFrequencies.get(o1)).intValue();
					}
					
				}
			);
				
			cache.put(input.getCustomerId(), sortedProductList);
		
		}
		
		return sortedProductList.subList(0, Math.min(max, sortedProductList.size()));
	}

}
