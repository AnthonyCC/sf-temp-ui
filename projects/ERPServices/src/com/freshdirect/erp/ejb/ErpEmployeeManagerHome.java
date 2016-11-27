package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;

public interface ErpEmployeeManagerHome extends EJBHome {

	public ErpEmployeeManagerSB create() throws CreateException, EJBException, RemoteException;
}
