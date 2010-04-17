package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpActivityRecord;

public interface ActivityLogSB extends EJBObject {
	
    public Collection<ErpActivityRecord> findActivityByTemplate(ErpActivityRecord template) throws RemoteException;

    public void logActivity(ErpActivityRecord rec) throws RemoteException;
    
    public void logActivityNewTX(ErpActivityRecord rec) throws RemoteException;
    
}
