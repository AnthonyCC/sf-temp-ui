package com.freshdirect.event.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.framework.event.FDRecommendationEvent;

/**
 * Impression logger session bean interface.
 * @author istvan
 *
 */
public interface RecommendationEventLoggerSB  extends EJBObject {

	/**
	 * Log a batch of impression event aggregates.
	 * @param eventClazz the subclass of {@link FDRecommendationEvent}
	 * @param events Collection<{@link RecommendationEventsAggregate>}
	 * @throws RemoteException
	 */
	public void log(Class eventClazz,Collection events) throws RemoteException;
	
	/**
	 * Log a single impression event.
	 * @param event impression 
	 * @param frequency assigned frequency
	 * @throws RemoteException
	 */
	public void log(FDRecommendationEvent event, int frequency) throws RemoteException;
}
