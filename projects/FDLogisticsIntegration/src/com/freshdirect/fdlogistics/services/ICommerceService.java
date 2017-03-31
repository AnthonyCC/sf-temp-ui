package com.freshdirect.fdlogistics.services;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.erp.ErpCOOLInfo;

public interface ICommerceService {

	public void savePricingZoneData(List<ErpZoneMasterInfo> zoneInfoList) throws RemoteException, LoaderException;
	
	public void saveCountryOfOriginData(List<ErpCOOLInfo> cooList) throws RemoteException, LoaderException;
	
	public void getCountryOfOriginData() throws RemoteException, LoaderException;

	public void healthCheck()throws RemoteException;
	public boolean ping() throws RemoteException;
	public List<Long> getDYFEligibleCustomerIDs() throws RemoteException;
	public List<Long> getErpCustomerIds() throws RemoteException;
	public String getFDCustomerIDForErpId(String erpCustomerPK) throws RemoteException;
	public String getErpIDForUserID(String userID) throws RemoteException;
	public Collection<String> getSkuCodes() throws RemoteException;
} 