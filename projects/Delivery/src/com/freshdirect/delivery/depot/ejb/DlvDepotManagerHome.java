/*
 * 
 * DlvDepotManagerHome.java
 * Date: Jul 29, 2002 Time: 7:48:44 PM
 */
package com.freshdirect.delivery.depot.ejb;

/**
 * 
 * @author knadeem
 */
import javax.ejb.*;
import java.rmi.RemoteException;

public interface DlvDepotManagerHome extends EJBHome {
	
	public DlvDepotManagerSB create() throws CreateException, RemoteException;

}
