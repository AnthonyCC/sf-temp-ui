package com.freshdirect.delivery;

import java.io.Serializable;

import com.freshdirect.common.address.AddressModel;

public class DlvAddressGeocodeResponse implements Serializable {
	
	private final AddressModel address;
	private final String result;
	
	public DlvAddressGeocodeResponse(AddressModel address, String result) {
		this.address = address;
		this.result = result;
	}
	
	public AddressModel getAddress(){
		return this.address;
	}
	
	public String getResult() {
		return this.result;
	}
	
	public String toString() {
		return "DlvAddressGeocodeResponse[address " + address
			+ ", result "
			+ result
			+ "]";
	}
}
