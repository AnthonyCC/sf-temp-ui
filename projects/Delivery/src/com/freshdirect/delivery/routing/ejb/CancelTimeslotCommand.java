package com.freshdirect.delivery.routing.ejb;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.model.DlvReservationModel;

public class CancelTimeslotCommand implements java.io.Serializable {
	
	private DlvReservationModel reservation;
	private ContactAddressModel address;
	public void setReservation(DlvReservationModel reservation) {
		this.reservation=reservation;
	}

	public DlvReservationModel getReservation() {
		return reservation;
	}
	public ContactAddressModel getAddress() {
		return address;
	}
	public void setAddress(ContactAddressModel address) {
		this.address = address;
	}
}