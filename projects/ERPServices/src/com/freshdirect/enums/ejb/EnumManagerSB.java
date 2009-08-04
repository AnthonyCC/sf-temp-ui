package com.freshdirect.enums.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface EnumManagerSB extends EJBObject {

	public List loadEnum(String daoClassName) throws RemoteException;

}
