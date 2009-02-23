package com.freshdirect.transadmin.service.impl;

import java.util.Collection;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.RestrictionManagerDaoI;
import com.freshdirect.transadmin.dao.SpatialManagerDaoI;
import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.service.RestrictionManagerI;
import com.freshdirect.transadmin.web.model.SpatialBoundary;

public class RestrictionManagerImpl extends BaseManagerImpl implements RestrictionManagerI {
	
	private RestrictionManagerDaoI restrictionManagerDao = null;
	
	private SpatialManagerDaoI spatialManagerDao = null;
	
	protected BaseManagerDaoI getBaseManageDao() {
		return getRestrictionManagerDao();
	}
	
	public RestrictionManagerDaoI getRestrictionManagerDao() {
		return restrictionManagerDao;
	}


	public void setRestrictionManagerDao(
			RestrictionManagerDaoI restrictionManagerDao) {
		this.restrictionManagerDao = restrictionManagerDao;
	}

	public GeoRestriction getGeoRestriction(String id) {
		return getRestrictionManagerDao().getGeoRestriction(id);
	}

	public Collection getGeoRestrictions() {
		// TODO Auto-generated method stub
		// first get the kornos data
		// then get the role for the kornos data
		//then construct the viewer model
		// return the viewer model 
		
		
		return getRestrictionManagerDao().getGeoRestrictions();
		
	}

	public Collection getGeoRestrictionBoundaries() {
		// TODO Auto-generated method stub
		return getRestrictionManagerDao().getGeoRestrictionBoundaries();
	}

	public void saveGeoRestriction(GeoRestriction geoRestriction) {
		// TODO Auto-generated method stub
		getRestrictionManagerDao().saveGeoRestriction(geoRestriction);
	}

	public Collection getGeoRestrictionDays(String restrictionId) {
		// TODO Auto-generated method stub
		return getRestrictionManagerDao().getGeoRestrictionDays(restrictionId);
	}

	public SpatialManagerDaoI getSpatialManagerDao() {
		return spatialManagerDao;
	}

	public void setSpatialManagerDao(SpatialManagerDaoI spatialManagerDao) {
		this.spatialManagerDao = spatialManagerDao;
	}

	public SpatialBoundary getGeoRestrictionBoundary(String code) {
		return this.getSpatialManagerDao().getGeoRestrictionBoundary(code);
	}
	
	public SpatialBoundary getZoneBoundary(String code) {
		return this.getSpatialManagerDao().getZoneBoundary(code);
	}
	
}	
	

