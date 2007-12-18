/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import com.freshdirect.sap.bapi.BapiCreateCustomer;
import com.sap.mw.jco.JCO;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiCreateCustomer extends JcoBapiFunction implements BapiCreateCustomer {

	private JCO.Structure personalData;

	public JcoBapiCreateCustomer() {
		super("Z_BAPI_CUSTOMER_CREATE");

		this.personalData = this.function.getImportParameterList().getStructure("PI_PERSONALDATA");
		this.personalData.setValue("E", "LANGU_P");
		this.personalData.setValue("USD", "CURRENCY");
	}

	public void setCopyReference(CopyReference copyRef) {
		JCO.Structure copyReference = this.function.getImportParameterList().getStructure("PI_COPYREFERENCE");
		copyReference.setValue(copyRef.getSalesOrg(), "SALESORG");
		copyReference.setValue(copyRef.getDistrChan(), "DISTR_CHAN");
		copyReference.setValue(copyRef.getDivision(), "DIVISION");
		copyReference.setValue(copyRef.getRefCustomer(), "REF_CUSTMR");
	}

	public void setFirstName(String firstName) {
		personalData.setValue(firstName, "FIRSTNAME");
	}

	public void setLastName(String lastName) {
		personalData.setValue(lastName, "LASTNAME");
	}

	public void setStreet(String street) {
		personalData.setValue(street, "STREET");
	}

	public void setPostalCode(String zip) {
		personalData.setValue(zip, "POSTL_COD1");
	}

	public void setCity(String city) {
		personalData.setValue(city, "CITY");
	}

	public void setRegion(String region) {
		personalData.setValue(region, "REGION");
	}

	public void setCountry(String country) {
		personalData.setValue(country, "COUNTRY");
	}

	public String getCustomerNo() {
		return this.function.getExportParameterList().getString("CUSTOMERNO");
	}

}
