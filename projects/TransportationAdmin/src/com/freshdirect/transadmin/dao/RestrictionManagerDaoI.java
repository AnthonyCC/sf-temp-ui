package com.freshdirect.transadmin.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.GeoRestriction;

public interface RestrictionManagerDaoI extends BaseManagerDaoI {

	Collection getGeoRestrictions() throws DataAccessException;
	
  	GeoRestriction getGeoRestriction(String id) throws DataAccessException;		//agb
  	 	

}