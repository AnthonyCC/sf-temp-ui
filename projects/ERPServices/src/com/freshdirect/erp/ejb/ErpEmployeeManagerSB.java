package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface ErpEmployeeManagerSB extends EJBObject {
	
	public List getEmployees() throws RemoteException;

}

