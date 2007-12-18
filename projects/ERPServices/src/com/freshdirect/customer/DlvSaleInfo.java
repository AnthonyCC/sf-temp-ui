/*
 * 
 * DlvSaleInfo.java
 * Date: Sep 20, 2002 Time: 12:11:40 PM
 */
package com.freshdirect.customer;

/**
 * 
 * @author knadeem
 */

public class DlvSaleInfo extends BasicSaleInfo{
	
	private final String stopSequence;
	private final String firstName;
	private final String lastName;
	private String address;
	private String apartment;
	private String zipcode;
	
	public DlvSaleInfo (String saleId, String erpCustomerId, EnumSaleStatus status, String stopSequence, String firstName, String lastName){
		super(saleId,erpCustomerId, status);
		this.stopSequence = stopSequence;
		this.firstName = firstName;
		this.lastName = lastName;
	} 
	
	public String getStopSequence(){
		return this.stopSequence;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public String getZipcode(){
		return this.zipcode;
	}
	
	public void setZipcode(String zipcode){
		this.zipcode = zipcode;
	}
	
	public String getApartment(){
		return this.apartment;
	}
	
	public void setApartment(String apartment){
		this.apartment = apartment;
	}
}
