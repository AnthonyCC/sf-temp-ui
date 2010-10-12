/*
 * $Workfile: AttributesI.java$
 *
 * $Date: 9/4/2001 5:41:11 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes;

/**
 * Interface for objects with attributes.
 *
 * @version $Revision: 2$
 * @author $Author: Viktor Szathmary$
 */
public interface AttributesI extends java.io.Serializable {

	/**
	 * Check for the presence of an attribute.
	 *
	 * @param name name of the attribute
	 * @return true if the attribute was found
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
	 */
	public boolean hasAttribute(String name);

	/**
	 * Get attribute as a String.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return String attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
	 */
	public String getAttribute(String name, String defaultValue);

	/**
	 * Get attribute as a String.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return String attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public String getAttribute(EnumAttributeName attributeName);

	/**
	 * Get attribute as a boolean.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return boolean attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
	 */
	public boolean getAttributeBoolean(String name, boolean defaultValue);

	/**
	 * Get attribute as a boolean.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return boolean attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public boolean getAttributeBoolean(EnumAttributeName attributeName);

	/**
	 * Get attribute as an integer.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return int attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
	 */
	public int getAttributeInt(String name, int defaultValue);

	/**
	 * Get attribute as a integer.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return int attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public int getAttributeInt(EnumAttributeName attributeName);

}
