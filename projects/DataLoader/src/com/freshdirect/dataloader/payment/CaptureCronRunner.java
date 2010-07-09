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
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class CaptureCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(CaptureCronRunner.class);

	public static void main(String[] args) {
		
		long captureTimeout;
		if (args.length >= 1) {
			int minutes = 50;
			try {
				// first parameter is for captureTimeout
				minutes = Integer.parseInt(args[0]);
			} catch (NumberFormatException ne) {
				LOGGER.warn("Passed in param is not an int", ne);
			}

			captureTimeout = minutes * 60 * 1000;
		}else{
			LOGGER.warn("NO timeout was passed for Capture Authorizations defaulting to 50 mins");
			captureTimeout = 50 * 60 * 1000;
		}

		Context ctx = null;
		try {
			LOGGER.info("CaptureCron started");
			ctx = getInitialContext();
			SaleCronHome home = (SaleCronHome) ctx.lookup("freshdirect.dataloader.SaleCron");

			SaleCronSB sb = home.create();
			//First post auth sales for gift cards.
			sb.postAuthSales(captureTimeout);
			sb.captureSales(captureTimeout);
			LOGGER.info("CaptureCron finished");
		} catch (Exception e) {
			LOGGER.error(e);
			LOGGER.info(new StringBuilder("CaptureCron failed with Exception...").append(e.toString()).toString());
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
			String subject="CaptureCron:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			LOGGER.warn("Error Sending CaptureCron report email: ", e);
		}
		
	}

}
