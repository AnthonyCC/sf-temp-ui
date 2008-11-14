package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

public interface DyfModelSB extends EJBObject {
	public Map getProductFrequencies(String customerID) throws RemoteException;
	public List getProducts(String customerID) throws RemoteException;
	public Map getGlobalProductScores() throws RemoteException;
}
