package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.ejb.DynamicSiteFeature;

public interface SmartStoreConfigurationServiceI {

	/**
	 * Get the available variants for the requested feature.
	 * 
	 * @param feature
	 * @return available variants, {@link Collection}<{@link Variant}>
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection<Variant> getVariants(EnumSiteFeature feature) throws RemoteException,
			SQLException;

	/**
	 * Get the available site features stored in database
	 * 
	 * @return available site features , {@link DynamicSiteFeature}<
	 *         {@link DynamicSiteFeature}>
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection<DynamicSiteFeature> getSiteFeatures(final String eStoreId) throws RemoteException, SQLException;
	
}
