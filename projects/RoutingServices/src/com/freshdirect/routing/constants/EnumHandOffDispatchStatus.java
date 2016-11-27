package com.freshdirect.routing.constants;

public enum EnumHandOffDispatchStatus {
	PENDING, COMPLETE;
	
	public String value(){
	    switch(this) {
	     case PENDING: return "PEN";
	     case COMPLETE: return "CPD";	     
	     default: return "";
	   }
	}
	
	public static EnumHandOffDispatchStatus getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(PENDING.value())) {
			return PENDING;
		} else if(value.equals(COMPLETE.value())) {
			return COMPLETE;
		} else throw new RuntimeException("EnumHandOffDispatchStatus: undefined enum :"+value);
	}
}

