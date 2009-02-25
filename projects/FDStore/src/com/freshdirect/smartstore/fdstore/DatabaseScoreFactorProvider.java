package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.ejb.ScoreFactorHome;
import com.freshdirect.smartstore.ejb.ScoreFactorSB;

public class DatabaseScoreFactorProvider {
	
	
	private static Category LOGGER = LoggerFactory.getInstance(DatabaseScoreFactorProvider.class);
	
	private static DatabaseScoreFactorProvider instance = null;
	
	public synchronized static DatabaseScoreFactorProvider getInstance() {
		if (instance == null) {
			try {
				instance = new DatabaseScoreFactorProvider();
			} catch (NamingException e) {
				LOGGER.warn("Could not create " + DatabaseScoreFactorProvider.class.getName() + " instance", e);
				throw new RuntimeException(e);
			}
		}
		return instance;
	}

	public Map getPersonalizedFactors(String erpCustomerId, List factors) {
		try {
			if (erpCustomerId == null) {
				return Collections.EMPTY_MAP;
			}
			return getSessionBean().getPersonalizedFactors(erpCustomerId, factors);
		} catch (RemoteException e) {
			LOGGER.warn(e);
			throw new FDRuntimeException(e);
		}
	}
	
	public Map getGlobalFactors(List factors) {
		try {
			return getSessionBean().getGlobalFactors(factors);
		} catch (RemoteException e) {
			LOGGER.warn(e);
			throw new FDRuntimeException(e);
		}
	}
	
	public Set getGlobalFactorNames() {
		try {
			return getSessionBean().getGlobalFactorNames();
		} catch (RemoteException e) {
			LOGGER.warn(e);
			throw new FDRuntimeException(e);
		}
	}
	
	public Set getPersonalizedFactorNames()  {
		try {
			return getSessionBean().getPersonalizedFactorNames();
		} catch (RemoteException e) {
			LOGGER.warn(e);
			throw new FDRuntimeException(e);
		}
	}
	
	public Set getPersonalizedProducts(String erpCustomerId) {
		try {
			return getSessionBean().getPersonalizedProducts(erpCustomerId);
		} catch (RemoteException e) {
			LOGGER.warn(e);
			throw new FDRuntimeException(e);
		}
	}
	
	public Set getGlobalProducts() {
		try {
			return getSessionBean().getGlobalProducts();
		} catch (RemoteException e) {
			LOGGER.warn(e);
			throw new FDRuntimeException(e);
		}
	}
	
	private ServiceLocator serviceLocator = null;
	
	private DatabaseScoreFactorProvider() throws NamingException {
		serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
	}
	
	// get service configuration home bean
	private ScoreFactorHome getScoreFactorHome() throws NamingException {	
		return (ScoreFactorHome) serviceLocator.getRemoteHome(
			"freshdirect.smartstore.ScoreFactorHome", ScoreFactorHome.class);
		
	}
	
	private ScoreFactorSB getSessionBean() {
		try {
			return getScoreFactorHome().create();
		} catch (RemoteException e) {
			LOGGER.warn(e);
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			LOGGER.warn(e);
			throw new FDRuntimeException(e);
		} catch (NamingException e) {
			LOGGER.warn(e);
			throw new FDRuntimeException(e);
		}
	}
}
