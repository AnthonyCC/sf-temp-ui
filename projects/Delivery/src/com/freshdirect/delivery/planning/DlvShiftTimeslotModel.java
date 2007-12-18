package com.freshdirect.delivery.planning;

import java.util.Comparator;

import com.freshdirect.delivery.EnumTimeslotStatus;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.TimeOfDay;

/**
 * User: knadeem
 * Date: Dec 26, 2002
 * Time: 4:27:07 PM
 * File: DlvShiftTimeslot
 */
public class DlvShiftTimeslotModel  extends ModelSupport {

	public final static Comparator COMPARATOR_START_DATE = new Comparator() {
		public int compare(Object o1, Object o2) {
			TimeOfDay t1 = ((DlvShiftTimeslotModel)o1).getStartTime();
			TimeOfDay t2 = ((DlvShiftTimeslotModel)o2).getStartTime();
			if (t1==null) return 1;
			if (t2==null) return -1;
			return t1.getAsDate().compareTo( t2.getAsDate() );
		}
	};
	public DlvShiftTimeslotModel(){
		super();
	}
	
    private TimeOfDay startTime;
    private TimeOfDay endTime;
    private TimeOfDay cutoffTime;
    private EnumTimeslotStatus status;
    private double trafficFactor;

	public TimeOfDay getCutoffTime() {
		return cutoffTime;
	}

	public TimeOfDay getEndTime() {
		return endTime;
	}

	public TimeOfDay getStartTime() {
		return startTime;
	}

	public void setCutoffTime(TimeOfDay cutoffTime) {
		this.cutoffTime = cutoffTime;
	}

	public void setEndTime(TimeOfDay endTime) {
		this.endTime = endTime;
	}

	public void setStartTime(TimeOfDay startTime) {
		this.startTime = startTime;
	}

	public EnumTimeslotStatus getStatus() {
		return status;
	}

	public void setStatus(EnumTimeslotStatus status) {
		this.status = status;
	}
	
	public double getTrafficFactor() {
		return trafficFactor;
	}

	public void setTrafficFactor(double trafficFactor) {
		this.trafficFactor = trafficFactor;
	}

	public String toString() {
		return "DlvShiftTimesotModel["+this.startTime+","+this.endTime+": "+this.getPK()+"]";
	}
	
	/**
	 * @return double duration in hours
	 */
	public double getDuration() {
		return TimeOfDay.getDurationAsHours(startTime, endTime);
	}

}
