package com.freshdirect.dataloader.payment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.CustomerCreditModel;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.ecomm.gateway.CustomerReportService;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class AutoLateDeliveryCredit {

	private final static Category LOGGER = LoggerFactory.getInstance(AutoLateDeliveryCredit.class);

	public static void main(String[] args) throws NamingException, RemoteException, CreateException {
		LOGGER.info("Automatic Late Delivery Credit Started");
		Context ctx = getInitialContext();

		
		try {
			List<CustomerCreditModel> dlList;
			List<CustomerCreditModel> sList;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerReport)) {

				CustomerReportService.getInstance().generateLateDeliveryCreditReport();
			} else {
				Set<CustomerCreditModel> ccmList;
				FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
				FDCustomerManagerSB sb = home.create();
				ccmList = new HashSet<CustomerCreditModel>();
				// 1. Get driver reported lates
				dlList = sb.getDriverReportedLates();

				// 2. get Scan reported lates
				sList = sb.getScanReportedLates();
				LOGGER.info("DRIVERLATES:" + dlList.size());
				LOGGER.info("SCANLATES:" + sList.size());
				ccmList.addAll(dlList);
				ccmList.addAll(sList);
				LOGGER.debug(ccmList);
				sb.storeLists(ccmList);
			}

		} catch (FDResourceException e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg = sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("AutoLateCredit failed with Exception...").append(_msg).toString());
			LOGGER.error(_msg);
			if (_msg != null)
				email(Calendar.getInstance().getTime(), _msg);
		}
		LOGGER.info("Automatic Late Delivery Credit Ended");
	}

	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}

	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject = "AutoLateCredit:	"
					+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");

			if (exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(), ErpServicesProperties.getCronFailureMailCC(), subject,
					buff.toString(), true, "");

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending Sale Cron report email: ", e);
		}

	}

}
