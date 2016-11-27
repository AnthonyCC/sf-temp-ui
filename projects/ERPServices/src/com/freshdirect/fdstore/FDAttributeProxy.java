/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import com.freshdirect.content.attributes.AttributesI;
import com.freshdirect.content.attributes.EnumAttributeName;

/**
 * Attribute proxy class.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDAttributeProxy implements AttributesI {

	private AttributesI attributes;

	public FDAttributeProxy(AttributesI attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * Check for the presence of an attribute.
	 *
	 * @param name name of the attribute
	 * @return true if the attribute was found
	 */
	public boolean hasAttribute(String name){
		return this.attributes.hasAttribute(name);
	}

	/**
	 * Get attribute as a String.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return String attribute
	 */
	public String getAttribute(String name, String defaultValue) {
		return this.attributes.getAttribute(name, defaultValue);
	}

	/**
	 * Get attribute as a String.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return String attribute
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public String getAttribute(EnumAttributeName attributeName) {
		return this.attributes.getAttribute( attributeName );
	}

	/**
	 * Get attribute as a boolean.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return boolean attribute
	 */
	public boolean getAttributeBoolean(String name, boolean defaultValue) {
		return this.attributes.getAttributeBoolean(name, defaultValue);
	}

	/**
	 * Get attribute as a boolean.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return boolean attribute
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public boolean getAttributeBoolean(EnumAttributeName attributeName) {
		return this.attributes.getAttributeBoolean( attributeName );
	}

	/**
	 * Get attribute as an integer.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return int attribute
	 */
	public int getAttributeInt(String name, int defaultValue) {
		return this.attributes.getAttributeInt(name, defaultValue);
	}

	/**
	 * Get attribute as a integer.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return int attribute
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public int getAttributeInt(EnumAttributeName attributeName) {
		return this.attributes.getAttributeInt( attributeName );
	}

        public String getSkuCode() {
            return getAttribute(EnumAttributeName.SKUCODE);
        }

        public String getUnderLabel() {
            return getAttribute(EnumAttributeName.UNDER_LABEL);
        }
    
        public int getPriority() {
            return getAttributeInt(EnumAttributeName.PRIORITY);
        }
    
        public int getPriority(int defValue) {
            return getAttributeInt(EnumAttributeName.PRIORITY.getName(), defValue);
        }
    
        public String getDescription(String defValue) {
            return getAttribute(EnumAttributeName.DESCRIPTION.getName(), defValue);
        }

        public boolean isSelected() {
            return getAttributeBoolean(EnumAttributeName.SELECTED);
        }

        public boolean isLabelValue() {
            return getAttributeBoolean(EnumAttributeName.LABEL_VALUE);
        }
        
        
        public String getAttributeDescription() {
            return getAttribute(EnumAttributeName.DESCRIPTION);
        }
        
        public String getRestrictions() {
            return getAttribute(EnumAttributeName.RESTRICTIONS);
        }
	
}
