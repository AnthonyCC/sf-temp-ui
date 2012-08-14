package com.freshdirect.athena.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class SystemMessageManager {
	
	private static SystemMessageManager instance = null;
	
	List<SystemMessage> messages = Collections.synchronizedList(new ArrayList<SystemMessage>(100));
	
	protected SystemMessageManager() {		
		
	}
	
	public static SystemMessageManager getInstance() {
		
		if(instance == null) {
			instance = new SystemMessageManager();
		}
		return instance;
	}
	
	public void addMessage(String message) {
		messages.add(new SystemMessage(new Date(), message));
	}

	public List<SystemMessage> getMessages() {
		return messages;
	}
	
	
}
