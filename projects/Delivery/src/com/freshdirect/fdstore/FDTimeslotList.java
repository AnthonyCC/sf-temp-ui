package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;

public class FDTimeslotList implements Serializable{
	
	private final SortedMap timeslotMap = new TreeMap();
	
	public FDTimeslotList(List timeslots, Calendar startCal, Calendar endCal, DlvRestrictionsList restrictions){
		Collections.sort(timeslots, TIMESLOT_COMPARATOR);
		
		List lst;
		for(Iterator i = timeslots.iterator(); i.hasNext();){
			FDTimeslot timeslot = (FDTimeslot)i.next();
			lst = (List)timeslotMap.get(timeslot.getBaseDate());
			if(lst == null){
				lst = new ArrayList();
				lst.add(timeslot);
				timeslotMap.put(timeslot.getBaseDate(), lst);
			}else{
				lst.add(timeslot);
			}
		}
		while(startCal.before(endCal)){
			lst = (List)timeslotMap.get(startCal.getTime());
			if(lst == null || restrictions.isRestricted(EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.CLOSED, startCal.getTime())){
				timeslotMap.put(startCal.getTime(), Collections.EMPTY_LIST);
			}
			startCal.add(Calendar.DATE, 1);
		}
	}
	
	public boolean hasCapacity(){
		
		for(Iterator i = timeslotMap.values().iterator(); i.hasNext(); ){
			List timeslots = (List)i.next();
			for(Iterator j = timeslots.iterator(); j.hasNext(); ){
				FDTimeslot slot = (FDTimeslot)j.next();
				if (slot.getTotalAvailable()>0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isKosherSlotAvailable(DlvRestrictionsList restrictions) {
		for(Iterator i = this.timeslotMap.keySet().iterator(); i.hasNext(); ){
			Date day = (Date)i.next();
			if (!restrictions.isRestricted(null, EnumDlvRestrictionReason.KOSHER, day)) {
				return false;
			}
		}
		return true;
	}
	
	public Date getStartDate(){
		return (Date)this.timeslotMap.firstKey();
	}
	
	public Date getEndDate(){
		return (Date)this.timeslotMap.lastKey();
	}
	
	public Collection getDays(){
		return this.timeslotMap.keySet();
	}
	
	/**
	 * Returns list of list of FDTimeslot.
	 * @return list of list of FDTimeslot
	 */
	public Collection getTimeslots(){
		return this.timeslotMap.values();
	}
	
	public List getTimeslotsForDate(Date day){
		List lst = (List)this.timeslotMap.get(day);
		if(lst != null){
			return lst;
		} else {
			return Collections.EMPTY_LIST;
		}
	}
	
	public FDTimeslot getTimeslot(Date day, Date startTime, Date endTime) {
	    List lst = (List)this.timeslotMap.get(day);
	    if(lst != null){
	        for(Iterator i = lst.iterator(); i.hasNext(); ){
	            FDTimeslot t = (FDTimeslot) i.next();
	            if(t.isMatching(day, startTime, endTime)){
	                return t;
	            }
	        }
	    }
	    return null;
	}
	
	public int size() {
		return this.timeslotMap.size();
	}
	
	public boolean isSelectedTimeslot(Date day, String timeslotId){
		List lst = (List)this.timeslotMap.get(day);
		if(lst == null){
			return false;
		}
		for(Iterator i = lst.iterator(); i.hasNext(); ){
			FDTimeslot slot = (FDTimeslot) i.next();
			if(slot.getTimeslotId().equals(timeslotId)){
				return true;
			}
		}
		return false;
	}
	
	private final static Comparator TIMESLOT_COMPARATOR = new Comparator() {
		public int compare(Object obj1, Object obj2) {
			FDTimeslot t1 = (FDTimeslot)obj1;
			FDTimeslot t2 = (FDTimeslot)obj2;
			return t1.getBegDateTime().compareTo(t2.getBegDateTime());
		}
	};
}
