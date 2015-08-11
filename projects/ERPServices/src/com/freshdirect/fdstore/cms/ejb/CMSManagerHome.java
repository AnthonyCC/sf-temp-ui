package com.freshdirect.fdstore.cms.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface CMSManagerHome extends EJBHome {
   public CMSManagerSB create() throws CreateException, RemoteException;
}
