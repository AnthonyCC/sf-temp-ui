package com.freshdirect.routing.service.proxy;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneScenarioModel;
import com.freshdirect.routing.service.IRoutingInfoService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public class RoutingInfoServiceProxy  extends BaseServiceProxy  {
	
	public Collection getRoutingScenarios()  throws RoutingServiceException {
		return getService().getRoutingScenarios();
	}
	
	public IServiceTimeScenarioModel getRoutingScenarioByDate(Date deliveryDate)  throws RoutingServiceException {
		return getService().getRoutingScenarioByDate(deliveryDate);
	}
	
	public IServiceTimeScenarioModel getRoutingScenarioByCode(String code)  throws RoutingServiceException {
		return getService().getRoutingScenarioByCode(code);
	}
	
	public Map<String, IZoneScenarioModel> getRoutingScenarioMapping(String code)  throws RoutingServiceException {
		return getService().getRoutingScenarioMapping(code);
	}
	
	public Map<String, IServiceTimeTypeModel> getRoutingServiceTimeTypes()  throws RoutingServiceException {
		return getService().getRoutingServiceTimeTypes();
	}
	
	public int flagReRouteReservation(Date deliveryDate, List<String> zones) throws RoutingServiceException {
		return getService().flagReRouteReservation(deliveryDate, zones);
	}
	
	public Map<String, Map<RoutingTimeOfDay, Map<Date, List<IWaveInstance>>>> getPlannedDispatchTree(Date deliveryDate)  throws RoutingServiceException {
		return getService().getPlannedDispatchTree(deliveryDate);
	}
		
	public IRoutingInfoService getService() {
		return RoutingServiceLocator.getInstance().getRoutingInfoService();
	}
}
