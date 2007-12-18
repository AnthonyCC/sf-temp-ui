/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.attributes;

import java.io.Serializable;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class Attribute implements Serializable {
    
	private final EnumAttributeType type;
	private final String key;
    private Object value;
    private final boolean inheritable;

    public Attribute(EnumAttributeType type, String key, Object value) {
		this(type, key, value, false);
    }

    public Attribute(EnumAttributeType type, String key, Object value, boolean inheritable) {
		this( type, key, inheritable );
        this.value = value;
    }
	
	public Attribute(EnumAttributeType type, String key, boolean inheritable) {
		this.type = type;
		this.key = key;
        this.inheritable = inheritable;
	}

	public String getKey() {
		return this.key;
    }

	public Object getValue() {
		return this.value;
    }

	public boolean isInheritable() {
        return this.inheritable;
    }
    
    public EnumAttributeType getType() {
        return this.type;
    }
    
	public void setValue(Object value){
		this.value = value;
	}
    
    public String toString() {
        return "Attribute[" + this.getKey() + " - " + this.getType().getName() + " - " + this.getValue() + "]";
    }
    
    public Attribute copy(){
    	return new Attribute(this.getType(),this.getKey(),this.getValue(),this.inheritable);
    }

}
