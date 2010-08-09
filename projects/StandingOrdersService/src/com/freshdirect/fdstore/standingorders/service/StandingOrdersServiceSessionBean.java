package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.ReservationUnavailableException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDTransientCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.customer.ejb.FDCustomerHome;
import com.freshdirect.fdstore.lists.FDStandingOrderList;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.standingorders.DeliveryInterval;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode;
import com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceResult.Result;
import com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceResult.Status;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.webapp.taglib.fdstore.DeliveryAddressValidator;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;


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
			re.printStackTrace();
			LOGGER.error( "Could not retrieve standing orders list! - FDResourceException" );
			sendTechnicalMail( "Could not retrieve standing orders list! - FDResourceException" );
			return null;
		}
		
		if ( soList == null ) {
			LOGGER.error( "Could not retrieve standing orders list! - loadActiveStandingOrders() returned null" );
			sendTechnicalMail( "Could not retrieve standing orders list! - loadActiveStandingOrders() returned null" );
			return null;
		}
		
		StandingOrdersServiceResult.Counter resultCounter = new StandingOrdersServiceResult.Counter();
		
		LOGGER.info( "Processing " + soList.size() + " standing orders." );
		for ( FDStandingOrder so : soList ) {
			Result result;
			try {
				
				result = process( so );
				
			} catch (FDResourceException re) {
				invalidateFCHome();
				invalidateMailerHome();
				re.printStackTrace();
				LOGGER.error( "Processing standing order failed with FDResourceException!" );
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
					re.printStackTrace();
					LOGGER.error( "Saving standing order failed! (FDResourceException)" );
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
	
	private void sendSuccessMail ( FDStandingOrder so, FDCustomerInfo customerInfo, String orderId, boolean hasInvalidItems ) {
		try {
			FDOrderI order = FDCustomerManager.getOrder( orderId );
			XMLEmailI mail = FDEmailFactory.getInstance().createConfirmStandingOrderEmail( customerInfo, order, so, hasInvalidItems );		
			MailerGatewaySB mailer = mailerHome.create();
			mailer.enqueueEmail( mail );
		} catch ( FDResourceException e ) {
			e.printStackTrace();
		} catch ( RemoteException e ) {
			e.printStackTrace();
		} catch ( CreateException e ) {
			e.printStackTrace();
		}
	}
	
	private void sendNotificationMail ( FDStandingOrder so, FDCustomerInfo customerInfo, String orderId ) {
		try {
			FDOrderI order = FDCustomerManager.getOrder( orderId );
			XMLEmailI mail = FDEmailFactory.getInstance().createConfirmDeliveryStandingOrderEmail( customerInfo, order, so );		
			MailerGatewaySB mailer = mailerHome.create();
			mailer.enqueueEmail( mail );
		} catch ( FDResourceException e ) {
			e.printStackTrace();
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
			String message = "Standing Orders background process failed." + msg + " Please check the server log for more details...";
			
			mailer.sendMail(from, recipient, "", subject, message );
			
		} catch ( MessagingException e ) {
			LOGGER.error( "Failed to send out technical error report email!" );
			e.printStackTrace();
		}
	}
	
	private StandingOrdersServiceResult.Result process( FDStandingOrder so ) throws FDResourceException {
		
		LOGGER.info( "Processing Standing Order : " + so );		
		
		// skip if : deleted or has some error - redundant check...
		if ( so == null || so.isDeleted() || so.getLastError() != null ) {
			LOGGER.info( "Skipping." );
			return new Result(Status.SKIPPED);
		}
		
		// First of all : check if next delivery date is passed for whatever reason,
		// and step it to the first possible date which is in the future
		Date now = new Date();
		while ( now.after( so.getNextDeliveryDate() ) ) {
			LOGGER.info( "Skipping delivery date, because it is in the past." );
			so.skipDeliveryDate();
		}
		
		// =====================
		//  2days notification 
		// =====================
		
		sendNotification( so );
		
		// ==========================
		//  Customer related infos
		// ==========================
		
		FDIdentity customer = null;
		FDCustomerInfo customerInfo = null;
		FDUserI customerUser = null;
		try {
			customer = so.getCustomerIdentity();
			customerInfo = so.getUserInfo();
			customerUser = so.getUser();
		} catch ( FDAuthenticationException e ) {
			e.printStackTrace();
			LOGGER.warn( "Failed to retreive customer information.", e );
		}
		
		if ( customer == null || customerInfo == null || customerUser == null ) {
			LOGGER.warn( "No valid customer." );
			return new Result( ErrorCode.TECHNICAL, ErrorCode.TECHNICAL.getErrorHeader(), "No valid customer.", customerInfo );
		}
		
		LOGGER.info( "Customer information is valid." );
		

		
		// ==========================
		//   Delivery address ...
		// ==========================
		
		String deliveryAddressId = so.getAddressId();
		AddressModel deliveryAddressModel = FDCustomerManager.getAddress( customer, deliveryAddressId );
		
		if ( deliveryAddressModel == null ) {
			LOGGER.warn( "No delivery address found for this ID. ["+deliveryAddressId+"]" );
			return new Result( ErrorCode.ADDRESS, customerInfo );
		}
		
		DeliveryAddressValidator addressValidator = new DeliveryAddressValidator( deliveryAddressModel );
		ActionResult addressValidatorResult = new ActionResult();
		
		try { 
			addressValidator.validateAddress( addressValidatorResult );
		} catch (FDResourceException e) {
			LOGGER.warn( "Address validation failed with FDResourceException ", e );
			return new Result( ErrorCode.TECHNICAL, ErrorCode.TECHNICAL.getErrorHeader(), "Address validation failed.", customerInfo );
		}
		
		if ( addressValidatorResult.isFailure() ) {
			LOGGER.warn( "Address validation failed : " + addressValidatorResult.getFirstError().getDescription() );
			return new Result( ErrorCode.ADDRESS, customerInfo );
		}
		
		// continue with the scrubbed address
		deliveryAddressModel = addressValidator.getScrubbedAddress();
		
		if ( deliveryAddressModel == null ) {
			LOGGER.warn( "No valid delivery address." );
			return new Result( ErrorCode.ADDRESS, customerInfo );
		}
		
		LOGGER.info( "Delivery address is valid: " + deliveryAddressModel );
		
		// These are already handled by the DeliveryAddressValidator :
		
//		DlvServiceSelectionResult checkAddressResult = null;
//		try {
//			checkAddressResult = FDDeliveryManager.getInstance().checkAddress( deliveryAddressModel );
//		} catch ( FDInvalidAddressException e1 ) {
//			so.setLastError( "Address is invalid : " + e1.getMessage() );
//			return Status.FAILURE;
//		}
//		
//		if ( checkAddressResult.isServiceRestricted() ) {
//			so.setLastError( "Address is restricted : " + checkAddressResult.getRestrictionReason().getDescription() );
//			return Status.FAILURE;			
//		}
//	
//		EnumDeliveryStatus deliveryStatus = checkAddressResult.getServiceStatus( deliveryAddressModel.getServiceType() );
//		// what to do with this result??
		
		
		
		// ==========================
		//   Payment method ...
		// ==========================
		
		String paymentMethodID = so.getPaymentMethodId();
		
		if ( paymentMethodID == null || paymentMethodID.trim().equals( "" ) ) {
			LOGGER.warn( "No payment method id." );
			return new Result( ErrorCode.PAYMENT, customerInfo );
		}

		ErpPaymentMethodI paymentMethod = null;
		try { 
			paymentMethod = FDCustomerManager.getPaymentMethod( customer, paymentMethodID );
		} catch (FDResourceException e) {
			LOGGER.warn( "FDResourceException while getting payment method", e );
			return new Result( ErrorCode.TECHNICAL, ErrorCode.TECHNICAL.getErrorHeader(), "FDResourceException while getting payment method.", customerInfo );
		}
		
		if ( paymentMethod == null ) {
			LOGGER.warn( "No valid payment method id." );
			return new Result( ErrorCode.PAYMENT, customerInfo );
		}

		ActionResult paymentValidatorResult = new ActionResult();
		try {
			PaymentMethodUtil.validatePaymentMethod( paymentMethod, paymentValidatorResult, customerUser, false, false, false );
		} catch (FDResourceException e) {
			LOGGER.warn( "FDResourceException while validating payment method", e );
			return new Result( ErrorCode.TECHNICAL, ErrorCode.TECHNICAL.getErrorHeader(), "FDResourceException while validating payment method.", customerInfo );
		}

		if ( paymentValidatorResult.isFailure() ) {
			LOGGER.warn( "Payment method not valid: " + paymentValidatorResult.getFirstError().getDescription() );
			return new Result( ErrorCode.PAYMENT, customerInfo );
		}
		
		LOGGER.info( "Payment method is valid: " + paymentMethod );



//		paymentMethod.setBillingRef( billingRef );
//		paymentMethod.setPaymentType( makeGoodOrder ? EnumPaymentType.MAKE_GOOD : EnumPaymentType.REGULAR );
//		paymentMethod.setReferencedOrder( referencedOrder );

//		if ( user.isDepotUser() ) {
//			if ( user.isEligibleForSignupPromotion() ) {
//				if ( FDCustomerManager.checkBillToAddressFraud( info, paymentMethod ) ) {
//
//					session.setAttribute( SessionName.SIGNUP_WARNING, MessageFormat.format( SystemMessageList.MSG_NOT_UNIQUE_INFO, new Object[] { user.getCustomerServiceContact() } ) );
//
//				}
//			}
//		}
		
//		FDCustomerCreditUtil.applyCustomerCredit( cart, identity );
		
//		UserValidationUtil.validateOrderMinimum(); ???
		
		
			
		// ==========================
		//    Delivery date ...
		// ==========================
		
		
		DeliveryInterval deliveryTimes;		
		try {
			deliveryTimes = new DeliveryInterval( so );
		} catch ( IllegalArgumentException ex ) {
			LOGGER.warn( "No valid dates." );
			return new Result( ErrorCode.TIMESLOT, customerInfo );
		}

		// check if we are within the delivery window
		if ( !deliveryTimes.isWithinDeliveryWindow() ) {
			LOGGER.info( "Skipping order because delivery date falls outside of the current delivery window." );
			return new Result(Status.SKIPPED);
		}		


		
		// ==========================
		//   Timeslot reservation
		// ==========================
		
		
		// WARNING: getAllTimeslotsForDateRange-s select will ignore houre:minute in start/end dates!
		List<FDTimeslot> timeslots = FDDeliveryManager.getInstance().getAllTimeslotsForDateRange( deliveryTimes.getDayStart(), deliveryTimes.getDayEnd(), deliveryAddressModel );
				
		if ( timeslots == null || timeslots.size() == 0 ) {
			LOGGER.info( "No timeslots for this day: " + FDStandingOrder.DATE_FORMATTER.format( deliveryTimes.getDayStart() ) );
			return new Result( ErrorCode.TIMESLOT, customerInfo );
		}
		
		
		// making a reservation ...
		FDActionInfo reserveActionInfo = new FDActionInfo( EnumTransactionSource.STANDING_ORDER, customer, INITIATOR_NAME, "Reserving timeslot for Standing Order", null );
		
		FDReservation reservation = null;
		FDTimeslot selectedTimeslot = null;
		for ( FDTimeslot timeslot : timeslots ) {
			
			if ( !deliveryTimes.checkTimeslot( timeslot ) ) {
				// this timeslot falls outside the required time interval, or is past the cutoff time, skip it
				LOGGER.info( "Skipping timeslot: " + timeslot.toString() );
				continue;
			}
						
			try { 
				LOGGER.info( "Trying to make reservation for timeslot: " + timeslot.toString() );
				reservation = FDCustomerManager.makeReservation( customer, timeslot, EnumReservationType.STANDARD_RESERVATION, deliveryAddressId, reserveActionInfo, false );
				selectedTimeslot = timeslot;
				LOGGER.info( "Timeslot reserved successfully: " + timeslot.toString() );
			} catch ( ReservationUnavailableException e ) {
				// no more capacity in this timeslot
				LOGGER.info( "No more capacity in timeslot: " + timeslot.toString(), e );
			} catch (ReservationException e) {
				// other error
				LOGGER.warn( "Reservation failed for timeslot: " + timeslot.toString(), e );
			}
			if ( reservation != null ) 
				break;
		}
		
		if ( reservation == null || selectedTimeslot == null ) {
			LOGGER.warn( "Failed to make timeslot reservation." );
			return new Result( ErrorCode.TIMESLOT, customerInfo );
		}
		
		LOGGER.info( "Selected timeslot = " + selectedTimeslot.toString() );
		LOGGER.info( "Timesot reservation = " + reservation.toString() );
		
	
				
		// ==========================
		//    Extra validations
		// ==========================
		
		DlvZoneInfoModel zoneInfo = null;
		try {
			zoneInfo = FDDeliveryManager.getInstance().getZoneInfo(deliveryAddressModel, selectedTimeslot.getBegDateTime() );
		} catch (FDInvalidAddressException e) {
			e.printStackTrace();
			LOGGER.info( "Invalid zone info. - FDInvalidAddressException" );
			return new Result( ErrorCode.ADDRESS, customerInfo );
		}
		if ( zoneInfo == null ) {
			LOGGER.info( "Missing zone info." );
			return new Result( ErrorCode.ADDRESS, customerInfo );			
		}
		
		FDStandingOrderList soList = so.getCustomerList();
		
		// build a cart
		FDCartModel cart = new FDTransientCartModel();
		boolean hasInvalidItems = false;

		if ( ! OrderLineUtil.isValidCustomerList( soList.getLineItems() ) ) {
			LOGGER.info( "Shopping list contains some unavailable/invalid items." );
			hasInvalidItems = true;
			// This is not an error
			//return new Result( ErrorCode.CART, "Shopping list contains some unavailable/invalid items.", customerInfo );
		}
		
		// set cart parameters		
		cart.setPaymentMethod( paymentMethod );
		
		ErpAddressModel erpDeliveryAddress;
		if ( deliveryAddressModel instanceof ErpAddressModel ) {
			erpDeliveryAddress = (ErpAddressModel)deliveryAddressModel;
		} else {
			erpDeliveryAddress = new ErpAddressModel( deliveryAddressModel );
		}
		
		cart.setDeliveryAddress( erpDeliveryAddress );		
		cart.setDeliveryReservation( reservation );
        cart.setZoneInfo( zoneInfo );
        
        // fill the cart with items
		List<FDProductSelectionI> productSelectionList = OrderLineUtil.getValidProductSelectionsFromCCLItems( soList.getLineItems() );
		
		try {
			for ( FDProductSelectionI ps : productSelectionList ) {
				FDCartLineI cartLine = new FDCartLineModel( ps );
				if ( !cartLine.isInvalidConfig() ) {
					cart.addOrderLine( cartLine );
				} else {
					hasInvalidItems = true;
				}
			}
			cart.refreshAll();			
		} catch ( FDInvalidConfigurationException e ) {
			LOGGER.info( "Shopping list contains some items with invalid configuration." );
			hasInvalidItems = true;
			// This is not an error
			// return new Result( ErrorCode.CART, "Shopping list contains some items with invalid configuration.", customerInfo );
		}

		// check for alcohol
		if ( cart.containsAlcohol() ) {
			if ( so.isAlcoholAgreement() ) {
				cart.setAgeVerified( true );				
			} else {
				LOGGER.info( "Shopping list contains alcohol, and age was not verified." );
				return new Result( ErrorCode.ALCOHOL, customerInfo );
			}
		}
		
		// verify order minimum
		double cartPrice = cart.getSubTotal();
		double minimumOrder = customerUser.getMinimumOrderAmount();
		if ( cartPrice < minimumOrder ) {
			String msg = "The order subtotal ($"+cartPrice+") was below our $"+minimumOrder+" minimum."; 
			LOGGER.info( msg );
			return new Result( ErrorCode.MINORDER, msg, ErrorCode.MINORDER.getErrorDetail(), customerInfo );
		}
		
		LOGGER.info( "Cart contents are valid." );
	

		
		// ==========================
		//    Placing the order
		// ==========================
		
		try {
			FDActionInfo orderActionInfo = new FDActionInfo( EnumTransactionSource.STANDING_ORDER, customer, INITIATOR_NAME, "Placing order for Standing Order", null );
			CustomerRatingI cra = new CustomerRatingAdaptor( customerUser.getFDCustomer().getProfile(), customerUser.isCorporateUser(), customerUser.getAdjustedValidOrderCount() );
			String orderId = FDCustomerManager.placeOrder( orderActionInfo, cart, null, false, cra, null );

			try {
				FDStandingOrdersManager.getInstance().assignStandingOrderToSale(orderId, so);
			} catch (FDResourceException e) {
				LOGGER.error("Failed to assign standing order to sale, corresponding order ID="+orderId, e);
				LOGGER.warn( e.getFDStackTrace() );
			}
			
			LOGGER.info( "Order placed successfully. OrderId = " + orderId );
			
			sendSuccessMail( so, customerInfo, orderId, hasInvalidItems );
			
			// step delivery date 
			so.skipDeliveryDate();
			
			return new Result(Status.SUCCESS, hasInvalidItems, orderId);
			
		} catch ( DeliveryPassException e ) {
			e.printStackTrace();
			LOGGER.info( "DeliveryPassException while placing order." );
			return new Result( ErrorCode.PAYMENT, customerInfo );
		} catch ( ErpFraudException e ) {
			e.printStackTrace();
			LOGGER.info( "ErpFraudException while placing order." );
			return new Result( ErrorCode.PAYMENT, customerInfo );
		} catch ( ErpAuthorizationException e ) {
			e.printStackTrace();
			LOGGER.info( "ErpAuthorizationException while placing order." );
			return new Result( ErrorCode.PAYMENT, customerInfo );
		} catch ( FDPaymentInadequateException e ) {
			LOGGER.info( "FDPaymentInadequateException while placing order." );
			return new Result( ErrorCode.PAYMENT, customerInfo );
		} catch ( ErpTransactionException e ) {
			e.printStackTrace();
			LOGGER.info( "ErpTransactionException while placing order." );
			return new Result( ErrorCode.PAYMENT, customerInfo );
		} catch ( ReservationException e ) {
			e.printStackTrace();
			LOGGER.info( "ReservationException while placing order." );
			return new Result( ErrorCode.TIMESLOT, customerInfo );
		} catch ( ErpAddressVerificationException e ) {
			e.printStackTrace();
			LOGGER.info( "ErpAddressVerificationException while placing order." );
			return new Result( ErrorCode.ADDRESS, customerInfo );
		}
	}
	
	private void sendNotification( FDStandingOrder so ) throws FDResourceException {		
		try {
			List<FDOrderInfoI> orders = so.getAllOrders();
			for ( FDOrderInfoI order : orders ) {
				Date notificationDate = subtract2WorkDays( order.getRequestedDate() );
				if ( isWithin24Hours( notificationDate ) ) {
					LOGGER.info( "Sending 2days notification email: so["+so.getId()+"], order["+order.getErpSalesId()+"]" );
					sendNotificationMail( so, so.getUserInfo(), order.getErpSalesId() );
				}
			}			
		} catch (FDAuthenticationException e) {
			e.printStackTrace();
		}		
	}
	
	private static Date subtract2WorkDays( Date d ) {
		Calendar c = Calendar.getInstance();
		c.setTime( d );
		int i = 2;
		while ( i > 0 ) {
			if ( !( c.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY ) && !( c.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY ) ) {
				i--;
			}
			c.add( Calendar.DATE, -1 );
		}
		return c.getTime();
	}
	
	private static boolean isWithin24Hours( Date d ) {
		Calendar c = Calendar.getInstance();
		c.setTime( d );
		Calendar now = Calendar.getInstance();
		now.setTime( new Date() );
		if ( c.after( now ) ) {
			now.add( Calendar.DATE, 1 );
			if ( c.before( now ) ) {
				return true;
			}
		}
		return false;
	}
	
}
