package com.freshdirect.dataloader.giftcard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.mail.MessagingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class GCNSMCronRunner {
	private final static Category LOGGER = LoggerFactory.getInstance(GCNSMCronRunner.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			LOGGER.info("Start GCNSMCronRunner..");
			FDCustomerManager.resubmitGCOrders();
			LOGGER.info("Stop GCNSMCronRunner..");
		} catch (Exception e) {			
			e.printStackTrace();
			LOGGER.info(new StringBuilder("GCNSMCronRunner failed with Exception...").append(e.toString()).toString());
			LOGGER.error(e);
			email(Calendar.getInstance().getTime(), e.toString());
		}
	}
	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="GCNSM Cron : "+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			LOGGER.warn("Error Sending Capacity cron report email: ", e);
		}
		
	}

}
