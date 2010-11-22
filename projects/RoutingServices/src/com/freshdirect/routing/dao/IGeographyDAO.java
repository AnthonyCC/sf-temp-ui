package com.freshdirect.routing.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IZoneModel;

public interface IGeographyDAO {
	
	public ILocationModel getLocation(String street, String apartment, List zipCodes) throws SQLException;
	
	public String getLocationId() throws SQLException;
	
	public void insertLocations(List dataList) throws SQLException;
	
	IGeographicLocation getLocalGeocode(String srubbedStreet, String apartment, String zipCode) throws SQLException;
	
	public List getStateList() throws SQLException;	
	
	IBuildingModel getBuildingLocation(String street, List zipCodes) throws SQLException;
	
	String getBuildingId() throws SQLException;
	
	void insertBuildings(List dataList) throws SQLException;
	
	List getLocationByIds(final List locIds) throws SQLException;
	
	ILocationModel getLocationById(final String locId) throws SQLException;

	List getZoneMapping(final double latitude, final double longitude)  throws SQLException;
	
	void insertLocation(ILocationModel model) throws SQLException;
	
	void insertBuilding(IBuildingModel model) throws SQLException;
	
	Map<String, IAreaModel> getAreaLookup() throws SQLException;
	
	Map<String, IZoneModel> getZoneLookup() throws SQLException;
	
}
