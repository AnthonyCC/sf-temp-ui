package com.freshdirect.dataloader.airclic;

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
import com.freshdirect.delivery.ejb.AirclicManagerHome;
import com.freshdirect.delivery.ejb.AirclicManagerSB;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;


public class AirclicDataSyncCron {

	/**
	 * @param args
	 */

	private final static Category LOGGER = LoggerFactory.getInstance(AirclicDataSyncCron.class);

	public static void main(String[] args) {
		
		Context ctx = null;
		try 
		{
			Date jobDate = null;
			boolean signature = true;
			if (args.length >= 1) {
				for (String arg : args) {
					try { 
						if (arg.startsWith("jobDate=")) {
							jobDate = DateUtil.truncate(DateUtil.toCalendar
																(DateUtil.parse(arg.substring("jobDate=".length())))).getTime();
						} 
						else if (arg.startsWith("signature=")) {
							signature = Boolean.valueOf(arg.substring("signature=".length())).booleanValue(); 
						} 
					} catch (Exception e) {
						System.err.println("Usage: java com.freshdirect.dataloader.airclic.AirclicDataSyncCron [jobDate={date value}]");
						System.exit(-1);
					}
				}
			}
			if(jobDate==null)
			{
				jobDate = DateUtil.truncate(Calendar.getInstance()).getTime();
			}
			if(!ErpServicesProperties.isAirclicBlackhole())
			{
				ctx = getInitialContext();
				AirclicManagerHome  home = (AirclicManagerHome) ctx.lookup("freshdirect.delivery.AirclicManager");
				AirclicManagerSB sb = home.create();
				sb.sendMessages();
				if(signature)
					sb.getSignatureData(jobDate);
			}
			
			
			
		}
	catch (Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));			
		LOGGER.info(new StringBuilder("AirclicDataSyncCron failed with Exception...").append(sw.toString()).toString());
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
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="AirclicDataSyncCron:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			LOGGER.warn("Error Sending Airclic DataSync Cron report email: ", e);
		}
		
	}

}
