package com.freshdirect.transadmin.model;

import java.io.Serializable;

public class ParkingSlot implements Serializable {
	
	private String slotNumber;
	private String slotDesc;
	private ParkingLocation location;
	private String barcodeStatus;
	private String pavedStatus;
	
	public String getSlotNumber() {
		return slotNumber;
	}
	public ParkingSlot(String slotNumber, String slotDesc,
			ParkingLocation location, String barcodeStatus, String pavedStatus) {
		super();
		this.slotNumber = slotNumber;
		this.slotDesc = slotDesc;
		this.location = location;
		this.barcodeStatus = barcodeStatus;
		this.pavedStatus = pavedStatus;
	}
	
	public ParkingSlot() {	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}
	public String getSlotDesc() {
		return slotDesc;
	}
	public void setSlotDesc(String slotDesc) {
		this.slotDesc = slotDesc;
	}
	public ParkingLocation getLocation() {
		return location;
	}
	public void setLocation(ParkingLocation location) {
		this.location = location;
	}	
	
	public String getBarcodeStatus() {
		return barcodeStatus;
	}
	public void setBarcodeStatus(String barcodeStatus) {
		this.barcodeStatus = barcodeStatus;
	}
	public String getPavedStatus() {
		return pavedStatus;
	}
	public void setPavedStatus(String pavedStatus) {
		this.pavedStatus = pavedStatus;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result
				+ ((slotNumber == null) ? 0 : slotNumber.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParkingSlot other = (ParkingSlot) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (slotNumber == null) {
			if (other.slotNumber != null)
				return false;
		} else if (!slotNumber.equals(other.slotNumber))
			return false;
		return true;
	}
}
