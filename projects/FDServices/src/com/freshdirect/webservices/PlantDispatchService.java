package com.freshdirect.webservices;

import java.util.Date;
import java.util.List;

import com.freshdirect.analytics.model.PlantDispatchData;

public interface PlantDispatchService {

	public List<PlantDispatchData> getDispatchVolume(String deliveryDate);

	public String getRefreshTime();
	
	public String getConnection() throws Exception;
}