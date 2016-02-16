package com.freshdirect.customer;

import java.util.HashMap;
import java.util.Map;

public class EnumNotificationType implements java.io.Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2704798364134137333L;
	
	private final static Map<String,EnumNotificationType> NOTIFICATION_MAP = new HashMap<String,EnumNotificationType>();
	public final static EnumNotificationType	AVALARA	 = new EnumNotificationType("AVAL", "Avalara");
	
	private final String name;
	private  final String code;
	
	private EnumNotificationType(String code, String name){
		this.code = code;
		this.name = name;
		NOTIFICATION_MAP.put(code, this);
	}
	
	public static EnumNotificationType getNotificationType(String code){
		return NOTIFICATION_MAP.get(code);
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EnumNotificationType) {
			return this.name == ((EnumNotificationType)o).name;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 53;
		int result = 1;
		result = prime * result
				+ ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
}
