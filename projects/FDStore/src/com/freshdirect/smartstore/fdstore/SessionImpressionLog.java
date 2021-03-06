package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.sessionimpressionlog.SessionImpressionLogEntryData;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.HiLoGenerator;
import com.freshdirect.fdstore.customer.IDGenerator;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.smartstore.SessionImpressionLogEntry;

public class SessionImpressionLog {
	private static SessionImpressionLog sharedInstance = null;
	private ServiceLocator serviceLocator = null;
	
	private static Logger LOGGER = LoggerFactory.getInstance(SessionImpressionLog.class);

	static IDGenerator ID_GENERATOR = new HiLoGenerator ("CUST","PAGE_IMPRESSION_SEQ");  

	
	private SessionImpressionLog() throws NamingException {
		serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
	}

	/**
	 * Get shared instance.
	 * @return instance
	 */
	synchronized public static SessionImpressionLog getInstance() {
		if (sharedInstance == null) {
			try {
				sharedInstance = new SessionImpressionLog();
			} catch (NamingException e) {
				throw new FDRuntimeException(e,"Could not create session impression log helper shared instance");
			}
		}
		return sharedInstance;
	}

	public void saveLogEntry(SessionImpressionLogEntry entry) {
		try {

			Request<SessionImpressionLogEntryData> request = new Request<SessionImpressionLogEntryData>();
			SessionImpressionLogEntryData data = dataConversion(entry);
			request.setData(data);
			try {
				FDECommerceService.getInstance().saveLogEntry(request);
			} catch (FDResourceException e) {
				LOGGER.warn("Session impression log", e);
			}

		} catch (RemoteException e) {
			LOGGER.warn("Session impression log", e);
		}
	}

	public void saveLogEntries(Collection entries) {
		try {
			try {
				Collection<SessionImpressionLogEntryData> entriesCollection = new ArrayList<SessionImpressionLogEntryData>();
				Request<Collection<SessionImpressionLogEntryData>> request = new Request<Collection<SessionImpressionLogEntryData>>();

				for (Object object : entries) {
					SessionImpressionLogEntryData data = dataConversion((SessionImpressionLogEntry) object);
					entriesCollection.add(data);
				}
				request.setData(entriesCollection);
				FDECommerceService.getInstance().saveLogEntries(request);
			} catch (FDResourceException e) {
				LOGGER.warn("Session impression log", e);
			}

		} catch (RemoteException e) {
			LOGGER.warn("Session impression log",e);
		} 
	}
	
	public static String getPageId() {
	    return ID_GENERATOR.getNextId();
	}
	
	/**
	 * This is used for testing, to mock out the 
	 * @param generator
	 */
	public static void setIdGenerator(IDGenerator generator) {
	    ID_GENERATOR = generator;
	}
	
	private SessionImpressionLogEntryData dataConversion(SessionImpressionLogEntry entry) {
		SessionImpressionLogEntryData data = new SessionImpressionLogEntryData();
		if (entry != null) {
			data = new SessionImpressionLogEntryData(entry.getId(), entry.getUserPrimaryKey(), entry.getSessionId(),
					entry.getStartTime(), entry.getEndTime(), entry.getVariantId(), entry.getProductImpressions(),
					entry.getFeatureImpressions());
			data.setZoneId(entry.getZoneId());
		}
		return data;
	}
	
}
