package com.freshdirect.routing.constants;

public enum EnumOrderMetricsSource {
	
	DEFAULT, HISTORY, ACTUAL;
	
	public String value(){
	    switch(this) {
	     case DEFAULT: return "DEF";
	     case HISTORY: return "HIS";
	     case ACTUAL: return "ACT";	     
	     default: return null;
	   }
	}
	
	public static EnumOrderMetricsSource getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(DEFAULT.value())) {
			return DEFAULT;
		} else if(value.equals(HISTORY.value())) {
			return HISTORY;
		} else if(value.equals(ACTUAL.value())) {
			return ACTUAL;
		} else throw new RuntimeException("EnumOrderMetricsSource: undefined enum :"+value);
	}
}

