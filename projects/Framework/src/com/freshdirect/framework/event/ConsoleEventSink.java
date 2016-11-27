package com.freshdirect.framework.event;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author knadeem Date May 3, 2005
 */
public class ConsoleEventSink implements EventSinkI {
	
	private static Category LOGGER = LoggerFactory.getInstance(ConsoleEventSink.class);
	
	public boolean log(FDWebEvent event) {
		LOGGER.debug(event);
		return true;
	}

}
