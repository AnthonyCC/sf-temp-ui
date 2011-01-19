package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;

public class FDTimeslotList implements Serializable {
	
	private static final long	serialVersionUID	= -6937768211070595636L;
	
	private final SortedMap<Date,List<FDTimeslot>> timeslotMap = new TreeMap<Date,List<FDTimeslot>>();
	
	public FDTimeslotList( List<FDTimeslot> timeslots, Calendar startCal, Calendar endCal, DlvRestrictionsList restrictions ) {
		Collections.sort( timeslots, TIMESLOT_COMPARATOR );

		List<FDTimeslot> lst;
		for ( FDTimeslot timeslot : timeslots ) {
			lst = timeslotMap.get( timeslot.getBaseDate() );
			if ( lst == null ) {
				lst = new ArrayList<FDTimeslot>();
				lst.add( timeslot );
				timeslotMap.put( timeslot.getBaseDate(), lst );
			} else {
				lst.add( timeslot );
			}
		}
		while ( startCal.before( endCal ) ) {
			lst = timeslotMap.get( startCal.getTime() );
			if ( lst == null || restrictions.isRestricted( EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.CLOSED, startCal.getTime() ) ) {
				timeslotMap.put( startCal.getTime(), Collections.<FDTimeslot> emptyList() );
			}
			startCal.add( Calendar.DATE, 1 );
		}
	}
	
	public boolean hasCapacity() {
		for ( List<FDTimeslot> timeslots : timeslotMap.values() ) {
			for ( FDTimeslot slot : timeslots ) {
				if ( slot.getTotalAvailable() > 0 ) {
					return true;
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
	
	/**
	 * Returns list of list of FDTimeslot.
	 * @return list of list of FDTimeslot
	 */
	public Collection<List<FDTimeslot>> getTimeslots() {
		return timeslotMap.values();
	}
	
	public List<FDTimeslot> getTimeslotsFlat() {
		List<FDTimeslot> slots = new ArrayList<FDTimeslot>();
		
		for ( List<FDTimeslot> list : timeslotMap.values() ) {
			for ( FDTimeslot slot : list ) {
				slots.add( slot );
			}
		}
		return slots;
	}
	
	public List<FDTimeslot> getTimeslotsForDate( Date day ) {
		List<FDTimeslot> lst = timeslotMap.get( day );
		if ( lst != null ) {
			return lst;
		} else {
			return Collections.<FDTimeslot> emptyList();
		}
	}
	
	public FDTimeslot getTimeslot( Date day, Date startTime, Date endTime ) {
		List<FDTimeslot> lst = timeslotMap.get( day );
		if ( lst != null ) {
			for ( FDTimeslot t : lst ) {
				if ( t.isMatching( day, startTime, endTime ) ) {
					return t;
				}
			}
		}
		return null;
	}
	
	public int size() {
		return timeslotMap.size();
	}
	
	public boolean isSelectedTimeslot( Date day, String timeslotId ) {
		List<FDTimeslot> lst = timeslotMap.get( day );
		if ( lst == null ) {
			return false;
		}
		for ( FDTimeslot slot : lst ) {
			if ( slot.getTimeslotId().equals( timeslotId ) ) {
				return true;
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
