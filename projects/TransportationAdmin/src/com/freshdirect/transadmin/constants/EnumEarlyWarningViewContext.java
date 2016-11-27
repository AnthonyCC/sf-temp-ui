package com.freshdirect.transadmin.constants;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumEarlyWarningViewContext extends Enum implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static EnumEarlyWarningViewContext	DISPLAY	= new EnumEarlyWarningViewContext( "DISPLAY", "Display" );
	public final static EnumEarlyWarningViewContext	ORDER	= new EnumEarlyWarningViewContext( "ORDER", "Order" );
	public final static EnumEarlyWarningViewContext	TIME	= new EnumEarlyWarningViewContext( "TIME", "Time" );
	
	private final String description;
	public EnumEarlyWarningViewContext(String name, String description) {
		super(name);
		this.description = description;
	}
	
	public static EnumEarlyWarningViewContext getEnum(String name) {
		return (EnumEarlyWarningViewContext) getEnum(EnumEarlyWarningViewContext.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumEarlyWarningViewContext.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumEarlyWarningViewContext.class);
	}

	public static Iterator iterator() {
		return iterator(EnumEarlyWarningViewContext.class);
	}

	public String getDescription() {
		return description;
	}	

}
