package com.freshdirect.fdstore.content.test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.freshdirect.cms.ContentKey;

public interface ContentNodeSampler {

	public abstract void addContentKeys(Collection contentKeys);

	/**
	 * Set the frequency of a particular content key.
	 * 
	 * @param id
	 * @param frequency
	 */
	public abstract void setFrequency(String id, int frequency);

	public abstract void resetFrequencies();

	/**
	 * 
	 * @return content keys 
	 */
	public abstract Collection getContentKeys();

	/**
	 * Get the frequency of the content key.
	 * @param id
	 * @return
	 */
	public abstract long getFrequency(String id);

	public abstract void addFrequenciesFromMap(Map frequencies);

	public abstract void setFrequenciesFromMap(Map frequencies);

	/**
	 * 
	 * @param n number of keys to get
	 * @param R random number generator
	 * @return a random sample of content keys according to the reference distribution
	 */
	public abstract List getSampleContentKeys(int n, Random R);

	/**
	 * @param R uniform random stream
	 * @return a random content key according to the loaded distribution
	 */
	public abstract ContentKey next(Random R);

}