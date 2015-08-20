package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * ErpClass entity home interface.
 *
 * @author kkanuganti
 */
@SuppressWarnings("javadoc")
public interface ErpClassHome extends EJBHome {

	/**
	 * Create from model with specified version.
	 */
	public ErpClassEB create(int version, ModelI model) throws CreateException, RemoteException;
	
	public ErpClassEB findByPrimaryKey(VersionedPrimaryKey vpk) throws FinderException, RemoteException;

	public ErpClassEB findBySapId(String sapId) throws FinderException, RemoteException;

	public ErpClassEB findBySapIdAndVersion(String sapId, int version) throws FinderException, RemoteException;

	public Collection findAllClasses() throws FinderException, RemoteException;

}

