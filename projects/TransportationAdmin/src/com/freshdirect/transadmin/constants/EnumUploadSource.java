package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumUploadSource  extends Enum {
	
	public static final EnumUploadSource SCHEDULE = new EnumUploadSource("SCH","SCHEDULE");

    public static final EnumUploadSource SCRIB = new EnumUploadSource("SCR","SCRIB");
       
    private final String description;

	public EnumUploadSource(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumUploadSource getEnum(String name) {
		return (EnumUploadSource) getEnum(EnumUploadSource.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumUploadSource.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumUploadSource.class);
	}

	public static Iterator iterator() {
		return iterator(EnumUploadSource.class);
	}

	public String toString() {
		return this.getName();
	}
}
