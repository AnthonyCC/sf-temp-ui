package com.freshdirect.mktAdmin.model;

import java.util.Date;

import com.freshdirect.common.address.AddressModel;

public class CustomerAddressModel extends AddressModel {

	private String customerId=null;
	private Date dateCreated=null;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
