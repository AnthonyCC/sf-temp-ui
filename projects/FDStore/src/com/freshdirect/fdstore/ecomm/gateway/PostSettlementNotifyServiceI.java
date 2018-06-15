package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.erp.model.NotificationModel;

public interface PostSettlementNotifyServiceI {

	public void updateNotification(NotificationModel notificationModel) throws RemoteException, ErpTransactionException ;
	public Collection<String> findByStatusAndType(EnumSaleStatus saleStatus, EnumNotificationType type) throws RemoteException, SQLException;
	public NotificationModel findBySalesIdAndType(String salesId, EnumNotificationType avalara) throws RemoteException, SQLException ;
	public List<String> commitToAvalara(Collection<String> pendingNotifications) throws RemoteException;

}
