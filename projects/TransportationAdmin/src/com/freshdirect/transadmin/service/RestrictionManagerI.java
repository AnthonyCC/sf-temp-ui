package com.freshdirect.transadmin.service;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.GeoRestriction;

public interface RestrictionManagerI extends BaseManagerI {
	
  	GeoRestriction getGeoRestriction(String id);		//agb

	Collection getGeoRestrictions();

	Collection getGeoRestrictionBoundaries();
	
	public void saveGeoRestriction(GeoRestriction geoRestriction);
	
	public Collection getGeoRestrictionDays(String restrictionId); 
}
