/*
 * Created on Oct 28, 2004
 */
package com.freshdirect.cms;

import java.util.Set;

/**
 * Schema definition of a relationship attribute.
 * Navigable relationships are "holding" relationships,
 * regular relationships are simple references - this has implications
 * for validation and content-tree display.
 * 
 * @see com.freshdirect.cms.RelationshipI
 */
public interface RelationshipDefI extends AttributeDefI {

	/**
	 * Get the destination content types this relationship can point to.
	 * 
	 * @return Set of {@link ContentType}
	 */
	public Set<ContentType> getContentTypes();

	/**
	 * 
	 * @return true if the relationship is navigable
	 */
	public boolean isNavigable();

}