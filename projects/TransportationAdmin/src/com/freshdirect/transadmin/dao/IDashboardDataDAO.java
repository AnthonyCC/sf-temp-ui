package com.freshdirect.transadmin.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.freshdirect.transadmin.model.OrderRateException;
import com.freshdirect.transadmin.model.PlantCapacity;
import com.freshdirect.transadmin.model.PlantDispatch;

public interface IDashboardDataDAO {
	
	Collection getPlantCapacity(String dayOfWeek)  throws SQLException;

	public void purgePlantCapacity(final String dayOfWeek) throws SQLException;
	
	public void savePlantCapacity(String dayOfWeek, List<PlantCapacity> capacities) throws SQLException;

	Collection getPlantDispatch() throws SQLException;

	void purgePlantDispatch() throws SQLException;

	void savePlantCapacity(List<PlantDispatch> dispatch) throws SQLException;

	Collection getOrderRateExceptions() throws SQLException;

	void saveOrderRateExceptions(List<OrderRateException> exceptions) throws SQLException;

	void purgeOrderRateExceptions() throws SQLException;
}
