package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
/**
 * 
 * @author ksriram
 *
 */
public interface SAPProductPromotionLoaderHome extends EJBHome {

	public SAPProductPromotionLoaderSB create() throws CreateException, EJBException, RemoteException;
}
