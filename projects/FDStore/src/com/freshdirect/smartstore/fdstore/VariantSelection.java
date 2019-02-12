package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.ecommerce.data.smartstore.EnumSiteFeatureData;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class VariantSelection {
	private static VariantSelection sharedInstance = null;

	private static Category LOGGER = LoggerFactory.getInstance(VariantSelection.class);

	/**
	 * Get shared instance.
	 * 
	 * @return instance
	 */
	public static synchronized VariantSelection getInstance() {
		if (sharedInstance == null) {

			sharedInstance = new VariantSelection();

		}
		return sharedInstance;
	}

	/**
	 * Returns the map of variant selection for a given date
	 * 
	 * @return
	 */
	public Map<String, String> getVariantMap(EnumSiteFeature feature, Date date) {
		try {
			return FDECommerceService.getInstance().getVariantMap(buildEnumFeatureData(feature), date);
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection", e);
			throw new FDRuntimeException(e);
		}
	}

	/**
	 * Returns the map of variant selection
	 * 
	 * @return
	 */
	public Map<String, String> getVariantMap(EnumSiteFeature feature) {
		try {
			return FDECommerceService.getInstance().getVariantMap(buildEnumFeatureData(feature));
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection", e);
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
	 * 
	 * @return
	 */
	public Map<String, Integer> getCohorts() {
		try {
			return FDECommerceService.getInstance().getCohorts();
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection", e);
			throw new FDRuntimeException(e);
		}
	}

	public List<String> getCohortNames() {
		List<String> names = null;
		try {
			names = FDECommerceService.getInstance().getCohortNames();
			return names;
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection", e);
			throw new FDRuntimeException(e);
		}
	}

	public List<String> getVariants(EnumSiteFeature feature) {
		try {
			return FDECommerceService.getInstance().getVariants(buildEnumFeatureData(feature));
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection", e);
			throw new FDRuntimeException(e);
		}
	}

	/**
	 * Returns a list of start dates in cohort-variant assignment (history dates)
	 * 
	 * @return
	 */
	public List<Date> getStartDates() {
		try {
			return FDECommerceService.getInstance().getStartDates();
		} catch (RemoteException e) {
			LOGGER.warn("Variant selection", e);
			throw new FDRuntimeException(e);
		}
	}
}
