package com.freshdirect.framework.util;

import java.util.Iterator;
import java.util.Random;

/**
 * Sampling from a discrete distribution.
 * 
 * @author istvan
 */
public interface DiscreteRandomSampler {

	/**
	 * Set the frequency of the item.
	 * @param o item
	 * @param frequency
	 */
	public void setItemFrequency(Object o, long frequency);
	
	/**
	 * Get a frequency of an item.
	 * 
	 * @param o item
	 * @return frequency of item (and 0 if item is not part of distribution)
	 */
	public long getItemFrequency(Object o);
	
	/**
	 * Get the number of items known to the distribution.
	 * @return number of items
	 */
	public int getItemCount();
	
	/**
	 * Retrieve an item randomly from the distribution.
	 * 
	 * @param R that implements Uniform Discrete ({@link Random#nextInt(int)} and {@link Random#nextLong()})
	 * @return random value from the distribution
	 */
	public Object getRandomItem(Random R);
	
	/**
	 * Iterator over items.
	 * @return iterator over items
	 */
	public Iterator items();
	
	/**
	 * Get the total frequency of all items.
	 * @return total frequency
	 */
	public long getTotalFrequency();

}
