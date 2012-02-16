package com.freshdirect.transadmin.model;

public class SectorZipcode {
	
	private String zipcode;
	private String county;
	private String state;
	private Sector sector;
	
	public SectorZipcode() {
		super();
	}
	public SectorZipcode(String zipcode, String county, String state,
			Sector sector) {
		super();
		this.zipcode = zipcode;
		this.county = county;
		this.state = state;
		this.sector = sector;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}	
	public Sector getSector() {
		return sector;
	}
	public void setSector(Sector sector) {
		this.sector = sector;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
