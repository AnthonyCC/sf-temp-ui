package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.erp.model.NotificationModel;

public interface PostSettlementNotifySB extends EJBObject {

	void updateNotification(NotificationModel notificationModel) throws RemoteException, EJBException, CreateException, ErpTransactionException, SQLException,FinderException;
	Collection<String> findByStatusAndType(EnumSaleStatus saleStatus, EnumNotificationType type) throws RemoteException, EJBException, FinderException, SQLException;
	NotificationModel findBySalesIdAndType(String salesId, EnumNotificationType avalara) throws RemoteException, SQLException, FinderException;
	List<String> commitToAvalara(Collection<String> pendingNotifications) throws RemoteException,FinderException;
}
