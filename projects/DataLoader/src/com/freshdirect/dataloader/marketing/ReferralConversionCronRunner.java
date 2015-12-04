package com.freshdirect.dataloader.marketing;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.referral.extole.ExtoleServiceException;
import com.freshdirect.referral.extole.FDExtoleManager;
import com.freshdirect.referral.extole.model.ExtoleResponse;

/**
 * This is a daily job for creating a conversion request and
 * approve conversion request to Extole API. 
 * The Extole responses are stored/updated accordingly in
 * the cust.raf_trans table
 */

public class ReferralConversionCronRunner {

	private final static Category LOGGER = LoggerFactory
			.getInstance(ReferralConversionCronRunner.class);

	public static void main(String[] args) {

		try {
			FDExtoleManager.createConversion();
		} catch (IOException ie) {
			StringWriter sw = new StringWriter();
			ie.printStackTrace(new PrintWriter(sw));
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
		} catch (FDResourceException fe) {
			StringWriter sw = new StringWriter();
			fe.printStackTrace(new PrintWriter(sw));
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
		} catch (ExtoleServiceException ee) {
			StringWriter sw = new StringWriter();
			ee.printStackTrace(new PrintWriter(sw));
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg = sw.getBuffer().toString();
			LOGGER.info(new StringBuilder(
					"ReferralConversionCronRunner- Create Conversion failed with Exception...")
					.append(_msg).toString());
			LOGGER.error(_msg);
			if (_msg != null)
				email(Calendar.getInstance().getTime(), _msg);
		}

		try {
			FDExtoleManager.approveConversion();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg = sw.getBuffer().toString();
			LOGGER.info(new StringBuilder(
					"ReferralConversionCronRunner - Approve Conversion failed with Exception...")
					.append(_msg).toString());
			LOGGER.error(_msg);
			if (_msg != null)
				email(Calendar.getInstance().getTime(), _msg);
		}

	}

	private static void email(Date processDate, String exceptionMsg) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(
					"EEE, MMM d, yyyy");
			String subject = "ReferralConversionCronRunner:	"
					+ (processDate != null ? dateFormatter.format(processDate)
							: " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");

			if (exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),
					ErpServicesProperties.getCronFailureMailCC(), subject,
					buff.toString(), true, "");

		} catch (MessagingException e) {
			LOGGER.warn(
					"Error Sending ReferralConversionCronRunner report email: ",
					e);
		}

	}

}
