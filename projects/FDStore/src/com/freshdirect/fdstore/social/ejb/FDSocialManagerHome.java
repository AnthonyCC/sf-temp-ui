package com.freshdirect.fdstore.social.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;


public interface FDSocialManagerHome extends EJBHome {
	
	public FDSocialManagerSB create() throws CreateException, RemoteException;

}
