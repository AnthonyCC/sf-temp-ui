package com.freshdirect.transadmin.constants;

public enum EnumCrisisMngBatchActionType {
	
	CREATE, ORDERDATAIN, ORDERCANCEL, CREATERESERVATION, PLACEORDER, COMPLETE, CANCEL;
	
	public String value(){
	    switch(this) {
	     case CREATE: return "CRO";
	     case ORDERDATAIN: return "ODC";
	     case ORDERCANCEL: return "OCL";
	     case CREATERESERVATION: return "CRV";
	     case PLACEORDER: return "POR";
	     case COMPLETE: return "CPD";
	     case CANCEL: return "CAN";
	     default: return "";
	   }
	}
	
	public static EnumCrisisMngBatchActionType getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(CREATE.value())) {
			return CREATE;
		} else if(value.equals(ORDERDATAIN.value())) {
			return ORDERDATAIN;
		} else if(value.equals(ORDERCANCEL.value())) {
			return ORDERCANCEL;
		} else if(value.equals(CREATERESERVATION.value())) {
			return CREATERESERVATION;
		} else if(value.equals(PLACEORDER.value())) {
			return PLACEORDER;
		} else if(value.equals(COMPLETE.value())) {
			return COMPLETE;
		} else if(value.equals(CANCEL.value())) {
			return CANCEL;
		} else throw new RuntimeException("EnumOrderScenarioBatchActionType: undefined enum :"+value);
	}
}

