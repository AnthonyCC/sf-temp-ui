package com.freshdirect.cms.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility methods to handle complex collections.
 */
public class CollectionUtil {

	private CollectionUtil() {
	}

	/**
	 * Add a keyed element to a Map where values are Lists. The
	 * specified value is added to the List with the given key.
	 * An ArrayList is created if no value exists for this key.
	 * 
	 * @param map Map to add to
	 * @param key key in the Map
	 * @param value value to add to the List.
	 */
	public static void addToMapOfLists(Map map, Object key, Object value) {
		List l = (List) map.get(key);
		if (l == null) {
			l = new ArrayList();
			map.put(key, l);
		}
		l.add(value);
	}

	/**
	 * Add a keyed element to a Map where values are Sets. The
	 * specified value is added to the Set with the given key.
	 * An HashSet is created if no value exists for this key.
	 * 
	 * @param map Map to add to
	 * @param key key in the Map
	 * @param value value to add to the List.
	 */
	public static void addToMapOfSets(Map map, Object key, Object value) {
		Set s = (Set) map.get(key);
		if (s == null) {
			s = new HashSet();
			map.put(key, s);
		}
		s.add(value);
	}
}
