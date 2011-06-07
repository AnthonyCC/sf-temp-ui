package com.freshdirect.routing.constants;

public enum EnumHandOffBatchActionType {
	
	CREATE, ROUTEIN, ROUTEOUT, COMMIT, STOP, CANCEL, AUTODISPATCH;
	
	public String value(){
	    switch(this) {
	     case CREATE: return "CRO";
	     case ROUTEIN: return "ROI";
	     case ROUTEOUT: return "ROU";
	     case COMMIT: return "COM";
	     case AUTODISPATCH: return "ADP";
	     case STOP: return "STP";
	     case CANCEL: return "CCL";
	     default: return "";
	   }
	}
	
	public static EnumHandOffBatchActionType getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(CREATE.value())) {
			return CREATE;
		} else if(value.equals(ROUTEIN.value())) {
			return ROUTEIN;
		} else if(value.equals(ROUTEOUT.value())) {
			return ROUTEOUT;
		} else if(value.equals(COMMIT.value())) {
			return COMMIT;
		} else if(value.equals(AUTODISPATCH.value())) {
			return AUTODISPATCH;
		} else if(value.equals(STOP.value())) {
			return STOP;
		} else if(value.equals(CANCEL.value())) {
			return CANCEL;
		} else throw new RuntimeException("EnumHandOffBatchActionType: undefined enum :"+value);
	}
}

