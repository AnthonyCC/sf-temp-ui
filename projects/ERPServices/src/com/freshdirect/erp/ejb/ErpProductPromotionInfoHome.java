package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ErpProductPromotionInfoHome extends EJBHome{

	public ErpProductPromotionInfoSB create() throws CreateException, RemoteException;
}
