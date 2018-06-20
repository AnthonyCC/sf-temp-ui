package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDDeliveryTimeslotModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FDTimeslotUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.model.DlvZoneModel;
import com.freshdirect.mobileapi.model.tagwrapper.CheckoutControllerTagWrapper;

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

    public ResultBundle reserveSlotEx(String deliveryTimeslotId) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(user);
        ResultBundle result = tagWrapper.reserveDeliveryTimeslot(deliveryTimeslotId);
        if(tagWrapper.getWrapTarget().getSuccessPage() != null && tagWrapper.getWrapTarget().getSuccessPage().contains("step_2_verify_age")){
        	result.setActionResult(new ActionResult());
        }
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

		private boolean showPremiumSlots= false;
		
		private boolean showDPTermsAndConditions = false;

		private Date sameDayCutoff;
		
		private boolean minOrderReqd;

		private boolean showMinNotMetMessage;

        public boolean isUserChefTable() {
            return isUserChefTable;
        }

        public Timeslot findTimeslotById(String timeslotId,  SessionUser user) {
            Timeslot timeslot = null;
            if (timeslotId != null) {
                for (TimeslotList timeslotList : this.timeslotList) {
                    timeslot = timeslotList.findTimeslotById(timeslotId, this.isUserChefTable, user);
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
        public TimeSlotCalculationResult(FDDeliveryTimeslotModel model, boolean isUserChefTable, boolean preReservationMode, boolean isDpNewTcBlocking) throws FDResourceException {
            this(model.getTimeslotList(), model.getZones(), model.isZoneCtActive(), model.getGeoRestrictionmessages(), isUserChefTable
            			, model.isShowPremiumSlots(), isDpNewTcBlocking, model.getSameDayCutoff(), model.isMinOrderReqd());
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
                List<String> messages, boolean isUserChefTable, boolean showPremiumSlots, boolean isDpNewTcBlocking, Date sameDayCutoff, boolean minOrderReqd) throws FDResourceException {
            this.timeslotList = TimeslotList.wrap(timeslotLists);
            //this.zones = zones;
            this.zoneCtActive = zoneCtActive;
            this.messages = messages;
            this.isUserChefTable = isUserChefTable;
            calculatSlotAvailability();
            this.showPremiumSlots = showPremiumSlots;
            this.showDPTermsAndConditions = showPremiumSlots && isDpNewTcBlocking;
            this.sameDayCutoff = sameDayCutoff;
            this.minOrderReqd = minOrderReqd;
            
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
        
		public Date getSameDayCutoff() {
			return sameDayCutoff;
		}

		public boolean isShowPremiumSlots() {
			return showPremiumSlots;
		}
		
		public boolean isShowDPTermsAndConditions() {
			return showDPTermsAndConditions;
		}
		public boolean isMinOrderReqd() {
			return minOrderReqd;
		}

		public void setShowMinNotMetMessage(boolean showMinNotMetMessage) {
			this.showMinNotMetMessage  = showMinNotMetMessage;
		}
		
		public boolean isShowMinNotMetMessage() {
			return showMinNotMetMessage;
		}
    }

}
