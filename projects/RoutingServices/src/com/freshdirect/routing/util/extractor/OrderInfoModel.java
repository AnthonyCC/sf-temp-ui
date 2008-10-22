package com.freshdirect.routing.util.extractor;

public class OrderInfoModel {
	
	private String plant;	
	private String deliveryDate;
	private String deliveryModel;
	private String deliveryZone;
	private String orderNumber;
	private String deliveryStartTime;
	private String deliveryEndTime;
	private String customerNumber;
	private String customerName;
	private String alternateReceiver;
	private String streetAddress1;
	private String apartmentNumber;
	private String streetAddress2;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	
	private String locationId;
	private String locationName;
	private String packageSize1;
	private String packageSize2;	
	private String serviceTime;
	private String equipmentType;
	private String latitude;
	private String longitude;
	
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEquipmentType() {
		return equipmentType;
	}
	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getPackageSize1() {
		return packageSize1;
	}
	public void setPackageSize1(String packageSize1) {
		this.packageSize1 = packageSize1;
	}
	public String getPackageSize2() {
		return packageSize2;
	}
	public void setPackageSize2(String packageSize2) {
		this.packageSize2 = packageSize2;
	}
	public String getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}
	public String getAlternateReceiver() {
		return alternateReceiver;
	}
	public void setAlternateReceiver(String alternateReceiver) {
		this.alternateReceiver = alternateReceiver;
	}
	public String getApartmentNumber() {
		return apartmentNumber;
	}
	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getDeliveryEndTime() {
		return deliveryEndTime;
	}
	public void setDeliveryEndTime(String deliveryEndTime) {
		this.deliveryEndTime = deliveryEndTime;
	}
	public String getDeliveryModel() {
		return deliveryModel;
	}
	public void setDeliveryModel(String deliveryModel) {
		this.deliveryModel = deliveryModel;
	}
	public String getDeliveryStartTime() {
		return deliveryStartTime;
	}
	public void setDeliveryStartTime(String deliveryStartTime) {
		this.deliveryStartTime = deliveryStartTime;
	}
	public String getDeliveryZone() {
		return deliveryZone;
	}
	public void setDeliveryZone(String deliveryZone) {
		this.deliveryZone = deliveryZone;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStreetAddress1() {
		return streetAddress1;
	}
	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}
	public String getStreetAddress2() {
		return streetAddress2;
	}
	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();	
		strBuf.append("["+"plant="+plant+",");
		strBuf.append("deliveryDate="+deliveryDate+",");
		strBuf.append("deliveryModel="+deliveryModel+",");
		strBuf.append("deliveryZone="+deliveryZone+",");
		strBuf.append("orderNumber="+orderNumber+",");
		strBuf.append("deliveryStartTime="+deliveryStartTime+",");
		strBuf.append("deliveryEndTime="+deliveryEndTime+",");
		strBuf.append("customerNumber="+customerNumber+",");
		strBuf.append("customerName="+customerName+",");
		strBuf.append("alternateReceiver="+alternateReceiver+",");
		strBuf.append("streetAddress1="+streetAddress1+",");
		strBuf.append("apartmentNumber="+apartmentNumber+",");
		strBuf.append("streetAddress2="+streetAddress2+",");
		strBuf.append("city="+city+",");
		strBuf.append("state="+state+",");
		strBuf.append("zipCode="+zipCode+",");
		strBuf.append("country="+country+",");
		
		strBuf.append("locationId="+locationId+",");
		strBuf.append("locationName="+locationName+",");
		strBuf.append("packageSize1="+packageSize1+",");
		strBuf.append("packageSize2="+packageSize2+",");	
		strBuf.append("serviceTime="+serviceTime+",");
		strBuf.append("equipmentType="+equipmentType+",");
		strBuf.append("latitude="+latitude+",");
		strBuf.append("longitude="+longitude+"]");
		
		return strBuf.toString();
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

}
