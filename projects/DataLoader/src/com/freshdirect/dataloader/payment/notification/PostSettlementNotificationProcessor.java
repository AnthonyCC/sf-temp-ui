package com.freshdirect.dataloader.payment.notification;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.erp.model.NotificationModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.PostSettlementNotificationEB;
import com.freshdirect.payment.ejb.PostSettlementNotificationHome;

public abstract class PostSettlementNotificationProcessor implements
		IPostSettlementNotification {
	
	private static final Category LOGGER = LoggerFactory.getInstance(PostSettlementNotificationProcessor.class);


	@Override
	public abstract void Notify() throws RemoteException, EJBException, FinderException, ErpTransactionException, CreateException, SQLException; 
	
	@Override
	public boolean saveNoification(NotificationModel notificationModel) throws RemoteException, EJBException, CreateException, ErpTransactionException, SQLException{
		PostSettlementNotificationEB postSettlementNotificationhome = lookupPostSettlementNotificationHome().create(notificationModel);
		postSettlementNotificationhome.updateNotification(notificationModel);
		return false;
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
}
