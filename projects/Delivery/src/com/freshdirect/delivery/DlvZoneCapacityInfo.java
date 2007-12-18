package com.freshdirect.delivery;

import java.io.Serializable;

/**
 * FIXME remainingCapacity should consider chef's table and current time, but currently does not.
 */
public class DlvZoneCapacityInfo implements Serializable {
	
	private final String zoneCode;
	private final int totalCapacity;
	private final int remainingCapacity;
	
	public DlvZoneCapacityInfo (String zoneCode, int totalCapacity, int remainingCapacity) {
		this.zoneCode = zoneCode;
		this.totalCapacity = totalCapacity;
		this.remainingCapacity = remainingCapacity;
	}
	
	public String getZoneCode(){
		return this.zoneCode;
	}
	
	public int getTotalCapacity () {
		return this.totalCapacity;
	}
	
	public int getRemainingCapacity () {
		return this.remainingCapacity;
	}
	
	public int getRemainingCapacityPerc() {
		return (int) (((double)this.remainingCapacity / this.totalCapacity) * 100);
	}
	
	public String toString() {
		return "DlvZoneCapacityInfo[ZONECODE:"
			+ this.zoneCode
			+ " TOTAL CAPACITY:"
			+ this.totalCapacity
			+ " REMAININ CAPACITY:"
			+ this.remainingCapacity
			+ " REMAINING%:"
			+ this.getRemainingCapacityPerc()
			+ "]";
	}
}
