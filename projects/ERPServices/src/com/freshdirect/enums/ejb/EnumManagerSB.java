package com.freshdirect.enums.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

/**
 *@deprecated Please use the EnumManagerController and EnumManagerServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface EnumManagerSB extends EJBObject {
	 @Deprecated
	public List loadEnum(String daoClassName) throws RemoteException;

}
