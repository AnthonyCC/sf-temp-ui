package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.Variant;

/**
 *@deprecated Please use the SmartStoreServiceConfigurationController and SmartStoreConfigurationServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface SmartStoreServiceConfigurationSB extends EJBObject {
	/**
	 * Get the available variants for the requested feature.
	 * 
	 * @param feature
	 * @return available variants, {@link Collection}<{@link Variant}>
	 * @throws RemoteException
	 * @throws SQLException
	 */
	 @Deprecated
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
	 @Deprecated
	public Collection<DynamicSiteFeature> getSiteFeatures(final String eStoreId) throws RemoteException, SQLException;
}
