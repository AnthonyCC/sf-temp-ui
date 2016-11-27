/*
 * Created on Oct 28, 2004
 *
 */
package com.freshdirect.cms;



/**
 * Binding class for a named relationship attribute of a specific
 * {@link com.freshdirect.cms.ContentNodeI} instance. A relationship's
 * value are {@link com.freshdirect.cms.ContentKey}s: 
 * a single key for {@link com.freshdirect.cms.EnumCardinality#ONE}, or a
 * List of keys for {@link com.freshdirect.cms.EnumCardinality#MANY}).
 * 
 * @see com.freshdirect.cms.AttributeI
 */
public interface RelationshipI extends AttributeI {

	
}
