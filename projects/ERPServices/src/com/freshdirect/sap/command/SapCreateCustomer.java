/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import com.freshdirect.common.address.BasicContactAddressI;
import com.freshdirect.sap.SapCustomerI;
import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.bapi.BapiCreateCustomer;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapCreateCustomer extends SapCommandSupport {

	private final String erpCustomerNumber;
	private final SapCustomerI customer;

	private String sapCustomerNumber;

	public SapCreateCustomer(String erpCustomerNumber, SapCustomerI customer) {
		this.erpCustomerNumber = erpCustomerNumber;
		this.customer = customer;
	}

	public String getErpCustomerNumber() {
		return this.erpCustomerNumber;
	}

	public String getSapCustomerNumber() {
		return this.sapCustomerNumber;
	}

	public void execute() throws SapException {

		BapiCreateCustomer bapi = BapiFactory.getInstance().getCreateCustomerBuilder();
		bapi.setCopyReference(new BapiCreateCustomer.CopyReference() {
			public String getSalesOrg() {
				return SapProperties.getSalesOrg();
			}
			public String getDistrChan() {
				return SapProperties.getDistrChan();
			}
			public String getDivision() {
				return SapProperties.getDivision();
			}
			public String getRefCustomer() {
				return SapProperties.getRefCustomer();
			}
		});

		BasicContactAddressI addr = this.customer.getBillToAddress();
		if (addr == null) {
			throw new NullPointerException("Bill to address cannot be null");
		}
		bapi.setFirstName(addr.getFirstName());
		bapi.setLastName(addr.getLastName());
		bapi.setStreet(SalesOrderHelper.getSimplifiedStreet(addr));
		bapi.setPostalCode(addr.getZipCode());
		bapi.setCity(addr.getCity());
		bapi.setRegion(addr.getState());
		bapi.setCountry(addr.getCountry());

		this.invoke(bapi);

		this.sapCustomerNumber = bapi.getCustomerNo();
		if (this.sapCustomerNumber == null) {
			throw new SapException("Customer create failed (customerno null)");
		}
	}

}
