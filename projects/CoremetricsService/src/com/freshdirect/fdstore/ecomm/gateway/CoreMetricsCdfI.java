package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.service.CdfProcessResult;

public interface CoreMetricsCdfI {

	@Deprecated
	public CdfProcessResult processCdf() throws RemoteException;
	public CdfProcessResult processCdf(CmContext ctx) throws RemoteException;
}
