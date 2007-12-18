/*
 * $Workfile: EnumAttributeType.java$
 *
 * $Date: 8/30/2001 8:38:09 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes;

/**
 * Type-safe enumeration for attributes' data types.
 *
 * @version $Revision: 3$
 * @author $Author: Mike Rose$
 */
public class EnumAttributeType {

	public final static EnumAttributeType STRING = new EnumAttributeType(0, "S");
	public final static EnumAttributeType BOOLEAN = new EnumAttributeType(1, "B");
	public final static EnumAttributeType INTEGER = new EnumAttributeType(2, "I");
	
	protected final int id;
	private final String name;

	private EnumAttributeType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return this.name;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumAttributeType) {
			return this.id == ((EnumAttributeType)o).id;
		}
		return false;
	}

}
