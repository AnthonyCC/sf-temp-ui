package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;

public interface StandingOrder3CronServiceI {

	public List<String> queryForDeactivatingTimeslotEligible() throws FDResourceException, RemoteException;
	public void removeTimeSlotInfoFromSO(List<String> soIds) throws FDResourceException, RemoteException;
	public void removeSOfromLogistics(List<String> soIds) throws FDResourceException, RemoteException;
}
