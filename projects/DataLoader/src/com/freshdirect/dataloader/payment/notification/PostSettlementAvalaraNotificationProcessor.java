package com.freshdirect.dataloader.payment.notification;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.dataloader.payment.ejb.PostSettlementNotifySB;


public class PostSettlementAvalaraNotificationProcessor extends 
PostSettlementNotificationProcessor	 {
	
	//private static Category LOGGER = LoggerFactory.getInstance(PostSettlementAvalaraNotificationProcessor.class);

	public static void main(String[] args){
		PostSettlementAvalaraNotificationProcessor processor = new PostSettlementAvalaraNotificationProcessor();
		try {
			processor.Notify();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		} catch (ErpTransactionException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void Notify() throws RemoteException, FinderException, EJBException, ErpTransactionException, CreateException, SQLException {
		PostSettlementNotifySB postSettlementNotify = lookupPostSettlementNotifyHome().create();		
		Collection<String> pendingNotifications = postSettlementNotify.findByStatusAndType(EnumSaleStatus.PENDING, EnumNotificationType.AVALARA);
		List<String> salesNotCommitted = postSettlementNotify.commitToAvalara(pendingNotifications);		
	}
	

/*	private void changeNotificationStatus(PostSettlementNotifySB postSettlementNotify, String salesId) throws RemoteException, EJBException, FinderException, ErpTransactionException, CreateException, SQLException {
		NotificationModel notificationModel = postSettlementNotify.findBySalesIdAndType(salesId, EnumNotificationType.AVALARA).getModel();
		notificationModel.setNotification_status(EnumSaleStatus.SETTLED);
		notificationModel.setCommitDate(new Date());
		this.saveNoification(notificationModel);		
	}*/
}