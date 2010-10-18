package com.freshdirect.routing.model;

import java.util.Date;


public class HandOffBatchDepotScheduleEx extends BaseModel implements IHandOffBatchDepotScheduleEx, Comparable<HandOffBatchDepotScheduleEx>  {
	
	private String dayOfWeek;
	private Date cutOffDateTime;
	private String area;
	private Date depotArrivalTime;
	private Date truckDepartureTime;
	private String originId;
	
	public HandOffBatchDepotScheduleEx() {
		super();
	}
	
	public HandOffBatchDepotScheduleEx(String dayOfWeek,Date cutOffDateTime, String area,
			Date depotArrivalTime, Date truckDepartureTime, String originId) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.cutOffDateTime = cutOffDateTime;
		this.area = area;
		this.depotArrivalTime = depotArrivalTime;
		this.truckDepartureTime = truckDepartureTime;
		this.originId = originId;
	}

	
	public String getArea() {
		return area;
	}

	public Date getDepotArrivalTime() {
		return depotArrivalTime;
	}

	public Date getTruckDepartureTime() {
		return truckDepartureTime;
	}


	public void setArea(String area) {
		this.area = area;
	}

	public void setDepotArrivalTime(Date depotArrivalTime) {
		this.depotArrivalTime = depotArrivalTime;
	}

	public void setTruckDepartureTime(Date truckDepartureTime) {
		this.truckDepartureTime = truckDepartureTime;
	}

	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}
		
	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public Date getCutOffDateTime() {
		return cutOffDateTime;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public void setCutOffDateTime(Date cutOffDateTime) {
		this.cutOffDateTime = cutOffDateTime;
	}

	@Override
	public int hashCode() {
		return (area==null || dayOfWeek==null|| cutOffDateTime==null || depotArrivalTime==null|| truckDepartureTime==null || originId==null) ?	super.hashCode() :
			area.hashCode() ^ dayOfWeek.hashCode() ^ cutOffDateTime.hashCode() ^ depotArrivalTime.hashCode() ^ truckDepartureTime.hashCode() ^ originId.hashCode();		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HandOffBatchDepotScheduleEx other = (HandOffBatchDepotScheduleEx) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (dayOfWeek == null) {
			if (other.dayOfWeek != null)
				return false;
		} else if (!dayOfWeek.equals(other.dayOfWeek))
			return false;
		if (cutOffDateTime == null) {
			if (other.cutOffDateTime != null)
				return false;
		} else if (!cutOffDateTime.equals(other.cutOffDateTime))
			return false;
		if (depotArrivalTime == null) {
			if (other.depotArrivalTime != null)
				return false;
		} else if (!depotArrivalTime.equals(other.depotArrivalTime))
			return false;
		if (truckDepartureTime == null) {
			if (other.truckDepartureTime != null)
				return false;
		} else if (!truckDepartureTime.equals(other.truckDepartureTime))
			return false;
		if (originId == null) {
			if (other.originId != null)
				return false;
		} else if (!originId.equals(other.originId))
			return false;
		return true;
	}

	@Override
	public int compareTo(HandOffBatchDepotScheduleEx o) {
		// TODO Auto-generated method stub
		if(this.equals(o)) {
			return 0;
		} else {
			return this.toString().compareTo(o.toString());
		}
	}
	
	@Override
	public String toString() {
		return "HandOffBatchDepotSchedule [dayOfWeek=" + dayOfWeek + ", cutOffDateTime=" + cutOffDateTime  + ", area=" + area + ", depotarrivaltime="
				+ depotArrivalTime+ ", truckdeparturetime="
				+ truckDepartureTime+ ", originId="
				+ originId + "]";
	}
	
	
	
}
