package com.freshdirect.common.address;

public class ContactAddressAdapter implements BasicContactAddressI {

	private String firstName;
	private String lastName;
	private PhoneNumber phone;
	private BasicAddressI address;

	public ContactAddressAdapter(BasicAddressI address, String firstName, String lastName) {
		this(address, firstName, lastName, null);
	}

	public ContactAddressAdapter(BasicAddressI address, String firstName, String lastName, PhoneNumber phone) {
		this.address = address;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public PhoneNumber getPhone() {
		return this.phone;
	}

	public String getAddress1() {
		return this.address.getAddress1();
	}

	public String getAddress2() {
		return this.address.getAddress2();
	}

	public String getApartment() {
		return this.address.getApartment();
	}
	
	public String getCity() {
		return this.address.getCity();
	}

	public String getZipCode() {
		return this.address.getZipCode();
	}

	public String getState() {
		return this.address.getState();
	}

	public String getCountry() {
		return this.address.getCountry();
	}

	// added as geocode phase 
	public AddressInfo getAddressInfo() {
		// TODO Auto-generated method stub
		return address.getAddressInfo();
	}
}

