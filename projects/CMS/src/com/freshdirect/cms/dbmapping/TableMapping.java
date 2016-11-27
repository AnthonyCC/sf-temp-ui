package com.freshdirect.cms.dbmapping;

/**
 * Configuration object for {@link com.freshdirect.cms.dbmapping.DbMappingContentService}
 * that describes a query mapping to a content type.
 * 
 * @TODO could support multiple content types per table
 */
public class TableMapping {

	private String contentType;
	private String query;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
