/*
 * $Workfile: ErpModelSupport.java$
 *
 * $Date: 9/4/2001 5:47:45 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp;

import com.freshdirect.content.attributes.AttributesI;
import com.freshdirect.content.attributes.AttributeCollection;
import com.freshdirect.content.attributes.EnumAttributeName;

import com.freshdirect.framework.core.ModelSupport;

/**
 * Base class for ERP Model objects.
 *
 * @version $Revision: 7$
 * @author $Author: Viktor Szathmary$
 */
public abstract class ErpModelSupport extends ModelSupport implements AttributesI {
	
	/** Map to store optional attributes in */
	private AttributeCollection attributes = null;

	/**
	 * Set the optional attribute Map.
	 */
	public void setAttributes(AttributeCollection attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * Get the optional attribute Map.
	 *
	 * @return null if attributes have not been set
	 */
	public AttributeCollection getAttributes() {
		return this.attributes;
	}
    
    /**
	 * Check for the presence of an attribute.
	 *
	 * @param name name of the attribute
	 * @return true if the attribute was found
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
	 */
	public boolean hasAttribute(String name) {
        if (attributes == null)
            throw new IllegalStateException("Attributes for this ERP object are not available.");
        else
            return attributes.hasAttribute(name);
    }

	/**
	 * Get attribute as a String.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return String attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
	 */
	public String getAttribute(String name, String defaultValue) {
        if (attributes == null)
            throw new IllegalStateException("Attributes for this ERP object are not available.");
        else
            return attributes.getAttribute(name, defaultValue);
    }

	/**
	 * Get attribute as a String.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return String attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public String getAttribute(EnumAttributeName attributeName) {
        if (attributes == null)
            throw new IllegalStateException("Attributes for this ERP object are not available.");
        else
            return attributes.getAttribute(attributeName);
	}

	/**
	 * Get attribute as a boolean.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return boolean attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
	 */
	public boolean getAttributeBoolean(String name, boolean defaultValue) {
        if (attributes == null)
            throw new IllegalStateException("Attributes for this ERP object are not available.");
        else
            return attributes.getAttributeBoolean(name, defaultValue);
    }

	/**
	 * Get attribute as a boolean.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return boolean attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public boolean getAttributeBoolean(EnumAttributeName attributeName) {
        if (attributes == null)
            throw new IllegalStateException("Attributes for this ERP object are not available.");
        else
            return attributes.getAttributeBoolean(attributeName);
	}

	/**
	 * Get attribute as an integer.
	 *
	 * @param name name of the attribute
	 * @param defaultValue default value to return when attribute is not found or is of different type
	 * @return int attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
	 */
	public int getAttributeInt(String name, int defaultValue) {
        if (attributes == null)
            throw new IllegalStateException("Attributes for this ERP object are not available.");
        else
            return attributes.getAttributeInt(name, defaultValue);
    }

	/**
	 * Get attribute as a integer.
	 *
	 * @param attributeName EnumAttributeName instance, containing the name and the default value
	 * @return int attribute
     * @throws IllegalStateException if the attributes for the object haven't been loaded yet
     * @throws ClassCastException if the specified attributeName is of different type
	 */
	public int getAttributeInt(EnumAttributeName attributeName) {
        if (attributes == null)
            throw new IllegalStateException("Attributes for this ERP object are not available.");
        else
            return attributes.getAttributeInt(attributeName);
	}

	/**
	 * Accept an ErpVisitor. Calls the template method visitChildren().
	 */
	public final void accept(ErpVisitorI visitor) {
		visitor.pushModel(this);
		this.visitChildren(visitor);
		visitor.popModel();
	}
	
	/**
	 * Template method to visit the children of this ErpModel.
	 * It should call accept(visitor) on these (or do nothing).
	 *
	 * @param visitor visitor instance to pass around
	 */
	public abstract void visitChildren(ErpVisitorI visitor);

}