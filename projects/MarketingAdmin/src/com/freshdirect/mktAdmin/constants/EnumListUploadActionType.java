package com.freshdirect.mktAdmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumListUploadActionType extends Enum {

	public static final EnumListUploadActionType ADD = new EnumListUploadActionType("ADD", "Add to List");
	public static final EnumListUploadActionType DELETE = new EnumListUploadActionType("DELETE", "Delete from List");	
	public static final EnumListUploadActionType REPLACE = new EnumListUploadActionType("REPLACE", "Replace List");
	public static final EnumListUploadActionType CREATE = new EnumListUploadActionType("CREATE", "Create List");

	private final String description;
	
	public EnumListUploadActionType(String name, String description) {
		super(name);
		this.description = description;
	}
	
	public static EnumListUploadActionType getEnum(String name) {
		return (EnumListUploadActionType) getEnum(EnumListUploadActionType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumListUploadActionType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumListUploadActionType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumListUploadActionType.class);
	}

	public String toString() {
		return this.getName();
	}
	
	public String getDescription(){
		return this.description;
	}
    		
}

