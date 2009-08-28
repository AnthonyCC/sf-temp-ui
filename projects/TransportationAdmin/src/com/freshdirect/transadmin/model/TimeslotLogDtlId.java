package com.freshdirect.transadmin.model;

import java.util.Date;

public class TimeslotLogDtlId implements java.io.Serializable {

	private String timeslotLogId;
	private Date baseDate;
	private Date startTime;
	private Date endTime;

	public TimeslotLogDtlId() {
	}

	public TimeslotLogDtlId(String timeslotLogId, Date baseDate,
			Date startTime, Date endTime) {
		this.timeslotLogId = timeslotLogId;
		this.baseDate = baseDate;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getTimeslotLogId() {
		return this.timeslotLogId;
	}

	public void setTimeslotLogId(String timeslotLogId) {
		this.timeslotLogId = timeslotLogId;
	}

	public Date getBaseDate() {
		return this.baseDate;
	}

	public void setBaseDate(Date baseDate) {
		this.baseDate = baseDate;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TimeslotLogDtlId))
			return false;
		TimeslotLogDtlId castOther = (TimeslotLogDtlId) other;

		return ((this.getTimeslotLogId() == castOther.getTimeslotLogId()) || (this
				.getTimeslotLogId() != null
				&& castOther.getTimeslotLogId() != null && this
				.getTimeslotLogId().equals(castOther.getTimeslotLogId())))
				&& ((this.getBaseDate() == castOther.getBaseDate()) || (this
						.getBaseDate() != null
						&& castOther.getBaseDate() != null && this
						.getBaseDate().equals(castOther.getBaseDate())))
				&& ((this.getStartTime() == castOther.getStartTime()) || (this
						.getStartTime() != null
						&& castOther.getStartTime() != null && this
						.getStartTime().equals(castOther.getStartTime())))
				&& ((this.getEndTime() == castOther.getEndTime()) || (this
						.getEndTime() != null
						&& castOther.getEndTime() != null && this.getEndTime()
						.equals(castOther.getEndTime())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getTimeslotLogId() == null ? 0 : this.getTimeslotLogId()
						.hashCode());
		result = 37 * result
				+ (getBaseDate() == null ? 0 : this.getBaseDate().hashCode());
		result = 37 * result
				+ (getStartTime() == null ? 0 : this.getStartTime().hashCode());
		result = 37 * result
				+ (getEndTime() == null ? 0 : this.getEndTime().hashCode());
		return result;
	}


}
