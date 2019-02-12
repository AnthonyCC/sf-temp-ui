package com.freshdirect.dataloader.payment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.mail.MessagingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ecomm.gateway.CustomerReportService;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class AutoLateDeliveryCredit {

	private static final Category LOGGER = LoggerFactory.getInstance(AutoLateDeliveryCredit.class);

	public static void main(String[] args) throws RemoteException {
		LOGGER.info("Automatic Late Delivery Credit Started");
				
		try {
			CustomerReportService.getInstance().generateLateDeliveryCreditReport();
		} catch (FDResourceException e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String msg = sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("AutoLateCredit failed with Exception...").append(msg).toString());
			LOGGER.error(msg);
			if (msg != null)
				email(Calendar.getInstance().getTime(), msg);
		}
		LOGGER.info("Automatic Late Delivery Credit Ended");
	}

	private static void email(Date processDate, String exceptionMsg) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject = "AutoLateCredit:	"
					+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");

			if (exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(), ErpServicesProperties.getCronFailureMailCC(), subject,
					buff.toString(), true, "");

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending Sale Cron report email: ", e);
		}

	}

}
