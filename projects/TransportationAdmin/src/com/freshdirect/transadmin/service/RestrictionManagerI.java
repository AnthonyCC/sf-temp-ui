package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.model.TimeslotRestriction;
import com.freshdirect.transadmin.web.model.SpatialBoundary;

public interface RestrictionManagerI extends BaseManagerI {
	
  	GeoRestriction getGeoRestriction(String id);		//agb

	Collection getGeoRestrictions();

  	TimeslotRestriction getTimeslotRestriction(String id);		//agb

	Collection getTimeslotRestrictions();
	
	void saveTimeslotRestriction(TimeslotRestriction tsRestriction);
	
	Collection getGeoRestrictionBoundaries();
	
	void saveGeoRestriction(GeoRestriction geoRestriction);
	
	Collection getGeoRestrictionDays(String restrictionId); 
	
	SpatialBoundary getGeoRestrictionBoundary(String code);
	
	SpatialBoundary getZoneBoundary(String code, Date startDate);
	
	void deleteGeoRestrictions(Set restrictions);
	
	void deleteTimeslotRestrictions(Set restrictions);

	List<SpatialBoundary> getSectorBoundary(String code);

}
