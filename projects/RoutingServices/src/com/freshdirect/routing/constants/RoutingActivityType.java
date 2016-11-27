package com.freshdirect.routing.constants;

public enum RoutingActivityType {
	GET_TIMESLOT,CHECK_TIMESLOT,RESERVE_TIMESLOT,CONFIRM_TIMESLOT,CANCEL_TIMESLOT,PROCESS_ADDRESS,UPDATE_TIMESLOT, RETRIEVE_ORDER, SAVE_LOCATION;
	
	public String value(){
	    switch(this) {
	     case GET_TIMESLOT: return "GET_TIMESLOT";
	     case CHECK_TIMESLOT: return "CHECK_TIMESLOT";
	     case RESERVE_TIMESLOT: return "RESERVE_TIMESLOT";
	     case UPDATE_TIMESLOT: return "UPDATE_TIMESLOT";
	     case CONFIRM_TIMESLOT: return "CONFIRM_TIMESLOT";
	     case CANCEL_TIMESLOT: return "CANCEL_TIMESLOT";
	     case RETRIEVE_ORDER: return "RETRIEVE_ORDER";
	     case SAVE_LOCATION: return "SAVE_LOCATION";
	     case PROCESS_ADDRESS: return "ROUTINGADDRESS/process";
	     default: return "";
	   }
	}
	
	public static RoutingActivityType getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(GET_TIMESLOT.value())) {
			return GET_TIMESLOT;
		} else if(value.equals(CHECK_TIMESLOT.value())) {
			return CHECK_TIMESLOT;
		} else if(value.equals(RESERVE_TIMESLOT.value())) {
			return RESERVE_TIMESLOT;
		} else if(value.equals(CONFIRM_TIMESLOT.value())) {
			return CONFIRM_TIMESLOT;
		} else if(value.equals(CANCEL_TIMESLOT.value())) {
			return CANCEL_TIMESLOT;
		} else if(value.equals(UPDATE_TIMESLOT.value())) {
			return UPDATE_TIMESLOT;
		} else if(value.equals(PROCESS_ADDRESS.value())) {
			return PROCESS_ADDRESS;
		} else if(value.equals(RETRIEVE_ORDER.value())) {
			return RETRIEVE_ORDER;
		}  else if(value.equals(SAVE_LOCATION.value())) {
			return SAVE_LOCATION;
		} else throw new RuntimeException("IRoutingActivityType: undefined enum :"+value);
	}
}

