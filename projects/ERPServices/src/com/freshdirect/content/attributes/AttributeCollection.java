/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes;

import java.util.Map;
import java.util.HashMap;

/**
 * Collection of attributes.
 *
 * @version $Revision$
 * @author $Author$
 */
public class AttributeCollection implements AttributesI {

	private final Map attributes;

	public AttributeCollection() {
		this.attributes = new HashMap();
	}

	public AttributeCollection(Map attributes) {
		if (attributes==null) {
			throw new NullPointerException();
		}
		this.attributes = attributes;
	}
    
    public AttributeCollection(AttributeCollection attrColl) {
        if (attrColl == null)
            throw new NullPointerException();
        if (attrColl.attributes == null)
            throw new NullPointerException();
        this.attributes = new HashMap(attrColl.attributes);
    }
    
    Map getMap() {
        return this.attributes;
    }

	public void setAttribute(String name, String value) {
		this.attributes.put(name, value);
	}

	public void setAttribute(String name, boolean value) {
		this.attributes.put(name, new Boolean(value));
	}

	public void setAttribute(String name, int value) {
		this.attributes.put(name, new Integer(value));
	}

	public boolean hasAttribute(String name) {
		return this.attributes.get(name)!=null;
	}

	/**
	 * Get attribute as a String.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return String attribute
	 */
	public String getAttribute(String name, String defaultValue) {
		try {
			String s = (String)this.attributes.get(name);
			if (s==null) {
				return defaultValue;
			}
			return s;
		} catch (ClassCastException e) {
			return defaultValue;
		}
	}

	/**
	 * Get attribute as a String.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return String attribute
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public String getAttribute(EnumAttributeName attributeName) {
		return this.getAttribute( attributeName.getName(), (String)attributeName.getDefaultValue() );
	}

	/**
	 * Get attribute as a boolean.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return boolean attribute
	 */
	public boolean getAttributeBoolean(String name, boolean defaultValue) {
		try {
			Boolean b = (Boolean)this.attributes.get(name);
			if (b==null) {
				return defaultValue;
			}
			return b.booleanValue();
		} catch (ClassCastException e) {
			return defaultValue;
		}
	}

	/**
	 * Get attribute as a boolean.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return boolean attribute
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public boolean getAttributeBoolean(EnumAttributeName attributeName) {
		return this.getAttributeBoolean( attributeName.getName(), ((Boolean)attributeName.getDefaultValue()).booleanValue() );
	}

	/**
	 * Get attribute as an integer.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return int attribute
	 */
	public int getAttributeInt(String name, int defaultValue) {
		try {
			Integer i = (Integer)this.attributes.get(name);
			if (i==null) {
				return defaultValue;
			}
			return i.intValue();
		} catch (ClassCastException e) {
			return defaultValue;
		}
	}

	/**
	 * Get attribute as a integer.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return int attribute
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public int getAttributeInt(EnumAttributeName attributeName) {
		return this.getAttributeInt( attributeName.getName(), ((Integer)attributeName.getDefaultValue()).intValue() );
	}

}
