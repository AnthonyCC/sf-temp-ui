package com.freshdirect.delivery.admin;
/*
 * Created on Apr 17, 2003
 */

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.framework.util.QuickDateFormat;

/**
 * @author knadeem
 */
public class DlvHistoricTimeslotData implements Serializable {
	
	private static final long serialVersionUID = 6134576405509066681L;
	
	//daily Timeslot data Map -> zoneCode [Map -> day[timeslotList]]
	private Map data;
	
	public DlvHistoricTimeslotData(){
		this.data = new HashMap();
	}
	
	public void addTimeslot(String zoneCode, DlvTimeslotModel timeslot){
		
		Map zoneTimeslots = (Map)data.get(zoneCode);
		String day = QuickDateFormat.SHORT_DATE_FORMATTER.format(timeslot.getBaseDate());
		if(zoneTimeslots == null){
			zoneTimeslots = new TreeMap();
			Set dayTimeslots = getTimeslotCollection();
			dayTimeslots.add(timeslot);
			zoneTimeslots.put(day, dayTimeslots);
			data.put(zoneCode, zoneTimeslots);
		}else{
			Set dayTimeslots = (Set)zoneTimeslots.get(day);
			if(dayTimeslots == null){
				dayTimeslots = getTimeslotCollection();
				dayTimeslots.add(timeslot);
				zoneTimeslots.put(day, dayTimeslots);
			}else{
				dayTimeslots.add(timeslot);
			}
		}
	}
	
	private Set getTimeslotCollection(){
		return new TreeSet(new TimeslotComparator() );
	}
	
	public Collection getTimeslots(String zoneCode, Date date){
		Map zoneTimeslots = (Map)this.data.get(zoneCode);
		if(zoneTimeslots == null){
			return Collections.EMPTY_LIST;
		}
		String day = QuickDateFormat.SHORT_DATE_FORMATTER.format(date);
		Set timeslots = (Set)zoneTimeslots.get(day);
		if(timeslots == null){
			return Collections.EMPTY_SET;
		}else{
			return timeslots;
		}
	}
	
	
	private static class TimeslotComparator implements Serializable, Comparator {
	
		private static final long serialVersionUID = -133798486694767913L;

		public int compare(Object o1, Object o2) {
			DlvTimeslotModel t1 = ((DlvTimeslotModel)o1);
			DlvTimeslotModel t2 = ((DlvTimeslotModel)o2);
			return t1.getStartTimeAsDate().compareTo( t2.getStartTimeAsDate() );
		}
	}
	
	
	// The rest of the methods have been reworked to easier accommodate different views into allocation date
	// @author istvan
	
	/**
	 * Helper template that sums up certain quantities associated with {@link DlvTimeslotModel}s.
	 * @author istvan
	 *
	 */
	private abstract static class Accummulate {
		
		/**
		 * Get the quantity.
		 * @param slot map parameter
		 * @return quantity
		 */
		public abstract int get(DlvTimeslotModel slot);
		
		/**
		 * Calculate the sum of quantities.
		 * 
		 * Note that the argument may contain both {@link DlvTimeslotModel} objects and {@link Map.Entry}<?,{@link DlvTimeslotModel}>
		 * objects. The latter comes from the "timeslot detail view" index.
		 * 
		 * @param zoneTimeSlots Collection of {@link Map.Entry}s (with value {@link DlvTimeslotModel}) or {@link DlvTimeslotModel}s.
		 * @return sum of selected quantity.
		 */
		public int calculate(Collection zoneTimeSlots) {
			int sum = 0;
			for(Iterator i = zoneTimeSlots.iterator(); i.hasNext();) {
				Object o = i.next();
				DlvTimeslotModel timeslot= (DlvTimeslotModel)(o instanceof Map.Entry ? ((Map.Entry)o).getValue() : o);
				sum += get(timeslot);
			}
			
			return sum;
		}
	}
	
	
	// ALL THE METHODS BELOW CALCULATE SUMS OF DIFFERENT METRICS OF TIMESLOTS BUILT ON THE SAME PATTERN
	
	/** Capacity total in timeslots.
	 * 
	 * @param timeslots 
	 * @return sum of {@link DlvTimeslotModel#getCapacity()}
	 */
	public static int getCapacityTotal(Collection timeslots) {
		return new Accummulate() {

			public int get(DlvTimeslotModel slot) {
				return slot.getCapacity();
			}
			
		}.calculate(timeslots);
	}
	
	/**
	 * Allocation total in timeslots on given date.
	 * 
	 * @param timeslots
	 * @param date
	 * @return sum of {@link DlvTimeslotModel#calculateCurrentAllocation(Date) DlvTimslotModel.calculateCurrentAllocation(date)}
	 */
	public static int getAllocationTotal(Collection timeslots, final Date date) {
		return new Accummulate() {

			public int get(DlvTimeslotModel slot) {
				return slot.calculateCurrentAllocation(date);
			}
			
		}.calculate(timeslots);
	}
	
	/**
	 * Base capacity total in timeslots.
	 * @param timeslots
	 * @return sum of {@link DlvTimeslotModel#getBaseCapacity()}
	 */
	public static int getBaseCapacityTotal(Collection timeslots) {
		return new Accummulate() {

			public int get(DlvTimeslotModel slot) {
				return slot.getBaseCapacity();
			}
			
		}.calculate(timeslots);
	}
	
	/**
	 * Base allocation total in timeslots.
	 * 
	 * @param timeslots
	 * @return sum of {@link DlvTimeslotModel#getBaseAllocation()}
	 */
	public static int getBaseAllocationTotal(Collection timeslots) {
		return new Accummulate() {

			public int get(DlvTimeslotModel slot) {
				return slot.getBaseAllocation();
			}
			
		}.calculate(timeslots);
	}
	
	/**
	 * Chefs table capacity total in timeslots.
	 * 
	 * @param timeslots
	 * @return sum of {@link DlvTimeslotModel#getChefsTableCapacity()}
	 */
	public static int getCTCapacityTotal(Collection timeslots) {
		return new Accummulate() {

			public int get(DlvTimeslotModel slot) {
				return slot.getChefsTableCapacity();
			}
			
		}.calculate(timeslots);
	}
	
	/**
	 * Chefs table allocation total.
	 * 
	 * @param timeslots
	 * @return sum of {@link DlvTimeslotModel#getChefsTableAllocation()}
	 */
	public static int getCTAllocationTotal(Collection timeslots) {
		return new Accummulate() {

			public int get(DlvTimeslotModel slot) {
				return slot.getChefsTableAllocation();
			}
			
		}.calculate(timeslots);
	}
	
	/**
	 * Get percentage total in timeslots on given date.
	 * 
	 *  
	 * @param timeslots
	 * @param date
	 * @return {@link #getCTAllocationTotal(Collection) getCTAllocation(timeslots)}/
	 *         {@link #getAllocationTotal(Collection, Date) getAllocationTotal(timeslots,date)} or 0 if no allocations
	 */
	public static double getPercentageTotal(Collection timeslots, final Date date) {
		int currentAllocationTotal = getAllocationTotal(timeslots, date);
				
		if (currentAllocationTotal == 0) return 0;
		
		int CTAllocationTotal = getCTAllocationTotal(timeslots);
		
		return ((double)CTAllocationTotal)/currentAllocationTotal;
	}
}
