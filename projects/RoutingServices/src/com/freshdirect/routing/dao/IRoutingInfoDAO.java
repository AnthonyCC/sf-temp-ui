package com.freshdirect.routing.dao;

import java.sql.SQLException;
import java.util.Collection;

public interface IRoutingInfoDAO {
	Collection getRoutingScenarios()  throws SQLException;
}
