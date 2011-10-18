package com.freshdirect.fdstore.standingorders.service;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.ejb.FDCustomerHome;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.StandingOrdersServiceResult;
import com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode;
import com.freshdirect.fdstore.standingorders.StandingOrdersServiceResult.Result;
import com.freshdirect.fdstore.standingorders.StandingOrdersServiceResult.Status;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.webapp.util.StandingOrderUtil;


public class StandingOrdersServiceSessionBean extends SessionBeanSupport {
	
	private static final long	serialVersionUID	= -4164707518041489389L;
	
	private final static Category LOGGER = LoggerFactory.getInstance(StandingOrdersServiceSessionBean.class);
	
	private static FDStandingOrdersManager	soManager			= FDStandingOrdersManager.getInstance();
	private static FDCustomerHome			fcHome				= null;
	private static MailerGatewayHome		mailerHome			= null;
	
	public static final String INITIATOR_NAME = "Standing Order Service";
	
	private static void invalidateFCHome() {
		fcHome = null;
	}
	
	private static void lookupFCHome() {
		if (fcHome != null) {
			return;
		}		
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			fcHome = (FDCustomerHome) ctx.lookup( FDStoreProperties.getFDCustomerHome() );
		} catch ( NamingException ne ) {
			ne.printStackTrace();
		} finally {
			try {
				if ( ctx != null ) {
					ctx.close();
				}
			} catch ( NamingException ne ) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}

	private static void invalidateMailerHome() {
		mailerHome = null;
	}
	
	private static void lookupMailerHome() {
		if (mailerHome != null) {
			return;
		}		
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			mailerHome = (MailerGatewayHome) ctx.lookup( "freshdirect.mail.MailerGateway" );
		} catch ( NamingException ne ) {
			ne.printStackTrace();
		} finally {
			try {
				if ( ctx != null ) {
					ctx.close();
				}
			} catch ( NamingException ne ) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}
	
	public StandingOrdersServiceResult.Counter placeStandingOrders() {
				
		lookupFCHome();
		lookupMailerHome();
		
		Collection<FDStandingOrder> soList;
		try {
			LOGGER.info( "Loading active standing orders." );
			soList = soManager.loadActiveStandingOrders();			
		} catch (FDResourceException re) {
			invalidateFCHome();
			invalidateMailerHome();
			LOGGER.error( "Could not retrieve standing orders list! - FDResourceException", re );
			sendTechnicalMail( "Could not retrieve standing orders list! - FDResourceException" );
			return null;
		}
		
		if ( soList == null ) {
			LOGGER.error( "Could not retrieve standing orders list! - loadActiveStandingOrders() returned null" );
			sendTechnicalMail( "Could not retrieve standing orders list! - loadActiveStandingOrders() returned null" );
			return null;
		}
		
		StandingOrdersServiceResult.Counter resultCounter = new StandingOrdersServiceResult.Counter();
		Map<Date, Date> altDates = null;
		try {
			altDates = FDStandingOrdersManager.getInstance().getStandingOrdersAlternateDeliveryDates();
		} catch (FDResourceException e) {
			LOGGER.error( "Getting standing order alternate delivery dates failed with FDResourceException!", e );
		}
		LOGGER.info( "Processing " + soList.size() + " standing orders." );
		for ( FDStandingOrder so : soList ) {
			Result result;
			try {
				
				TimeslotEventModel event = new TimeslotEventModel(EnumTransactionSource.STANDING_ORDER.getCode(), 
						false, 0.00, false, false);

				if(null !=altDates){
					result = StandingOrderUtil.process( so,altDates.get(so.getNextDeliveryDate()), event, null, mailerHome );
				}else{
					result = StandingOrderUtil.process( so,null, event, null, mailerHome);
				}
//				result = process( so );
				
			} catch (FDResourceException re) {
				invalidateFCHome();
				invalidateMailerHome();
				LOGGER.error( "Processing standing order failed with FDResourceException!", re );
				result = new Result( ErrorCode.TECHNICAL, ErrorCode.TECHNICAL.getErrorHeader(), "Processing standing order failed with FDResourceException!", null );
			}
			
			if ( result.isError() ) {
				if ( result.isTechnicalError() ) {
					// technical error
					sendTechnicalMail( result.getErrorDetail() );
				} else {
					// other error -> set so to error state
					so.setLastError( result.getErrorCode(), result.getErrorHeader(), result.getErrorDetail() );
					sendErrorMail( so, result.getCustomerInfo() );
				}
			}
			
			resultCounter.count( result.getStatus() );
			
			if ( result.getStatus() != Status.SKIPPED ) {
				try {
					FDActionInfo info = new FDActionInfo(EnumTransactionSource.STANDING_ORDER, so.getCustomerIdentity(),
							INITIATOR_NAME, "Updating Standing Order Status", null);
					soManager.save( info, so );
				} catch (FDResourceException re) {
					invalidateFCHome();
					invalidateMailerHome();
					LOGGER.error( "Saving standing order failed! (FDResourceException)", re );
				}
				
				logActivity( so, result );
			}			
		}
		
		return resultCounter;		
			
	}
	
	private void logActivity ( FDStandingOrder so, Result result ) {
		
		ErpActivityRecord activityRecord = new ErpActivityRecord();
		activityRecord.setStandingOrderId( so.getId() );
		activityRecord.setCustomerId( so.getCustomerId() );
		activityRecord.setDate( new Date() );
		activityRecord.setInitiator( INITIATOR_NAME );
		activityRecord.setSource( EnumTransactionSource.STANDING_ORDER );
		if (result.getErrorHeader() != null || result.getErrorDetail() != null) {
			String note;
			if (result.getErrorHeader() != null) {
				if (result.getErrorDetail() != null)
					note = result.getErrorHeader() + ";" + result.getErrorDetail();
				else
					note = result.getErrorHeader();
			} else {
				if (result.getErrorDetail() != null)
					note = result.getErrorDetail();
				else
					note = null;
			}
			activityRecord.setNote( note );
		}
		activityRecord.setChangeOrderId( result.getSaleId() );
		
		Status status = result.getStatus();
		
		if ( status == Status.SUCCESS ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_PLACED );
		} else if ( status == Status.FAILURE ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_FAILED );
		} else if ( status == Status.SKIPPED ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_SKIPPED );					
		}
		
		new ErpLogActivityCommand( activityRecord ).execute();		
	}
	
	private void sendErrorMail ( FDStandingOrder so, FDCustomerInfo customerInfo ) {
		try {
			XMLEmailI mail = FDEmailFactory.getInstance().createStandingOrderErrorEmail( customerInfo, so );		
			MailerGatewaySB mailer;
			mailer = mailerHome.create();
			mailer.enqueueEmail( mail );
		} catch ( RemoteException e ) {
			e.printStackTrace();
		} catch ( CreateException e ) {
			e.printStackTrace();
		}
	}

	private void sendTechnicalMail ( String msg ) {
		try {
						
			ErpMailSender mailer = new ErpMailSender();
			
			String from = ErpServicesProperties.getStandingOrdersTechnicalErrorFromAddress();
			String recipient = ErpServicesProperties.getStandingOrdersTechnicalErrorRecipientAddress();
			String subject = "TECHNICAL ERROR occurred in the Standing Orders background service!";
			
			StringBuilder message = new StringBuilder(); 
			message.append( "Standing Orders background process failed!\n" );
			
			message.append( "message: " );
			message.append( msg );
			message.append( '\n' );
			
			message.append( " Please check the server log for more details...\n" );
			
			message.append( "timestamp: " );
			message.append( new Date() );
			message.append( '\n' );
			
			try {
				message.append( "ip: " );
				message.append( java.net.InetAddress.getLocalHost().getHostAddress() );
				message.append( '\n' );
				message.append( "host: " );
				message.append( java.net.InetAddress.getLocalHost().getCanonicalHostName() );
				message.append( '\n' );
			} catch ( UnknownHostException e ) {
				message.append( "host is unknown" );				
			}
			
			mailer.sendMail(from, recipient, "", subject, message.toString() );
			
		} catch ( MessagingException e ) {
			LOGGER.error( "Failed to send out technical error report email!" );
			e.printStackTrace();
		}
	}
	
	public StandingOrdersServiceResult.Counter placeStandingOrders(Collection<String> soList) {
		
		lookupFCHome();
		lookupMailerHome();
		
		
		if ( soList == null || soList.size()==0) {
			LOGGER.error( "Empty List passed." );
			sendTechnicalMail( "Empty List passed." );
			return null;
		}
		
		StandingOrdersServiceResult.Counter resultCounter = new StandingOrdersServiceResult.Counter();
		Map<Date, Date> altDates = null;
		try {
			altDates = FDStandingOrdersManager.getInstance().getStandingOrdersAlternateDeliveryDates();
		} catch (FDResourceException e) {
			LOGGER.error( "Getting standing order alternate delivery dates failed with FDResourceException!", e );
		}
		
		TimeslotEventModel event = new TimeslotEventModel(EnumTransactionSource.STANDING_ORDER.getCode(), 
				false, 0.00, false, false);
		
		LOGGER.info( "Processing " + soList.size() + " standing orders." );
		for ( String _so : soList ) {
			Result result;
			FDStandingOrder so=null;
			try {
				so=soManager.load(new PrimaryKey(_so));
				if(null !=altDates){
					result = StandingOrderUtil.process( so,altDates.get(so.getNextDeliveryDate()), event, null, mailerHome );
				}else{
					result = StandingOrderUtil.process( so, null, event, null, mailerHome);
				}
//				result = process( so );
				
			} catch (FDResourceException re) {
				invalidateFCHome();
				invalidateMailerHome();
				LOGGER.error( "Processing standing order failed with FDResourceException!", re );
				result = new Result( ErrorCode.TECHNICAL, ErrorCode.TECHNICAL.getErrorHeader(), "Processing standing order failed with FDResourceException!", null );
			}
			
			if ( result.isError() ) {
				if ( result.isTechnicalError() ) {
					// technical error
					sendTechnicalMail( result.getErrorDetail() );
				} else {
					// other error -> set so to error state
					so.setLastError( result.getErrorCode(), result.getErrorHeader(), result.getErrorDetail() );
					sendErrorMail( so, result.getCustomerInfo() );
				}
			}
			
			resultCounter.count( result.getStatus() );
			
			if ( result.getStatus() != Status.SKIPPED ) {
				try {
					FDActionInfo info = new FDActionInfo(EnumTransactionSource.STANDING_ORDER, so.getCustomerIdentity(),
							INITIATOR_NAME, "Updating Standing Order Status", null);
					soManager.save( info, so );
				} catch (FDResourceException re) {
					invalidateFCHome();
					invalidateMailerHome();
					LOGGER.error( "Saving standing order failed! (FDResourceException)", re );
				}
				
				logActivity( so, result );
			}			
		}
		
		return resultCounter;		
			
	}
}
