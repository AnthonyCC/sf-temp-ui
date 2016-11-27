package com.freshdirect.routing.constants;

public enum EnumRoutingUpdateStatus {
	FAILED, PENDING, OVERRIDDEN, SUCCESS;
	
	public String value(){
	    switch(this) {
	     case FAILED: return "FLD";
	     case PENDING: return "PEN";
	     case OVERRIDDEN: return "OVD";
	     case SUCCESS: return "SUS";	     
	     default: return "";
	   }
	}
	
	public static EnumRoutingUpdateStatus getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(FAILED.value())) {
			return FAILED;
		} else if(value.equals(PENDING.value())) {
			return PENDING;
		} else if(value.equals(OVERRIDDEN.value())) {
			return OVERRIDDEN;
		} else if(value.equals(SUCCESS.value())) {
			return SUCCESS;
		} else throw new RuntimeException("EnumRoutingUpdateStatus: undefined enum :"+value);
	}
}

