package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationHome;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationSB;

/**
 * Configures SmartStore services.
 * 
 * @author istvan
 * 
 */
public class SmartStoreServiceConfiguration {
	// logger instance
	private static Category LOGGER = LoggerFactory
			.getInstance(SmartStoreServiceConfiguration.class);

	// static instance
	private static SmartStoreServiceConfiguration instance = null;

	// private constructor
	private SmartStoreServiceConfiguration() {
	}

	/**
	 * Get the unique instance.
	 * 
	 * @return the instance or null
	 */
	public static SmartStoreServiceConfiguration getInstance() {
		if (instance == null) {
			instance = new SmartStoreServiceConfiguration();
		}
		return instance;
	}

	// get service configuration home bean
	private SmartStoreServiceConfigurationHome getServiceConfigurationHome() {
		try {
			return (SmartStoreServiceConfigurationHome) new ServiceLocator(
					FDStoreProperties.getInitialContext()).getRemoteHome(
					"freshdirect.smartstore.SmartStoreServiceConfiguration",
					SmartStoreServiceConfigurationHome.class);
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
	}

	public Collection loadDynamicSiteFeatures() {
		try {
			SmartStoreServiceConfigurationSB sb;
			sb = getServiceConfigurationHome().create();
			return sb.getSiteFeatures();
		} catch (RemoteException e) {
			LOGGER.warn("SmartStore Service Configuration", e);
			return Collections.EMPTY_LIST;
		} catch (CreateException e) {
			LOGGER.warn("SmartStore Service Configuration", e);
			return Collections.EMPTY_LIST;
		} catch (SQLException e) {
			LOGGER.warn("SmartStore Service Configuration", e);
			return Collections.EMPTY_LIST;
		}
	}
}
