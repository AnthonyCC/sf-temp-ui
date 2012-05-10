package com.freshdirect.webapp.util;


import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.ReservationUnavailableException;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.FDRestrictedAvailabilityInfo;
import com.freshdirect.delivery.restriction.GeographyRestriction;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDProductInfo;
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
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDTransientCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.QuickCart;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.rules.FDRuleContextI;
import com.freshdirect.fdstore.rules.FDRulesContextImpl;
import com.freshdirect.fdstore.standingorders.DeliveryInterval;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.StandingOrdersServiceResult;
import com.freshdirect.fdstore.standingorders.StandingOrdersServiceResult.Result;
import com.freshdirect.fdstore.standingorders.StandingOrdersServiceResult.Status;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.DeliveryAddressValidator;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class StandingOrderUtil {
	
	private final static Category LOGGER = LoggerFactory.getInstance(StandingOrderUtil.class);
	
	public static final String INITIATOR_NAME = "Standing Order Service";
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
	public static StandingOrdersServiceResult.Result process( FDStandingOrder so, Date altDate, TimeslotEventModel event, FDActionInfo info, MailerGatewayHome mailerHome, boolean forceCapacity, boolean createIfSoiExistsForWeek) throws FDResourceException {
		
		LOGGER.info( "Processing Standing Order : " + so );		
		
		Result result = new Result(so);
		
		// skip if : deleted or has some error - redundant check...
		if ( so == null || so.isDeleted() ) {
			LOGGER.info( "Skipping." );
			return result.withStatus(Status.SKIPPED);
		}
		
		// checking if SO is in erroneous state
		Date now = new Date();
		if ( so.getLastError() != null ) {
			if ( now.after( so.getNextDeliveryDate() ) ) {
				// clearing the error since the delivery date has been passed
				// and the customer did not touch the erroneous SO so far
				so.clearLastError();
				try {
					if(info == null){
						info = new FDActionInfo(EnumTransactionSource.STANDING_ORDER, so.getCustomerIdentity(),
							INITIATOR_NAME, "Resetting Standing Order Error Status", null);
					}else{
						info.setNote( "Resetting Standing Order Error Status");
					}
					FDStandingOrdersManager.getInstance().save( info, so );
				} catch (FDResourceException re) {
					LOGGER.error( "Saving standing order failed! (FDResourceException)", re );
					return result.withTechError("Cannot reset error state of SO.");
				}
			} else {
				// skipping because it is erroneous
				LOGGER.info( "Skipping." );
				return result.withStatus(Status.SKIPPED);
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
		
		sendNotification( so, mailerHome );
		
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
		result.setCustomerData(customer.getErpCustomerPK(), customerInfo);
		
		if ( customer == null || customerInfo == null || customerUser == null ) {
			LOGGER.warn( "No valid customer." );
			return result.withTechError("No valid customer.");
		}
		
		LOGGER.info( "Customer information is valid." );


		// =====================================
		//   Check if SOI exists for given week
		// =====================================
		if (!createIfSoiExistsForWeek){
			int weekNumNewSoi = DateUtil.getWeekNumFromEpochFirstMonday(so.getNextDeliveryDate());
			String soId = so.getId();
			
			if(soId != null){
				for(FDOrderInfoI fDOrderInfo : ((FDOrderHistory)customerUser.getOrderHistory()).getStandingOrderInstances(soId)){
					int weekNumExistingSoi = DateUtil.getWeekNumFromEpochFirstMonday(fDOrderInfo.getCreateRequestedDate());
					if (weekNumNewSoi == weekNumExistingSoi){
						LOGGER.info( "Skipping, because SOI exists for given week." );
						return result.withStatus(Status.SKIPPED);
					}
				}
			}
		}
		

		// =============================
		//   Validate delivery address
		// =============================
		
		String deliveryAddressId = so.getAddressId();
		AddressModel deliveryAddressModel = FDCustomerManager.getAddress( customer, deliveryAddressId );
		
		
		if ( deliveryAddressModel == null ) {
			LOGGER.warn( "No delivery address found for this ID. ["+deliveryAddressId+"]" );
			return result.withUserRelatedError(ErrorCode.NO_ADDRESS, customerUser);
		}
		
		DeliveryAddressValidator addressValidator = new DeliveryAddressValidator( deliveryAddressModel );
		ActionResult addressValidatorResult = new ActionResult();
		
		try { 
			addressValidator.validateAddress( addressValidatorResult );
		} catch (FDResourceException e) {
			LOGGER.warn( "Address validation failed with FDResourceException ", e );
			return result.withTechError("Address validation failed.");
		}
		
		if ( addressValidatorResult.isFailure() ) {
			LOGGER.warn( "Address validation failed : " + addressValidatorResult.getFirstError().getDescription() );
			return result.withUserRelatedError(ErrorCode.ADDRESS, customerUser);
		}
		
		// continue with the scrubbed address
		deliveryAddressModel = addressValidator.getScrubbedAddress();
		
		if ( deliveryAddressModel == null ) {
			LOGGER.warn( "No valid delivery address." );
			return result.withUserRelatedError(ErrorCode.ADDRESS, customerUser);
		}
		
		LOGGER.info( "Delivery address is valid: " + deliveryAddressModel );
		
		
		
		// ============================
		//   Validate payment methods
		// ============================
		
		String paymentMethodID = so.getPaymentMethodId();
		
		if ( paymentMethodID == null || paymentMethodID.trim().equals( "" ) ) {
			LOGGER.warn( "No payment method id." );
			return result.withUserRelatedError(ErrorCode.PAYMENT, customerUser);
		}

		ErpPaymentMethodI paymentMethod = null;
		try { 
			paymentMethod = FDCustomerManager.getPaymentMethod( customer, paymentMethodID );
		} catch (FDResourceException e) {
			LOGGER.warn( "FDResourceException while getting payment method", e );
			return result.withTechError("FDResourceException while getting payment method.");
		}
		
		if ( paymentMethod == null ) {
			LOGGER.warn( "No valid payment method id." );
			return result.withUserRelatedError(ErrorCode.PAYMENT, customerUser);
		}

		ActionResult paymentValidatorResult = new ActionResult();
		try {
			PaymentMethodUtil.validatePaymentMethod(null,paymentMethod, paymentValidatorResult, customerUser, false, false, false,false,null );
		} catch (FDResourceException e) {
			LOGGER.warn( "FDResourceException while validating payment method", e );
			return result.withTechError("FDResourceException while validating payment method.");
		}

		if ( paymentValidatorResult.isFailure() ) {
			LOGGER.warn( "Payment method not valid: " + paymentValidatorResult.getFirstError().getDescription() );
			return result.withUserRelatedError(ErrorCode.PAYMENT, customerUser);
		}
		
		LOGGER.info( "Payment method is valid: " + paymentMethod );


		
			
		// ============================
		//    Validate delivery date
		// ============================
		
		
		DeliveryInterval deliveryTimes;		
		try {
			if ( null != altDate ) {
				deliveryTimes = new DeliveryInterval( altDate, so.getStartTime(), so.getEndTime() );
			} else {
				deliveryTimes = new DeliveryInterval( so );
			}			
		} catch ( IllegalArgumentException ex ) {
			LOGGER.warn( "No valid dates." );
			result.withUserRelatedError(ErrorCode.TIMESLOT, customerUser);
			return result;
		}

		// check if we are within the delivery window
		if ( !deliveryTimes.isWithinDeliveryWindow() ) {
			LOGGER.info( "Skipping order because delivery date falls outside of the current delivery window." );
			return result.withStatus(Status.SKIPPED);
		}		


		// =================================================
		//   Validate SO nextDelivery date is a holiday or closed
		// =================================================
		
		DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
		if(isNextDeliveryDateHolidayRestricted(restrictions, deliveryTimes)){
			LOGGER.info( "Skipping order because next delivery date is closed holiday." );
			return result.withUserRelatedError(ErrorCode.CLOSED_DAY, customerUser);
		}
		
		// ==================================
		//   Validate Timeslot reservation
		// ==================================
		
		//Allowing COS customers to use HOME zone capacity for the configured set of HOME zones
		AddressModel clonedDeliveryAddressModel = performCosResidentialMerge(deliveryAddressModel);
		// WARNING: getAllTimeslotsForDateRange-s select will ignore houre:minute in start/end dates!
		List<FDTimeslot> timeslots = FDDeliveryManager.getInstance().getAllTimeslotsForDateRange( deliveryTimes.getDayStart(), deliveryTimes.getDayEnd(), clonedDeliveryAddressModel );
				
		if ( timeslots == null || timeslots.size() == 0 ) {
			LOGGER.info( "No timeslots for this day: " + new SimpleDateFormat(FDStandingOrder.DATE_FORMAT).format( deliveryTimes.getDayStart() ) );
			return result.withUserRelatedError(ErrorCode.TIMESLOT, customerUser);
		}
		
		
		// making a reservation ...
		FDActionInfo reserveActionInfo = info;
		if(reserveActionInfo == null) {
			reserveActionInfo = new FDActionInfo( EnumTransactionSource.STANDING_ORDER, customer, INITIATOR_NAME, "Reserving timeslot for Standing Order", null );
		} else {
			reserveActionInfo.setNote("Reserving timeslot for Standing Order");
		}
		FDReservation reservation = null;
		FDTimeslot selectedTimeslot = null;
		
		//Geo-Restrictions
		List<GeographyRestriction> geographicRestrictions = new ArrayList<GeographyRestriction>();
		geographicRestrictions = FDDeliveryManager.getInstance().getGeographicDlvRestrictions(deliveryAddressModel);
		
		for ( FDTimeslot timeslot : timeslots ) {
			
			if ( !deliveryTimes.checkTimeslot( timeslot ) ) {
				// this timeslot falls outside the required time interval, or is past the cutoff time, skip it
				LOGGER.info( "Skipping timeslot: " + timeslot.toString() );
				continue;
			}
			//check if timeslot is Geo-Restricted.
			if(GeographyRestriction.isTimeSlotGeoRestricted(geographicRestrictions,timeslot, new ArrayList<String>(),new DateRange(deliveryTimes.getDayStart(),deliveryTimes.getDayEnd()),new ArrayList<String>())){
				// this timeslot is geo-restricted, skip it
				LOGGER.info( "Skipping Geo-Restricted timeslot: " + timeslot.toString() );
				continue;
			}
			if ( event == null ) {
				event = new TimeslotEventModel(EnumTransactionSource.STANDING_ORDER.getCode(), false, 0.00, false, false);
			}			
			try { 
				LOGGER.info( "Trying to make reservation for timeslot: " + timeslot.toString() );
				reservation = FDCustomerManager.makeReservation( customer, timeslot, EnumReservationType.STANDARD_RESERVATION, deliveryAddressId, reserveActionInfo, false, event, false );
				selectedTimeslot = timeslot;
				LOGGER.info( "Timeslot reserved successfully: " + timeslot.toString() );
			} catch ( ReservationUnavailableException e ) {
				if(forceCapacity){
					try {
						reservation = FDCustomerManager.makeReservation( customer, timeslot, EnumReservationType.STANDARD_RESERVATION, deliveryAddressId, reserveActionInfo, false, event, forceCapacity);
					} catch (ReservationException e1) {						
						e1.printStackTrace();
					}
					selectedTimeslot = timeslot;
					LOGGER.info( "Timeslot reserved successfully: " + timeslot.toString() );
				} else {
					// no more capacity in this timeslot
					LOGGER.info( "No more capacity in timeslot: " + timeslot.toString(), e );
				}
			} catch (ReservationException e) {
				if(forceCapacity){
					try {
						reservation = FDCustomerManager.makeReservation( customer, timeslot, EnumReservationType.STANDARD_RESERVATION, deliveryAddressId, reserveActionInfo, false, event, forceCapacity);
					} catch (ReservationException e1) {						
						e1.printStackTrace();
					}
					selectedTimeslot = timeslot;
					LOGGER.info( "Timeslot reserved successfully: " + timeslot.toString() );
				} else {
					// other error
					LOGGER.warn( "Reservation failed for timeslot: " + timeslot.toString(), e );
				}
			}
			if ( reservation != null ) 
				break;
		}
		
		if ( reservation == null || selectedTimeslot == null ) {
			LOGGER.warn( "Failed to make timeslot reservation." );
			return result.withUserRelatedError(ErrorCode.TIMESLOT, customerUser);
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
			return result.withUserRelatedError(ErrorCode.ADDRESS, customerUser);
		}
		if ( zoneInfo == null ) {
			LOGGER.info( "Missing zone info." );
			return result.withUserRelatedError(ErrorCode.ADDRESS, customerUser);
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
				return result.withUserRelatedError(ErrorCode.ALCOHOL, customerUser);
			}
		}

		// ============================
		//    Check SKU availability
		//      (SKU availability)
		// ============================
		List<String> unavailableItems = new ArrayList<String>();
		vr = new ProcessActionResult();
		doAvailabilityCheck(customer, cart, vr, unavailableItems);
		if (vr.hasInvalidItems()) {
			hasInvalidItems = true;
		}
		result.setUnavailableItems(unavailableItems);
		LOGGER.info( "SKU availability check passed." );

		// ==================================
		//    Check inventory availability
		//            (ATP Check)
		// ==================================
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
		double hardLimit = FDStoreProperties.getStandingOrderHardLimit();
		double softLimit = FDStoreProperties.getStandingOrderSoftLimit();
		if ( cartPrice < hardLimit ) {
			//Display soft limit info for user. He doesn't know about hard limit. 
			String msg = "The order subtotal ($"+cartPrice+") was below our $"+softLimit+" minimum.";
			LOGGER.info( msg );
			return result.setError(ErrorCode.MINORDER, msg, ErrorCode.MINORDER.getErrorDetail(customerUser));
		} else if(cartPrice >= hardLimit && cartPrice < softLimit) {
			result.setInternalMessage("The order subtotal ($"+cartPrice+") was between hard ($"+hardLimit+") and soft limit ($"+softLimit+").");
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
			FDActionInfo orderActionInfo = info; 
			if(orderActionInfo == null){
				orderActionInfo = new FDActionInfo( EnumTransactionSource.STANDING_ORDER, customer, INITIATOR_NAME, "Placing order for Standing Order", null );
			}else{
				orderActionInfo.setNote("Placing order for Standing Order");
			}
			CustomerRatingI cra = new CustomerRatingAdaptor( customerUser.getFDCustomer().getProfile(), customerUser.isCorporateUser(), customerUser.getAdjustedValidOrderCount() );
			String orderId = FDCustomerManager.placeOrder( orderActionInfo, cart, null, false, cra, null );

			try {
				FDStandingOrdersManager.getInstance().assignStandingOrderToSale(orderId, so);
			} catch (FDResourceException e) {
				LOGGER.error("Failed to assign standing order to sale, corresponding order ID="+orderId, e);
				LOGGER.warn( e.getFDStackTrace() );
			}

			if (altDate != null){
				try {
					FDStandingOrdersManager.getInstance().markSaleAltDeliveryDateMovement(orderId);
				} catch (FDResourceException e) {
					LOGGER.error("Failed to mark sale as moved because of holiday, corresponding order ID="+orderId, e);
					LOGGER.warn( e.getFDStackTrace() );
				}
			}

			LOGGER.info( "Order placed successfully. OrderId = " + orderId );
			
			sendSuccessMail( so, customerInfo, orderId, hasInvalidItems, mailerHome );
			
			// step delivery date 
			so.skipDeliveryDate();
			
			//check possible duplicate order instances in delivery window
			FDStandingOrdersManager.getInstance().checkForDuplicateSOInstances(customer);
			
			result.setInvalidItems(hasInvalidItems);
			result.withStatus(Status.SUCCESS);
			
		} catch ( DeliveryPassException e ) {
			LOGGER.info( "DeliveryPassException while placing order.", e );
			result.withUserRelatedError(ErrorCode.PAYMENT, customerUser);
		} catch ( ErpFraudException e ) {
			LOGGER.info( "ErpFraudException while placing order.", e );
			result.withUserRelatedError(ErrorCode.PAYMENT, customerUser);
		} catch ( ErpAuthorizationException e ) {
			LOGGER.info( "ErpAuthorizationException while placing order.", e );
			result.withUserRelatedError(ErrorCode.PAYMENT, customerUser);
		} catch ( FDPaymentInadequateException e ) {
			LOGGER.info( "FDPaymentInadequateException while placing order.", e );
			result.withUserRelatedError(ErrorCode.PAYMENT, customerUser);
		} catch ( ErpTransactionException e ) {
			LOGGER.info( "ErpTransactionException while placing order.", e );
			result.withUserRelatedError(ErrorCode.PAYMENT, customerUser);
		} catch ( ReservationException e ) {
			LOGGER.info( "ReservationException while placing order.", e );
			result.withUserRelatedError(ErrorCode.TIMESLOT, customerUser);
		} catch ( ErpAddressVerificationException e ) {
			LOGGER.info( "ErpAddressVerificationException (Payment Address) while placing order.", e );
			result.withUserRelatedError(ErrorCode.PAYMENT_ADDRESS, customerUser);
		}
		return result;
	}
	
	private static void sendSuccessMail ( FDStandingOrder so, FDCustomerInfo customerInfo, String orderId, boolean hasInvalidItems, MailerGatewayHome mailerHome ) {
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
	
	private static void sendNotificationMail ( FDStandingOrder so, FDCustomerInfo customerInfo, String orderId, MailerGatewayHome mailerHome ) {
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

	
	public static boolean isNextDeliveryDateHolidayRestricted(DlvRestrictionsList restrictions, DeliveryInterval deliveryTimes){
		
		DateRange validRange = new DateRange(deliveryTimes.getDayStart(), deliveryTimes.getDayEnd());
		if(restrictions.getRestrictions(EnumDlvRestrictionReason.CLOSED, validRange) != null &&
				restrictions.getRestrictions(EnumDlvRestrictionReason.CLOSED, validRange).size() > 0){
			return true;
		}
		
		return false;
	}
	
	protected static boolean isValidCustomerList(List<FDCustomerListItem> lineItems) throws FDResourceException {
		return OrderLineUtil.isValidCustomerList( lineItems );
	}
	
	protected static FDCartModel checkAvailability(FDIdentity identity, FDCartModel cart, long timeout) throws FDResourceException {
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
	public static void updateDeliverySurcharges(FDCartModel cart, FDRuleContextI ctx) {
		cart.updateSurcharges(ctx);
	}


	public static FDCartModel buildCart(FDCustomerList soList, ErpPaymentMethodI paymentMethod, AddressModel deliveryAddressModel, List<FDTimeslot> timeslots, DlvZoneInfoModel zoneInfo, FDReservation reservation, ProcessActionResult vr) throws FDResourceException {
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
			cart.refreshAll(true);			
		} catch ( FDInvalidConfigurationException e ) {
			LOGGER.info( "Shopping list contains some items with invalid configuration." );
			vr.increment();
			// This is not an error
			// return new Result( ErrorCode.CART, "Shopping list contains some items with invalid configuration.", customerInfo );
		}
		
		
		return cart;
	}

	/**
	 * Perform availability check on cart items
	 * ===============================
	 * 
	 * Removes SKUs which are unavailable/discontinued
	 * 
	 * 
	 * @param customer
	 * @param cart
	 * @param unavailableItems List where unavailable item info should have been put.
	 * @return <code>true</code> if there was at least one unavailable item.
	 * @throws FDResourceException
	 */
	public static boolean doAvailabilityCheck(FDIdentity customer, FDCartModel cart, ProcessActionResult vr, List<String> unavailableItems)
			throws FDResourceException {
		List<FDCartLineI> list = cart.getOrderLines();
		for (int i = 0; i < list.size(); i++) {
			FDCartLineI cartLine = list.get(i);
			int randomId = cartLine.getRandomId();
			FDProductInfo prodInfo = cartLine.lookupFDProductInfo();
			if (!prodInfo.isAvailable()) {
				unavailableItems.add(prodInfo.getSkuCode() + " " + cartLine.getProductName());
				vr.increment();
				cart.removeOrderLineById(randomId);
				LOGGER.debug("[AVAILABILITY CHECK] Item " + randomId + " / '" + cartLine.getProductName() + "' - SKU is unavailable/discontinued and therefore item was removed.");
			}
		}

		return vr.hasInvalidItems();
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
	public static boolean doATPCheck(FDIdentity customer, FDCartModel cart, ProcessActionResult vr)
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
	
	private static void sendNotification( FDStandingOrder so, MailerGatewayHome mailerHome ) throws FDResourceException {		
		try {
			List<FDOrderInfoI> orders = so.getAllOrders();
			for ( FDOrderInfoI order : orders ) {
				Date notificationDate = subtract2WorkDays( order.getRequestedDate() );
				if ( isWithin24Hours( notificationDate )) {
					if (isCancelledOrder(order)) {
						LOGGER.info( "Not sending 2days notification email as order instance is cancelled: so["+so.getId()+"], order["+order.getErpSalesId()+"]" );
					} else {
						LOGGER.info( "Sending 2days notification email: so["+so.getId()+"], order["+order.getErpSalesId()+"]" );
						sendNotificationMail( so, so.getUserInfo(), order.getErpSalesId(), mailerHome );
					}
				}
			}			
		} catch (FDAuthenticationException e) {
			LOGGER.error("authentication error while sending notification mail", e);
		}		
	}
	private static boolean isCancelledOrder(FDOrderInfoI order) {
		if (EnumSaleStatus.CANCELED.equals(order.getOrderStatus()) ||
			EnumSaleStatus.MODIFIED_CANCELED.equals(order.getOrderStatus())
		    )
			return true;
		return false;
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
	
	private static AddressModel performCosResidentialMerge(AddressModel address)	throws FDResourceException {
		AddressModel timeslotAddress = address;
		if(address!=null){
			if(EnumServiceType.CORPORATE.equals(address.getServiceType())){
				try{
					DlvServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().checkAddress(address);
			 		EnumDeliveryStatus status = serviceResult.getServiceStatus(address.getServiceType());
			 		if(EnumDeliveryStatus.COS_ENABLED.equals(status)){	
			 			//Clone the address model object
			 			timeslotAddress = cloneAddress(address);
			 			timeslotAddress.setServiceType(EnumServiceType.HOME);
			 			LOGGER.info("Address "+address+" is COS Enabled. ServiceType set to HOME.");
			 		}
			 		
				}catch (FDInvalidAddressException iae) {
					LOGGER
					.warn("GEOCODE FAILED FOR ADDRESS setRegularDeliveryAddress  FDInvalidAddressException :"
							+ address + "EXCEPTION :" + iae);
				}
			}
		}
		return timeslotAddress;
	}
	
	private static AddressModel cloneAddress(AddressModel address) {
		ErpAddressModel model = new ErpAddressModel(address);		
		return model;
	}

	
	
	/**
	 * Create a new standing order template
	 * 
	 * @param session Web session [mandatory]
	 * @param cart Customer cart [mandatory]
	 * @param so Standing Order object [mandatory]
	 * @param saleId Sale ID [mandatory]
	 * 
	 * @throws FDResourceException
	 */
	public static void createStandingOrder(HttpSession session, FDCartModel cart, FDStandingOrder so, String saleId) throws FDResourceException {
		FDActionInfo info = AccountActivityUtil.getActionInfo(session);

		info.setNote("Standing Order Created w/First Order Placed");
		String soPk = FDStandingOrdersManager.getInstance().manageStandingOrder(info, cart, so, saleId);
		so.setId( soPk );

		// establish link between SO and Sale
		FDStandingOrdersManager.getInstance().assignStandingOrderToSale(saleId, so);
	}



	/**
	 * Update an existing standing order template with the corresponding shopping list
	 * according to the input
	 * 
	 * @param session Web session [mandatory]
	 * @param mode Cart mode. Must be one of MODIFY_SO_* [mandatory]
	 * @param cart Cart content object [mandatory]
	 * @param so Standing order object [mandatory]
	 * @param saleId SALE ID (optional, MODIFY_SO_TMPL does not require sale object reference)
	 * 
	 * @throws FDResourceException
	 */
	public static void updateStandingOrder(HttpSession session, EnumCheckoutMode mode, FDCartModel cart, FDStandingOrder so, String saleId) throws FDResourceException {
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDActionInfo info = AccountActivityUtil.getActionInfo(session);

		if (mode == null || EnumCheckoutMode.NORMAL == mode || !mode.isModifyStandingOrder()) {
			// skip
			return;
		} else if (mode.isModifyStandingOrder()) {
			if (mode==EnumCheckoutMode.MODIFY_SO_CSOI) {
				info.setNote("Standing Order Template Modified w/ New Order Checkout");
			} else if (mode==EnumCheckoutMode.MODIFY_SO_MSOI) {
				info.setNote("Standing Order Template Modified w/ Modify Order Checkout");
			} else {
				info.setNote("Standing Order Template Modified (no order changed or created)");
			}

			// pick the default payment method
			so.setPaymentMethodId( FDCustomerManager.getDefaultPaymentMethodPK(user.getIdentity()) );

			// update standing order
			FDStandingOrdersManager.getInstance().manageStandingOrder(info, cart, so, saleId);
			
			// connect order to SO template
			if (saleId != null && mode != EnumCheckoutMode.MODIFY_SO_TMPL) {
				// establish link between SO and Sale
				FDStandingOrdersManager.getInstance().assignStandingOrderToSale(saleId, so);
			}


			// perform cache cleanup
			QuickCartCache.invalidateOnChange(session, QuickCart.PRODUCT_TYPE_SO, so.getCustomerListId(), null);
		}
	}




	/**
	 * Close the standing order part of the checkout process
	 * 
	 * 
	 * @param session
	 * @return Landing page URL
	 * @throws FDResourceException 
	 * @throws FDAuthenticationException 
	 * @throws JspException 
	 */
	public static String endStandingOrderCheckoutPhase(final HttpSession session) throws FDAuthenticationException, FDResourceException {
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		EnumCheckoutMode origMode = user.getCheckoutMode();
		FDStandingOrder so = user.getCurrentStandingOrder();
		
		user.setCurrentStandingOrder(null);
		if ( !EnumCheckoutMode.NORMAL.equals( origMode ) ) {
			
			// RESET
			user.setCheckoutMode(EnumCheckoutMode.NORMAL);

			if ( origMode.isCartSaved() ) {
				// RESTORE ORIGINAL CART
				ShoppingCartUtil.restoreCart(session);
			}
			
			//as in ModifyOrderControllerTag.cancelModifyOrder()
			if (origMode == EnumCheckoutMode.MODIFY_SO_MSOI ) {
				FDGiftCardInfoList gcList = user.getGiftCardList();
	    		//Clear any hold amounts.
	    		gcList.clearAllHoldAmount();
			}


			return origMode.isModifyStandingOrder() ?
					FDURLUtil.getStandingOrderLandingPage(so, null)
					:
					FDURLUtil.getStandingOrderMainPage();
		} else {
			LOGGER.error("endStandingOrderCheckoutPhase() was invoked although no standing order was being modified!");
		}

		return null;
	}
}
