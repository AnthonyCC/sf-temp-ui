package com.freshdirect.common.address;

public class ContactAddressModel extends AddressModel implements BasicContactAddressI {

	private String firstName = "";
	private String lastName = "";
	private PhoneNumber phone = new PhoneNumber("");
	
	private String customerId="";

	public ContactAddressModel() {
		
	}

	/** Copy constructor */
	public ContactAddressModel(BasicContactAddressI address) {
		this.setFrom(address);
	}
	
    public String getFirstName() {
    	return firstName;
    }

    public void setFirstName(String fname) {
    	this.firstName = fname;
    }

    public String getLastName() {
    	return lastName;
    }

    public void setLastName(String lname) {
    	this.lastName = lname;
    }

    public PhoneNumber getPhone() {
    	return this.phone;
    }

    public void setPhone(PhoneNumber p) {
    	this.phone = p==null ? new PhoneNumber("") : p;
    }

	public void setFrom(BasicContactAddressI address) {
		super.setFrom(address);
		this.setFirstName(address.getFirstName());
		this.setLastName(address.getLastName());
		this.setPhone(address.getPhone());
		this.setCustomerId(address.getCustomerId());
	}
	
	public void setFrom(AddressI address, String firstName, String lastName, String customerId) {
		super.setFrom(address);		
		this.setFirstName(firstName);
		this.setLastName(lastName);		
		this.setCustomerId(customerId);
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
