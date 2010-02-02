package com.freshdirect.routing.ejb;

import com.freshdirect.common.address.ContactAddressModel;


public class ReservationUpdateCommand implements java.io.Serializable {
	
	
	private String reservationId;
	
	private String sapOrderNumber;
	
	private ContactAddressModel address;

	public ContactAddressModel getAddress() {
		return address;
	}

	public void setAddress(ContactAddressModel address) {
		this.address = address;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public String getSapOrderNumber() {
		return sapOrderNumber;
	}

	public void setSapOrderNumber(String sapOrderNumber) {
		this.sapOrderNumber = sapOrderNumber;
	}
	
	
}
