package com.freshdirect.fdstore.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;

public class FDTimeslotUtil implements Serializable {
	
	private static final long	serialVersionUID	= -6937768211070595636L;
	
	private final SortedMap<Date,Map<Date,List<FDTimeslot>>> timeslotMap = new TreeMap<Date,Map<Date,List<FDTimeslot>>>();
	private final Set<Date> holidaySet = new HashSet<Date>();
	
	private final Set<FDTimeslot> uniqueTimeslots = new TreeSet<FDTimeslot>();
	
	public FDTimeslotUtil( List<FDTimeslot> timeslots, Calendar startCal, Calendar endCal, DlvRestrictionsList restrictions ) {
		Collections.sort( timeslots, TIMESLOT_COMPARATOR );
		
		for ( FDTimeslot timeslot : timeslots ) {

			List<FDTimeslot> cutOffTimeslotList = null;
			Map<Date,List<FDTimeslot>> cutOffMap = null;
			
			cutOffMap = timeslotMap.get( timeslot.getBaseDate());
			if(null == cutOffMap){
				cutOffMap = new HashMap<Date,List<FDTimeslot>>();
			}else{
				cutOffTimeslotList = cutOffMap.get(timeslot.getCutoffNormalDateTime());
			}
			if(null == cutOffTimeslotList || cutOffTimeslotList.isEmpty()){					
				cutOffTimeslotList = new ArrayList<FDTimeslot>();					
			}
			cutOffTimeslotList.add(timeslot);
			cutOffMap.put(timeslot.getCutoffNormalDateTime(), cutOffTimeslotList);
			timeslotMap.put(timeslot.getBaseDate(),cutOffMap);
			
		}		
		
		Map<Date,List<FDTimeslot>> cutOffMap = null;				
		while ( startCal.before( endCal ) ) {
			cutOffMap = timeslotMap.get( startCal.getTime() );
			if(cutOffMap!=null){
				for(List<FDTimeslot> list : cutOffMap.values()){
					if ( list == null || restrictions.isRestricted( EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.CLOSED, startCal.getTime() ) ) {
						timeslotMap.put( startCal.getTime(), new HashMap<Date,List<FDTimeslot>>());
						holidaySet.add(startCal.getTime());
					}	
				}		
			}else{
				timeslotMap.put( startCal.getTime(), new HashMap<Date,List<FDTimeslot>>());
			}
			
			startCal.add( Calendar.DATE, 1 );
		}
	}
	
	public boolean hasCapacity() {
		for(Iterator<Map<Date, List<FDTimeslot>>> itr = timeslotMap.values().iterator();itr.hasNext();){
			Map<Date, List<FDTimeslot>> mapCutOff  = itr.next();
			for ( List<FDTimeslot> timeslots : mapCutOff.values() ) {
				for ( FDTimeslot slot : timeslots ) {
					if ( slot.getTotalAvailable() > 0 ) {
						return true;
					}
				}
			}	
		}		
		return false;
	}
	
	public boolean isKosherSlotAvailable( DlvRestrictionsList restrictions ) {
		for ( Date day : timeslotMap.keySet() ) {
			if ( !restrictions.isRestricted( null, EnumDlvRestrictionReason.KOSHER, day ) ) {
				return false;
			}
		}
		return true;
	}
	
	public Date getStartDate() {
		return timeslotMap.firstKey();
	}
	
	public Date getEndDate() {
		return timeslotMap.lastKey();
	}
	
	public Collection<Date> getDays() {
		return timeslotMap.keySet();
	}
	
	public Set<FDTimeslot> getUniqueSlots() {
		return uniqueTimeslots;
	}
	
	public Set<FDTimeslot> setUniqueSlots(Set<FDTimeslot> uniquieSlots) {
		uniqueTimeslots.addAll(uniquieSlots);
		return uniqueTimeslots;
	}

	public Collection<Date> getHolidays() {
		return holidaySet;
	}
	
	public Set<Date> getCutoffs(Date day) {
		
		Map<Date, List<FDTimeslot>> map = timeslotMap.get( day );
		if(map.isEmpty()){
			return (Set<Date>) Collections.<Date> emptySet(); 
		}else{			
			return map.keySet();
		}			
	}
	
	public Set<Date> getUniqueCutoffs() {
		SortedSet<Date> uniqueCutOffs = new TreeSet<Date>();
			
		for(Iterator<Map<Date, List<FDTimeslot>>> itr = timeslotMap.values().iterator();itr.hasNext();){
			Map<Date, List<FDTimeslot>> mapCutOff  = (Map<Date, List<FDTimeslot>>)itr.next();
			for (Iterator<Date> iterator = mapCutOff.keySet().iterator(); iterator.hasNext();) {
				Date date = (Date) iterator.next();	
				if(!uniqueCutOffs.contains(date)){
					uniqueCutOffs.add(date);
				}
			}
		}
		return uniqueCutOffs;		
	}
	
	public Set<Date> getWeekCuttOffs() {
		Collection<Date> days = this.getDays();
		Set<Date> cutoffs = new HashSet<Date>();   
		for (Iterator<Date> itr = days.iterator(); itr.hasNext();) {
			Date day = itr.next();
			Map<Date, List<FDTimeslot>> map = timeslotMap.get( day );
			if(map.isEmpty()){
				 
			}else{				
				cutoffs.addAll(map.keySet());			
			}	
		}		
		return cutoffs;				
	}
	/**
	 * Returns list of list of FDTimeslot.
	 * @return list of list of FDTimeslot
	 */
	public Collection<List<FDTimeslot>> getTimeslots() {
		List<List<FDTimeslot>> timeslots = new ArrayList<List<FDTimeslot>>(); 
		for(Iterator<Map<Date, List<FDTimeslot>>> itr = timeslotMap.values().iterator();itr.hasNext();){
			Map<Date, List<FDTimeslot>> tempMap = itr.next();
			timeslots.addAll(tempMap.values());
		}
		return timeslots;
	}
	
	
	public List<FDTimeslot> getTimeslotsFlat() {
		List<FDTimeslot> slots = new ArrayList<FDTimeslot>();
		for(Iterator<Map<Date, List<FDTimeslot>>> itr = timeslotMap.values().iterator();itr.hasNext();){
			Map<Date, List<FDTimeslot>> mapCutOff  = itr.next();
			for ( List<FDTimeslot> list : mapCutOff.values() ) {
				for ( FDTimeslot slot : list ) {
					slots.add( slot );
				}
			}		
		}		
		return slots;
	}
	
	public List<FDTimeslot> getTimeslotsForDate( Date day ) {
		List<FDTimeslot> slots = new ArrayList<FDTimeslot>();
		Map<Date, List<FDTimeslot>> mapCutOff  = timeslotMap.get( day );
		for ( List<FDTimeslot> list : mapCutOff.values()) {
				for ( FDTimeslot slot : list ) {
					slots.add( slot );
				}
		}
		
		if ( slots != null ) {
			return slots;
		} else {
			return Collections.<FDTimeslot> emptyList();
		}
	}
	public RestrictionI getHolidayRestrictionForDate( Date day ) {
		
		Calendar begCal = Calendar.getInstance();
		begCal.setTime(day);
		begCal = DateUtil.truncate(begCal);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(day); 
		endCal.add(Calendar.DATE, 1);
		endCal = DateUtil.truncate(endCal);
		try {
			DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
			List<RestrictionI> r = restrictions.getRestrictions(EnumDlvRestrictionReason.CLOSED, new DateRange(begCal.getTime(),endCal.getTime()));
			if ( r != null ) {
				return r.get(0);
			} else {
				return null;
			}
		} catch (FDResourceException e) {			
			e.printStackTrace();
		}
		return null;
	}
	public FDTimeslot getTimeslot( Date day, Date startTime, Date endTime ) {
		
		Map<Date, List<FDTimeslot>> mapCutOff  = timeslotMap.get( day );
		for ( List<FDTimeslot> lst : mapCutOff.values()) {
			if ( lst != null ) {
				for ( FDTimeslot slot : lst ) {
					if ( slot.isMatching( day, startTime, endTime ) ) {
						return slot;
					}			
				}
			}
		}	
		return null;
	}
	
	public int size() {
		return timeslotMap.size();
	}	
	
	public List<FDTimeslot> getTimeslotsForDayAndCuttoff( Date day, Date cutoffTime ) {
		
		Map<Date,List<FDTimeslot>> tempMap = timeslotMap.get(day);
		if(tempMap.isEmpty()){
			return Collections.<FDTimeslot> emptyList();
		}else{
			List<FDTimeslot> lst = tempMap.get( cutoffTime );
			if ( lst != null ) {
				return lst;
			} else {
				return Collections.<FDTimeslot> emptyList();
			}
		}
	}
	
	public int getNoTimeslotsForDayAndCuttoff( Date day, Date cutoffTime ) {
		
		Map<Date,List<FDTimeslot>> tempMap= timeslotMap.get(day);
		if(tempMap.isEmpty()){
			return 0;
		}else{
			List<FDTimeslot> lst = tempMap.get( cutoffTime );
			if ( lst != null ) {
				return lst.size();
			} else {
				return 0;
			}
		}
	}
	public int getMaxTimeslotsForWeekCutoff(Date cutoff) {
		Collection<Date> days = this.getDays();
		int maxTimeslots = 0; 
		for (Iterator<Date> iterator = days.iterator(); iterator.hasNext();) {
			Date day = iterator.next();
			List<FDTimeslot> timeslots = this.getTimeslotsForDayAndCuttoff(day, cutoff);
			if(timeslots.size()>=maxTimeslots)
				maxTimeslots = timeslots.size();					
		}
		
		return maxTimeslots;
	}
	
	public boolean isSelectedTimeslot( Date day, String timeslotId ) {
		
		Map<Date, List<FDTimeslot>> mapCutOff  = timeslotMap.get( day );
		for ( List<FDTimeslot> lst : mapCutOff.values()) {
			if ( lst != null ) {
				for ( FDTimeslot slot : lst ) {
					if ( slot.getTimeslotId().equals(timeslotId) ) {
						return true;
					}			
				}
			}
		}	
		return false;
	}
	
	public FDTimeslot getTimeslotForDayAndTime( Date day, FDTimeslot slot ) {
		List<FDTimeslot> lst = this.getTimeslotsForDate(day);
		if ( lst != null ) {
			Collections.sort( lst, TIMESLOT_COMPARATOR );
			for ( FDTimeslot t : lst ) {
				Calendar startTimeCal = DateUtil.toCalendar(slot.getBegDateTime());
				int startHour = startTimeCal.get(Calendar.HOUR_OF_DAY);
				
				Calendar tempStartTimeCal = DateUtil.toCalendar(t.getBegDateTime());
				int tempStartHour = tempStartTimeCal.get(Calendar.HOUR_OF_DAY);					
					
				if((startHour == tempStartHour)){
					DlvTimeslotModel ts = t.getDlvTimeslot();
					if(ts.getCapacity()>0)
						return t;
				}
			}
		}
		return null;
	}

	public int getTimeslotIndexForDay( Date day, FDTimeslot slot ) {
		boolean isMatching  = false;
		Map<Date, List<FDTimeslot>> mapCutOff  = timeslotMap.get( day );
		for ( List<FDTimeslot> lst : mapCutOff.values()) {
			Collections.sort( lst, TIMESLOT_COMPARATOR );
			for(FDTimeslot _slot:lst){
				if(_slot.getTimeslotId().equals(slot.getTimeslotId())){
					isMatching = true;
					break;					
				}
			}
			if(isMatching)
				return lst.indexOf(slot);
		}	
		
		return -1;
	}
	
	private final static Comparator<FDTimeslot> TIMESLOT_COMPARATOR = new Comparator<FDTimeslot>() {
		public int compare( FDTimeslot t1, FDTimeslot t2 ) {
			return t1.getBegDateTime().compareTo(t2.getBegDateTime());
		}
	};
	
}
