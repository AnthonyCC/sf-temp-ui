package com.freshdirect.fdstore.standingorders.service;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.SOResult;
import com.freshdirect.fdstore.standingorders.StandingOrdersJobConfig;
import com.freshdirect.fdstore.standingorders.SOResult.Result;
import com.freshdirect.fdstore.standingorders.SOResult.Status;
import com.freshdirect.fdstore.standingorders.UnavDetailsReportingBean;
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
	private static MailerGatewayHome		mailerHome			= null;
	
	public static final String INITIATOR_NAME = "Standing Order Service";
	

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
			LOGGER.error("NamingException",ne);
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
	

	public SOResult.ResultList placeStandingOrders(Collection<String> soIdList, StandingOrdersJobConfig jobConfig) {
		Collection<FDStandingOrder> soList;

		if ( soIdList == null ) {
			// We got no list at all, which means we need to process everything
			LOGGER.info( "Null list passed, processing all standing orders." );
			try {
				LOGGER.info( "Loading active standing orders." );
				// Need to Pass parameter to get EXISTING and NEW standing orders
				// true :: New Standing Orders3.0  with active flag is Y
				// false: existing standing Orders with active flag as null
				
				soList = soManager.loadActiveStandingOrdersForAWeek(false);	 				//soList = soManager.loadActiveStandingOrders(false);	
				
				soList.addAll(soManager.loadActiveStandingOrdersForAWeek(true));			//soList.addAll(soManager.loadActiveStandingOrders(true));
				
				if ( soList.isEmpty()  ) {
					LOGGER.error( "Empty list retrieved for standing orders list! - loadActiveStandingOrders() returned empty list" );
					sendTechnicalMail( "Empty list retrieved for standing orders list! - loadActiveStandingOrders() returned empty list" );
					return null;
				}
			} catch (FDResourceException re) {
				invalidateMailerHome();
				LOGGER.error( "Could not retrieve standing orders list! - FDResourceException", re );
				sendTechnicalMail( "Could not retrieve standing orders list! - FDResourceException" );
				return null;
			}
		} else if ( soIdList.size() == 0 ) {
			// We got an empty list, which is strange, send a technical mail about this.
			LOGGER.error( "Empty List passed." );
			sendTechnicalMail( "Empty List passed." );
			return null;
		} else {
			// We got some list of SO id-s, lets fetch the SO objects
			try {
				soList = new ArrayList<FDStandingOrder>();
				for ( String soId : soIdList ) {
					FDStandingOrder so = soManager.load( new PrimaryKey( soId ) );
					if ( so != null ) {
							soList.add( so );
						}
				}
			} catch (FDResourceException re) {
				invalidateMailerHome();
				LOGGER.error( "Could not retrieve standing order! - FDResourceException", re );
				sendTechnicalMail( "Could not retrieve standing order! - FDResourceException" );
				return null;
			}
		}				
		// We now have a list of FDStandingOrder objects to be processed in soList
		
		
		// Load alternate dates - will be null if there are none. (?)
		Map<Date, List<FDStandingOrderAltDeliveryDate>> altDates = null;
		try {
			altDates = FDStandingOrdersManager.getInstance().getStandingOrdersGlobalAlternateDeliveryDates();
		} catch (FDResourceException e) {
			LOGGER.error( "Getting standing order alternate delivery dates failed with FDResourceException!", e );
		}

		
		// Begin processing SO-s, and count the results
		SOResult.ResultList resultCounter = new SOResult.ResultList();
		LOGGER.info( "Processing " + soList.size() + " standing orders." );
		for ( FDStandingOrder so : soList ) {
			Result result;
			try {			
				// The main processing occurs here.
				lookupMailerHome();		
				FDStandingOrderAltDeliveryDate altDate = getStandingOrderAltDeliveryDateForSO(altDates, so);
				result = StandingOrderUtil.process( so, altDate, null, null, 
											jobConfig.isForceCapacity(), jobConfig.isCreateIfSoiExistsForWeek(), jobConfig.isSendReminderNotificationEmail() );
			} catch (FDResourceException re) {
				LOGGER.error( "Processing standing order failed with FDResourceException!", re );
				result = SOResult.createTechnicalError( so, "Processing standing order failed with FDResourceException!" );
			//APPDEV-2286 catch unchecked exceptions
			} catch (Exception e) {
				LOGGER.error( "Processing standing order failed with Exception!", e );
				e.printStackTrace(); // DEBUG
				result = SOResult.createTechnicalError( so, "Processing standing order failed with unchecked exception!!!" );				
			} finally {
				invalidateMailerHome();
			}
			
			// Check result, and send an error report if something went wrong
			if ( result.isError() && result.isSendErrorEmail()) {
				if ( result.isTechnicalError() ) {
					// technical error - send email to sysadmins
					if ( sendTechnicalMail(
							result.getErrorDetail(),
							result.getErrorCode()==null ? "" : result.getErrorCode().toString(),
							result.getCustId(),
							result.getCustomerInfo()==null ? "" : result.getCustomerInfo().getEmailAddress(),
							result.getSoId(),
							result.getSoName() ) ) {
						result.setErrorEmailSentToAdmins();
					}
				}else  {
					// other error, except persisting -> set so to error state, and send email to customer
					if (result.getErrorCode() != ErrorCode.PERSISTING_ERROR) {
						so.setLastError( result.getErrorCode(), result.getErrorHeader(), result.getErrorDetail() );
					}
					if ( sendErrorMail( so, result.getCustomerInfo() ) ) {
						result.setErrorEmailSentToCustomer();						
					}
				}
			}
			
			// add (and count) the result
			resultCounter.add( result );
			
			// if there was any change (not skipped), then save the SO and log the activity
			if ( result.getStatus() != Status.SKIPPED ) {
				if (result.getErrorCode() != ErrorCode.PERSISTING_ERROR) {
					try {
						FDActionInfo info = new FDActionInfo(EnumTransactionSource.STANDING_ORDER, so.getCustomerIdentity(), INITIATOR_NAME, "Updating Standing Order Status", null, null);
						LOGGER.info("Saving SO: " + so.getId());
						soManager.save(info, so);
					} catch (FDResourceException re) {
						invalidateMailerHome();
						LOGGER.error("Saving standing order failed! (FDResourceException)", re);
					}
				}
				logActivity( so, result );				
			}			
		}
		
		// =====================
		//  2days notification 
		// =====================
		if (jobConfig.isSendReminderNotificationEmail()) {
			Collection<FDStandingOrder> soListEmailNotification = null;
			if (soIdList == null) {
				try {
					LOGGER.info("Loading all active standing orders for 2days email notification.");
					soListEmailNotification = soManager.loadActiveStandingOrders(true);
					soListEmailNotification.addAll(soManager.loadActiveStandingOrders(false));
					if (soListEmailNotification.isEmpty()) {
						LOGGER.error("Could not retrieve standing orders list! - loadSOFor2DayNotification() returned null");
						sendTechnicalMail("Could not retrieve standing orders list! - loadSOFor2DayNotification() returned null");
						return null;
					}
				} catch (FDResourceException re) {
					invalidateMailerHome();
					LOGGER.error("Could not retrieve standing orders list! - FDResourceException", re);
					sendTechnicalMail("Could not retrieve standing orders list! - FDResourceException");
					return null;
				}
			}
			if (null !=soListEmailNotification && !soListEmailNotification.isEmpty()) {
				for (FDStandingOrder so : soListEmailNotification) {
					try {
						// The main processing occurs here.
						lookupMailerHome();
						StandingOrderUtil.sendNotification(so);
					} catch (FDResourceException re) {
						LOGGER.error("2days notification for SO failed with FDResourceException!", re);
						SOResult.createTechnicalError(so, "2days notification for SO failed with FDResourceException!");
					} finally {
						invalidateMailerHome();
					}
				}LOGGER.info("2days email notification has been exicuted.");
			}
		}
		
		return resultCounter;			
	}

	private FDStandingOrderAltDeliveryDate getStandingOrderAltDeliveryDateForSO(
			Map<Date, List<FDStandingOrderAltDeliveryDate>> altDates,
			FDStandingOrder so) {
		FDStandingOrderAltDeliveryDate altDate = null;
		if(null != altDates){
			List<FDStandingOrderAltDeliveryDate> altDatesList = altDates.get(so.getNextDeliveryDate());
			if(null != altDatesList){
				for (Iterator<FDStandingOrderAltDeliveryDate> iterator = altDatesList.iterator(); iterator.hasNext();) {
					FDStandingOrderAltDeliveryDate fdStandingOrderAltDeliveryDate = iterator.next();
					if(so.getStartTime().equals(fdStandingOrderAltDeliveryDate.getOrigStartTime()) &&
							so.getEndTime().equals(fdStandingOrderAltDeliveryDate.getOrigEndTime())){
						altDate = fdStandingOrderAltDeliveryDate;
						break ;
					}else if(null == altDate && null == fdStandingOrderAltDeliveryDate.getOrigStartTime() && null == fdStandingOrderAltDeliveryDate.getOrigEndTime()){
						//Default alternate date for the delivery date
						altDate = fdStandingOrderAltDeliveryDate;
					}
					
				}
			}
		}
		return altDate;
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
			if(result.getErrorCode().equals(ErrorCode.PERSISTING_ERROR)){
				note = "SO: "+so.getId() +" "+ result.getErrorHeader() + " : " +so.getLastErrorCode()+", "+result.getErrorDetail();
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
		} else if ( status == Status.FORCED_SKIPPED ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_FORCED_SKIPPED );					
		}
		
		new ErpLogActivityCommand( activityRecord ).execute();		
	}
	
	
	/**
	 * Sends an error report email to the customer about a broken SO.
	 * @param msg
	 */
	private boolean sendErrorMail ( FDStandingOrder so, FDCustomerInfo customerInfo ) {
		lookupMailerHome();
		try {
			XMLEmailI mail = FDEmailFactory.getInstance().createStandingOrderErrorEmail( customerInfo, so );		

			MailerGatewaySB mailer;
			mailer = mailerHome.create();
			mailer.enqueueEmail( mail );
			return true;
		} catch ( RemoteException e ) {
			invalidateMailerHome();
			LOGGER.error("RemoteException", e);
		} catch ( CreateException e ) {
			invalidateMailerHome();
			LOGGER.error("CreateException", e);
		}
		return false;
	}

	private static boolean sendTechnicalMail ( String msg, String errorCode, String customerId, String customerName, String soId, String soName ) {
		return sendTechnicalMail(msg,errorCode,customerId,customerName,soId,soName,null,null,null);
	}
	/**
	 * Sends an error report email about a technical error to the 'so admins' (?)  (address is configurable via erpservices.properties)
	 * 
	 * Please note that all of the following errors are regarded as 'technical', so email is not sent to the customer but to an internal address. [APPDEV-2217]
	 * a.	TECHNICAL
	 * b.	GENERIC
	 * c.	TIMESLOT
	 * d.	CLOSED_DAY
	 * 
	 * @param msg
	 */
	private static boolean sendTechnicalMail ( String msg, String errorCode, String customerId, String customerName, String soId, String soName,Date deliveryDate, Date startTime, Date endTime ) {
		try {
						
			ErpMailSender mailer = new ErpMailSender();
			
			String from = ErpServicesProperties.getStandingOrdersTechnicalErrorFromAddress();
			String recipient = ErpServicesProperties.getStandingOrdersTechnicalErrorRecipientAddress();
			String subject = "Error occurred in the Standing Orders background service!";
			
			StringBuilder message = new StringBuilder(); 
			message.append( "Standing Orders background process failed!\n" );
			
			if ( customerId != null ) {
				message.append( "customerId: " );
				message.append( customerId );
				message.append( '\n' );
			}

			if ( customerName != null ) {
				message.append( "customer: " );
				message.append( customerName );
				message.append( '\n' );
			}

			if ( soId != null ) {
				message.append( "soId: " );
				message.append( soId );
				message.append( '\n' );
			}
			
			if ( soName != null ) {
				message.append( "SO: " );
				message.append( soName );
				message.append( '\n' );
			}

			if ( errorCode != null ) {
				message.append( "errorCode: " );
				message.append( errorCode );
				message.append( '\n' );				
			}
			
			if ( deliveryDate != null ) {
				message.append( "Next Delivery: " );
				message.append( deliveryDate);
				message.append( '\n' );				
			}
			if(null !=startTime && null != endTime){
				message.append("Delivery Time:");
				message.append(startTime+" - "+ endTime);
				message.append( '\n' );			
				
			}
			
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
			
			return true;
			
		} catch ( MessagingException e ) {
			LOGGER.error( "Failed to send out technical error report email!", e );
		}
		return false;
	}

	private static boolean sendTechnicalMail ( String msg ) {
		return sendTechnicalMail( msg, null, null, null, null, null );
	}
	
	public void persistUnavDetailsToDB(SOResult.ResultList resultList) throws FDResourceException{
			soManager.persistUnavailableDetailsToDB(resultList.getResultsList());	
	}
	
	public UnavDetailsReportingBean getDetailsForReportGeneration() throws FDResourceException {		
			return soManager.getDetailsForReportGeneration();	
	}

}	
