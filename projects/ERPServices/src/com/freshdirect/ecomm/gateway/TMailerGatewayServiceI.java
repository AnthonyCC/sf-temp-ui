package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.framework.mail.TEmailI;

public interface TMailerGatewayServiceI {

	public void enqueue(TEmailI email) throws RemoteException;

	public void notifyFailedTransactionEmails() throws RemoteException;

}
