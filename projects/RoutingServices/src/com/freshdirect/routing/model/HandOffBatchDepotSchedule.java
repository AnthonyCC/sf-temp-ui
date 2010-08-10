package com.freshdirect.routing.model;

import java.util.Date;


public class HandOffBatchDepotSchedule extends BaseModel implements IHandOffBatchDepotSchedule, Comparable<HandOffBatchDepotSchedule>  {
	
	private String batchId;
	private String area;
	private Date depotArrivalTime;
	private Date truckDepartureTime;
	
	public HandOffBatchDepotSchedule() {
		super();
	}
	
	public HandOffBatchDepotSchedule(String batchId, String area,
			Date depotArrivalTime, Date truckDepartureTime) {
		super();
		this.batchId = batchId;
		this.area = area;
		this.depotArrivalTime = depotArrivalTime;
		this.truckDepartureTime = truckDepartureTime;
	}

	public String getBatchId() {
		return batchId;
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

	public void setBatchId(String batchId) {
		this.batchId = batchId;
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

	@Override
	public int hashCode() {
		return (area==null || batchId==null || depotArrivalTime==null|| truckDepartureTime==null) ?	super.hashCode() :
			area.hashCode() ^ batchId.hashCode() ^ depotArrivalTime.hashCode() ^ truckDepartureTime.hashCode();		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HandOffBatchDepotSchedule other = (HandOffBatchDepotSchedule) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
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
		return true;
	}

	@Override
	public int compareTo(HandOffBatchDepotSchedule o) {
		// TODO Auto-generated method stub
		if(this.equals(o)) {
			return 0;
		} else {
			return this.toString().compareTo(o.toString());
		}
	}
	
	@Override
	public String toString() {
		return "HandOffBatchDepotSchedule [batchId=" + batchId  + ", area=" + area + ", depotarrivaltime="
				+ depotArrivalTime+ ", truckdeparturetime="
				+ truckDepartureTime + "]";
	}
	
	
	
}
