package com.freshdirect.dataloader.analytics;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
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
import com.freshdirect.analytics.ejb.DispatchVolumeHome;
import com.freshdirect.analytics.ejb.DispatchVolumeSB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class DispatchVolumeCronRunner {

	/**
	 * @param args
	 */

	private final static Category LOGGER = LoggerFactory.getInstance(DispatchVolumeCronRunner.class);

	public static void main(String[] args) {
		
		Context ctx = null;
		try 
		{
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			ctx = getInitialContext();
			DispatchVolumeHome home = (DispatchVolumeHome) ctx.lookup("freshdirect.analytics.DispatchVolume");
			DispatchVolumeSB sb = home.create();
			sb.captureDispatchVolume(new Timestamp(cal.getTimeInMillis()));
		}
	catch (Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));			
		LOGGER.info(new StringBuilder("DispatchVolumeCronRunner failed with Exception...").append(sw.toString()).toString());
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

	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="DispatchVolumeCronRunner:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			LOGGER.warn("Error Sending DispatchVolume Cron report email: ", e);
		}
		
	}
	
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
}
