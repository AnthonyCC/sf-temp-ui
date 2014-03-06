package com.freshdirect.dashboard.exception;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ErrorCodeDispatchVolumeEnum implements ErrorCodeEnum {
	
	
	UNKNOWN_ERROR(1, "UNKNOWN_ERROR", "dispatchvolume.error.unknown"),
	INVALID_PARAMS(2, "INVALID_PARAMS", "dispatchvolume.error.params.invalid"),
	NOT_FOUND(3, "NOT_FOUND", "dispatchvolume.error.entity.notfound"),
	EMPTY_LIST(4, "EMPTY_LIST", "dispatchvolume.error.entity.list"),
	
	//FIXME: add more here
	
	;
	
	// lookup table to be used to find enum for conversion
	private static final Map<Integer,ErrorCodeDispatchVolumeEnum> lookup = new HashMap<Integer,ErrorCodeDispatchVolumeEnum>();
	static {
		for(ErrorCodeDispatchVolumeEnum e : EnumSet.allOf(ErrorCodeDispatchVolumeEnum.class))
			lookup.put(e.getErrorCode(), e);
	}
	
	private static ServiceEnum serviceEnum = ServiceEnum.DISPATCHVOLUME_SERVICE;
	private int errorCode;
	private String name;
	private String i18nKey;
	
	ErrorCodeDispatchVolumeEnum(int errorCode, String name, String i18nKey) {
		this.errorCode = errorCode;
		this.name = name;
		this.i18nKey = i18nKey;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}
	
	public String getName() {
		return this.name;
	}

	public int getServiceId() {
		return serviceEnum.getServiceId();
	}

	public String getMessageKey() {
		return i18nKey;
	}

	public String getDefaultMessage() {
		switch (this){
		case UNKNOWN_ERROR:
			return "An unknown error has been encountered";
		case INVALID_PARAMS:
			return "Invalid parameters were received";
		case NOT_FOUND:
			return "Requested entity was not found";
		case EMPTY_LIST:
			return "No dispatch volume records found for requested dates";
		
		//FIXME: add more here and can use resource bundle with i18nKey if desired 
		
		default: 
			return "An undefined error has been encountered";
		}
	}
	
	public static ErrorCodeDispatchVolumeEnum get(int errorCode) { 
		return lookup.get(errorCode); 
	}


}
