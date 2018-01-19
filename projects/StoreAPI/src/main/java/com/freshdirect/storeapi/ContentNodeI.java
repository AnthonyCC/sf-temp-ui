package com.freshdirect.storeapi;

import java.io.Serializable;
import java.util.Set;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;

/**
 * A self-describing, typed content object instance with a primary key identifier.
 * <p>
 * Content nodes have a number of named key-value bindings called attributes. These
 * can be simple scalar values ({@link com.freshdirect.storeapi.AttributeI}) or
 * relationships to other content nodes ({@link com.freshdirect.cms.RelationshipI}).
 * <p>
 * A content object can describe its structure and schema by providing a
 * {@link com.freshdirect.cms.ContentTypeDefI} and related attribute definition objects.
 * <p>
 * Implementations of <code>hashCode()</code> and <code>equals(o)</code> are expected
 * to check against the {@link com.freshdirect.cms.ContentKey} only, not attribute values.
 */
@CmsLegacy
public interface ContentNodeI extends Serializable {

	//
	// core
	//

	/**
	 *
	 * @return ContentKey
	 */
	public ContentKey getKey();

	/**
	 * Get the type definition for this content object.
	 * Default implementations will look up the type definition in the global CmsManager,
	 * but certain nodes might have custom dynamic definitions.
	 */
	// public ContentTypeDefI getDefinition();

	//
	// attributes
	//

	/**
	 * Get a named attribute.
	 *
	 * @return {@link Attribute} or null only if no such attribute is defined
	 */
	public Attribute getAttribute(String name);

    public Object getAttributeValue(Attribute definition);

    public Object getAttributeValue(String name);

	public boolean setAttributeValue(String name, Object value);

	//
	// convenience
	//

	/**
	 * Get keys of nodes that this node has navigable relationships to.
	 *
	 * @return Set of {@link ContentKey} (never null)
	 */
	public Set<ContentKey> getChildKeys();

	/**
	 * Get a human readable label for this content node.
	 *
	 * @TODO should be moved out to an adapter
	 */
	public String getLabel();

	//
	// infrastructure
	//

	/**
	 * @return a deep copy of the node
	 */
	public ContentNodeI copy();

}