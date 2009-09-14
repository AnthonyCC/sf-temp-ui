package com.freshdirect.routing.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public interface IRoutingEngineService {
	
	void saveLocations(Collection locationList, String region, String locationType) throws RoutingServiceException;
	
	void purgeOrders(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException;
	
	List schedulerBulkReserveOrder(IRoutingSchedulerIdentity schedulerId, Collection orderList, String region, String locationType
										, String orderType) throws RoutingServiceException;
	
	void sendRoutesToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException;
	
	List saveUnassignedToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionId, Collection orderList) throws RoutingServiceException;
	
	String retrieveRoutingSession(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException;
	
	void schedulerBalanceRoutes(IRoutingSchedulerIdentity schedulerId, String balanceBy, double balanceFactor)  throws RoutingServiceException;
	
	void schedulerRemoveFromServer(IRoutingSchedulerIdentity schedulerId)  throws RoutingServiceException;
	
	List<IDeliverySlot> schedulerAnalyzeOrder(IOrderModel orderModel, String locationType
			, String orderType, Date startDate, int noOfDays, List<IDeliverySlot> slots) throws RoutingServiceException;

	IDeliveryReservation schedulerReserveOrder(IOrderModel orderModel,IDeliverySlot deliverySlot,
			String locationType
			, String orderType) throws RoutingServiceException;

	void schedulerConfirmOrder(IOrderModel orderModel) throws RoutingServiceException;
	
	boolean schedulerUpdateOrder(IOrderModel orderModel, String previousOrderNumber) throws RoutingServiceException;
	
	void schedulerCancelOrder(IOrderModel orderModel) throws RoutingServiceException;
	
	List<IDeliveryWindowMetrics> retrieveCapacityMetrics(IRoutingSchedulerIdentity schedulerId, List<IDeliverySlot> slots) throws RoutingServiceException;
	
}
