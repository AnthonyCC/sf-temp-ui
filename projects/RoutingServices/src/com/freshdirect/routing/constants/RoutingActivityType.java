package com.freshdirect.routing.constants;

/*public class IRoutingMessageType {
	public static final String GET_TIMESLOT = "GET_TIMESLOT";
	public static final String RESERVE_TIMESLOT = "RESERVE_TIMESLOT";
	public static final String CONFIRM_TIMESLOT = "CONFIRM_TIMESLOT";
	public static final String CANCEL_TIMESLOT = "CANCEL_TIMESLOT";
	public static final String PROCESS_ADDRESS="ROUTINGADDRESS/process";
	
	
	

}*/

public enum RoutingActivityType {
	GET_TIMESLOT,RESERVE_TIMESLOT,CONFIRM_TIMESLOT,CANCEL_TIMESLOT,PROCESS_ADDRESS,UPDATE_TIMESLOT;
	
	public String value(){
	    switch(this) {
	     case GET_TIMESLOT: return "GET_TIMESLOT";
	     case RESERVE_TIMESLOT: return "RESERVE_TIMESLOT";
	     case UPDATE_TIMESLOT: return "UPDATE_TIMESLOT";
	     case CONFIRM_TIMESLOT: return "CONFIRM_TIMESLOT";
	     case CANCEL_TIMESLOT: return "CANCEL_TIMESLOT";
	     case PROCESS_ADDRESS: return "ROUTINGADDRESS/process";
	     default: return "";
	   }
	}
	
	public static RoutingActivityType getEnum(String value) {
		
		if(value==null || "".equals(value.trim())) {
			return null;
		} else if(value.equals(GET_TIMESLOT.value())) {
			return GET_TIMESLOT;
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
		} else throw new RuntimeException("IRoutingActivityType: undefined enum :"+value);
	}
}

