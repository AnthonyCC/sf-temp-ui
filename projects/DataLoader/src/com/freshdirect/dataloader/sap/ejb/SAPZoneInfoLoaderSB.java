package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.dataloader.LoaderException;

/**
 * the remote interface for the SAPLoader session bean
 *
 *
 * @version $Revision$
 * @author $Author$
 */

/**
 *@deprecated Please use the ErpZoneInfoController and ErpZoneInfoLoaderI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface SAPZoneInfoLoaderSB extends EJBObject {

	/**
	 * @param zoneInfoList
	 * @throws RemoteException
	 * @throws LoaderException
	 */
	@Deprecated
	public void loadData(List<ErpZoneMasterInfo> zoneInfoList) throws RemoteException, LoaderException;

}

