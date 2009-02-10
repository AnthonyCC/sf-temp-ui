package com.freshdirect.transadmin.datamanager.report.model;

import java.io.Serializable;
import java.util.Date;

public class CutOffReportKey implements Serializable, Comparable {
	
	private Date orderDate;
	private Date timeWindowStart;
	private Date timeWindowStop;
	private String routeId;
	
	

	public CutOffReportKey(Date orderDate, Date timeWindowStart, Date timeWindowStop, String routeId) {
		super();
		this.orderDate = orderDate;
		this.timeWindowStart = timeWindowStart;
		this.timeWindowStop = timeWindowStop;
		this.routeId = routeId;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((orderDate == null) ? 0 : orderDate.hashCode());
		result = PRIME * result + ((routeId == null) ? 0 : routeId.hashCode());
		result = PRIME * result + ((timeWindowStart == null) ? 0 : timeWindowStart.hashCode());
		result = PRIME * result + ((timeWindowStop == null) ? 0 : timeWindowStop.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CutOffReportKey other = (CutOffReportKey) obj;
		if (orderDate == null) {
			if (other.orderDate != null)
				return false;
		} else if (!orderDate.equals(other.orderDate))
			return false;
		if (routeId == null) {
			if (other.routeId != null)
				return false;
		} else if (!routeId.equals(other.routeId))
			return false;
		if (timeWindowStart == null) {
			if (other.timeWindowStart != null)
				return false;
		} else if (!timeWindowStart.equals(other.timeWindowStart))
			return false;
		if (timeWindowStop == null) {
			if (other.timeWindowStop != null)
				return false;
		} else if (!timeWindowStop.equals(other.timeWindowStop))
			return false;
		return true;
	}

	public int compareTo(Object objCompare) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    
	    if ( this.equals(objCompare)) return EQUAL;
	    
	    if(objCompare instanceof CutOffReportKey) {
	    	CutOffReportKey tmpKey = (CutOffReportKey)objCompare;
	    	int compareStartTime = this.getTimeWindowStart().compareTo(tmpKey.getTimeWindowStart());
	    	if(compareStartTime !=  EQUAL) {
	    		return compareStartTime; 
	    	} else {
	    		return this.getRouteId().compareTo(tmpKey.getRouteId());
	    	}
	    }

		return 0;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
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

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	public String toString() {
		return timeWindowStart+"-"+timeWindowStop+"-"+routeId;
	}

}
