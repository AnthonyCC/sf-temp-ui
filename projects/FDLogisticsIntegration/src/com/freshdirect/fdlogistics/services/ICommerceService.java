package com.freshdirect.fdlogistics.services;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.dto.Customer;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.ReservationException;
import com.freshdirect.logistics.delivery.model.ReservationUnavailableException;

public interface ICommerceService {

	public void healthCheck()throws RemoteException;
	public boolean ping() throws RemoteException;
	public List<Long> getDYFEligibleCustomerIDs() throws RemoteException;
	public List<Long> getErpCustomerIds() throws RemoteException;
	public String getFDCustomerIDForErpId(String erpCustomerPK) throws RemoteException;
	public String getErpIDForUserID(String userID) throws RemoteException;
	public Collection<String> getSkuCodes() throws RemoteException;
	public void loadData(List<ErpZoneMasterInfo> zoneInfos)throws RemoteException;
	public String createFeedCmsFeed(String feedId, String storeId, String feedData) throws FDResourceException;
	public String getCmsFeed(String storeID) throws FDResourceException;
	
	//DYFMODELSERVICES

	public Set<String> getDYFModelProducts(String customerID) throws FDResourceException,  RemoteException;
	public  Map<String, Float>getDYFModelProductFrequencies(String customerID)throws FDResourceException;

	public Map<String , Float> getDYFModelGlobalProductscores() throws FDResourceException, RemoteException;
	// Map<ContentKey, Float>getDYFModelProductFrequencies(String customerID)

	public FDReservation reserveTimeslot(String timeslotId, String customerId,
			EnumReservationType type, Customer customer, boolean chefsTable,
			String ctDeliveryProfile, boolean isForced, TimeslotEvent event,
			boolean hasSteeringDiscount, String deliveryFeeTier)
			throws FDResourceException, ReservationUnavailableException, ReservationException;
	

} 