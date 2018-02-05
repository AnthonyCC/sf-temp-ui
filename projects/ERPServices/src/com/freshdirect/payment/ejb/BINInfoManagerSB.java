package com.freshdirect.payment.ejb;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.payment.BINInfo;

/**
 *@deprecated Please use the BINController and BINServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface BINInfoManagerSB extends EJBObject{
	
	@Deprecated
	public void saveBINInfo( List<List<BINInfo>> binInfos) throws FDResourceException, RemoteException;
	@Deprecated
	public NavigableMap<Long, BINInfo> getActiveBINs() throws FDResourceException, RemoteException;
	
}