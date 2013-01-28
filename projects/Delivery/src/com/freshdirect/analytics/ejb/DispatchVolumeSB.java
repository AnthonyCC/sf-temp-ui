package com.freshdirect.analytics.ejb;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJBObject;

public interface DispatchVolumeSB extends EJBObject{ 

	public void captureDispatchVolume(Date timeStamp) throws RemoteException;
	

}
