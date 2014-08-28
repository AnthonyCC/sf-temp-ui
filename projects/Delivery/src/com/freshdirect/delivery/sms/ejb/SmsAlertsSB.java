package com.freshdirect.delivery.sms.ejb;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJBObject;

public interface SmsAlertsSB extends EJBObject{

	
	public boolean smsOptIn(String customerId,String mobileNumber) throws RemoteException;
	public void expireOptin() throws RemoteException;
	public void updateSmsReceived(String mobileNumber, String shortCode, String carrierName, Date receivedDate, String message) throws RemoteException;
	public void sendNextStopSms() throws RemoteException;
	public void sendETASms() throws RemoteException;
	public void sendUnattendedDoormanDlvSms() throws RemoteException;
	public void sendDlvAttemptedSms() throws RemoteException;
}
