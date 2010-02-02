package com.freshdirect.delivery.routing.ejb;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.model.DlvReservationModel;

public class ConfirmTimeslotCommand implements java.io.Serializable {
	
	private DlvReservationModel reservation;
	private ContactAddressModel address;
	private String erpOrderId;
	private boolean isUpdateOnly;
	
	
	public boolean isUpdateOnly() {
		return isUpdateOnly;
	}

	public void setUpdateOnly(boolean isUpdateOnly) {
		this.isUpdateOnly = isUpdateOnly;
	}

	
	public String getErpOrderId() {
		return erpOrderId;
	}

	public void setErpOrderId(String erpOrderId) {
		this.erpOrderId = erpOrderId;
	}

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
