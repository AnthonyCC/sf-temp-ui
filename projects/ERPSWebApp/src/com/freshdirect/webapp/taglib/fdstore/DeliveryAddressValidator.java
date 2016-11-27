package com.freshdirect.webapp.taglib.fdstore;

import java.text.MessageFormat;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdlogistics.model.FDDeliveryAddressGeocodeResponse;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.model.EnumAddressVerificationResult;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;

/**
 * Utility class to validate a delivery address. 
 *
 * @author segabor
 *
 */
public class DeliveryAddressValidator {
	
	private final static Category LOGGER = LoggerFactory.getInstance(DeliveryAddressValidator.class);

	// address to check
	private final AddressModel address;

	// ignore address geocode failure if set to false
	private final boolean strictCheck;

	// scrubbed address
	private AddressModel scrubbedAddress;
	private FDDeliveryServiceSelectionResult serviceResult;
	
	private String eStoreId;
	private EnumServiceType serviceType = null;

	public DeliveryAddressValidator(AddressModel address) {
		this(address, true);
	}

	public DeliveryAddressValidator(AddressModel address, boolean strictCheck) {
		this.address = address;
		this.strictCheck = strictCheck;
	}

	/**
	 * Validate the address. After validation, additional information will be available in
	 * {@link #getScrubbedAddress()} and {@link #getServiceResult()}.
	 * 
	 * @param actionResult validation errors are recoreded here. (note: should not have prior validation errors)
	 * @return false if there were any validation errors
	 * @throws FDResourceException
	 */
	public boolean validateAddress(ActionResult actionResult) throws FDResourceException {
		
		// [1] normalize (scrub) address
		scrubbedAddress = doScrubAddress(address, actionResult);
		LOGGER.debug("scrubbedAddress after scrub:"+scrubbedAddress);

		if (actionResult.isFailure()){
			return false;
		}

		// Rule: restrict corporate addresses from registering for home delivery
		if ( EnumAddressType.FIRM.equals( scrubbedAddress.getAddressType() ) && !EnumServiceType.CORPORATE.equals( address.getServiceType() ) ) {
			giveCommercialAddressError(actionResult);
			return false;
		}

		scrubbedAddress.setServiceType(address.getServiceType());
		String geocodeResult = "";
		try {
			// [2] Check services for scrubbed address

			serviceResult = doCheckAddress(scrubbedAddress);
			if ( !isAddressDeliverable() ) {
				// post validations
				if ( !EnumServiceType.HOME.equals( address.getServiceType() ) || serviceResult.getServiceStatus( EnumServiceType.PICKUP ).equals( EnumDeliveryStatus.DONOT_DELIVER ) ) {

					if ( EnumServiceType.CORPORATE.equals( address.getServiceType() ) && !serviceResult.getServiceStatus( EnumServiceType.HOME ).equals( EnumDeliveryStatus.DONOT_DELIVER ) ) {
						actionResult.addError( true, EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), SystemMessageList.MSG_HOME_NO_COS_DLV_ADDRESS );
					} else {
						actionResult.addError( true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS );
					}
					return false;
				}// NOT(address type == HOME AND service status == DELIVER)
				
				if(getEStoreId() != null && EnumServiceType.FDX.equals(EnumServiceType.getEnum(getEStoreId())) && 
						serviceResult.getServiceStatus(EnumServiceType.FDX).equals(EnumDeliveryStatus.DONOT_DELIVER)) {
					actionResult.addError( true, EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS );
					return false;
				}
			}
			
			// [3] since address looks alright need geocode
			FDDeliveryAddressGeocodeResponse geocodeResponse = doGeocodeAddress(scrubbedAddress);		
		    geocodeResult = geocodeResponse.getResult();
		    
			if ( !"GEOCODE_OK".equalsIgnoreCase( geocodeResult ) ) {
				//
				// since geocoding is not happening silently ignore it
				LOGGER.warn( "GEOCODE FAILED FOR ADDRESS :" + scrubbedAddress );
				// actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(),
				// SystemMessageList.MSG_INVALID_ADDRESS);

			} else {
				LOGGER.debug( "geocodeResponse.getAddress() :" + geocodeResponse.getAddress() );
				scrubbedAddress = geocodeResponse.getAddress();
			}			
			
		} catch (FDInvalidAddressException iae) {

			//
			// Other comment came from RegistrationAction:
			// <i>geocoding failed. if user is pickup or depot, we don't care</i>
			/*
			 * batchley 20110203 -
			 * except, if it's pickup only AND geocode fails, we need an error here.
			 * otherwise, we end up with the MSG_TECHNICAL_ERROR msg, which is less than helpful
			 */
			if (this.strictCheck) {
				//check for just a missing Address1
				if ((this.scrubbedAddress.getAddress1()).trim().equals("")) {
					//return just that error
					actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
				}else{
					//otherwise, possible bad geocode. return new geocode msg
					actionResult.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), 
						MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE_EXTRA, new Object[] {SystemMessageList.CUSTOMER_SERVICE_CONTACT}));
				}
				return false;
			}else if (!"GEOCODE_OK".equalsIgnoreCase( geocodeResult ) && serviceResult == null) {
				actionResult.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), 
					MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE, new Object[] {SystemMessageList.CUSTOMER_SERVICE_CONTACT}));
				return false;
			}
		}
		
		return true;
	}

	private void giveCommercialAddressError(ActionResult actionResult) {
		if(getEStoreId() != null && EnumServiceType.FDX.equals(EnumServiceType.getEnum(getEStoreId()))){
			actionResult.addError( new ActionError( EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), SystemMessageList.MSG_COMMERCIAL_ADDRESS_FDX ) );
		}
		else
		{
			actionResult.addError( new ActionError( EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), SystemMessageList.MSG_COMMERCIAL_ADDRESS ) );
		}
	}

	/**
	 * Validate the address. After validation, additional information will be available in
	 * {@link #getScrubbedAddress()} and {@link #getServiceResult()}.
	 * 
	 * @param actionResult validation errors are recoreded here. (note: should not have prior validation errors)
	 * @return false if there were any validation errors
	 * @throws FDResourceException
	 */
	public boolean validateAddressWithoutGeoCode(ActionResult actionResult) throws FDResourceException {
		// [1] normalize (scrub) address
		scrubbedAddress = doScrubAddress(address, actionResult);
		LOGGER.debug("scrubbedAddress after scrub:"+scrubbedAddress);

		if (actionResult.isFailure()){
			return false;
		}

		// Rule: restrict corporate addresses from registering for home delivery
		if ( EnumAddressType.FIRM.equals( scrubbedAddress.getAddressType() ) && !EnumServiceType.CORPORATE.equals( address.getServiceType() ) ) {
			giveCommercialAddressError(actionResult);
			return false;
		}

		scrubbedAddress.setServiceType(address.getServiceType());
		String geocodeResult = "";
		try {
			// [2] Check services for scrubbed address

			serviceResult = doCheckAddress(scrubbedAddress);
			
			if(!serviceTypeValidation(actionResult)){
				return false;
			}
			// This Validation is required if User selected Service type is mismatched with SmartyStreets returned RDI type.
			if(this.getServiceType() != null){
				EnumServiceType serviceType = EnumServiceType.getEnum(NVL.apply(this.getServiceType().getName(), ""));
				if(!serviceType.equals(this.scrubbedAddress.getServiceType())){
					this.scrubbedAddress.setServiceType(serviceType);
					if(!serviceTypeValidation(actionResult)){
						return false;
					}
				}
			}
			
		} catch (FDInvalidAddressException iae) {
			if (this.strictCheck) {
				//check for just a missing Address1
				if ((this.scrubbedAddress.getAddress1()).trim().equals("")) {
					//return just that error
					actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
				}else{
					//otherwise, possible bad geocode. return new geocode msg
					actionResult.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), 
						MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE_EXTRA, new Object[] {SystemMessageList.CUSTOMER_SERVICE_CONTACT}));
				}
				return false;
			}
			/*else if (!"GEOCODE_OK".equalsIgnoreCase( geocodeResult ) && serviceResult == null) {
				actionResult.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), 
					MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE, new Object[] {SystemMessageList.CUSTOMER_SERVICE_CONTACT}));
				return false;
			}*/
		}
		
		return true;
	}

	/**
	 * Validate the address. After validation, additional information will be available in
	 * {@link #getScrubbedAddress()} and {@link #getServiceResult()}.
	 * 
	 * @param actionResult validation errors are recoreded here. (note: should not have prior validation errors)
	 * @return false if there were any validation errors
	 * @throws FDResourceException
	 */
	public boolean validateScrubbedAddress(ActionResult actionResult,String geocodeResult) throws FDResourceException {
		scrubbedAddress = this.address;
		
		if (actionResult.isFailure()){
			return false;
		}
		// Rule: restrict corporate addresses from registering for home delivery
		if ( EnumAddressType.FIRM.equals( scrubbedAddress.getAddressType() ) && !EnumServiceType.CORPORATE.equals( scrubbedAddress.getServiceType() ) ) {
			giveCommercialAddressError(actionResult);
			return false;
		}
		try {
			// [1] Check services for scrubbed address
			
			serviceResult = doCheckAddress(scrubbedAddress);
			if(!serviceTypeValidation(actionResult)){
				return false;
			}
			// This Validation is required if User selected Service type is mismatched with SmartyStreets returned RDI type.
			if(this.getServiceType() != null){
				EnumServiceType serviceType = EnumServiceType.getEnum(NVL.apply(this.getServiceType().getName(), ""));
				if(!serviceType.equals(this.scrubbedAddress.getServiceType())){
					this.scrubbedAddress.setServiceType(serviceType);
					if(!serviceTypeValidation(actionResult)){
						return false;
					}
				}
			}
		} catch (FDInvalidAddressException iae) {
			if (this.strictCheck) {
				//check for just a missing Address1
				if ((this.scrubbedAddress.getAddress1()).trim().equals("")) {
					//return just that error
					actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
				}else{
					//otherwise, possible bad geocode. return new geocode msg
					actionResult.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), 
						MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE_EXTRA, new Object[] {SystemMessageList.CUSTOMER_SERVICE_CONTACT}));
				}
				return false;
			}else if (!"GEOCODE_OK".equalsIgnoreCase( geocodeResult ) && serviceResult == null) {
				actionResult.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), 
					MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE, new Object[] {SystemMessageList.CUSTOMER_SERVICE_CONTACT}));
				return false;
			}
		}
		return true;
	}
	
	private boolean serviceTypeValidation(ActionResult actionResult) throws FDResourceException{
		
		if ( !isAddressDeliverable() ) {
			// post validations
			if ( !EnumServiceType.HOME.equals( scrubbedAddress.getServiceType() ) || serviceResult.getServiceStatus( EnumServiceType.PICKUP ).equals( EnumDeliveryStatus.DONOT_DELIVER ) ) {

				if ( EnumServiceType.CORPORATE.equals( scrubbedAddress.getServiceType() ) && !serviceResult.getServiceStatus( EnumServiceType.HOME ).equals( EnumDeliveryStatus.DONOT_DELIVER ) ) {
					actionResult.addError( true, EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), SystemMessageList.MSG_HOME_NO_COS_DLV_ADDRESS );
				} else {
					actionResult.addError( true, EnumUserInfoName.DLV_ADDRESS_1SS.getCode(), SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS_SS );
				}
				return false;
			}// NOT(address type == HOME AND service status == DELIVER)
			
			if(getEStoreId() != null && EnumServiceType.FDX.equals(EnumServiceType.getEnum(getEStoreId())) && 
					serviceResult.getServiceStatus(EnumServiceType.FDX).equals(EnumDeliveryStatus.DONOT_DELIVER)) {
				actionResult.addError( true, EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS );
				return false;
			}
			
			// 
		}
		return true;
	}
	
	public AddressModel getScrubbedAddress() {
		return scrubbedAddress;
	}


	public FDDeliveryServiceSelectionResult getServiceResult() {
		return serviceResult;
	}

	public String getEStoreId() {
		return eStoreId;
	}

	public void setEStoreId(String eStoreId) {
		this.eStoreId = eStoreId;
	}

	public boolean isAddressDeliverable() throws FDResourceException {
		if (serviceResult == null) {
			return false;
		}
		EnumDeliveryStatus status = serviceResult.getServiceStatus(scrubbedAddress.getServiceType());
		if(this.getEStoreId() != null && this.getEStoreId().equalsIgnoreCase("FDX")) {
			status = serviceResult.getServiceStatus(EnumServiceType.getEnum(eStoreId.toUpperCase()));
		}
		return EnumDeliveryStatus.DELIVER.equals(status) || EnumDeliveryStatus.PARTIALLY_DELIVER.equals(status) || EnumDeliveryStatus.COS_ENABLED.equals(status);
	}
	
	
	protected AddressModel doScrubAddress(AddressModel addr, ActionResult result) throws FDResourceException {
		return AddressUtil.scrubAddress(addr, result);
	}

	protected FDDeliveryServiceSelectionResult doCheckAddress(AddressModel addr) throws FDResourceException, FDInvalidAddressException {
		return FDDeliveryManager.getInstance().getDeliveryServicesByAddress(addr);
	}

	protected FDDeliveryAddressGeocodeResponse doGeocodeAddress(AddressModel addr) throws FDResourceException, FDInvalidAddressException {
		 return FDDeliveryManager.getInstance().geocodeAddress(addr);		
	}

	/**
	 * @return the serviceType
	 */
	public EnumServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(EnumServiceType serviceType) {
		this.serviceType = serviceType;
	}
	
	//This is added to resolved the SO issue. needs to refactor Address Cleansing Code as it duplicate in several places. will need to come back to this later.
	

	/**
	 * Validate the address. After validation, additional information will be available in
	 * {@link #getScrubbedAddress()} and {@link #getServiceResult()}.
	 * 
	 * @param actionResult validation errors are recoreded here. (note: should not have prior validation errors)
	 * @return false if there were any validation errors
	 * @throws FDResourceException
	 */
	public boolean validateAddressWithoutScrub(ActionResult actionResult) throws FDResourceException {
		
		// [1] normalize (scrub) address
		if(!isAddressScrubbed(address)){
			scrubbedAddress = doScrubAddress(address, actionResult);
			LOGGER.debug("scrubbedAddress after scrub:"+scrubbedAddress);
		}else{
			scrubbedAddress = address;
		}
		if (actionResult.isFailure()){
			return false;
		}

		// Rule: restrict corporate addresses from registering for home delivery
		if ( EnumAddressType.FIRM.equals( scrubbedAddress.getAddressType() ) && !EnumServiceType.CORPORATE.equals( address.getServiceType() ) ) {
			giveCommercialAddressError(actionResult);
			return false;
		}

		scrubbedAddress.setServiceType(address.getServiceType());
		String geocodeResult = "";
		try {
			// [2] Check services for scrubbed address

			serviceResult = doCheckAddress(scrubbedAddress);
			if ( !isAddressDeliverable() ) {
				// post validations
				if ( !EnumServiceType.HOME.equals( address.getServiceType() ) || serviceResult.getServiceStatus( EnumServiceType.PICKUP ).equals( EnumDeliveryStatus.DONOT_DELIVER ) ) {

					if ( EnumServiceType.CORPORATE.equals( address.getServiceType() ) && !serviceResult.getServiceStatus( EnumServiceType.HOME ).equals( EnumDeliveryStatus.DONOT_DELIVER ) ) {
						actionResult.addError( true, EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), SystemMessageList.MSG_HOME_NO_COS_DLV_ADDRESS );
					} else {
						actionResult.addError( true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS );
					}
					return false;
				}// NOT(address type == HOME AND service status == DELIVER)
				
				if(getEStoreId() != null && EnumServiceType.FDX.equals(EnumServiceType.getEnum(getEStoreId())) && 
						serviceResult.getServiceStatus(EnumServiceType.FDX).equals(EnumDeliveryStatus.DONOT_DELIVER)) {
					actionResult.addError( true, EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS );
					return false;
				}
			}
			
			// [3] since address looks alright need geocode
			FDDeliveryAddressGeocodeResponse geocodeResponse = doGeocodeAddress(scrubbedAddress);		
		    geocodeResult = geocodeResponse.getResult();
		    
			if ( !"GEOCODE_OK".equalsIgnoreCase( geocodeResult ) ) {
				//
				// since geocoding is not happening silently ignore it
				LOGGER.warn( "GEOCODE FAILED FOR ADDRESS :" + scrubbedAddress );
				// actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(),
				// SystemMessageList.MSG_INVALID_ADDRESS);

			} else {
				LOGGER.debug( "geocodeResponse.getAddress() :" + geocodeResponse.getAddress() );
				scrubbedAddress = geocodeResponse.getAddress();
			}			
			
		} catch (FDInvalidAddressException iae) {

			//
			// Other comment came from RegistrationAction:
			// <i>geocoding failed. if user is pickup or depot, we don't care</i>
			/*
			 * batchley 20110203 -
			 * except, if it's pickup only AND geocode fails, we need an error here.
			 * otherwise, we end up with the MSG_TECHNICAL_ERROR msg, which is less than helpful
			 */
			if (this.strictCheck) {
				//check for just a missing Address1
				if ((this.scrubbedAddress.getAddress1()).trim().equals("")) {
					//return just that error
					actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
				}else{
					//otherwise, possible bad geocode. return new geocode msg
					actionResult.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), 
						MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE_EXTRA, new Object[] {SystemMessageList.CUSTOMER_SERVICE_CONTACT}));
				}
				return false;
			}else if (!"GEOCODE_OK".equalsIgnoreCase( geocodeResult ) && serviceResult == null) {
				actionResult.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), 
					MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE, new Object[] {SystemMessageList.CUSTOMER_SERVICE_CONTACT}));
				return false;
			}
		}
		
		return true;
	}	
 
	/**
	 * @param address
	 * @return
	 */
	private boolean isAddressScrubbed(AddressModel address){
		if(address != null){
			if(address.getId() != null && !"".equalsIgnoreCase(address.getId())){
				AddressInfo addressInfo = address.getAddressInfo();
				if( addressInfo !=null && addressInfo.getScrubbedStreet() != null && !"".equalsIgnoreCase(addressInfo.getScrubbedStreet())){
					return true;
				}else if(address.getScrubbedStreet() !=null &&  !"".equalsIgnoreCase(address.getScrubbedStreet()) ){
					return true;
				}
			}
		}
		return false;
	}
	
	
	
}
