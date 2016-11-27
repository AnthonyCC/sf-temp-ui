package com.freshdirect.transadmin.manager;

public interface ICrisisManagerProcessMessage {
			
	String ERROR_MESSAGE_NOORDER = "There are no orders to processing batch";
	
	String INFO_MESSAGE_BATCHCANCELCOMPLETED = "Batch Cancel : Completed";
	
	String INFO_MESSAGE_CRISISMNGREGORDERBATCHTRIGERRED = "Regular order batch created successfully";
	
	String INFO_MESSAGE_CRISISMNGSOORDERBATCHTRIGERRED = "Standing order batch created successfully";
	
	String INFO_MESSAGE_ORDERDATACOLLECTIONPROGRESS = "OrderIn : Data collection in progress";
	
	String INFO_MESSAGE_ORDERDATACOLLECTIONCOMPLETED = "OrderIn : Completed";
	
	String INFO_MESSAGE_ORDERCANCELPROGRESS = "Order Cancel : in progress";
	
	String INFO_MESSAGE_ORDERCANCELCOMPLETED = "Order Cancel : Completed";
	
	String INFO_MESSAGE_REGULARORDERCANCELCOMPLETED = "Order Cancel : Completed, either you can create reservation(s) or mark batch as completed.";
	
	String INFO_MESSAGE_CREATERESERVATIONPROGRESS = "Create Reservation : in progress";
	
	String INFO_MESSAGE_CREATERESERVATIONFAILED = "Create Reservation : Failed";
	
	String INFO_MESSAGE_CREATERESERVATIONCOMPLETED = "Create Reservation : Completed";
	
	String ERROR_MESSAGE_TIMESLOTEXCEPTION = "There are timeslot exceptions. Please map the timeslots or setup if needed. ";	
	
	String ERROR_MESSAGE_ORDEREXCEPTION = "There are orders with status isn't right for cancelleation. Contact AppSupport!";
	
	String INFO_MESSAGE_PLACESTANDINGORDERPROGRESS = "Place Standing Order : in progress";
	
	String INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED = "Place Standing Order : Completed";
	
	String INFO_MESSAGE_PLACESTANDINGORDERFAILED = "Place Standing Order : Failed";
	
	String INFO_MESSAGE_BATCHCOMPLETED = "Batch Completed";
	
	String INFO_MESSAGE_BATCHTIMESLOTEXPMSG = "Batch contains timeslot exception(s)";

	String INFO_MESSAGE_PLACESTANDINGORDERMSG = "There are standing order failures. Please check the standing order report for more details";

	String INFO_MESSAGE_STANDINGORDEREXPCLEARMSG = "No Timeslot Exceptions. You can start placing order(s) or mark batch as complete.";

}
