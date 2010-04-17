package com.freshdirect.webapp.checkout;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

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
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.AddressForm;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.DeliveryAddressValidator;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class DeliveryAddressManipulator extends CheckoutManipulator {
	private static Category		LOGGER	= LoggerFactory.getInstance( DeliveryAddressManipulator.class );
	

	public DeliveryAddressManipulator(PageContext context, ActionResult result, String actionName) {
		super(context, result, actionName);
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
		} else {
			this.setRegularDeliveryAddress( user, result, addressOrLocation, noContactPhonePage, request );
		}

		FDCartModel cart = user.getShoppingCart();
		if ( user.getSelectedServiceType() == EnumServiceType.HOME && ( user.isDlvPassActive() || cart.getDeliveryPassCount() > 0 ) && !( cart.isDlvPromotionApplied() ) ) {
			cart.setDlvPassApplied( true );
		}

		user.updateUserState();
		session.setAttribute( SessionName.USER, user );
	}

	
	/**
	 * Only regular addresses
	 * 
	 * @throws FDResourceException
	 */
	public void performEditAndSetDeliveryAddress() throws FDResourceException {

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
		DlvZoneInfoModel zoneInfo = AddressUtil.getZoneInfo( request, dlvAddress, result, date.getTime() );
		if ( !result.isSuccess() )
			return;

		ErpAddressModel erpAddress = addressForm.getErpAddress();
		erpAddress.setFrom( dlvAddress );
		erpAddress.setAddressInfo( dlvAddress.getAddressInfo() );

		if ( "SUFFOLK".equals( FDDeliveryManager.getInstance().getCounty( dlvAddress ) ) && erpAddress.getAltContactPhone() == null ) {
			result.addError( true, EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(), SystemMessageList.MSG_REQUIRED );
			return;
		}


		final boolean foundFraud = AddressUtil.updateShipToAddress( request, result, this.getUser(), addressId, erpAddress );

		if ( !result.isSuccess() )
			return;

		ErpAddressModel thisAddress = FDCustomerManager.getAddress( getIdentity(), addressId );

		
		FDSessionUser user = (FDSessionUser)session.getAttribute( SessionName.USER );
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
					FDCustomerManager.storeUser(user.getUser());
				}else {
					//Already is a home or a corporate customer.
					if(user.getOrderHistory().getValidOrderCount()==0) {
						//check if customer has no order history and if zipcode has changed. If yes then update the
						//service type to most recent service type.					
						//Added the following line for zone pricing to keep user service type up-to-date.
						user.setZPServiceType(dlvAddress.getServiceType());
						user.setZipCode(dlvAddress.getZipCode());
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
	}


	private void setRegularDeliveryAddress( FDUserI user, ActionResult result, String addressPK, String noContactPhonePage, HttpServletRequest request ) throws FDResourceException, JspException, RedirectToPage {
		FDIdentity identity = user.getIdentity();

		// locate the shipto address with the specified PK
		ErpAddressModel shippingAddress = FDCustomerManager.getAddress( identity, addressPK );

		if ( shippingAddress == null ) {
			throw new FDResourceException( "Specified address doesn't exist" );
		}

		AddressModel address = AddressUtil.scrubAddress( shippingAddress, result );
		// if it is a Hamptons address without the altContactNumber have user
		// edit and provide it.
		if ( "SUFFOLK".equals( FDDeliveryManager.getInstance().getCounty( address ) ) && shippingAddress.getAltContactPhone() == null ) {
			result.addError( true, "missingContactPhone", SystemMessageList.MSG_REQUIRED );
			// this.redirectTo( this.noContactPhonePage + "?addressId=" + addressPK + "&missingContactPhone=true" );
			throw new RedirectToPage( noContactPhonePage + "?addressId=" + addressPK + "&missingContactPhone=true");
		}
		EnumRestrictedAddressReason reason = FDDeliveryManager.getInstance().checkAddressForRestrictions( address );
		if ( !EnumRestrictedAddressReason.NONE.equals( reason ) ) {
			result.addError( true, "undeliverableAddress", SystemMessageList.MSG_RESTRICTED_ADDRESS );
		}
		if ( !result.isSuccess() )
			return;

		// since address looks alright need geocode
		try {
			DlvAddressGeocodeResponse geocodeResponse = FDDeliveryManager.getInstance().geocodeAddress( address );
			String geocodeResult = geocodeResponse.getResult();

			if ( !"GEOCODE_OK".equalsIgnoreCase( geocodeResult ) ) {
				//
				// since geocoding is not happening silently ignore it
				LOGGER.warn( "GEOCODE FAILED FOR ADDRESS :" + address );
			} else {
				LOGGER.debug( "setRegularDeliveryAddress : geocodeResponse.getAddress() :" + geocodeResponse.getAddress() );
				address = geocodeResponse.getAddress();
			}

		} catch ( FDInvalidAddressException iae ) {
			LOGGER.warn( "GEOCODE FAILED FOR ADDRESS setRegularDeliveryAddress  FDInvalidAddressException :" + address + "EXCEPTION :" + iae );
		}

		int validCount = this.getUser().getOrderHistory().getValidOrderCount();
		if ( validCount < 1 ) {

			String specialInstructions = ( request.getParameter( EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode() ) );
			String altDelivery = request.getParameter( EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode() );

			if ( specialInstructions != null ) {
				shippingAddress.setInstructions( specialInstructions.replaceAll( FDStoreProperties.getDlvInstructionsSpecialChar(), " " ) );
			}
			if ( altDelivery != null ) {
				shippingAddress.setAltDelivery( EnumDeliverySetting.DOORMAN );
			}
		}

		Calendar date = new GregorianCalendar();
		date.add( Calendar.DATE, 7 );

		DlvZoneInfoModel dlvResponse = AddressUtil.getZoneInfo( request, address, result, date.getTime() );
		if ( !result.isSuccess() )
			return;
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
				LOGGER.debug( "Keeping user unattended delivery instructions: " + ( shippingAddress.getUnattendedDeliveryInstructions() == null ? "OK" : shippingAddress.getUnattendedDeliveryInstructions() ) );

			}
		} else if ( "true".equals( request.getParameter( EnumUserInfoName.DLV_UNATTENDED_CONSENT_SEEN.getCode() ) ) ) {

			if ( dlvResponse.isUnattended() ) {

				if ( "OPT_IN".equals( request.getParameter( EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode() ) ) ) {
					shippingAddress.setUnattendedDeliveryFlag( EnumUnattendedDeliveryFlag.OPT_IN );
					shippingAddress.setUnattendedDeliveryInstructions( EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode() );
					String unattendedInstructions = request.getParameter( EnumUserInfoName.DLV_UNATTENDED_DELIVERY_INSTRUCTIONS.getCode() );
					if ( "".equals( unattendedInstructions ) )
						unattendedInstructions = null;
					shippingAddress.setUnattendedDeliveryInstructions( unattendedInstructions );
				} else {
					shippingAddress.setUnattendedDeliveryFlag( EnumUnattendedDeliveryFlag.OPT_OUT );
					shippingAddress.setUnattendedDeliveryInstructions( null );
				}
				AddressUtil.updateShipToAddress( request, result, this.getUser(), shippingAddress.getPK().getId(), shippingAddress );

			}

		}



		if (EnumCheckoutMode.NORMAL == user.getCheckoutMode()) {
			setDeliveryAddress(shippingAddress, dlvResponse, null, true);
		} else {
			setSODeliveryAddress(shippingAddress, dlvResponse, addressPK);
		}
	}

	private void setDepotDeliveryLocation( String locationId, HttpServletRequest request, ActionResult result ) throws FDResourceException {
		FDIdentity identity = getIdentity();

		LOGGER.debug( "Setting depot delivery location " );
		DlvDepotModel depot = FDDepotManager.getInstance().getDepotByLocationId( locationId );

		DlvLocationModel location = depot.getLocation( locationId );

		if ( location == null ) {
			throw new FDResourceException( "Specified location doesn't exist" );
		}
		PhoneNumber contactPhone = null;
		if ( depot.isPickup() ) {
			String contactNumber = request.getParameter( "contact_phone_" + locationId );
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
				DlvAddressGeocodeResponse geocodeResponse = FDDeliveryManager.getInstance().geocodeAddress( addrModel );
				String geocodeResult = geocodeResponse.getResult();

				if ( !"GEOCODE_OK".equalsIgnoreCase( geocodeResult ) ) {
					//
					// since geocoding is not happening silently ignore it
					LOGGER.warn( "GEOCODE FAILED FOR ADDRESS in setDepotDeliveryLocation :" + addrModel );
				} else {
					LOGGER.debug( "setDepotDeliveryLocation : geocodeResponse.getAddress() :" + geocodeResponse.getAddress() );
					addrModel = geocodeResponse.getAddress();
				}

			} catch ( FDInvalidAddressException iae ) {
				LOGGER.warn( "GEOCODE FAILED FOR ADDRESS setRegularDeliveryAddress  FDInvalidAddressException :" + addrModel + "EXCEPTION :" + iae );
			}

			// FDCartModel cart = getCart();
			ErpDepotAddressModel address = new ErpDepotAddressModel( addrModel );
			address.setRegionId( depot.getRegionId() );
			address.setZoneCode( location.getZoneCode() );
			address.setLocationId( location.getPK().getId() );
			address.setFacility( location.getFacility() );
			if ( this.getUser().isCorporateUser() ) {
				String instructions = NVL.apply( request.getParameter( "corpDlvInstructions" ), "" );
				address.setInstructions( instructions );
			} else {
				address.setInstructions( location.getInstructions() );
			}
			address.setPickup( depot.isPickup() );
			address.setDeliveryChargeWaived( location.getDeliveryChargeWaived() );

			ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer( getIdentity().getErpCustomerPK() );
			address.setFirstName( erpCustomer.getCustomerInfo().getFirstName() );
			address.setLastName( erpCustomer.getCustomerInfo().getLastName() );
			if ( contactPhone != null ) {
				address.setPhone( contactPhone );
			} else {
				address.setPhone( erpCustomer.getCustomerInfo().getBusinessPhone() );
			}

			// get the real zoneInfo object from deliveryManager
			DlvZoneInfoModel zoneInfo = FDDeliveryManager.getInstance().getZoneInfoForDepot( depot.getRegionId(), location.getZoneCode(), new Date() );


			// Don't record selected service type in setDeliveryAddressInternal() when called from here!
			//
			//This is commented out because we don't want to track last order type to be pick up using fduser.
			//as this creates ambiguity. selected service type is set to pickup in cases a customer signed
			//up with a pick up only address and cases when customer who have valid deliverable address places
			//a last order as pick up. Now onwards selected service type  will only define customer's type
			// from a delivery standpoint. PICKUP only or HOME or CORPORATE.
			//Use delivery info table to track last order type.
			//user.setSelectedServiceType(address.isPickup() ? EnumServiceType.PICKUP: EnumServiceType.DEPOT);
			setDeliveryAddress(address, zoneInfo, locationId, false);
		}
	}

	public void performDeleteDeliveryAddress() throws FDResourceException {
		String shipToAddressId = request.getParameter( "deleteShipToAddressId" );
		if ( shipToAddressId == null ) {
			shipToAddressId = (String)request.getAttribute( "deleteShipToAddressId" );
		}

		AddressUtil.deleteShipToAddress( getIdentity(), shipToAddressId, result, request );
	}


	/**
	 * Stores delivery address in cart
	 * 
	 * @param address Delivery address to store
	 * @param zoneInfo Zone info
	 * @param locationId Location ID for Depot type addresses
	 * @throws FDResourceException
	 */
	private void setDeliveryAddress(ErpAddressModel address, DlvZoneInfoModel zoneInfo, String locationId, boolean setServiceType) throws FDResourceException {
		final FDUserI user = setDeliveryAddressInternal(address, zoneInfo, setServiceType);

		if (address instanceof ErpDepotAddressModel) {
			FDCustomerManager.setDefaultDepotLocationPK( user.getIdentity(), locationId );
			FDCustomerManager.setDefaultShipToAddressPK( user.getIdentity(), null );
		} else {
			FDCustomerManager.setDefaultShipToAddressPK( user.getIdentity(), address.getPK().getId() );
			FDCustomerManager.setDefaultDepotLocationPK( user.getIdentity(), null );
		}
		
	}



	/**
	 * @param address
	 * @param zoneInfo
	 * @return
	 */
	private FDUserI setDeliveryAddressInternal(ErpAddressModel address,
			DlvZoneInfoModel zoneInfo, boolean setServiceType) {
		final FDCartModel cart = getCart();
		
		cart.setZoneInfo( zoneInfo );
		cart.setDeliveryAddress( address );

		final FDUserI user = this.getUser();

		// store service type except for depot locations
		if (setServiceType)
			user.setSelectedServiceType( address.getServiceType() );
		user.setShoppingCart( cart );
		session.setAttribute( SessionName.USER, user );
		return user;
	}

	private void setSODeliveryAddress(ErpAddressModel address, DlvZoneInfoModel zoneInfo, String pk) {
		setDeliveryAddressInternal(address, zoneInfo, true);
		
		FDStandingOrder so = getUser().getCurrentStandingOrder();
		LOGGER.debug("SO["+so.getId()+"] ADDRESS := " + pk);
		so.setAddressId(pk);
	}
}
