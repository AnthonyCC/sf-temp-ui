package com.freshdirect.dataloader.productfeed;
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

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.content.productfeed.FDProductFeedHome;
import com.freshdirect.fdstore.content.productfeed.FDProductFeedSB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;


public class ProductFeedXMLGeneratorCron {
	

	private static final Logger LOGGER = LoggerFactory.getInstance(ProductFeedXMLGeneratorCron.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Context ctx = null;
		try {
			LOGGER.info("ProductFeedXMLGeneratorCron Started.");
			ctx = getInitialContext();
			FDProductFeedHome managerHome = (FDProductFeedHome) ctx.lookup("freshdirect.fdstore.ProductFeed");
			FDProductFeedSB sb = managerHome.create();
			sb.uploadProductFeed();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg=sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("ProductFeedXMLGeneratorCron failed with Exception...").append(_msg).toString());
			LOGGER.error(_msg);
			if(_msg!=null && _msg.indexOf("timed out while waiting to get an instance from the free pool")==-1)
				email(Calendar.getInstance().getTime(), _msg);		
		} 
		LOGGER.info("ProductFeedXMLGeneratorCron Stopped.");
	}

	
	private static void email(Date processDate, String exceptionMsg) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="ProductFeedXMLGeneratorCron: "+ (processDate != null ? dateFormatter.format(processDate) : " ");
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
			LOGGER.warn("Error Sending ProductFeedXMLGeneratorCron report email: ", e);
		}
		
	}
	
	public static Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
}
