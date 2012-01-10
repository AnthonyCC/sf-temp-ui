/**
 * 
 */
package com.freshdirect.transadmin.model;

import java.io.Serializable;

/**
 * @author kkanuganti
 *
 */
public class ParkingLocation implements Serializable {
	
	private String locationName;
	private String locationDesc;
	
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationDesc() {
		return locationDesc;
	}
	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}
	
	public ParkingLocation(String locationName, String locationDesc) {
		super();
		this.locationName = locationName;
		this.locationDesc = locationDesc;
	}
	public ParkingLocation() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((locationName == null) ? 0 : locationName.hashCode());
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
		ParkingLocation other = (ParkingLocation) obj;
		if (locationName == null) {
			if (other.locationName != null)
				return false;
		} else if (!locationName.equals(other.locationName))
			return false;
		return true;
	}
	
}
