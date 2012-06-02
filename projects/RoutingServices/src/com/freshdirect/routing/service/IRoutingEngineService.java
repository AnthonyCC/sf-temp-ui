package com.freshdirect.routing.service;

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
import com.freshdirect.routing.service.exception.RoutingServiceException;

public interface IRoutingEngineService {
	
	void saveLocations(Collection locationList, String region, String locationType) throws RoutingServiceException;
	
	void saveLocationsEx(Collection locationList, IRoutingSchedulerIdentity schedulerId, String region, String locationType) throws RoutingServiceException;
	
	void purgeOrders(IRoutingSchedulerIdentity schedulerId, boolean reloadXml) throws RoutingServiceException;
	
	void purgeBatchOrders(IRoutingSchedulerIdentity schedulerId, boolean reloadXml) throws RoutingServiceException;
	
	List schedulerBulkReserveOrder(IRoutingSchedulerIdentity schedulerId, Collection orderList, String region, String locationType
										, String orderType) throws RoutingServiceException;
	
	void sendRoutesToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionDescription, String waveCode) throws RoutingServiceException;
	
	List saveUnassignedToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionId, Collection orderList) throws RoutingServiceException;
	
	String retrieveRoutingSession(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException;
	
	void schedulerBalanceRoutes(IRoutingSchedulerIdentity schedulerId, String balanceBy, double balanceFactor)  throws RoutingServiceException;
	
	void schedulerUnload(IRoutingSchedulerIdentity schedulerId)  throws RoutingServiceException;
	
	List<IDeliverySlot> schedulerAnalyzeOrder(IOrderModel orderModel, String locationType
			, String orderType, Date startDate, int noOfDays, List<IDeliverySlot> slots) throws RoutingServiceException;

	IDeliveryReservation schedulerReserveOrder(IOrderModel orderModel,IDeliverySlot deliverySlot,
			String locationType
			, String orderType) throws RoutingServiceException;

	void schedulerConfirmOrder(IOrderModel orderModel) throws RoutingServiceException;
	
	boolean schedulerUpdateOrder(IOrderModel orderModel) throws RoutingServiceException;
	
	boolean schedulerUpdateOrderNo(IOrderModel orderModel) throws RoutingServiceException;
	
	void schedulerCancelOrder(IOrderModel orderModel) throws RoutingServiceException;
	
	List<IDeliveryWindowMetrics> retrieveCapacityMetrics(IRoutingSchedulerIdentity schedulerId, List<IDeliverySlot> slots) throws RoutingServiceException;
	
	void schedulerSaveLocation(IOrderModel orderModel, String locationType) throws RoutingServiceException;
	
	List<IRoutingNotificationModel> retrieveNotifications() throws RoutingServiceException;
	
	void deleteNotifications(List<IRoutingNotificationModel> notifications)  throws RoutingServiceException;

	DeliveryAreaOrder getDeliveryAreaModel(IRoutingSchedulerIdentity schedulerId, IOrderModel orderModel
			, String region, String locationType
			, String orderType);
	
}
