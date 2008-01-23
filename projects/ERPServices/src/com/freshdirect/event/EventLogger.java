package com.freshdirect.event;

import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.event.AsyncEventSink;
import com.freshdirect.framework.event.ConsoleEventSink;
import com.freshdirect.framework.event.EventSinkI;
import com.freshdirect.framework.event.FDEvent;

/**
 * @author knadeem Date May 4, 2005
 */
public class EventLogger {
	
	private static EventLogger instance;
	private EventSinkI consoleSink;
	private EventSinkI asyncSink;
	private FDEvent    lastEvent;
	
	private EventLogger () throws NamingException {
		
		this.consoleSink = new ConsoleEventSink();
		/*
		 * The Buffer size has been increased to 1500. This can
		 * ideally handle 1500 users concurrently adding an event
		 * to the queue.
		 */
		this.asyncSink = new AsyncEventSink(new RemoteEventSink(), ErpServicesProperties.getEventQueueSize());
		
		lastEvent = null;
	}
	
	public static EventLogger getInstance () {
		if(instance == null){
			try {
				instance = new EventLogger();
			} catch (NamingException e) {
				throw new FDRuntimeException(e, "Cannot create instanceof RemoteSink");
			}
		}
		
		return instance;
	}
	
	public void logEvent(FDEvent event) {
		this.consoleSink.log(event);
		this.asyncSink.log(event);
		lastEvent = event;
	}

	/**
	 *  Get the lest event logged.
	 *  Used for testing purposes.
	 *  
	 *  @return the last event logged, or null, if none logged so far.
	 */
	public FDEvent getLastEvent() {
		return lastEvent;
	}
}
