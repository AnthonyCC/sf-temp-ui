/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.fdstore.attributes.EnumAttributeType;

public class EnumDomainType implements Serializable {
    
    public final static EnumDomainType VARIATION = new EnumDomainType(EnumAttributeType.STRING, "Variation", "V");
    public final static EnumDomainType RATING = new EnumDomainType(EnumAttributeType.INTEGER, "Rating", "R");
    public final static EnumDomainType USAGE = new EnumDomainType(EnumAttributeType.BOOLEAN, "Usage", "U");
    public final static EnumDomainType BRAND = new EnumDomainType(EnumAttributeType.STRING, "Brand", "B");
    public final static EnumDomainType MEASURE = new EnumDomainType(EnumAttributeType.STRING, "Unit of Measure", "M");
    public final static EnumDomainType REGION = new EnumDomainType(EnumAttributeType.STRING, "Region", "P");
   // public final static EnumDomainType NOCAFFEINE = new EnumDomainType(EnumAttributeType.BOOLEAN, "No Caffeine", "N");

    private static List types = null;
    
    static {
        ArrayList t = new ArrayList();
        t.add(VARIATION);
        t.add(RATING);
        t.add(USAGE);
        t.add(BRAND);
        t.add(MEASURE);
        t.add(REGION);
        //t.add(NOCAFFEINE);
        types = Collections.unmodifiableList(t);
    }
    
    public static List getAttributeTypes() {
        return types;
    }
    
    public static EnumDomainType getDomainType(String tid) {
        for (int i=0;i < types.size();i++) {
            EnumDomainType dt = (EnumDomainType) types.get(i);
            if (dt.getId().equals(tid))
                return dt;
        }
        return null;
    }
    
    public boolean equals(Object o) {
        if (o instanceof EnumDomainType) {
            if (((EnumDomainType)o).getId().equals(this.id))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }
    
    private EnumDomainType(EnumAttributeType type, String n, String i) {
        this.attrType = type;
        this.name = n;
        this.id = i;
    }
    
    public EnumAttributeType getAttributeType() {
        return attrType;
    }
    
    public String getName(){
        return name;
    }
    
    public String getId(){
        return id;
    }
    
    private String name;
    private String id;
    private EnumAttributeType attrType;
}
