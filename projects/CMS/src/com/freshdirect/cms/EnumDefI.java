package com.freshdirect.cms;

import java.util.Map;

/**
 * Schema definition of an enumerated attribute. An enumerated
 * attribute has a fixed set of values of a primitve type
 * (with a label for each).
 */
public interface EnumDefI extends AttributeDefI {

	/**
	 * Get the Map of possible values with their labels.
	 * 
	 * @return Map of Object (value) -> String (label)
	 */
	public Map<Object,String> getValues();

	/**
	 * Get the type of the enumerated value.
	 * 
	 * @return a primitive attribute type
	 */
	public EnumAttributeType getValueType();
	
}
