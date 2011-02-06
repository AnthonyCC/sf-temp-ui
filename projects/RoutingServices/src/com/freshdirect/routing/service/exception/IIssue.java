package com.freshdirect.routing.service.exception;

public interface IIssue {
	
	public final static String UNDEFINED = "UNDEFINED";
	public final static String EMPTY = "EMPTY";
	public final static String PROCESS_SCENARIO_NOTFOUND = "ISSUE001";
	public final static String PROCESS_ZONEINFO_NOTFOUND = "ISSUE002";
	public final static String PROCESS_BUILDING_SAVEERROR = "ISSUE003";
	public final static String PROCESS_LOCATION_SAVEERROR = "ISSUE004";
	public final static String PROCESS_SERVICETIME_NOTFOUND = "ISSUE005";
	public final static String PROCESS_DELIVERYINFO_NOTFOUND = "ISSUE006";
	public final static String PROCESS_DELIVERYTYPE_NOTFOUND = "ISSUE007";
	public final static String PROCESS_ZONETYPE_NOTFOUND = "ISSUE008";
	public final static String PROCESS_BUILDING_NOTFOUND = "ISSUE009";
	public final static String PROCESS_LOCATION_NOTFOUND = "ISSUE010";
	public final static String PROCESS_GEOCODE_UNSUCCESSFUL = "ISSUE011";
	public final static String PROCESS_LOCALGECODE_UNSUCCESSFUL = "ISSUE012";
	public final static String PROCESS_STATES_NOTFOUND = "ISSUE013";
	public final static String PROCESS_ADDRESSSTANDARDIZE_UNSUCCESSFUL = "ISSUE014";
	public final static String PROCESS_PURGEORDERS_UNSUCCESSFUL = "ISSUE015";
	public final static String PROCESS_BULKRESERVE_UNSUCCESSFUL = "ISSUE016";
	public final static String PROCESS_SENDROUTES_UNSUCCESSFUL = "ISSUE017";
	public final static String PROCESS_SENDUNASSIGNED_UNSUCCESSFUL = "ISSUE018";
	public final static String DATEPARSE_ERROR = "ISSUE019";
	public final static String PROCESS_BALANCEROUTES_UNSUCCESSFUL = "ISSUE020";
	public final static String PROCESS_RETRIEVESESSION_UNSUCCESSFUL = "ISSUE021";
	public final static String PROCESS_REMOVEFROMSERVER_UNSUCCESSFUL = "ISSUE022";
	public final static String PROCESS_LOADLATEDELIVERYORDERS_UNSUCCESSFUL = "ISSUE023";
	
	public final static String PROCESS_ANALYZE_UNSUCCESSFUL = "ISSUE024";
	public final static String PROCESS_TIMESLOTSELECT_UNSUCCESSFUL = "ISSUE025";
	public final static String PROCESS_RESERVE_UNSUCCESSFUL = "ISSUE026";
	public final static String PROCESS_CONFIRM_UNSUCCESSFUL = "ISSUE027";
	
	public final static String PROCESS_RETRIEVEMETRICS_UNSUCCESSFUL = "ISSUE028";
	
	public final static String PROCESS_UPDATE_UNSUCCESSFUL = "ISSUE029";
	
	public final static String PROCESS_CANCEL_UNSUCCESSFUL = "ISSUE030";
	
	public final static String PROCESS_UNASSIGNED_UNSUCCESSFUL = "ISSUE031";
	
	public final static String PROCESS_RETRIEVENOTIFICATION_UNSUCCESSFUL = "ISSUE032";
	
	public final static String PROCESS_DELETENOTIFICATION_UNSUCCESSFUL = "ISSUE033";
	
	public final static String PROCESS_RETRIEVEORDER_NOTFOUND = "ISSUE034";
	
	public final static String PROCESS_RETRIEVEORDER_UNSUCCESSFUL = "ISSUE035";
	
	public final static String PROCESS_HANDOFFBATCH_ERROR = "ISSUE036";
	
	public final static String PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL = "ISSUE037";
	
	public final static String PROCESS_SAVEWAVEINSTANCE_UNSUCCESSFUL = "ISSUE038";
	
	public final static String PROCESS_WAVEINSTANCE_NOTPUBLISHED = "ISSUE039";
}
