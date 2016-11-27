package com.freshdirect.sms;


public enum EnumSMSAlertStatus {
	
	NONE, PENDING, SUBSCRIBED;
	
	public String value(){
	    switch(this) {
	     case NONE: return "N";	     
	     case PENDING: return "P";
	     case SUBSCRIBED: return "S";
	     default: return "";
	   }
	}
	
	public static EnumSMSAlertStatus getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(NONE.value())) {
			return NONE;
		} else if(value.equals(PENDING.value())) {
			return PENDING;
		} else if(value.equals(SUBSCRIBED.value())) {
			return SUBSCRIBED;
		} else {
			throw new RuntimeException("EnumSmsAlertStatus: undefined enum :"+value);
		}
	}
}