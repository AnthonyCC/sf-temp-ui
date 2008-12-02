package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.framework.util.UniqueRandomSequence;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.ProductStatisticsProvider;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;


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

	public RandomDyfVariant(Variant variant) {
		super(variant);
	}


	/**
	 * Randomly selects keys.
	 *
	 */
	public List recommend(int max, SessionInput input) {
		// see if product history in cache
		
		SessionCache.TimedEntry shoppingHistory = (SessionCache.TimedEntry)cache.get(input.getCustomerId());
		
		if (shoppingHistory == null || shoppingHistory.expired()) {
			LOGGER.debug("Loading order history for " + input.getCustomerId() + (shoppingHistory != null ? " (EXPIRED)" : ""));
			
			// if not, retrieve history
			Set products = prefersDB ? getItemsFromAnalysis(input.getCustomerId()) : getItemsFromEIEO(input.getCustomerId());
			
			if (products == null) return Collections.EMPTY_LIST;
			
			List productList = new ArrayList(products.size());
			productList.addAll(products);
			
			shoppingHistory = new SessionCache.TimedEntry(productList, 10*60*1000);
			cache.put(input.getCustomerId(), shoppingHistory);
		}
		
		// select randomly
		
		List productList = (List)shoppingHistory.getPayload();
		
		// so we have enough products for sure
		int toSelect = Math.min(input.getCartContents().size()+2*max, productList.size());
		UniqueRandomSequence U = UniqueRandomSequence.getInstance(
				toSelect, 
				productList.size());
		
		List selection = new ArrayList(toSelect);
		for(int i = 0; i < toSelect; ++i) {
			try {
				selection.add((ContentKey)productList.get(U.next()));
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
		return selection;
	}




	/**
	 * Lookup keys in customer's Every Item Ever Ordered history (EIEO)
	 * 
	 * @param customerId
	 * 
	 * @return Set of content keys
	 */
	private Set getItemsFromEIEO(String customerId) {
		List lineItems;
		try {
			lineItems =  FDListManager.getEveryItemEverOrdered(new FDIdentity(customerId));
		} catch (FDResourceException e) {
			e.printStackTrace();
			return null;
		}
		
		Set products = new HashSet();
		
		for(Iterator i = lineItems.iterator(); i.hasNext(); ) {
			FDProductSelectionI selection = (FDProductSelectionI)i.next();
			products.add(SmartStoreUtil.getProductContentKey(selection.getSkuCode()));
		}
		
		return products;
	}


	/***
	 * Retrieve keys from analyzed data
	 * 
	 * @param customerId
	 *
	 * @return Set of content keys
	 */
	private Set getItemsFromAnalysis(String customerId) {
	    return ProductStatisticsProvider.getInstance().getProducts(customerId);
	}

}
