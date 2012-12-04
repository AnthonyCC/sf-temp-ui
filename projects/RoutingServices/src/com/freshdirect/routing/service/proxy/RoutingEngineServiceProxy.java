package com.freshdirect.routing.service.proxy;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingNotificationModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder;
import com.freshdirect.routing.service.IRoutingEngineService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public class RoutingEngineServiceProxy extends BaseServiceProxy {
	
	public void saveLocations(Collection locationList, String region, String locationType) throws RoutingServiceException {
		getService().saveLocations(locationList, region, locationType);
	}
	public void saveLocationsEx(Collection locationList, IRoutingSchedulerIdentity schedulerId, String region, String locationType) throws RoutingServiceException {
		getService().saveLocationsEx(locationList, schedulerId, region, locationType);
	}
	
	
	
	public void purgeBatchOrders(IRoutingSchedulerIdentity schedulerId, boolean reloadXml) throws RoutingServiceException {
		getService().purgeBatchOrders(schedulerId, reloadXml);
	}
	
	public void purgeOrders(IRoutingSchedulerIdentity schedulerId, boolean reloadXml) throws RoutingServiceException {
		getService().purgeOrders(schedulerId, reloadXml);
	}
	
	public List schedulerBulkReserveOrder(IRoutingSchedulerIdentity schedulerId, Collection orderList
											, String region, String locationType
											, String orderType) throws RoutingServiceException {
		return getService().schedulerBulkReserveOrder(schedulerId, orderList, region, locationType, orderType);
	}
	
	public void sendRoutesToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionDescription,String waveCode) throws RoutingServiceException {
		getService().sendRoutesToRoadNet(schedulerId, sessionDescription, waveCode);
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
	
	public void schedulerUnload(IRoutingSchedulerIdentity schedulerId)  throws RoutingServiceException {
		getService().schedulerUnload(schedulerId);
	}
	
	public IDeliveryReservation schedulerReserveOrder(IOrderModel orderModel,IDeliverySlot deliverySlot,
			 String locationType
			, String orderType) throws RoutingServiceException {
		return getService().schedulerReserveOrder(orderModel, deliverySlot, locationType, orderType);
	}
	
	public List<IDeliverySlot> schedulerAnalyzeOrder(IOrderModel orderModel, String locationType
			, String orderType, Date startDate, int noOfDays, List<IDeliverySlot> slots) throws RoutingServiceException {
		return getService().schedulerAnalyzeOrder(orderModel, locationType, orderType, startDate, noOfDays, slots);
	}
	
	public void schedulerConfirmOrder(IOrderModel orderModel) throws RoutingServiceException {
		getService().schedulerConfirmOrder(orderModel);
	}
	
	public boolean schedulerUpdateOrder(IOrderModel orderModel) throws RoutingServiceException {
		return getService().schedulerUpdateOrder(orderModel);
	}
	
	public boolean schedulerUpdateOrderNo(IOrderModel orderModel) throws RoutingServiceException {
		return getService().schedulerUpdateOrderNo(orderModel);
	}
	public IOrderModel schedulerRetrieveOrder(IOrderModel orderModel) throws RoutingServiceException {
		return getService().schedulerRetrieveOrder(orderModel);
	}
	
	public void schedulerCancelOrder(IOrderModel orderModel) throws RoutingServiceException {
		getService().schedulerCancelOrder(orderModel);
	}
	
	public List<IDeliveryWindowMetrics> retrieveCapacityMetrics(IRoutingSchedulerIdentity schedulerId, List<IDeliverySlot> slots) throws RoutingServiceException {
		return getService().retrieveCapacityMetrics(schedulerId, slots);
	}
	
	public void schedulerSaveLocation(IOrderModel orderModel, String locationType) throws RoutingServiceException {
		getService().schedulerSaveLocation(orderModel, locationType);
	}
	
	public List<IRoutingNotificationModel> retrieveNotifications() throws RoutingServiceException {
		return getService().retrieveNotifications();
	}
	
	public void deleteNotifications(List<IRoutingNotificationModel> notifications)  throws RoutingServiceException {
		getService().deleteNotifications(notifications);
	}
	
	public DeliveryAreaOrder getDeliveryAreaModel(IRoutingSchedulerIdentity schedulerId, IOrderModel orderModel
			, String region, String locationType
			, String orderType) throws RoutingServiceException {
		return getService().getDeliveryAreaModel(schedulerId, orderModel,region, locationType, orderType);
	}
	
	public IRoutingEngineService getService() {
		return RoutingServiceLocator.getInstance().getRoutingEngineService();
	}
}
