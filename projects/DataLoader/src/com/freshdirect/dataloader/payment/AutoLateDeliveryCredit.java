package com.freshdirect.dataloader.payment;

import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.customer.CustomerCreditModel;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;

public class AutoLateDeliveryCredit {

	public static void main(String[] args) throws Exception {
		//1. Get customers who reported lates
		Context ctx = getInitialContext();
		
		FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
		FDCustomerManagerSB sb = home.create();
		List<CustomerCreditModel> ccmList = sb.getCustomerReprotedLates();
		//2. Get driver reported lates
		List<CustomerCreditModel> dlList = sb.getDriverReportedLates();
		//3. get Scan reported lates
		List<CustomerCreditModel> sList = sb.getScanReportedLates();
		ccmList.addAll(dlList);
		ccmList.addAll(sList);
		System.out.println(ccmList);
		sb.storeLists(ccmList);
	}	
	

	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}	

}
