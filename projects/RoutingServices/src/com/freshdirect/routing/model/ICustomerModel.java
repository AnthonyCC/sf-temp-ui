package com.freshdirect.routing.model;

public interface ICustomerModel {
	String getFirstName();
	void setFirstName(String firstName);	

	String getLastName();
	void setLastName(String lastName);
	
	String getErpCustomerPK();
	void setErpCustomerPK(String erpCustomerPK);
	
	String getFdCustomerPK();	
	void setFdCustomerPK(String fdCustomerPK);
	
	String getEmail();
	void setEmail(String email);

	String getHomePhone();
	void setHomePhone(String homePhone);

	String getBusinessPhone();
	void setBusinessPhone(String businessPhone);

	String getCellPhone();
	void setCellPhone(String cellPhone);
	
	String getBusinessExt();
	void setBusinessExt(String businessExt);
	
	String getCompanyName();
	void setCompanyName(String companyName);
	
	String getBatchId();
	void setBatchId(String batchId);
}
