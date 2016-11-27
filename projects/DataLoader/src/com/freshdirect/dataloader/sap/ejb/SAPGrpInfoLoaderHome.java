package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;

public interface SAPGrpInfoLoaderHome extends EJBHome{

	 /** creates a new SAPLoader session bean
     * @throws CreateException any problems creating a bean for use
     * @throws EJBException any unexpected container-related problems
     * @throws RemoteException any unexpected system or communications related problems
     * @return a remote SAPLoader session bean
     */    
    public SapGrpInfoLoaderSB create() throws CreateException, EJBException, RemoteException;
}
