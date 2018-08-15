package com.freshdirect.dataloader.smsalerts;

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

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.backoffice.service.BackOfficeClientService;
import com.freshdirect.backoffice.service.IBackOfficeClientService;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.delivery.sms.SMSAlertManager;
import com.freshdirect.delivery.sms.ejb.SmsAlertsHome;
import com.freshdirect.delivery.sms.ejb.SmsAlertsSB;
import com.freshdirect.ecommerce.data.delivery.sms.RecievedSmsData;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.payment.service.FDECommerceService;

public class CaseCreationByReceivedSMSCron {

	private static final Logger LOGGER = LoggerFactory.getInstance(CaseCreationByReceivedSMSCron.class);
	private static List<RecievedSmsData> recievedSmsList=null;;
	
	public static void main(String[] args) throws Exception {
		Context ctx = null;
		try {
			ctx = getInitialContext();
			SmsAlertsHome smsAlertsHome = (SmsAlertsHome) ctx
					.lookup(DlvProperties.getSmsAlertsHome());

			if (FDStoreProperties
					.isSF2_0_AndServiceEnabled("sms.ejb.SmsAlertsSB")) {
				recievedSmsList = FDECommerceService.getInstance()
						.getReceivedSmsData();
			} else {
				SmsAlertsSB smsAlertSB = smsAlertsHome.create();
				recievedSmsList = smsAlertSB.getReceivedSmsData();
			}
			for (RecievedSmsData model : recievedSmsList) {
				LOGGER.info("Start:::::Received SMS data  is sending to Backoffice. MobileNumber:"
						+ model.getMobileNumber());
				IBackOfficeClientService service = BackOfficeClientService
						.getInstance();
				boolean isCaseCreated = service
						.createCaseByRecievedSmsData(SMSAlertManager
								.populateRecievedSmsData(PhoneNumber
										.retainDigits(model.getMobileNumber()),
										model.getCarrierName(), model
												.getMessage(), model
												.getReceivedDate()));
				if (FDStoreProperties
						.isSF2_0_AndServiceEnabled("sms.ejb.SmsAlertsSB")) {
					FDECommerceService.getInstance().updateCaseCreationStatus(
							model.geteStoreId(), isCaseCreated);
				} else {
					SmsAlertsSB smsAlertSB = smsAlertsHome.create();
					smsAlertSB.updateCaseCreationStatus(model.geteStoreId(),
							isCaseCreated);
				}
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			LOGGER.info(new StringBuilder(
					"CaseCreationByReceivedSMSCron failed with Exception...")
					.append(sw.toString()).toString());
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
				email(Calendar.getInstance().getTime(), sw.getBuffer()
						.toString());
			}
		}
	}
	private static void email(Date processDate, String exceptionMsg) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="CaseCreationByReceivedSMSCron: "+ (processDate != null ? dateFormatter.format(processDate) : " ");
			StringBuffer buff = new StringBuffer();
			buff.append("<html>").append("<body>");			
			if(exceptionMsg != null) {
				buff.append("<b>").append(exceptionMsg).append("</b>");
			}
			buff.append("</body>").append("</html>");
	
			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");
			
			}catch (MessagingException e) {
			LOGGER.warn("Error Sending CaseCreationByReceivedSMSCron report email: ", e);
			}
	
	}

public static Context getInitialContext() throws NamingException {
	Hashtable<String, String> h = new Hashtable<String, String>();
	h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
	h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
	return new InitialContext(h);
}
}
