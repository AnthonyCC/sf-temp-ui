/*
 * 
 * FDXOrderPickEligibleCronRunner.java
 * Date: Jul 5, 2002 Time: 5:53:45 PM
 */

package com.freshdirect.dataloader.payment;

/**
 * 
 * @author tbalumuri
 */
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.sap.SapOrderPickEligibleInfo;

public class FDXOrderPickEligibleCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(FDXOrderPickEligibleCronRunner.class);

	public static void main(String[] args) {

		try {
				List<SapOrderPickEligibleInfo> eligibleSapOrderLst = FDECommerceService.getInstance().queryForFDXSalesPickEligible();
				FDECommerceService.getInstance().sendFDXEligibleOrdersToSap(eligibleSapOrderLst);
			

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg = sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("FDXOrderPickEligibleCronRunner failed with Exception...").append(_msg)
					.toString());
			LOGGER.error(_msg);
		} 
	}

	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject = "FDXOrderPickEligibleCronRunner:	"
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
			LOGGER.warn("Error Sending FDXOrderPickEligible Cron report email: ", e);
		}

	}

}
