package com.freshdirect.dashboard.exception;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Issue implements IIssue {
	
	private static Map messages = new HashMap();
	
	static {
		messages.put(UNDEFINED, "Unknown error occured");
		messages.put(EMPTY, "");
		
		messages.put(BOUNCE_DATA_ERROR,"Bounce data processing Error");
	}
	
	public static String getMessage(String id) {
		return (String)messages.get(id);
	}
}
