package com.freshdirect.dataloader.smsalerts;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.delivery.sms.ejb.SmsAlertsHome;
import com.freshdirect.delivery.sms.ejb.SmsAlertsSB;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class NextStopSmsCronRunner {
	
	private final static Category LOGGER = LoggerFactory.getInstance(NextStopSmsCronRunner.class);
	
	public static void main(String[] args){
		Context ctx = null;
		try 
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			cal = DateUtil.truncate(cal);
			Date startDate = cal.getTime();
			cal.add(Calendar.DATE, 7);
			Date endDate = cal.getTime();
			
			
			ctx = getInitialContext();
			SmsAlertsHome smsAlertsHome = (SmsAlertsHome) ctx.lookup( DlvProperties.getSmsAlertsHome());
			SmsAlertsSB smsAlertSB = smsAlertsHome.create();
			
			smsAlertSB.sendNextStopSms();
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));			
			LOGGER.info(new StringBuilder("NextStopSmsCronRunner failed with Exception...").append(sw.toString()).toString());
			LOGGER.error(sw.toString());
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());		
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				StringWriter sw = new StringWriter();
				ne.printStackTrace(new PrintWriter(sw));	
				email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
			}
		}
	}
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL,DlvProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="NextStopSmsCronRunner:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			LOGGER.warn("Error Sending Next Stop Sms Cron report email: ", e);
		}
		
	}

}
