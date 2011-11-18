package com.freshdirect.transadmin.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchType;
import com.freshdirect.transadmin.model.IActiveOrderModel;
import com.freshdirect.transadmin.model.ICancelOrderModel;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.model.ICrisisMngBatchOrder;
import com.freshdirect.routing.model.ICustomerModel;
import com.freshdirect.routing.model.StandingOrderModel;


public interface ICrisisManagerDAO {
		
	String addNewCrisisMngBatch(Date deliveryDate, Date destinationDate, String[] zone, 
			Date cutOffDateTime, Date startTime, Date endTime, String[] deliveryType, EnumCrisisMngBatchType batchType, String profileName, boolean isStandByMode) throws SQLException;
		
	void addNewCrisisMngBatchAction(String orderCrisisBatchId, Date actionDateTime
			, EnumCrisisMngBatchActionType actionType, String userId) throws SQLException;
	
	Set<ICrisisManagerBatch> getCrisisMngBatch(final Date deliveryDate) throws SQLException;
	
	void updateCrisisMngBatchMessage(String orderCrisisBatchId, String message) throws SQLException;
	
	void updateCrisisMngBatchStatus(String orderCrisisBatchId, EnumCrisisMngBatchStatus status) throws SQLException;
	
	ICrisisManagerBatch getCrisisMngBatchById(String batchId) throws SQLException;
	
	void clearCrisisMngBatchCustomer(String batchId) throws SQLException;
	
	void clearCrisisMngBatchOrder(String orderCrisisBatchId) throws SQLException;
	
	List<ICrisisMngBatchOrder> getOrderByCriteria(Date deliveryDate, Date cutOffDateTime
			, String[] area, String startTime, String endTime, String[] deliveryType, String profileName) throws SQLException;
		
	Map<EnumSaleStatus, Integer> getOrderStatsByDate(Date deliveryDate, String batchId, EnumCrisisMngBatchType batchType) throws SQLException;
	
	void addNewCrisisMngBatchCustomer(List<ICustomerModel> batchCustomers, String batchId) throws SQLException;
	
	void addNewCrisisMngBatchRegularOrder(List<ICrisisMngBatchOrder> batchOrders, String batchId) throws SQLException;
	
	List<ICrisisMngBatchOrder> getCrisisMngBatchRegularOrder(String batchId, boolean filterException, boolean filterOrder) throws SQLException;
	
	void updateCrisisMngOrderException(String orderCrisisBatchId, String batchType, List<String> exceptionOrderIds) throws SQLException;
	
	void updateCrisisMngOrderStatus(String orderCrisisBatchId, String batchType, List<String> exceptionOrderIds) throws SQLException;
	
	List<ICrisisManagerBatchReservation> getReservationByCriteria(Date deliveryDate, Date cutOffDateTime, String[] area, String startTime, String endTime, String profileName) throws SQLException;
	
	void clearCrisisMngBatchReservation(String orderCrisisBatchId) throws SQLException;
	
	void addNewCrisisMngBatchReservation(List<ICrisisManagerBatchReservation> batchReservations, String batchId) throws SQLException;
	
	Map<String, ICrisisManagerBatchReservation> getCrisisMngBatchReservation(String batchId, boolean filterException) throws SQLException;
	
	void updateCrisisMngReservationException(String crisisMngBatchId, List<String> exceptionRsvIds) throws SQLException;
	
	void updateCrisisMngReservationStatus(String orderCrisisBatchId, List<String> exceptionRsvIds) throws SQLException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslotByZone(String batchId, EnumCrisisMngBatchType batchType) throws SQLException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getTimeslotByDate(Date deliveryDate) throws SQLException;
	
	void addCrisisMngBatchDeliveryslot(Map<String, List<ICrisisManagerBatchDeliverySlot>> batchGroupedSlots) throws SQLException;
	
	void updateCrisisMngBatchDeliveryslot(List<ICrisisManagerBatchDeliverySlot> batchGroupedSlots) throws SQLException;
	
	Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslot(final String batchId, boolean filterException) throws SQLException;
	
	void clearCrisisMngBatchDeliverySlot(String batchId) throws SQLException;
	
	List<ICrisisMngBatchOrder> getStandingOrderByCriteria(final Date deliveryDate, final Date cutOffDateTime
			, final String[] area, final String startTime, final String endTime, final String[] deliveryType, String profileName) throws SQLException;
	
	void addNewCrisisMngBatchStandingOrder(List<ICrisisMngBatchOrder> batchSOs, String batchId) throws SQLException;
	
	void clearCrisisMngBatchStandingOrder(String batchId) throws SQLException;
	
	List<ICrisisMngBatchOrder> getCrisisMngBatchStandingOrder(String batchId, boolean filterException, boolean filterOrder) throws SQLException;
	
	void updateCrisisMngBatchStandingOrder(String batchId, List<StandingOrderModel> batchStandingOrders) throws SQLException;
	
	List<IActiveOrderModel> getActiveOrderByArea(Date deliveryDate, EnumCrisisMngBatchType batchType) throws SQLException;
	
	void updateCrisisMngBatchOrderReservation(String batchId, Map<String, String> batchOrderReservations) throws SQLException;
	
	List<ICancelOrderModel> getCancelOrderByArea(String batchId, EnumCrisisMngBatchType batchType) throws SQLException;
}
