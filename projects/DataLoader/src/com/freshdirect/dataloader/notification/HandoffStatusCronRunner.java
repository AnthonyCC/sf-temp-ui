package com.freshdirect.dataloader.notification;

/**
*
* @author  kkanuganti
* @version 
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
import com.freshdirect.dataloader.notification.ejb.HandoffNotificationHome;
import com.freshdirect.dataloader.notification.ejb.HandoffNotificationSB;
import com.freshdirect.delivery.model.HandoffStatusNotification;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class HandoffStatusCronRunner {

	/**
	 * @param args
	 */

	private final static Category LOGGER = LoggerFactory.getInstance(HandoffStatusCronRunner.class);
	
	private final static long HANDOFFSTATUS_INTERVEL = 15 * 60 * 1000;

	public static void main(String[] args) {
		
		Context ctx = null;
		StringBuffer stringBuffer = new StringBuffer();
		try {
			ctx = getInitialContext();
			HandoffNotificationHome home = (HandoffNotificationHome) ctx.lookup("freshdirect.dataloader.HandoffNotificationCron");
			HandoffNotificationSB sb = home.create();
			List<HandoffStatusNotification> handoffBatchs = sb.getHandoffStatus();
			if(handoffBatchs != null) {
				for(HandoffStatusNotification batch : handoffBatchs) {
					if(batch.getCommitTime() != null
							&& (System.currentTimeMillis() - batch.getCommitTime().getTime()) >= HANDOFFSTATUS_INTERVEL) {
						stringBuffer.append(batch.getBatchId()).append(",");
					}
				}
				if(stringBuffer.length() > 0) {				
					ErpMailSender emailer = new ErpMailSender();
					emailer.sendMail(RoutingServicesProperties.getHandOffMailFrom(), RoutingServicesProperties.getHandOffMailTo()
												, RoutingServicesProperties.getHandOffMailCC(), "Handoff Status Notification"
												, "There are handoff batch(s) " + stringBuffer.toString() + " comitted to SAP successfully but no Auto-dispatch. Please perform Auto-dispatch.");
				}
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			LOGGER.info(new StringBuilder("HandoffStatusCronRunner failed with Exception...").append(sw.toString()).toString());
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
		
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="HandoffStatusCronRunner:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			
		} catch (MessagingException e) {
			LOGGER.warn("Error Sending HandoffStatusCronRunner Cron report email: ", e);
		}
		
	}
	
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
}
