package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.erp.ErpCOOLInfo;

public interface ErpCOOLManagerSB extends EJBObject {

	public void updateCOOLInfo(List<ErpCOOLInfo> erpCOOLInfo) throws RemoteException;
	public Map<String, ErpCOOLInfo> load(Date lastModified) throws RemoteException;

}

