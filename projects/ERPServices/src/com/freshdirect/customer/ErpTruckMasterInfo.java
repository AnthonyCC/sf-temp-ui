package com.freshdirect.customer;

import java.io.Serializable;
import java.util.*;

public class ErpTruckMasterInfo implements Serializable {

	public static String TRUCK_TYPE_REGULAR = "Regular";
	public static String TRUCK_TYPE_BEER = "Beer";
	public static String TRUCK_TYPE_FREEZER = "Freezer";
	public static String TRUCK_TYPE_PLATTER = "Platter";
	

	
	public ErpTruckMasterInfo(
		String truckNumber,
		String truckType,
		String truckLicenceNumber,	
		String truckLocation
		) {
		this.truckNumber = truckNumber;		
		this.truckLicenceNumber = truckLicenceNumber;
		this.truckType = truckType;
		this.location=truckLocation;
	}

	public void setDetails(List list) {
		if(list == null)
			list = new ArrayList();
		else
			details = list;
	}
	
	public List getDetails() {
		return details;
	}
	
	public String toString() {
		return "ErpTruckInfo[orderNumber: "
			+ this.truckNumber
			+ " cartonNumber: "
			+ this.truckLicenceNumber
			+ " cartonType: "
			+ this.truckType
			+ " details: "
			+ details.toString();
	}

	private String truckNumber;	
	private String truckLicenceNumber;
	private String truckType;
	private String location;
	private List details = new ArrayList();



	public String getTruckLicenceNumber() {
		return truckLicenceNumber;
	}

	public void setTruckLicenceNumber(String truckLicenceNumber) {
		this.truckLicenceNumber = truckLicenceNumber;
	}

	public String getTruckNumber() {
		return truckNumber;
	}

	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}

	public String getTruckType() {
		return truckType;
	}

	public void setTruckType(String truckType) {
		this.truckType = truckType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
