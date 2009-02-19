package com.freshdirect.smartstore.sampling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.smartstore.impl.IConfigurable;

/**
 * Sampler for lists of ranked content.
 * 

 * @author istvan
 *
 */
public class ContentSampler {
	
	/**
	 * A rule that decides how many items to consider in sampling.
	 * @author istvan
	 *
	 */
	public interface ConsiderationLimit extends IConfigurable {
	
		/**
		 * 
		 * @param rankedItems (List<{@link RankedContent}>)
		 * @return how many to remove max from ranked items
		 */
		public int max(List rankedItems);
	}
	
	
	/** List<{@link RankedContent}> */
	private List sortedItems;
	
	/** List<{@link RankedContent}> */
	protected List getSortedItems() {
		return sortedItems;
	}
	
	// total content
	private int total = 0;
	
	/**
	 * 
	 * @param allSortedItems List<{@link RankedContent}>
	 * @param cart Set<{@link ContentKey}>
	 * @param cl rule to decide number of content to keep
	 */
	private ContentSampler(List allSortedItems, Set cart, ConsiderationLimit cl) {
		sortedItems = new LinkedList();
		
		boolean reSort = false;
		
		double lastScore = Float.MAX_VALUE;
		
		int n = cl.max(allSortedItems); 
			
		NEXT_SORTED_ITEM: for(Iterator i = allSortedItems.iterator(); i.hasNext();) {
			RankedContent item = (RankedContent)i.next();
			if (n == sortedItems.size()) break;
			RankedContent itemToUse = item;
			if (item instanceof RankedContent.Aggregate) {
				// needs to copy since may mute it
				RankedContent.Aggregate copyAggregate = new RankedContent.Aggregate(item.getId());
				for(Iterator j = ((RankedContent.Aggregate)item).getItems().iterator(); j.hasNext(); ) {
					RankedContent.Single partItem = (RankedContent.Single)j.next();
					if (!cart.contains(partItem.getContentKey())) {
						copyAggregate.add(partItem);
						++total;
					}
				}
				if (copyAggregate.getCount() == 0) continue NEXT_SORTED_ITEM;
				itemToUse = copyAggregate;
			} else { // Single
				if (cart.contains(((RankedContent.Single)item).getContentKey())) continue NEXT_SORTED_ITEM;
				itemToUse = item;
				++total;
			}
			
			// this only happens if not sorted to begin with, or aggregate score changed
			if (itemToUse.getScore() > lastScore) {
				reSort = true;
			}
			
			sortedItems.add(itemToUse);
			lastScore = itemToUse.getScore();
		}
		
		if (reSort) {
			Collections.sort(sortedItems,new Comparator() {

				public int compare(Object o1, Object o2) {
					RankedContent r1 = (RankedContent)o1;
					RankedContent r2 = (RankedContent)o2;
					return r1.getScore() < r2.getScore() ? +1 : r1.getScore() > r2.getScore() ? -1 : 0; 
				}
			});
		}
	}
	
	/**
	 * 
	 * @return item count (including those in aggregates)
	 */
	public int totalItems() {
		return total;
	}
	
	/**
	 * 
	 * @param k
	 * @param sampler
	 * @return List<{@link ContentNode}>
	 */
	private List drawWithoutReplacement(int k, ListSampler sampler) {
		if (k > totalItems()) k = totalItems();
		List items = new ArrayList(k);
		for(int i=0; i<k && sortedItems.size() > 0; ++i) {
			
			int ind = sampler.next(sortedItems.size()); 
		
			ListIterator it = sortedItems.listIterator(ind);
			RankedContent item = (RankedContent)it.next();
			
			RankedContent.Single itemToUse = null;
			
			if (item instanceof RankedContent.Aggregate) { // must re-calculate distribution
				RankedContent.Aggregate aggregate = (RankedContent.Aggregate)item;
				itemToUse = aggregate.take();
				if (aggregate.getCount() == 0) { // lucky, we can just remove
					it.remove();
				} else { 
					// not so lucky, needs to re-insert node in proper place, plus 
					// re-calculate the distribution
					ListIterator prev = sortedItems.listIterator(ind);
					// reinsert where it belongs to	
					sampler.changeWeight(ind, aggregate.getScore());
					while(prev.hasPrevious()) {
						RankedContent prevItem = (RankedContent)prev.previous();
						RankedContent thisItem = (RankedContent)it.previous();
						if (prevItem.getScore() > thisItem.getScore()) {
							prev.set(thisItem);
							it.set(prevItem);
							sampler.changeWeight(ind,prevItem.getScore());
							sampler.changeWeight(--ind,thisItem.getScore());
						} else {
							break;
						}
					}
				}
			} else { // Single (and how simple)
				itemToUse = (RankedContent.Single)item;
				it.remove();
				sampler.changeWeight(ind,0);
			}
			
			//items.add(itemToUse.getContentKey());
			items.add(itemToUse);
			--total;
		}
		return items;
	}
	
	/**
	 * Sample without replacement.
	 * 
	 * @param allSortedItems List<{@link RankedContent}>
	 * @param cart Set<{@link ContentKey}>
	 * @param n maximum number of {@link RankedContent}s to take from allSortedItems which are not in the cart 
	 * @param k number of items to draw
	 * @param sampler
	 * @return List<{@link ContentKey}>
	 */
	public static List drawWithoutReplacement(List allSortedItems, Set cart, ConsiderationLimit cl, int k, ListSampler sampler) {
		ContentSampler rankedItems = new ContentSampler(allSortedItems,cart,cl);
		return rankedItems.drawWithoutReplacement(k, sampler);
	}
	
	/**
	 * Sampler that interprets the content score ratios as actual probabilities.
	 * 
	 * This is an expensive sampler, since it needs to repeatedly recalculate the
	 * distribution after an item has been drawn.
	 *  
	 * @author istvan
	 */
	private static class ExplicitSampler extends ListSampler {
		
		private int[] weights; // individual weights
		private int[] cumWeights; // sum of weigths
		private boolean recalculate = false; // lazy stuff
		
		private int weight(double w) {
			return (int)(100*w);
		}
		
		private void calculateCumulativeWeights(int n) {
			
			for(int i=0; i< n; ++i) {
				int j = i;
				while(weights[j] == 0) { // remove zeros
					if (j >= weights.length-1) break;
					++j;
				}
				if (j > 0) {
					for(int k=i; k< weights.length-(j-i); ++k) {
						weights[k] = weights[k+j-i];
					}
				}
				cumWeights[i] = weights[i] + (i > 0 ? cumWeights[i-1] : 0);
			}
			recalculate = false;
		}
		
		/**
		 * Constructor.
		 * 
		 * @param R
		 * @param items List<{@link RankedContent}>
		 */
		private ExplicitSampler(Random R, List items) {
			super(R);
			weights = new int[items.size()];
			cumWeights = new int[items.size()];
			int i = 0;
			for(Iterator it = items.iterator(); it.hasNext();) {
				RankedContent item = (RankedContent)it.next();
				int w = weight(item.getScore());
				if (w < 0) {
					w = 0;
				}
				weights[i++] = w;
			}
			calculateCumulativeWeights(items.size());
		}

		public int cumulativeWeight(int i, int n) {
			return cumWeights[i];
		}

		public int weight(int i, int n) {
			return weights[i];
		}
		
		public int next(int n) {
			if (recalculate) calculateCumulativeWeights(n);
			return super.next(n);
		}
		
		public void changeWeight(int i, double nw) {
			int w = weight(nw);
			if (w < 0) {
				w = 0;
			}
			if (w != weights[i]) {
				weights[i] = w;
				recalculate = true;
			}
		}

		public String getName() {
			return "explicit";
		}
	};
	
	/**
	 * Sample without replacement using the {@link ExplicitSampler} sampler.
	 * 
	 * @param allSoretedItems List<{@link RankedContent}>
	 * @param cart Set<{@link ContentKey}>
	 * @param cl max content to sample from
	 * @param k max content to select
	 * @return List<{@link ContentKey}>
	 */
	public static List drawWithoutReplacement(final List allSoretedItems, Set cart, ConsiderationLimit cl, int k) {
		ContentSampler rankedItems = new ContentSampler(allSoretedItems,cart,cl);
		return rankedItems.drawWithoutReplacement(k, new ExplicitSampler(new Random(),rankedItems.getSortedItems()));
	}
	
	/**
	 * Remove the top scoring content.
	 * 
	 * @param allSortedItems List<{@link RankedContent}>
	 * @param cart Set<{@link ContentKey}>
	 * @param n number of items to remove (those that are in cart don't count)
	 * @return List<{@link ContentKey}>
	 */
	public static List drawTop(List allSortedItems, Set cart, int n) {
		List results  = new ArrayList(n);
		for(Iterator i = allSortedItems.iterator(); i.hasNext();) {
			RankedContent item = (RankedContent)i.next();
			if (results.size() == n) break;
			if (item instanceof RankedContent.Aggregate) {
				for(Iterator j = ((RankedContent.Aggregate)item).getItems().iterator(); j.hasNext(); ) {
					RankedContent.Single partItem = (RankedContent.Single)j.next();
					if (!cart.contains(partItem)) {
						results.add(partItem);
						((RankedContent.Aggregate)item).remove(partItem.getId());
					}
					break;
				}
			} else {
				if (!cart.contains(((RankedContent.Single)item).getContentKey())) {
					results.add(((RankedContent.Single)item).getContentKey());
				}
			}
		}
		return results;
	}
}