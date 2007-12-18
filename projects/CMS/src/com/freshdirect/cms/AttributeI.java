/*
 * Created on Oct 28, 2004
 *
 */
package com.freshdirect.cms;

import java.io.Serializable;

/**
 * Binding class for a named attribute of a specific
 * {@link com.freshdirect.cms.ContentNodeI} instance. 
 */
public interface AttributeI extends Serializable {

	/**
	 * Get the value bound to this attribute.
	 * 
	 * @return the value of the binding (may be null)
	 */
	public Object getValue();

	
	/**
	 * Set the value of the attribute binding.
	 * 
	 * @param o binding value (may be null)
	 */
	public void setValue(Object o);

	/**
	 * Get the {@link ContentNodeI} that this attribute binds to.
	 * 
	 * @return content node (never null)
	 */
	public ContentNodeI getContentNode();

	/**
	 * @return schema definition of this attribute
	 */
	public AttributeDefI getDefinition();

	/**
	 * @return name of attribute binding (never null)
	 */
	public String getName();

}
