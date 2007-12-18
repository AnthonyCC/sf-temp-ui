/*
 * Interface.java
 *
 * Created on November 27, 2001, 11:06 AM
 */

package com.freshdirect.delivery.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import javax.ejb.*;
import java.rmi.RemoteException;

public interface DlvTemplateManagerHome extends EJBHome {
	
	public DlvTemplateManagerSB create() throws CreateException, RemoteException;

}

