package com.freshdirect.transadmin.service;

import java.util.Collection;

import com.freshdirect.transadmin.model.GeoRestriction;

public interface RestrictionManagerI extends BaseManagerI {
	
  	GeoRestriction getGeoRestriction(String id);		//agb

	Collection getGeoRestrictions();


}
