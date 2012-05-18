package com.freshdirect.dataloader.analytics;

/**
*
* @author  tbalumuri
* @version 
*/
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
import com.freshdirect.delivery.ejb.CapacitySnapshotHome;
import com.freshdirect.delivery.ejb.CapacitySnapshotSB;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class CapacitySnapshotCronRunner {

	/**
	 * @param args
	 */

	private final static Category LOGGER = LoggerFactory.getInstance(CapacitySnapshotCronRunner.class);

	public static void main(String[] args) {
		
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
			CapacitySnapshotHome home = (CapacitySnapshotHome) ctx.lookup("freshdirect.delivery.CapacitySnapshot");
			CapacitySnapshotSB sb = home.create();
			sb.capture(startDate, endDate);
		}
	catch (Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));			
		LOGGER.info(new StringBuilder("CapacitySnapshotCronRunner failed with Exception...").append(sw.toString()).toString());
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
			String subject="CapacitySnapshotCronRunner:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			LOGGER.warn("Error Sending CapacitySnapshot Cron report email: ", e);
		}
		
	}
	
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
}
