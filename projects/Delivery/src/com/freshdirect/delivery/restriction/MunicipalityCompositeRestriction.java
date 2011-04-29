package com.freshdirect.delivery.restriction;

import java.util.List;
import java.util.Map;

import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.TimeOfDayRange;

public class MunicipalityCompositeRestriction extends CompositeRestriction {
	private final String state;
	private final String county;
	private final String city;
	private final String municipalityId;
	
	public MunicipalityCompositeRestriction(
		String id,	
		EnumDlvRestrictionCriterion criterion,
		EnumDlvRestrictionReason reason,
		String name,
		String message,
		DateRange dateRange,
		EnumDlvRestrictionType type,
		String path, String state, String county, String city, String municipalityId) {
		super(id, criterion, reason, name, message,dateRange,type,path);
		this.state = state;
		this.county = county;
		this.city = city;
		this.municipalityId = municipalityId;
	}

	public String getState() {
		return state;
	}

	public String getCounty() {
		return county;
	}

	public String getCity() {
		return city;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}
	
}