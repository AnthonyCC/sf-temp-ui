package com.freshdirect.smartstore.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
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
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.framework.util.DiscreteRandomSamplerWithReplacement;
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
	

	// used in drawing
	protected Random R = new Random();
	
	/**
	 * Draw elements from without replacement from the top elements.
	 * @param sortedAggregates sorted aggregates
	 * @param n items to draw
	 * @return items drawn
	 */
	protected List draw(List sortedAggregates, Collection cartContents, int n) {
		List result = new ArrayList(n);
		
		DiscreteRandomSamplerWithReplacement sampler = 
			new DiscreteRandomSamplerWithReplacement(
				new Comparator() {

					public int compare(Object o1, Object o2) {
						ContentAggregate ca1 = (ContentAggregate)o1;
						ContentAggregate ca2 = (ContentAggregate)o2;
						return ca1.getLabel().compareTo(ca2.getLabel());
					}
				
				}
			);
		
		{
			int c = 0;
			for(Iterator i= sortedAggregates.iterator(); i.hasNext() && c < n;) {
				ContentAggregate ca = (ContentAggregate)i.next();
				for(Iterator j = ca.keys(); j.hasNext(); ) {
					ContentKey productKey = (ContentKey)j.next();
					if (cartContents.contains(productKey)) {
						j.remove();
					}
				}
				if (ca.getScore() > 0) {
					sampler.setItemFrequency(ca, Math.round(100*ca.getScore()));
					++c;
				}
			}
			
			while(sampler.getItemCount() > 0 && result.size() < n) {
				ContentAggregate ca = (ContentAggregate)sampler.getRandomItem(R);
				//float score = ca.getScore(); // comment out for testing
				ContentKey product = ca.take(R);
				//System.err.println(" ==> " + product + " " + score + " ("  + sortedAggregates.size() + ")");
				result.add(product);
				sampler.setItemFrequency(ca, Math.round(100*ca.getScore()));
				
						
			}
		}
		
		return result;
	}

	/**
	 * Recommend the most frequently bought products.
	 * @param max maximum number of products to produce
	 * @param input session input
	 */
	public List recommend(int max, SessionInput input) {
		// see if list in cache
		// List<ContentKey>
		SessionCache.TimedEntry cachedSortedAggregates = null; //(SessionCache.TimedEntry)cache.get(input.getCustomerId());
		
		if (cachedSortedAggregates == null ||  cachedSortedAggregates.expired()) {
			LOGGER.debug("Loading order history for " + input.getCustomerId() + (cachedSortedAggregates != null ? " (EXPIRED)" : ""));

			
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
			
			List sortedAggregates = new ArrayList(aggregates.values());
			Collections.sort(
				sortedAggregates, 
				new Comparator() {

					public int compare(Object o1, Object o2) {
						ContentAggregate ca1 = (ContentAggregate)o1;
						ContentAggregate ca2 = (ContentAggregate)o2;
						float diff = ca1.getScore() - ca2.getScore();
						return diff < 0 ? +1 : diff > 0 ? -1 : 0;
					}
				}
			);
			
			/* Comment out for testing.
			for(Iterator x = sortedAggregates.iterator(); x.hasNext();) {
				ContentAggregate ca = (ContentAggregate)x.next();
				
				System.err.println(ca.getLabel() + " -> " + ca.getScore());
			}
			*/
			
			cachedSortedAggregates = new SessionCache.TimedEntry(sortedAggregates,60*10*1000);
			cache.put(input.getCustomerId(),cachedSortedAggregates);
		} 
		
		{
			List sortedAggregates = (List)cachedSortedAggregates.getPayload();
			List shortList = 
				draw(
					sortedAggregates,input.getCartContents(),
					Math.max(
						Math.round(FDStoreProperties.getDYFFreqboughtTopPercent()*sortedAggregates.size()),
						FDStoreProperties.getDYFFreqboughtTopN()));
			return shortList.subList(0,Math.min(max, shortList.size()));
		}
	
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
