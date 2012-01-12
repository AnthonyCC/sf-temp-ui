package com.freshdirect.transadmin.constants;

public enum EnumRouteStatus {
	
	NODATA, OPEN, LOADED, PARTIALLOADED;
	
	public String value(){
	    switch(this) {
	     case NODATA: return "No-Data";
	     case OPEN: return "Open";
	     case LOADED: return "Loaded";
	     case PARTIALLOADED: return "Partially Loaded";
	     default: return "";
	   }
	}
	
	public static EnumRouteStatus getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(NODATA.value())) {
			return NODATA;
		} else if(value.equals(OPEN.value())) {
			return OPEN;
		} else if(value.equals(LOADED.value())) {
			return LOADED;
		} else if(value.equals(PARTIALLOADED.value())) {
			return PARTIALLOADED;
		} else {
			throw new RuntimeException("EnumRouteStatus: undefined enum :"+value);
		}
	}
}

