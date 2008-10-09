package com.freshdirect.delivery.restriction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateRange;

public class DlvRestrictionsList implements Serializable {

	private final List restrictions;

	public DlvRestrictionsList(List restrictions) {
		this.restrictions = restrictions;
	}

	public boolean isRestricted(EnumDlvRestrictionCriterion criterion, EnumDlvRestrictionReason reason, Date date) {
		for (Iterator i = this.restrictions.iterator(); i.hasNext();) {
			RestrictionI rest = (RestrictionI) i.next();
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
					
		if(EnumDlvRestrictionReason.THANKSGIVING.equals(reason))
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
				
		List l = getRestrictions(null, reason, null, range);
		return l.size() > 0;
	}

	public List getRestrictions(DateRange range) {
		return this.getRestrictions(null, null, null, range);
	}

	public List getRestrictions(EnumDlvRestrictionReason reason, DateRange range) {
		return this.getRestrictions(null, reason, null, range);
	}

	public List getRestrictions(
		EnumDlvRestrictionCriterion criterion,
		EnumDlvRestrictionReason reason,
		EnumDlvRestrictionType type,
		DateRange range) {
		List l = new ArrayList();
		for (Iterator i = this.restrictions.iterator(); i.hasNext();) {
			RestrictionI rest = (RestrictionI) i.next();
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
	public List getRestrictionsContaining(DateRange range) {
		List l = new ArrayList();
		for (Iterator i = this.restrictions.iterator(); i.hasNext();) {
			RestrictionI rest = (RestrictionI) i.next();
			if (rest.contains(range)) {
				l.add(rest);
			}
		}
		return l;
	}

	public List getRestrictions(EnumDlvRestrictionReason reason) {
		return this.getRestrictions(reason, null);
	}

	public List getRestrictions(EnumDlvRestrictionCriterion criterion, Set reasons) {
		List l = new ArrayList();
		for (Iterator i = this.restrictions.iterator(); i.hasNext();) {
			RestrictionI r = (RestrictionI) i.next();
			if ((criterion == null || criterion.equals(r.getCriterion())) && reasons.contains(r.getReason())) {
				l.add(r);
			}
		}
		return l;
	}

	public List getRestrictions(EnumDlvRestrictionCriterion criterion, EnumDlvRestrictionReason reason, EnumDlvRestrictionType type) {
		List l = new ArrayList();
		for (Iterator i = this.restrictions.iterator(); i.hasNext();) {
			RestrictionI r = (RestrictionI) i.next();
			if (!r.isMatching(criterion, reason, type)) {
				continue;
			}
			l.add(r);
		}
		return l;
	}

	public String toString() {
		return this.restrictions.toString();
	}

}
