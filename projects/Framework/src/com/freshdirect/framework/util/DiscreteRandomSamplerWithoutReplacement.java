package com.freshdirect.framework.util;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.SortedMap;

import java.util.TreeMap;

/**
 * Get random values from a discrete distribution specified as a histogram.
 * 
 * The method used for drawing samples is the same as for {@link DiscreteRandomSamplerWithReplacement}
 * (where it is described in more detail), but this class implements changes to the histogram much
 * more efficiently (at the cost of storing extra indexes) and provides the 
 * {@link #removeRandomValue(Random)} method which draws an item
 * randomly and then removes it from the distribution (thus "without replacement"). 
 * 
 * Repeatedly removing all <i>n</i> items with {@link #removeItem(Object)} has 
 * complexity O(<i>n</i><sup>2</sup>).
 *   
 * @author istvan
 *
 */
public class DiscreteRandomSamplerWithoutReplacement implements DiscreteRandomSampler {
	
	// Map<Object,Postion>
	private SortedMap items;
	
	// List<CumulativeWeight>
	private List totals;
	
	// Mutable integer (since ints cannot be stored in Maps)
	private static class Position {
		private int value;
		
		public Position(int value) {
			this.value = value;
		}
		
		public int intValue() {
			return value;
		}
		
		public void dec() {
			--value;
		}
	}
	
	// Stores the item and its cumulative weight (aka CDF)
	private static class CumulativeWeight implements Comparable {
		private Object item;
		private long cummulativeWeight;
		
		private CumulativeWeight(long cummulativeWeight) {
			this(null,cummulativeWeight);
		}
		
		public CumulativeWeight(Object item, long cummulativeWeight) {
			this.cummulativeWeight = cummulativeWeight;
			this.item = item;
		}
		
		public void adjust(long adjustment) {
			cummulativeWeight += adjustment;
		}
		
		public Object getItem() {
			return item;
		}
		
		public long getWeight() {
			return cummulativeWeight;
		}

		public int compareTo(Object o) {
			CumulativeWeight cw = (CumulativeWeight)o;
			return (int)(getWeight() - cw.getWeight());
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cmp comparator over items
	 * @param capacity a tight upper bound on item count
	 */
	public DiscreteRandomSamplerWithoutReplacement(Comparator cmp, int capacity) {
		totals = new ArrayList(capacity);
		items = new TreeMap(cmp);
	}
	
	/**
	 * Constructor.
	 * @param cmp comparator over items
	 */
	public DiscreteRandomSamplerWithoutReplacement(Comparator cmp) {
		this(cmp,32);
	}
	
	/**
	 * Get total frequency of all items.
	 */
	public long getTotalFrequency() {
		return totals.isEmpty() ? 0l : ((CumulativeWeight)(totals.get(totals.size()-1))).getWeight();
	}
	
	// with pos
	private void setItemFrequency(int pos, long frequency) {
		long oldFreq = getItemFrequency(pos);
		
		long diffFreq = frequency - oldFreq;
		if (diffFreq == 0) return;
		
		for(Iterator i = totals.listIterator(pos); i.hasNext();) {
			CumulativeWeight cw = (CumulativeWeight)i.next();
			cw.adjust(diffFreq);
		}
		
		if (frequency == 0) {
			Object item = ((CumulativeWeight)totals.get(pos)).getItem();
			
			for(int i = pos + 1; i < totals.size(); ++i) {
				CumulativeWeight cw = (CumulativeWeight)totals.get(i);
				((Position)items.get(cw.getItem())).dec();
			}
			
			items.remove(item);
			totals.remove(pos);
		}
	}
	
	/**
	 * Get number of items.
	 * @return number of items
	 */
	public int getItemCount() {
		return items.size();
	}
	
	/**
	 * Set the frequency of an item.
	 * 
	 * If the item is not known, it will be added. If it exists its
	 * frequency will be reset.
	 * 
	 * @param item 
	 * @param frequency
	 */
	public void setItemFrequency(Object item, long frequency) {
		
		Position pos = (Position)items.get(item);
		if (pos == null) {
			if (frequency == 0) return;
			totals.add(new CumulativeWeight(item,getTotalFrequency() + frequency));
			items.put(item,new Position(totals.size()-1));
		} else {
			setItemFrequency(pos.intValue(), frequency);
			
		}
	}
	
	// from pos
	private long getItemFrequency(int pos) {
		long cdfBottom = pos > 0 ? ((CumulativeWeight)totals.get(pos-1)).getWeight() : 0;
		long cdfOldTop = ((CumulativeWeight)totals.get(pos)).getWeight();
		return cdfOldTop - cdfBottom;
	}
	
	/**
	 * Get an item's frequency.
	 * 
	 * @param item
	 * @return item's frequency (0 when item not known)
	 */
	public long getItemFrequency(Object item) {
		Position pos = (Position)items.get(item);
		return pos == null ? 0 : getItemFrequency(pos.intValue());
	}
		
	/**
	 * Remove item from distribution.
	 * 
	 * Recalculates the distribution.
	 * @param item
	 */
	public void removeItem(Object item) {
		setItemFrequency(item, 0);
	}
	
	// get pos
	private int getRandomValuePosition(Random R) {
		if (items.isEmpty()) throw new NoSuchElementException();
		
		long total = getTotalFrequency();
		
		long rand = total <= Integer.MAX_VALUE ? R.nextInt((int)total) : R.nextLong() % total;
		if (rand < 0) rand = -rand;
		
		int z = Collections.binarySearch(totals,new CumulativeWeight(rand));
		if (z < 0) z = -z - 1;
		else ++z;
	
		return z;
	}
	
	/**
	 * Generate a random item without removing it from distribution.
	 * @param R random stream
	 * @return a random item
	 */
	public Object getRandomItem(Random R) {
		return ((CumulativeWeight)totals.get(getRandomValuePosition(R))).getItem();	
	}
	
	/**
	 * Generate a random item and remove it from the distribution.
	 * @param R random stream
	 * @return a random item
	 */
	public Object removeRandomValue(Random R) {
		int z = getRandomValuePosition(R);
		Object item = ((CumulativeWeight)totals.get(z)).getItem();
		setItemFrequency(z,0);
		return item;
	}
	
	/**
	 * Iterator over items.
	 * @return iterator items
	 */
	public Iterator items() {
		return items.keySet().iterator();
	}
	
}
