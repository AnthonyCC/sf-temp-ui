package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
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
	 * Returns the map of variant selection for a given date
	 * @return
	 */
	public Map<String, String> getVariantMap(EnumSiteFeature feature, Date date) {
		try {
			VariantSelectionSB bean = this.getVariantSelectionHome().create();
			
			return bean.getVariantMap(feature, date);
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection",e);
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			LOGGER.warn("Variant selection",e);
			throw new FDRuntimeException(e);
		}
	}

	/**
	 * Returns the map of variant selection
	 * @return
	 */
        public Map<String, String> getVariantMap(EnumSiteFeature feature) {
		try {
			VariantSelectionSB bean = this.getVariantSelectionHome().create();
			
			return bean.getVariantMap(feature);
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection",e);
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			LOGGER.warn("Variant selection",e);
			throw new FDRuntimeException(e);
		}
	}
	
	/**
	 * Returns the set of (cohort ID, weight) couples
	 * @return
	 */
	public Map<String, Integer> getCohorts() {
		try {
			VariantSelectionSB bean = this.getVariantSelectionHome().create();
			
			return bean.getCohorts();
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection", e);
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			LOGGER.warn("Variant selection", e);
			throw new FDRuntimeException(e);
		}
	}
	

	public List<String> getCohortNames() {
		try {
                    VariantSelectionSB bean = this.getVariantSelectionHome().create();
        
                    List<String> names = bean.getCohortNames();
    		    return names;
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection", e);
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			LOGGER.warn("Variant selection", e);
			throw new FDRuntimeException(e);
		}
	}

	public List<String> getVariants(EnumSiteFeature feature) {
		try {
			VariantSelectionSB bean = this.getVariantSelectionHome().create();
			
			return bean.getVariants(feature);
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection",e);
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			LOGGER.warn("Variant selection",e);
			throw new FDRuntimeException(e);
		}
	}

	/**
	 * Returns a list of start dates in cohort-variant assignment (history dates)
	 * @return
	 */
	public List<Date> getStartDates() {
		try {
			VariantSelectionSB bean = this.getVariantSelectionHome().create();
			
			return bean.getStartDates();
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection",e);
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			LOGGER.warn("Variant selection",e);
			throw new FDRuntimeException(e);
		}
	}
}
