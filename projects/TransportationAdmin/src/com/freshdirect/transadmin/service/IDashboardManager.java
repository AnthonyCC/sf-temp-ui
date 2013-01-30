package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.List;

import com.freshdirect.transadmin.model.PlantCapacity;
import com.freshdirect.transadmin.model.PlantDispatch;


public interface IDashboardManager {

	Collection getPlantCapacity(String dayOfWeek);

	void savePlantCapacity(String dayOfWeek, List<PlantCapacity> capacity);
	
	Collection getPlantDispatch();

	void savePlantDispatch(List<PlantDispatch> dispatch);
}
