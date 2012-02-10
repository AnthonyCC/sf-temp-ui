package com.freshdirect.transadmin.model;

public class NeighbourhoodZipcode {
	
	private String zipcode;
	private String county;
	private String state;
	private Neighbourhood neighborhood;
	
	public NeighbourhoodZipcode() {
		super();
	}
	public NeighbourhoodZipcode(String zipcode, String county, String state,
			Neighbourhood neighborhood) {
		super();
		this.zipcode = zipcode;
		this.county = county;
		this.state = state;
		this.neighborhood = neighborhood;
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
	public Neighbourhood getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(Neighbourhood neighborhood) {
		this.neighborhood = neighborhood;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
