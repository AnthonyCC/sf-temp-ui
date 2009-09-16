package com.freshdirect.delivery.routing.ejb;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.fdstore.FDTimeslot;

public class ReserveTimeslotCommand implements java.io.Serializable {
	private DlvReservationModel reservation;
	private ContactAddressModel address;
	private FDTimeslot timeslot;
	
	public FDTimeslot getTimeslot() {
		return timeslot;
	}
	public void setTimeslot(FDTimeslot timeslot) {
		this.timeslot = timeslot;
	}
	public DlvReservationModel getReservation() {
		return reservation;
	}
	public void setReservation(DlvReservationModel reservation) {
		this.reservation = reservation;
	}
	
	public ContactAddressModel getAddress() {
		return address;
	}
	public void setAddress(ContactAddressModel address) {
		this.address = address;
	}
}
