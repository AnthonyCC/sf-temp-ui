package com.freshdirect.delivery.restriction;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.TimeOfDayRange;

public class AlcoholRestriction extends MunicipalityCompositeRestriction {
	private final boolean alcoholRestricted;
	
	public AlcoholRestriction(
		String id,	
		EnumDlvRestrictionCriterion criterion,
		EnumDlvRestrictionReason reason,
		String name,
		String message,
		Date startDate,
		Date endDate,
		EnumDlvRestrictionType type,
		String path, String state, String county, String city, String municipalityId, boolean alcoholRestricted) {
		super(id, criterion, reason, name, message,new DateRange(startDate, endDate),type,path,state,county,city,municipalityId);
		this.alcoholRestricted = alcoholRestricted;
	}

	public boolean isAlcoholRestricted() {
		return alcoholRestricted;
	}

}