package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FDTimeslotUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.model.tagwrapper.CheckoutControllerTagWrapper;
import com.freshdirect.webapp.taglib.fdstore.Result;

/**
 * Rich domain object to encapsulate business logic for reservation / delivery timeslot.
 * 
 * @see com.freshdirect.webapp.taglib.fdstore.DeliveryTimeSlotTag
 * @see step_2_select.jsp
 * 
 * @author Rob
 *
 */
public class DeliveryTimeslots {

    private static Category LOGGER = LoggerFactory.getInstance(DeliveryTimeslots.class);

    private FDUserI user = null;

    /**
     * Used in regular timeslot page (checkout)
     * @param shipToAddress
     */
    public DeliveryTimeslots(SessionUser sessionUser) {
        this(sessionUser, false);
    }

    public ResultBundle reserveSlot(String deliveryTimeslotId) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(user);
        ResultBundle result = tagWrapper.reserveDeliveryTimeslot(deliveryTimeslotId);
        tagWrapper.setParams(result);
        return result;
    }

    /**
     * Used in reserve timeslot (non-checkout)
     * @param shipToAddress
     * @param deliveryInfo
     */
    public DeliveryTimeslots(SessionUser sessionUser, boolean deliveryInfo) {
        user = sessionUser.getFDSessionUser();
    }

    /**
     * @author Rob
     *
     */
    public static class TimeSlotCalculationResult {
        public boolean isZoneCtActive() {
            return zoneCtActive;
        }

        private boolean isKosherSlotAvailable = false;

        private boolean hasCapacity = true;

        private boolean isUserChefTable = false;

        public boolean isUserChefTable() {
            return isUserChefTable;
        }

        public Timeslot findTimeslotById(String timeslotId) {
            Timeslot timeslot = null;
            if (timeslotId != null) {
                for (TimeslotList timeslotList : this.timeslotList) {
                    timeslot = timeslotList.findTimeslotById(timeslotId, this.isUserChefTable);
                    if (timeslot != null) {
                        break;
                    }
                }
            }
            return timeslot;
        }

        /**
         * @param result
         * @param isUserChefTable
         * @throws FDResourceException
         */
        public TimeSlotCalculationResult(Result result, boolean isUserChefTable, boolean preReservationMode) throws FDResourceException {
            //(isUserChefTable && !preReservationMode) condition essentially makes the user non-chef user for during pre-reservation
        	//Reservation against CT capacity
            this(result.getTimeslots(), result.getZones(), result.isZoneCtActive(), result.getMessages(), isUserChefTable);
        }

        /**
         * @param timeslotLists
         * @param zones
         * @param zoneCtActive
         * @param messages
         * @param isUserChefTable
         * @throws FDResourceException
         */
        public TimeSlotCalculationResult(List<FDTimeslotUtil> timeslotLists, Map<String, DlvZoneModel> zones, boolean zoneCtActive,
                List<String> messages, boolean isUserChefTable) throws FDResourceException {
            this.timeslotList = TimeslotList.wrap(timeslotLists);
            //this.zones = zones;
            this.zoneCtActive = zoneCtActive;
            this.messages = messages;
            this.isUserChefTable = isUserChefTable;
            calculatSlotAvailability();
        }

        /**
         * @throws FDResourceException
         */
        private void calculatSlotAvailability() throws FDResourceException {
            DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
            for (TimeslotList lst : timeslotList) {
                isKosherSlotAvailable = isKosherSlotAvailable || lst.isKosherSlotAvailable(restrictions);
                hasCapacity = hasCapacity || lst.hasCapacity();
            }
        }

        public boolean isKosherSlotAvailable() {
            return isKosherSlotAvailable;
        }

        public boolean isHasCapacity() {
            return hasCapacity;
        }

        private List<TimeslotList> timeslotList = new ArrayList<TimeslotList>();

        private boolean zoneCtActive = false;

        private List<String> messages = null;

        public List<TimeslotList> getTimeslotList() {
            return timeslotList;
        }

        public List<String> getMessages() {
            return messages;
        }

        public void setMessages(List<String> messages) {
            this.messages = messages;
        }

        private String preselectedTimeslotId = null;

        private String reservationTimeslotId = null;

        public void setReservationTimeslotId(String reservationTimeslotId) {
            this.reservationTimeslotId = reservationTimeslotId;
        }

        public String getReservationTimeslotId() {
            return reservationTimeslotId;
        }

        public String getPreselectedTimeslotId() {
            return preselectedTimeslotId;
        }

        public void setPreselectedTimeslotId(String preselectedTimeslotId) {
            this.preselectedTimeslotId = preselectedTimeslotId;
        }

    }

}
