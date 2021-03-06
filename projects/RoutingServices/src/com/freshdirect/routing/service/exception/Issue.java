package com.freshdirect.routing.service.exception;

import java.util.HashMap;
import java.util.Map;

public class Issue implements IIssue {
	
	private static Map messages = new HashMap();
	
	static {
		messages.put(UNDEFINED, "Unknown error occured");
		messages.put(EMPTY, "");
		messages.put(PROCESS_SCENARIO_NOTFOUND, "Unable to load scenario or no default scenario defined");
		messages.put(PROCESS_ZONEINFO_NOTFOUND,"Unable to load zone information");
		messages.put(PROCESS_BUILDING_SAVEERROR, "Unable to save building information");
		messages.put(PROCESS_LOCATION_SAVEERROR,"Unable to save location information");
		messages.put(PROCESS_SERVICETIME_NOTFOUND,"Unable to load service time information");
		messages.put(PROCESS_DELIVERYINFO_NOTFOUND,"Unable to load delivery info information");
		messages.put(PROCESS_DELIVERYTYPE_NOTFOUND,"Unable to load delivery type information");
		messages.put(PROCESS_ZONETYPE_NOTFOUND,"Unable to load zone type information");
		
		messages.put(PROCESS_BUILDING_NOTFOUND,"Unable to load building information");
		messages.put(PROCESS_LOCATION_NOTFOUND,"Unable to load location information");
		messages.put(PROCESS_GEOCODE_UNSUCCESSFUL,"Unable to geocode");
		messages.put(PROCESS_LOCALGECODE_UNSUCCESSFUL,"Unable to geocode with local geocode engine");
		messages.put(PROCESS_STATES_NOTFOUND,"Unable to load states information");
		messages.put(PROCESS_ADDRESSSTANDARDIZE_UNSUCCESSFUL,"Unable to standardize address information");
		messages.put(PROCESS_PURGEORDERS_UNSUCCESSFUL,"Unable to purge orders in routing system");
		messages.put(PROCESS_BULKRESERVE_UNSUCCESSFUL,"Routing Failed");
		messages.put(PROCESS_SENDROUTES_UNSUCCESSFUL,"Send Routes to RoadNet Failed");
		messages.put(PROCESS_SENDUNASSIGNED_UNSUCCESSFUL,"Send Unassigned to RoadNet Failed");
		messages.put(DATEPARSE_ERROR,"Date Parse Error");
		messages.put(PROCESS_BALANCEROUTES_UNSUCCESSFUL,"Balance Routes Failed");
		messages.put(PROCESS_RETRIEVESESSION_UNSUCCESSFUL,"Retrieve Session Failed");
		messages.put(PROCESS_REMOVEFROMSERVER_UNSUCCESSFUL,"Retrieve Session Failed");
		messages.put(PROCESS_LOADLATEDELIVERYORDERS_UNSUCCESSFUL,"Retrieve Late Delivery Orders Failed");
		
		messages.put(PROCESS_ANALYZE_UNSUCCESSFUL,"Analyze Order Failed");
		messages.put(PROCESS_TIMESLOTSELECT_UNSUCCESSFUL,"Timeslot Selection Failed");
		messages.put(PROCESS_RESERVE_UNSUCCESSFUL,"Reserve Order Failed");
		messages.put(PROCESS_CONFIRM_UNSUCCESSFUL,"Confirm Order Failed");
		
		messages.put(PROCESS_UPDATE_UNSUCCESSFUL,"Update Order Failed");
		messages.put(PROCESS_CANCEL_UNSUCCESSFUL,"Cancel Order Failed");
		
		messages.put(PROCESS_RETRIEVEMETRICS_UNSUCCESSFUL,"Retrieve Delivery Window metrics Failed");
		
		messages.put(PROCESS_UNASSIGNED_UNSUCCESSFUL,"Retrieve Unassigned Failed");
		
		messages.put(PROCESS_RETRIEVENOTIFICATION_UNSUCCESSFUL,"Retrieve Notification Failed");
		
		messages.put(PROCESS_DELETENOTIFICATION_UNSUCCESSFUL,"Delete Notifications Failed");
		
		messages.put(PROCESS_RETRIEVEORDER_NOTFOUND,"Retrieve Order Failed");
		messages.put(PROCESS_RETRIEVEORDER_UNSUCCESSFUL,"Retrieve Order Not Found");
		messages.put(PROCESS_HANDOFFBATCH_ERROR,"Hand Off Batch Processing Error");
		
		messages.put(PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL,"Retrieve Wave Instance Failed");
		messages.put(PROCESS_SAVEWAVEINSTANCE_UNSUCCESSFUL,"Save Wave Instance Failed");
		messages.put(PROCESS_WAVEINSTANCE_NOTPUBLISHED,"Plan not published");
		messages.put(PROCESS_AUTODISPATCH_ERROR,"Auto-Dispatch running longer than usual");
		messages.put(PROCESS_CANCEL_RESERVATIONS,"Cancel Reservations found. Cannot proceed further.");		
		messages.put(RETRIEVE_CUTOFFS_FAILED,"Retrieve cutoffs for sequence failed");		
	}
	
	public static String getMessage(String id) {
		return (String)messages.get(id);
	}

}
