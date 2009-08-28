package com.freshdirect.delivery.routing.ejb;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.fdstore.FDReservation;

public class ReserveTimeslotCommand implements java.io.Serializable {
	private FDReservation reservation;
	private ContactAddressModel address;
	
	public FDReservation getReservation() {
		return reservation;
	}
	public void setReservation(FDReservation reservation) {
		this.reservation = reservation;
	}
	public ContactAddressModel getAddress() {
		return address;
	}
	public void setAddress(ContactAddressModel address) {
		this.address = address;
	}
}
