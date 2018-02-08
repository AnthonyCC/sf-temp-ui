package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
/**
 *@deprecated Please use the CountryOfOriginController and CountryOfOriginServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface ErpCOOLManagerSB extends EJBObject {
	@Deprecated
	public void updateCOOLInfo(List<ErpCOOLInfo> erpCOOLInfo) throws RemoteException;
	@Deprecated
	public Map<ErpCOOLKey, ErpCOOLInfo> load(Date lastModified) throws RemoteException;

}

