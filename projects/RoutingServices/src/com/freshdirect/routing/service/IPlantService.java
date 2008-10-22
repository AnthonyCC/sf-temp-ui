package com.freshdirect.routing.service;

import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public interface IPlantService {
	
	IPackagingModel getPackagingInfo(IOrderModel model, String orderSizeExpression, int defaultCartonCount
			, int defaultFreezerCount, int defaultCaseCount) throws RoutingServiceException;
	
	Map getPackagingInfoList(List orderIdLst) throws RoutingServiceException;
	
	public IPackagingModel getPackageModel(Map rowMap, String orderSizeExpression, int defaultCartonCount
			, int defaultFreezerCount, int defaultCaseCount) throws RoutingServiceException;
}
