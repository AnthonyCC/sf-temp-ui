package com.freshdirect.delivery;

import java.io.Serializable;

import com.freshdirect.framework.util.TimeOfDay;

public class DlvTimeslotCapacityInfo implements Serializable {

	private final TimeOfDay cutoffTime;
	private final String zoneCode;
	private final String zoneName;
	private final TimeOfDay startTime;
	private final TimeOfDay endTime;
	private final int capacity;

	public DlvTimeslotCapacityInfo(
		TimeOfDay cutoffTime,
		String zoneCode,
		String zoneName,
		TimeOfDay startTime,
		TimeOfDay endTime,
		int capacity) {
		this.cutoffTime = cutoffTime;
		this.zoneCode = zoneCode;
		this.zoneName = zoneName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.capacity = capacity;
	}

	public TimeOfDay getCutoffTime() {
		return cutoffTime;
	}

	public TimeOfDay getEndTime() {
		return endTime;
	}

	public TimeOfDay getStartTime() {
		return startTime;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public String getZoneName() {
		return zoneName;
	}

	public int getCapacity() {
		return capacity;
	}

}
