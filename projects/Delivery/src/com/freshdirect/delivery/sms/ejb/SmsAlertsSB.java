package com.freshdirect.delivery.sms.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.delivery.sms.SmsAlertETAInfo;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.sms.model.st.STSmsResponse;

/**
 *@deprecated Please use the SmsAlertController and SmsAlertsServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */ 
public interface SmsAlertsSB extends EJBObject{

	@Deprecated
	public boolean smsOptIn(String customerId,String mobileNumber, String eStoreId) throws RemoteException;
	@Deprecated
	public boolean smsOptInNonMarketing(String customerId,String mobileNumber, String eStoreId) throws RemoteException;
	@Deprecated
	public boolean smsOptInMarketing(String customerId,String mobileNumber, String eStoreId) throws RemoteException;
	@Deprecated
	public void expireOptin() throws RemoteException;
	@Deprecated
	public void updateSmsReceived(String mobileNumber, String shortCode, String carrierName, Date receivedDate, String message, EnumEStoreId eStoreId) throws RemoteException;
	@Deprecated
	public List<STSmsResponse> sendSmsToGateway(List<SmsAlertETAInfo> etaInfoList) throws RemoteException;
	@Deprecated
	public boolean smsOrderCancel(String customerId, String mobileNumber, String orderId, String eStoreId) throws RemoteException;
	@Deprecated
	public boolean smsOrderConfirmation(String customerId, String mobileNumber, String orderId, String eStoreId) throws RemoteException;
	@Deprecated
	public boolean smsOrderModification(String customerId, String mobileNumber, String orderId, String eStoreId) throws RemoteException;
	
}
