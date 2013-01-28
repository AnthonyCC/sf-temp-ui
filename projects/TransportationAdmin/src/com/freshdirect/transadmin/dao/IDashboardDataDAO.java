package com.freshdirect.transadmin.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.freshdirect.transadmin.model.PlantCapacity;

public interface IDashboardDataDAO {
	
	Collection getPlantCapacity(String dayOfWeek)  throws SQLException;

	public void purgePlantCapacity(final String dayOfWeek) throws SQLException;
	
	public void savePlantCapacity(List<PlantCapacity> capacities) throws SQLException;
}
