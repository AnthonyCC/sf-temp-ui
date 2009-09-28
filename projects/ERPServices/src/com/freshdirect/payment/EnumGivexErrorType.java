package com.freshdirect.payment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;



public  class EnumGivexErrorType extends Enum{
	
	public static final EnumGivexErrorType ERROR_TIME_OUT = new EnumGivexErrorType("-1",  -1, "Read timed out");
	public static final EnumGivexErrorType ERROR_FAIL_OVER = new EnumGivexErrorType("-2",  -2, "Connection refused");
	public static final EnumGivexErrorType ERROR_GENERIC = new EnumGivexErrorType("-3", -3, "Generic Error");
	public static final EnumGivexErrorType ERROR_CERT_NOT_EXIST = new EnumGivexErrorType("2", 2, "Cert not exist");
	public static final EnumGivexErrorType ERROR_BALANCE = new EnumGivexErrorType("9", 9, "ERR  bal");
	public static final EnumGivexErrorType ERROR_CERT_ON_HOLD = new EnumGivexErrorType("27", 27, "Cert on hold");
	public static final EnumGivexErrorType ERROR_CERT_CANCELLED = new EnumGivexErrorType("28", 28, "Certificate cancelled");
	public static final EnumGivexErrorType ERROR_INVALID_PRE_AUTH = new EnumGivexErrorType("48", 48, "Post-Auth references invalid Pre-Auth");
	
	
	private int code; 	
	private String description;
	
	public EnumGivexErrorType(String name, int code, String description) {
		super(name);
		this.code = code;
		this.description = description;
	}

	public static EnumGivexErrorType getEnum(String name) {
		return (EnumGivexErrorType) getEnum(EnumGivexErrorType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumGivexErrorType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumGivexErrorType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumGivexErrorType.class);
	}

	public static EnumGivexErrorType getErrorTypeFromMessage(String message){
		if(message!=null && message.trim().length()>0){
			Iterator iter = iterator();
			while(iter.hasNext()){
				EnumGivexErrorType errorType = (EnumGivexErrorType) iter.next();
				if(message.indexOf(errorType.getDescription())!=-1){
					return errorType;
				}
			}
			return EnumGivexErrorType.ERROR_GENERIC;
		}	
		return null;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public int getErrorCode() {
		return this.code;
	}
	
	public String toString() {
		return this.getName();
	}
	
	public String getInfo() {
		return this.getName()+" - "+this.getDescription();
	}

}
