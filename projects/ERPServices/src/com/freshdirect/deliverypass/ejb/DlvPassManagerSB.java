package com.freshdirect.deliverypass.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.EnumDlvPassStatus;

public interface DlvPassManagerSB extends EJBObject {
	public String create(DeliveryPassModel model) throws RemoteException, DeliveryPassException;

	public void apply(DeliveryPassModel model) throws DeliveryPassException, RemoteException;

	public void revoke(DeliveryPassModel model)throws RemoteException;

	public String modify(String purchaseOrderId, DeliveryPassModel newPass) throws DeliveryPassException, RemoteException;

	public void cancel(DeliveryPassModel dlvPassModel) throws RemoteException;

	public void reactivate(DeliveryPassModel dlvPassModel)throws RemoteException;

	public void creditDelivery(DeliveryPassModel dlvPassModel, int delta) throws RemoteException;

	public void extendExpirationPeriod(DeliveryPassModel dlvPassModel, int noOfdays)  throws RemoteException;

	public List<DeliveryPassModel> getDeliveryPasses(String customerPk) throws RemoteException;

	public List<DeliveryPassModel> getDlvPassesByStatus(String customerPk, EnumDlvPassStatus status) throws RemoteException;

	public DeliveryPassModel getDeliveryPassInfo(String deliveryPassId) throws RemoteException;

	public List<DeliveryPassModel> getDlvPassesByOrderId(String orderId) throws RemoteException;

	public void applyNew(DeliveryPassModel model) throws RemoteException;

	public void remove(DeliveryPassModel model) throws RemoteException;

	public Map<Comparable, Serializable> getAllStatusMap(String customerPk) throws RemoteException;

	public void updatePrice(DeliveryPassModel dlvPassModel, double newPrice) throws RemoteException;

	public void activateReadyToUsePass(DeliveryPassModel dlvPassModel) throws RemoteException;

	public void revoke(DeliveryPassModel appliedPass, DeliveryPassModel activePass) throws RemoteException;

	public boolean hasPurchasedPass(String customerPK) throws RemoteException;

	public List<DeliveryPassModel> getUsableAutoRenewPasses(String customerPK )throws RemoteException;

	public  Object[] getAutoRenewalInfo()throws RemoteException;

	public int getDaysSinceDPExpiry(String customerID) throws RemoteException;

	public int getDaysToDPExpiry(String customerID, String activeDPID)throws RemoteException;

	public List<List<String>> getPendingPasses()throws RemoteException;

	public List<String> getAllCustIdsOfFreeTrialSubsOrder() throws RemoteException;

}

