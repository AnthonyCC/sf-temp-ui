package com.freshdirect.transadmin.constants;

public enum EnumRouteStatus {
	
	NEW, EMPTY, LOADED;
	
	public String value(){
	    switch(this) {
	     case NEW: return "NEW";	     
	     case EMPTY: return "EYT";
	     case LOADED: return "LAD";
	     default: return "";
	   }
	}
	
	public static EnumRouteStatus getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(NEW.value())) {
			return NEW;
		} else if(value.equals(EMPTY.value())) {
			return EMPTY;
		} else if(value.equals(LOADED.value())) {
			return LOADED;
		} else {
			throw new RuntimeException("EnumRouteStatus: undefined enum :"+value);
		}
	}
}

