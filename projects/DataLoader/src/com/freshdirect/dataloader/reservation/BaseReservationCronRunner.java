package com.freshdirect.dataloader.reservation;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;

public abstract class BaseReservationCronRunner {
	 protected  abstract void processReservation(DlvManagerSB dlvManager,FDCustomerManagerSB sb,DlvReservationModel reservation) ;
	 
	   public Context getInitialContext() throws NamingException {
			Hashtable h = new Hashtable();
			h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			System.out.println("FDStoreProperties.getRoutingProviderURL() :"+FDStoreProperties.getRoutingProviderURL());
			h.put(Context.PROVIDER_URL, FDStoreProperties.getRoutingProviderURL());
			return new InitialContext(h);
		}
		
		public static FDIdentity getIdentity(String erpCustomerId) {
			return new FDIdentity(erpCustomerId);
		}
}
