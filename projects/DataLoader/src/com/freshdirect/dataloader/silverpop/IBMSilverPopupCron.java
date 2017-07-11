package com.freshdirect.dataloader.silverpop;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.customer.SilverPopupDetails;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.silverpopup.util.FDIBMPushNotification;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

/*
 *	@Author Vamsi Krishna
 */
public class IBMSilverPopupCron {

	private static Category LOGGER = LoggerFactory.getInstance(IBMSilverPopupCron.class);
	private static FDCustomerManagerHome fcHome = null;

	public static void main(String[] args) throws Exception {
		try {
			LOGGER.info("SilverPopupCron Started.");
			lookupSPSHome();
			FDCustomerManagerSB clientSB = fcHome.create();
			List<SilverPopupDetails> result = clientSB.getSilverPopupDetails();
			LOGGER.info("FDCustomer manager silver popup retrieved successfully " + result);
			if (null != result && !result.isEmpty()) {
				LOGGER.debug("Before calling IBM silverpopup ");
				FDIBMPushNotification service = new FDIBMPushNotification();
				for (SilverPopupDetails detail : result) {
					if (service.execute(detail)) {
						clientSB.updateSPSuccessDetails(detail);
					}
				}

			} else {
				LOGGER.debug("There are no details are available to call notificaiton");
			}
		} catch (Exception e) {
			LOGGER.error("Failed to run SilverPopupCronServlet cron job manually", e);
			invalidateFCHome();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg = sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("SilverPopupCron failed with Exception...").append(_msg).toString());
			LOGGER.error(_msg);
			email(Calendar.getInstance().getTime(), _msg);
		}
		LOGGER.info("SilverPopupCron Stopped.");
	}

	private static void email(Date processDate, String exceptionMsg) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject = "SilverPopupCron for IBM pushnotification : "
					+ (processDate != null ? dateFormatter.format(processDate) : " ");
			StringBuffer buff = new StringBuffer();
			buff.append("<html>").append("<body>");
			if (exceptionMsg != null) {
				buff.append("<b>").append(exceptionMsg).append("</b>");
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(), ErpServicesProperties.getCronFailureMailCC(), subject,
					buff.toString(), true, "");

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending SilverPopupCron report email: ", e);
		}

	}

	private static void lookupSPSHome() throws NamingException {

		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			fcHome = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
		} catch (NamingException ne) {
			throw ne;
		} catch (Exception ne) {
			LOGGER.error("unable to lookup standing order client ejb", ne);
			throw new NamingException("unable to lookup standing order client ejb ");
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.error("unable to lookup standing order client ejb", ne);
			}
		}
	}

	private static void invalidateFCHome() {
		fcHome = null;
	}
}
