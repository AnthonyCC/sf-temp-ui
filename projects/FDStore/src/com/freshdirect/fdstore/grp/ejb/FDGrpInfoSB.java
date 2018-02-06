package com.freshdirect.fdstore.grp.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.fdstore.FDGroup;
/**
 *@deprecated Please use the ERPGroupScaleController and ErpGrpInfoServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface FDGrpInfoSB extends EJBObject{
	@Deprecated
    public Collection<FDGroup> loadAllGrpInfoMaster() throws RemoteException;
	@Deprecated
    public int getLatestVersionNumber(String grpId) throws RemoteException;   


}
