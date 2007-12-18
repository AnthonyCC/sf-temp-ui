package com.freshdirect.dataloader.payment;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AutoCreditApprovalCron {

	private final static Category LOGGER = LoggerFactory.getInstance(AutoCreditApprovalCron.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("Automatic Credit Approval Started");
		Context ctx = null;
		try {
			
			ctx = getInitialContext();
			FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
			FDCustomerManagerSB sb = home.create();
			List ids = sb.getComplaintsForAutoApproval();
			LOGGER.info("Going to AUTO approve " + ids.size() + " complaints");
			for (Iterator i = ids.iterator(); i.hasNext();) {
				String complaintId = (String) i.next();
				LOGGER.info("Auto approve STARTED for complaint ID : " + complaintId);
				try {
					sb.approveComplaint(complaintId, true, "SYSTEM", true);
					LOGGER.info("Auto approve FINISHED for comolaint ID : " + complaintId);
				} catch (ErpComplaintException ex) {
					LOGGER.warn("Auto approve FAILED for complaint ID : " + complaintId);
				}
			}
			LOGGER.info("AutoCreditApprovalCron-finished");
		} catch (Exception e) {
			LOGGER.warn("Exception during approval", e);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				//could not do the cleanup
			}
		}
		
	}

	static public Context getInitialContext() throws NamingException {
		Hashtable h = new Hashtable();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}

}
