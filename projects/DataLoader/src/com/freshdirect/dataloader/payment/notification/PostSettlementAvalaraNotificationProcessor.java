package com.freshdirect.dataloader.payment.notification;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.erp.model.NotificationModel;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;


public class PostSettlementAvalaraNotificationProcessor extends 
PostSettlementNotificationProcessor	 {
	private final static ServiceLocator LOCATOR = new ServiceLocator();

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
	public void Notify() throws RemoteException, EJBException, FinderException, ErpTransactionException, CreateException, SQLException {
		Collection<PrimaryKey> pendingNotifications = lookupPostSettlementNotificationHome().findSaleIdsByStatusAndType(EnumSaleStatus.PENDING, EnumNotificationType.AVALARA);			
			for(PrimaryKey salesId: pendingNotifications){
				ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(salesId);
				ErpSaleModel saleModel = (ErpSaleModel) eb.getModel();
				FDOrderAdapter fdOrder = new FDOrderAdapter(saleModel);
				if(commitToAvalara(fdOrder)){
					changeNotificationStatus(salesId);
				}
			}
	}
	

	private void changeNotificationStatus(PrimaryKey salesId) throws RemoteException, EJBException, FinderException, ErpTransactionException, CreateException, SQLException {
		NotificationModel notificationModel = lookupPostSettlementNotificationHome().findBySalesIdAndType(salesId.getId(), EnumNotificationType.AVALARA).getModel();
		notificationModel.setNotification_status(EnumSaleStatus.SETTLED);
		this.saveNoification(notificationModel);		
	}


	private boolean commitToAvalara(FDOrderAdapter fdOrder) {
		AvalaraContext avalaraContext = new AvalaraContext(fdOrder);
		avalaraContext.setCommit(true);
		fdOrder.getAvalaraTaxValue(avalaraContext);
		if(null != avalaraContext.getDocCode() && !"".equals(avalaraContext.getDocCode())){
			return true;
		}
		return false;
	}


	private ErpSaleHome getErpSaleHome() {
		try {
			return (ErpSaleHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpSale");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

}
