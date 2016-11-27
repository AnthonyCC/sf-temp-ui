package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.List;

import com.freshdirect.framework.service.ServiceException;
import com.freshdirect.transadmin.model.OrderRateException;
import com.freshdirect.transadmin.model.PlantCapacity;
import com.freshdirect.transadmin.model.PlantDispatch;


public interface IDashboardManager {

	Collection getPlantCapacity(String dayOfWeek) throws ServiceException;

	void savePlantCapacity(String dayOfWeek, List<PlantCapacity> capacity) throws ServiceException;
	
	Collection getPlantDispatch() throws ServiceException;

	void savePlantDispatch(List<PlantDispatch> dispatch) throws ServiceException;

	Collection getOrderRateExceptions() throws ServiceException;

	void saveOrderRateExceptions(List<OrderRateException> exceptions) throws ServiceException;
}
