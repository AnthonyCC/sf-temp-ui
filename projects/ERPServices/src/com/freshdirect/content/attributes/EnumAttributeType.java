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
public enum EnumAttributeType {
	STRING(0, "S") {
		@Override
		public Object parseValue(String value) {
			return value;
		}
	},
	BOOLEAN(1, "B") {
		@Override
		public Object parseValue(String value) {
			return Boolean.valueOf(value);
		}
	},
	INTEGER(2, "I") {
		@Override
		public Object parseValue(String value) {
			return Integer.valueOf(value);
		}
	};

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

	public abstract Object parseValue(String value);

	public static EnumAttributeType getByName(String name) {
		for (EnumAttributeType type : EnumAttributeType.values())
			if (type.getName().equals(name))
				return type;

		return null;
	}
}
