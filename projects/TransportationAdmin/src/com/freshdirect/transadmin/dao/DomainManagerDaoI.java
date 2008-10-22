package com.freshdirect.transadmin.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.model.TrnRoute;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZone;
import com.freshdirect.transadmin.model.TrnZoneType;

public interface DomainManagerDaoI extends BaseManagerDaoI {

	Collection getEmployees() throws DataAccessException;

	Collection getZones() throws DataAccessException;

	Collection getRoutes() throws DataAccessException;
	
	Collection getRouteForZone(String zoneId) throws DataAccessException;

	Collection getTrucks() throws DataAccessException;

	String[] getDays() throws DataAccessException;

	String[] getTimings() throws DataAccessException;

	String[] getTruckTypes() throws DataAccessException;

	Collection getEmployeeJobType() throws DataAccessException;

	Collection getSupervisors() throws DataAccessException;

	TrnEmployee getEmployee(String id) throws DataAccessException;

	TrnZone getZone(String id) throws DataAccessException;

	TrnRoute getRoute(String id) throws DataAccessException;

	TrnTruck getTruck(String id) throws DataAccessException;

	Collection getTimeSlots() throws DataAccessException;		
	
	TrnZoneType getZoneType(String id) throws DataAccessException;
	
	Collection getZoneTypes() throws DataAccessException;
	
	Collection getMarkedAreas() throws DataAccessException;
	
	TrnArea getArea(String id) throws DataAccessException;
	
	Collection getAreas() throws DataAccessException;
	
	TrnCutOff getCutOff(String id) throws DataAccessException;
	
	Collection getCutOffs() throws DataAccessException;
	
	Collection getRouteNumberGroup(String date, String area) throws DataAccessException;
	
	Collection getDeliveryModels() throws DataAccessException;
	
	
}
