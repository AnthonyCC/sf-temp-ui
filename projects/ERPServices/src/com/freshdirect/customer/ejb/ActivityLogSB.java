package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpPaymentMethodI;
/**
 *@deprecated Please use the ActivityLogController and ActivityLogServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface ActivityLogSB extends EJBObject {
	 @Deprecated
    public Collection<ErpActivityRecord> findActivityByTemplate(ErpActivityRecord template) throws RemoteException;
	 @Deprecated
    public void logActivity(ErpActivityRecord rec) throws RemoteException;
	 @Deprecated
    public void logActivityNewTX(ErpActivityRecord rec) throws RemoteException;
	 @Deprecated
    public Map<String, List> getFilterLists(ErpActivityRecord template)throws RemoteException;
	 @Deprecated
    public Collection<ErpActivityRecord> getCCActivitiesByTemplate(ErpActivityRecord template) throws RemoteException;
    
}
