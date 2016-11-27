package com.freshdirect.mobileapi.model;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDDeliveryTimeslotModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.model.tagwrapper.DeliveryTimeSlotTagWrapper;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.Result;

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

    public TimeSlotCalculationResult getDeliveryTimeslot(SessionUser user, boolean preReservationMode) throws FDException {
        DeliveryTimeSlotTagWrapper wrapper = new DeliveryTimeSlotTagWrapper(user);
        Result result = wrapper.getDeliveryTimeSlotResult(this.address);
        final FDDeliveryTimeslotModel model = result.getDeliveryTimeslotModel();
        TimeSlotCalculationResult timeSlotCalculationResult = new TimeSlotCalculationResult(model, user.isChefsTable(), preReservationMode, user.isDpNewTcBlocking());
        if(user.getFDSessionUser() != null && user.getFDSessionUser().getIdentity() != null) {
        	user.setReservationAndPreselectedTimeslotIds(model.getTimeslotList(), timeSlotCalculationResult, this.address);
        }
        
        return timeSlotCalculationResult;
    }
    
    public TimeSlotCalculationResult getDeliveryTimeslot(SessionUser user, boolean preReservationMode, boolean isAuthenticated) throws FDException {
        DeliveryTimeSlotTagWrapper wrapper = new DeliveryTimeSlotTagWrapper(user);
        Result result = wrapper.getDeliveryTimeSlotResult(this.address,isAuthenticated);
        final FDDeliveryTimeslotModel model = result.getDeliveryTimeslotModel();
        TimeSlotCalculationResult timeSlotCalculationResult = new TimeSlotCalculationResult(model, user.isChefsTable(), preReservationMode, user.isDpNewTcBlocking());
        if(user.getFDSessionUser() != null && user.getFDSessionUser().getIdentity() != null) {
        	user.setReservationAndPreselectedTimeslotIds(model.getTimeslotList(), timeSlotCalculationResult, this.address);
        }
        
        return timeSlotCalculationResult;
    }

}
