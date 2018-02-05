package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.dataloader.payment.ProfileCreatorResult;
import com.freshdirect.fdstore.FDResourceException;

/**
 *@deprecated Please use the ProfileCreatorController and ProfileCreatorServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface ProfileCreatorSB extends EJBObject {
	@Deprecated
	public void createProfiles(String batchId) throws SQLException, FDResourceException,RemoteException;

}
