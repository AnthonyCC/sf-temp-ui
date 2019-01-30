package com.freshdirect.event;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBException;

import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.payment.service.FDECommerceService;

/**
 * Recommendation event logger instance.
 * 
 * This is a singleton class.
 * 
 * @author istvan
 *
 */
public class RecommendationEventLogger {

	private static RecommendationEventLogger instance = null;

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	synchronized public static RecommendationEventLogger getInstance() {
		if (instance == null) {

			instance = new RecommendationEventLogger();

		}
		return instance;
	}

	/**
	 * This is used just for testing.
	 * 
	 * @param logger
	 */
	synchronized public static void setInstance(RecommendationEventLogger logger) {
		instance = logger;
	}

	/**
	 * Log one recommendation event.
	 * 
	 * @param event     impression event
	 * @param frequency assigned frequency
	 */
	public void log(FDRecommendationEvent event, int frequency) {
		try {
			FDECommerceService.getInstance().log((FDRecommendationEvent) event, frequency);
		} catch (RemoteException e) {
			throw new EJBException("Could not log event " + event, e);
		}

	}

	/**
	 * Log a batch of aggregated impression events.
	 * 
	 * @param eventClazz subclass of {@link FDRecommendationEvent}
	 * @param events     Collection<{@link RecommendationEventsAggregate}>
	 */
	public void log(Class<? extends FDRecommendationEvent> eventClazz,
			Collection<RecommendationEventsAggregate> events) {

		try {
			FDECommerceService.getInstance().log(eventClazz, events);
		} catch (RemoteException e) {
			throw new EJBException("Could not log " + events.size() + " events", e);
		}

	}

}
