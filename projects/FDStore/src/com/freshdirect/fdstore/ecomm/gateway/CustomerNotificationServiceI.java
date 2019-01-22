package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.PasswordNotExpiredException;
import com.freshdirect.framework.mail.XMLEmailI;

public interface CustomerNotificationServiceI {

	public boolean sendPasswordEmail(String emailAddress, boolean isAltEmail)
			throws RemoteException, FDResourceException, PasswordNotExpiredException;

	public List<String> getReminderListForToday() throws FDResourceException, RemoteException;

	public void sendReminderEmail(String custId) throws FDResourceException, RemoteException;

	public boolean iPhoneCaptureEmail(String email, String zipCode, String serviceType)
			throws FDResourceException, RemoteException;

	public void sendSettlementFailedEmail(String saleID) throws FDResourceException, RemoteException;

	public String getIdByEmail(String email) throws FDResourceException, RemoteException;

	public boolean isOnAlert(String pk, String alertType) throws RemoteException;
	
	public void doEmail(XMLEmailI email) throws RemoteException, FDResourceException;

	public boolean resendInvoiceEmail(String orderId) throws FDResourceException, RemoteException;
	
}
