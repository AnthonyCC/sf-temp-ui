package com.freshdirect.referral.extole;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

import com.freshdirect.payment.ejb.BINInfoManagerSB;

public interface FDExtoleManagerHome extends EJBHome {

	public FDExtoleManagerSB create() throws CreateException, RemoteException;
}
