package com.freshdirect.routing.model;

public class CustomerModel implements java.io.Serializable, ICustomerModel{
	
	private String firstName;
	private String lastName;	
	private String erpCustomerPK;
	private String fdCustomerPK;
	private String email;
	private String homePhone;
	private String businessPhone;
	private String businessExt;
	private String cellPhone;
	private String companyName;
	private String batchId;
	
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
	public String getBusinessExt() {
		return businessExt;
	}
	public void setBusinessExt(String businessExt) {
		this.businessExt = businessExt;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 7;
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result
				+ ((erpCustomerPK == null) ? 0 : erpCustomerPK.hashCode());		
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;		
		if (getClass() != obj.getClass())
			return false;
		CustomerModel other = (CustomerModel) obj;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (erpCustomerPK == null) {
			if (other.erpCustomerPK != null)
				return false;
		} else if (!erpCustomerPK.equals(other.erpCustomerPK))
			return false;
		return true;
	}	
}
