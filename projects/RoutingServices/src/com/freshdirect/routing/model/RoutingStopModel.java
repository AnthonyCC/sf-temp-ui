package com.freshdirect.routing.model;

import java.util.Date;

public class RoutingStopModel extends OrderModel implements IRoutingStopModel, Comparable  {
	
	private int stopNo;
	
	private ILocationModel location;
	
	private boolean isDepot;
	
	private Date timeWindowStart;
	private Date timeWindowStop;
	private Date stopArrivalTime;
	
	public RoutingStopModel() {
		super();	
	}
	
	public RoutingStopModel(int stopNo) {
		super();
		this.stopNo = stopNo;
	}

	public boolean isDepot() {
		return isDepot;
	}

	public void setDepot(boolean isDepot) {
		this.isDepot = isDepot;
	}
	
	public Date getStopArrivalTime() {
		return stopArrivalTime;
	}

	public void setStopArrivalTime(Date stopArrivalTime) {
		this.stopArrivalTime = stopArrivalTime;
	}

	public Date getTimeWindowStart() {
		return timeWindowStart;
	}

	public void setTimeWindowStart(Date timeWindowStart) {
		this.timeWindowStart = timeWindowStart;
	}

	public Date getTimeWindowStop() {
		return timeWindowStop;
	}

	public void setTimeWindowStop(Date timeWindowStop) {
		this.timeWindowStop = timeWindowStop;
	}

	
	public ILocationModel getLocation() {
		return location;
	}

	public void setLocation(ILocationModel location) {
		this.location = location;
	}
	
	public int getStopNo() {
		return stopNo;
	}

	public void setStopNo(int stopNo) {
		this.stopNo = stopNo;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + stopNo;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final IRoutingStopModel other = (IRoutingStopModel) obj;
		if (stopNo != other.getStopNo())
			return false;
		return true;
	}

	
	
	public int compareTo(Object objCompare) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    
	    if ( this.equals(objCompare)) return EQUAL;
	    
	    if(objCompare instanceof RoutingStopModel) {
	    	IRoutingStopModel tmpKey = (IRoutingStopModel)objCompare;
	    	
	    	if(this.getStopNo() < tmpKey.getStopNo()) {
	    		return BEFORE; 
	    	} else {
	    		return AFTER; 
	    	}
	    }

		return 0;
	}

	
}
