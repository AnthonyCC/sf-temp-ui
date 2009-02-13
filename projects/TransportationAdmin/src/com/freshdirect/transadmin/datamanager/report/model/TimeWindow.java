package com.freshdirect.transadmin.datamanager.report.model;

import java.io.Serializable;
import java.util.Date;


public class TimeWindow implements  Serializable, Comparable {
	
	private Date timeWindowStart;
	private Date timeWindowStop;
	
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
	public TimeWindow(Date timeWindowStart, Date timeWindowStop) {
		super();
		this.timeWindowStart = timeWindowStart;
		this.timeWindowStop = timeWindowStop;
	}
	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
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
		final TimeWindow other = (TimeWindow) obj;
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
	    
	    if(objCompare instanceof TimeWindow) {
	    	TimeWindow tmpKey = (TimeWindow)objCompare;	    	
	    	int compareStartTime = this.getTimeWindowStart().compareTo(tmpKey.getTimeWindowStart());
	    	return compareStartTime;
	    	/*if(compareStartTime !=  EQUAL) {
	    		return compareStartTime; 
	    	} else {
	    		return this.getTimeWindowStop().compareTo(tmpKey.getTimeWindowStop());
	    	}*/
	    }

		return 0;
	}
	public String toString() {
		return "["+timeWindowStart + ":"+timeWindowStop+"]";
	}
}
