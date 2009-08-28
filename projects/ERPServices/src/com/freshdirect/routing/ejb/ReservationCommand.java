package com.freshdirect.routing.ejb;

import com.freshdirect.common.address.ContactAddressModel;

public class ReservationCommand implements java.io.Serializable {
	
	
	private Object reservation;
	
	private ContactAddressModel address;
	
	public Object getReservation() {
		return reservation;
	}
	public void setReservation(Object reservation) {
		this.reservation = reservation;
	}
	public ContactAddressModel getAddress() {
		return address;
	}
	public void setAddress(ContactAddressModel address) {
		this.address = address;
	}
}
