package com.freshdirect.transadmin.dao;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.web.model.SpatialBoundary;

public interface SpatialManagerDaoI {
	
	SpatialBoundary getGeoRestrictionBoundary(String code) throws DataAccessException;
	SpatialBoundary getZoneBoundary(String code) throws DataAccessException;
}
