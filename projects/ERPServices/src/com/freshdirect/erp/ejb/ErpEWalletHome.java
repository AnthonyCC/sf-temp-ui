package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author Aniwesh Vatsal
 *
 */
public interface ErpEWalletHome extends EJBHome{
	
	public ErpEWalletSB create() throws CreateException, RemoteException;

}
