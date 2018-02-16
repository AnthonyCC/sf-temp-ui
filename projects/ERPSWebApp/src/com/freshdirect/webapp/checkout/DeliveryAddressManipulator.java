package com.freshdirect.webapp.checkout;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpDuplicateAddressException;
import com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason;
import com.freshdirect.fdlogistics.model.FDDeliveryAddressVerificationResponse;
import com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel;
import com.freshdirect.fdlogistics.model.FDDeliveryDepotModel;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.FulfillmentInfo;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.AddressForm;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.DeliveryAddressValidator;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.util.StandingOrderHelper;

/** keep in sync with LocationHandlerTag*/
public class DeliveryAddressManipulator extends CheckoutManipulator {
	private static Category		LOGGER	= LoggerFactory.getInstance( DeliveryAddressManipulator.class );
	private boolean locationHandlerMode;

    public DeliveryAddressManipulator(HttpServletRequest request, HttpServletResponse response, ActionResult result, String actionName) {
        super(request, response, result, actionName);
	}

	public static void performSetDeliveryAddress(HttpSession session, FDUserI user, String addressId, String contactNumber, String corpDlvInstructions, String actionName, boolean locationHandlerMode,
			ActionResult result, String noContactPhonePage, String specialInstructions, String altDelivery, String unattendedDeliveryNoticeSeen, String unattendedDeliveryOpt,
			String unattendedDeliveryInstr) throws FDResourceException, JspException, RedirectToPage {
        if (addressId.startsWith("DEPOT_")) {
            addressId = addressId.substring("DEPOT_".length());
        }
		FDDeliveryDepotModel depot = FDDeliveryManager.getInstance().getDepotByLocationId(addressId);
		if (depot != null) {
			setDepotDeliveryLocation(session, user, addressId, contactNumber, corpDlvInstructions, actionName);
			LOGGER.debug("performSetDeliveryAddress :" + addressId);
		} else {
			setRegularDeliveryAddress(user, session, locationHandlerMode, result, addressId, noContactPhonePage, specialInstructions, altDelivery, unattendedDeliveryNoticeSeen, unattendedDeliveryOpt,
					unattendedDeliveryInstr, actionName);
			LOGGER.debug("setRegularDeliveryAddress :" + addressId);
		}
		FDCartModel cart = user.getShoppingCart();
		if (user.getSelectedServiceType() == EnumServiceType.HOME && (user.isDlvPassActive() || cart.getDeliveryPassCount() > 0) && !(cart.isDlvPromotionApplied())) {
			cart.setDlvPassApplied(true);
		}

		user.updateUserState();
	}

	public void performSetDeliveryAddress(String noContactPhonePage) throws FDResourceException, JspException, RedirectToPage {
		String addressOrLocation = request.getParameter( "selectAddressList" );

		if ( addressOrLocation != null && addressOrLocation.startsWith( "field_" ) ) {
			addressOrLocation = request.getParameter( addressOrLocation.substring( "field_".length() ) );
		}

		if ( addressOrLocation == null || addressOrLocation.length() < 1 ) {
			result.addError( new ActionError( "address", "Please select a delivery address." ) );
			return;
		}

		FDSessionUser user = (FDSessionUser)session.getAttribute( SessionName.USER );
		
		if ( addressOrLocation.startsWith( "DEPOT_" ) ) {
			String locationId = addressOrLocation.substring( "DEPOT_".length() );
			this.setDepotDeliveryLocation( locationId, request, result );
			LOGGER.debug("performSetDeliveryAddress[DEPOT_] :"+locationId);
		} else {
			this.setRegularDeliveryAddress( user, result, addressOrLocation, noContactPhonePage, request );
			LOGGER.debug("setRegularDeliveryAddress[] :"+addressOrLocation);
		}

		FDCartModel cart = user.getShoppingCart();
		if ( user.getSelectedServiceType() == EnumServiceType.HOME && ( user.isDlvPassActive() || cart.getDeliveryPassCount() > 0 ) && !( cart.isDlvPromotionApplied() ) ) {
			cart.setDlvPassApplied( true );
		}

		user.updateUserState();
		//if(!locationHandlerMode)
			//user.resetUserContext();
        
		
		session.setAttribute( SessionName.USER, user );
	}

	public void performSetOrderMobileNumber() throws FDResourceException, JspException, RedirectToPage {
		FDSessionUser user = (FDSessionUser)session.getAttribute( SessionName.USER );
		PhoneNumber phoneNumber=new PhoneNumber(PhoneNumber.format(request.getParameter( "orderMobileNumber" ))); 
		FDCartModel cart = user.getShoppingCart();
		cart.setOrderMobileNumber(phoneNumber);
	}

	public ErpAddressModel performAddDeliveryAddress() throws FDResourceException {
		FDSessionUser user = (FDSessionUser) session.getAttribute( SessionName.USER);
		FDCartModel cart =  user.getShoppingCart();
		String actionName = getActionName();

		AddressForm addressForm = new AddressForm();
		addressForm.populateForm(request);
		
		if (actionName.equals("addDeliveryAddressEx"))
			addressForm.validateFormEx(result);
		else
			addressForm.validateForm(result);
		
		if (!result.isSuccess()){
			return null;
		}
		
		ErpAddressModel erpAddress = addressForm.getErpAddress();
		AddressModel deliveryAddressModel = addressForm.getDeliveryAddress();
		deliveryAddressModel.setServiceType(addressForm.getDeliveryAddress().getServiceType());
		
		return performAddDeliveryAddress(user, session, result, cart, actionName, erpAddress, deliveryAddressModel);
	}
	
	public static ErpAddressModel performAddDeliveryAddress(FDSessionUser user, HttpSession session, ActionResult result,FDCartModel cart,String actionName,ErpAddressModel erpAddressModel, AddressModel deliveryAddressModel) throws FDResourceException {
		// call common delivery address check
		ErpAddressModel erpAddress = checkDeliveryAddressInForm(user, result, session, cart, actionName, erpAddressModel,deliveryAddressModel);
		if (erpAddress == null) {
			return null;
		}

		try {
			boolean foundFraud = FDCustomerManager.addShipToAddress(AccountActivityUtil.getActionInfo(session), !user.isDepotUser(), erpAddress);
			if (foundFraud) {
//				session.setAttribute(SessionName.SIGNUP_WARNING, MessageFormat.format(
//					SystemMessageList.MSG_NOT_UNIQUE_INFO,
//					new Object[] {user.getCustomerServiceContact()}));
				 applyFraudChange(user, session);
			}
			/*
			 * if(user.getOrderHistory().getValidOrderCount()==0) { user.setZipCode(erpAddress.getZipCode());
			 * user.setSelectedServiceType(erpAddress.getServiceType()); }
			*/
			user.invalidateAllAddressesCaches();
		} catch (ErpDuplicateAddressException ex) {
			LOGGER.warn("AddressUtil:addShipToAddress(): ErpDuplicateAddressException caught while trying to add a shipping address to the customer info:", ex);
			result.addError(new ActionError("duplicate_user_address", "The information entered for this address matches an existing address in your account."));
		}
		return erpAddress;
	}
	
	public void performAddAndSetDeliveryAddress() throws FDResourceException {
		FDSessionUser user = (FDSessionUser) session.getAttribute( SessionName.USER);
		FDCartModel cart =  user.getShoppingCart();
		String actionName = getActionName();

		AddressForm addressForm = new AddressForm();
		addressForm.populateForm(request);
		
		if (actionName.equals("addDeliveryAddressEx"))
			addressForm.validateFormEx(result);
		else
			addressForm.validateForm(result);
		
		if (!result.isSuccess()){
			return;
		}
		
		ErpAddressModel erpAddress = addressForm.getErpAddress();
		AddressModel deliveryAddressModel = addressForm.getDeliveryAddress();
		deliveryAddressModel.setServiceType(addressForm.getDeliveryAddress().getServiceType());
		
		performAddAndSetDeliveryAddress(user, result, session, cart,actionName, erpAddress, deliveryAddressModel);
	}
	
	public static void performAddAndSetDeliveryAddress(FDSessionUser user, ActionResult result, HttpSession session, FDCartModel cart, String actionName,ErpAddressModel erpAddressModel, AddressModel deliveryAddressModel) throws FDResourceException {

		// call common delivery address check
		ErpAddressModel erpAddress = checkDeliveryAddressInForm(user, result, session, cart, actionName, erpAddressModel,deliveryAddressModel);
		if (erpAddress == null) {
			return;
		}
		
		Calendar date = new GregorianCalendar();
		date.add( Calendar.DATE, 7 );
		
		FDDeliveryZoneInfo zoneInfo = AddressUtil.getZoneInfo( user, erpAddress, result, date.getTime(), user.getHistoricOrderSize(), null);
		//APPDEV 6442 FDC Transition - We will validate the zone information again when address change happens
		try {
			FDDeliveryZoneInfo deliveryZoneInfo = user.overrideZoneInfo(erpAddress, zoneInfo);
			if(deliveryZoneInfo!=null && deliveryZoneInfo.getFulfillmentInfo()!=null){
				zoneInfo = deliveryZoneInfo;
			}
		} catch (FDInvalidAddressException e) {
			e.printStackTrace();
		}

		try {
			boolean foundFraud = FDCustomerManager.addShipToAddress(AccountActivityUtil.getActionInfo(session), !user.isDepotUser(), erpAddress);
			if (foundFraud) {
				// session.setAttribute(SessionName.SIGNUP_WARNING, MessageFormat.format(
				// SystemMessageList.MSG_NOT_UNIQUE_INFO,
				// new Object[] {user.getCustomerServiceContact()}));
				
				applyFraudChange(user, session);
			}
			if ( !result.isSuccess() )
				return;

			List<ErpAddressModel> dlvAddresses = FDCustomerFactory.getErpCustomer(user.getIdentity()).getShipToAddresses();
			ErpAddressModel thisAddress = null;
			if( dlvAddresses.size() > 0 ) {
				for(Iterator<ErpAddressModel> it = dlvAddresses.iterator(); it.hasNext();){
					ErpAddressModel addr = it.next();
					if(matchAddress(addr, erpAddress)) {
						thisAddress = addr;
						break;
					}
						
				}
				//thisAddress =  (ErpAddressModel)dlvAddresses.get( dlvAddresses.size()-1 ) ;
				if (EnumCheckoutMode.NORMAL == user.getCheckoutMode()) {
					String zoneId = zoneInfo.getZoneCode();
					if ( zoneId != null && zoneId.length() > 0 ) {
						LOGGER.debug( "success! adding address to cart & setting defaultShipToAddress." );
			
						setDeliveryAddress(user, session, actionName, thisAddress,  zoneInfo, null, true);
					}
			
					if ( foundFraud ) {
						user.invalidateCache();
					}
					user.updateUserState();
					if ( user.isFraudulent() ) {
						PromotionI promo = user.getRedeemedPromotion();
						if ( promo != null && !user.getPromotionEligibility().isEligible( promo.getPromotionCode() ) ) {
							user.setRedeemedPromotion( null );
						}
					}
					session.setAttribute( SessionName.USER, user );
				} else {
					// Set delivery address PK
					setSODeliveryAddress(user, session, thisAddress, zoneInfo, thisAddress.getPK().getId());
				}
				
				user.invalidateAllAddressesCaches();
			}
		} catch (ErpDuplicateAddressException ex) {
			LOGGER.warn("AddressUtil:addShipToAddress(): ErpDuplicateAddressException caught while trying to add a shipping address to the customer info:", ex);
			result.addError(new ActionError("duplicate_user_address", "The information entered for this address matches an existing address in your account."));
		}
	}

	public static boolean matchAddress(ErpAddressModel addr1, ErpAddressModel addr2) {
		if (addr1 == null || addr2 == null)
			return false;

		if(addr1.getAddress2() == null){
			addr1.setAddress2("");
		}
		if(addr2.getAddress2() == null){
			addr2.setAddress2("");
		}
		if(addr1.getApartment() == null){
			addr1.setApartment("");
		}
		if(addr2.getApartment() == null){
			addr2.setApartment("");
		}

		if (addr1.getAddress1() != null && addr1.getAddress1().equalsIgnoreCase(addr2.getAddress1())
				&& ((addr1.getAddress2() == null && addr2.getAddress2() == null) || (addr1.getAddress2() != null && addr1.getAddress2().equalsIgnoreCase(addr2.getAddress2())))
				&& ((addr1.getApartment() == null && addr2.getApartment() == null) || (addr1.getApartment() != null && addr1.getApartment().replaceAll(" " , "").equalsIgnoreCase(addr2.getApartment().replaceAll(" " , ""))))
				&& addr1.getCity() != null && addr1.getCity().equalsIgnoreCase(addr2.getCity()) && ((addr1.getFirstName()==null && addr2.getFirstName()==null) || (addr1.getFirstName()!=null && addr1.getFirstName().equalsIgnoreCase(addr2.getFirstName()))) && ((addr1.getLastName()==null && addr2.getLastName()==null) || (addr1.getLastName()!=null && addr1.getLastName().equalsIgnoreCase(addr2.getLastName())))) {
			return true;
		}
		return false;
	}
	public void performEditDeliveryAddress(TimeslotEvent event) throws FDResourceException {
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDCartModel cart =  user.getShoppingCart();
		String actionName = getActionName();
		String shipToAddressId = request.getParameter("updateShipToAddressId");

		AddressForm addressForm = new AddressForm();
		addressForm.populateForm(request);
		
		if (actionName.equals("addDeliveryAddressEx"))
			addressForm.validateFormEx(result);
		else
			addressForm.validateForm(result);
		if (!result.isSuccess())
			return;
		
		ErpAddressModel erpAddress = addressForm.getErpAddress();
		
		AddressModel deliveryAddressModel = addressForm.getDeliveryAddress();
		deliveryAddressModel.setServiceType(addressForm.getDeliveryAddress().getServiceType());
		
		performEditDeliveryAddress(event, user, result, session, cart, actionName, erpAddress, deliveryAddressModel,shipToAddressId);
		
		if (StandingOrderHelper.isEligibleForSo3_0(user)) {
				StandingOrderHelper.evaluteEditSoAddressID(session, user, shipToAddressId);
			}
		}
		
	public static void performEditDeliveryAddress(TimeslotEvent event,FDSessionUser user, ActionResult actionResult, HttpSession session, FDCartModel cart, String actionName,ErpAddressModel erpAddress, AddressModel deliveryAddressModel, String updatedDeliveryAddressId) throws FDResourceException {
		ErpAddressModel erpAddressModel = checkDeliveryAddressInForm(user, actionResult, session, cart, actionName, erpAddress, deliveryAddressModel);
		if (erpAddressModel == null) {
			return;
		}
		/*boolean foundFraud = AddressUtil.updateShipToAddress(session, actionResult, user, updatedDeliveryAddressId, erpAddress);
		
		//[APPDEV-5568]- Reset the usercontext, as the address is updated.
		user.resetUserContext();
		cart.setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(user));
		if (!cart.isEmpty()) {
			for (FDCartLineI cartLine : cart.getOrderLines()) {
				cartLine.setUserContext(user.getUserContext());
				
			}
		}
		try {
			cart.refreshAll(true);
		} catch (FDInvalidConfigurationException e) {
			LOGGER.warn(e);
		}*/
		boolean foundFraud = updateShipToAddress(session, actionResult, user, updatedDeliveryAddressId, erpAddress);
		
		if(foundFraud){
			/*
			 * session.setAttribute(SessionName.SIGNUP_WARNING, MessageFormat.format( SystemMessageList.MSG_NOT_UNIQUE_INFO, new Object[]
			 * {user.getCustomerServiceContact()}));
			 */
			applyFraudChange(user, session);
		}
		/*
		 * if(user.getOrderHistory().getValidOrderCount()==0) { user.setZipCode(erpAddress.getZipCode());
		 * user.setSelectedServiceType(erpAddress.getServiceType()); //user.resetPricingContext(); }
		*/
		FDReservation reservation = user.getReservation();
		if(reservation != null){
			reservation = FDCustomerManager.validateReservation(user, reservation, event);
			user.setReservation(reservation);
			session.setAttribute(SessionName.USER, user);
			if(reservation == null){
				session.setAttribute(SessionName.REMOVED_RESERVATION, Boolean.TRUE);
			}
		}
		
		user.invalidateAllAddressesCaches();
	}

	public ErpAddressModel checkDeliveryAddressInForm(HttpServletRequest request, ActionResult actionResult, HttpSession session) throws FDResourceException {
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDCartModel cart =  user.getShoppingCart();
		String actionName = getActionName();

		AddressForm addressForm = new AddressForm();
		addressForm.populateForm(request);
		
		if (actionName.equals("addDeliveryAddressEx"))
			addressForm.validateFormEx(actionResult);
		else
			addressForm.validateForm(actionResult);
		if (!actionResult.isSuccess())
			return null;

		ErpAddressModel erpAddress = addressForm.getErpAddress();
		
		AddressModel deliveryAddressModel = addressForm.getDeliveryAddress();
		deliveryAddressModel.setServiceType(addressForm.getDeliveryAddress().getServiceType());
		
		return checkDeliveryAddressInForm(user, actionResult, session, cart, actionName, erpAddress,deliveryAddressModel);
	}
	
	public static ErpAddressModel checkDeliveryAddressInForm(FDSessionUser user, ActionResult actionResult, HttpSession session, FDCartModel cart, String actionName,ErpAddressModel erpAddress, AddressModel deliveryAddressModel) throws FDResourceException {
		DeliveryAddressValidator validator = new DeliveryAddressValidator(deliveryAddressModel);
		validator.setServiceType(deliveryAddressModel.getServiceType());
		validator.setEStoreId(user.getUserContext().getStoreContext().getEStoreId().toString());

/*		if (!validator.validateAddress(actionResult)) {
			return  null;
		}*/
		if (!validator.validateAddressWithoutGeoCode(actionResult)) {
			return  null;
		}
		
		AddressModel scrubbedAddress = validator.getScrubbedAddress(); // get 'normalized' address
//		DlvServiceSelectionResult serviceResult =FDDeliveryManager.getInstance().checkZipCode(scrubbedAddress.getZipCode());
		
		if (validator.isAddressDeliverable()) {
			
			checkAndSetEbtAccepted(scrubbedAddress.getZipCode(), user, cart);
			if (user.isPickupOnly() && user.getOrderHistory().getValidOrderCount()==0) {
				//
				// now eligible for home/corporate delivery and still not placed an order.
				//
				user.setSelectedServiceType(scrubbedAddress.getServiceType());
				// Added the following line for zone pricing to keep user service type up-to-date.
				user.setZPServiceType(scrubbedAddress.getServiceType());
				user.setZipCode(scrubbedAddress.getZipCode());
				// user.setEbtAccepted(isEBTAccepted);
				FDCustomerManager.storeUser(user.getUser());
				session.setAttribute(SessionName.USER, user);
			}else {
				//Already is a home or a corporate customer.
				if(user.getOrderHistory().getValidOrderCount()==0) {
					//check if customer has no order history.					
					user.setSelectedServiceType(scrubbedAddress.getServiceType());
					//Added the following line for zone pricing to keep user service type up-to-date.
					user.setZPServiceType(scrubbedAddress.getServiceType());
					user.setZipCode(scrubbedAddress.getZipCode());
//					user.setEbtAccepted(isEBTAccepted);
					FDCustomerManager.storeUser(user.getUser());
					session.setAttribute(SessionName.USER, user);
				}
			}
		}
		
		erpAddress.setFrom(scrubbedAddress);
		erpAddress.setCity(scrubbedAddress.getCity());
		erpAddress.setScrubbedStreet(scrubbedAddress.getScrubbedStreet());
		erpAddress.setAddressInfo(scrubbedAddress.getAddressInfo());				

		LOGGER.debug("ErpAddressModel:"+scrubbedAddress);

		/*
		 * Remove Alt Contact as required for Hamptons as well as COS (for Unattended Delivery process) batchley 20110208
		 * if("SUFFOLK".equals(FDDeliveryManager.getInstance().getCounty(scrubbedAddress)) && erpAddress.getAltContactPhone() == null){
		 * actionResult.addError(true, EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(), SystemMessageList.MSG_REQUIRED); return null; }
		*/

		return erpAddress;
	}

	
	/**
	 * Order is EBT accepted => zipcode for cart's deliveryAddress is ebt accepted 
	 * 					AND => user does not have any kind of EBT alerts
	 * 					AND => user does not have any unsettled EBT order from before
	 * @param zipCode
	 * @param user
	 * @param cart
	 * @throws FDResourceException
	 */
	public static void checkAndSetEbtAccepted(String zipCode, FDUserI user, FDCartModel cart) throws FDResourceException {
		if(null != zipCode){
			FDDeliveryServiceSelectionResult serviceResult =FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode);
			boolean isEBTAccepted = null !=serviceResult ? serviceResult.isEbtAccepted():false;
			if(null !=cart.getDeliveryAddress()){
				cart.getDeliveryAddress().setEbtAccepted(isEBTAccepted);
			}
			if(isEBTAccepted){				
				isEBTAccepted = isOrderEbtAccepted(user);
			}
			user.setEbtAccepted(isEBTAccepted);
		}
	}
	
	public static boolean checkEbtAccepted(FDUserI user, String zipCode) throws FDResourceException {
		boolean isEBTAccepted = false;
		if (null != zipCode) {
			isEBTAccepted = isZipCodeEbtAccepted(zipCode);
			if (isEBTAccepted) {
				isEBTAccepted = isOrderEbtAccepted(user);
			}
		}
		return isEBTAccepted;
	}
	
	private static boolean isZipCodeEbtAccepted(String zipCode) throws FDResourceException {
		boolean isEBTAccepted = false;
		if (zipCode != null) {
			FDDeliveryServiceSelectionResult serviceResult =FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode);		
			isEBTAccepted = null != serviceResult ? serviceResult.isEbtAccepted() : false;
		}
		return isEBTAccepted;
	}
	
	private static boolean isOrderEbtAccepted(FDUserI user) throws FDResourceException {
		boolean isEBTAccepted = false;
		FDCartModel cart = user.getShoppingCart();
		if (cart instanceof FDModifyCartModel) {
			String orgSaleId = ((FDModifyCartModel) cart).getOriginalOrder().getErpSalesId();
			isEBTAccepted = !user.hasEBTAlert() && (user.getOrderHistory().getUnSettledEBTOrderCount(orgSaleId) < 1);
		} else {
			isEBTAccepted = !user.hasEBTAlert() && (user.getOrderHistory().getUnSettledEBTOrderCount() < 1);
		}
		return isEBTAccepted;
			}

	/**
	 * Only regular addresses
	 * 
	 * @throws FDResourceException
	 */
	public void performEditAndSetDeliveryAddress() throws FDResourceException {

		FDSessionUser user = (FDSessionUser)session.getAttribute( SessionName.USER );	
		String addressId = request.getParameter( "updateShipToAddressId" );

		AddressForm addressForm = new AddressForm();
		addressForm.populateForm( request );
		addressForm.validateForm( result );
		if ( !result.isSuccess() )
			return;

		AddressModel deliveryAddress = addressForm.getDeliveryAddress();
		DeliveryAddressValidator dav = new DeliveryAddressValidator( deliveryAddress );
		if ( !dav.validateAddress( result ) ) {
			return;
		}
		AddressModel dlvAddress = dav.getScrubbedAddress();

		Calendar date = new GregorianCalendar();
		date.add( Calendar.DATE, 7 );
		FDDeliveryZoneInfo zoneInfo = AddressUtil.getZoneInfo( request, dlvAddress, result, date.getTime(), this.getUser().getHistoricOrderSize(), this.getUser().getRegionSvcType(addressId) );
		//APPDEV 6442 FDC Transition - We will validate the zone information again when address switch happens 
		try {
			FDDeliveryZoneInfo deliveryZoneInfo = user.overrideZoneInfo(new ErpAddressModel(dlvAddress), zoneInfo);
			if(deliveryZoneInfo!=null && deliveryZoneInfo.getFulfillmentInfo()!=null){
				zoneInfo = deliveryZoneInfo;
			}
		} catch (FDInvalidAddressException e) {
			e.printStackTrace();
		}		
		
		if ( !result.isSuccess() )
			return;

		ErpAddressModel erpAddress = addressForm.getErpAddress();
		erpAddress.setFrom( dlvAddress );
		erpAddress.setAddressInfo( dlvAddress.getAddressInfo() );
		/*
		 * Remove Alt Contact as required for Hamptons as well as COS (for Unattended Delivery process) batchley 20110209 if ( "SUFFOLK".equals(
		 * FDDeliveryManager.getInstance().getCounty( dlvAddress ) ) && erpAddress.getAltContactPhone() == null ) { result.addError( true,
		 * EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(), SystemMessageList.MSG_REQUIRED ); return; }
		*/
		
		//final boolean foundFraud = AddressUtil.updateShipToAddress( request.getSession(), result, this.getUser(), addressId, erpAddress );
		final boolean foundFraud = updateShipToAddress( request.getSession(), result, this.getUser(), addressId, erpAddress );

		if ( !result.isSuccess() )
			return;

		ErpAddressModel thisAddress = FDCustomerManager.getAddress( getIdentity(), addressId );

		FDDeliveryServiceSelectionResult serviceResult =FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(thisAddress.getZipCode());		
		
		boolean isEBTAccepted = null !=serviceResult ? serviceResult.isEbtAccepted():false;
		isEBTAccepted = isEBTAccepted && (user.getOrderHistory().getUnSettledEBTOrderCount()<=0)&&!user.hasEBTAlert();
		if (EnumCheckoutMode.NORMAL == user.getCheckoutMode()) {
			String zoneId = zoneInfo.getZoneCode();
			if ( zoneId != null && zoneId.length() > 0 ) {
				LOGGER.debug( "success! adding address to cart & setting defaultShipToAddress." );
	
				setDeliveryAddress(thisAddress, zoneInfo, null, true);
			}
	
			if ( foundFraud ) {
				user.invalidateCache();
			}
			if (dav.isAddressDeliverable()) {
				if (user.isPickupOnly() && user.getOrderHistory().getValidOrderCount()==0) {
					//
					// now eligible for home/corporate delivery and still not placed an order.
					//
					//Added the following line for zone pricing to keep user service type up-to-date.
					user.setZPServiceType(dlvAddress.getServiceType());
					user.setZipCode(dlvAddress.getZipCode());
					user.setEbtAccepted(isEBTAccepted);
					FDCustomerManager.storeUser(user.getUser());
				}else {
					//Already is a home or a corporate customer.
					if(user.getOrderHistory().getValidOrderCount()==0) {
						//check if customer has no order history and if zipcode has changed. If yes then update the
						//service type to most recent service type.					
						//Added the following line for zone pricing to keep user service type up-to-date.
						user.setZPServiceType(dlvAddress.getServiceType());
						user.setZipCode(dlvAddress.getZipCode());
						user.setEbtAccepted(isEBTAccepted);
						FDCustomerManager.storeUser(user.getUser());
					}
				}
			}
			user.updateUserState();
			if ( user.isFraudulent() ) {
				PromotionI promo = user.getRedeemedPromotion();
				if ( promo != null && !user.getPromotionEligibility().isEligible( promo.getPromotionCode() ) ) {
					user.setRedeemedPromotion( null );
				}
			}
			session.setAttribute( SessionName.USER, user );
		} else {
			// Set delivery address PK
			setSODeliveryAddress(thisAddress, zoneInfo, addressId);
		}
		
		user.invalidateAllAddressesCaches();
	}

	private void setRegularDeliveryAddress(FDUserI user, ActionResult result, String addressPK, String noContactPhonePage, HttpServletRequest request) throws FDResourceException, JspException,
			RedirectToPage {
		String specialInstructions = (request.getParameter(EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode()));
		String altDelivery = request.getParameter(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode());
		String unattendedDeliveryNoticeSeen = request.getParameter(EnumUserInfoName.DLV_UNATTENDED_CONSENT_SEEN.getCode());
		String unattendedDeliveryOpt = request.getParameter(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode());
		String unattendedDeliveryInstr = request.getParameter(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_INSTRUCTIONS.getCode());

		setRegularDeliveryAddress(user, session, locationHandlerMode, result, addressPK, noContactPhonePage, specialInstructions, altDelivery, unattendedDeliveryNoticeSeen, unattendedDeliveryOpt,
				unattendedDeliveryInstr, getActionName());
	}

	private static void setRegularDeliveryAddress(FDUserI user, HttpSession session, boolean locationHandlerMode, ActionResult result, String addressPK, String noContactPhonePage,
			String specialInstructions, String altDelivery, String unattendedDeliveryNoticeSeen, String unattendedDeliveryOpt, String unattendedDeliveryInstr, String actionName)
			throws FDResourceException, JspException, RedirectToPage {
		FDIdentity identity = user.getIdentity();

		// locate the shipto address with the specified PK
		ErpAddressModel shippingAddress = FDCustomerManager.getAddress( identity, addressPK );

		if ( shippingAddress == null ) {
			throw new FDResourceException( "Specified address doesn't exist: "+addressPK );
		}

		// Suppress the Address Validation if the address is already scrubbed.
		AddressModel address = new AddressModel();
		address = mapToAddressModel(shippingAddress);
		String geocodeResult ="";
			try {
				FDDeliveryAddressVerificationResponse response = FDDeliveryManager.getInstance().scrubAddress(shippingAddress);
				AddressUtil.verifyAddress(shippingAddress, true, result, response);
				
				
			
		
		
//		DlvServiceSelectionResult serviceResult =FDDeliveryManager.getInstance().checkZipCode(address.getZipCode());
		/*
		 * boolean isEBTAccepted = null !=serviceResult ? (serviceResult.isEbtAccepted() && (user.getOrderHistory().getUnSettledEBTOrderCount()<=0)):false;
		 * user.setEbtAccepted(isEBTAccepted);
		 */
		// if it is a Hamptons address without the altContactNumber have user
		// edit and provide it.
		/*
		 * Remove Alt Contact as required for Hamptons as well as COS (for Unattended Delivery process) batchley 20110209 if ( "SUFFOLK".equals(
		 * FDDeliveryManager.getInstance().getCounty( address ) ) && shippingAddress.getAltContactPhone() == null ) { result.addError( true,
		 * "missingContactPhone", SystemMessageList.MSG_REQUIRED ); // this.redirectTo( this.noContactPhonePage + "?addressId=" + addressPK +
		 * "&missingContactPhone=true" ); throw new RedirectToPage( noContactPhonePage + "?addressId=" + addressPK + "&missingContactPhone=true"); }
		*/
        checkAddressRestriction(locationHandlerMode, result, address);
        
     
		if ( !result.isSuccess() ) {
			LOGGER.debug("setRegularDeliveryAddress[checkAddressForRestrictions:FAILED] :"+result);
			return;
		}
		
			geocodeResult =  response.getGeocodeResult();

			if ( !"GEOCODE_OK".equalsIgnoreCase( geocodeResult ) ) {
				// since geocoding is not happening silently ignore it
				LOGGER.warn( "GEOCODE FAILED FOR ADDRESS :" + address );
			} else {
				LOGGER.debug( "setRegularDeliveryAddress : geocodeResponse.getAddress() :" + response.getAddress() );
				address = response.getAddress();
			}

		} catch ( FDInvalidAddressException iae ) {
			LOGGER.warn( "GEOCODE FAILED FOR ADDRESS setRegularDeliveryAddress  FDInvalidAddressException :" + address + "EXCEPTION :" + iae );
		}
    
    
		
		
		int validCount = user.getOrderHistory().getValidOrderCount();
		if ( validCount < 1 ) {

			if ( specialInstructions != null ) {
				shippingAddress.setInstructions( specialInstructions.replaceAll( FDStoreProperties.getDlvInstructionsSpecialChar(), " " ) );
			}
			if ( altDelivery != null ) {
				shippingAddress.setAltDelivery( EnumDeliverySetting.DOORMAN );
			}
		}

		Calendar date = new GregorianCalendar();
		date.add( Calendar.DATE, 7 );
		
		LOGGER.debug("setRegularDeliveryAddress[getZoneInfo:START] :"+date.getTime());
		
		Date startDateTime = date.getTime();
		FDCartModel cart =  user.getShoppingCart();
		if(cart!=null && cart.getDeliveryReservation()!=null && cart.getDeliveryReservation().getStartTime()!=null){
			startDateTime = cart.getDeliveryReservation().getStartTime();
		}
		
		FDDeliveryZoneInfo dlvResponse = AddressUtil.getZoneInfo( user, address, result, startDateTime, user.getHistoricOrderSize(),  user.getRegionSvcType(address.getId()));
		//APPDEV 6442 FDC Transition - We will validate the zone information again when address change happens
		try {
			FDDeliveryZoneInfo deliveryZoneInfo = user.overrideZoneInfo(shippingAddress, dlvResponse);
			if(deliveryZoneInfo!=null && deliveryZoneInfo.getFulfillmentInfo()!=null){
				dlvResponse = deliveryZoneInfo;
			}
		} catch (FDInvalidAddressException e) {
			e.printStackTrace();
		}
		if ( !result.isSuccess() ) {
			LOGGER.debug("setRegularDeliveryAddress[getZoneInfo:FAILED] :"+result);
			return;
		}
		AddressInfo info = address.getAddressInfo();
		if ( info == null ) {
			info = new AddressInfo();
		}
		info.setZoneId( dlvResponse.getZoneId() );
		info.setZoneCode( dlvResponse.getZoneCode() );
		address.setAddressInfo( info );

		//
		// set the scrubbed address on the erpAddress
		//
		shippingAddress.setAddressInfo( address.getAddressInfo() );

		// check unattended delivery at this time
		//
		// if the user opted out or has not seen the unattended delivery notice,
		// simply ignore
		// if he opted in, check if the unattended delivery is available for the
		// zone.
		// if not, set the flag to DO_NOT_USE. This will ensure that Unattended
		// Delivery
		// instructions are not written on to SAP
		if ( EnumUnattendedDeliveryFlag.OPT_IN.equals( shippingAddress.getUnattendedDeliveryFlag() ) ) {
			// TODO IMPORTANT: this checks date for today + 7
			if ( !dlvResponse.isUnattended() ) {
				shippingAddress.setUnattendedDeliveryFlag( EnumUnattendedDeliveryFlag.DISCARD_OPT_IN );
				LOGGER.debug( "Overriding user preferences for unattended delivery: zone is not eligible for requested time" );
			} else {
				LOGGER.debug("Keeping user unattended delivery instructions: "
						+ (shippingAddress.getUnattendedDeliveryInstructions() == null ? "OK" : shippingAddress.getUnattendedDeliveryInstructions()));

			}
		} else if ("true".equals(unattendedDeliveryNoticeSeen)) {

			if ( dlvResponse.isUnattended() ) {

				if ("OPT_IN".equals(unattendedDeliveryOpt)) {
					shippingAddress.setUnattendedDeliveryFlag( EnumUnattendedDeliveryFlag.OPT_IN );
					shippingAddress.setUnattendedDeliveryInstructions( EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode() );
					if ("".equals(unattendedDeliveryInstr))
						unattendedDeliveryInstr = "OK";
					shippingAddress.setUnattendedDeliveryInstructions(unattendedDeliveryInstr);
				} else {
					shippingAddress.setUnattendedDeliveryFlag( EnumUnattendedDeliveryFlag.OPT_OUT );
					shippingAddress.setUnattendedDeliveryInstructions( null );
				}
//				AddressUtil.updateShipToAddress(session, result, user, shippingAddress.getPK().getId(), shippingAddress);
				updateShipToAddress(session, result, user, shippingAddress.getPK().getId(), shippingAddress);

			}

		}

		if (EnumCheckoutMode.NORMAL == user.getCheckoutMode()) {
			setDeliveryAddress(user, session, actionName, shippingAddress, dlvResponse, null, true);
		} else {
			setSODeliveryAddress(user, session, shippingAddress, dlvResponse, addressPK);
		}
	}

	
	/**
	 * @param addressModel
	 * @return
	 */
	private static AddressModel mapToAddressModel(ErpAddressModel shippingAddress){
		AddressModel address =null;
		if(shippingAddress != null){
			address = new AddressModel();
			address.setId(shippingAddress.getId());
			address.setAddress1(shippingAddress.getAddress1());
			address.setAddress2(shippingAddress.getAddress2());
			if(shippingAddress.getAddressInfo() != null){
				address.setAddressInfo(shippingAddress.getAddressInfo());
			}else{
				AddressInfo addressInfo = new AddressInfo();
				if(shippingAddress.getScrubbedStreet() != null){
					addressInfo.setScrubbedStreet(shippingAddress.getScrubbedStreet());
				}
				address.setAddressInfo(addressInfo);
			}
			address.setApartment(shippingAddress.getApartment());
			address.setCity(shippingAddress.getCity());
			address.setState(shippingAddress.getState());
			address.setZipCode(shippingAddress.getZipCode());
			address.setCountry(shippingAddress.getCountry());
			address.setServiceType(shippingAddress.getServiceType());
		}
		return address;
	}
    public static void checkAddressRestriction(boolean locationHandlerMode, ActionResult result, AddressModel address) throws FDResourceException {
        EnumRestrictedAddressReason reason = FDDeliveryManager.getInstance().checkAddressForRestrictions( address );
		if ( !EnumRestrictedAddressReason.NONE.equals( reason ) ) {
            result.addError(true, "undeliverableAddress", locationHandlerMode ? SystemMessageList.MSG_RESTRICTED_ADDRESS_LOCATION_BAR : SystemMessageList.MSG_RESTRICTED_ADDRESS);
		}
    }

    
    
	private static void setDepotDeliveryLocation(HttpSession session, FDUserI user, String locationId, String contactNumber, String corpDlvInstructions, String actionName) throws FDResourceException {
		FDIdentity identity = user.getIdentity();

		LOGGER.debug( "Setting depot delivery location " );
		FDDeliveryDepotModel depot = FDDeliveryManager.getInstance().getDepotByLocationId( locationId );
		
		FDDeliveryDepotLocationModel location = depot.getLocation(locationId);

		if ( location == null ) {
			throw new FDResourceException( "Specified location doesn't exist" );
		}
		PhoneNumber contactPhone = null;
		if ( depot.isPickup() ) {
			LOGGER.debug( "setDepotDeliveryLocation(): contactNumber=" + contactNumber );

			if ( contactNumber != null && !"".equals( contactNumber ) ) {
				contactPhone = new PhoneNumber( contactNumber );

				ErpCustomerInfoModel infoModel = FDCustomerFactory.getErpCustomerInfo( identity );
				if ( !contactPhone.equals( infoModel.getOtherPhone() ) ) {
					infoModel.setOtherPhone( contactPhone );
					FDCustomerManager.updateCustomerInfo( AccountActivityUtil.getActionInfo( session ), infoModel );
				}
			}
		}

		if ( depot != null ) {

			// since address need geocode

			AddressModel addrModel = location.getAddress();
			try {
				FDDeliveryManager.getInstance().scrubAddress(addrModel);
			} catch ( FDInvalidAddressException iae ) {
				LOGGER.warn( "GEOCODE FAILED FOR ADDRESS setRegularDeliveryAddress  FDInvalidAddressException :" + addrModel + "EXCEPTION :" + iae );
			}

			// FDCartModel cart = getCart();
			ErpDepotAddressModel address = new ErpDepotAddressModel( addrModel );
			address.setRegionId( depot.getRegionId() );
			address.setZoneCode( location.getZoneCode() );
			address.setLocationId( location.getAddress().getId() );
			address.setFacility( location.getFacility() );
			if (user.isCorporateUser()) {
				address.setInstructions(corpDlvInstructions);
			} else {
				address.setInstructions( location.getInstructions() );
			}
			address.setPickup( depot.isPickup() );
			address.setDeliveryChargeWaived( location.getDeliveryChargeWaived() );

			ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer(identity.getErpCustomerPK());
			address.setFirstName( erpCustomer.getCustomerInfo().getFirstName() );
			address.setLastName( erpCustomer.getCustomerInfo().getLastName() );
			if ( contactPhone != null ) {
				address.setPhone( contactPhone );
			} else {
				address.setPhone( erpCustomer.getCustomerInfo().getBusinessPhone() );
			}

			// get the real zoneInfo object from deliveryManager
			FDDeliveryZoneInfo zoneInfo = null;
			try {
				zoneInfo = FDDeliveryManager.getInstance().getZoneInfo(address, new Date(), null, null, (identity!=null)?identity.getErpCustomerPK():null );
			} catch (FDInvalidAddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Don't record selected service type in setDeliveryAddressInternal() when called from here!
			//
			//This is commented out because we don't want to track last order type to be pick up using fduser.
			//as this creates ambiguity. selected service type is set to pickup in cases a customer signed
			//up with a pick up only address and cases when customer who have valid deliverable address places
			//a last order as pick up. Now onwards selected service type  will only define customer's type
			// from a delivery standpoint. PICKUP only or HOME or CORPORATE.
			//Use delivery info table to track last order type.
            user.setSelectedServiceType(address.isPickup() ? EnumServiceType.PICKUP : EnumServiceType.DEPOT);
			setDeliveryAddress(user, session, actionName, address, zoneInfo, locationId, false);
		}
	}

	private void setDepotDeliveryLocation(String locationId, HttpServletRequest request, ActionResult result) throws FDResourceException {
		String corpDlvInstructions = NVL.apply(request.getParameter("corpDlvInstructions"), "");
		String contactNumber = request.getParameter("contact_phone_" + locationId);
		setDepotDeliveryLocation(session, getUser(), locationId, contactNumber, corpDlvInstructions, getActionName());
	}

	public void performDeleteDeliveryAddress(TimeslotEvent event) throws FDResourceException {
		String shipToAddressId = request.getParameter( "deleteShipToAddressId" );
		if ( shipToAddressId == null ) {
			shipToAddressId = (String)request.getAttribute( "deleteShipToAddressId" );
		}
		performDeleteDeliveryAddress(getUser(), session, shipToAddressId, result, event);
	}

	public static void performDeleteDeliveryAddress(FDUserI user, HttpSession session, String shipToAddressId, ActionResult result, TimeslotEvent event) throws FDResourceException {
		if(StandingOrderHelper.isEligibleForSo3_0(user)){
			StandingOrderHelper.evaluteSoAddressId(session, user, shipToAddressId);
		}
		AddressUtil.deleteShipToAddress(user.getIdentity(), shipToAddressId, result, session);
		//check that if this address had any outstanding reservations.
		FDReservation reservation = user.getReservation();
		if (reservation != null) {
			reservation = FDCustomerManager.validateReservation(user, reservation, event);
			user.setReservation(reservation);
			session.setAttribute(SessionName.USER, user);
			if(reservation == null){
				session.setAttribute(SessionName.REMOVED_RESERVATION, Boolean.TRUE);
			}
		}
		user.invalidateAllAddressesCaches();
	}

	private void setDeliveryAddress(ErpAddressModel address, FDDeliveryZoneInfo zoneInfo, String locationId, boolean setServiceType) throws FDResourceException {
		setDeliveryAddress(getUser(), session, getActionName(), address, zoneInfo, locationId, setServiceType);
	}

	/**
	 * Stores delivery address in cart
	 * 
	 * @param address
	 *            Delivery address to store
	 * @param zoneInfo
	 *            Zone info
	 * @param locationId
	 *            Location ID for Depot type addresses
	 * @throws FDResourceException
	 */
	private static void setDeliveryAddress(FDUserI user, HttpSession session, String actionName, ErpAddressModel address, FDDeliveryZoneInfo zoneInfo, String locationId, boolean setServiceType)
			throws FDResourceException {
		FDCartModel cart = getCart(user, actionName);
		setDeliveryAddressInternal(user, session, cart, address, zoneInfo, setServiceType);


		if (address instanceof ErpDepotAddressModel) {
			FDCustomerManager.setDefaultDepotLocationPK( user.getIdentity(), locationId );
			FDCustomerManager.setDefaultShipToAddressPK( user.getIdentity(), null );
		} else {
			if(!(user.isVoucherHolder() && user.getMasqueradeContext() == null)){
				FDCustomerManager.setDefaultShipToAddressPK( user.getIdentity(), address.getPK().getId() );
				FDCustomerManager.setDefaultDepotLocationPK( user.getIdentity(), null );
			}
		}
		
	}

	private static void setDeliveryAddressInternal(FDUserI user, HttpSession session, final FDCartModel cart, ErpAddressModel address, FDDeliveryZoneInfo zoneInfo, boolean setServiceType)
			throws FDResourceException {
		
		FDDeliveryZoneInfo _zoneInfo=cart.getZoneInfo();
		boolean isAddressNotMatched = (address!=null && !address.equals(cart.getDeliveryAddress()));
		boolean isZoneInfoNotMatched = ((_zoneInfo!=null && zoneInfo!=null && !_zoneInfo.getZoneCode().equals(zoneInfo.getZoneCode()))||(_zoneInfo==null && zoneInfo!=null));
		if((isAddressNotMatched || isZoneInfoNotMatched) || (null == cart.getDeliveryAddress() && address!=null)){
//		if(address!=null && !address.equals(cart.getDeliveryAddress()) &&
//				((_zoneInfo!=null && zoneInfo!=null && !_zoneInfo.getZoneCode().equals(zoneInfo.getZoneCode()))||(_zoneInfo==null && zoneInfo!=null)) ) {
			cart.setZoneInfo( zoneInfo );
			cart.setDeliveryAddress( address );
			user.setAddress(address);
			user.setZPServiceType(address.getServiceType());// added as part of APPDEV-6036. We are updating the zone pricing service type to be in sync with ErpAddressModel address object
			user.resetUserContext();
			cart.setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(user));
			if (!cart.isEmpty()) {
				for (FDCartLineI cartLine : cart.getOrderLines()) {
					cartLine.setUserContext(user.getUserContext());
					cartLine.setFDGroup(null);//clear the group
					
				}
			}
			try {
				cart.refreshAll(true);
			} catch (FDInvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cart.setZoneInfo( zoneInfo );
		cart.setDeliveryAddress( address );
			if(cart.getDeliveryPlantInfo()==null) {
				LOGGER.warn("DeliveryPlantInfo is null.Setting it for user:"+user.getPrimaryKey());
				cart.setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(user));
			}
		if(cart.getEStoreId() == null){
			cart.setEStoreId(user.getUserContext().getStoreContext().getEStoreId());
		}
		ErpDeliveryPlantInfoModel dpInfoModel = FDUserUtil.getDeliveryPlantInfo(user);
		if(null !=dpInfoModel && !dpInfoModel.equals(cart.getDeliveryPlantInfo())){
			String customerId =cart.getDeliveryAddress()!=null?cart.getDeliveryAddress().getCustomerId():"";
			LOGGER.warn("DeliveryPlantInfo is not matching for customer: "+customerId +" and eStore :"+cart.getEStoreId());
			LOGGER.warn("In FDUser:"+dpInfoModel);
			LOGGER.warn("In Cart:"+cart.getDeliveryPlantInfo());
			user.setAddress(address);
			user.resetUserContext();
			cart.setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(user));
			if (!cart.isEmpty()) {
				for (FDCartLineI cartLine : cart.getOrderLines()) {
					cartLine.setUserContext(user.getUserContext());
					cartLine.setFDGroup(null);//clear the group
					
				}
			}
			try {
				cart.refreshAll(true);
			} catch (FDInvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(null != address){
			checkAndSetEbtAccepted(address.getZipCode(), user,cart);
	
			// store service type except for depot locations
			if (setServiceType)
				user.setSelectedServiceType( address.getServiceType() );
		}
		user.setShoppingCart( cart );
		//cart.doCleanup();
		session.setAttribute( SessionName.USER, user );
		//return user;
	}

	private static void setSODeliveryAddress(FDUserI user, HttpSession session, ErpAddressModel address, FDDeliveryZoneInfo zoneInfo, String pk) throws FDResourceException {
		setDeliveryAddressInternal(user, session, user.getShoppingCart(), address, zoneInfo, true);
		FDStandingOrder so = user.getCurrentStandingOrder();
		LOGGER.debug("SO["+so.getId()+"] ADDRESS := " + pk);
		so.setAddressId(pk);
	}
	
	private void setSODeliveryAddress(ErpAddressModel address, FDDeliveryZoneInfo zoneInfo, String pk) throws FDResourceException {
		setSODeliveryAddress(getUser(), session, address, zoneInfo, pk);
	}

	private static void applyFraudChange(FDUserI user, HttpSession session) throws FDResourceException {
		user.invalidateCache();
		if(user.isFraudulent()){
			user.updateUserState();
			PromotionI promo = user.getRedeemedPromotion();
			if(promo != null && !user.getPromotionEligibility().isEligible(promo.getPromotionCode())) {
				user.setRedeemedPromotion(null);
			}
		}
		session.setAttribute(SessionName.USER, user);
	}

	public boolean isLocationHandlerMode() {
		return locationHandlerMode;
	}

	public void setLocationHandlerMode(boolean locationHandlerMode) {
		this.locationHandlerMode = locationHandlerMode;
	}
	
	
	public static boolean updateShipToAddress(HttpSession session, ActionResult result, FDUserI user, String shipToAddressId, ErpAddressModel address) throws FDResourceException {
		boolean foundFraud = AddressUtil.updateShipToAddress(session, result, user, shipToAddressId, address);
		FDCartModel cart =user.getShoppingCart();
		if(null !=cart){
			//[APPDEV-5568]- Reset the usercontext, as the address is updated.
			user.setZPServiceType(address.getServiceType());// added as part of APPDEV-6036. We are updating the zone pricing service type to be in sync with ErpAddressModel address object
			user.resetUserContext();
			cart.setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(user));
			if (!cart.isEmpty()) {
				for (FDCartLineI cartLine : cart.getOrderLines()) {
					cartLine.setUserContext(user.getUserContext());
					cartLine.setFDGroup(null);//clear the group
					
				}
			}
			try {
				cart.refreshAll(true);
			} catch (FDInvalidConfigurationException e) {
				LOGGER.warn(e);
			}
		}
		return foundFraud;
	}
}
