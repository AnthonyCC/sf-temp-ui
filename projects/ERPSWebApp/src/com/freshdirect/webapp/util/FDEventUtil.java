package com.freshdirect.webapp.util;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.event.ClickThroughEventAggregator;
import com.freshdirect.event.EventLogger;
import com.freshdirect.event.ImpressionEventAggregator;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.event.FDWebEvent;

/**
 * 
 * @author skrishnasamy
 *
 */
public class FDEventUtil {
	
	public static void logAddToCartEvent(FDCartLineI cartline, HttpServletRequest request) {
		FDWebEvent event = FDEventFactory.getFDAddToCartEvent(cartline, request);
		EventLogger.getInstance().logEvent(event);
	}

	public static void logEditCartEvent(FDCartLineI cartline, HttpServletRequest request) {
		FDWebEvent event = FDEventFactory.getFDEditCartEvent(cartline, request);
		EventLogger.getInstance().logEvent(event);
	}
	
	public static void logRemoveCartEvent(FDCartLineI cartline, HttpServletRequest request) {
		FDWebEvent event = FDEventFactory.getFDRemoveCartEvent(cartline, request);
		EventLogger.getInstance().logEvent(event);
	}
	
	
	public static void logRecommendationImpression(String variantId, ContentKey contentKey) {
		FDRecommendationEvent event = FDEventFactory.getImpressionEvent(variantId, contentKey);
		ImpressionEventAggregator.getInstance().note(event);
	}
	
	public static void flushImpressions() {
		ImpressionEventAggregator.getInstance().flush();
	}
	
	public static void logRecommendationClickThrough(String variantId, ContentKey contentKey) {
		FDRecommendationEvent event = FDEventFactory.getClickThroughEvent(variantId, contentKey);
		ClickThroughEventAggregator.getInstance().note(event);
	}
	
	public static void flushClickThroughs() {
		ClickThroughEventAggregator.getInstance().flush();
	}

}
