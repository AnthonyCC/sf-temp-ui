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
import java.util.TreeMap;
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

	private final SortedMap<Date, Map<String, List<FDTimeslot>>> timeslotMap = new TreeMap<Date, Map<String, List<FDTimeslot>>>();
	private final Set<Date> holidaySet = new HashSet<Date>();
	
	public FDTimeslotUtil( List<FDTimeslot> timeslots, Calendar startCal, Calendar endCal, DlvRestrictionsList restrictions ) {
		Collections.sort( timeslots, TIMESLOT_COMPARATOR );
		
		for ( FDTimeslot timeslot : timeslots ) {
			
			List<FDTimeslot> shiftTimeslotList = null;
			Map<String,List<FDTimeslot>> shiftMap = null;
			
			shiftMap = timeslotMap.get(timeslot.getBaseDate());
			if(null == shiftMap){
				shiftMap = new HashMap<String, List<FDTimeslot>>();
			}else{
				shiftTimeslotList = shiftMap.get(timeslot.getTimeslotShift());
			}
			
			if(null == shiftTimeslotList || shiftTimeslotList.isEmpty()){
				shiftTimeslotList = new ArrayList<FDTimeslot>();
			}
			shiftTimeslotList.add(timeslot);
			shiftMap.put(timeslot.getTimeslotShift(), shiftTimeslotList);
			timeslotMap.put(timeslot.getBaseDate(), shiftMap);
			
		}
		
		Map<String,List<FDTimeslot>> shiftMap = null;
		while ( startCal.before( endCal ) ) {
			shiftMap = timeslotMap.get( startCal.getTime() );
			if(shiftMap!=null){
				for(List<FDTimeslot> list : shiftMap.values()){
					if ( list == null || restrictions.isRestricted( EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.CLOSED, startCal.getTime() ) ) {
						timeslotMap.put( startCal.getTime(), new HashMap<String,List<FDTimeslot>>());
						holidaySet.add(startCal.getTime());
					}	
				}		
			}else{
				timeslotMap.put( startCal.getTime(), new HashMap<String,List<FDTimeslot>>());
			}			
			startCal.add( Calendar.DATE, 1 );
		}		
	}
	
	public boolean hasCapacity() {
		for(Iterator<Map<String, List<FDTimeslot>>> itr = timeslotMap.values().iterator();itr.hasNext();){
			Map<String, List<FDTimeslot>> mapCutOff  = itr.next();
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

	public Collection<Date> getHolidays() {
		return holidaySet;
	}
	
	/**
	 * Returns list of list of FDTimeslot.
	 * @return list of list of FDTimeslot
	 */	
	public Collection<List<FDTimeslot>> getTimeslots() {
		List<List<FDTimeslot>> timeslots = new ArrayList<List<FDTimeslot>>(); 
		for(Iterator<Map<String, List<FDTimeslot>>> itr = timeslotMap.values().iterator();itr.hasNext();){
			Map<String, List<FDTimeslot>> tempMap = itr.next();
			timeslots.addAll(tempMap.values());
		}
		return timeslots;
	}
	
	public List<FDTimeslot> getTimeslotsFlat() {
		List<FDTimeslot> slots = new ArrayList<FDTimeslot>();
		for(Iterator<Map<String, List<FDTimeslot>>> itr = timeslotMap.values().iterator();itr.hasNext();){
			Map<String, List<FDTimeslot>> mapShift  = itr.next();
			for ( List<FDTimeslot> list : mapShift.values() ) {
				for ( FDTimeslot slot : list ) {
					slots.add( slot );
				}
			}		
		}		
		return slots;
	}
	
	public List<FDTimeslot> getTimeslotsForDate( Date day ) {
		List<FDTimeslot> slots = new ArrayList<FDTimeslot>();
		Map<String, List<FDTimeslot>> mapShift  = timeslotMap.get( day );
		for ( List<FDTimeslot> list : mapShift.values()) {
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
	
	public Date getMaxCutoffForDate( Date day ) {
		Date cutOff = null;
		List<Date> cTimes = null;
		try {
			cTimes = FDDeliveryManager.getInstance().getCutofftimesByDate(day);
			Collections.sort(cTimes);
			for (Date _cutoff : cTimes) {
				cutOff = _cutoff;
			}

			if (cutOff != null) {
				Calendar requestedDate = Calendar.getInstance();
				requestedDate.setTime(day);
				requestedDate.add(Calendar.DATE, -1);

				Calendar timeDate = Calendar.getInstance();
				timeDate.setTime(cutOff);
				timeDate.set(Calendar.MONTH, requestedDate.get(Calendar.MONTH));
				timeDate.set(Calendar.DATE, requestedDate.get(Calendar.DATE));
				timeDate.set(Calendar.YEAR, requestedDate.get(Calendar.YEAR));
				cutOff = timeDate.getTime();
			}
		} catch (FDResourceException e) {
			e.printStackTrace();
		}

		return cutOff;
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
		
		Map<String, List<FDTimeslot>> shiftMap  = timeslotMap.get( day );
		for ( List<FDTimeslot> lst : shiftMap.values()) {
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
	
	public List<FDTimeslot> getTimeslotsForDayAndShift( Date day, String shift ) {
		
		Map<String,List<FDTimeslot>> tempMap = timeslotMap.get(day);
		if(tempMap.isEmpty()){
			return Collections.<FDTimeslot> emptyList();
		}else{
			List<FDTimeslot> lst = tempMap.get( shift );
			if ( lst != null ) {
				return lst;
			} else {
				return Collections.<FDTimeslot> emptyList();
			}
		}
	}
	
	public int getNumDayShiftTimeslots( Date day, String shift ) {
		
		Map<String,List<FDTimeslot>> tempMap= timeslotMap.get(day);
		if(tempMap.isEmpty()){
			return 0;
		}else{
			List<FDTimeslot> lst = tempMap.get( shift );
			if ( lst != null ) {
				return lst.size();
			} else {
				return 0;
			}
		}
	}

	public int getMaxNumShiftTimeslots(String shift) {
		Collection<Date> days = this.getDays();
		int maxTimeslots = 0; 
		for (Iterator<Date> iterator = days.iterator(); iterator.hasNext();) {
			Date day = iterator.next();
			List<FDTimeslot> timeslots = this.getTimeslotsForDayAndShift(day, shift);
			if(timeslots.size()>=maxTimeslots)
				maxTimeslots = timeslots.size();					
		}
		
		return maxTimeslots;
	}
	
	public boolean isSelectedTimeslot( Date day, String timeslotId ) {
		
		Map<String, List<FDTimeslot>> mapCutOff  = timeslotMap.get( day );
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
	
	private final static Comparator<FDTimeslot> TIMESLOT_COMPARATOR = new Comparator<FDTimeslot>() {
		public int compare( FDTimeslot t1, FDTimeslot t2 ) {
			return t1.getBegDateTime().compareTo(t2.getBegDateTime());
		}
	};
	
}
