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
import com.freshdirect.delivery.sms.SMSAlertManager;
import com.freshdirect.ecommerce.data.delivery.sms.RecievedSmsData;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.payment.service.FDECommerceService;

public class CaseCreationByReceivedSMSCron {

	private static final Logger LOGGER = LoggerFactory.getInstance(CaseCreationByReceivedSMSCron.class);
	private static List<RecievedSmsData> recievedSmsList=null;;
	
	public static void main(String[] args) throws Exception {
		try {
				recievedSmsList = FDECommerceService.getInstance()
						.getReceivedSmsData();
			
			for (RecievedSmsData model : recievedSmsList) {
				LOGGER.info("Start:::::CaseCreationByReceivedSMSCron for  MobileNumber:"
						+ model.getMobileNumber());
				IBackOfficeClientService service = BackOfficeClientService
						.getInstance();
				boolean isCaseCreated = service
						.createCaseByRecievedSmsData(SMSAlertManager
								.populateRecievedSmsData(retainDigits(model.getMobileNumber()),
										model.getCarrierName(), model
												.getMessage(), model
												.getReceivedDate()));
					FDECommerceService.getInstance().updateCaseCreationStatus(
							model.getSmsId(), isCaseCreated);
				
				LOGGER.info("End::::: CaseCreationByReceivedSMSCron  process has been completed for MobileNumber:"+model.getMobileNumber()+"CaseCreation Response:"+isCaseCreated);
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			LOGGER.info(new StringBuilder(
					"CaseCreationByReceivedSMSCron failed with Exception...")
					.append(sw.toString()).toString());
			LOGGER.error(sw.toString());
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
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
	private static String retainDigits(String string) {
		StringBuffer clean = new StringBuffer();
		if (string == null)
			return "";
		for (int i = 0; i < string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				clean.append(string.charAt(i));
			}
		}
		return clean.toString();
	}
	
public static Context getInitialContext() throws NamingException {
	Hashtable<String, String> h = new Hashtable<String, String>();
	h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
	h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
	return new InitialContext(h);
}
}
