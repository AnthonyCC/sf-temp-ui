package com.freshdirect.delivery;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.ErpAddressModel;

public class DepotLocationModel {
	private String depot;
	private AddressModel address;
	public DepotLocationModel(String depot, AddressModel address) {
		this.depot = depot;
		this.address = address;
	}
	public String getDepot() {
		return depot;
	}
	public void setDepot(String depot) {
		this.depot = depot;
	}
	public AddressModel getAddress() {
		return address;
	}
	public void setAddress(AddressModel address) {
		this.address = address;
	}
}
