package com.freshdirect.transadmin.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.web.model.SpatialBoundary;

public interface SpatialManagerDaoI {
	
	SpatialBoundary getGeoRestrictionBoundary(String code) throws DataAccessException;
	SpatialBoundary getZoneBoundary(String code) throws DataAccessException;
	List matchCommunity(double latitiude, double longitude, String deliveryModel) throws DataAccessException;
}
