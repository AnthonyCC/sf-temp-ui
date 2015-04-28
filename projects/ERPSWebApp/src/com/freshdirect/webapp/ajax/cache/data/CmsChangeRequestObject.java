package com.freshdirect.webapp.ajax.cache.data;

import java.util.HashSet;
import java.util.Set;

public class CmsChangeRequestObject {

	private Set<String> contentKeys = new HashSet<String>();

	/**
	 * @return the contentKeys
	 */
	public Set<String> getContentKeys() {
		return contentKeys;
	}

	/**
	 * @param contentKeys the contentKeys to set
	 */
	public void setContentKeys(Set<String> contentKeys) {
		this.contentKeys = contentKeys;
	}
	
}
