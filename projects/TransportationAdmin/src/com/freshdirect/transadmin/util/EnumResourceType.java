package com.freshdirect.transadmin.util;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public final class EnumResourceType extends Enum {
    public static final EnumResourceType DRIVER = new EnumResourceType("001","Driver");
    public static final EnumResourceType HELPER = new EnumResourceType("002","Helper");
    public static final EnumResourceType RUNNER = new EnumResourceType("003","Runner");
    public static final EnumResourceType SUPERVISOR = new EnumResourceType("006","Supervisor");
    
    private final String desc;
    private EnumResourceType(String id,String desc) {
        super(id);
        this.desc=desc;
    }

    public static EnumResourceType getEnum(String id) {
        return (EnumResourceType) getEnum(EnumResourceType.class, id);
    }

    public static Map getEnumMap() {
        return getEnumMap(EnumResourceType.class);
    }

    public static List getEnumList() {
        return getEnumList(EnumResourceType.class);
    }

    public static Iterator iterator() {
        return iterator(EnumResourceType.class);
    }
    public String toString() {
		return this.getName();
	}
    
	public String getDesc() {
		return this.desc;
	}
}

