package com.freshdirect.delivery.restriction;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.framework.util.DateRange;

public interface RestrictionI extends Serializable {
	
	public String getId();

	public EnumDlvRestrictionCriterion getCriterion();
	
	public EnumDlvRestrictionType getType();

	public EnumDlvRestrictionReason getReason();

	public boolean isMatching(EnumDlvRestrictionCriterion criterion, EnumDlvRestrictionReason reason, EnumDlvRestrictionType type);

	public String getName();

	public String getMessage();

	public String getDisplayDate();

	/** @return true if restriction is in effect on the specified date */
	public boolean contains(Date date);

	/** @return true if restriction is in effect any time in the specified date range */
	public boolean overlaps(DateRange range);

	/** @return true if restriction fully covers the entire specified date range */
	public boolean contains(DateRange range);

}