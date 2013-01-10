package com.freshdirect.transadmin.service.exception;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Issue implements IIssue {
	
	private static Map messages = new HashMap();
	
	static {
		messages.put(UNDEFINED, "Unknown error occured");
		messages.put(EMPTY, "");
		messages.put(PROCESS_CRISISMNGBATCH_ERROR,"Crisis Manager Batch Processing Error");
		messages.put(PROCESS_YARDMONITOR_ERROR,"Yard Monitor Processing Error");
		messages.put(PROCESS_EVENTLOG_ERROR,"Event Log Processing Error");

	}
	
	public static String getMessage(String id) {
		return (String)messages.get(id);
	}

}
