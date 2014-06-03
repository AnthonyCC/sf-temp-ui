package com.freshdirect.delivery.model;

import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;

import com.freshdirect.framework.util.DateUtil;

public class BreakWindow implements Comparable {
	
	public BreakWindow(Date startTime, Date endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}
	private Date startTime;
	private Date endTime;
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Override
	public String toString() {
		return "TimeslotWindow [startTime=" + startTime + ", endTime=" + endTime
				+ "]";
	}
	
	public String format(int i){
		FastDateFormat fdf = FastDateFormat.getInstance("hh:mm a");
		return Integer.toString(i)+" "+DateUtil.getDiffInMinutes(startTime, endTime)+"-minute "+fdf.format(startTime)+"-"+fdf.format(endTime);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
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
		BreakWindow other = (BreakWindow) obj;
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
		return true;
	}

	public boolean isWithinRange(BreakWindow w1) {
		if (w1.getStartTime().compareTo(this.getStartTime()) <=0
				&& w1.getStartTime().compareTo(this.getStartTime())>0) {
			return true;
		}
		return false;
	}
	public boolean overlaps(BreakWindow consolidatedWindow) {
		if(this.getStartTime().equals(consolidatedWindow.getEndTime()))
			return true;
		return false;
	}
	
	public int compare(BreakWindow w1, BreakWindow w2){
		if(w1.getStartTime() != null &&  w2.getStartTime() != null) {
			return w1.getStartTime().compareTo(w2.getStartTime());
		}
		return 0;
	}
	
	@Override
	public int compareTo(Object o) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    
	    if ( this.equals(o)) return EQUAL;
	    
	    if(o instanceof BreakWindow) {
	    	BreakWindow tmpKey = (BreakWindow)o;
	    	
	    	if(this.getStartTime().before(tmpKey.getStartTime())) {
	    		return BEFORE; 
	    	} else {
	    		return AFTER; 
	    	}
	    }

		return 0;
	}	
}
