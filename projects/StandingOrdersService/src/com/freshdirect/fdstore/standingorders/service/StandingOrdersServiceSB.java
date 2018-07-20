package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;
import java.util.Collection;


import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.standingorders.SOResult;
import com.freshdirect.fdstore.standingorders.StandingOrdersJobConfig;
import com.freshdirect.fdstore.standingorders.UnavDetailsReportingBean;

public interface StandingOrdersServiceSB extends EJBObject {
	public SOResult.ResultList placeStandingOrders(Collection<String> soList, StandingOrdersJobConfig jobConfig)throws RemoteException;
	public void persistUnavDetailsToDB(SOResult.ResultList resultCounter) throws FDResourceException, RemoteException;
	public UnavDetailsReportingBean getDetailsForReportGeneration() throws FDResourceException, RemoteException;
	
}
