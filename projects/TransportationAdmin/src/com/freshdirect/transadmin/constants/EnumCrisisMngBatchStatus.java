package com.freshdirect.transadmin.constants;

public enum EnumCrisisMngBatchStatus {
	
	NEW, ORDERCOLECTIONCOMPLETE, COMPLETED, STOPPED, PROCESSING, CANCELLED, ORDERCOLECTIONFAILED, ORDERCANCELCOMPLETE, ORDERCANCELFAILED, CREATERESERVATIONCOMPLETE, CREATERESERVATIONFAILED, PLACESOCOMPLETE, PLACESOFAILED, AUTOCOMPLETED;
	
	public String value(){
	    switch(this) {
	     case NEW: return "NEW";	     
	     case PROCESSING: return "PRC";
	     case ORDERCOLECTIONCOMPLETE: return "DCC";
	     case ORDERCOLECTIONFAILED: return "DCF";
	     case CREATERESERVATIONFAILED: return "CRF";
	     case CREATERESERVATIONCOMPLETE: return "CRC";
	     case ORDERCANCELCOMPLETE: return "OCC";
	     case ORDERCANCELFAILED: return "OCF";
	     case PLACESOCOMPLETE: return "POC";
	     case PLACESOFAILED: return "POF";
	     case CANCELLED: return "CAN";
	     case STOPPED: return "STD";
	     case COMPLETED: return "CPD";
	     case AUTOCOMPLETED: return "CPD/OCC";	 
	    
	     default: return "";
	   }
	}
	
	public static EnumCrisisMngBatchStatus getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(NEW.value())) {
			return NEW;
		} else if(value.equals(COMPLETED.value())) {
			return COMPLETED;
		} else if(value.equals(AUTOCOMPLETED.value())) {
			return AUTOCOMPLETED;
		} else if(value.equals(STOPPED.value())) {
			return STOPPED;
		} else if(value.equals(PROCESSING.value())) {
			return PROCESSING;
		} else if(value.equals(CANCELLED.value())) {
			return CANCELLED;
		} else if(value.equals(ORDERCOLECTIONCOMPLETE.value())) {
			return ORDERCOLECTIONCOMPLETE;
		} else if(value.equals(ORDERCOLECTIONFAILED.value())) {
			return ORDERCOLECTIONFAILED;
		} else if(value.equals(ORDERCANCELCOMPLETE.value())) {
			return ORDERCANCELCOMPLETE;
		} else if(value.equals(ORDERCANCELFAILED.value())) {
			return ORDERCANCELFAILED;
		} else if(value.equals(CREATERESERVATIONCOMPLETE.value())) {
			return CREATERESERVATIONCOMPLETE;
		} else if(value.equals(CREATERESERVATIONFAILED.value())) {
			return CREATERESERVATIONFAILED;
		} else if(value.equals(PLACESOFAILED.value())) {
			return PLACESOFAILED;
		} else if(value.equals(PLACESOCOMPLETE.value())) {
			return PLACESOCOMPLETE;
		} else {
			throw new RuntimeException("EnumOrderScenarioBatchStatus: undefined enum :"+value);
		}
	}
}

