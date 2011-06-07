package com.freshdirect.routing.constants;

public enum EnumHandOffBatchStatus {
	
	NEW, COMPLETED, STOPPED, PROCESSING, CANCELLED, INITFAILED, ROUTINGFAILED, ROUTEGENFAILED, COMMMITFAILED, ROUTINGCOMPETE, ROUTEGENERATED, AUTODISPATCHFAILED, AUTODISPATCHCOMPLETED;
	
	public String value(){
	    switch(this) {
	     case NEW: return "NEW";	     
	     case PROCESSING: return "PRC";
	     case ROUTINGCOMPETE: return "RTC";
	     case ROUTEGENERATED: return "RGC";
	     case AUTODISPATCHCOMPLETED: return "CPD/ADC";
	     case CANCELLED: return "CAN";
	     case STOPPED: return "STD";
	     case COMPLETED: return "CPD";
	     case INITFAILED: return "NEF";
	     case ROUTINGFAILED: return "RTF";
	     case ROUTEGENFAILED: return "RGF";
	     case COMMMITFAILED: return "CPF";
	     case AUTODISPATCHFAILED: return "CPD/ADF";
	     default: return "";
	   }
	}
	
	public static EnumHandOffBatchStatus getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(NEW.value())) {
			return NEW;
		} else if(value.equals(COMPLETED.value())) {
			return COMPLETED;
		} else if(value.equals(STOPPED.value())) {
			return STOPPED;
		} else if(value.equals(PROCESSING.value())) {
			return PROCESSING;
		} else if(value.equals(CANCELLED.value())) {
			return CANCELLED;
		} else if(value.equals(ROUTINGCOMPETE.value())) {
			return ROUTINGCOMPETE;
		} else if(value.equals(ROUTEGENERATED.value())) {
			return ROUTEGENERATED;
		}else if(value.equals(INITFAILED.value())) {
			return INITFAILED;
		} else if(value.equals(ROUTINGFAILED.value())) {
			return ROUTINGFAILED;
		} else if(value.equals(ROUTEGENFAILED.value())) {
			return ROUTEGENFAILED;
		} else if(value.equals(COMMMITFAILED.value())) {
			return COMMMITFAILED;
		} else if(value.equals(AUTODISPATCHFAILED.value())) {
			return AUTODISPATCHFAILED;
		} else if(value.equals(AUTODISPATCHCOMPLETED.value())) {
			return AUTODISPATCHCOMPLETED;
		} else {
			throw new RuntimeException("EnumHandOffBatchStatus: undefined enum :"+value);
		}
	}
}

