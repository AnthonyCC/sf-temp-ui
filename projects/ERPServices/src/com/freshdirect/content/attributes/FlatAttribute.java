/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes;

import java.io.Serializable;
import java.util.Date;

public class FlatAttribute implements Serializable {

	private final String[] idPath;

	private final String name;

	private final Object value;
	
	private Date lastModifiedDate;

	public FlatAttribute(String[] idPath, String name, Object value) {
		this.idPath = idPath;
		this.name = name != null ? name.intern() : null;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	public String[] getIdPath() {
		return this.idPath;
	}
	
	public EnumAttributeType getAttributeType() {
		if (value instanceof String) {
			return EnumAttributeType.STRING;
		} else if (value instanceof Boolean) {
			return EnumAttributeType.BOOLEAN;
		} else if (value instanceof Integer) {
			return EnumAttributeType.INTEGER;
		} else {
			throw new IllegalStateException("Invalid attribute type "+value.getClass().getName());
		}	
	}
	
	public void setLastModified(Date lastModifiedDate){
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public Date getLastModifiedDate(){
		return this.lastModifiedDate;
	}

	public String toString() {
		return "RawAttribute["+idPath+" "+name+"="+value+"]";
	}

	public boolean isSameIdPath(String[] otherPath) {
		if (idPath.length != otherPath.length) {
			return false;
		}
		for (int i=0; i<idPath.length; i++) {
			if (!idPath[i].equals(otherPath[i])) {
				return false;
			}
		}
		return true;
	}

}
