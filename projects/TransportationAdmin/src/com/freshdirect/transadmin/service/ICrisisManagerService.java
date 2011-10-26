package com.freshdirect.transadmin.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.model.IActiveOrderModel;
import com.freshdirect.transadmin.model.ICancelOrderModel;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.model.ICrisisManagerBatchOrder;
import com.freshdirect.transadmin.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.model.IStandingOrderModel;
import com.freshdirect.transadmin.web.model.TriggerCrisisManagerResult;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;

public interface ICrisisManagerService {
	
	TriggerCrisisManagerResult createNewCrisisMngBatch(Date deliveryDate, Date destinationDate, String userId, String[] zone, 
			Date cutOffDateTime, Date startTime, Date endTime, String[] deliveryType, boolean includeSO, String profileName, boolean isStandByMode) throws TransAdminServiceException;
	
	Set<ICrisisManagerBatch> getCrisisMngBatch(final Date deliveryDate) throws TransAdminServiceException;
	
	void updateCrisisMngBatchMessage(String orderCrisisBatchId, String message) throws TransAdminServiceException;
	
	void updateCrisisMngBatchStatus(String orderCrisisBatchId, EnumCrisisMngBatchStatus status) throws TransAdminServiceException;
	
	ICrisisManagerBatch getCrisisMngBatchById(String batchId) throws TransAdminServiceException;
	
	void addNewCrisisMngBatchAction(String orderCrisisBatchId, Date actionDateTime, EnumCrisisMngBatchActionType actionType
			, String userId) throws TransAdminServiceException;
	
	void clearCrisisMngBatch(String orderCrisisBatchId) throws TransAdminServiceException;
	
	List<ICrisisManagerBatchOrder> getOrderByCriteria(Date deliveryDate, Date cutOffDateTime, String[] area
			, String startTime, String endTime, String[] deliveryType, String profileName, boolean isSOIncluded) throws TransAdminServiceException;
	
	Map<EnumSaleStatus, Integer> getOrderStatsByDate(Date deliveryDate, String batchId) throws TransAdminServiceException;

	void addNewCrisisMngBatchOrder(List<ICrisisManagerBatchOrder> batchOrders) throws TransAdminServiceException;
	
	List<ICrisisManagerBatchOrder> getCrisisMngBatchOrders(String batchId, boolean filterException, boolean filterOrder) throws TransAdminServiceException;
	
	void updateCrisisMngOrderException(String orderCrisisBatchId, List<String> exceptionOrderIds) throws TransAdminServiceException;
	
	List<ICrisisManagerBatchReservation> getReservationByCriteria(Date deliveryDate, Date cutOffDateTime, String[] area, String startTime, String endTime, String profileName) throws TransAdminServiceException;
	
	void addNewCrisisMngBatchReservation(List<ICrisisManagerBatchReservation> reservations) throws TransAdminServiceException;
	
	Map<String, ICrisisManagerBatchReservation> getCrisisMngBatchReservation(String batchId, boolean filterException) throws TransAdminServiceException;

	void updateCrisisMngReservationException(String crisisMngBatchId, List<String> exceptionRsvIds) throws TransAdminServiceException;
	
	void updateCrisisMngOrderStatus(String crisisMngBatchId, List<String> exceptionOrderIds) throws TransAdminServiceException;
	
	void updateCrisisMngReservationStatus(String crisisMngBatchId, List<String> exceptionRsvIds) throws TransAdminServiceException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslotByZone(String crisisMngBatchId) throws TransAdminServiceException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getTimeslotByDate(Date deliveryDate) throws TransAdminServiceException;
	 
	void addCrisisMngBatchDeliveryslot(Map<String, List<ICrisisManagerBatchDeliverySlot>> batchGroupedSlots) throws TransAdminServiceException;
	
	void updateCrisisMngBatchDeliveryslot(List<ICrisisManagerBatchDeliverySlot> batchGroupedSlots) throws TransAdminServiceException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslot(String batchId, boolean filterException) throws TransAdminServiceException;
	
	void clearCrisisMngBatchDeliverySlot(String batchId) throws TransAdminServiceException;
	
	List<IStandingOrderModel> getStandingOrderByCriteria(Date deliveryDate,Date cutOffDateTime, String[] area, String startTime, String endTime, String[] deliveryType, String profileName, boolean isSOIncluded) throws TransAdminServiceException;
	
	void addNewCrisisMngBatchStandingOrder(List<IStandingOrderModel> soOrders) throws TransAdminServiceException;
	
	List<IStandingOrderModel> getStandingOrderByBatchId(String batchId) throws TransAdminServiceException;
	
	void updateCrisisMngBatchStandingOrder(String batchId, List<IStandingOrderModel> batchStandingOrders) throws TransAdminServiceException;
	
	List<IActiveOrderModel> getActiveOrderByArea(Date deliveryDate) throws TransAdminServiceException;
	
	void updateCrisisMngBatchOrderReservation(String batchId, Map<String, String> batchOrderReservations) throws TransAdminServiceException;
	
	List<ICancelOrderModel> getCancelOrderByArea(String batchId) throws TransAdminServiceException;
}
