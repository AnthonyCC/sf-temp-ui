package com.freshdirect.event;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.event.ejb.EventLoggerHome;
import com.freshdirect.event.ejb.EventLoggerSB;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.event.EventSinkI;
import com.freshdirect.framework.event.FDWebEvent;

/**
 * @author knadeem Date May 18, 2005
 */
public class RemoteEventSink implements EventSinkI {

	private final ServiceLocator serviceLocator;
	
	public RemoteEventSink() throws NamingException {
		this.serviceLocator = new ServiceLocator(ErpServicesProperties.getInitialContext());
	}

	public boolean log(FDWebEvent event) {
		try {
			EventLoggerSB sb = this.getEventLoggerHome().create();
			sb.log(event);
			return true;
		} catch (RemoteException e) {
			throw new EJBException("Cannot Create EventLoggerSB", e);
		} catch (CreateException e) {
			throw new EJBException("Cannot talk to EventLoggerSB", e);
		}
	}
	
	private EventLoggerHome getEventLoggerHome() {
		try {
			return (EventLoggerHome) serviceLocator.getRemoteHome("freshdirect.event.EventLogger", EventLoggerHome.class);
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
	}

}
