package com.freshdirect.delivery.routing.ejb;


import java.util.List;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.fdstore.FDTimeslot;

public class TimeslotCommand implements java.io.Serializable {
	
	private List<FDTimeslot> timeSlots;
	
	private ContactAddressModel address;

	public List<FDTimeslot> getTimeSlots() {
		return timeSlots;
	}

	public void setTimeSlots(List<FDTimeslot> timeSlots) {
		this.timeSlots = timeSlots;
	}

	public ContactAddressModel getAddress() {
		return address;
	}

	public void setAddress(ContactAddressModel address) {
		this.address = address;
	}
	

}