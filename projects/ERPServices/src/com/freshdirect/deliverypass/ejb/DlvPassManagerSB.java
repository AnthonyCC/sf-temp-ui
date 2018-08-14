package com.freshdirect.deliverypass.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.EnumEStoreId;
/**
 *@deprecated Please use the DlvPassManagerController and DlvPassManagerServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface DlvPassManagerSB extends EJBObject {
	@Deprecated
	public String create(DeliveryPassModel model, EnumEStoreId eStore, String fdPk) throws RemoteException, DeliveryPassException;
	@Deprecated
	public void apply(DeliveryPassModel model) throws DeliveryPassException, RemoteException;
	@Deprecated
	public void revoke(DeliveryPassModel model)throws RemoteException;
	@Deprecated
	public String modify(String purchaseOrderId, DeliveryPassModel newPass, EnumEStoreId eStore, String fdPk) throws DeliveryPassException, RemoteException;
	@Deprecated
	public void cancel(DeliveryPassModel dlvPassModel, EnumEStoreId eStore, String fdPk) throws RemoteException;
	@Deprecated
	public void reactivate(DeliveryPassModel dlvPassModel)throws RemoteException;
	@Deprecated
	public void creditDelivery(DeliveryPassModel dlvPassModel, int delta) throws RemoteException;
	@Deprecated
	public void extendExpirationPeriod(DeliveryPassModel dlvPassModel, int noOfdays)  throws RemoteException;
	@Deprecated
	public List<DeliveryPassModel> getDeliveryPasses(String customerPk, EnumEStoreId estore) throws RemoteException;
	@Deprecated
	public List<DeliveryPassModel> getDlvPassesByStatus(String customerPk, EnumDlvPassStatus status,EnumEStoreId eStore) throws RemoteException;
	@Deprecated
	public DeliveryPassModel getDeliveryPassInfo(String deliveryPassId) throws RemoteException;
	@Deprecated
	public List<DeliveryPassModel> getDlvPassesByOrderId(String orderId) throws RemoteException;
	@Deprecated
	public void applyNew(DeliveryPassModel model) throws RemoteException;
	@Deprecated
	public void remove(DeliveryPassModel model, EnumEStoreId eStore, String fdPk) throws RemoteException;
	@Deprecated
	public Map<Comparable, Serializable> getAllStatusMap(String customerPk, EnumEStoreId estore) throws RemoteException;
	@Deprecated
	public void updatePrice(DeliveryPassModel dlvPassModel, double newPrice) throws RemoteException;
	@Deprecated
	public void activateReadyToUsePass(DeliveryPassModel dlvPassModel) throws RemoteException;
	@Deprecated
	public void revoke(DeliveryPassModel appliedPass, DeliveryPassModel activePass) throws RemoteException;
	@Deprecated
	public boolean hasPurchasedPass(String customerPK) throws RemoteException;
	@Deprecated
	public List<DeliveryPassModel> getUsableAutoRenewPasses(String customerPK )throws RemoteException;
	@Deprecated
	public  Object[] getAutoRenewalInfo(EnumEStoreId eStore)throws RemoteException;
	@Deprecated
	public int getDaysSinceDPExpiry(String customerID,EnumEStoreId eStore) throws RemoteException;
	@Deprecated
	public int getDaysToDPExpiry(String customerID, String activeDPID)throws RemoteException;
	@Deprecated
	public List<List<String>> getPendingPasses(EnumEStoreId eStore)throws RemoteException;
	@Deprecated
	public List<String> getAllCustIdsOfFreeTrialSubsOrder() throws RemoteException;
	@Deprecated
	public void updateDeliveryPassActivation(String saleId) throws RemoteException;

}

