/*
 * 
 * FDXOrderPickEligibleCronRunner.java
 * Date: Jul 5, 2002 Time: 5:53:45 PM
 */

package com.freshdirect.dataloader.payment;

/**
 * 
 * @author tbalumuri
 */
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.payment.ejb.FDXOrderPickEligibleCronHome;
import com.freshdirect.dataloader.payment.ejb.FDXOrderPickEligibleCronSB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class FDXOrderPickEligibleCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(FDXOrderPickEligibleCronRunner.class);

	public static void main(String[] args) {
	

		Context ctx = null;
		try {
			ctx = getInitialContext();
			FDXOrderPickEligibleCronHome home = (FDXOrderPickEligibleCronHome) ctx.lookup("freshdirect.dataloader.FDXOrderPickEligibleCron");

			FDXOrderPickEligibleCronSB sb = home.create();
			sb.queryForSalesPickEligible();

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg=sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("FDXOrderPickEligibleCronRunner failed with Exception...").append(_msg).toString());
			LOGGER.error(_msg);	
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				//could not do the cleanup
				StringWriter sw = new StringWriter();
				ne.printStackTrace(new PrintWriter(sw));	
				email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
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
			String subject="FDXOrderPickEligibleCronRunner:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");			
			
			if(exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");
			
		}catch (MessagingException e) {
			LOGGER.warn("Error Sending FDXOrderPickEligible Cron report email: ", e);
		}
		
	}
	
	
}
