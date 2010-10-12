package com.freshdirect.transadmin.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.web.model.TimeRange;

public interface ZoneManagerDaoI {
	
	Collection getActiveZoneCodes() throws DataAccessException;
	Collection getActiveZoneCodes(String date) throws DataAccessException;
	Map<String, List<TimeRange>> getWindowSteeringDiscounts(Date deliveryDate) throws DataAccessException;
	Collection getDefaultZoneSupervisor(String zoneCode, String dayPart, String date)throws DataAccessException;
}
