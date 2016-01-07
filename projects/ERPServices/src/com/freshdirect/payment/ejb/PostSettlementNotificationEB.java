package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.erp.model.NotificationModel;


public interface PostSettlementNotificationEB extends EJBObject {

	public void updateNotification(NotificationModel notificationModel) throws ErpTransactionException, RemoteException, SQLException;
	public NotificationModel getModel() throws RemoteException;
}
