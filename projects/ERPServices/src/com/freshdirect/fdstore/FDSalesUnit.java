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
 * Sales unit class - equivalent of ErpSalesUnit.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDSalesUnit extends FDAttributeProxy {

	/** Name (alternative unit of measure) */
	private String name;

	/** Sales unit description */
	private String description;

	/**
	 * Constructor with all properties.
	 *
	 * @param attributes Attributes
	 * @param name Name (alternative unit of measure)
	 * @param description Sales unit description
	 */
	public FDSalesUnit(AttributesI attributes, String name, String description) {
		super(attributes);
		this.name = name;
		this.description = description;
	}

	/**
	 * Get name (alternative unit of measure).
	 * This is the machine readable / SAP name of the sales unit.
	 *
	 * @return SAP name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get sales unit description (human readable).
	 * This is from the batch feeds, but can be overriden by the DESCRIPTION attribute.
	 *
	 * @return description
	 */
	public String getDescription() {
		return this.getAttribute(EnumAttributeName.DESCRIPTION.getName(), this.description);
	}

	public boolean equals(Object o) {
		if (o instanceof FDSalesUnit) {
			return this.name.equals(((FDSalesUnit) o).name);
		}
		return false;
	}

	public String getDescriptionUnit() {
		String salesUnitDescr = this.getDescription();
		int ppos = salesUnitDescr.indexOf("(");
		if (ppos > -1) {
			salesUnitDescr = salesUnitDescr.substring(0, ppos).trim();
		}

		char[] chars = salesUnitDescr.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (Character.isDigit(c) || c == '.') {
				continue;
			}
			return salesUnitDescr.substring(i, salesUnitDescr.length()).trim();
		}
		return "";
	}

	public String getDescriptionQuantity() {
		StringBuffer qty = new StringBuffer();
		String salesUnitDescr = this.getDescription();
		int ppos = salesUnitDescr.indexOf("(");
		if (ppos > -1) {
			salesUnitDescr = salesUnitDescr.substring(0, ppos).trim();
		}

		char[] chars = salesUnitDescr.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (Character.isDigit(c) || c == '.') {
				qty.append(c);
			} else {
				break;
			}
		}
		return qty.toString();
	}

	public String toString() {
		return "FDSalesUnit[" + this.name + "," + this.description + "," + this.getDescription() + "]";
	}
}
