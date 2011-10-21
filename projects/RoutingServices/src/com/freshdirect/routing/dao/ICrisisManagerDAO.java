package com.freshdirect.routing.dao;

import java.sql.SQLException;
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


public interface ICrisisManagerDAO {
		
	String addNewCrisisMngBatch(Date deliveryDate, Date destinationDate, String[] zone, 
			Date cutOffDateTime, Date startTime, Date endTime, String[] deliveryType, boolean includeSO, String profileName, boolean isStandByMode) throws SQLException;
		
	void addNewCrisisMngBatchAction(String orderCrisisBatchId, Date actionDateTime
			, EnumCrisisMngBatchActionType actionType, String userId) throws SQLException;
	
	Set<ICrisisManagerBatch> getCrisisMngBatch(final Date deliveryDate) throws SQLException;
	
	void updateCrisisMngBatchMessage(String orderCrisisBatchId, String message) throws SQLException;
	
	void updateCrisisMngBatchStatus(String orderCrisisBatchId, EnumCrisisMngBatchStatus status) throws SQLException;
	
	ICrisisManagerBatch getCrisisMngBatchById(String batchId) throws SQLException;
	
	void clearCrisisMngBatchOrder(String orderCrisisBatchId) throws SQLException;
	
	List<ICrisisManagerBatchOrder> getOrderByCriteria(Date deliveryDate, Date cutOffDateTime
			, String[] area, String startTime, String endTime, String[] deliveryType, String profileName,boolean isSOIncluded) throws SQLException;
		
	Map<EnumSaleStatus, Integer> getOrderStatsByDate(Date deliveryDate, String batchId) throws SQLException;
	
	void addNewCrisisMngBatchOrder(List<ICrisisManagerBatchOrder> batchOrders) throws SQLException;
	
	List<ICrisisManagerBatchOrder> getCrisisMngBatchOrders(String batchId, boolean filterException, boolean filterOrder) throws SQLException;
	
	void updateCrisisMngOrderException(String orderCrisisBatchId, List<String> exceptionOrderIds) throws SQLException;
	
	void updateCrisisMngOrderStatus(String orderCrisisBatchId, List<String> exceptionOrderIds) throws SQLException;
	
	List<ICrisisManagerBatchReservation> getReservationByCriteria(Date deliveryDate, Date cutOffDateTime, String[] area, String startTime, String endTime, String profileName) throws SQLException;
	
	void clearCrisisMngBatchReservation(String orderCrisisBatchId) throws SQLException;
	
	void addNewCrisisMngBatchReservation(List<ICrisisManagerBatchReservation> batchReservations) throws SQLException;
	
	Map<String, ICrisisManagerBatchReservation> getCrisisMngBatchReservation(String batchId, boolean filterException) throws SQLException;
	
	void updateCrisisMngReservationException(String crisisMngBatchId, List<String> exceptionRsvIds) throws SQLException;
	
	void updateCrisisMngReservationStatus(String orderCrisisBatchId, List<String> exceptionRsvIds) throws SQLException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslotByZone(final String batchId) throws SQLException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getTimeslotByDate(final Date deliveryDate) throws SQLException;
	
	void addCrisisMngBatchDeliveryslot(Map<String, List<ICrisisManagerBatchDeliverySlot>> batchGroupedSlots) throws SQLException;
	
	void updateCrisisMngBatchDeliveryslot(List<ICrisisManagerBatchDeliverySlot> batchGroupedSlots) throws SQLException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslot(final String batchId, boolean filterException) throws SQLException;
	
	void clearCrisisMngBatchDeliverySlot(String batchId) throws SQLException;
	
	List<IStandingOrderModel> getStandingOrderByCriteria(final Date deliveryDate, final Date cutOffDateTime
			, final String[] area, final String startTime, final String endTime, final String[] deliveryType, String profileName, boolean isSOIncluded) throws SQLException;
	
	void addNewCrisisMngBatchStandingOrder(List<IStandingOrderModel> batchSOs) throws SQLException;
	
	void clearCrisisMngBatchStandingOrder(String batchId) throws SQLException;
	
	List<IStandingOrderModel> getStandingOrderByBatchId(final String batchId) throws SQLException;
	
	void updateCrisisMngBatchStandingOrder(String batchId, List<IStandingOrderModel> batchStandingOrders) throws SQLException;
	
	List<IActiveOrderModel> getActiveOrderByArea(final Date deliveryDate) throws SQLException;
	
	void updateCrisisMngBatchOrderReservation(String batchId, Map<String, String> batchOrderReservations) throws SQLException;
	
	List<ICancelOrderModel> getCancelOrderByArea(final String batchId) throws SQLException;
}
