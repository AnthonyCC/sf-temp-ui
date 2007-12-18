package com.freshdirect.framework.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.Map;
import java.util.Random;
import java.util.Iterator;

/**
 * Picks random objects according to a reference distribution.
 * 
 * Objects with frequencies can be added to establish a reference distribution.
 * The {@link #getRandomValue} method then can pick randomly according to this reference
 * distribution. It is assumed that the reference distribution is a good model for the "real" distribution
 * (i.e. it is based on representative samples and contains adequate samples).
 * 
 * Internally the so called <i>inverse transform sampling method</i> is implemented.
 * Suppose there are 3 objects, A, B and C with frequencies 2, 3 and 5. The cumulative frequency
 * distribution (i.e. F(x) = P(X <= x)) of these objects is: F(A) = 2, F(B) = (2+3) = 5 and F(C) = (5+5) = 10,
 * when the objects are listed in this order. Thus the following mapping can be (implicitly) established:
 * {0,1} -> A, {2,3,4} -> B and {5,6,7,8,9} -> C (i.e. 2 values map to A, 3 values map to B and 5 values map to C;
 * the sets are individually bounded above by F(x)). Pick a uniform random number in [0,10) and find which value
 * it maps to. (e.g. if 6 were picked, the outcome is C). The implementation caches the reverse cumulative 
 * distribution (ie. A:2, B:5, C:10), which changes with each value added or frequency revised. Thus it is best 
 * to first establish the distribution with samples and then start generating the random values.
 * 
 * @author istvan
 */
public class DiscreteRandomSampler {
	
	
	private boolean calculated = false; // needs to revise inverse cdf
	private boolean newValue = false; // new value was added
	private long total = 0; // total number of samples
	
	private Map values; // value->frequency
	
	private long[] cdf = new long[0]; // cdf corresponding to sortedValues
	private Object[] sortedValues = new String[0]; // values in any reliably established order
	
	
	/**
	 * Constructor.
	 *
	 * Uses the natural ordering of values.
	 */
	public DiscreteRandomSampler() {
		values = new TreeMap();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param comp custom comparator for values
	 */
	public DiscreteRandomSampler(Comparator comp) {
		values = new TreeMap(comp);
	}
	
	public void clear() {
		values.clear();
		calculated = false;
		newValue = false;
		total = 0;
		cdf = new long[0];
		sortedValues = new String[0];
	}
	
	/**
	 * Add a value with a frequency.
	 *
	 * If the value already exists, its frequency is incremented by the argument.
	 * In case of negative frequencies, the frequency is decreased (but cannot fall
	 * below zero).
	 * @param value
	 * @param frequency
	 * @see #setValue(Object, long)
	 */
	public void addValue(Object value, long frequency) {
		
		if (frequency == 0) return;
		
		calculated = false;
		
		Long freq = (Long)values.get(value);
		
		if (freq == null) {		
			freq = new Long(frequency);	
			newValue = true;
		} else {
			freq = new Long(freq.longValue() + frequency);
		}
		
		if (freq.longValue() < 0) throw new IllegalArgumentException("Frequency must be non-negative (and not " + freq.longValue() + ')');
		else if (freq.longValue() == 0) {
			newValue = true;
			values.remove(value);
		} else {
			values.put(value, freq);
		}

		total += frequency;
	}
	
	/**
	 * Set a value's frequency.
	 * 
	 * If the value does not exist, it will be added with the given frequency, otherwise
	 * the old frequency is overwritten. A 0 frequency will erase the object from the possible
	 * choices.
	 * 
	 * @param value
	 * @param frequency
	 * @see #addValue(Object, long)
	 */
	public void setValue(Object value, long frequency) {
		
		if (frequency < 0) throw new IllegalArgumentException("Frequency must be non-negative (and not " + frequency + ')');
		
		
		Long freq = (Long)values.get(value);
		
		
		if (freq == null) {
			addValue(value,frequency);
		} else {
			long oldFreq = freq.longValue();
			if (frequency == 0) {
				newValue = true;
				values.remove(value);
				calculated = false;
				total -= oldFreq;
			} else {
				if (oldFreq != frequency) {
					total += frequency - oldFreq;
					calculated = false;
					values.put(value, new Long(frequency));
				}
			}
		}
	}
	
	/**
	 * Add a value.
	 * 
	 * Same as <code>addValue(value,1)</code>.
	 * @param value
	 */
	public void addValue(Object value) {
		addValue(value,1);
	}
	
	/**
	 * Calculate reverse CDF (if changes have been made).
	 */
	private void calculate() {
		if (calculated) return;
		
		if (newValue) {
			cdf = new long[values.size()];
			sortedValues = new Object[values.size()];
		}
		
		int cumm = 0;
		int index = 0;
		for(Iterator i = values.keySet().iterator(); i.hasNext(); ++index) {
			Object value = i.next();
			cumm += ((Long)values.get(value)).longValue();
			cdf[index] = cumm;
			sortedValues[index] = value;
			
		}
		
		newValue = false;
		calculated = true;
	}
	
	/**
	 * Get total number of frequencies across all objects.
	 * @return total number of object frequencies.
	 */
	public long getTotal() { return total; }
	
	/**
	 * Get the frequency of an object.
	 *
	 * @param value
	 * @return frequency of value (if not found, return 0)
	 */
	public long getFrequency(Object value) {
		Long freq = (Long)values.get(value);
		if (freq == null) return 0;
		return freq.longValue();
	}

	/**
	 * Get a value according to the reference distribution.
	 * 
	 * R's {@link java.util.Random#nextInt(int) nextInt} method is used to pick the random values
	 * if the total sum of frequencies is less or equal to {@link Integer#MAX_VALUE}, otherwise
	 * the {@link java.util.Random#nextLong() nextLong} value's absolute value is truncated (wich is
	 * "less random").
	 * 
	 * The complexity is O(log(n)), where n is the total number of discrete values (and not samples)
	 * when the inverse CDF is cached. Otherwise the cache is also calculated with O(n) performance. 
	 *  
	 * @param R random number generator
	 * @return a random value from the pool
	 * @throws NoSuchElementException is there are no values
	 */
	public Object getRandomValue(Random R) {
		if (total == 0) throw new NoSuchElementException();
		calculate();
		
		long rand = total <= Integer.MAX_VALUE ? R.nextInt((int)total) : R.nextLong() % total;
		if (rand < 0) rand = -rand;
		
		int z = Arrays.binarySearch(cdf,rand);
		if (z < 0) z = -z - 1;
		else ++z;
		
		return sortedValues[z];	
	}
}
