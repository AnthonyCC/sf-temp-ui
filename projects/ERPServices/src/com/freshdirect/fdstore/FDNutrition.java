/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

/**
 * Nutrition object.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDNutrition implements java.io.Serializable {

	/** name */
	private String name;

	/** value */
	private double value;
    
    /** uom */
    private String uom;
    
    public FDNutrition() {
        super();
    }

	/**
	 * Constructor with all properties.
	 *
	 * @param attributes Attributes
	 * @param name Name
	 * @param value Value
     * @param unitOfMeasure Unit Of Mesaure
	 */
	public FDNutrition(String name, double value, String uom) {
        this();
		this.name = name != null ? name.intern() : null;
		this.value = value;
        this.uom = uom != null ? uom.intern() : null;
	}

	/**
	 * Get name
	 *
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get value
	 *
	 * @return value
	 */
	public double getValue() {
		return this.value;
	}
    
    /**
     * Get unit of measure
     *
     * @return unit of measure
     */
    public String getUnitOfMeasure() {
        return this.uom;
    }
	
	public boolean equals(Object o) {
		if (o instanceof FDNutrition) {
			return this.name.equals( ((FDNutrition)o).name );
		}	
		return false;
	}

	public String toString() {
		return "FDNutrition["+this.name+" "+this.value+" "+this.uom+"]";
	}
}
