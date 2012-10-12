package com.freshdirect.dataloader.payment;

import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.customer.CustomerCreditModel;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AutoLateDeliveryCredit {
	
	private final static Category LOGGER = LoggerFactory.getInstance(AutoLateDeliveryCredit.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("Automatic Late Delivery Credit Started");
		//1. Get customers who reported lates
		Context ctx = getInitialContext();
		
		FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
		FDCustomerManagerSB sb = home.create();
		List<CustomerCreditModel> ccmList = sb.getCustomerReprotedLates();
		LOGGER.info("CUSTLATES:" + ccmList.size());
		//2. Get driver reported lates
		List<CustomerCreditModel> dlList = sb.getDriverReportedLates();
		LOGGER.info("DRIVERLATES:" + dlList.size());
		//3. get Scan reported lates
		List<CustomerCreditModel> sList = sb.getScanReportedLates();
		LOGGER.info("SCANLATES:" + sList.size());
		ccmList.addAll(dlList);
		ccmList.addAll(sList);
		LOGGER.debug(ccmList);
		sb.storeLists(ccmList);
		//Check Thresholds
		LOGGER.info("Automatic Late Delivery Credit Ended");
	}	
	

	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}	

}
