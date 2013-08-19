package com.freshdirect.transadmin.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.model.TimeslotRestriction;

public interface RestrictionManagerDaoI extends BaseManagerDaoI {

	Collection getGeoRestrictions() throws DataAccessException;
	
  	GeoRestriction getGeoRestriction(String id) throws DataAccessException;		//agb

  	Collection getGeoRestrictionBoundaries()  throws DataAccessException;

  	public void saveGeoRestriction(GeoRestriction geoRestriction) throws DataAccessException;
  	
  	public Collection getGeoRestrictionDays(String restrictionId)  throws DataAccessException ;

	TimeslotRestriction getTimeslotRestriction(String id) throws DataAccessException;

	Collection getTimeslotRestrictions() throws DataAccessException;

	void saveTimeslotRestriction(TimeslotRestriction tsRestriction) throws DataAccessException;
  	
}