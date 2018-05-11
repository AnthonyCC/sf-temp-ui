package com.freshdirect.common.address;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;

public class AddressModel extends ModelSupport implements AddressI {

	private static final long	serialVersionUID	= 5400543540252405370L;
	
	private String address1 = "";
	private String address2 = "";
	private String apartment = "";
	private String city = "";
	private String state = "";
	private String zipCode = "";
	private String country = "US";
	private EnumServiceType serviceType;
	private String companyName;
	private AddressInfo addressInfo = null;
	
	//FDX-1873 - Show timeslots for anonymous address
	private  boolean isCustomerAnonymousAddress;

	public AddressModel() {
	}

	/**
	 * Copy constructor
	 */
	public AddressModel(BasicAddressI address) {
		this.setFrom(address);
	}

	/** Convenience constructor */
	public AddressModel(String address1, String apartment, String city, String state, String zipCode) {
		this.setAddress1(address1);
		this.setAddress2(address2);
		this.setApartment(apartment);
		this.setCity(city);
		this.setState(state);
		this.setZipCode(zipCode);
	}
	
	public void setPK(PrimaryKey pk) {
		super.setPK(pk);
	}
	
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.toUpperCase();
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zip) {
		this.zipCode = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public boolean isCustomerAnonymousAddress() {
		return isCustomerAnonymousAddress;
	}

	public void setCustomerAnonymousAddress(boolean isCustomerAnonymousAddress) {
		this.isCustomerAnonymousAddress = isCustomerAnonymousAddress;
	}
	
	
	public EnumServiceType getServiceType(){
		
		if(this.serviceType != null){
			return this.serviceType;
		}
		
		EnumAddressType addressType = this.getAddressType();
		if(addressType == null){
			return EnumServiceType.HOME;
		}
    	return (EnumAddressType.FIRM.equals(addressType) ? EnumServiceType.CORPORATE : EnumServiceType.HOME);
    }

    public void setServiceType (EnumServiceType serviceType){
    	this.serviceType = serviceType;
    }
    
    public String getCompanyName() {
    	return this.companyName;
    }
    
    public void setCompanyName(String companyName){
    	this.companyName = companyName;
    }
	
	public AddressInfo getAddressInfo(){
		return this.addressInfo;
	}
	
	public void setAddressInfo(AddressInfo addressInfo){		
		this.addressInfo = addressInfo;
	}

	public double getLongitude() {
		return (this.addressInfo != null) ? addressInfo.getLongitude() : 0.0;
	}

	public double getLatitude() {
		return (this.addressInfo != null) ? addressInfo.getLatitude() : 0.0;
	}
	
	public String getScrubbedStreet(){
		return (this.addressInfo != null) ? addressInfo.getScrubbedStreet() : "";
	}
	
	public EnumAddressType getAddressType() {
		return (this.addressInfo != null) ? addressInfo.getAddressType() : null;
	}

	public String getBuildingId() {
		return (this.addressInfo != null) ? addressInfo.getBuildingId() : "";
	}
	public String getLocationId() {
		return (this.addressInfo != null) ? addressInfo.getLocationId() : "";
	}
	
	/**
	 * Check match on address1, address2, apt, city, state, zipcode.
	 */
	public boolean isSameLocation(BasicAddressI address) {
		return NVL.apply(this.getAddress1(),"").equalsIgnoreCase(address.getAddress1())
			&& NVL.apply(this.getCity(),"").equalsIgnoreCase(address.getCity())
			&& NVL.apply(this.getState(),"").equalsIgnoreCase(address.getState())
			&& this.getZipCode().equalsIgnoreCase(address.getZipCode())
			&& NVL.apply(this.getAddress2(), "").equalsIgnoreCase(NVL.apply(address.getAddress2(), ""))
			&& NVL.apply(this.getApartment(), "").equalsIgnoreCase(NVL.apply(address.getApartment(), ""));
	}

	  
	
	public void setFrom(BasicAddressI address) {
		this.setAddress1(address.getAddress1());
		this.setAddress2(address.getAddress2());
		this.setApartment(address.getApartment());
		this.setCity(address.getCity());
		this.setState(address.getState());
		this.setZipCode(address.getZipCode());
		this.setCountry(address.getCountry());
		// changes done for geocode		
		this.setAddressInfo(address.getAddressInfo());
	}
	
	public String toString() {
		return "AddressModel[addr1 " + address1
			+ ", addr2 "
			+ address2
			+ ", apt "
			+ apartment
			+ ", city "
			+ city
			+ ", country "
			+ country
			+ ", state "
			+ state
			+ ", zipcode "
			+ zipCode
			+ ", serviceType "
			+ serviceType
			+ ", companyName "
			+ companyName
			+ ", info "
			+ addressInfo
			+ "]";
	}

	public String toShortString(boolean isCorporate, String delimiter) {

		StringBuilder sb = new StringBuilder();
		
		if(address1!=null){
			sb.append(address1);

			if (apartment!=null && apartment.trim().length()>0) {
				sb.append(", ");
				sb.append(isCorporate ? "Floor/Suite " : "Apt. ");
				sb.append(apartment);
			}
		}
		

		if(address2!=null && address2.trim().length()>0) {
			sb.append(delimiter);
			sb.append(address2);
		}
		
		return sb.toString();
	}
	
	@Override
	public void setId( String id ) {
		if (id != null)
			this.setPK( new PrimaryKey( id ) );
	}

}
