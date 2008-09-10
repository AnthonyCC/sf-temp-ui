package com.freshdirect.smartstore.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ejb.CreateException;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.framework.util.DiscreteRandomSamplerWithoutReplacement;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.ejb.DyfModelSB;
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
	public List recommend(int max, SessionInput input) {
		// see if list in cache
		// List<ContentKey>
		SessionCache.TimedEntry userHistory = (SessionCache.TimedEntry)cache.get(input.getCustomerId());
		
		if (userHistory == null ||  userHistory.expired()) {
			LOGGER.debug("Loading order history for " + input.getCustomerId() + (userHistory != null ? " (EXPIRED)" : ""));

			
			final Map productFrequencies = prefersDB ? getItemsFromAnalysis(input.getCustomerId()) : getItemsFromEIEO(input.getCustomerId());
			if (productFrequencies == null) {
				return Collections.EMPTY_LIST;
			}
			
			
			
			Map aggregates = new HashMap();
			for(Iterator i = productFrequencies.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Map.Entry)i.next();
				
				ContentKey key = (ContentKey)e.getKey();
				String aggregateLabel = ContentAggregate.getAggregateLabel(key);
				Number score = (Number)e.getValue();
				
				if (score.floatValue() == 0) continue;
				
				ContentAggregate aggregate = (ContentAggregate)aggregates.get(aggregateLabel);
				if (aggregate == null) {
					aggregate = new ContentAggregate(aggregateLabel);
					aggregates.put(aggregateLabel, aggregate);
				}
				aggregate.addContent(key, score.floatValue());
			}

			Random R = new Random();
			DiscreteRandomSamplerWithoutReplacement sampler = new DiscreteRandomSamplerWithoutReplacement(
					new Comparator() {

						public int compare(Object o1, Object o2) {
							ContentAggregate ca1 = (ContentAggregate)o1;
							ContentAggregate ca2 = (ContentAggregate)o2;
							return ca1.getLabel().compareTo(ca2.getLabel());
						}
					}
			);
			
			for(Iterator i = aggregates.values().iterator(); i.hasNext();) {
				ContentAggregate ca = (ContentAggregate)i.next();
				sampler.setItemFrequency(ca, Math.round(100*ca.getScore()));
			}
			
			List sortedProductList = new ArrayList(productFrequencies.size());
			
			for(int i = 0; i < Math.min(sampler.getItemCount(),25); ++i) {
				ContentAggregate ca = (ContentAggregate)sampler.getRandomItem(R);
				sortedProductList.add(ca.take(R));
				long newFrequency = Math.round(100*ca.getScore());
				sampler.setItemFrequency(ca, newFrequency);	
			}
			
			for(Iterator i = aggregates.values().iterator(); i.hasNext();) {
				ContentAggregate ca = (ContentAggregate)i.next();
				
				for(Iterator j = ca.keys(); j.hasNext(); ) {
					sortedProductList.add(j.next());
				}
			}
			
			userHistory = new SessionCache.TimedEntry(sortedProductList,10*60*1000);
			cache.put(input.getCustomerId(), userHistory);
		
		} 
		
		List productList = (List)userHistory.getPayload();
		
		return productList.subList(
			0, 
			Math.min(input.getCartContents().size()+max, productList.size()));
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
		try {
			DyfModelSB source = getModelHome().create();
			
			Map productFrequencies = source.getProductFrequencies(customerId);

			return productFrequencies;
		} catch (RemoteException e) {
			LOGGER.error(getVariant().getId() + ": remote exception!", e);
		} catch (CreateException e) {
			LOGGER.error(getVariant().getId() + ": create exception!", e);
		}
		
		return null;
	}
}
