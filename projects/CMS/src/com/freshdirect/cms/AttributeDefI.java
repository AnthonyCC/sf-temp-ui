package com.freshdirect.cms;

import java.io.Serializable;

/**
 * Schema definition for an attribute of a {@link com.freshdirect.cms.ContentTypeDefI}.
 */
public interface AttributeDefI extends Serializable {

	/**
	 * @return type of the attribute
	 */
	public EnumAttributeType getAttributeType();
	
	/**
	 * @return machine readable name of the attribute
	 */
	public String getName();

	/**
	 * @return human readable name of the attribute
	 */
	public String getLabel();

	/**
	 * @return true if this attribute doesn't allow null values
	 */
	public boolean isRequired();

	/**
	 * @return true if the attribute is subject to contextual acquisition
	 */
	public boolean isInheritable();

	/**
	 * @return cardinality of the attribute
	 */
	public EnumCardinality getCardinality();
	
	/**
	 * @return true if attribute cardinality is One
	 */
	public boolean isCardinalityOne();
	
	public Object getEmptyValue();
	
	/**
	 * @return true if the attribute is unmutable
	 */
	public boolean isReadOnly();

}