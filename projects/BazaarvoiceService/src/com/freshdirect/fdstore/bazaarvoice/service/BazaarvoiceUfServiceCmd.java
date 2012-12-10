package com.freshdirect.fdstore.bazaarvoice.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class BazaarvoiceUfServiceCmd {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(BazaarvoiceUfServiceCmd.class);
	private static ThreadLocal<BazaarvoiceUfServiceHome> ufHome = new ThreadLocal<BazaarvoiceUfServiceHome>();


	private static void lookupCdfHome() throws FDResourceException {
		if ( ufHome.get() != null ) {
			return;
		}
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			ufHome.set( (BazaarvoiceUfServiceHome)ctx.lookup( BazaarvoiceUfServiceHome.JNDI_HOME ) );
		
		} catch (NamingException ne) {
			throw new FDResourceException(ne);			
		
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("cannot close Context while trying to cleanup", ne);				
			}
		}
	}
	
	private static void invalidateCdfHome() {
		ufHome.set(null);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (FDStoreProperties.isBazaarvoiceEnabled()){
			try{
				String param = "";
				if (args.length > 0) {
					param = args[0];
				}
				BazaarvoiceFeedProcessResult result = process(param); 
				if (!result.isSuccess()) {
					throw new Exception("Process upload feed failed: " + result.getError());
				}
				
			} catch (Exception e) {
				LOGGER.error("BazaarvoiceUfService failed with Exception...",e);
				sendExceptionMail(Calendar.getInstance().getTime(), e);
			}
		
		} else {
			LOGGER.info("Bazaarvoice is disabled");
		}
	}
	
	private static BazaarvoiceFeedProcessResult process(String param) {
		try {
			LOGGER.info( "Starting to process Bazaarvoice upload feed..." );
			lookupCdfHome();

			BazaarvoiceUfServiceSB sb = ufHome.get().create();
			BazaarvoiceFeedProcessResult result = null;
			if (!"--disableUpload".equals(param)) {
				result = sb.processFile();
				LOGGER.info( "Bazaarvoice upload feed process " + (result.isSuccess() ? "is successful" : "FAILED"));
			}
			if (!"--disableDownload".equals(param)) {
				result = sb.processRatings();
				LOGGER.info( "Bazaarvoice download feed process " + (result.isSuccess() ? "is successful" : "FAILED"));
			}
			return result;
			
		} catch ( CreateException e ) {
			invalidateCdfHome();
			LOGGER.error("CreateException",e);
			return new BazaarvoiceFeedProcessResult(false, e.getMessage());
		
		} catch ( RemoteException e ) {
			invalidateCdfHome();
			LOGGER.error("RemoteException",e);
			return new BazaarvoiceFeedProcessResult(false, e.getMessage());
		
		} catch ( FDResourceException e ) {
			invalidateCdfHome();
			LOGGER.error("FDResourceException",e);
			return new BazaarvoiceFeedProcessResult(false, e.getMessage());
		}
	}
	
	private static void sendExceptionMail( Date processDate, Throwable exception ) {
		try {
			
			StringWriter sw = new StringWriter();
			exception.printStackTrace(new PrintWriter(sw));
			String exceptionMsg = sw.getBuffer().toString();

			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="BazaarvoiceUfCron:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");			
			
			if(exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");
			
		}catch (MessagingException e) {
			LOGGER.error("Error Sending Bazaarvoice Uf Cron report email: ", e);
		}
	}

}
