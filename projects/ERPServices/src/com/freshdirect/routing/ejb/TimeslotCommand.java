package com.freshdirect.routing.ejb;

import java.util.Date;
import java.util.List;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.common.address.ContactAddressModel;

public class TimeslotCommand implements java.io.Serializable {
	
	private List timeSlots;
	
	private ContactAddressModel address;

	public List getTimeSlots() {
		return timeSlots;
	}

	public void setTimeSlots(List timeSlots) {
		this.timeSlots = timeSlots;
	}

	public ContactAddressModel getAddress() {
		return address;
	}

	public void setAddress(ContactAddressModel address) {
		this.address = address;
	}
	

}
