package com.freshdirect.mobileapi.model;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.model.tagwrapper.DeliveryTimeSlotTagWrapper;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.Result;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class DeliveryAddress {

    public static DeliveryAddress wrap(ErpAddressModel address) {
        DeliveryAddress newInstance = new DeliveryAddress();
        newInstance.address = address;
        return newInstance;
    }

    protected ErpAddressModel address;

    private static Category LOGGER = LoggerFactory.getInstance(DeliveryAddress.class);

    public enum DeliveryAddressType {
        RESIDENTIAL, CORP, PICKUP, DEPOT
    };

    public enum AltAddressType {
        NONE("NONE"), NEIGHBOR("NEIGHBOR"), DOORMAN("DOORMAN");
        private String code;

        AltAddressType(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    };

    /**
     * DUP: duplicated code from 
     * Class - com.freshdirect.webapp.taglib.fdstore.AddressUtil
     * Method - getZoneInfo
     * Comment - refactored to remove dependencies on HttpServletRequest

     * @param request
     * @param address
     * @param result
     * @param date
     * @return
     * @throws FDResourceException
     */
    protected DlvZoneInfoModel getZoneInfo(AddressModel address, FDSessionUser user, ActionResult result, Date date)
            throws FDResourceException {
        try {
            DlvZoneInfoModel zoneInfo = FDDeliveryManager.getInstance().getZoneInfo(address, date);
            result.addError((!EnumZipCheckResponses.DELIVER.equals(zoneInfo.getResponse())), EnumUserInfoName.DLV_NOT_IN_ZONE.getCode(),
                    SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS);
            return zoneInfo;
        } catch (FDInvalidAddressException fdia) {
            LOGGER.info("Invalid address", fdia);
            result.addError(new ActionError(EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), MessageFormat.format(
                    SystemMessageList.MSG_CANT_GEOCODE, new Object[] { user.getCustomerServiceContact() })));

            return new DlvZoneInfoModel(null, null, null, EnumZipCheckResponses.DONOT_DELIVER, false,false);
        }
    }

    /**
     * DUP: duplicated code from 
     * Class - com.freshdirect.webapp.taglib.fdstore.AddressUtil
     * Method - scrubAddress
     * Comment - refactored to remove dependencies on HttpServletRequest
     * 
     * @param address
     * @param result
     * @return
     * @throws FDResourceException
     */
    public static AddressModel scrubAddress(AddressModel address, ActionResult result) throws FDResourceException {
        return DeliveryAddress.scrubAddress(address, true, result);
    }

    /**
     * @see com.freshdirect.webapp.taglib.fdstore.AddressUtil
     * 
     * @param address
     * @param useApartment
     * @param result
     * @return
     * @throws FDResourceException
     */
    public static AddressModel scrubAddress(AddressModel address, boolean useApartment, ActionResult result) throws FDResourceException {
        return AddressUtil.scrubAddress(address, result);
    }

    protected static String getCounty(AddressModel address) throws FDResourceException {
        return FDDeliveryManager.getInstance().getCounty(address);
    }

    /*
     * ============================================
     * Friendly
     * ============================================
     */
    /**
     * Helper method for performing geocoding of address.
     * 
     * @param address
     * @return geocoded location. if geocoding fails, original address object is returned
     * @throws FDResourceException
     * @throws FDInvalidAddressException
     */
    static AddressModel getAddressGeocode(AddressModel address) throws FDResourceException, FDInvalidAddressException {
        DlvAddressGeocodeResponse geocodeResponse = FDDeliveryManager.getInstance().geocodeAddress(address);
        String geocodeResult = geocodeResponse.getResult();
        if (!"GEOCODE_OK".equalsIgnoreCase(geocodeResult)) {
            // since geocoding is not happening silently ignore it  
            LOGGER.warn("GEOCODE FAILED FOR ADDRESS :" + address);
            //actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
        } else {
            LOGGER.debug("setRegularDeliveryAddress : geocodeResponse.getAddress() :" + geocodeResponse.getAddress());
            address = geocodeResponse.getAddress();
        }
        return address;
    }

    public TimeSlotCalculationResult getDeliveryTimeslot(SessionUser user, boolean preReservationMode) throws FDException {
        DeliveryTimeSlotTagWrapper wrapper = new DeliveryTimeSlotTagWrapper(user);
        Result result = wrapper.getDeliveryTimeSlotResult(this.address);
        TimeSlotCalculationResult timeSlotCalculationResult = new TimeSlotCalculationResult(result, user.isChefsTable(), preReservationMode);
        user.setReservationAndPreselectedTimeslotIds(result.getTimeslots(), timeSlotCalculationResult);
        
        return timeSlotCalculationResult;
    }
    
    public TimeSlotCalculationResult getDeliveryTimeslot(SessionUser user, boolean preReservationMode, boolean isAuthenticated) throws FDException {
        DeliveryTimeSlotTagWrapper wrapper = new DeliveryTimeSlotTagWrapper(user);
        Result result = wrapper.getDeliveryTimeSlotResult(this.address,isAuthenticated);
        TimeSlotCalculationResult timeSlotCalculationResult = new TimeSlotCalculationResult(result, user.isChefsTable(), preReservationMode);
        user.setReservationAndPreselectedTimeslotIds(result.getTimeslots(), timeSlotCalculationResult);
        
        return timeSlotCalculationResult;
    }

}
