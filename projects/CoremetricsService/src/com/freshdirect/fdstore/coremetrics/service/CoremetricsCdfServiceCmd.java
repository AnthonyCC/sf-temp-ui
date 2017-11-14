package com.freshdirect.fdstore.coremetrics.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.CmFacade;
import com.freshdirect.fdstore.coremetrics.CmInstance;
import com.freshdirect.fdstore.ecomm.gateway.CoreMetricsCdfService;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.fdstore.FDContentTypes;

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
			
			final String param = args.length > 0
					? args[args.length-1]
					: null;

			/*** 
			try {
				Context ctx = FDStoreProperties.getInitialContext();
			} catch (NamingException exc) {
				LOGGER.error(exc);
			}

			final boolean isMultiStoreEnv = CmsManager.getInstance().getContentKeysByType(FDContentTypes.STORE).size() > 1;
			final boolean isDBMode = !CmsManager.getInstance().isReadOnlyContent(); 
			***/

			// detect global mode by param
			final boolean globalParam = ("-g".equals(param) || "--global".equals(param));

			// write out environment
			LOGGER.debug(">> CMD 'global' param set: " + globalParam);
			// LOGGER.debug(">> multi-store mode detected: " + isMultiStoreEnv);
			// LOGGER.debug(">> CMS DB mode detected: " + isDBMode );


			final boolean globalMode = globalParam /* || isDBMode || isMultiStoreEnv */;
			
			if (globalMode) {
				LOGGER.info("*** Run task in global mode ***");
			} else {
				LOGGER.info("*** Run task in normal mode ***");
				
			}
			
			
			try{
				CdfProcessResult result = globalMode
						? processGlobalCdf()
						: processCdf(); 
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

	private abstract static class CdfProcessTemplate {
		abstract CdfProcessResult process(CoremetricsCdfServiceSB sb) throws RemoteException;

		public CdfProcessResult run() {
			try {
				LOGGER.info( "Starting to process Coremetrics CDF..." );
				lookupCdfHome();
				
				
				// Generate CDF files
				CoremetricsCdfServiceSB sb = cdfHome.get().create();

				CdfProcessResult result = process(sb);

				// evaluate process result
				if (result != null) {
					LOGGER.info( "Coremetrics CDF process " + (result.isSuccess() ? "is successful" : "FAILED"));
				} else {
					LOGGER.warn("Actually nothing happened ... zero result.");
					result = new CdfProcessResult(false, "Premature exit");
				}

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
	}


	private static CdfProcessResult processGlobalCdf() {
		return new CdfProcessTemplate() {
			@Override
			CdfProcessResult process(CoremetricsCdfServiceSB sb) throws RemoteException {
				CmContext ctx = CmContext.createGlobalContext();
				CdfProcessResult result;
				if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.CoremetricsCdfServiceSB)){
					result = CoreMetricsCdfService.getInstance().processCdf(ctx);
				}else{
					result = sb.processCdf(ctx);
				}

				return result;
			}
		}.run();
	}



	/**
	 * Generate CDF files for all available facades (channels) of the store
	 * Defined in context
	 *
	 * @see CmContext
	 * @see CmFacade
	 * 
	 * @return
	 */
	private static CdfProcessResult processCdf() {
		return new CdfProcessTemplate() {
			@Override
			CdfProcessResult process(CoremetricsCdfServiceSB sb) throws RemoteException {
				Collection<CmContext> ctxs = new ArrayList<CmContext>(CmFacade.values().length);
				
				// determine basic context from the CM Client ID
				
				boolean isTest = false;
				CmInstance inst = CmInstance.lookupByClientId( FDStoreProperties.getCoremetricsClientId() );
				
				if (inst == null) {
					isTest = true;
					inst = CmInstance.lookupByTestClientId( FDStoreProperties.getCoremetricsClientId() );
				}

				if (inst == null) {
					LOGGER.error("Failed to determine CoreMetrics context, aborting ...");
					return new CdfProcessResult(false, "Missing CoreMetrics Client ID, perhaps not set in fdstore.properties");
				}

				// Synthesize contexts for WEB, PHONE and TABLET facades
				for (CmFacade facade : CmFacade.values()) {
					ctxs.add(
						CmContext.createContextFor(inst.getEStoreId(), facade, isTest)
					);
				}

				CdfProcessResult result = null;
				if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.CoremetricsCdfServiceSB)){
					for (CmContext ctx : ctxs) {
						result = CoreMetricsCdfService.getInstance().processCdf(ctx);
						if (!result.isSuccess()) {
							break;
						}
					}
				}else{
					for (CmContext ctx : ctxs) {
						result = sb.processCdf(ctx);
						if (!result.isSuccess()) {
							break;
						}
					}
				}

				return result;
			}
		}.run();
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
