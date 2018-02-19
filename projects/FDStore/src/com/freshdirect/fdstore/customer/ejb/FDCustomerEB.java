/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.fdstore.customer.ejb;

import java.rmi.RemoteException;
import java.util.Date;

import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.fdstore.customer.FDCustomerI;
import com.freshdirect.framework.core.EntityBeanRemoteI;

/**
 * FDCustomer remote interface.
 *
 * @version    $Revision$
 * @author     $Author$
 */
public interface FDCustomerEB extends EntityBeanRemoteI, FDCustomerI {

	public void setProfileAttribute(String name, String value) throws RemoteException;
	public void removeProfileAttribute(String name) throws RemoteException;
	public String generatePasswordRequest(java.util.Date expiration) throws RemoteException;
	public Date getPasswordRequestExpiration() throws RemoteException;
	public void erasePasswordRequest() throws RemoteException;
	public void updatePasswordHint(String s) throws RemoteException;
	public int incrementPasswordRequestAttempts() throws RemoteException;
	public String getDepotCode() throws RemoteException;
	public void setDepotCode(String depotCode) throws RemoteException;
	@Override
	public void setPymtVerifyAttempts(int pymtVerifyAttempts)  throws RemoteException;
	@Override
	public int getPymtVerifyAttempts()  throws RemoteException;
	@Override
	public void resetPymtVerifyAttempts() throws RemoteException;
	@Override
	public int incrementPymtVerifyAttempts() throws RemoteException;
    public EnumPaymentMethodDefaultType getDefaultPaymentMethodType() throws RemoteException;
	public void setDefaultPaymentMethodType(EnumPaymentMethodDefaultType defaultPaymentType) throws RemoteException;
    public void setFDCustomerEStore(FDCustomerEStoreModel fdCustomerEStoreModel) throws RemoteException;
}
