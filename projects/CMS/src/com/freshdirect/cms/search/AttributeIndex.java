package com.freshdirect.cms.search;

/**
 * Configuration object to instruct {@link com.freshdirect.cms.search.LuceneSearchService}
 * to index a named attribute of nodes with a particular {@link com.freshdirect.cms.ContentType}.
 */
public class AttributeIndex extends ContentIndex {

	private String attributeName;

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

}