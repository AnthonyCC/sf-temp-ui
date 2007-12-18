/*
 * $Workfile:EnumAttributeType.java$
 *
 * $Date:12/12/02 5:58:59 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.fdstore.attributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.fdstore.content.ContentNodeI;

public class EnumAttributeType implements Serializable {

    public final static EnumAttributeType GENERIC_NODE = new EnumAttributeType("Generic Node","G");

    public final static EnumAttributeType STRING = new EnumAttributeType("String", "S");
    public final static EnumAttributeType INTEGER = new EnumAttributeType("Integer", "I");
    public final static EnumAttributeType DOUBLE = new EnumAttributeType("Double", "D");
    public final static EnumAttributeType BOOLEAN = new EnumAttributeType("Boolean", "B");
    public final static EnumAttributeType MEDIA = new EnumAttributeType("Media", "M");
    public final static EnumAttributeType DEPARTMENTREF = new EnumAttributeType("Department Node Ref", "RD");
    public final static EnumAttributeType CATEGORYREF = new EnumAttributeType("Category Node Ref", "RC");
    public final static EnumAttributeType PRODUCTREF = new EnumAttributeType("Product Node", "RP");
    public final static EnumAttributeType RECIPEREF = new EnumAttributeType("Recipe Node", ContentNodeI.TYPE_RECIPE);
    public final static EnumAttributeType SKUREF = new EnumAttributeType("Sku Node Ref", "RS");
    public final static EnumAttributeType BRANDREF = new EnumAttributeType("Brand Node Ref", "RB");
    public final static EnumAttributeType TITLEDMEDIA = new EnumAttributeType("Titled Media","TM");
    public final static EnumAttributeType ARTICLEMEDIA = new EnumAttributeType("Article Media","AM");
    public final static EnumAttributeType DOMAINREF = new EnumAttributeType("Domain Ref","ZR");
    public final static EnumAttributeType DOMAINVALUEREF = new EnumAttributeType("DomainValue Ref","VR");
    //public final static EnumAttributeType DOMAIN = new EnumAttributeType("Domain", "Z");
    //public final static EnumAttributeType DOMAINVALUE = new EnumAttributeType("Domain Value", "V");
    //public final static EnumAttributeType BANNER = new EnumAttributeType("Banner", "R");

    private static List types = null;

    static {
        ArrayList t = new ArrayList();
        t.add(STRING);
        t.add(INTEGER);
        t.add(DOUBLE);
        t.add(BOOLEAN);
        t.add(MEDIA);
        t.add(DEPARTMENTREF);
        t.add(CATEGORYREF);
        t.add(PRODUCTREF);
        t.add(SKUREF);
        t.add(BRANDREF);
        t.add(TITLEDMEDIA);
        t.add(ARTICLEMEDIA);
        t.add(DOMAINREF);
        t.add(DOMAINVALUEREF);
       // t.add(DOMAIN);
       //t.add(DOMAINVALUE);
       //t.add(BANNER);
        types = Collections.unmodifiableList(t);
    }

    public static List getAttributeTypes() {
        return types;
    }

    public static EnumAttributeType getAttributeType(String lid) {
		for (int i=0;i < types.size();i++) {
            EnumAttributeType ls = (EnumAttributeType) types.get(i);
            if (ls.getId().equals(lid))
                return ls;
        }
        return null;
    }

     public boolean equals(Object o) {
        if (o instanceof EnumAttributeType) {
			if (((EnumAttributeType)o).getId().equals(this.id))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

	private EnumAttributeType(String n, String i) {
        this.name = n;
        this.id = i;
    }

    public String getName(){
            return name;
        }

    public String getId(){
            return id;
        }

    private String name;
    private String id;
}
