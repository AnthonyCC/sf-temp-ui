package com.freshdirect.delivery.planning;

import java.util.*;

import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.TimeOfDay;

/**
 * User: knadeem
 * Date: Dec 26, 2002
 * Time: 4:39:16 PM
 * File: DlvTrafficFactor
 */
public class DlvTrafficFactor extends ModelSupport{

	public static class Period implements Comparable {
		private final TimeOfDay start;
		private final double trafficFactor;
		
		/**
		 * @param startTime "hh:mm AM"
		 */
		public Period(TimeOfDay startTime, double trafficFactor) {
			this.start = startTime;
			this.trafficFactor = trafficFactor;
		}

		public TimeOfDay getStart() {
			return start;
		}

		public double getTrafficFactor() {
			return trafficFactor;
		}
		
		/**
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(Object o) {
			return this.start.compareTo( ((Period)o).getStart() );
		}

	}

	public static class Interval extends Period {
		private final TimeOfDay end;

		public Interval(TimeOfDay start, TimeOfDay end, double trafficFactor) {
			super(start, trafficFactor);
			this.end = end;			
		}

		public boolean isWithinExclusive(TimeOfDay time) {
			return !time.before(this.getStart()) && time.before(end);		
		}

		public boolean isWithinInclusive(TimeOfDay time) {
			return !time.before(this.getStart()) && !time.after(end);
		}		
		
		public TimeOfDay getEnd(){
			return this.end;
		}
		
		public String toString() {
			return "From "+this.getStart()+" to "+this.getEnd();
		}
	}

    private Interval[] traffic;


	public Period[] getTraffic() {
		return traffic;
	}

	public void setTraffic(Period[] periods) {
		
		if (periods.length==0) {
			throw new IllegalArgumentException("No periods");			
		}
		Arrays.sort(periods);

		List intervals = new ArrayList();
		if (!periods[0].getStart().equals(TimeOfDay.MIDNIGHT)) {
			intervals.add(new Interval(
				TimeOfDay.MIDNIGHT,
				periods[0].getStart(),
				periods[periods.length-1].getTrafficFactor()
			));
		}
		
		for (int i=0; i<periods.length; i++) {
			TimeOfDay e = (i+1 < periods.length) ? 
				new TimeOfDay( periods[i+1].getStart().getAsString() ) : 
				TimeOfDay.NEXT_MIDNIGHT;
			intervals.add( new Interval( periods[i].getStart(), e, periods[i].getTrafficFactor() ) );
		}
		this.traffic = (Interval[])intervals.toArray( new Interval[intervals.size()]);
	}
    
	public double getTrafficFactor(DlvTimeslotModel timeslot) {
		return this.getTrafficFactor( timeslot.getStartTime(), timeslot.getEndTime() );
	}

	// !!! broken if there's more than two periods between start & end... no big deal...
    protected double getTrafficFactor(TimeOfDay start, TimeOfDay end) {
    	Interval startPeriod = null;
    	for(int i = 0; i < traffic.length; i++){
			startPeriod = traffic[i];
    		if(startPeriod.isWithinExclusive(start)){
    			break;	
    		}
    	}
    	
		Interval endPeriod = null;
		for(int i = 0; i < traffic.length; i++){
			endPeriod = traffic[i];
			if(endPeriod.isWithinInclusive(end)){
				break;
			}
		}

		return Math.min( startPeriod.getTrafficFactor(), endPeriod.getTrafficFactor() );
    }

}
