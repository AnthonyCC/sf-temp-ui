package com.freshdirect.fdstore.ecoupon;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface FDCouponActivityLogHome extends EJBHome {

	public FDCouponActivityLogSB create() throws CreateException, RemoteException;
}
