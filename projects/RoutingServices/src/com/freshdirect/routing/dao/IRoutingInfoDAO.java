package com.freshdirect.routing.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneScenarioModel;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public interface IRoutingInfoDAO {
	
	Collection getRoutingScenarios()  throws SQLException;
	
	IServiceTimeScenarioModel getRoutingScenarioByDate(final Date deliveryDate)  throws SQLException;
	
	IServiceTimeScenarioModel getRoutingScenarioByCode(final String code)  throws SQLException;
	
	Map<String, IZoneScenarioModel> getRoutingScenarioMapping(final String code)  throws SQLException;
	
	Map<String, IServiceTimeTypeModel> getRoutingServiceTimeTypes()  throws SQLException;
	
	int flagReRouteReservation(final Date deliveryDate, final List<String> zones) throws SQLException;
	
	Map<String, Map<RoutingTimeOfDay, Map<Date, List<IWaveInstance>>>> getPlannedDispatchTree(final Date deliveryDate)  throws SQLException;
}
