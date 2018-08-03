package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.standingorders.SOResult;
import com.freshdirect.fdstore.standingorders.StandingOrdersJobConfig;
import com.freshdirect.fdstore.standingorders.UnavDetailsReportingBean;


public interface StandingOrdersServiceI {
	public SOResult.ResultList placeStandingOrders(Collection<String> soList, StandingOrdersJobConfig jobConfig)throws RemoteException;
	public void persistUnavDetailsToDB(SOResult.ResultList resultCounter) throws FDResourceException, RemoteException;
	public UnavDetailsReportingBean getDetailsForReportGeneration() throws FDResourceException, RemoteException;
	
}
