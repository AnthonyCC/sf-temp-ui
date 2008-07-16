package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.ejb.VariantSelectionHome;
import com.freshdirect.smartstore.ejb.VariantSelectionSB;

public class VariantSelection {
	private static VariantSelection sharedInstance = null;
	private ServiceLocator serviceLocator = null;
	
	private static Category LOGGER = LoggerFactory.getInstance(VariantSelection.class);

	private VariantSelection() throws NamingException {
		serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
	}

	private VariantSelectionHome getVariantSelectionHome() {
		try {
			return (VariantSelectionHome) serviceLocator.getRemoteHome(
				"freshdirect.smartstore.VariantSelection", VariantSelectionHome.class);
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
	}


	/**
	 * Get shared instance.
	 * @return instance
	 */
	synchronized public static VariantSelection getInstance() {
		if (sharedInstance == null) {
			try {
				sharedInstance = new VariantSelection();
			} catch (NamingException e) {
				throw new FDRuntimeException(e,"Could not create variant selection helper shared instance");
			}
		}
		return sharedInstance;
	}



	/**
	 * Returns the map of variant selection
	 * @return
	 */
	public Map getVariantMap(EnumSiteFeature feature) {
		try {
			VariantSelectionSB bean = this.getVariantSelectionHome().create();
			
			return bean.getVariantMap(feature);
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection",e);
		} catch (CreateException e) {
			LOGGER.warn("Variant selection",e);
		}
		return Collections.EMPTY_MAP;
	}
	
	/**
	 * Returns the set of (cohort ID, weight) couples
	 * @return
	 */
	public Map getCohorts() {
		try {
			VariantSelectionSB bean = this.getVariantSelectionHome().create();
			
			return bean.getCohorts();
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection",e);
		} catch (CreateException e) {
			LOGGER.warn("Variant selection",e);
		}
		return Collections.EMPTY_MAP;
	}
	

	public List getVariants(EnumSiteFeature feature) {
		try {
			VariantSelectionSB bean = this.getVariantSelectionHome().create();
			
			return bean.getVariants(feature);
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection",e);
		} catch (CreateException e) {
			LOGGER.warn("Variant selection",e);
		}
		return Collections.EMPTY_LIST;
	}
}
