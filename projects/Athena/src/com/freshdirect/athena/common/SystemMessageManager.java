package com.freshdirect.athena.common;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class SystemMessageManager {
	
	private static SystemMessageManager instance = null;
	
	List<SystemMessage> messages = Collections.synchronizedList(new LimitedQueue<SystemMessage>(250));
	
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
	
	private class LimitedQueue<E> extends LinkedList<E> {

	    private int limit;

	    public LimitedQueue(int limit) {
	        this.limit = limit;
	    }

	    @Override
	    public boolean add(E o) {
	        boolean added = super.add(o);
	        while (added && size() > limit) {
	           super.remove();
	        }
	        return added;
	    }
	}
}
