package com.freshdirect.webapp.util;


import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.FDRestrictedAvailabilityInfo;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdlogistics.model.FDTimeslotList;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDCompositeAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDMuniAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
import com.freshdirect.fdstore.coremetrics.mobileanalytics.CJVFContextHolder;
import com.freshdirect.fdstore.coremetrics.mobileanalytics.CreateCMRequest;
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
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.QuickCart;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.rules.FDRuleContextI;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.fdstore.standingorders.DeliveryInterval;
import com.freshdirect.fdstore.standingorders.EnumStandingOrderAlternateDeliveryType;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.ProcessActionResult;
import com.freshdirect.fdstore.standingorders.SOResult;
import com.freshdirect.fdstore.standingorders.UnavailabilityReason;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumCompanyCode;
import com.freshdirect.logistics.delivery.model.EnumOrderAction;
import com.freshdirect.logistics.delivery.model.EnumOrderType;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.TimeslotContext;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductReference;
import com.freshdirect.webapp.ajax.expresscheckout.availability.service.AvailabilityService;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
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
	 * @throws FDAuthenticationException 
	 * 
	 */
	public static SOResult.Result process( FDStandingOrder so, FDStandingOrderAltDeliveryDate altDateInfo, TimeslotEvent event, FDActionInfo info
					, MailerGatewayHome mailerHome, boolean forceCapacity, boolean createIfSoiExistsForWeek, boolean isSendReminderNotificationEmail) throws FDResourceException {
		
		LOGGER.info( "Processing Standing Order : " + so );
		boolean errorOccured = false;
		
		// Check for null object here, so we are safe later
		if ( so == null ) {
			LOGGER.warn( "StandingOrderUtil.process() received a null SO object, ignoring it." );
			return SOResult.createNull();
		}
		
		// skip if : deleted or has some error - redundant check...
		if ( so.isDeleted() ) {
			
			LOGGER.warn( "StandingOrderUtil.process() received a deleted SO, ignoring it." );
			LOGGER.info( "Skipping deleted order." );
			return SOResult.createSkipped( so, "Skipping because SO is deleted" );
		}
		
		// check if the standingorder is Active or not..
		if("N".equalsIgnoreCase(so.getActivate())){	
			LOGGER.warn( "Standing order template is not activated "+so.getId() );
			LOGGER.info( "Standing order template is not activated, Skipping order  "+so.getId() );
			FDStandingOrdersManager.getInstance().updateDeActivatedSOError(so.getId() );
			return SOResult.createSkipped( so, "Skipping because SO is Not Activated" );
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		// checking if SO is in erroneous state
		Date now = new Date();
		if ( so.getLastError() != null ) {
			if (so.getDeleteDate()!=null && dateFormat.format(now).equals(dateFormat.format(so.getDeleteDate()))) {
				try {
					FDActionInfo soinfo = new FDActionInfo( EnumTransactionSource.STANDING_ORDER, so.getCustomerIdentity(), 
							INITIATOR_NAME, " Delete date removed because of So has an error", null, null);
					
					FDStandingOrdersManager.getInstance().deleteActivatedSO(soinfo, so, null);
					LOGGER.info("updated the delete date as null because of So template has the errors.");
				} catch (Exception e) {
					LOGGER.error(" Got the exception while deleting the delete date of So template:"+so.getId(), e);
				}
			}
			
			if ( now.after( so.getNextDeliveryDate() ) ) {
				// clearing the error since the delivery date has been passed
				// and the customer did not touch the erroneous SO so far
				
				LOGGER.info( "Clearing SO error state." );
				so.clearLastError();
				
				try {
					if ( info == null ) {
						info = new FDActionInfo( EnumTransactionSource.STANDING_ORDER, so.getCustomerIdentity(), 
								INITIATOR_NAME, "Resetting Standing Order Error Status", null, null);
					} else {
						info.setNote( "Resetting Standing Order Error Status" );
					}
					
					LOGGER.info( "Saving SO to DB." );
					FDStandingOrdersManager.getInstance().save( info, so );
					
				} catch (FDResourceException re) {
					LOGGER.error( "Saving standing order failed! (FDResourceException)", re );
					return SOResult.createTechnicalError( so, "Cannot reset error state of SO" );
				}
			} else {
				// skipping because it is erroneous
				LOGGER.info( "SO has a permanent error." );
				return SOResult.createUserError(so, so.getCustomerIdentity(), so.getUserInfo(), ErrorCode.PERSISTING_ERROR);
			}
		}else {
			// delete date which was choose by user.
			if(so.getDeleteDate()!=null && dateFormat.format(now).equals(dateFormat.format(so.getDeleteDate()))) {
				LOGGER.info("Starting to delete standing orders template based on delete date choosen by user.");
				try {
					FDActionInfo soinfo = new FDActionInfo( EnumTransactionSource.STANDING_ORDER, so.getCustomerIdentity(), 
							INITIATOR_NAME, "so template deleted as per the delete date: "+so.getDeleteDate()+", choosen by user", null, null);
					so.setDeleteDate(null);
					deleteActivateSo(so, soinfo);
					return SOResult.createNull();
				} catch (Exception e) {
					LOGGER.error(" Got the exception while deleting the So template:"+so.getId(), e);
				}
				LOGGER.info("Delete the So template based on template criteria choosen by user.");
			}
		}
	
		while ( now.after( so.getNextDeliveryDate() ) ) {
			LOGGER.info( "Skipping delivery date, because it is in the past." );
			so.skipDeliveryDate();
		}
		
		// ============================
		//    Validate delivery date
		// ============================
		
		
		DeliveryInterval deliveryTimes = null;	
		altDateInfo = null != so.getAltDeliveryInfo() ? so.getAltDeliveryInfo():altDateInfo; //Priority should be given standing order level alternate delivery info.
		Date altDate = null;
		Date startTime = so.getStartTime();
		Date endTime = so.getEndTime();
		EnumStandingOrderAlternateDeliveryType altDeliveryType = null;
		if(null != altDateInfo){
			altDate = altDateInfo.getAltDate();
			altDeliveryType = EnumStandingOrderAlternateDeliveryType.getEnum(altDateInfo.getActionType());
			startTime = null != altDateInfo.getAltStartTime() ? altDateInfo.getAltStartTime() : startTime;
			endTime = null != altDateInfo.getAltEndTime() ? altDateInfo.getAltEndTime() : endTime;			
		}
		try {
			if ( null != altDate ) {
				if(null == altDeliveryType || EnumStandingOrderAlternateDeliveryType.ALTERNATE_DELIVERY.equals(altDeliveryType)){
					deliveryTimes = new DeliveryInterval( altDate, startTime, endTime );
				}else if(EnumStandingOrderAlternateDeliveryType.SKIP_DELIVERY.equals(altDeliveryType)){
					//skip the current delivery and move to next delivery date.
					so.skipDeliveryDate();
					return SOResult.createForcedSkipped( so, new FDIdentity(so.getCustomerId()), new FDCustomerInfo(so.getCustomerEmail()), "Skipping because customer has requested to cancel the next delivery." );				
				}
			} else if(null != altDateInfo && EnumStandingOrderAlternateDeliveryType.SKIP_DELIVERY.equals(altDeliveryType) ){
				//skip the current delivery and move to next delivery date.
				so.skipDeliveryDate();
				return SOResult.createForcedSkipped( so, new FDIdentity(so.getCustomerId()), new FDCustomerInfo(so.getCustomerEmail()), "Skipping because customer has requested to cancel the next delivery." );
			} else{
				deliveryTimes = new DeliveryInterval( so );
			}			
		} catch ( IllegalArgumentException ex ) {
			LOGGER.warn( "No valid dates." );
			return SOResult.createUserError( so, new FDIdentity(so.getCustomerId()), new FDCustomerInfo(so.getCustomerEmail()), ErrorCode.TIMESLOT );
		}

		// check if we are within the delivery window
		if ( null == deliveryTimes || !deliveryTimes.isWithinDeliveryWindow() ) {
			LOGGER.info( "Skipping order because delivery date falls outside of the current delivery window." );
			return SOResult.createSkipped( so, new FDIdentity(so.getCustomerId()), new FDCustomerInfo(so.getCustomerEmail()), "Skipping because delivery date falls outside of the current delivery window." ); 
		}		
		
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
					customerInfo.getUserInfo(customerUser);
				} catch ( FDAuthenticationException e ) {
					LOGGER.warn( "Failed to retreive customer information.", e );
				}
				
				if ( customer == null || customerInfo == null || customerUser == null ) {
					LOGGER.warn( "No valid customer." );
					return SOResult.createTechnicalError( so, "No valid customer found" );
				}
				// Note: customer data will be added to the result if the order creation succeeds 

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
						return SOResult.createSkipped( so, customer, customerInfo, "Skipping because SOI exists for given week" ); 
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
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.NO_ADDRESS );
		}
			
		DeliveryAddressValidator addressValidator = new DeliveryAddressValidator( deliveryAddressModel );
		ActionResult addressValidatorResult = new ActionResult();
		
		try { 
			addressValidator.validateAddress( addressValidatorResult );
		}catch (FDResourceException e) {
			LOGGER.warn( "Address validation failed with FDResourceException ", e );
			return SOResult.createTechnicalError( so, "Address validation failed with FDResourceException." );
		}
		
		if ( addressValidatorResult.isFailure() ) {
			LOGGER.warn( "Address validation failed : " + addressValidatorResult.getFirstError().getDescription() );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.ADDRESS );
		}
		
		// continue with the scrubbed address
		deliveryAddressModel = addressValidator.getScrubbedAddress();
		
		if ( deliveryAddressModel == null ) {
			LOGGER.warn( "No valid delivery address." );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.ADDRESS );
		}
		
		LOGGER.info("Delivery address is valid. Address ID: " + deliveryAddressModel.getId());
		
		
		
		// ============================
		//   Validate payment methods
		// ============================
		
		String paymentMethodID = so.getPaymentMethodId();
		
		if ( paymentMethodID == null || paymentMethodID.trim().equals( "" ) ) {
			LOGGER.warn( "No payment method id." );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.PAYMENT );
		}

		ErpPaymentMethodI paymentMethod = null;
		try { 
			paymentMethod = FDCustomerManager.getPaymentMethod( customer, paymentMethodID );
		} catch (FDResourceException e) {
			LOGGER.warn( "FDResourceException while getting payment method", e );
			return SOResult.createTechnicalError( so, "FDResourceException while getting payment method." );
		}
		
		if ( paymentMethod == null ) {
			LOGGER.warn( "No valid payment method id." );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.PAYMENT );
		}

		ActionResult paymentValidatorResult = new ActionResult();
		try {
			PaymentMethodUtil.validatePaymentMethod(null,paymentMethod, paymentValidatorResult, customerUser, false, false, false,false,null,EnumAccountActivityType.UNKNOWN );
		} catch (FDResourceException e) {
			LOGGER.warn( "FDResourceException while validating payment method", e );
			return SOResult.createTechnicalError( so, "FDResourceException while validating payment method." );
		}

		if ( paymentValidatorResult.isFailure() ) {
			LOGGER.warn( "Payment method not valid: " + paymentValidatorResult.getFirstError().getDescription() );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.PAYMENT );
		}
		
		LOGGER.info( "Payment method is valid, payment ID: "+paymentMethod.getPK().getId()+". Account num: "+ paymentMethod.getMaskedAccountNumber()+". Type: "+ paymentMethod.getCardType());


		
	
		// =================================================
		//   Validate SO nextDelivery date is a holiday or closed
		// =================================================
		
		DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
		if(isNextDeliveryDateHolidayRestricted(restrictions, deliveryTimes)){
			LOGGER.info( "Skipping order because next delivery date is closed holiday." );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.CLOSED_DAY );
		}
		
		// ==================================
		//   Validate Timeslot reservation
		// ==================================
		
		// WARNING: getAllTimeslotsForDateRange-s select will ignore houre:minute in start/end dates!
		
		ErpAddressModel contactAddress = (ErpAddressModel)deliveryAddressModel;
		
		if ( event == null ) {
			event = new TimeslotEvent(EnumTransactionSource.STANDING_ORDER.getCode(), false, 0.00, false, 
					false,(customerUser!=null)?customerUser.getPrimaryKey():null, EnumCompanyCode.fd.name());
		}	
		//contactAddress.setFrom(deliveryAddressModel, customerUser.getFirstName(), customerUser.getLastName(), customer.getErpCustomerPK());
		List<DateRange> ranges = new ArrayList<DateRange>();
		ranges.add(new DateRange(deliveryTimes.getDayStart(), deliveryTimes.getDayEnd()));
		FDTimeslotList timeslotList = FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone
				(ranges, event, TimeslotLogic.encodeCustomer(contactAddress, customerUser), 
						TimeslotLogic.getOrderContext(EnumOrderAction.CREATE, customer.getErpCustomerPK(), EnumOrderType.REGULAR), TimeslotContext.CHECKOUT_TIMESLOTS,customerUser.isNewSO3Enabled())
				.getTimeslotList().get(0);
		
		List<FDTimeslot> timeslots = timeslotList.getTimeslots();
				
		if ( timeslots == null || timeslots.size() == 0 ) {
			LOGGER.info( "No timeslots for this day: " + new SimpleDateFormat(FDStandingOrder.DATE_FORMAT).format( deliveryTimes.getDayStart() ) );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.TIMESLOT );
		}
		
		// making a reservation ...
		FDActionInfo reserveActionInfo = info;
		if(reserveActionInfo == null) {
			reserveActionInfo = new FDActionInfo( EnumTransactionSource.STANDING_ORDER, customer, INITIATOR_NAME, "Reserving timeslot for Standing Order", null,null);
		} else {
			reserveActionInfo.setNote("Reserving timeslot for Standing Order");
		}
		FDReservation reservation = null;
		FDTimeslot selectedTimeslot = null;
		ErpAddressModel deliveryAddress = null;
		TimeslotReservationInfo _windowInfo = null;
				
		FDTimeslot _tmpTimeslot = null;
		for ( FDTimeslot timeslot : timeslots ) {		
			if ( deliveryTimes.checkTimeslot( timeslot ) ) {
				// this time slot matches to SO template window, and is within the cutoff time
				LOGGER.info( "Found matched timeslot, Timeslot ID: " + timeslot.getId() );
				_tmpTimeslot = timeslot;
				
				_windowInfo = new TimeslotReservationInfo(timeslot, deliveryTimes,
														event, deliveryAddress,
														customer, deliveryAddressId,
														customerUser, reservation,
														selectedTimeslot, forceCapacity);				
				
				//check if time slot is Geo-Restricted.
				if(_windowInfo.isTimeSlotGeoRestricted()){
					// this time slot is geo-restricted, skip it
					LOGGER.info( "Skipping Geo-Restricted template match timeslot: " + timeslot.toString() );
				} else {
					reservation = _windowInfo.reserveTimeslot();
					selectedTimeslot = _windowInfo.getSelectedTimeslot();
				}
				break;
			}
		}
		
		//To allow time windows that do not exactly match the window specified in the standing order template. 
		//If an exact match time window is not available, but time windows exist that overlap the template's time window, 
		//then select the overlapping time window with the earliest start time 		
		if(_tmpTimeslot == null && FDStoreProperties.isStandingOrdersOverlapWindowsEnabled()) {
			
			List<FDTimeslot> altTimeslots = deliveryTimes.getAltTimeslots(timeslots);
			for ( FDTimeslot timeslot : altTimeslots ) {
				
				_windowInfo = new TimeslotReservationInfo(timeslot, deliveryTimes,
														event, deliveryAddress,
														customer, deliveryAddressId,
														customerUser, reservation,
														selectedTimeslot, forceCapacity);
				
				//check if time slot is Geo-Restricted.
				if(_windowInfo.isTimeSlotGeoRestricted()){
					// this time slot is geo-restricted, skip it
					LOGGER.info( "Skipping Geo-Restricted overlap timeslot: " + timeslot.toString() );
					continue;
				}
				reservation = _windowInfo.reserveTimeslot();
				selectedTimeslot = _windowInfo.getSelectedTimeslot();
				if ( reservation != null ) 
					break;
			}
		}
		
		if ( reservation == null || selectedTimeslot == null ) {
			LOGGER.warn( "Failed to make timeslot reservation." );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.TIMESLOT );
		}
		
		LOGGER.info( "Selected timeslot : " + selectedTimeslot.getDlvStartTime()+" to "+ selectedTimeslot.getDlvEndTime());
		LOGGER.info( "Reservation ID: " + reservation.getId()+" [DLV.RESERVATION], Timeslot ID: "+reservation.getTimeslotId()+" [DLV.TIMESLOT]");

		// ==========================
		//    Extra validations
		// ==========================
		
		FDDeliveryZoneInfo zoneInfo = null;
		try {
			FDTimeslot timeSlot = FDDeliveryManager.getInstance()
					.getTimeslotsById(selectedTimeslot.getId(),
							deliveryAddressModel.getBuildingId(), true);
			zoneInfo = timeSlot.getZoneInfo();

			if (zoneInfo == null || StringUtils.isEmpty(zoneInfo.getZoneId())
					|| FDStoreProperties.isRefreshZoneInfoEnabled()) {
				zoneInfo = FDDeliveryManager.getInstance()
						.getZoneInfo(deliveryAddressModel,
								selectedTimeslot.getStartDateTime(),
								customerUser.getHistoricOrderSize(),
								selectedTimeslot.getRegionSvcType(),
								so.getCustomerId());
			}

		} catch (FDInvalidAddressException e) {
			LOGGER.info( "Invalid zone info. - FDInvalidAddressException", e );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.ADDRESS );
		}
		if ( zoneInfo == null ) {
			LOGGER.info( "Missing zone info." );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.ADDRESS );
		}



		// ==========================
		//    Build Cart
		// ==========================
		
		ProcessActionResult vr = new ProcessActionResult();
		FDCartModel cart = buildCart(so.getCustomerList(), paymentMethod, deliveryAddressModel, timeslots, zoneInfo, reservation, so.getTipAmount(), vr, customerUser);		
		// boolean hasInvalidItems = vr.isFail();
		
		final List<FDCartLineI> originalCartItems = new ArrayList<FDCartLineI>(cart.getOrderLines());
		
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
				return SOResult.createUserError( so, customer, customerInfo, ErrorCode.ALCOHOL );
			}
		}

		// ============================
		//    Check SKU availability
		//      (SKU availability)
		// ============================
		List<String> unavailableItems = new ArrayList<String>();
		
		Set<ContentKey> excludedProductKeys = prepareExcludedProduct(cart);
		
		// vr = new ProcessActionResult();
		doAvailabilityCheck(customer, cart, vr, unavailableItems,excludedProductKeys);

		// Note: unavailableItems will be added to the result if the order creation succeeds 
		LOGGER.info( "SKU availability check passed." );

		// ==================================
		//    Check inventory availability
		//            (ATP Check)
		// ==================================
		// vr = new ProcessActionResult();
		doATPCheck(customer, cart, vr,excludedProductKeys);

		// Note: hasInvalidItems will be added to the result if the order creation succeeds 
		LOGGER.info( "ATP check passed." );


		// ==========================
		//    Verify order minimum
		// ==========================
		String internalMessage = null;
		double cartPrice = cart.getSubTotal();
		double hardLimit = ErpServicesProperties.getStandingOrderHardLimit();
		double softLimit = ErpServicesProperties.getStandingOrderSoftLimit();
		if ( cartPrice < hardLimit ) {
			//Display soft limit info for user. He doesn't know about hard limit. 
			String msg = "The order subtotal ($"+cartPrice+") was below our $"+softLimit+" minimum.";
			if( !vr.getUnavailableItems().isEmpty() && vr.getUnavailableItems().size()!= 0){
				msg="The order subtotal ($"+cartPrice+") was below our $"+softLimit+" minimum. Some of the items in your cart are unavailable temporarily.";
			}
			LOGGER.info( msg );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.MINORDER, msg );
		} else if(cartPrice >= hardLimit && cartPrice < softLimit) {
			internalMessage = "The order subtotal ($"+cartPrice+") was between hard ($"+hardLimit+") and soft limit ($"+softLimit+").";
			// Note: internalMessage will be added to the result if the order creation succeeds 
		}
		
		LOGGER.info( "Cart contents are valid." );


		// ==========================
		//    Update Tax and Bottle deposits
		//    Update Delivery Fees
		// ==========================
		// commented the below two lines as this logic is replaced by updateUserState call
		//cart.recalculateTaxAndBottleDeposit(deliveryAddressModel.getZipCode());
		//updateDeliverySurcharges(cart, new FDRulesContextImpl(customerUser));


		// ==========================
		//    Placing the order
		// ==========================
		
		try {
			final boolean hasInvalidItems = vr.isFail();
			final List<FDCartLineI> unavCartItems = new ArrayList<FDCartLineI>(originalCartItems);
			
			FDActionInfo orderActionInfo = info; 
			if (orderActionInfo == null){
				orderActionInfo = new FDActionInfo( EnumTransactionSource.STANDING_ORDER, customer, INITIATOR_NAME, "Placing order for Standing Order", null,customerUser.getPrimaryKey());
			} else{
				orderActionInfo.setNote("Placing order for Standing Order");
				orderActionInfo.setFdUserId(customerUser.getPrimaryKey());
			}
			CustomerRatingI cra = new CustomerRatingAdaptor( customerUser.getFDCustomer().getProfile(), customerUser.isCorporateUser(), customerUser.getAdjustedValidOrderCount() );

			// Get unavailable cart items (ie. not shipped with the recent order)
			unavCartItems.removeAll(cart.getOrderLines());
			cart.setTransactionSource(EnumTransactionSource.STANDING_ORDER);
			customerUser.updateUserState();
			

			// ==========================
			//    Verify variable order minimum
			// ==========================
			double variableMinimum = cart.getDeliveryReservation().getMinOrderAmt();
			if(variableMinimum > cartPrice){
				String msg = "The order subtotal ($"+cartPrice+") was below the "+TimeslotLogic.formatMinAmount(variableMinimum)+" minimum for a premium delivery window.";
				LOGGER.info( msg );
				return SOResult.createUserError( so, customer, customerInfo, ErrorCode.TIMESLOT_MINORDER,  msg );
			}

			AvalaraContext avalaraContext = null;
			
			if(FDStoreProperties.getAvalaraTaxEnabled()){
				avalaraContext = new AvalaraContext(cart);
				avalaraContext.setCommit(false);
				avalaraContext.setReturnTaxValue(cart.getAvalaraTaxValue(avalaraContext));
				if(avalaraContext.isAvalaraTaxed()){
					orderActionInfo.setTaxationType(EnumNotificationType.AVALARA);
				}
			}
			
			
			String orderId = FDCustomerManager.placeOrder( orderActionInfo, cart, customerUser.getAllAppliedPromos(), false, cra, null ,false);
			
			long cmrequestStart = System.currentTimeMillis();
			try {
				
				CJVFContextHolder cjvfContextHolder = new CJVFContextHolder(customerUser.getPrimaryKey(), 7);
				if (FDStandingOrdersManager.getInstance().getCoremetricsUserinfo(customerUser)) {
					cjvfContextHolder.thisIsRepeatedStandingOrder();
				} else {
					FDStandingOrdersManager.getInstance().insertIntoCoremetricsUserinfo(customerUser, 1);
					cjvfContextHolder.thisIsFirstStandingOrder();
				}
				CreateCMRequest cCMR = new CreateCMRequest(customerUser.getPrimaryKey(), cjvfContextHolder);
				FDOrderI order = FDCustomerManager.getOrder(orderId);
				List httpResponseCodes =Arrays.asList(cCMR.sendShop9Tags(cart, order, customerUser));
				int httpResponseCode = cCMR.sendOrderTag(order, customerUser);
				
				if (httpResponseCodes.contains(CreateCMRequest.GENERAL_ERROR) || httpResponseCode == CreateCMRequest.GENERAL_ERROR) {
					errorOccured = true;
				} else {
					errorOccured = false;
				}
			} catch (FDResourceException e) {
				LOGGER.error("Failed to send coremetrics information corresponding order ID="+orderId, e);
			}
			LOGGER.info("Time taken to send CM Request for order ID="+orderId+" (in sec) " + (System.currentTimeMillis() - cmrequestStart)/1000 );
			
			
			try {
				FDStandingOrdersManager.getInstance().assignStandingOrderToSale(orderId, so);
			} catch (FDResourceException e) {
				LOGGER.error("Failed to assign standing order to sale, corresponding order ID="+orderId, e);
			}

			if (altDate != null){
				try {
					FDStandingOrdersManager.getInstance().markSaleAltDeliveryDateMovement(orderId);
				} catch (FDResourceException e) {
					LOGGER.error("Failed to mark sale as moved because of holiday, corresponding order ID="+orderId, e);
				}
			}

			LOGGER.info( "Order placed successfully. OrderId = " + orderId );
			
			sendSuccessMail( so, customerInfo, orderId, unavCartItems, mailerHome );
			
			// step delivery date 
			so.skipDeliveryDate();
			
			//check possible duplicate order instances in delivery window
			FDStandingOrdersManager.getInstance().checkForDuplicateSOInstances(customer);
			
			return SOResult.createSuccess( so, customer, customerInfo, hasInvalidItems, unavailableItems, orderId, internalMessage, errorOccured, vr.getUnavItemsMap(),cart.getDeliveryReservation().getStartTime() );
			
		} catch ( DeliveryPassException e ) {
			LOGGER.info( "DeliveryPassException while placing order.", e );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.PAYMENT );
		} catch ( ErpFraudException e ) {
			LOGGER.info( "ErpFraudException while placing order.", e );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.PAYMENT );
		} catch ( ErpAuthorizationException e ) {
			LOGGER.info( "ErpAuthorizationException while placing order.", e );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.PAYMENT );
		} catch ( FDPaymentInadequateException e ) {
			LOGGER.info( "FDPaymentInadequateException while placing order.", e );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.PAYMENT );
		} catch ( ErpTransactionException e ) {
			LOGGER.info( "ErpTransactionException while placing order.", e );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.PAYMENT );
		} catch ( ReservationException e ) {
			LOGGER.info( "ReservationException while placing order.", e );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.TIMESLOT );
		} catch ( ErpAddressVerificationException e ) {
			LOGGER.info( "ErpAddressVerificationException (Payment Address) while placing order.", e );
			return SOResult.createUserError( so, customer, customerInfo, ErrorCode.PAYMENT_ADDRESS );
		}
	}

	private static void deleteActivateSo(FDStandingOrder so, FDActionInfo soinfo)
			throws FDResourceException {
		FDStandingOrdersManager.getInstance().deleteActivatedSO(soinfo, so, null);
		FDStandingOrdersManager.getInstance().delete(soinfo, so);
		FDStandingOrdersManager.getInstance().deletesoTemplate(so.getId());
	}
	
	private static void sendSuccessMail ( FDStandingOrder so, FDCustomerInfo customerInfo, String orderId, List<FDCartLineI> unavCartItems, MailerGatewayHome mailerHome ) {
		try {
			FDOrderI order = FDCustomerManager.getOrder( orderId );
			XMLEmailI mail = FDEmailFactory.getInstance().createConfirmStandingOrderEmail( customerInfo, order, so, unavCartItems );
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MailerGatewaySB)) {
				FDECommerceService.getInstance().enqueueEmail(mail);
			} else {		
				MailerGatewaySB mailer = mailerHome.create();
				mailer.enqueueEmail( mail );
			}
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
			XMLEmailI mail = FDEmailFactory.getInstance().createConfirmDeliveryStandingOrderEmail( customerInfo, order, so, null );
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MailerGatewaySB)) {
				FDECommerceService.getInstance().enqueueEmail(mail);
			} else {		
				MailerGatewaySB mailer = mailerHome.create();
				mailer.enqueueEmail( mail );
			}
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
		return FDCustomerManager.checkAvailability( identity, cart, timeout,AvailabilityService.ATP_CHECKOUT );
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


	public static FDCartModel buildCart(FDCustomerList soList, ErpPaymentMethodI paymentMethod, AddressModel deliveryAddressModel, List<FDTimeslot> timeslots, FDDeliveryZoneInfo zoneInfo, FDReservation reservation, double tipAmount, ProcessActionResult vr, FDUserI user) throws FDResourceException {
		FDCartModel cart = new FDTransientCartModel();
		
		if ( ! isValidCustomerList( soList.getLineItems() ) ) {
			LOGGER.info( "Shopping list contains some unavailable/invalid items." );
			vr.markGeneralIssue();

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
        cart.setTip(tipAmount);
        user.setAddress(erpDeliveryAddress);
        user.setSelectedServiceType(erpDeliveryAddress.getServiceType() );
        user.setZPServiceType(erpDeliveryAddress.getServiceType());
        user.setShoppingCart( cart );
        user.resetUserContext();
        UserContext userContext = user.getUserContext();
        if(null ==userContext){
        	userContext = ContentFactory.getInstance().getCurrentUserContext();
        }
        cart.setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(userContext));
        cart.setEStoreId(userContext.getStoreContext().getEStoreId());
        // fill the cart with items
		List<FDProductSelectionI> productSelectionList = OrderLineUtil.getValidProductSelectionsFromCCLItems( soList.getLineItems() );
		
		try {
			for ( FDProductSelectionI ps : productSelectionList ) {
				FDCartLineI cartLine = new FDCartLineModel( ps );
				if ( !cartLine.isInvalidConfig() ) {
					cart.addOrderLine( cartLine );
				} else {
					vr.addUnavailableItem(cartLine, UnavailabilityReason.INVALID_CONFIG, null, cartLine.getQuantity(),null);
				}
			}
			cart.setUserContextToOrderLines(userContext);
			cart.refreshAll(true);			
		} catch ( FDInvalidConfigurationException e ) {
			LOGGER.warn( "Shopping list contains some items with invalid configuration.", e);
			vr.markGeneralIssue();
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
	 * @param excludedProductKeys 
	 * @return <code>true</code> if there was at least one unavailable item.
	 * @throws FDResourceException
	 */
	public static boolean doAvailabilityCheck(FDIdentity customer, FDCartModel cart, ProcessActionResult vr, List<String> unavailableItems, Set<ContentKey> excludedProductKeys)
			throws FDResourceException {
		List<FDCartLineI> list = cart.getOrderLines();
		List<FDCartLineI> detachedList = new ArrayList<FDCartLineI>(list.size());
		detachedList.addAll(list);	
		for (int i = 0; i < detachedList.size(); i++) {
			boolean isDiscountinuedSoon=false;
			FDCartLineI cartLine = detachedList.get(i);
			int randomId = cartLine.getRandomId();
			FDProductInfo prodInfo = cartLine.lookupFDProductInfo();
			ZoneInfo zone=cartLine.getUserContext().getPricingContext().getZoneInfo();
			if(EnumAvailabilityStatus.TO_BE_DISCONTINUED_SOON.equals(prodInfo.getAvailabilityStatus(zone.getSalesOrg(),zone.getDistributionChanel()))){
				isDiscountinuedSoon=true;
			}if (!prodInfo.isAvailable(zone.getSalesOrg(),zone.getDistributionChanel()) || isDiscountinuedSoon) {
				String altSkuCode = getAlternateSkuCode(cartLine,excludedProductKeys);
				final String err = "Item " + randomId + " / '" + cartLine.getProductName() + "' - SKU is unavailable/discontinued and therefore item was removed.";
				unavailableItems.add(prodInfo.getSkuCode() + " " + cartLine.getProductName());
				if(isDiscountinuedSoon) {
					vr.addUnavailableItem(cartLine, UnavailabilityReason.TBDS, err, cartLine.getQuantity(),altSkuCode);
				}	
				else if(prodInfo.isDiscontinued(zone.getSalesOrg(),zone.getDistributionChanel())){
					vr.addUnavailableItem(cartLine, UnavailabilityReason.DISC, err, cartLine.getQuantity(),altSkuCode);
				}
				else if(prodInfo.isTempUnavailable(zone.getSalesOrg(),zone.getDistributionChanel()) || !prodInfo.isAvailable(zone.getSalesOrg(),zone.getDistributionChanel())){
				vr.addUnavailableItem(cartLine, UnavailabilityReason.UNAV, err, cartLine.getQuantity(),altSkuCode);
				} 
				cart.removeOrderLineById(randomId);				
				LOGGER.debug("[AVAILABILITY CHECK] " + err);
			}
		}
						
		return vr.isFail();
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
	 * @param excludedProductKeys2 
	 * @param so 
	 * @return
	 * @throws FDResourceException
	 */
	public static boolean doATPCheck(FDIdentity customer, FDCartModel cart, ProcessActionResult vr, Set<ContentKey> excludedProductKeys)
			throws FDResourceException {
		// Cart ATP check ...
		cart = checkAvailability( customer, cart, 30000 );
		Map<String,FDAvailabilityInfo> invsInfoMap = cart.getUnavailabilityMap();
		
		// Iterate through troubled items by their cartLineID
		for (String key : invsInfoMap.keySet()) {
			FDAvailabilityInfo info = invsInfoMap.get(key);
			final Integer randomId = new Integer(key);
			FDCartLineI cartLine = cart.getOrderLineById(randomId);
			String altSkuCode = getAlternateSkuCode(cartLine,excludedProductKeys);
			final String lineId = cartLine.getRandomId() + " / '" + cartLine.getProductName();
			if (info instanceof FDRestrictedAvailabilityInfo) {
				LOGGER.debug("[ATP CHECK/1] Item '" + lineId + "' has restriction: " + ((FDRestrictedAvailabilityInfo)info).getRestriction().getReason());								
				vr.addUnavailableItem(cartLine, UnavailabilityReason.GENERAL, "Restricted availabity", cartLine.getQuantity(),altSkuCode);
				cart.removeOrderLineById(randomId);
			} else if (info instanceof FDStockAvailabilityInfo) {
				/**
				 * Cause:  less or zero amount is available
				 * Effect: remove cartLine item if qty == 0
				 *    otherwise adjust item to available qty
				 */

				// Limited quantity zero or less than desired amount available
				double availQty = ((FDStockAvailabilityInfo)info).getQuantity();
				LOGGER.debug("[ATP CHECK/2] Item '" + lineId + "' has only " + availQty + " items available.");
				if (availQty > 0) {
					// adjust quantity to amount of available
					double requestedQuantity = cart.getOrderLineById(randomId).getQuantity();				
					if(!FDStoreProperties.isIgnoreATPFailureForSO()) {
						cart.getOrderLineById(randomId).setQuantity(availQty);
					}
					if(requestedQuantity - availQty > 0) {
						vr.addUnavailableItem(cartLine, UnavailabilityReason.ATP, "Partial quantity", (requestedQuantity - availQty),altSkuCode);
					}
				} else {
					vr.addUnavailableItem(cartLine, UnavailabilityReason.ATP, "Zero quantity", cartLine.getQuantity(),altSkuCode);
					cart.removeOrderLineById(randomId);
					LOGGER.debug("item has been removed from SO cart[only for this order instance] due to unavailablity "+cartLine.getSkuCode()+", of quantity:"+cartLine.getQuantity()+
								", cart price drop is: "+cartLine.getPrice()+"$");
				}
			} else if (info instanceof FDCompositeAvailabilityInfo) {
				/**
				 * Cause:  some options are unavailable
				 * Effect: remove cartLine item
				 */
				LOGGER.debug("[ATP CHECK/3] Item '" + lineId + "' has problem with its options.");

				vr.addUnavailableItem(cartLine, UnavailabilityReason.ATP, "Some options are unavailable", cartLine.getQuantity(),altSkuCode);
				if(!FDStoreProperties.isIgnoreATPFailureForSO()) {
					cart.removeOrderLineById(randomId);
				}
			} else if (info instanceof FDMuniAvailabilityInfo) {
				/**
				 * Cause:  'FreshDirect does not deliver alcohol outside NY'
				 * Effect: remove cartLine item
				 */
				LOGGER.debug("[ATP CHECK/4] Item '" + lineId + "' -- 'FreshDirect does not deliver alcohol outside NY'");

				/// final MunicipalityInfo muni = ((FDMuniAvailabilityInfo)info).getMunicipalityInfo();
				//
				vr.addUnavailableItem(cartLine, UnavailabilityReason.GENERAL, "FreshDirect does not deliver alcohol outside NY", cartLine.getQuantity(),altSkuCode);
				cart.removeOrderLineById(randomId);
			} else { /* info.isa? {@link FDStatusAvailabilityInfo} */
				/**
				 * Cause:  OUT OF STOCK
				 * Effect: remove cartLine item
				 */
				LOGGER.debug("[ATP CHECK/5] Item '" + lineId + "' OUT OF STOCK");

				vr.addUnavailableItem(cartLine, UnavailabilityReason.ATP, "Out of stock", cartLine.getQuantity(),altSkuCode);
				if(!FDStoreProperties.isIgnoreATPFailureForSO()) {
					cart.removeOrderLineById(randomId);
				}
			}
		}

		return vr.isFail();
	}
		
	private static String getAlternateSkuCode(FDCartLineI cartLine,
			Set<ContentKey> excludedProductKeys) {
		
		String altSkuCode = null;
		
		ProductReference prodRef = cartLine.getProductRef();
		ProductModel originalProduct = prodRef.lookupProductModel();
		
		List<ProductModel> replacementProducts = ProductRecommenderUtil.getUnavailableReplacementProducts(originalProduct, excludedProductKeys);

		for (ProductModel productModel : replacementProducts) {

				if (productModel != null) {

					for (String skuCode : productModel.getSkuCodes()) {
						if (skuCode != null) {
							altSkuCode = skuCode;
							break;
						}
					}
				}
				
				if(altSkuCode != null){
					break;
				}
			}
		
		return altSkuCode;
	}
			
	
	private static Set<ContentKey> prepareExcludedProduct(FDCartModel cart) {
		
		Set<ContentKey> contentKey = new HashSet<ContentKey>();
	
		List<FDCartLineI> list = cart.getOrderLines();
		for (FDCartLineI cartLine : list){		
			contentKey.add(cartLine.getProductRef().getContentKey());		
		}
		return contentKey;
	}

	public static void sendNotification( FDStandingOrder so, MailerGatewayHome mailerHome ) throws FDResourceException {		
		try {
			List<FDOrderInfoI> orders = so.getAllOrders();
			for ( FDOrderInfoI order : orders ) {
				Date notificationDate = subtract2WorkDays( order.getRequestedDate() );
				if ( isWithin24Hours( notificationDate )) {
					if (isCancelledOrder(order)) {
						LOGGER.info( "Not sending 2days notification email as order instance is cancelled: so["+so.getId()+"], order["+order.getErpSalesId()+"]" );
					} else {
						LOGGER.info( "Sending 2days notification email: so["+so.getId()+"], order["+order.getErpSalesId()+"]" );
						sendNotificationMail( so, so.getUserInfoEx(), order.getErpSalesId(), mailerHome );
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
		 so = FDStandingOrdersManager.getInstance().manageStandingOrder(info, cart, so, saleId);

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
	 * @param isUpdateSO 
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
		user.setSuspendShowPendingOrderOverlay(false);
		if ( !EnumCheckoutMode.NORMAL.equals( origMode ) ) {
			
			// RESET
			user.setCheckoutMode(EnumCheckoutMode.NORMAL);
			user.getShoppingCart().setTransactionSource(null);
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
					FDURLUtil.getStandingOrderLandingPage(so, null, user)
					:
					FDURLUtil.getStandingOrderMainPage(user);
		} else {
			LOGGER.error("endStandingOrderCheckoutPhase() was invoked although no standing order was being modified!");
		}

		return null;
	}
	
}

class TimeslotReservationInfo {	
	
	private final static Category LOGGER = LoggerFactory.getInstance(TimeslotReservationInfo.class);
	
	private FDTimeslot timeslot;
	private DeliveryInterval deliveryTimes;
	private TimeslotEvent event;
	private ErpAddressModel deliveryAddress;
	private FDIdentity customer;
	private String deliveryAddressId;
	private FDUserI customerUser;
	private FDReservation reservation;
	private FDTimeslot selectedTimeslot;
	private boolean forceCapacity;		
	
	public TimeslotReservationInfo(
			FDTimeslot timeslot, DeliveryInterval deliveryTimes,
			TimeslotEvent event, ErpAddressModel deliveryAddress,
			FDIdentity customer, String deliveryAddressId,
			FDUserI customerUser, FDReservation reservation,
			FDTimeslot selectedTimeslot, boolean forceCapacity) {
		super();
		this.timeslot = timeslot;
		this.deliveryTimes = deliveryTimes;
		this.event = event;
		this.deliveryAddress = deliveryAddress;
		this.customer = customer;
		this.deliveryAddressId = deliveryAddressId;
		this.customerUser = customerUser;
		this.reservation = reservation;
		this.selectedTimeslot = selectedTimeslot;
		this.forceCapacity = forceCapacity;
	}	
	public FDTimeslot getSelectedTimeslot() {
		return selectedTimeslot;
	}
	public void setSelectedTimeslot(FDTimeslot selectedTimeslot) {
		this.selectedTimeslot = selectedTimeslot;
	}
	public boolean isTimeSlotGeoRestricted() {
		return timeslot.isGeoRestricted();
	}

	public FDReservation reserveTimeslot() throws FDResourceException {			
		
		if ( event == null ) {
			event = new TimeslotEvent(EnumTransactionSource.STANDING_ORDER.getCode(), false, 0.00, false, 
					false,(customerUser!=null)?customerUser.getPrimaryKey():null, EnumCompanyCode.fd.name());
		}	
		deliveryAddress = FDCustomerManager.getAddress(customer, deliveryAddressId);
		try { 
			LOGGER.info( "Trying to make reservation for timeslot: " + timeslot.getDlvStartTime()+" to "+ timeslot.getDlvEndTime());
			reservation = FDDeliveryManager.getInstance().reserveTimeslot(timeslot.getId(), customer.getErpCustomerPK(), EnumReservationType.STANDARD_RESERVATION, 
					TimeslotLogic.encodeCustomer(deliveryAddress, customerUser), false,
					null, false, event, false, null);
			
			selectedTimeslot = timeslot;
			LOGGER.info( "Timeslot reserved successfully: " + timeslot.getId()+" for time: "+timeslot.getDlvStartTime()+" to "+timeslot.getDlvEndTime() );
		} catch ( ReservationException e ) {
			if(forceCapacity){
				try {
					reservation = FDDeliveryManager.getInstance().reserveTimeslot(timeslot.getId(), customer.getErpCustomerPK(), EnumReservationType.STANDARD_RESERVATION, 
							TimeslotLogic.encodeCustomer(deliveryAddress, customerUser), false,
							null, forceCapacity, event, false, null);
					
				} catch (ReservationException e1) {						
					e1.printStackTrace();
				}
				selectedTimeslot = timeslot;
				LOGGER.info( "Timeslot reserved successfully[forceCapacity]: " + timeslot.toString() );
			} else {
				// no more capacity in this time slot
				LOGGER.info( "No more capacity in timeslot[forceCapacity]: " + timeslot.toString(), e );
			}
		}
		
		return reservation;
	}
}
