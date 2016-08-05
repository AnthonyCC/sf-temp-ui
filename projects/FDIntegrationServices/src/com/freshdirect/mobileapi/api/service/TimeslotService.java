package com.freshdirect.mobileapi.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots;
import com.freshdirect.mobileapi.controller.data.response.Timeslot;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.ShipToAddress;

@Component
public class TimeslotService {

    public DeliveryTimeslots getDeliveryTimeslots(SessionUser user, String addressId) throws FDException {
        addressId = ensureDeliveryAddressId(user, addressId);
        ShipToAddress shipToAddress = user.getDeliveryAddress(addressId);
        TimeSlotCalculationResult timeSlotResult = shipToAddress.getDeliveryTimeslot(user, true);
        DeliveryTimeslots deliveryTimeslots = new DeliveryTimeslots(timeSlotResult);
        updateEstDstTimeslot(deliveryTimeslots);
        return deliveryTimeslots;
    }

    private String ensureDeliveryAddressId(SessionUser user, String addressId) throws FDException {
        if (addressId == null) {
            addressId = getDeliveryAddressId(user);
            if (addressId == null){
                throw new FDException("You need to add a delivery address to perform this action.");
            }
        }
        return addressId;
    }

    private String getDeliveryAddressId(SessionUser user) throws FDException {
        String addressId = null;

        if ((user.getAddress() == null || (user.getAddress() != null && !user.getAddress().isCustomerAnonymousAddress()))) {
            addressId = user.getReservationAddressId();
        }

        if (addressId == null) {
            if (user.getAddress() != null && user.getAddress().getAddress1() != null && user.getAddress().getAddress1().trim().length() > 0
                    && user.getAddress().isCustomerAnonymousAddress()) {
                addressId = user.getAddress().getId();
            } else {
                if (user.getFDSessionUser() != null && user.getFDSessionUser().getIdentity() != null) {
                    if (user.getDefaultShipToAddress() != null) {
                        addressId = user.getDefaultShipToAddress();
                    } else {
                        if (user.getDeliveryAddresses().size() > 0) {// This can happen when user signed up through Iphone.
                            addressId = user.getDeliveryAddresses().get(0).getId();
                        }
                    }
                }
            }
        }
        return addressId;
    }

    private void updateEstDstTimeslot(DeliveryTimeslots deliveryTimeslots) {
        if (FDStoreProperties.isEdtEstTimeslotConversionEnabled()) {
            deliveryTimeslots.setTimeSlots(convertEstToDstTimeslots(deliveryTimeslots.getTimeSlots()));
        }
    }

    private Timeslot convertEstToDstTimeslot(Timeslot timeslot) {
        timeslot.setStart(DateUtil.addHours(timeslot.getStartDate(), 1));
        timeslot.setEnd(DateUtil.addHours(timeslot.getEndDate(), 1));
        timeslot.setCutoffDate(DateUtil.addHours(timeslot.getCutoffDateDate(), 1));
        return timeslot;
    }

    private List<Timeslot> convertEstToDstTimeslots(List<Timeslot> estTimeslots) {
        List<Timeslot> edtTimeslots = new ArrayList<Timeslot>();
        for (Timeslot timeslot : estTimeslots) {
            edtTimeslots.add(convertEstToDstTimeslot(timeslot));
        }
        return edtTimeslots;
    }

}
