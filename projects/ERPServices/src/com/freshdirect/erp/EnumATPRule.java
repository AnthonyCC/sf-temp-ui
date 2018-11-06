/*
 * $Workfile: EnumATPRule.java$
 *
 * $Date: 8/31/2001 10:35:17 AM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.enums.ValuedEnum;

/**
 * Type-safe enumeration for ATP check rules.
 *
 * @version $Revision: 2$
 * @author $Author: Mike Rose$
 */
public class EnumATPRule extends ValuedEnum {

	public final static EnumATPRule MATERIAL = new EnumATPRule("MATERIAL", 0, "Material Availability");
	public final static EnumATPRule SIMULATE = new EnumATPRule("SIMULATE", 1, "Sales Order Simulate");
	public final static EnumATPRule JIT = new EnumATPRule("JIT", 2, "Just-in-Time");
	public final static EnumATPRule COMPONENT = new EnumATPRule("COMPONENT", 3, "Component Simulate");
	public final static EnumATPRule MULTILEVEL_MATERIAL = new EnumATPRule("MULTILEVEL_MATERIAL", 4, "Multi-level Material Availability");
	public final static EnumATPRule SINGLELEVEL_MATERIAL = new EnumATPRule("SINGLELEVEL_MATERIAL", 5, "Single-level Material Availability");

	private final String displayName;

	private EnumATPRule(String name, int value, String displayName) {
		super(name, value);
		this.displayName = displayName;
	}

	public static EnumATPRule getEnum(String name) {
		return (EnumATPRule) getEnum(EnumATPRule.class, name);
	}

	public static EnumATPRule getEnum(int value) {
		return (EnumATPRule) getEnum(EnumATPRule.class, value);
	}

	public static List getEnumList() {
		return getEnumList(EnumATPRule.class);
	}

	public static Iterator iterator() {
		return iterator(EnumATPRule.class);
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String toString() {
		return this.displayName;
	}
	
	public static void main(String[] a) {
		System.out.println(EnumATPRule.getEnum("Material Availability"));
	}

}