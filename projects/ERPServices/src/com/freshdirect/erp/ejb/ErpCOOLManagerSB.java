package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.content.attributes.AttributeException;

public interface ErpCOOLManagerSB extends EJBObject {

	public void updateCOOLInfo(List erpCOOLInfo) throws RemoteException;
	public Map load(Date lastModified) throws RemoteException;

}

