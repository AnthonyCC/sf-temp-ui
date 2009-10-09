package com.freshdirect.smartstore.offline;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.smartstore.ejb.OfflineBatchRecommenderHome;
import com.freshdirect.smartstore.ejb.OfflineBatchRecommenderSB;

public class OfflineBatchRecommenderCmd {
	public static void main(String[] args) {
		Context ctx;
		try {
			ctx = getInitialContext();
			OfflineBatchRecommenderHome home = (OfflineBatchRecommenderHome) ctx
					.lookup(OfflineBatchRecommenderHome.JNDI_HOME);
			OfflineBatchRecommenderSB sb = home.create();
			System.out.println("No. of users processed: " + sb.runBatch("DYF").getUserCount());
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (FDResourceException e) {
			e.printStackTrace();
		}
	}

	static public Context getInitialContext() throws NamingException {
		Hashtable h = new Hashtable();
		h.put(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
}
