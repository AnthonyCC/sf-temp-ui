package com.freshdirect.smartstore.filter;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;

public class ArrayFilter extends ContentFilter {
	List<ContentFilter> filters;

	protected ArrayFilter() {
		filters = new ArrayList<ContentFilter>();
	}

	public ContentKey filter(ContentKey key) {
		for (int i = 0; i < filters.size(); i++) {
			key = filters.get(i).filter(key);
			if (key == null) {
				return null;
			}
		}
		return key;
	}

	public void addFilter(ContentFilter filter) {
		filters.add(filter);
	}
}
