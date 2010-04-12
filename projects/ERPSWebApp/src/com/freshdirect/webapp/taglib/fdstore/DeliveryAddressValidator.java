package com.freshdirect.webapp.taglib.fdstore;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.DlvZipInfoModel;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;



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
	private DlvServiceSelectionResult serviceResult;


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
		if (EnumAddressType.FIRM.equals(scrubbedAddress.getAddressType()) &&
				!EnumServiceType.CORPORATE.equals(address.getServiceType())){
			actionResult.addError(new ActionError(EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), SystemMessageList.MSG_COMMERCIAL_ADDRESS));
			return false;
		}

		scrubbedAddress.setServiceType(address.getServiceType());

		try {
			// [2] Check services for scrubbed address

			serviceResult = doCheckAddress(scrubbedAddress);
			if (!isAddressDeliverable()) {
				// post validations
				if (!EnumServiceType.HOME.equals(address.getServiceType())
						|| serviceResult.getServiceStatus(
								EnumServiceType.PICKUP).equals(
								EnumDeliveryStatus.DONOT_DELIVER)) {

					if (EnumServiceType.CORPORATE.equals(address.getServiceType()) && !serviceResult.getServiceStatus(
								EnumServiceType.HOME).equals(
								EnumDeliveryStatus.DONOT_DELIVER)) {
						actionResult.addError(true, EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), SystemMessageList.MSG_HOME_NO_COS_DLV_ADDRESS);
					} else {
						actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(),
								SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS);
					}
					return false;
				}// NOT(address type == HOME AND service status == DELIVER)
			}
			
			// [3] since address looks alright need geocode
			DlvAddressGeocodeResponse geocodeResponse = doGeocodeAddress(scrubbedAddress);		
		    String geocodeResult = geocodeResponse.getResult();
		    
		    if (!"GEOCODE_OK".equalsIgnoreCase(geocodeResult)) {
		    	//

				// since geocoding is not happening silently ignore it  
		    	LOGGER.warn("GEOCODE FAILED FOR ADDRESS :"+scrubbedAddress);		    	
				//actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
		
		    } else {
		    	LOGGER.debug("geocodeResponse.getAddress() :"+geocodeResponse.getAddress());			    	
		    	scrubbedAddress = geocodeResponse.getAddress();		    
		    }
			
			
		} catch (FDInvalidAddressException iae) {

			//
			// Other comment came from RegistrationAction:
			// <i>geocoding failed. if user is pickup or depot, we don't care</i>
			if (this.strictCheck) {
				actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
				return false;
			}
		}
		
		return true;
	}


	public AddressModel getScrubbedAddress() {
		return scrubbedAddress;
	}


	public DlvServiceSelectionResult getServiceResult() {
		return serviceResult;
	}

	public boolean isAddressDeliverable() throws FDResourceException {
		if (serviceResult == null) {
			return false;
		}
		EnumDeliveryStatus status = serviceResult.getServiceStatus(scrubbedAddress.getServiceType());
		return EnumDeliveryStatus.DELIVER.equals(status) || EnumDeliveryStatus.PARTIALLY_DELIVER.equals(status) || EnumDeliveryStatus.COS_ENABLED.equals(status);
	}
	
	
	protected AddressModel doScrubAddress(AddressModel addr, ActionResult result) throws FDResourceException {
		return AddressUtil.scrubAddress(addr, result);
	}

	protected DlvServiceSelectionResult doCheckAddress(AddressModel addr) throws FDResourceException, FDInvalidAddressException {
		return FDDeliveryManager.getInstance().checkAddress(addr);
	}

	protected DlvAddressGeocodeResponse doGeocodeAddress(AddressModel addr) throws FDResourceException, FDInvalidAddressException {
		 return FDDeliveryManager.getInstance().geocodeAddress(addr);		
	}
}
