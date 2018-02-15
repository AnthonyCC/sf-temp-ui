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

	public boolean smsOptIn(String customerId,String mobileNumber, String eStoreId) throws RemoteException;
	public boolean smsOptInNonMarketing(String customerId,String mobileNumber, String eStoreId) throws RemoteException;
	public boolean smsOptInMarketing(String customerId,String mobileNumber, String eStoreId) throws RemoteException;
	public void expireOptin() throws RemoteException;
	public void updateSmsReceived(String mobileNumber, String shortCode, String carrierName, Date receivedDate, String message, EnumEStoreId eStoreId) throws RemoteException;
	public List<STSmsResponse> sendSmsToGateway(List<SmsAlertETAInfo> etaInfoList) throws RemoteException;
	public boolean smsOrderCancel(String customerId, String mobileNumber, String orderId, String eStoreId) throws RemoteException;
	public boolean smsOrderConfirmation(String customerId, String mobileNumber, String orderId, String eStoreId) throws RemoteException;
	public boolean smsOrderModification(String customerId, String mobileNumber, String orderId, String eStoreId) throws RemoteException;
	
}
