package com.freshdirect.fdstore.ecoupon;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface FDCouponManagerHome extends EJBHome {

	public FDCouponManagerSB create() throws CreateException, RemoteException;
}
