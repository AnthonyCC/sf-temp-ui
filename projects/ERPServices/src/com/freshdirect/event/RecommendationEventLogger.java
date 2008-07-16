package com.freshdirect.event;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.event.ejb.RecommendationEventLoggerHome;
import com.freshdirect.event.ejb.RecommendationEventLoggerSB;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.event.FDRecommendationEvent;

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
	
	private ServiceLocator serviceLocator = null;
	
	
	/**
	 * Get instance.
	 * @return instance
	 */
	synchronized public static RecommendationEventLogger getInstance() {
		if (instance == null) {
			try {
				instance = new RecommendationEventLogger();
			} catch (NamingException e) {
				throw new FDRuntimeException(e,"Could not create recommendation event logger instance");
			}
		}
		return instance;
	}
	
	/**
	 * Log one recommendation event.
	 * @param event impression event
	 * @param frequency assigned frequency
	 */
	public void log(FDRecommendationEvent event, int frequency) {
		RecommendationEventLoggerSB bean;
		try {
			bean = this.getImpressionLoggerHome().create();
		} catch (RemoteException e) {
			throw new EJBException("Could not create impression logger home",e);
		} catch (CreateException e) {
			throw new EJBException("Could not create impression logger home",e);
		}
		try {
			bean.log((FDRecommendationEvent)event,frequency);
		} catch (RemoteException e) {
			throw new EJBException("Could not log event " + event,e);
		}
	}
	
	/**
	 * Log a batch of aggregated impression events.
	 * 
	 * @param eventClazz subclass of {@link FDRecommendationEvent}
	 * @param events Collection<{@link RecommendationEventsAggregate}>
	 */
	public void log(Class eventClazz, Collection events) {
		RecommendationEventLoggerSB bean;
		try {
			bean = this.getImpressionLoggerHome().create();
		} catch (RemoteException e) {
			throw new EJBException("Could not create impression logger home",e);
		} catch (CreateException e) {
			throw new EJBException("Could not create impression logger home",e);
		}
		try {
			bean.log(eventClazz,events);
		} catch (RemoteException e) {
			throw new EJBException("Could not log " + events.size() + " events",e);
		}
	}
	
	private RecommendationEventLogger () throws NamingException {
		serviceLocator = new ServiceLocator(ErpServicesProperties.getInitialContext());
	}

	private RecommendationEventLoggerHome getImpressionLoggerHome() {
		try {
			return (RecommendationEventLoggerHome) serviceLocator.getRemoteHome(
				"freshdirect.event.RecommendationEventLogger", RecommendationEventLoggerHome.class);
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
	}

}
