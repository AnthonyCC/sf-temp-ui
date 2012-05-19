package com.freshdirect.delivery.planning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.TimeOfDay;

public class DlvAbstractShiftModel extends ModelSupport {

	private String name;
	private TimeOfDay start;
	private TimeOfDay end;
	private List timeslots = new ArrayList();

	public DlvAbstractShiftModel() {
		super();
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public TimeOfDay getStart() {
		return start;
	}

	public void setStart(TimeOfDay startTime) {
		this.start = startTime;
	}

	public TimeOfDay getEnd() {
		return end;
	}

	public void setEnd(TimeOfDay endTime) {
		this.end = endTime;
	}

	/**
	 * @return double duration in hours
	 */
	public double getDuration() {
		return TimeOfDay.getDurationAsHours(this.start, this.end);
	}

	public List getTimeslotsByStartDate() {
		List slots = new ArrayList( this.getTimeslots() );
		Collections.sort( slots, DlvShiftTimeslotModel.COMPARATOR_START_DATE );
		return slots;
	}
	
	public List getTimeslots(){
	   return this.timeslots;
   	}

   	public void setTimeslots(List timeslots){
	   this.timeslots = timeslots;
   	}
   	
   	protected void decorateCopy(DlvAbstractShiftModel c) {
		c.setPK( this.getPK() );
		c.setName(name);
		c.setEnd(end);
		c.setStart(start);
		List l = new ArrayList( this.timeslots.size() );
		for (Iterator i=this.timeslots.iterator(); i.hasNext(); ) {
			l.add( ((DlvShiftTimeslotModel)i.next()).deepCopy() );
		}
		c.setTimeslots(l);
	}
}
