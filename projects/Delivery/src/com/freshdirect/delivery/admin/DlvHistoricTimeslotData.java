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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.framework.util.QuickDateFormat;

/**
 * @author knadeem
 */
public class DlvHistoricTimeslotData implements Serializable{
	
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
		String day = QuickDateFormat.SHORT_DATE_FORMATTER.format(date);
		if(zoneTimeslots == null){
			return Collections.EMPTY_LIST;
		}
		Set timeslots = (Set)zoneTimeslots.get(day);
		if(timeslots == null){
			return Collections.EMPTY_SET;
		}else{
			return timeslots;
		}
	}
	
	public int getCapacityTotal(String zoneCode, Date date){
		int total = 0;
		Map zoneTimeslots = (Map)this.data.get(zoneCode);
		if(zoneTimeslots == null){
			return total;
		}
		String day = QuickDateFormat.SHORT_DATE_FORMATTER.format(date);
		Set timeslots = (Set)zoneTimeslots.get(day);
		if(timeslots == null){
			return total;
		}else{
			for(Iterator i = timeslots.iterator(); i.hasNext(); ){
				DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
				total += timeslot.getCapacity();
			}
			return total;
		}
	}
	
	/**
	 * @param currentDate
	 * @param zoneCode
	 * @param date
	 * @return
	 */
	public int calculateAllocationTotal(Date currentDate, String zoneCode, Date date){
		int total = 0;
		Map zoneTimeslots = (Map)this.data.get(zoneCode);
		if(zoneTimeslots == null){
			return total;
		}
		String day = QuickDateFormat.SHORT_DATE_FORMATTER.format(date);
		Set timeslots = (Set)zoneTimeslots.get(day);
		if(timeslots == null){
			return total;
		}else{
			Date now = new Date();
			for(Iterator i = timeslots.iterator(); i.hasNext(); ){
				DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
				total += timeslot.calculateCurrentAllocation(now);
			}
			return total;
		}
	}
	public int getBaseCapacityTotal(String zoneCode, Date date){
		int total = 0;
		Map zoneTimeslots = (Map)this.data.get(zoneCode);
		if(zoneTimeslots == null){
			return total;
		}
		String day = QuickDateFormat.SHORT_DATE_FORMATTER.format(date);
		Set timeslots = (Set)zoneTimeslots.get(day);
		if(timeslots == null){
			return total;
		}else{
			for(Iterator i = timeslots.iterator(); i.hasNext(); ){
				DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
				total += timeslot.getBaseCapacity();
			}
			return total;
		}
	}
	
	public int getBaseAllocationTotal(String zoneCode, Date date){
		int total = 0;
		Map zoneTimeslots = (Map)this.data.get(zoneCode);
		if(zoneTimeslots == null){
			return total;
		}
		String day = QuickDateFormat.SHORT_DATE_FORMATTER.format(date);
		Set timeslots = (Set)zoneTimeslots.get(day);
		if(timeslots == null){
			return total;
		}else{
			for(Iterator i = timeslots.iterator(); i.hasNext(); ){
				DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
				total += timeslot.getBaseAllocation();
			}
			return total;
		}
	}
	public int getCTCapacityTotal(String zoneCode, Date date){
		int total = 0;
		Map zoneTimeslots = (Map)this.data.get(zoneCode);
		if(zoneTimeslots == null){
			return total;
		}
		String day = QuickDateFormat.SHORT_DATE_FORMATTER.format(date);
		Set timeslots = (Set)zoneTimeslots.get(day);
		if(timeslots == null){
			return total;
		}else{
			for(Iterator i = timeslots.iterator(); i.hasNext(); ){
				DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
				total += timeslot.getChefsTableCapacity();
			}
			return total;
		}
	}
	
	public int getCTAllocationTotal(String zoneCode, Date date){
		int total = 0;
		Map zoneTimeslots = (Map)this.data.get(zoneCode);
		if(zoneTimeslots == null){
			return total;
		}
		String day = QuickDateFormat.SHORT_DATE_FORMATTER.format(date);
		Set timeslots = (Set)zoneTimeslots.get(day);
		if(timeslots == null){
			return total;
		}else{
			for(Iterator i = timeslots.iterator(); i.hasNext(); ){
				DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
				total += timeslot.getChefsTableAllocation();
			}
			return total;
		}
	}
	
	public int calculateZoneAllocationTotal(Date currentDate, String zoneCode, List days){
		int total = 0;
		Map zoneTimeslots = (Map)this.data.get(zoneCode);
		if(zoneTimeslots == null){
			return total;
		}
		
		for(Iterator i = days.iterator(); i.hasNext();){
			Date date = (Date)i.next();
			String day = QuickDateFormat.SHORT_DATE_FORMATTER.format(date);
			Set timeslots = (Set)zoneTimeslots.get(day);
			if(timeslots != null){
				for(Iterator j = timeslots.iterator(); j.hasNext(); ){
					DlvTimeslotModel timeslot = (DlvTimeslotModel)j.next();
					total += timeslot.calculateCurrentAllocation(currentDate);
				}
			}
		}
		return total;
	}
	
	public int getZoneCapacityTotal(String zoneCode, List days){
		int total = 0;
		Map zoneTimeslots = (Map)this.data.get(zoneCode);
		if(zoneTimeslots == null){
			return total;
		}
	
		for(Iterator i = days.iterator(); i.hasNext();){
			Date date = (Date)i.next();
			String day = QuickDateFormat.SHORT_DATE_FORMATTER.format(date);
			Set timeslots = (Set)zoneTimeslots.get(day);
			if(timeslots != null){
				for(Iterator j = timeslots.iterator(); j.hasNext(); ){
					DlvTimeslotModel timeslot = (DlvTimeslotModel)j.next();
					total += timeslot.getCapacity();
				}
			}
		}
		return total;
	}
	
	private static class TimeslotComparator implements Serializable, Comparator {
	
		public int compare(Object o1, Object o2) {
			DlvTimeslotModel t1 = ((DlvTimeslotModel)o1);
			DlvTimeslotModel t2 = ((DlvTimeslotModel)o2);
			return t1.getStartTimeAsDate().compareTo( t2.getStartTimeAsDate() );
		}
	}
}
