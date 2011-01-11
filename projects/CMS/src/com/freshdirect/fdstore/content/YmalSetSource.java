package com.freshdirect.fdstore.content;

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
