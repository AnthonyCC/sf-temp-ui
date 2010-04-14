package com.freshdirect.smartstore.filter;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * This filter removes item duplicates producing a list of unique items
 * 
 * @author segabor
 * 
 */
public final class UnicityFilter extends ContentFilter {
	private static final Logger LOGGER = LoggerFactory.getInstance(UnicityFilter.class);
	
	Set<ContentKey> keys;

	public UnicityFilter() {
		this.keys = new HashSet<ContentKey>();
	}

	public ContentKey filter(ContentKey key) {
		if (!keys.contains(key)) {
			keys.add(key);
			return key;
		}
		LOGGER.debug("not unique: " + key);
		return null;
	}

	public void reset() {
		keys.clear();
	}
}