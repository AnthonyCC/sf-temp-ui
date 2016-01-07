package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumSaleStatus;

import com.freshdirect.erp.model.NotificationModel;
import com.freshdirect.framework.core.PrimaryKey;

public interface PostSettlementNotificationHome extends EJBHome {
	public PostSettlementNotificationEB create(NotificationModel notificationModel) throws CreateException, RemoteException;

	public PostSettlementNotificationEB findByPrimaryKey(PrimaryKey pk) throws FinderException, RemoteException;
	
	public Collection<PostSettlementNotificationEB> findMultipleByPrimaryKeys(Collection<PrimaryKey> pks) throws FinderException, RemoteException;
	
	public Collection<PrimaryKey> findByStatusAndType(EnumSaleStatus status, EnumNotificationType type) throws FinderException, RemoteException;
	
	public Collection<PrimaryKey> findSaleIdsByStatusAndType(EnumSaleStatus status, EnumNotificationType type) throws FinderException, RemoteException;

	public PostSettlementNotificationEB findBySalesIdAndType(String salesId, EnumNotificationType type) throws FinderException, RemoteException;
	
	public PostSettlementNotificationEB findByCriteria(String salesId, EnumSaleStatus status, EnumNotificationType type) throws FinderException, RemoteException;
	
}
