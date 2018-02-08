package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.mobileapi.model.TimeslotList;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;

public class DeliveryTimeslots extends CheckoutResponse {

    private Map<String, String> reservationTypes;

    public Map<String, String> getReservationTypes() {
        return reservationTypes;
    }

    public void setReservationTypes(Map<String, String> reservationTypes) {
        this.reservationTypes = reservationTypes;
    }

    public String getSelectedTimeslotId() {
        return selectedTimeslotId;
    }

    public void setSelectedTimeslotId(String selectedTimeslotId) {
        this.selectedTimeslotId = selectedTimeslotId;
    }

    private String selectedTimeslotId;

    private String reservedTimeslotId;
    
    private boolean showPremiumSlots;
    
    private boolean showDPTermsAndConditions;

    private Date sameDayCutoff;
    
    private boolean minOrderReqd;
    
    private ShipToAddress address;
    

    public String getReservedTimeslotId() {
        return reservedTimeslotId;
    }

    public void setReservedTimeslotId(String reservedTimeslotId) {
        this.reservedTimeslotId = reservedTimeslotId;
    }

    private List<Timeslot> timeSlots = new ArrayList<Timeslot>();

    private List<Restriction> restrictions = new ArrayList<Restriction>();

	private boolean showMinNotMetMessage;

    public List<Timeslot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<Timeslot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public List<Restriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<Restriction> restrictions) {
        this.restrictions = restrictions;
    }

    /**
     * @param result
     */
    public DeliveryTimeslots(TimeSlotCalculationResult result) {
        List<TimeslotList> slotLists = result.getTimeslotList();
        for (TimeslotList slotList : slotLists) {
            timeSlots.addAll(Timeslot.initWithList(slotList.getTimeslots(result.isUserChefTable())));
        }
        List<String> restrictionMessages = result.getMessages();
        if(restrictionMessages != null) {
	        for (String restrictionMessage : restrictionMessages) {
	            this.restrictions.add(new Restriction(restrictionMessage));
	        }
        }
        this.selectedTimeslotId = result.getPreselectedTimeslotId();
        this.reservedTimeslotId = result.getReservationTimeslotId();
        this.showPremiumSlots = result.isShowPremiumSlots();
        this.showDPTermsAndConditions = result.isShowDPTermsAndConditions();
        this.sameDayCutoff = result.getSameDayCutoff();
        this.minOrderReqd = result.isMinOrderReqd();
        this.showMinNotMetMessage = result.isShowMinNotMetMessage();
        
    }

	public boolean isShowPremiumSlots() {
		return showPremiumSlots;
	}

	public void setShowPremiumSlots(boolean showPremiumSlots) {
		this.showPremiumSlots = showPremiumSlots;
	}

	public Date getSameDayCutoff() {
		return sameDayCutoff;
	}

	public void setSameDayCutoff(Date sameDayCutoff) {
		this.sameDayCutoff = sameDayCutoff;
	}

	public boolean isShowDPTermsAndConditions() {
		return showDPTermsAndConditions;
	}

	public void setShowDPTermsAndConditions(boolean showDPTermsAndConditions) {
		this.showDPTermsAndConditions = showDPTermsAndConditions;
	}

	public boolean isMinOrderReqd() {
		return minOrderReqd;
	}
	
	public boolean isShowMinNotMetMessage() {
		return showMinNotMetMessage;
	}

	
    public ShipToAddress getAddress() {
        return address;
    }
    
    public void setAddress(ShipToAddress address) {
        this.address = address;
    }
	
}
