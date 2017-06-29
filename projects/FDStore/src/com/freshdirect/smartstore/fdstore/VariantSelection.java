package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ecommerce.data.smartstore.EnumSiteFeatureData;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class VariantSelection {
	private static VariantSelection sharedInstance = null;
	private FDServiceLocator serviceLocator = null;
	
	private static Category LOGGER = LoggerFactory.getInstance(VariantSelection.class);

	private VariantSelection() throws NamingException {
		serviceLocator = new FDServiceLocator(FDStoreProperties.getInitialContext());
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
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("smartstore.ejb.VariantSelectionSB")){
				return FDECommerceService.getInstance().getVariantMap(buildEnumFeatureData(feature),date);
			}
			else{
				return serviceLocator.getVariantSelectionSessionBean().getVariantMap(feature, date);
			}
		} catch (RemoteException e) {
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
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("smartstore.ejb.VariantSelectionSB")){
				return FDECommerceService.getInstance().getVariantMap(buildEnumFeatureData(feature));
			}
			else{
				return serviceLocator.getVariantSelectionSessionBean().getVariantMap(feature);
			}
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection",e);
			throw new FDRuntimeException(e);
		}
	}
	
	private EnumSiteFeatureData buildEnumFeatureData(EnumSiteFeature feature) {
		EnumSiteFeatureData enumSiteFeatureData = new EnumSiteFeatureData();
		enumSiteFeatureData.setName(feature.getName());
		enumSiteFeatureData.setPrez_desc(feature.getPresentationTitle());
		enumSiteFeatureData.setPrez_desc(feature.getPresentationDescription());
		enumSiteFeatureData.setSmartStore(feature.isSmartSavings());
		enumSiteFeatureData.setTitle(feature.getTitle());
		return enumSiteFeatureData;
	}


	/**
	 * Returns the set of (cohort ID, weight) couples
	 * @return
	 */
	public Map<String, Integer> getCohorts() {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("smartstore.ejb.VariantSelectionSB")){
				return FDECommerceService.getInstance().getCohorts();
			}
			else{
				return serviceLocator.getVariantSelectionSessionBean().getCohorts();
			}
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection", e);
			throw new FDRuntimeException(e);
		}
	}
	

	public List<String> getCohortNames() {
		List<String> names = null;
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("smartstore.ejb.VariantSelectionSB")){
				names =  FDECommerceService.getInstance().getCohortNames();
			}
			else{
                names = serviceLocator.getVariantSelectionSessionBean().getCohortNames();
			}
    		    return names;
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection", e);
			throw new FDRuntimeException(e);
		}
	}

	public List<String> getVariants(EnumSiteFeature feature) {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("smartstore.ejb.VariantSelectionSB")){
				return  FDECommerceService.getInstance().getVariants(buildEnumFeatureData(feature));
			}
			else{
				return serviceLocator.getVariantSelectionSessionBean().getVariants(feature);
			}
		} catch (RemoteException e) {
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
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("smartstore.ejb.VariantSelectionSB")){
				return  FDECommerceService.getInstance().getStartDates();
			}
			else{
				return serviceLocator.getVariantSelectionSessionBean().getStartDates();
			}
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection",e);
			throw new FDRuntimeException(e);
		}
	}
}
