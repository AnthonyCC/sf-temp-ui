package com.freshdirect.mobileapi.catalog.model;

public class GroupInfo {

	
	private String id;
	private String name;
    private String offer;
    private double price;
    private double minQty;
    private int version;
    
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOffer() {
		return offer;
	}
	public void setOffer(String offer) {
		this.offer = offer;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getMinQty() {
		return minQty;
	}
	public void setMinQty(double minQty) {
		this.minQty = minQty;
	}
    

}
