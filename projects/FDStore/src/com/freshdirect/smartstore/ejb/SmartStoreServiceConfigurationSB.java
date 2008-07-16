package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.Variant;

/**
 * Service configuration session bean interface.
 * @author istvan
 *
 */
public interface SmartStoreServiceConfigurationSB extends EJBObject {
	
	/**
	 * Get the available variants for the requested feature.
	 * @param feature
	 * @return available variants, {@link Collection}<{@link Variant}>
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection getVariants(EnumSiteFeature feature) throws RemoteException, SQLException;

}
