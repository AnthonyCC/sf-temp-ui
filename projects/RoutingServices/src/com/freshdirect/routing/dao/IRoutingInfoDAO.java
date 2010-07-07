package com.freshdirect.routing.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IZoneScenarioModel;

public interface IRoutingInfoDAO {
	
	Collection getRoutingScenarios()  throws SQLException;
	
	IServiceTimeScenarioModel getRoutingScenarioByDate(final Date deliveryDate)  throws SQLException;
	
	IServiceTimeScenarioModel getRoutingScenarioByCode(final String code)  throws SQLException;
	
	Map<String, IZoneScenarioModel> getRoutingScenarioMapping(final String code)  throws SQLException;
	
	Map<String, IServiceTimeTypeModel> getRoutingServiceTimeTypes()  throws SQLException;
}
