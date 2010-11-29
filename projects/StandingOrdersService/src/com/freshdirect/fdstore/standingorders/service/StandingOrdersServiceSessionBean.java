package com.freshdirect.fdstore.standingorders.service;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.freshdirect.delivery.restriction.FDRestrictedAvailabilityInfo;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDCompositeAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDMuniAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
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
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.rules.FDRuleContextI;
import com.freshdirect.fdstore.rules.FDRulesContextImpl;
import com.freshdirect.fdstore.standingorders.DeliveryInterval;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
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
		
		LOGGER.info( "Processing " + soList.size() + " standing orders." );
		for ( FDStandingOrder so : soList ) {
			Result result;
			try {
				
				result = process( so );
				
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
	
	private void sendSuccessMail ( FDStandingOrder so, FDCustomerInfo customerInfo, String orderId, boolean hasInvalidItems ) {
		try {
			FDOrderI order = FDCustomerManager.getOrder( orderId );
			XMLEmailI mail = FDEmailFactory.getInstance().createConfirmStandingOrderEmail( customerInfo, order, so, hasInvalidItems );		
			MailerGatewaySB mailer = mailerHome.create();
			mailer.enqueueEmail( mail );
		} catch ( FDResourceException e ) {
			LOGGER.error("sending success mail failed", e);
		} catch ( RemoteException e ) {
			LOGGER.error("sending success mail failed", e);
		} catch ( CreateException e ) {
			LOGGER.error("sending success mail failed", e);
		}
	}
	
	private void sendNotificationMail ( FDStandingOrder so, FDCustomerInfo customerInfo, String orderId ) {
		try {
			FDOrderI order = FDCustomerManager.getOrder( orderId );
			XMLEmailI mail = FDEmailFactory.getInstance().createConfirmDeliveryStandingOrderEmail( customerInfo, order, so );		
			MailerGatewaySB mailer = mailerHome.create();
			mailer.enqueueEmail( mail );
		} catch ( FDResourceException e ) {
			LOGGER.error("sending notification mail failed", e);
		} catch ( RemoteException e ) {
			LOGGER.error("sending notification mail failed", e);
		} catch ( CreateException e ) {
			LOGGER.error("sending notification mail failed", e);
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
	
	


	/**
	 * Processes a Standing Order.
	 * 
	 * The following steps are taken
	 * 
	 * 1. Checks basic info (customer)
	 * 2. Validates
	 *    a. Delivery address
	 *    b. Payment method
	 *    c. Delivery date and
	 *    d. Reserved timeslot
	 *    e. Extra validations (zone info)
	 * 3. Builds cart from ordered items
	 * 4. Checks alcoholic content
	 * 5. ATP Check (availability check)
	 * 6. Verify order minimum
	 * 7. Updates tax and bottle deposits
	 *      and delivery fees
	 * 8. Place order
	 * 
	 * 
	 * @param so Standing order to process
	 * @return
	 * 
	 * @throws FDResourceException
	 * 
	 */
	private StandingOrdersServiceResult.Result process( FDStandingOrder so ) throws FDResourceException {
		
		LOGGER.info( "Processing Standing Order : " + so );		
		
		// skip if : deleted or has some error - redundant check...
		if ( so == null || so.isDeleted() ) {
			LOGGER.info( "Skipping." );
			return new Result(Status.SKIPPED);
		}
		
		// checking if SO is in erroneous state
		Date now = new Date();
		if ( so.getLastError() != null ) {
			if ( now.after( so.getNextDeliveryDate() ) ) {
				// clearing the error since the delivery date has been passed
				// and the customer did not touch the erroneous SO so far
				so.clearLastError();
				try {
					FDActionInfo info = new FDActionInfo(EnumTransactionSource.STANDING_ORDER, so.getCustomerIdentity(),
							INITIATOR_NAME, "Resetting Standing Order Error Status", null);
					soManager.save( info, so );
				} catch (FDResourceException re) {
					invalidateFCHome();
					invalidateMailerHome();
					LOGGER.error( "Saving standing order failed! (FDResourceException)", re );
					return new Result( ErrorCode.TECHNICAL, ErrorCode.TECHNICAL.getErrorHeader(), "Cannot reset error state of SO.", null );
				}
			} else {
				// skipping because it is erroneous
				LOGGER.info( "Skipping." );
				return new Result(Status.SKIPPED);
			}
		}
		
		// First of all : check if next delivery date is passed for whatever reason,
		// and step it to the first possible date which is in the future
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
			LOGGER.warn( "Failed to retreive customer information.", e );
		}
		
		if ( customer == null || customerInfo == null || customerUser == null ) {
			LOGGER.warn( "No valid customer." );
			return new Result( ErrorCode.TECHNICAL, ErrorCode.TECHNICAL.getErrorHeader(), "No valid customer.", customerInfo );
		}
		
		LOGGER.info( "Customer information is valid." );



		// =============================
		//   Validate delivery address
		// =============================
		
		String deliveryAddressId = so.getAddressId();
		AddressModel deliveryAddressModel = FDCustomerManager.getAddress( customer, deliveryAddressId );
		
		if ( deliveryAddressModel == null ) {
			LOGGER.warn( "No delivery address found for this ID. ["+deliveryAddressId+"]" );
			return new Result( ErrorCode.ADDRESS, customerInfo, customerUser );
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
			return new Result( ErrorCode.ADDRESS, customerInfo, customerUser );
		}
		
		// continue with the scrubbed address
		deliveryAddressModel = addressValidator.getScrubbedAddress();
		
		if ( deliveryAddressModel == null ) {
			LOGGER.warn( "No valid delivery address." );
			return new Result( ErrorCode.ADDRESS, customerInfo, customerUser );
		}
		
		LOGGER.info( "Delivery address is valid: " + deliveryAddressModel );
		
		
		
		// ============================
		//   Validate payment methods
		// ============================
		
		String paymentMethodID = so.getPaymentMethodId();
		
		if ( paymentMethodID == null || paymentMethodID.trim().equals( "" ) ) {
			LOGGER.warn( "No payment method id." );
			return new Result( ErrorCode.PAYMENT, customerInfo, customerUser );
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
			return new Result( ErrorCode.PAYMENT, customerInfo, customerUser );
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
			return new Result( ErrorCode.PAYMENT, customerInfo, customerUser );
		}
		
		LOGGER.info( "Payment method is valid: " + paymentMethod );


		
			
		// ============================
		//    Validate delivery date
		// ============================
		
		
		DeliveryInterval deliveryTimes;		
		try {
			deliveryTimes = new DeliveryInterval( so );
		} catch ( IllegalArgumentException ex ) {
			LOGGER.warn( "No valid dates." );
			return new Result( ErrorCode.TIMESLOT, customerInfo, customerUser );
		}

		// check if we are within the delivery window
		if ( !deliveryTimes.isWithinDeliveryWindow() ) {
			LOGGER.info( "Skipping order because delivery date falls outside of the current delivery window." );
			return new Result(Status.SKIPPED);
		}		


		
		// ==================================
		//   Validate Timeslot reservation
		// ==================================
		
		
		// WARNING: getAllTimeslotsForDateRange-s select will ignore houre:minute in start/end dates!
		List<FDTimeslot> timeslots = FDDeliveryManager.getInstance().getAllTimeslotsForDateRange( deliveryTimes.getDayStart(), deliveryTimes.getDayEnd(), deliveryAddressModel );
				
		if ( timeslots == null || timeslots.size() == 0 ) {
			LOGGER.info( "No timeslots for this day: " + FDStandingOrder.DATE_FORMATTER.format( deliveryTimes.getDayStart() ) );
			return new Result( ErrorCode.TIMESLOT, customerInfo, customerUser );
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
			return new Result( ErrorCode.TIMESLOT, customerInfo, customerUser );
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
			LOGGER.info( "Invalid zone info. - FDInvalidAddressException", e );
			return new Result( ErrorCode.ADDRESS, customerInfo, customerUser );
		}
		if ( zoneInfo == null ) {
			LOGGER.info( "Missing zone info." );
			return new Result( ErrorCode.ADDRESS, customerInfo, customerUser );			
		}



		// ==========================
		//    Build Cart
		// ==========================
		
		ProcessActionResult vr = new ProcessActionResult();
		FDCartModel cart = buildCart(so.getCustomerList(), paymentMethod, deliveryAddressModel, timeslots, zoneInfo, reservation, vr);
		boolean hasInvalidItems = vr.hasInvalidItems();
		
		// set users shopping cart, needed for delivery fee rules later
		customerUser.setShoppingCart( cart );
		

		// ==========================
		//    Check for alcohol
		// ==========================
		if ( cart.containsAlcohol() ) {
			if ( so.isAlcoholAgreement() ) {
				cart.setAgeVerified( true );				
			} else {
				LOGGER.info( "Shopping list contains alcohol, and age was not verified." );
				return new Result( ErrorCode.ALCOHOL, customerInfo, customerUser );
			}
		}


		// ==========================
		//    Check availability
		//       (ATP Check)
		// ==========================
		vr = new ProcessActionResult();
		doATPCheck(customer, cart, vr);
		if (vr.hasInvalidItems()) {
			hasInvalidItems = true;
		}
		LOGGER.info( "ATP check passed." );


		// ==========================
		//    Verify order minimum
		// ==========================
		double cartPrice = cart.getSubTotal();
		double minimumOrder = customerUser.getMinimumOrderAmount();
		if ( cartPrice < minimumOrder ) {
			String msg = "The order subtotal ($"+cartPrice+") was below our $"+minimumOrder+" minimum."; 
			LOGGER.info( msg );
			return new Result( ErrorCode.MINORDER, msg, ErrorCode.MINORDER.getErrorDetail(customerUser), customerInfo );
		}
		
		LOGGER.info( "Cart contents are valid." );


		// ==========================
		//    Update Tax and Bottle deposits
		//    Update Delivery Fees
		// ==========================
		cart.recalculateTaxAndBottleDeposit(deliveryAddressModel.getZipCode());
		updateDeliverySurcharges(cart, new FDRulesContextImpl(customerUser));


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
			LOGGER.info( "DeliveryPassException while placing order.", e );
			return new Result( ErrorCode.PAYMENT, customerInfo, customerUser );
		} catch ( ErpFraudException e ) {
			LOGGER.info( "ErpFraudException while placing order.", e );
			return new Result( ErrorCode.PAYMENT, customerInfo, customerUser );
		} catch ( ErpAuthorizationException e ) {
			LOGGER.info( "ErpAuthorizationException while placing order.", e );
			return new Result( ErrorCode.PAYMENT, customerInfo, customerUser );
		} catch ( FDPaymentInadequateException e ) {
			LOGGER.info( "FDPaymentInadequateException while placing order.", e );
			return new Result( ErrorCode.PAYMENT, customerInfo, customerUser );
		} catch ( ErpTransactionException e ) {
			LOGGER.info( "ErpTransactionException while placing order.", e );
			return new Result( ErrorCode.PAYMENT, customerInfo, customerUser );
		} catch ( ReservationException e ) {
			LOGGER.info( "ReservationException while placing order.", e );
			return new Result( ErrorCode.TIMESLOT, customerInfo, customerUser );
		} catch ( ErpAddressVerificationException e ) {
			LOGGER.info( "ErpAddressVerificationException (Payment Address) while placing order.", e );
			return new Result( ErrorCode.PAYMENT_ADDRESS, customerInfo, customerUser );
		}
	}


	
	
	
	protected boolean isValidCustomerList(List<FDCustomerListItem> lineItems) throws FDResourceException {
		return OrderLineUtil.isValidCustomerList( lineItems );
	}
	
	protected FDCartModel checkAvailability(FDIdentity identity, FDCartModel cart, long timeout) throws FDResourceException {
		return FDCustomerManager.checkAvailability( identity, cart, timeout );
	}

	public List<FDProductSelectionI> getValidProductSelectionsFromCCLItems(List<FDCustomerListItem> cclItems) throws FDResourceException {
		return OrderLineUtil.getValidProductSelectionsFromCCLItems( cclItems );
	}


	/**
	 * Update delivery surcharges
	 * 
	 * @param cart
	 * @param ctx This is typically result of new FDRulesContextImpl(FDUserI user)
	 */
	public void updateDeliverySurcharges(FDCartModel cart, FDRuleContextI ctx) {
		cart.updateSurcharges(ctx);
	}


	protected FDCartModel buildCart(FDCustomerList soList, ErpPaymentMethodI paymentMethod, AddressModel deliveryAddressModel, List<FDTimeslot> timeslots, DlvZoneInfoModel zoneInfo, FDReservation reservation, ProcessActionResult vr) throws FDResourceException {
		FDCartModel cart = new FDTransientCartModel();
		
		if ( ! isValidCustomerList( soList.getLineItems() ) ) {
			LOGGER.info( "Shopping list contains some unavailable/invalid items." );
			vr.increment();
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
					vr.increment();
				}
			}
			cart.refreshAll();			
		} catch ( FDInvalidConfigurationException e ) {
			LOGGER.info( "Shopping list contains some items with invalid configuration." );
			vr.increment();
			// This is not an error
			// return new Result( ErrorCode.CART, "Shopping list contains some items with invalid configuration.", customerInfo );
		}
		
		
		return cart;
	}
	

	/**
	 * Perform ATP check on cart items
	 * ===============================
	 * 
	 * It runs an availability check then remove troubled items
	 * except when less than (but not zero) amount
	 * is available of a certain cart line item than wanted.
	 * In that case quantity will be adjusted.
	 * 
	 * Note: code is extracted from step_2_check.jsp and
	 * step_2_unavail.jsp files.
	 * 
	 * @param customer
	 * @param cart
	 * @return
	 * @throws FDResourceException
	 */
	protected boolean doATPCheck(FDIdentity customer, FDCartModel cart, ProcessActionResult vr)
			throws FDResourceException {
		// Cart ATP check ...
		//
		cart = checkAvailability( customer, cart, 30000 );
		Map<String,FDAvailabilityInfo> invsInfoMap = cart.getUnavailabilityMap();
		
		
		// Iterate through troubled items by their cartLineID
		for (String key : invsInfoMap.keySet()) {
			FDAvailabilityInfo info = invsInfoMap.get(key);
			final Integer randomId = new Integer(key);
			FDCartLineI cartLine = cart.getOrderLineById(randomId);

			if (info instanceof FDRestrictedAvailabilityInfo) {
				/**
				 * Cause:  restriction problem
				 * Effect: remove cartLine item
				 */
				LOGGER.debug("[ATP CHECK/1] Item " + cartLine.getRandomId() + " / '" + cartLine.getProductName() + "' has restriction: " + ((FDRestrictedAvailabilityInfo)info).getRestriction().getReason());
				
				/*** EnumDlvRestrictionReason rsn = ((FDRestrictedAvailabilityInfo)info).getRestriction().getReason();
				if (EnumDlvRestrictionReason.KOSHER.equals(rsn)) {
					// Kosher production item</a> - not available Fri, Sat, Sun AM, and holidays
					LOGGER.debug("Item " + cartLine.getProductName() + "/" + cartLine.getCartlineId() + " is Kosher product");
				} else {
					// Restriction message: ((FDRestrictedAvailabilityInfo)info).getRestriction().getMessage()
				} ***/

				vr.increment();
				cart.removeOrderLineById(randomId);
			} else if (info instanceof FDStockAvailabilityInfo) {
				/**
				 * Cause:  less or zero amount is available
				 * Effect: remove cartLine item if qty == 0
				 *    otherwise adjust item to available qty
				 */

				// Limited quantity zero or less than desired amount available
				double availQty = ((FDStockAvailabilityInfo)info).getQuantity();
				LOGGER.debug("[ATP CHECK/2] Item " + cartLine.getRandomId() + " / '" + cartLine.getProductName() + "' has only " + availQty + " items available.");
				if (availQty > 0) {
					// adjust quantity to amount of available
					cart.getOrderLineById(randomId).setQuantity(availQty);
				} else {
					vr.increment();
					cart.removeOrderLineById(randomId);
				}
			} else if (info instanceof FDCompositeAvailabilityInfo) {
				/**
				 * Cause:  some options are unavailable
				 * Effect: remove cartLine item
				 */
				LOGGER.debug("[ATP CHECK/3] Item " + cartLine.getRandomId() + " / '" + cartLine.getProductName() + "' has problem with its options.");

				// The following options are unavailable: ...
				//
				/**** Map<String,FDAvailabilityInfo> componentInfos = ((FDCompositeAvailabilityInfo)info).getComponentInfo();
				boolean singleOptionIsOut= false;
				for (Iterator<Entry<String, FDAvailabilityInfo>> i = componentInfos.entrySet().iterator(); i.hasNext(); ) {
					Map.Entry<String, FDAvailabilityInfo> e = i.next();
					String componentKey = (String)e.getKey();
					if (componentKey != null) {
						FDAvailabilityInfo componentInfo = (FDAvailabilityInfo)e.getValue();

						FDProduct fdp = cartLine.lookupFDProduct();
						String matNo = StringUtils.right(componentKey, 9);
						FDVariationOption option = fdp.getVariationOption(matNo);
						if (option != null) {
							/// Print missing option.getDescription()

							// Check to see if this option is the only option for the variation
							FDVariation[] vars = fdp.getVariations();
							for (int vi=0; vi <vars.length;vi++){
								FDVariation aVar = vars[vi];
								if (vars[vi].getVariationOption(matNo)!=null && vars[vi].getVariationOptions().length>0) {
								    singleOptionIsOut = true;
								}
							}
							
						}
					}
				}
				
				if (!singleOptionIsOut) {
					// JSP: go to modify other options...
				} ****/


				vr.increment();
				cart.removeOrderLineById(randomId);
			} else if (info instanceof FDMuniAvailabilityInfo) {
				/**
				 * Cause:  'FreshDirect does not deliver alcohol outside NY'
				 * Effect: remove cartLine item
				 */
				LOGGER.debug("[ATP CHECK/4] Item " + cartLine.getRandomId() + " / '" + cartLine.getProductName() + "' -- 'FreshDirect does not deliver alcohol outside NY'");

				/// final MunicipalityInfo muni = ((FDMuniAvailabilityInfo)info).getMunicipalityInfo();
				//
				vr.increment();
				cart.removeOrderLineById(randomId);
			} else { /* info.isa? {@link FDStatusAvailabilityInfo} */
				/**
				 * Cause:  OUT OF STOCK
				 * Effect: remove cartLine item
				 */
				LOGGER.debug("[ATP CHECK/5] Item " + cartLine.getRandomId() + " / '" + cartLine.getProductName() + "' OUT OF STOCK");

				vr.increment();
				cart.removeOrderLineById(randomId);
			}
		}

		return vr.hasInvalidItems();
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
			LOGGER.error("authentication error while sending notification mail", e);
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
	

	


	public static class ProcessActionResult {
		private int counter = 0;
		
		public void increment() {
			counter++;
		}
	
		public boolean hasInvalidItems() {
			return counter > 0;
		}
		
		@Override
		public String toString() {
			return "Bad items: " + counter + " -> " + hasInvalidItems();
		}
	}
}
