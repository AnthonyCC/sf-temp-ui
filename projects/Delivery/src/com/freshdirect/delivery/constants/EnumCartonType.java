package com.freshdirect.delivery.constants;

public enum EnumCartonType {
	
	Regular, Beer, Freezer, Platter, Case;
	
	public String value(){
	    switch(this) {
	     case Regular: return "Regular";	     
	     case Beer: return "Beer";
	     case Freezer: return "Freezer";
	     case Platter: return "Platter";
	     case Case: return "Case";
	     default: return "";
	   }
	}
	
	public static String getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(Regular.value())) {
			return "R";
		} else if(value.equals(Beer.value())) {
			return "B";
		} else if(value.equals(Freezer.value())) {
			return "F";
		} else if(value.equals(Platter.value())) {
			return "P";
		} else if(value.equals(Case.value())) {
			return "CP";
		} else {
			throw new RuntimeException("EnumCartonType: undefined enum :"+value);
		}
	}
}
