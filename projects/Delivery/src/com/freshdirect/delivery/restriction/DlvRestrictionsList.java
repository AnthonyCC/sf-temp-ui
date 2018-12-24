package com.freshdirect.delivery.restriction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateRange;

public class DlvRestrictionsList implements Serializable {

	private static final long	serialVersionUID	= -2213868641684699998L;
	
	private final List<RestrictionI> restrictions;

	public DlvRestrictionsList(List<RestrictionI> restrictions) {
		this.restrictions = restrictions;
		Collections.sort(restrictions, new RestrictionTypeComparator());
	}

	public boolean isRestricted(EnumDlvRestrictionCriterion criterion, EnumDlvRestrictionReason reason, Date date) {
		for ( RestrictionI rest : restrictions ) {
			if (rest.getReason().equals(reason) && rest.contains(date)) {
				return true;
			}
		}
		return false;
	}

	public boolean isRestricted(EnumDlvRestrictionCriterion criterion, EnumDlvRestrictionReason reason, DateRange range) {
		
		// need to make sure if this is called then if TKG restricetd day start date is less then adv order start date 
		// then  we should consider adv order start date as TKG start date
		// stupid requirement
		//System.out.println("range111 :"+range);
		
		// For advance orders on holidays we should check the holiday dates fall between the advance order dates
		// The check is for displaying holiday bar on top of timeslot when they fall under horizon
		boolean isAdvOrderGap = FDStoreProperties.IsAdvanceOrderGap();
		if(isAdvOrderGap){
			if(EnumDlvRestrictionReason.THANKSGIVING.equals(reason)){
				DateRange advOrdDateRange = FDStoreProperties.getAdvanceOrderRange();
				DateRange advOrdNewDateRange = FDStoreProperties.getAdvanceOrderNewRange();
				if (((range.getStartDate().after(advOrdDateRange.getStartDate()) || range.getStartDate().equals(advOrdDateRange.getStartDate())) && 
						range.getEndDate().before(advOrdDateRange.getEndDate())) ||
						((range.getStartDate().after(advOrdNewDateRange.getStartDate()) || range.getStartDate().equals(advOrdNewDateRange.getStartDate())) && 
								range.getEndDate().before(advOrdNewDateRange.getEndDate()))) {
				    return false;				
				}else
					return true;
			}
		}else if(EnumDlvRestrictionReason.THANKSGIVING.equals(reason))
		{
			DateRange advOrdDateRange = FDStoreProperties.getAdvanceOrderRange();
			reason=EnumDlvRestrictionReason.THANKSGIVING_MEALS;
			if (range.getStartDate().after(advOrdDateRange.getStartDate()) || range.getStartDate().equals(advOrdDateRange.getStartDate()) ) {
			    //range = new DateRange(advOrdDateRange.getStartDate(),range.getEndDate());			    
			    //System.out.println("New Date Range for bar display"+range);
				return false;				
			}
			else
				return true;
		}
				
		List<RestrictionI> l = getRestrictions(null, reason, null, range);
		return l.size() > 0;
	}

	public List<RestrictionI> getRestrictions(DateRange range) {
		return this.getRestrictions(null, null, null, range);
	}

	public List<RestrictionI> getRestrictions(EnumDlvRestrictionReason reason, DateRange range) {
		return this.getRestrictions(null, reason, null, range);
	}

	public List<RestrictionI> getRestrictions(
		EnumDlvRestrictionCriterion criterion,
		EnumDlvRestrictionReason reason,
		EnumDlvRestrictionType type,
		DateRange range) {
		List<RestrictionI> l = new ArrayList<RestrictionI>();
		for ( RestrictionI rest : restrictions ) {
			if (!rest.isMatching(criterion, reason, type)) {
				continue;
			}
			if (rest.overlaps(range)) {
				l.add(rest);
			}
		}
		return l;
	}

	/**
	 * @return List of all restrictions that contain the specified range  
	 */
	public List<RestrictionI> getRestrictionsContaining(DateRange range) {
		List<RestrictionI> l = new ArrayList<RestrictionI>();
		for ( RestrictionI rest : restrictions ) {
			if (rest.contains(range)) {
				l.add(rest);
			}
		}
		return l;
	}

	/**
	 * @return List of all restrictions that contain the specified range  
	 */
	public List<RestrictionI> getRestrictionsContainingExcept(DateRange range,EnumDlvRestrictionCriterion criterion,EnumDlvRestrictionReason reason) {
		List<RestrictionI> l = new ArrayList<RestrictionI>();
		for ( RestrictionI rest : restrictions ) {
			if ((criterion.equals(rest.getCriterion())) && reason.equals(rest.getReason())) {
				continue;
			}
			if (rest.contains(range)) {
				l.add(rest);
			}
		}
		return l;
	}
	
	public List<RestrictionI> getRestrictionsExcept(DateRange range,EnumDlvRestrictionCriterion criterion,EnumDlvRestrictionReason reason) {
		return this.getRestrictionsExcept( criterion, reason, range);
	}
	
	public List<RestrictionI> getRestrictionsExcept(
			EnumDlvRestrictionCriterion criterion,
			EnumDlvRestrictionReason reason,
			DateRange range) {
			List<RestrictionI> l = new ArrayList<RestrictionI>();
			for ( RestrictionI rest : restrictions ) {
				if ((criterion.equals(rest.getCriterion())) && reason.equals(rest.getReason())) {
					continue;
				}
				if (rest.overlaps(range)) {
					l.add(rest);
				}
			}
			return l;
		}
	
	public List<RestrictionI> getRestrictionsContaining(
			EnumDlvRestrictionCriterion criterion,
			EnumDlvRestrictionReason reason,
			EnumDlvRestrictionType type,
			DateRange range) {
			List<RestrictionI> l = new ArrayList<RestrictionI>();
			for ( RestrictionI rest : restrictions ) {
				if (!rest.isMatching(criterion, reason, type)) {
					continue;
				}
				if (rest.contains(range)) {
					l.add(rest);
				}
			}
			return l;
		}
	
	public List<RestrictionI> getRestrictions(EnumDlvRestrictionReason reason) {
		return this.getRestrictions(reason, null);
	}

	public List<RestrictionI> getRestrictions(EnumDlvRestrictionCriterion criterion, Set<EnumDlvRestrictionReason> reasons) {
		List<RestrictionI> l = new ArrayList<RestrictionI>();
		for ( RestrictionI r  : restrictions ) {
			if ((criterion == null || criterion.equals(r.getCriterion())) && reasons.contains(r.getReason())) {
				l.add(r);
			}
		}
		return l;
	}

	public List<RestrictionI> getRestrictions(EnumDlvRestrictionCriterion criterion, EnumDlvRestrictionReason reason, EnumDlvRestrictionType type) {
		List<RestrictionI> l = new ArrayList<RestrictionI>();
		for ( RestrictionI r : restrictions ) {
			if (!r.isMatching(criterion, reason, type)) {
				continue;
			}
			l.add(r);
		}
		return l;
	}

	public List<RestrictionI> getRestrictions(EnumDlvRestrictionCriterion criterion, Set<EnumDlvRestrictionReason> reasons, DateRange range) {
		List<RestrictionI> l = new ArrayList<RestrictionI>();
		
		List<RestrictionI> tempRestrictions = new ArrayList<RestrictionI>();
		tempRestrictions = this.getRestrictions(range);
		for ( RestrictionI r  : tempRestrictions ) {
			if ((criterion.equals(r.getCriterion())) && reasons.contains(r.getReason())) {
				l.add(r);
			}
		}
		return l;
	}

	public String toString() {
		return this.restrictions.toString();
	}

	
	/**
	 * [APPDEV-2149] helper method for generic time slots
	 */
	public DlvRestrictionsList getRestrictionsForGenericTimeslots() {
		List<RestrictionI> fl = new ArrayList<RestrictionI>();
		
		for (RestrictionI r : this.restrictions) {
			if (EnumDlvRestrictionType.RECURRING_RESTRICTION.equals(r.getType()) && EnumDlvRestrictionCriterion.DELIVERY.equals(r.getCriterion()) ) {
				fl.add(r);
			}
		}

		return  new DlvRestrictionsList(fl);
		/*if (fl.size() < this.restrictions.size())
			this.restrictions = fl;*/
	}

	public int size() {
		return this.restrictions.size();
	}
	
	class RestrictionTypeComparator implements Comparator<RestrictionI>{

		@Override
		public int compare(RestrictionI arg0, RestrictionI arg1) {
			if(null != arg0.getType() && null !=arg1.getType()){
				return arg0.getType().compareTo(arg1.getType());
			}else{
				return 0;
			}
		}
		
	}
}
