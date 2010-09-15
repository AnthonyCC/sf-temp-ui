/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.model.ErpSalesUnitModel;

/**
 * Sales unit class - equivalent of ErpSalesUnit.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDSalesUnit extends FDAttributesProxy {
	private static final long serialVersionUID = 6006350893549341570L;

	/** Name (alternative unit of measure) */
	private String name;

	/** Sales unit description */
	private String description;
	
	private int numerator;
	
	private int denominator;
	
	private String baseUnit;
	
	public FDSalesUnit(ErpSalesUnitModel model) {
		super(model);
		name = model.getAlternativeUnit();
		description = model.getDescription();
		numerator = model.getNumerator();
		denominator = model.getDenominator();
		baseUnit = model.getBaseUnit();
	}
	
	/**
	 * Just for testing !
	 * @param name
	 * @param description
	 */
	public FDSalesUnit(String name, String description) {
            this(name, description, 0, 0, null);
	}
	
	
        /**
         * Just for testing !
         * @param name
         * @param description
         */
	public FDSalesUnit(String name, String description, int numerator, int denominator, String baseUnit) {
            super();
            this.name = name;
            this.description = description;
            this.numerator = numerator;
            this.denominator = denominator;
            this.baseUnit = baseUnit;
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
		return getAttributes().getDescription(description);
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

	public int getNumerator() {
		return numerator;
	}

	public int getDenominator() {
		return denominator;
	}

	public String getBaseUnit() {
		return baseUnit;
	}
}
