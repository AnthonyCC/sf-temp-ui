package com.freshdirect.analytics.dao;

import java.util.Date;
import java.util.List;

import com.freshdirect.analytics.model.BounceData;
import com.freshdirect.analytics.model.PlantDispatchData;

public interface IPlantDispatchDAO {

	public List<PlantDispatchData> getData(Date deliveryDate);

}