package com.freshdirect.storeapi.spelling.domain;

public interface StringDistance {

	/**
	 * Returns an integer counting the number of differences found. Returning 0 means the two strings are identical.
	 * 
	 * @param s1
	 *            The first string.
	 * @param s2
	 *            The second string.
	 * @return an integer counting the number of differences found.
	 */
	public int getDistance(String s1, String s2);

}
