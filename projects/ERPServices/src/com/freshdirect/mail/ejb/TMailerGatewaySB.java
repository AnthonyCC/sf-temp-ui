package com.freshdirect.mail.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.framework.mail.TEmailI;

public interface TMailerGatewaySB extends EJBObject {

	public void enqueue(TEmailI email) throws RemoteException;
	
	public void notifyFailedTransactionEmails() throws RemoteException;
		
}
