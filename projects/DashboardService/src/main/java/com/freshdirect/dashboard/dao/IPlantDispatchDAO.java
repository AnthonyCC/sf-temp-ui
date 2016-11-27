package com.freshdirect.dashboard.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.dashboard.model.PlantDispatchData;

public interface IPlantDispatchDAO {

	public Map<String, List<PlantDispatchData>> getData(Date deliveryDate);

}