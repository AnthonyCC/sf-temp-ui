package com.freshdirect.webapp.taglib.fdstore;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDuplicateAddressException;
import com.freshdirect.fdlogistics.model.FDDeliveryAddressVerificationResponse;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;
import com.freshdirect.logistics.delivery.model.EnumAddressVerificationResult;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;
import com.freshdirect.logistics.delivery.model.EnumZipCheckResponses;

public class AddressUtil {
    private static Category LOGGER = LoggerFactory.getInstance( AddressUtil.class );

	private AddressUtil() {
	}

	public static boolean updateShipToAddress(HttpServletRequest request, ActionResult result, FDUserI user, String shipToAddressId, ErpAddressModel address) throws FDResourceException {
		return updateShipToAddress(request.getSession(), result, user, shipToAddressId, address);
	}

	public static boolean updateShipToAddress(HttpSession session, ActionResult result, FDUserI user, String shipToAddressId, ErpAddressModel address) throws FDResourceException {
		boolean foundFraud = false;
        ErpAddressModel shpAddrToModify = FDCustomerManager.getAddress(user.getIdentity(), shipToAddressId);
        if (shpAddrToModify==null) {
            throw new FDResourceException("Unable to locate specified address for this user.");
        }

		// overwrite fields with new address
        shpAddrToModify.setFrom(address);
                
        try {
            
			foundFraud = FDCustomerManager.updateShipToAddress(AccountActivityUtil.getActionInfo(session), !user.isDepotUser(), shpAddrToModify);
            user.getShoppingCart().setDeliveryAddress(shpAddrToModify);
            user.setAddress(shpAddrToModify);
            
//		      zero lat and long to clear cache and force geocode when order is placed (see dlvManagerDAO.getZoneCode())
	        AddressInfo info = address.getAddressInfo();	        
	        if(info != null){
	        	info.setLatitude(0.0);
	        	info.setLongitude(0.0);
	        }

			if (foundFraud) {
				user.updateUserState();
			}
			
        } catch (ErpDuplicateAddressException ex){
            LOGGER.error("AddressUtil:updateShipToAddress(): ErpDuplicateAddressException caught while trying to update a shipping address to the customer info:", ex);
            result.addError(new ActionError("duplicate_user_address", "The information entered for this address matches an existing address in your account."));
        }
        return foundFraud;
    }

	public static void deleteShipToAddress(FDIdentity identity, String shipToAddressId, ActionResult result, HttpServletRequest request) throws FDResourceException {
		deleteShipToAddress(identity, shipToAddressId, result, request.getSession());
	}

	public static void deleteShipToAddress(FDIdentity identity, String shipToAddressId, ActionResult result, HttpSession session) throws FDResourceException {
        ErpAddressModel shpAddrToRemove = FDCustomerManager.getAddress(identity, shipToAddressId);
        if (shpAddrToRemove==null) {
            throw new FDResourceException("Address not found");
        }
        
		FDCustomerManager.removeShipToAddress(AccountActivityUtil.getActionInfo(session), shpAddrToRemove);
    }
    
    public static AddressModel scrubAddress(AddressModel address, ActionResult result) throws FDResourceException {
        return scrubAddress(address, true, result);
    }

	public static AddressModel scrubAddress(AddressModel address, boolean useApartment ,ActionResult result) throws FDResourceException {
        FDDeliveryAddressVerificationResponse response = null;
		try {
		
		/**
		 * This calls verifyAddress service
		 * This verifyAddress does all the below things: 
			 1) Scrub Address
			 2) Check Address
			 3) Geocode result
			 4) Delivery services
		 * But here we want only scrub address
		 * In addition if the address is alread scrubbed we don not need to do anything here.
		 *
		 */
		response = FDDeliveryManager.getInstance().scrubAddress(address, useApartment);
				
		String apartment = address.getApartment();
		
        LOGGER.debug("Scrubbing response: "+response.getVerifyResult());
        if (!EnumAddressVerificationResult.ADDRESS_OK.equals(response.getVerifyResult())) {

            result.addError(EnumAddressVerificationResult.NOT_VERIFIED.equals(response.getVerifyResult()),
				EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS_SS);

            result.addError(EnumAddressVerificationResult.ADDRESS_BAD.equals(response.getVerifyResult()),
				EnumUserInfoName.DLV_ADDRESS_1.getCode() , SystemMessageList.MSG_INVALID_ADDRESS);
            
            result.addError(EnumAddressVerificationResult.STREET_WRONG .equals(response.getVerifyResult()),
				EnumUserInfoName.DLV_ADDRESS_1.getCode() , SystemMessageList.MSG_UNRECOGNIZE_ADDRESS);
            
            result.addError(EnumAddressVerificationResult.BUILDING_WRONG.equals(response.getVerifyResult()),
				EnumUserInfoName.DLV_ADDRESS_1.getCode() , SystemMessageList.MSG_UNRECOGNIZE_STREET_NUMBER);                                    

            if(useApartment){
	            result.addError(EnumAddressVerificationResult.APT_WRONG.equals(response.getVerifyResult()),
					EnumUserInfoName.DLV_APARTMENT.getCode() , 
					((apartment == null) || (apartment.length() < 1)) ? SystemMessageList.MSG_APARTMENT_REQUIRED : SystemMessageList.MSG_UNRECOGNIZE_APARTMENT_NUMBER
				
	            );
	            
	            result.addError(EnumAddressVerificationResult.APT_MISSING.equals(response.getVerifyResult()),
						EnumUserInfoName.DLV_APARTMENT.getCode() , SystemMessageList.MSG_ADDRESS_APT_REQ);
            }
            result.addError(EnumAddressVerificationResult.ADDRESS_NOT_UNIQUE.equals(response.getVerifyResult()),
            EnumUserInfoName.DLV_ADDRESS_SUGGEST.getCode(), SystemMessageList.MSG_UNRECOGNIZE_ADDRESS_POSSIBLE_MATCHES);

            //
            // return original broken address is there was an error
            //
            return address;
        }
        //
        // all's well, return fixed/cleaned corrected address
        //
        return response.getAddress();
       } catch (FDInvalidAddressException e) {
		LOGGER.info("FDInvalidAddressException thrown during address scrub");
       }
	
		return address;
			
    }

	public static FDDeliveryZoneInfo getZoneInfo(FDUserI user, AddressModel address, ActionResult result, Date date, CustomerAvgOrderSize iPackagingModel, EnumRegionServiceType serviceType) throws FDResourceException {

        try {
            //
            // need to look and see if delivery is available within the next seven days
            //
        	FDDeliveryZoneInfo zoneInfo =  FDDeliveryManager.getInstance().getZoneInfo(address, date, iPackagingModel, serviceType, (user!=null && user.getIdentity()!=null)?user.getIdentity().getErpCustomerPK():null);
        	LOGGER.debug("getZoneInfo[EnumZipCheckResponses] :"+EnumZipCheckResponses.DELIVER.equals(zoneInfo.getResponse()));
			result.addError((!EnumZipCheckResponses.DELIVER.equals(zoneInfo.getResponse())), EnumUserInfoName.DLV_NOT_IN_ZONE.getCode(), SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS);
            return zoneInfo;
        } catch (FDInvalidAddressException  fdia) {
            LOGGER.info("getZoneInfo Invalid address", fdia);
			result.addError(new ActionError(EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE,
					new Object[] { UserUtil.getCustomerServiceContact(user) })));
            
            return new FDDeliveryZoneInfo(null, null, null, EnumZipCheckResponses.DONOT_DELIVER);
        }
    }
    
	public static FDDeliveryZoneInfo getZoneInfo(HttpServletRequest request, AddressModel address, ActionResult result, Date date, CustomerAvgOrderSize iPackagingModel, EnumRegionServiceType serviceType)
			throws FDResourceException {
		FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);
		return getZoneInfo(user, address, result, date, iPackagingModel, serviceType);
	}

	public static EnumServiceType getDeliveryServiceType(AddressModel addressModel) throws FDResourceException {
		try {
			FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByAddress(addressModel);
			
			EnumDeliveryStatus dlvStatus = serviceResult.getServiceStatus(addressModel.getServiceType());
			
			if (EnumDeliveryStatus.DELIVER.equals(dlvStatus)||EnumDeliveryStatus.COS_ENABLED.equals(dlvStatus)) {
				return addressModel.getServiceType();
			} else { 
				return EnumServiceType.PICKUP;
			}
		}catch (FDInvalidAddressException  fdia) {
	            LOGGER.info("Invalid address", fdia);
	    }catch (FDResourceException  fde) {
	            LOGGER.info("Unexpected exception happened while getting delivery service type", fde);
	            throw fde;
	    }
	    return null;
    }
	
	public static boolean validateState(String state) {
		return stateAbbrevs.contains(state.toUpperCase());
	}

	
	private final static Set<String> stateAbbrevs = new HashSet<String>();
	static {
		stateAbbrevs.add("AL");
		stateAbbrevs.add("AK");
		stateAbbrevs.add("AS");
		stateAbbrevs.add("AZ");
		stateAbbrevs.add("AR");
		stateAbbrevs.add("CA");
		stateAbbrevs.add("CO");
		stateAbbrevs.add("CT");
		stateAbbrevs.add("DE");
		stateAbbrevs.add("DC");
		stateAbbrevs.add("FM");
		stateAbbrevs.add("FL");
		stateAbbrevs.add("GA");
		stateAbbrevs.add("HI");
		stateAbbrevs.add("ID");
		stateAbbrevs.add("IL");
		stateAbbrevs.add("IN");
		stateAbbrevs.add("IA");
		stateAbbrevs.add("KS");
		stateAbbrevs.add("KY");
		stateAbbrevs.add("LA");
		stateAbbrevs.add("ME");
		stateAbbrevs.add("MH");
		stateAbbrevs.add("MD");
		stateAbbrevs.add("MA");
		stateAbbrevs.add("MI");
		stateAbbrevs.add("MN");
		stateAbbrevs.add("MS");
		stateAbbrevs.add("MO");
		stateAbbrevs.add("MT");
		stateAbbrevs.add("NE");
		stateAbbrevs.add("NV");
		stateAbbrevs.add("NH");
		stateAbbrevs.add("NJ");
		stateAbbrevs.add("NM");
		stateAbbrevs.add("NY");
		stateAbbrevs.add("NC");
		stateAbbrevs.add("ND");
		stateAbbrevs.add("MP");
		stateAbbrevs.add("OH");
		stateAbbrevs.add("OK");
		stateAbbrevs.add("OR");
		stateAbbrevs.add("PW");
		stateAbbrevs.add("PA");
		stateAbbrevs.add("PR");
		stateAbbrevs.add("RI");
		stateAbbrevs.add("SC");
		stateAbbrevs.add("SD");
		stateAbbrevs.add("TN");
		stateAbbrevs.add("TX");
		stateAbbrevs.add("UT");
		stateAbbrevs.add("VT");
		stateAbbrevs.add("VI");
		stateAbbrevs.add("VA");
		stateAbbrevs.add("WA");
		stateAbbrevs.add("WV");
		stateAbbrevs.add("WI");
		stateAbbrevs.add("WY");
	}
}
