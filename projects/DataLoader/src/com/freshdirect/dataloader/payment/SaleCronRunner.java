/*
 * 
 * SaleCronRunner.java
 * Date: Jul 5, 2002 Time: 5:53:45 PM
 */

package com.freshdirect.dataloader.payment;

/**
 * 
 * @author knadeem
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.payment.ejb.SaleCronHome;
import com.freshdirect.dataloader.payment.ejb.SaleCronSB;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class SaleCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(SaleCronRunner.class);

	public static void main(String[] args) {
		long authTimeout;

		if (args.length >= 1) {
			int minutes = 15;
			try {
				// first parameter is for authTimeout
				minutes = Integer.parseInt(args[0]);
			} catch (NumberFormatException ne) {
				LOGGER.warn("Passed in param is not an int", ne);
			}

			authTimeout = minutes * 60 * 1000;
		}else{
			LOGGER.warn("NO timeout was passed for Authorizations defaulting to 15 mins");
			authTimeout = 15 * 60 * 1000;
		}

		Context ctx = null;
		try {
			ctx = getInitialContext();
			SaleCronHome home = (SaleCronHome) ctx.lookup("freshdirect.dataloader.SaleCron");

			SaleCronSB sb = home.create();
			sb.cancelAuthorizationFailed();
			int affected = sb.cutoffSales();

			if (affected > 0 && "true".equalsIgnoreCase(ErpServicesProperties.getSendCutoffEmail())) {
				Calendar cal = DateUtil.toCalendar(new Date());

				if (cal.get(Calendar.HOUR) > 2) {
					cal.add(Calendar.DATE, 1);
				}
				LOGGER.debug("Sending report for " + cal.getTime() + "...");
				CallCenterServices.emailCutoffTimeReport(cal.getTime());
			}
			//First clear pending reverse auth for cancelled orders.
			sb.reverseAuthorizeSales(authTimeout);
			//Second Pre auth gift card.
			sb.preAuthorizeSales(authTimeout);
			//Third perform CC authorization.
			sb.authorizeSales(authTimeout);
			// remved the following task, create a new cron job for it.
			//sb.captureSales(captureTimeout); 

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(new StringBuilder("SaleCronRunner failed with Exception...").append(e.toString()).toString());
			LOGGER.error(e);
			email(Calendar.getInstance().getTime(), e.toString());		
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				//could not do the cleanup
				email(Calendar.getInstance().getTime(), ne.toString());
			}
		}
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
			String subject="SaleCronRunner:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");			
			
			if(exceptionMsg != null) {
				buff.append("b").append(exceptionMsg).append("/b");
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");
			
		}catch (MessagingException e) {
			LOGGER.warn("Error Sending Sale Cron report email: ", e);
		}
		
	}

}
