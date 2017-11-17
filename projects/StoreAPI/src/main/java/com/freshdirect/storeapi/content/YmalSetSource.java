package com.freshdirect.storeapi.content;

import java.util.List;

public interface YmalSetSource extends ContentNodeModel {
	/**
	 * Returns YmalSet from CMS
	 * @return
	 */
	public List<YmalSet> getYmalSets();
	
	public boolean hasActiveYmalSets();

	public YmalSetSource getParentYmalSetSource();

}
