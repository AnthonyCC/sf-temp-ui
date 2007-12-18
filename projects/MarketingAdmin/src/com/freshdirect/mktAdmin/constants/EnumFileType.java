package com.freshdirect.mktAdmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumFileType  extends Enum{

//	 DEFAULT TYPES
	public static final EnumFileType CSV_FILE_TYPE = new EnumFileType("CSV", "CVS File Type");
	public static final EnumFileType EXCEL_FILE_TYPE = new EnumFileType("XLS", "Excel File Type");

	private final String description;

	public EnumFileType(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumFileType getEnum(String name) {
		return (EnumFileType) getEnum(EnumFileType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumFileType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumFileType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumFileType.class);
	}

	public String toString() {
		return this.getName();
	}
}