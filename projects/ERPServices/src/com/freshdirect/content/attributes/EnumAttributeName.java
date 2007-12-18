/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes;

/**
 * Type-safe enumeration for attribute names.
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumAttributeName {

	public final static EnumAttributeName DESCRIPTION               = new EnumAttributeName("description", EnumAttributeType.STRING, "");
	public final static EnumAttributeName OPTIONAL                  = new EnumAttributeName("optional", EnumAttributeType.BOOLEAN, new Boolean(false));
	public final static EnumAttributeName PRIORITY                  = new EnumAttributeName("priority", EnumAttributeType.INTEGER, new Integer(0));
    public final static EnumAttributeName DISPLAY_FORMAT            = new EnumAttributeName("display_format", EnumAttributeType.STRING, "dropdown");
	public final static EnumAttributeName LABEL_VALUE               = new EnumAttributeName("label_value", EnumAttributeType.BOOLEAN, new Boolean(false));
	public final static EnumAttributeName DEFAULT                   = new EnumAttributeName("default", EnumAttributeType.BOOLEAN, new Boolean(false));
    public final static EnumAttributeName UNDER_LABEL               = new EnumAttributeName("under_label", EnumAttributeType.STRING, "");
    public final static EnumAttributeName SELECTED                  = new EnumAttributeName("selected", EnumAttributeType.BOOLEAN, new Boolean(false));
    public final static EnumAttributeName PRODUCT_CODE              = new EnumAttributeName("product_code", EnumAttributeType.STRING, "");
    public final static EnumAttributeName DISPLAY_GROUP             = new EnumAttributeName("display_group", EnumAttributeType.STRING, "");
    public final static EnumAttributeName TAXABLE                   = new EnumAttributeName("taxable", EnumAttributeType.BOOLEAN, new Boolean(false));
    public final static EnumAttributeName CUST_PROMO                = new EnumAttributeName("cust_promo", EnumAttributeType.BOOLEAN, new Boolean(false));
    public final static EnumAttributeName SKUCODE                   = new EnumAttributeName("skucode", EnumAttributeType.STRING, "");
    public final static EnumAttributeName LABEL_NAME                = new EnumAttributeName("label_name", EnumAttributeType.STRING, "");
    public final static EnumAttributeName PRICING_UNIT_DESCRIPTION  = new EnumAttributeName("pricing_unit_description", EnumAttributeType.STRING, "");
    public final static EnumAttributeName DEPOSIT_AMOUNT            = new EnumAttributeName("deposit_amount", EnumAttributeType.INTEGER, new Integer(0));
    public final static EnumAttributeName KOSHER_PRODUCTION         = new EnumAttributeName("kosher_production", EnumAttributeType.BOOLEAN, new Boolean(false));
    public final static EnumAttributeName RESTRICTIONS				= new EnumAttributeName("restrictions", EnumAttributeType.STRING, "");
    public final static EnumAttributeName SPECIALPRODUCT			= new EnumAttributeName("specialproduct", EnumAttributeType.STRING, "");
    public final static EnumAttributeName ADVANCE_ORDER_FLAG		= new EnumAttributeName("advance_order_flag",EnumAttributeType.BOOLEAN,new Boolean(false));

    private final String name;
    private final EnumAttributeType type;
    private final Object defaultValue;

	private EnumAttributeName(String name, EnumAttributeType type, Object defaultValue) {
		this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
	}

	public String getName() {
		return this.name;
	}
    
    public EnumAttributeType getType() {
        return this.type;
    }
    
    public Object getDefaultValue() {
    	return this.defaultValue;
    }
	
	public String toString() {
		return this.name;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumAttributeName) {
			return this.getName().equals(((EnumAttributeName)o).getName());
		}
		return false;
	}

}
