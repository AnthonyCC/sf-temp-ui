package com.freshdirect.routing.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.routing.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.routing.model.IActiveOrderModel;
import com.freshdirect.routing.model.ICancelOrderModel;
import com.freshdirect.routing.model.ICrisisManagerBatch;
import com.freshdirect.routing.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.routing.model.ICrisisManagerBatchOrder;
import com.freshdirect.routing.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.model.IStandingOrderModel;
import com.freshdirect.routing.model.TriggerCrisisManagerResult;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public interface ICrisisManagerService {
	
	TriggerCrisisManagerResult createNewCrisisMngBatch(Date deliveryDate, Date destinationDate, String userId, String[] zone, 
			Date cutOffDateTime, Date startTime, Date endTime, String[] deliveryType, boolean includeSO, String profileName, boolean isStandByMode) throws RoutingServiceException;
	
	Set<ICrisisManagerBatch> getCrisisMngBatch(final Date deliveryDate) throws RoutingServiceException;
	
	void updateCrisisMngBatchMessage(String orderCrisisBatchId, String message) throws RoutingServiceException;
	
	void updateCrisisMngBatchStatus(String orderCrisisBatchId, EnumCrisisMngBatchStatus status) throws RoutingServiceException;
	
	ICrisisManagerBatch getCrisisMngBatchById(String batchId) throws RoutingServiceException;
	
	void addNewCrisisMngBatchAction(String orderCrisisBatchId, Date actionDateTime, EnumCrisisMngBatchActionType actionType
			, String userId) throws RoutingServiceException;
	
	void clearCrisisMngBatch(String orderCrisisBatchId) throws RoutingServiceException;
	
	List<ICrisisManagerBatchOrder> getOrderByCriteria(Date deliveryDate, Date cutOffDateTime, String[] area
			, Date startTime, Date endTime, String[] deliveryType, boolean isSOIncluded) throws RoutingServiceException;
	
	Map<EnumSaleStatus, Integer> getOrderStatsByDate(Date deliveryDate, String batchId) throws RoutingServiceException;

	void addNewCrisisMngBatchOrder(List<ICrisisManagerBatchOrder> batchOrders) throws RoutingServiceException;
	
	List<ICrisisManagerBatchOrder> getCrisisMngBatchOrders(String batchId, boolean filterException, boolean filterOrder) throws RoutingServiceException;
	
	void updateCrisisMngOrderException(String orderCrisisBatchId, List<String> exceptionOrderIds) throws RoutingServiceException;
	
	List<ICrisisManagerBatchReservation> getReservationByCriteria(Date deliveryDate, Date cutOffDateTime, String[] area, Date startTime, Date endTime) throws RoutingServiceException;
	
	void addNewCrisisMngBatchReservation(List<ICrisisManagerBatchReservation> reservations) throws RoutingServiceException;
	
	Map<String, ICrisisManagerBatchReservation> getCrisisMngBatchReservation(String batchId, boolean filterException) throws RoutingServiceException;

	void updateCrisisMngReservationException(String crisisMngBatchId, List<String> exceptionRsvIds) throws RoutingServiceException;
	
	void updateCrisisMngOrderStatus(String crisisMngBatchId, List<String> exceptionOrderIds) throws RoutingServiceException;
	
	void updateCrisisMngReservationStatus(String crisisMngBatchId, List<String> exceptionRsvIds) throws RoutingServiceException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslotByZone(String crisisMngBatchId) throws RoutingServiceException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getTimeslotByDate(Date deliveryDate) throws RoutingServiceException;
	 
	void addCrisisMngBatchDeliveryslot(Map<String, List<ICrisisManagerBatchDeliverySlot>> batchGroupedSlots) throws RoutingServiceException;
	
	void updateCrisisMngBatchDeliveryslot(List<ICrisisManagerBatchDeliverySlot> batchGroupedSlots) throws RoutingServiceException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslot(String batchId, boolean filterException) throws RoutingServiceException;
	
	void clearCrisisMngBatchDeliverySlot(String batchId) throws RoutingServiceException;
	
	List<IStandingOrderModel> getStandingOrderByCriteria(Date deliveryDate,Date cutOffDateTime, String[] area, Date startTime, Date endTime, String[] deliveryType, boolean isSOIncluded) throws RoutingServiceException;
	
	void addNewCrisisMngBatchStandingOrder(List<IStandingOrderModel> soOrders) throws RoutingServiceException;
	
	List<IStandingOrderModel> getStandingOrderByBatchId(String batchId) throws RoutingServiceException;
	
	void updateCrisisMngBatchStandingOrder(String batchId, List<IStandingOrderModel> batchStandingOrders) throws RoutingServiceException;
	
	List<IActiveOrderModel> getActiveOrderByArea(Date deliveryDate) throws RoutingServiceException;
	
	void updateCrisisMngBatchOrderReservation(String batchId, Map<String, String> batchOrderReservations) throws RoutingServiceException;
	
	List<ICancelOrderModel> getCancelOrderByArea(String batchId) throws RoutingServiceException;
}
