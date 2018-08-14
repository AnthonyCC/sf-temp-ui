package com.freshdirect.ecomm.gateway;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.EnumEStoreId;

public interface DlvPassManagerServiceI {
	
	public String create(DeliveryPassModel model, EnumEStoreId eStore, String fdPk) throws RemoteException, DeliveryPassException;
	
	public void apply(DeliveryPassModel model) throws DeliveryPassException, RemoteException;
	
	public void revoke(DeliveryPassModel model)throws RemoteException;
	
	public String modify(String purchaseOrderId, DeliveryPassModel newPass, EnumEStoreId eStore, String fdPk) throws DeliveryPassException, RemoteException;
	
	public void cancel(DeliveryPassModel dlvPassModel, EnumEStoreId eStore, String fdPk) throws RemoteException;
	
	public void reactivate(DeliveryPassModel dlvPassModel)throws RemoteException;
	
	public void creditDelivery(DeliveryPassModel dlvPassModel, int delta) throws RemoteException;
	
	public void extendExpirationPeriod(DeliveryPassModel dlvPassModel, int noOfdays)  throws RemoteException;
	
	public List<DeliveryPassModel> getDeliveryPasses(String customerPk, EnumEStoreId estore) throws RemoteException;
	
	public List<DeliveryPassModel> getDlvPassesByStatus(String customerPk, EnumDlvPassStatus status, EnumEStoreId eStoreId) throws RemoteException;
	
	public DeliveryPassModel getDeliveryPassInfo(String deliveryPassId) throws RemoteException;
	
	public List<DeliveryPassModel> getDlvPassesByOrderId(String orderId) throws RemoteException;
	
	public void applyNew(DeliveryPassModel model) throws RemoteException;
	
	public void remove(DeliveryPassModel model, EnumEStoreId eStore, String fdPk) throws RemoteException;
	
	public Map<Comparable, Serializable> getAllStatusMap(String customerPk) throws RemoteException;
	
	public void updatePrice(DeliveryPassModel dlvPassModel, double newPrice) throws RemoteException;
	
	public void activateReadyToUsePass(DeliveryPassModel dlvPassModel) throws RemoteException;
	
	public void revoke(DeliveryPassModel appliedPass, DeliveryPassModel activePass) throws RemoteException;
	
	public boolean hasPurchasedPass(String customerPK) throws RemoteException;
	
	public List<DeliveryPassModel> getUsableAutoRenewPasses(String customerPK )throws RemoteException;

	public  Object[] getAutoRenewalInfo(EnumEStoreId eStore)throws RemoteException;

	public int getDaysSinceDPExpiry(String customerID,EnumEStoreId eStore) throws RemoteException;

	public int getDaysToDPExpiry(String customerID, String activeDPID)throws RemoteException;
	
	public List<List<String>> getPendingPasses(EnumEStoreId eStore)throws RemoteException;
	
	public List<String> getAllCustIdsOfFreeTrialSubsOrder() throws RemoteException;
	
	public void updateDeliveryPassActivation(String saleId) throws RemoteException;


}
