package com.freshdirect.analytics;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.analytics.ejb.EventProcessorHome;
import com.freshdirect.analytics.ejb.EventProcessorSB;
import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;


public class EventLog {
	private static Category LOGGER = LoggerFactory.getInstance(EventLog.class);
	private static ERPServiceLocator LOCATOR = ERPServiceLocator.getInstance();
	private static EventProcessorHome managerHome = null;
	private static EventLog _instance = null;
	
	private EventLog() {
	}
		
	public static EventLog getInstance() {
		if (_instance == null) {
			_instance = new EventLog();
		}
		return _instance;
	}

	public void logEvent(SessionEvent event)
	{
		lookupManagerHome();

		try {
			EventProcessorSB sb = managerHome.create();
			sb.logEvent(event);
		} catch (CreateException ce) {
			invalidateManagerHome();
		} catch (RemoteException re) {
			invalidateManagerHome();
		}

	}
	
	private static void invalidateManagerHome() {
		managerHome = null;
	}

	private static void lookupManagerHome() {
		if (managerHome != null) {
			return;
		}
		managerHome = LOCATOR.getEventProcessorHome();
	}
	
}
