package com.freshdirect.dataloader.geocodefilter;

public class GFRecord {

	private String zip;
	private String zpf;
	private String seqNum;
	private String bldgNum;
	private String directional;
	private String streetAddress;
	private String postDirectional;
	private String aptDesignator;
	private String aptNum;
	
	public String getAptDesignator() {
		return aptDesignator;
	}
	
	public void setAptDesignator(String aptDesignator) {
		this.aptDesignator = aptDesignator;
	}
	
	public String getAptNum() {
		return aptNum;
	}
	
	public void setAptNum(String aptNum) {
		this.aptNum = aptNum;
	}
	
	public String getBldgNum() {
		return bldgNum;
	}
	
	public void setBldgNum(String bldgNum) {
		this.bldgNum = bldgNum;
	}
	
	public String getDirectional() {
		return directional;
	}
	
	public void setDirectional(String directional) {
		this.directional = directional;
	}
	
	public String getPostDirectional() {
		return postDirectional;
	}
	
	public void setPostDirectional(String postDirectional) {
		this.postDirectional = postDirectional;
	}
	
	public String getSeqNum() {
		return seqNum;
	}
	
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	
	public String getStreetAddress() {
		return streetAddress;
	}
	
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public String getZpf() {
		return zpf;
	}
	
	public void setZpf(String zpf) {
		this.zpf = zpf;
	}
	
	public String toString(){
		return "Record: zip:" + this.zip + 
				" zpf:" + this.zpf + 
				" seqNum:" + this.seqNum + 
				" bldgNum:" + this.bldgNum + 
				" directional:" + this.directional + 
				" streetAddress:" + this.streetAddress + 
				" postDirectional:" + this.postDirectional +
				" aptDesignator:" + this.aptDesignator +
				" aptNum:" + this.aptNum;
				
		
	}
	
	

}
