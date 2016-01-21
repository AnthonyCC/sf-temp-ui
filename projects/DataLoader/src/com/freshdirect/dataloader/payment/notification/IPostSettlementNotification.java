package com.freshdirect.dataloader.payment.notification;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.erp.model.NotificationModel;

public interface IPostSettlementNotification {

	public void Notify() throws RemoteException, EJBException, FinderException, ErpTransactionException, CreateException, SQLException;
	public boolean saveNoification(NotificationModel notificationModel) throws RemoteException, EJBException, CreateException, ErpTransactionException, SQLException, FinderException;
}
