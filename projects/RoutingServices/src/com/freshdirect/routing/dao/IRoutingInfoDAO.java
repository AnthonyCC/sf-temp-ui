package com.freshdirect.routing.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

import com.freshdirect.routing.model.IServiceTimeScenarioModel;

public interface IRoutingInfoDAO {
	
	Collection getRoutingScenarios()  throws SQLException;
	
	IServiceTimeScenarioModel getRoutingScenario(Date deliveryDate)  throws SQLException;
}
