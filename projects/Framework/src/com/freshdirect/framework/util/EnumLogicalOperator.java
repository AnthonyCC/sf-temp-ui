/*
 * Created on Jun 30, 2005
 *
 */
package com.freshdirect.framework.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

/**
 * @author Sivachandar
 *
 */
public class EnumLogicalOperator extends Enum {		
	
	public static final EnumLogicalOperator LESS_THAN_OR_EQUAL = new EnumLogicalOperator("<=","LESS THAN OR EQUAL");

    public static final EnumLogicalOperator GREATER_THAN_OR_EQUAL = new EnumLogicalOperator(">=","GREATER THAN OR EQUAL");
    
    public static final EnumLogicalOperator LESS_THAN = new EnumLogicalOperator("<","LESS THAN");

    public static final EnumLogicalOperator GREATER_THAN  = new EnumLogicalOperator(">","GREATER THAN");

    public static final EnumLogicalOperator BETWEEN = new EnumLogicalOperator("<>","BETWEEN");

    public static final EnumLogicalOperator NOT_EQUAL = new EnumLogicalOperator("!=","NOT EQUAL");;
    
	private final String description;

	public EnumLogicalOperator(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumLogicalOperator getEnum(String name) {
		return (EnumLogicalOperator) getEnum(EnumLogicalOperator.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumLogicalOperator.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumLogicalOperator.class);
	}

	public static Iterator iterator() {
		return iterator(EnumLogicalOperator.class);
	}

	public String toString() {
		return this.getName();
	}

}
