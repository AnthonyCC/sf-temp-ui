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
import com.freshdirect.dataloader.payment.ejb.SaleCronHome;
import com.freshdirect.dataloader.payment.ejb.SaleCronSB;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ecomm.gateway.SaleCronService;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class SaleCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(SaleCronRunner.class);

	public static void main(String[] args) {
		long authTimeout;

		if (args.length >= 1) {
			int minutes = 7;
			try {
				// first parameter is for authTimeout
				minutes = Integer.parseInt(args[0]);
			} catch (NumberFormatException ne) {
				LOGGER.warn("Passed in param is not an int", ne);
			}

			authTimeout = minutes * 60 * 1000;
		}else{
			LOGGER.warn("NO timeout was passed for Authorizations defaulting to 15 mins");
			authTimeout = 7 * 60 * 1000;
		}

		Context ctx = null;
		SaleCronSB sb = null;
		try {
			
			int affected ;
			List<Date> dates;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.SaleCronSB)) {
				SaleCronService.getInstance().cancelAuthorizationFailed();
				dates = SaleCronService.getInstance().queryCutoffReportDeliveryDates();
				affected = SaleCronService.getInstance().cutoffSales();
			} else {
				if (sb == null) {
					ctx = getInitialContext();
					SaleCronHome home = (SaleCronHome) ctx.lookup("freshdirect.dataloader.SaleCron");
					sb = home.create();
				}
				
				sb.cancelAuthorizationFailed();
				dates = sb.queryCutoffReportDeliveryDates();
				affected = sb.cutoffSales();
			}
			
			if (affected > 0 && "true".equalsIgnoreCase(ErpServicesProperties.getSendCutoffEmail())) {
				for(Date day : dates)
					{
					Calendar cal = DateUtil.toCalendar(day);
					LOGGER.debug("Sending report for " + cal.getTime() + "...");
					CallCenterServices.emailCutoffTimeReport(cal.getTime());
					}
			}
			//First clear pending reverse auth for cancelled orders.
			//Second Pre auth gift card.
			//Third perform CC authorization.
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.SaleCronSB)){
				SaleCronService.getInstance().reverseAuthorizeSales(authTimeout);
				SaleCronService.getInstance().preAuthorizeSales(authTimeout);
				SaleCronService.getInstance().authorizeSales(authTimeout);
			}else {
				if (sb == null) {
					if (ctx == null) {
						ctx = getInitialContext();
					}
					SaleCronHome home = (SaleCronHome) ctx.lookup("freshdirect.dataloader.SaleCron");
					sb = home.create();
				}
				sb.reverseAuthorizeSales(authTimeout);
				sb.preAuthorizeSales(authTimeout);
				sb.authorizeSales(authTimeout);
				
			}

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg=sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("SaleCronRunner failed with Exception...").append(_msg).toString());
			LOGGER.error(_msg);
			if(_msg!=null && _msg.indexOf("timed out while waiting to get an instance from the free pool")==-1)
				email(Calendar.getInstance().getTime(), _msg);		
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
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="SaleCronRunner:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			LOGGER.warn("Error Sending Sale Cron report email: ", e);
		}
		
	}
	
	
}
