package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface FDXOrderPickEligibleCronSB extends EJBObject{ 

	public void queryForSalesPickEligible() throws RemoteException;

}