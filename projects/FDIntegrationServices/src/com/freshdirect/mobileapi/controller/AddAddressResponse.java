package com.freshdirect.mobileapi.controller;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.model.ShipToAddress;

public class AddAddressResponse extends Message {
	
	private ShipToAddress addedAddress;
	
	public ShipToAddress getAddedAddress() {
		return addedAddress;
	}
	
	public void setAddedAddress(ShipToAddress addedAddress) {
		this.addedAddress = addedAddress;
	}
	
}
