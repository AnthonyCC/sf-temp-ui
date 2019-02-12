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
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.SilverPopupDetails;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.ecomm.gateway.TEmailInfoService;
import com.freshdirect.fdstore.silverpopup.util.FDIBMPushNotification;
import com.freshdirect.fdstore.temails.ejb.TEmailInfoHome;
import com.freshdirect.fdstore.temails.ejb.TEmailInfoSB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

/*
 *	@Author Dheller
 */
public class IBMSilverPopupEmailCron {

	private static Category LOGGER = LoggerFactory.getInstance(IBMSilverPopupEmailCron.class);
	private static TEmailInfoHome transactEmailInfoHome = null;
	//	return (TEmailInfoHome) LOCATOR.getRemoteHome("freshdirect.fdstore.TEmailInfoManager");

	public static void main(String[] args) throws Exception {
		try {
			LOGGER.info("SilverPopEmailCron Started.");
			int count;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.TEmailInfoSB)) {
				count = TEmailInfoService.getInstance().sendFailedTransactions(100);
			} else {
				lookupSPSHome();
				TEmailInfoSB clientSB = transactEmailInfoHome.create();
				count =  clientSB. sendFailedTransactions(100);
			}
			
			LOGGER.info(" ******* IBMSilverPopupEmailCron:  failed emails retried successfully " + count);
			
		} catch (Exception e) {
			LOGGER.error("Failed to run SilverPopupCronServlet cron job manually", e);
			invalidateFCHome();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg = sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("SilverPopupEmailCron failed with Exception...").append(_msg).toString());
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
			//	return (TEmailInfoHome) LOCATOR.getRemoteHome("freshdirect.fdstore.TEmailInfoManager");
			transactEmailInfoHome = (TEmailInfoHome) ctx.lookup("freshdirect.fdstore.TEmailInfoManager");
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
				LOGGER.error("unable to lookup TEmailHome ejb", ne);
			}
		}
	}

	private static void invalidateFCHome() {
		transactEmailInfoHome = null;
	}
}
