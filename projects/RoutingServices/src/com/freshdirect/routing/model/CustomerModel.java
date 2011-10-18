package com.freshdirect.routing.model;

public class CustomerModel extends BaseModel implements ICustomerModel{
	
	private String firstName;
	private String lastName;	
	private String erpCustomerPK;
	private String fdCustomerPK;
	private String email;
	private String homePhone;
	private String businessPhone;
	private String cellPhone;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getErpCustomerPK() {
		return erpCustomerPK;
	}
	public void setErpCustomerPK(String erpCustomerPK) {
		this.erpCustomerPK = erpCustomerPK;
	}
	public String getFdCustomerPK() {
		return fdCustomerPK;
	}
	public void setFdCustomerPK(String fdCustomerPK) {
		this.fdCustomerPK = fdCustomerPK;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getBusinessPhone() {
		return businessPhone;
	}
	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	
}
