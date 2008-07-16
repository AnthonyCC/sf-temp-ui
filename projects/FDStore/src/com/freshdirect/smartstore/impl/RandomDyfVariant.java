package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.framework.util.UniqueRandomSequence;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;


/**
 * Recommend randomly from purchase history.
 * 
 * The service keeps a {@link SessionCache session cache}, to spare
 * frequent database lookups.
 *  
 * @author istvan
 */
public class RandomDyfVariant implements RecommendationService {
	

	private Variant variant;
	
	private SessionCache cache = new SessionCache();
	
	public RandomDyfVariant(Variant variant) {
		this.variant = variant;
	}
	
	/**
	 * Randomly selects from "every item ever ordered".
	 *
	 */
	public List recommend(int max, SessionInput input) {
		
	
		// see if product history in cache
		List productList = (List)cache.get(input.getCustomerId());
		
		if (productList == null) {
			
			// if not, retrieve history
			
			List lineItems;
			try {
				lineItems =  FDListManager.getEveryItemEverOrdered(new FDIdentity(input.getCustomerId()));
			} catch (FDResourceException e) {
				e.printStackTrace();
				return Collections.EMPTY_LIST;
			}
			
			Set products = new HashSet();
			
			for(Iterator i = lineItems.iterator(); i.hasNext(); ) {
				FDProductSelectionI selection = (FDProductSelectionI)i.next();
				products.add(SmartStoreUtil.getProductContentKey(selection.getSkuCode()));
			}
			
			productList = new ArrayList(products.size());
			productList.addAll(products);
			
			cache.put(input.getCustomerId(), productList);
		}
		
		// select randomly
		UniqueRandomSequence U = UniqueRandomSequence.getInstance(max, productList.size());
		List selection = new ArrayList(Math.max(max, productList.size()));
		for(int i = 0; i < max; ++i) {
			try {
				selection.add((ContentKey)productList.get(U.next()));
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
		return selection;
	}

	public Variant getVariant() {
		return variant;
	}

}
