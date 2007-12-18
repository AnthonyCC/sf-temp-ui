/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.nutrition; 

/**
 * Type-safe enumeration for product information types.
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpNutritionInfoType implements java.io.Serializable {

	public final static ErpNutritionInfoType INGREDIENTS        = new ErpNutritionInfoType("INGR", "Ingredients");
    public final static ErpNutritionInfoType HIDDEN_INGREDIENTS = new ErpNutritionInfoType("HNGR", "Hidden Ingredients");
	public final static ErpNutritionInfoType HEATING            = new ErpNutritionInfoType("HEAT", "Heating Instructions");
    public final static ErpNutritionInfoType KOSHER_SYMBOL      = new ErpNutritionInfoType("KSYM", "Kosher symbol");
    public final static ErpNutritionInfoType KOSHER_TYPE        = new ErpNutritionInfoType("KTYP", "Kosher type");
    public final static ErpNutritionInfoType CLAIM              = new ErpNutritionInfoType("CLAM", "Claim", true);
    public final static ErpNutritionInfoType ALLERGEN           = new ErpNutritionInfoType("ALRG", "Allergen", true);
    public final static ErpNutritionInfoType ORGANIC            = new ErpNutritionInfoType("ORGN", "Organic Statement", true);

	protected final String code;
	private final String name;
    private final boolean multiValued;
    
    public static ErpNutritionInfoType getInfoType(String code) {
        if (INGREDIENTS.getCode().equalsIgnoreCase(code))
            return INGREDIENTS;
        else if (HIDDEN_INGREDIENTS.getCode().equalsIgnoreCase(code))
            return HIDDEN_INGREDIENTS;
        else if (HEATING.getCode().equalsIgnoreCase(code))
            return HEATING;
        else if (KOSHER_SYMBOL.getCode().equalsIgnoreCase(code))
            return KOSHER_SYMBOL;
        else if (KOSHER_TYPE.getCode().equalsIgnoreCase(code))
            return KOSHER_TYPE;
        else if (CLAIM.getCode().equalsIgnoreCase(code))
            return CLAIM;
        else if (ALLERGEN.getCode().equalsIgnoreCase(code))
            return ALLERGEN;
        else if (ORGANIC.getCode().equalsIgnoreCase(code))
            return ORGANIC;
        else
            return null;
    }

	private ErpNutritionInfoType(String code, String name) {
		this.code = code;
		this.name = name;
        this.multiValued = false;
	}
    
    private ErpNutritionInfoType(String code, String name, boolean multiValued) {
		this.code = code;
		this.name = name;
        this.multiValued = multiValued;
	}

	public String getCode() {
		return this.code;
	}
    
    public String getName() {
        return this.name;
    }
	
	public String toString() {
		return this.name;
	}
    
    public boolean isMultiValued() {
        return this.multiValued;
    }
	
	public int hashCode() {
		return this.code.hashCode();	
	}

	public boolean equals(Object o) {
		if (o instanceof ErpNutritionInfoType) {
			return this.code.equalsIgnoreCase(((ErpNutritionInfoType)o).code);
		}
		return false;
	}

}