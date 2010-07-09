package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;


public class StandingOrdersServiceCmd {

	private static final Logger LOGGER = LoggerFactory.getInstance(StandingOrdersServiceCmd.class); 
	
	public static void main( String[] args ) {
		placeStandingOrders();
	}
		

	private static ThreadLocal<StandingOrdersServiceHome> sosHome = new ThreadLocal<StandingOrdersServiceHome>();

	private static void lookupSOSHome() throws FDResourceException {
		if ( sosHome.get() != null ) {
			return;
		}
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			sosHome.set( (StandingOrdersServiceHome)ctx.lookup( StandingOrdersServiceHome.JNDI_HOME ) );
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
			email(Calendar.getInstance().getTime(), ne.toString());
		} finally {
			try {
				if ( ctx != null ) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("cannot close Context while trying to cleanup", ne);
				email(Calendar.getInstance().getTime(), ne.toString());
			}
		}
	}
	
	private static void invalidateSOSHome() {
		sosHome.set( null );
	}

	private static void placeStandingOrders() {
		try {
			lookupSOSHome();
			StandingOrdersServiceSB sb = sosHome.get().create();
			
			LOGGER.info( "Starting to place orders..." );
			
			StandingOrdersServiceResult.Counter result = sb.placeStandingOrders();
			
			LOGGER.info( "Finished placing orders." );
			LOGGER.info( "  success : " + result.getSuccessCount() );
			LOGGER.info( "  failed  : " + result.getFailedCount() );
			LOGGER.info( "  skipped : " + result.getSkippedCount() );
			LOGGER.info( "  total   : " + result.getTotalCount() );
			
		} catch ( CreateException e ) {
			invalidateSOSHome();
			e.printStackTrace();
		} catch ( RemoteException e ) {
			invalidateSOSHome();
			e.printStackTrace();
		} catch ( FDResourceException e ) {
			invalidateSOSHome();
			e.printStackTrace();
		}
	}

	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="SaleCronRunner:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");			
			
			if(exceptionMsg != null) {
				buff.append("b").append(exceptionMsg).append("/b");
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");
			
		}catch (MessagingException e) {
			LOGGER.warn("Error Sending Sale Cron report email: ", e);
		}
		
	}
}
