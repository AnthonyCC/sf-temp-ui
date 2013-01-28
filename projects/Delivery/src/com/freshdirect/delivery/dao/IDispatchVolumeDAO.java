package com.freshdirect.delivery.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.freshdirect.analytics.DispatchVolumeModel;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public interface IDispatchVolumeDAO {

	void saveDispatch(Connection conn, Map<RoutingTimeOfDay, DispatchVolumeModel> dispatchMap) throws SQLException;
	
}
