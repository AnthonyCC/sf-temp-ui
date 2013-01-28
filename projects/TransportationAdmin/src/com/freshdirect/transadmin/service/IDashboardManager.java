package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.List;

import com.freshdirect.transadmin.model.PlantCapacity;


public interface IDashboardManager {

	Collection getPlantCapacity(String dayOfWeek);

	void savePlantCapacity(String dayOfWeek, List<PlantCapacity> capacity);
}
