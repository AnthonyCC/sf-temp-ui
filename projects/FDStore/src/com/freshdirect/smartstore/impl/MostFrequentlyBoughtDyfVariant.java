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
import com.freshdirect.framework.util.DiscreteRandomSampler;
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
	 * Object that draws the requested number of elements from the distribution.
	 * @author istvan
	 *
	 */
	private static class Draw {
		private DiscreteRandomSampler sampler;
		private int totalProducts;
		private Random R = new Random();
		
		private Draw(DiscreteRandomSampler sampler, int totalProducts) {
			this.sampler = sampler;
			this.totalProducts = totalProducts;
		}
		
		// for testing only
		private float totalScore() {
			float z = 0;
			for(Iterator i = sampler.items(); i.hasNext(); ) {
				z += ((ContentAggregate)i.next()).getScore();
			}
			return z;
		}
		
		public List draw(int k) {
			/**
			 * This should only be commented out for testing.
			 * 
			 */
			//float zin = totalScore();
			
			
			int n = Math.min(k,totalProducts);
			List drawnProducts = new ArrayList(n);
			
			Map reverseMap = new HashMap(3*n/2+1);
			Map frequencyMap = new HashMap(3*n/2+1);
			
			for(int i=0; i<n && sampler.getItemCount() > 0; ++i) {
				ContentAggregate ca = (ContentAggregate)sampler.getRandomItem(R);
				float score = ca.getScore();
				ContentKey product = ca.take(R);
				drawnProducts.add(product);
				long newFrequency = Math.round(100*ca.getScore());
				sampler.setItemFrequency(ca, newFrequency);
				reverseMap.put(product, ca);
				frequencyMap.put(product, new Float(score-ca.getScore()));
				//System.err.println("drawn " + product + " " + (score - ca.getScore()));
			}
			
			for(Iterator i = reverseMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Map.Entry)i.next();
				ContentKey key = (ContentKey)e.getKey();
				((ContentAggregate)e.getValue()).addContent(key,((Float)frequencyMap.get(key)).floatValue());
			}
			
			for(Iterator i = reverseMap.values().iterator(); i.hasNext();) {
				ContentAggregate ca = (ContentAggregate)i.next();
				sampler.setItemFrequency(ca, Math.round(100*ca.getScore()));
			}
			
			/**
			 * Only comment out for testing.
			 */
			//float zout = totalScore(); 
			//if (zin != zout) {
			//	System.err.println("Frequency in: " + zin + " and out: " + zout);
			//}
			
			return drawnProducts;
		}
	}

	/**
	 * Recommend the most frequently bought products.
	 * @param max maximum number of products to produce
	 * @param input session input
	 */
	public List recommend(int max, SessionInput input) {
		// see if list in cache
		// List<ContentKey>
		SessionCache.TimedEntry cachedSampler = (SessionCache.TimedEntry)cache.get(input.getCustomerId());
		
		if (cachedSampler == null ||  cachedSampler.expired()) {
			LOGGER.debug("Loading order history for " + input.getCustomerId() + (cachedSampler != null ? " (EXPIRED)" : ""));

			
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
						
			cachedSampler = new SessionCache.TimedEntry(new Draw(sampler,productFrequencies.size()),10*60*1000);
			cache.put(input.getCustomerId(), cachedSampler);
		} 
		
		synchronized(cachedSampler) {
			Draw draw = (Draw)cachedSampler.getPayload();
		
		
			return draw.draw(input.getCartContents().size() + max);
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
