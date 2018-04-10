package com.freshdirect.webapp.ajax.expresscheckout.timeslot.data;

import java.util.ArrayList;
import java.util.List;

public class FormTimeslotsData {

    private String selectedTimeslotId;
    private String reservedTimeslotId;
    private String zonePromoAmount;
    private List<FormTimeslotData> timeSlots = new ArrayList<FormTimeslotData>();
    private List<String> warningMessages = new ArrayList<String>(); /* may need re-factoring with holiday msgs */

    public String getSelectedTimeslotId() {
        return selectedTimeslotId;
    }

    public void setSelectedTimeslotId(String selectedTimeslotId) {
        this.selectedTimeslotId = selectedTimeslotId;
    }

    public String getReservedTimeslotId() {
        return reservedTimeslotId;
    }

    public void setReservedTimeslotId(String reservedTimeslotId) {
        this.reservedTimeslotId = reservedTimeslotId;
    }

    public List<FormTimeslotData> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<FormTimeslotData> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public void addTimeSlots(List<FormTimeslotData> timeslots) {
        this.timeSlots.addAll(timeslots);
    }

    public String getZonePromoAmount() {
        return zonePromoAmount;
    }

    public void setZonePromoAmount(String zonePromoAmount) {
        this.zonePromoAmount = zonePromoAmount;
    }

    public List<String> getWarningMessages() {
		return warningMessages;
	}

	public void setWarningMessages(List<String> warningMessages) {
		this.warningMessages = warningMessages;
	}
	
	public void addWarningMessage(String warningMessage) {
		this.getWarningMessages().add(warningMessage);
	}
}
