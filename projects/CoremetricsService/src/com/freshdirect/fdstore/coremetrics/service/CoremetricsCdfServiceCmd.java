package com.freshdirect.fdstore.coremetrics.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

/**
 * <p>This class is used for processing coremetrics cdf upload from cron job.</p>
 * 
 * <p>It can be used for testing processing standing orders from command-line.
 * When running from command-line, directory of <code>erpservices.properties</code> must be on classpath
 * and it must contain a valid <code>mail.smtp.host</code> property in order to be able to send cron report emails.</p>
 * 
 */
public class CoremetricsCdfServiceCmd {

	private static final Logger LOGGER = LoggerFactory.getInstance(CoremetricsCdfServiceCmd.class);
	private static ThreadLocal<CoremetricsCdfServiceHome> cdfHome = new ThreadLocal<CoremetricsCdfServiceHome>();


	private static void lookupCdfHome() throws FDResourceException {
		if ( cdfHome.get() != null ) {
			return;
		}
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			cdfHome.set( (CoremetricsCdfServiceHome)ctx.lookup( CoremetricsCdfServiceHome.JNDI_HOME ) );
		
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
		cdfHome.set(null);
	}

	
	
	public static void main( String[] args ) {	
		if (FDStoreProperties.isCoremetricsEnabled()){
			try{
				CdfProcessResult result = processCdf(); 
				if (!result.isSuccess()) {
					throw new Exception("ProcessCdf failed: " + result.getError());
				}
				
			} catch (Exception e) {
				LOGGER.error("CoremetricsCdfService failed with Exception...",e);
				sendExceptionMail(Calendar.getInstance().getTime(), e);
			}

		} else {
			LOGGER.info("Coremetrics is disabled");
		}
	}

	private static CdfProcessResult processCdf() {
		try {
			LOGGER.info( "Starting to process Coremetrics CDF..." );
			lookupCdfHome();

			CoremetricsCdfServiceSB sb = cdfHome.get().create();
			CdfProcessResult result = sb.processCdf();

			LOGGER.info( "Coremetrics CDF process " + (result.isSuccess() ? "is successful" : "FAILED"));
			return result;
			
		} catch ( CreateException e ) {
			invalidateCdfHome();
			LOGGER.error("CreateException",e);
			return new CdfProcessResult(false, e.getMessage());
		
		} catch ( RemoteException e ) {
			invalidateCdfHome();
			LOGGER.error("RemoteException",e);
			return new CdfProcessResult(false, e.getMessage());
		
		} catch ( FDResourceException e ) {
			invalidateCdfHome();
			LOGGER.error("FDResourceException",e);
			return new CdfProcessResult(false, e.getMessage());
		}
	}

	private static void sendExceptionMail( Date processDate, Throwable exception ) {
		try {
			
			StringWriter sw = new StringWriter();
			exception.printStackTrace(new PrintWriter(sw));
			String exceptionMsg = sw.getBuffer().toString();

			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="CoremetricsCdfCron:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			LOGGER.error("Error Sending Coremetrics CDf Cron report email: ", e);
		}
	}
}
