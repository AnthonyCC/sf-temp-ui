package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.mail.EnumTranEmailType;

public interface TEmailInfoServiceI {

	/**
	 * do inventory checks for all skus in a department
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public void sendEmail(EnumTranEmailType tranType,Map input) throws FDResourceException, RemoteException;

    //public FDIdentity getRandomCustomerIdentity() throws FDResourceException, RemoteException;
	
	public int sendFailedTransactions(int timeout) throws RemoteException;
	
	public List getFailedTransactionList(int max_count,boolean isMailContentReqd) throws RemoteException;
	
	public Map getFailedTransactionStats() throws RemoteException;

}
