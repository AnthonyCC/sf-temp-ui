package com.freshdirect.sap.jco;

import com.freshdirect.sap.bapi.BapiCreateCustomer;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoStructure;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiCreateCustomer extends JcoBapiFunction implements BapiCreateCustomer {

	private JCoStructure personalData;

	public JcoBapiCreateCustomer() throws JCoException
	{
		super("Z_BAPI_CUSTOMER_CREATE");

		this.personalData = this.function.getImportParameterList().getStructure("PI_PERSONALDATA");
		this.personalData.setValue("LANGU_P", "E");
		this.personalData.setValue("CURRENCY", "USD");
	}

	public void setCopyReference(CopyReference copyRef)
	{
		JCoStructure copyReference = this.function.getImportParameterList().getStructure("PI_COPYREFERENCE");
	
		copyReference.setValue("SALESORG", copyRef.getSalesOrg());
		copyReference.setValue("DISTR_CHAN", copyRef.getDistrChan());
		copyReference.setValue("DIVISION", copyRef.getDivision());
		copyReference.setValue("REF_CUSTMR", copyRef.getRefCustomer());
	}

	public void setFirstName(String firstName) {
		personalData.setValue("FIRSTNAME", firstName);
	}

	public void setLastName(String lastName) {
		personalData.setValue("LASTNAME", lastName);
	}

	public void setStreet(String street) {
		personalData.setValue("STREET", street);
	}

	public void setPostalCode(String zip) {
		personalData.setValue("POSTL_COD1", zip);
	}

	public void setCity(String city) {
		personalData.setValue("CITY", city);
	}

	public void setRegion(String region) {
		personalData.setValue("REGION", region);
	}

	public void setCountry(String country) {
		personalData.setValue("COUNTRY", country);
	}

	public String getCustomerNo() {
		return this.function.getExportParameterList().getString("CUSTOMERNO");
	}

}
