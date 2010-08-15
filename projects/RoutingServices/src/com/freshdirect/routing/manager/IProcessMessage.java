package com.freshdirect.routing.manager;

public interface IProcessMessage {
	
	String ROUTING_SUCCESS = "Routing Success";
	
	String LOADBALANCE_SUCCESS = "Load Balance Success";

	String SENDROUTES_SUCCESS = "Send Routes to RoadNet Success";
	
	String INFO_MESSAGE_ROUTINGINPROGRESS = "Routing In : Routing in progress";
	
	String INFO_MESSAGE_ROUTINGINDATACOLLECTIONPROGRESS = "Routing In : Data collection in progress";
	
	String INFO_MESSAGE_ROUTINGINCOMPLETED = "Routing In : Completed";
	
	String INFO_MESSAGE_ROUTINGOUTPROGRESS = "Routing Out : Routing generation in progress";
	
	String INFO_MESSAGE_ROUTINGOUTCOMPLETED = "Routing Out : Completed";
	
	String INFO_MESSAGE_BATCHCANCELCOMPLETED = "Batch Cancel : Completed";
	
	String INFO_MESSAGE_BATCHCOMMITCOMPLETED = "Batch Commit : Completed";
	
	String INFO_MESSAGE_BATCHSTOPCOMPLETED = "Batch Stop : Completed";
	
	String ERROR_MESSAGE_NOORDER = "There are no orders to processing batch";
	
	String ERROR_MESSAGE_PENDINGORDER = "There are orders in Processing, Modified or Modified Canceled. Please inform AppSupport and continue with the routing process. ";
	
	String INFO_MESSAGE_STANDBYMODE = "Handoff creation is now in standby mode you will not be able to commit the handoff.";
	
	String INFO_MESSAGE_HANDOFFBATCHTRIGERRED = "HandOff Batch created successfully";
	
	String ERROR_MESSAGE_INELIGIBLECOMMIT = "Batch Commit : InEligible";
}
