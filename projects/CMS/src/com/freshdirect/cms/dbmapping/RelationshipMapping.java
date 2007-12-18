package com.freshdirect.cms.dbmapping;

/**
 * Configuration object for {@link com.freshdirect.cms.dbmapping.DbMappingContentService}
 * that describes a query mapping to a relationship between two content types.
 * 
 * @TODO support multiple content types
 */
public class RelationshipMapping {

	private String name;
	private String label;
	private String query;
	private String sourceContentType;
	private String destinationContentType;
	private boolean navigable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getDestinationContentType() {
		return destinationContentType;
	}

	public void setDestinationContentType(String childContentType) {
		this.destinationContentType = childContentType;
	}

	public String getSourceContentType() {
		return sourceContentType;
	}

	public void setSourceContentType(String parentContentType) {
		this.sourceContentType = parentContentType;
	}

	public boolean isNavigable() {
		return navigable;
	}

	public void setNavigable(boolean navigable) {
		this.navigable = navigable;
	}

}
