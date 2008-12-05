package com.freshdirect.routing.service.proxy;

import java.util.Collection;
import java.util.List;

import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.service.IRoutingEngineService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public class RoutingEngineServiceProxy extends BaseServiceProxy {
	
	public void saveLocations(Collection locationList, String region, String locationType) throws RoutingServiceException {
		getService().saveLocations(locationList, region, locationType);
	}
	
	public void purgeOrders(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
		getService().purgeOrders(schedulerId);
	}
	
	public List schedulerBulkReserveOrder(IRoutingSchedulerIdentity schedulerId, Collection orderList
											, String region, String locationType
											, String orderType) throws RoutingServiceException {
		return getService().schedulerBulkReserveOrder(schedulerId, orderList, region, locationType, orderType);
	}
	
	public void sendRoutesToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException {
		getService().sendRoutesToRoadNet(schedulerId, sessionDescription);
	}
	
	public List saveUnassignedToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionId, Collection orderList) throws RoutingServiceException {
		return getService().saveUnassignedToRoadNet(schedulerId, sessionId, orderList);
	}
	
	public String retrieveRoutingSession(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException {
		return getService().retrieveRoutingSession(schedulerId, sessionDescription);
	}
	
	public void schedulerBalanceRoutes(IRoutingSchedulerIdentity schedulerId, String balanceBy, double balanceFactor)  throws RoutingServiceException {
		getService().schedulerBalanceRoutes(schedulerId, balanceBy, balanceFactor);
	}
	
	public void schedulerRemoveFromServer(IRoutingSchedulerIdentity schedulerId)  throws RoutingServiceException {
		getService().schedulerRemoveFromServer(schedulerId);
	}
	
	public IRoutingEngineService getService() {
		return RoutingServiceLocator.getInstance().getRoutingEngineService();
	}
}
