package com.freshdirect.transadmin.service.exception;

import java.util.HashMap;
import java.util.Map;

public class Issue implements IIssue {
	
	private static Map messages = new HashMap();
	
	static {
		messages.put(UNDEFINED, "Unknown error occured");
		messages.put(EMPTY, "");
		messages.put(PROCESS_CRISISMNGBATCH_ERROR,"Crisis Manager Batch Processing Error");
	}
	
	public static String getMessage(String id) {
		return (String)messages.get(id);
	}

}
