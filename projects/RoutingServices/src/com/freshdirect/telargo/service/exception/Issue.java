package com.freshdirect.telargo.service.exception;

import java.util.HashMap;
import java.util.Map;

public class Issue implements IIssue {
	
	private static Map messages = new HashMap();
	
	static {
		messages.put(UNDEFINED, "Unknown error occured");
		messages.put(EMPTY, "");
		messages.put(LOCATE_TELARGOSERVICE_UNSUCCESSFUL, "Locating telargo service failed");
		messages.put(PROCESS_RETRIEVETSASSET_UNSUCCESSFUL,"Retrieving telargo service assets details failed");
	}
	
	public static String getMessage(String id) {
		return (String)messages.get(id);
	}

}
