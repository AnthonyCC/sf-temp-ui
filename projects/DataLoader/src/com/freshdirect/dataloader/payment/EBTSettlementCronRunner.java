package com.freshdirect.dataloader.payment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.payment.ejb.SaleCronHome;
import com.freshdirect.dataloader.payment.ejb.SaleCronSB;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ecomm.gateway.SaleCronService;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class EBTSettlementCronRunner {
	
	private final static Category LOGGER = LoggerFactory.getInstance(EBTSettlementCronRunner.class);
	public static void main(String[] args) {
		long captureTimeout;
		if (args.length >= 1) {
			int minutes = 50;
			try {
				// First parameter should be for 'Timeout'.
				minutes = Integer.parseInt(args[0]);
			} catch (NumberFormatException ne) {
				LOGGER.warn("Passed in param is not an int", ne);
			}

			captureTimeout = minutes * 60 * 1000;
		}else{
			LOGGER.warn("NO timeout was passed for EBTSettlementCron defaulting to 50 mins");
			captureTimeout = 50 * 60 * 1000;
		}
		Context ctx = null;
		try {
			LOGGER.info("EBTSettlementCron started");
			ctx = getInitialContext();
			SaleCronHome home = (SaleCronHome) ctx.lookup("freshdirect.dataloader.SaleCron");

			
			
			
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.SaleCronSB)) {
				SaleCronService.getInstance().postAuthEBTSales(captureTimeout);
				SaleCronService.getInstance().captureEBTSales(captureTimeout);
				SaleCronService.getInstance().settleEBTSales();
			} else {
				SaleCronSB sb = home.create();
				sb.postAuthEBTSales(captureTimeout);
				sb.captureEBTSales(captureTimeout);
				sb.settleEBTSales();
			}
			LOGGER.info("EBTSettlementCron finished");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg=sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("EBTSettlementCron failed with Exception...").append(_msg).toString());
			LOGGER.error(_msg);
			if(_msg!=null && _msg.indexOf("timed out while waiting to get an instance from the free pool")==-1)
				email(Calendar.getInstance().getTime(), _msg);			
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				//could not do the cleanup
				email(Calendar.getInstance().getTime(), ne.toString());
			}
		}
	}

	public static Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
//		try {
//			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
//			String subject="EBTSettlementCron:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");
//
//			StringBuffer buff = new StringBuffer();
//
//			buff.append("<html>").append("<body>");			
//			
//			if(exceptionMsg != null) {
//				buff.append("<b>").append(exceptionMsg).append("</b>");
//			}
//			buff.append("</body>").append("</html>");
//
//			ErpMailSender mailer = new ErpMailSender();
//			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
//					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
//					subject, buff.toString(), true, "");
//			
//		}catch (MessagingException e) {
//			LOGGER.warn("Error Sending EBTSettlementCron report email: ", e);
//		}
//		
	}
}
