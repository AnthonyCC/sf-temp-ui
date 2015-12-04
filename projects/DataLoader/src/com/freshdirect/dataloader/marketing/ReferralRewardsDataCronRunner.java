package com.freshdirect.dataloader.marketing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.referral.extole.ExtoleEarnedRewardsParser;
import com.freshdirect.referral.extole.ExtoleServiceException;
import com.freshdirect.referral.extole.ExtoleSftpService;
import com.freshdirect.referral.extole.FDExtoleManager;
import com.freshdirect.referral.extole.model.FDRafCreditModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

/**
 * This is a daily job which downloads the 
 * referral rewards file from Extole
 * remote folder location. We then parse the
 * downloaded file from our local
 * folder and save it into cust.raf_credit table
 */
public class ReferralRewardsDataCronRunner {

	private final static Category LOGGER = LoggerFactory
			.getInstance(ReferralRewardsDataCronRunner.class);

	public static void main(String args[]) {

		try {
			FDExtoleManager.downloadAndSaveRewards();
			LOGGER.info("Finished ReferralRewardsDataCronRunner");

		} catch (FileNotFoundException e) {
			// throw new ExtoleServiceException(e);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg = sw.getBuffer().toString();
			LOGGER.info(new StringBuilder(
					"ReferralRewardsDataCronRunner failed with FileNotFoundException...")
					.append(_msg).toString());
			LOGGER.error(_msg);
			if (_msg != null
					&& _msg.indexOf("timed out while waiting to get an instance from the free pool") == -1)
				email(Calendar.getInstance().getTime(), _msg);

		} catch (ParseException pe) {
			StringWriter sw = new StringWriter();
			pe.printStackTrace(new PrintWriter(sw));
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());

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
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
			
		}
	}

	private static void email(Date processDate, String exceptionMsg) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(
					"EEE, MMM d, yyyy");
			String subject = "ReferralRewardsDataCronRunner:	"
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
			LOGGER.warn("Error Sending ReferralRewardsDataCronRunner report email: ",e);
		}

	}

}
