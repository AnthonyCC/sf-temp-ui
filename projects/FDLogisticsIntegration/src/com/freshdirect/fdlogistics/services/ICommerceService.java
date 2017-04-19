package com.freshdirect.fdlogistics.services;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.fdstore.FDResourceException;

public interface ICommerceService {

	public void saveCountryOfOriginData(List<ErpCOOLInfo> cooList) throws RemoteException, LoaderException;
	
	public void getCountryOfOriginData() throws RemoteException, LoaderException;

	public void healthCheck()throws RemoteException;
	public boolean ping() throws RemoteException;
	public List<Long> getDYFEligibleCustomerIDs() throws RemoteException;
	public List<Long> getErpCustomerIds() throws RemoteException;
	public String getFDCustomerIDForErpId(String erpCustomerPK) throws RemoteException;
	public String getErpIDForUserID(String userID) throws RemoteException;
	public Collection<String> getSkuCodes() throws RemoteException;
	public BatchModel getBatch(int versionID) throws FDResourceException;
	public void loadData(List<ErpZoneMasterInfo> zoneInfos)throws RemoteException, LoaderException;
	public String createFeedCmsFeed(String feedId, String storeId, String feedData) throws FDResourceException;
	public String getCmsFeed(String storeID) throws FDResourceException;
	
	//DYFMODELSERVICES

	public Set<String> getDYFModelProducts(String customerID) throws FDResourceException,  RemoteException;
	public  Map<String, Float>getDYFModelProductFrequencies(String customerID)throws FDResourceException;

	public Map<String , Float> getDYFModelGlobalProductscores() throws FDResourceException, RemoteException;
	// Map<ContentKey, Float>getDYFModelProductFrequencies(String customerID)
	

} 