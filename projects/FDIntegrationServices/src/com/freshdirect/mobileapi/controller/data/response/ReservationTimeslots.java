package com.freshdirect.mobileapi.controller.data.response;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.AccountController;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.model.SessionUser;

public class ReservationTimeslots extends Message {

    private static Category LOGGER = LoggerFactory.getInstance(ReservationTimeslots.class);

    private boolean eligibleForPreReservation;
    
    public boolean isEligibleForPreReservation() {
        return eligibleForPreReservation;
    }

    private DeliveryAddresses deliveryAddresses;

    private DeliveryTimeslots deliveryTimeslots;


    public DeliveryAddresses getDeliveryAddresses() {
        return deliveryAddresses;
    }

    public ReservationTimeslots(DeliveryAddresses deliveryAddresses, DeliveryTimeslots deliveryTimeslots, SessionUser user) {

        //Pass down notices messages
    	if(deliveryAddresses!=null)
    		addNoticeMessages(deliveryAddresses.getNotice());
    	if(deliveryTimeslots!=null)
    		addNoticeMessages(deliveryTimeslots.getNotice());

        //Pass down warning messages
        if(deliveryAddresses!=null)
        	addWarningMessages(deliveryAddresses.getWarnings());
        if(deliveryTimeslots!=null)
        	addWarningMessages(deliveryTimeslots.getWarnings());

        //Pass down error messages
        if(deliveryAddresses!=null)
        	addErrorMessages(deliveryAddresses.getErrors());
        if(deliveryTimeslots!=null)
        	addErrorMessages(deliveryTimeslots.getErrors());

        setDeliveryAddresses(deliveryAddresses);
        setDeliveryTimeslots(deliveryTimeslots);
        
        try {
            this.eligibleForPreReservation = user.isEligibleForPreReservation();
        } catch (FDResourceException e) {
            LOGGER.warn("FDResourceException encountered while trying to read isEligibleForPreReservation flag.",e);
        }
    }

    public void setDeliveryAddresses(DeliveryAddresses deliveryAddresses) {
    	if(deliveryAddresses!=null){
    		this.deliveryAddresses = deliveryAddresses;
    		this.deliveryAddresses.disableMessageMetaData();
    		this.deliveryAddresses.setCheckoutHeader(null);
    	}
    }

    public DeliveryTimeslots getDeliveryTimeslots() {
        return deliveryTimeslots;
    }

    public void setDeliveryTimeslots(DeliveryTimeslots deliveryTimeslots) {
        this.deliveryTimeslots = deliveryTimeslots;
        this.deliveryTimeslots.disableMessageMetaData();
        this.deliveryTimeslots.setCheckoutHeader(null);
    }

}
