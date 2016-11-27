package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.sap.ejb.SapException;

public interface StandingOrder3CronSB extends EJBObject{

	public List<String> queryForDeactivatingTimeslotEligible() throws RemoteException;
	public void removeTimeSlotInfoFromSO(List<String> soIds) throws SQLException,RemoteException;
	
	public void removeSOfromLogistics(List<String> soIds) throws FinderException, RemoteException,
	ErpTransactionException, SapException;
	
}
