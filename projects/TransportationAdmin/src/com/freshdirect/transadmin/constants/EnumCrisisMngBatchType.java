package com.freshdirect.transadmin.constants;

public enum EnumCrisisMngBatchType {
	
	REGULARORDER, STANDINGORDER;
	
	public String value(){
	    switch(this) {
	     case REGULARORDER: return "ROB";
	     case STANDINGORDER: return "SOB";	     
	     default: return "";
	   }
	}
	
	public static EnumCrisisMngBatchType getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(REGULARORDER.value())) {
			return REGULARORDER;
		} else if(value.equals(STANDINGORDER.value())) {
			return STANDINGORDER;
		} else throw new RuntimeException("EnumCrisisMngBatchType: undefined enum :"+value);
	}
}

