package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.erp.model.NotificationModel;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.fdstore.services.tax.data.CommonResponse.Message;
import com.freshdirect.framework.core.DataSourceLocator;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.PostSettlementNotificationEB;
import com.freshdirect.payment.ejb.PostSettlementNotificationHome;

public class PostSettlementNotifySessionBean extends SessionBeanSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8306392432164870051L;
	private static final Category LOGGER = LoggerFactory.getInstance(PostSettlementNotifySessionBean.class);	
	
	public void updateNotification(NotificationModel notificationModel) throws RemoteException, EJBException, CreateException, ErpTransactionException, SQLException, FinderException{
		PostSettlementNotificationEB postSettlementNotificationhome = lookupPostSettlementNotificationHome().findByPrimaryKey(notificationModel.getPk());
		postSettlementNotificationhome.updateNotification(notificationModel);
	}
	
	public NotificationModel findBySalesIdAndType(String salesId, EnumNotificationType type) throws RemoteException, EJBException, FinderException{
		PostSettlementNotificationEB postSettlementNotification = lookupPostSettlementNotificationHome().findBySalesIdAndType(salesId, type);
		return postSettlementNotification.getModel();
	}
	
	public Collection<String> findByStatusAndType(EnumSaleStatus saleStatus, EnumNotificationType type) throws RemoteException, EJBException, FinderException, SQLException{
		Connection conn = null;
	       conn = DataSourceLocator.getConnection(null);
	       PreparedStatement ps = conn.prepareStatement("SELECT SALE_ID FROM CUST.PYMT_STLMNT_NOTIFICATION WHERE NOTIFICATION_STATUS=? and NOTIFICATION_TYPE=?");
	       ps.setString(1, saleStatus.getStatusCode());
	       ps.setString(2, type.getCode());
	       ResultSet rs = ps.executeQuery();

			List<String> lst = new ArrayList<String>();
			while (rs.next()) {
				String id = rs.getString(1);
				lst.add(id);
			}

			rs.close();
			ps.close();

			return lst;
	}
	
	public boolean commitToAvalara(PrimaryKey saleId) throws RemoteException, FinderException{
		ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
		ErpSaleModel saleModel = (ErpSaleModel) eb.getModel();
		FDOrderAdapter fdOrder = new FDOrderAdapter(saleModel);
		AvalaraContext avalaraContext = new AvalaraContext(fdOrder);
		avalaraContext.setCommit(true);
		fdOrder.getAvalaraTaxValue(avalaraContext);
		Message[] messages = avalaraContext.getMessages();
		boolean isAlreadySubmitted = false;
		if(null != messages){
		for(Message message : messages){
			if(message.getDetails().equals("Expected Saved|Posted")){
				isAlreadySubmitted = true;
				break;
			   }
			}
		}
		if((null != avalaraContext.getDocCode() && !"".equals(avalaraContext.getDocCode())) || isAlreadySubmitted){
			return true;
		}
		LOGGER.info("commitToAvalara - SaleId : "+saleId+ "Doc Code:"+ avalaraContext.getDocCode());
		if(null != messages && messages.length>0){
		for(Message message:messages){
			LOGGER.info("  Avalara Message detail : "+message.getDetails()+"\n  Avalara message "+message.getSummary());
		    }
		}
		return false;
	}
	
	public static PostSettlementNotificationHome lookupPostSettlementNotifyHome() throws EJBException {
		Context ctx = null;
		try {
			ctx = getInitialContext();
			return (PostSettlementNotificationHome)ctx.lookup("freshdirect.payment.Notify");
		} catch (NamingException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				if (ctx != null)
					ctx.close();
			} catch (NamingException ne) {
				LOGGER.debug(ne);
			}
		}
	}
	
	public static PostSettlementNotificationHome lookupPostSettlementNotificationHome() throws EJBException {
		Context ctx = null;
		try {
			ctx = getInitialContext();
			return (PostSettlementNotificationHome)ctx.lookup("freshdirect.payment.Notification");
		} catch (NamingException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				if (ctx != null)
					ctx.close();
			} catch (NamingException ne) {
				LOGGER.debug(ne);
			}
		}
	}
		
		private static Context getInitialContext() throws NamingException {

			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL()); //t3://localhost:7006
			env.put(Context.INITIAL_CONTEXT_FACTORY, weblogic.jndi.WLInitialContextFactory.class.getName());
			return new InitialContext(env);
		}
		
		private ErpSaleHome getErpSaleHome() {
			Context ctx = null;
			try {
				ctx = getInitialContext();
				return (ErpSaleHome)ctx.lookup("freshdirect.erp.Sale");
			} catch (NamingException ex) {
				throw new EJBException(ex);
			} finally {
				try {
					if (ctx != null)
						ctx.close();
				} catch (NamingException ne) {
				}
			}
		}
}
