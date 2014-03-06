package com.freshdirect.dashboard.service;

import java.util.Date;
import java.util.List;

import com.freshdirect.dashboard.exception.FDServiceException;
import com.freshdirect.dashboard.model.PlantDispatchData;

public interface IPlantDispatchService {

	List<PlantDispatchData> getDispatchVolume(Date deliveryDate) throws FDServiceException;
	
}