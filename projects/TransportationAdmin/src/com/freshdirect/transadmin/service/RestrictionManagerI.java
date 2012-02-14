package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.web.model.SpatialBoundary;

public interface RestrictionManagerI extends BaseManagerI {
	
  	GeoRestriction getGeoRestriction(String id);		//agb

	Collection getGeoRestrictions();

	Collection getGeoRestrictionBoundaries();
	
	void saveGeoRestriction(GeoRestriction geoRestriction);
	
	Collection getGeoRestrictionDays(String restrictionId); 
	
	SpatialBoundary getGeoRestrictionBoundary(String code);
	
	SpatialBoundary getZoneBoundary(String code);
	
	void deleteGeoRestrictions(Set restrictions);

	List<SpatialBoundary> getNeighbourhoodBoundary(String code);

}
