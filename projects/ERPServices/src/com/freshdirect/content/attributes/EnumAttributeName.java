/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Type-safe enumeration for attribute names.
 *
 * @version $Revision$
 * @author $Author$
 */
public enum EnumAttributeName {

	DESCRIPTION               ("description", EnumAttributeType.STRING),
	OPTIONAL                  ("optional", EnumAttributeType.BOOLEAN),
	PRIORITY                  ("priority", EnumAttributeType.INTEGER),
    DISPLAY_FORMAT            ("display_format", EnumAttributeType.STRING),
	LABEL_VALUE               ("label_value", EnumAttributeType.BOOLEAN),
	DEFAULT                   ("default", EnumAttributeType.BOOLEAN),
    UNDER_LABEL               ("under_label", EnumAttributeType.STRING),
    SELECTED                  ("selected", EnumAttributeType.BOOLEAN),
    PRODUCT_CODE              ("product_code", EnumAttributeType.STRING),
    DISPLAY_GROUP             ("display_group", EnumAttributeType.STRING),
    TAXABLE                   ("taxable", EnumAttributeType.BOOLEAN),
    CUST_PROMO                ("cust_promo", EnumAttributeType.BOOLEAN),
    SKUCODE                   ("skucode", EnumAttributeType.STRING),
    LABEL_NAME                ("label_name", EnumAttributeType.STRING),
    PRICING_UNIT_DESCRIPTION  ("pricing_unit_description", EnumAttributeType.STRING),
    DEPOSIT_AMOUNT            ("deposit_amount", EnumAttributeType.INTEGER),
    KOSHER_PRODUCTION         ("kosher_production", EnumAttributeType.BOOLEAN),
    RESTRICTIONS              ("restrictions", EnumAttributeType.STRING),
    SPECIALPRODUCT            ("specialproduct", EnumAttributeType.STRING),
    ADVANCE_ORDER_FLAG        ("advance_order_flag",EnumAttributeType.BOOLEAN),
    NEW_PRODUCT_DATE          ("new_prod_date", EnumAttributeType.STRING),
    BACK_IN_STOCK_DATE        ("back_in_stock", EnumAttributeType.STRING),
    RESET_TO_DEFAULT          ("reset_to_default", EnumAttributeType.STRING);

    private final String name;
    private final EnumAttributeType type;

    
    
    private EnumAttributeName(String name, EnumAttributeType type) {
    	if (name == null)
    		throw new IllegalArgumentException();
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public EnumAttributeType getType() {
        return this.type;
    }

    public String toString() {
        return this.name;
    }

    
    private final static Map<String, EnumAttributeName> byName = new HashMap<String, EnumAttributeName>();
    static {
        for (EnumAttributeName value : EnumAttributeName.values()) {
            byName.put(value.getName(), value);
        }
    }

    public static EnumAttributeName getByName(String name) {
        return byName.get(name);
    }
}
