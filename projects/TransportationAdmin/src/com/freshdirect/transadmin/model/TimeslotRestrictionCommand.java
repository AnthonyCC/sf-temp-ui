package com.freshdirect.transadmin.model;

import java.text.ParseException;

import com.freshdirect.transadmin.util.TransStringUtil;

public class TimeslotRestrictionCommand implements java.io.Serializable {


	public TimeslotRestrictionCommand(String id, String dayOfWeek,
			String zoneCode, String startTime, String endTime,
			String condition, String active) {
		super();
		this.id = id;
		this.dayOfWeek = dayOfWeek;
		this.zoneCode = zoneCode;
		this.startTime = startTime;
		this.endTime = endTime;
		this.condition = condition;
		this.active = active;
	}

	private String id;
	private String dayOfWeek;
	private String zoneCode;
	private String startTime;
	private String endTime;
	private String condition;
	private String active;

	
	public TimeslotRestrictionCommand() {
	}

	public TimeslotRestrictionCommand(String id) {
		this.id = id;
	}

	public TimeslotRestrictionCommand(TimeslotRestriction modelIn) throws ParseException {
		this.id = modelIn.getId();
		this.dayOfWeek = modelIn.getDayOfWeek();
		this.zoneCode = modelIn.getZoneCode();
		this.startTime = TransStringUtil.getServerTime(modelIn.getStartTime());
		this.endTime = TransStringUtil.getServerTime(modelIn.getEndTime());
		this.condition = modelIn.getCondition();
		this.active = modelIn.getActive();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((condition == null) ? 0 : condition.hashCode());
		result = prime * result
				+ ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result
				+ ((zoneCode == null) ? 0 : zoneCode.hashCode());
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
		TimeslotRestrictionCommand other = (TimeslotRestrictionCommand) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (dayOfWeek == null) {
			if (other.dayOfWeek != null)
				return false;
		} else if (!dayOfWeek.equals(other.dayOfWeek))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (zoneCode == null) {
			if (other.zoneCode != null)
				return false;
		} else if (!zoneCode.equals(other.zoneCode))
			return false;
		return true;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
}
