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
	
	/** ONLY FOR DEBUGGING */
	protected float[] findRank(ContentKey key, List sortedAggregates) {
		float[] result = new float[3];
		result[0] = -1;
		result[1] = -1;
		result[2] = -1;
		int z = 0;
		for(Iterator i = sortedAggregates.iterator(); i.hasNext();) {
			ContentAggregate ca = (ContentAggregate)i.next();
			if (ca.hashCode() == 0) continue;
			for (Iterator j = ca.keys(); j.hasNext();) {
				ContentKey product = (ContentKey)j.next();
				if (key.equals(product)) {
					result[0] = z;
					result[1] = ca.getScore();
					result[2] = ca.getScore(product);
					break;
				}
			}
			++z;
		}
		return result;
	}

	/**
	 * Used for putting back the temporarily removed items with their scores into 
	 * the sorted product list.
	 *
	 */
	private static class TemporarilyRemovedItem {
		private ContentKey key;
		private float score;
		private ContentAggregate aggregate;
		
		public TemporarilyRemovedItem(ContentKey key, float score, ContentAggregate aggregate) {
			this.key = key;
			this.score = score;
			this.aggregate = aggregate;
		}
		
		public void putBack() {
			aggregate.addContent(key, score);
		}
	}
	
	/**
	 * Draw elements from without replacement from the top elements.
	 * @param sortedAggregates sorted aggregates
	 * @param n items to draw
	 * @return items drawn
	 */
	protected List draw(List sortedAggregates, Collection cartContents, int n) {
		List result = new ArrayList(n);
		
		// SANITY CHECK FOR CORRECT IMPLEMENTATION, EXPENSIVE
		// ONLY COMMENT OUT FOR TESTING	
		int hashCodeIn = 0;
		//for(Iterator i = sortedAggregates.iterator(); i.hasNext();) {
		//	hashCodeIn += i.next().hashCode();
		//}
		
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
			List removed = new ArrayList(n);
			int c = 0;
			for(Iterator i= sortedAggregates.iterator(); i.hasNext() && c < n;) {
				ContentAggregate ca = (ContentAggregate)i.next();
				for(Iterator j = ca.keys(); j.hasNext(); ) {
					ContentKey productKey = (ContentKey)j.next();
					if (cartContents.contains(productKey)) {
						float score = ca.getScore(productKey);
						j.remove();
						//LOGGER.debug("Product " + productKey + " removed with score " + score);
						removed.add(new TemporarilyRemovedItem(productKey,score,ca));
						
					}
				}
				if (ca.getScore() > 0) {
					sampler.setItemFrequency(ca, Math.round(100*ca.getScore()));
					++c;
				}
			}
			
			
			
			while(sampler.getItemCount() > 0 && result.size() < n) {
				ContentAggregate ca = (ContentAggregate)sampler.getRandomItem(R);
				float scoreBefore = ca.getScore(); // comment out for testing
				ContentKey product = ca.take(R);
				float scoreAfter = ca.getScore();
				removed.add(new TemporarilyRemovedItem(product,(scoreBefore - scoreAfter),ca));
				result.add(product);
				sampler.setItemFrequency(ca, Math.round(100*ca.getScore()));
			}
			
			for(Iterator i = removed.iterator(); i.hasNext();) {
				((TemporarilyRemovedItem)i.next()).putBack();
			}
			
			//Comment out for debugging 
			//for(Iterator i = result.iterator(); i.hasNext();) {
			//	ContentKey product = (ContentKey)i.next();
			//	float[] ranks = findRank(product, sortedAggregates);
			//	
			//	System.err.println(" --> " + product + ranks[1] + ',' + ranks[2] + " (" + (int)ranks[0] + ',' + n + ")");
			//}
			
			// SANITY CHECK FOR CORRECT IMPLEMENTATION, EXPENSIVE
			// ONLY COMMENT OUT FOR TESTING	
			int hashCodeOut = 0;
			//for(Iterator i = sortedAggregates.iterator(); i.hasNext();) {
			//	hashCodeOut += i.next().hashCode();
			//}
			
			if (hashCodeIn != hashCodeOut) LOGGER.warn("Inconsistency in " + MostFrequentlyBoughtDyfVariant.class);
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
		SessionCache.TimedEntry cachedSortedAggregates = (SessionCache.TimedEntry)cache.get(input.getCustomerId());
		
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
				if (aggregateLabel == null) continue;
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
			
			cachedSortedAggregates = new SessionCache.TimedEntry(sortedAggregates,60*10*1000);
			cache.put(input.getCustomerId(),cachedSortedAggregates);
		} 
		
		synchronized(cachedSortedAggregates) {
			List sortedAggregates = (List)cachedSortedAggregates.getPayload();
			List shortList = 
				draw(
					sortedAggregates,input.getCartContents(),
					Math.max(
						(int)Math.round(
							((double)(
								FDStoreProperties.getDYFFreqboughtTopPercent()*
									sortedAggregates.size()))/100.0),
								FDStoreProperties.getDYFFreqboughtTopN()));
			return shortList;
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
