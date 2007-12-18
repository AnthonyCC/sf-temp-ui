package com.freshdirect.webapp.util;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.event.EventLogger;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.framework.event.FDEvent;

/**
 * 
 * @author skrishnasamy
 *
 */
public class FDEventUtil {
	
	public static void logAddToCartEvent(FDCartLineI cartline, HttpServletRequest request) {
		FDEvent event = FDEventFactory.getFDAddToCartEvent(cartline, request);
		EventLogger.getInstance().logEvent(event);
	}

	public static void logEditCartEvent(FDCartLineI cartline, HttpServletRequest request) {
		FDEvent event = FDEventFactory.getFDEditCartEvent(cartline, request);
		EventLogger.getInstance().logEvent(event);
	}
	
	public static void logRemoveCartEvent(FDCartLineI cartline, HttpServletRequest request) {
		FDEvent event = FDEventFactory.getFDRemoveCartEvent(cartline, request);
		EventLogger.getInstance().logEvent(event);
	}
	
	public static void logBookRetailerRedirect(HttpServletRequest request) {
		FDEvent event = FDEventFactory.getBookRetailerRedirectEvent(request);
		EventLogger.getInstance().logEvent(event);		
	}

}
