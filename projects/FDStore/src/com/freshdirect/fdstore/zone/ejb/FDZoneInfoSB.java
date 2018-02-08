package com.freshdirect.fdstore.zone.ejb;


import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBObject;
import javax.ejb.ObjectNotFoundException;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpProductInfoModel;

/**
 * the remote interface for the ErpMaterialInfo session bean
 *
 *
 * @version $Revision$
 * @author $Author$
 */
/**
 *@deprecated Please use the ZoneInfoController and ZoneInfoServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface FDZoneInfoSB extends EJBObject {
	@Deprecated
	public ErpZoneMasterInfo findZoneInfoMaster(String zoneId) throws RemoteException;
	@Deprecated
    public Collection loadAllZoneInfoMaster() throws RemoteException;       
	@Deprecated
    public  String findZoneId(String serviceType,String zipCode) throws RemoteException;
	@Deprecated
    public  String findZoneId(String serviceType,String zipCode,boolean isPickupOnlyORNotServicebleZip) throws RemoteException;
          
}