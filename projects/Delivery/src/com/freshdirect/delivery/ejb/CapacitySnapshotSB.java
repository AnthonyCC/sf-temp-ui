package com.freshdirect.delivery.ejb;

/**
*
* @author  tbalumuri
* @version 
*/
import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJBObject;

public interface CapacitySnapshotSB extends EJBObject {
	
    public void capture(Date startDate, Date endDate) throws RemoteException;
	}   
