package com.freshdirect.transadmin.manager;

public interface ICrisisManagerProcessMessage {
			
	String ERROR_MESSAGE_NOORDER = "There are no orders to processing batch";
	
	String INFO_MESSAGE_BATCHCANCELCOMPLETED = "Batch Cancel : Completed";
	
	String INFO_MESSAGE_CRISISMNGREGORDERBATCHTRIGERRED = "Crisis Manager Regular order Batch created successfully";
	
	String INFO_MESSAGE_CRISISMNGSOORDERBATCHTRIGERRED = "Crisis Manager Standing order Batch created successfully";
	
	String INFO_MESSAGE_ORDERDATACOLLECTIONPROGRESS = "OrderIn : Data collection in progress";
	
	String INFO_MESSAGE_ORDERDATACOLLECTIONCOMPLETED = "OrderIn : Completed";
	
	String INFO_MESSAGE_ORDERCANCELPROGRESS = "Order Cancel : in progress";
	
	String INFO_MESSAGE_ORDERCANCELCOMPLETED = "Order Cancel : Completed";
	
	String INFO_MESSAGE_REGULARORDERCANCELCOMPLETED = "Order Cancel : Completed, either you can perform create reservation action or mark as completed.";
	
	String INFO_MESSAGE_CREATERESERVATIONPROGRESS = "Create Reservation : in progress";
	
	String INFO_MESSAGE_CREATERESERVATIONCOMPLETED = "Create Reservation : Completed";
	
	String ERROR_MESSAGE_TIMESLOTEXCEPTION = "There are timeslot exceptions. Please map the timeslots or setup if needed. ";	
	
	String ERROR_MESSAGE_ORDEREXCEPTION = "There are orders whose status isn't right for order cancelleation";
	
	String INFO_MESSAGE_PLACESTANDINGORDERPROGRESS = "Place Standing Order : in progress";
	
	String INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED = "Place Standing Order : Completed";

}
