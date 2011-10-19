package com.freshdirect.routing.service.proxy;

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
import com.freshdirect.routing.service.ICrisisManagerService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;


public class CrisisManagerServiceProxy  extends BaseServiceProxy  {	
		
	public ICrisisManagerService getService() {
		return RoutingServiceLocator.getInstance().getCrisisManagerService();
	}
	
	public TriggerCrisisManagerResult createNewCrisisMngBatch(Date deliveryDate, Date destinationDate, String userId, String[] zone, 
									Date cutOffDateTime, Date startTime, Date endTime, String[] deliveryType, boolean includeSO, String profileName, boolean isStandByMode) throws RoutingServiceException {
		return getService().createNewCrisisMngBatch(deliveryDate, destinationDate, userId, zone 
									, cutOffDateTime, startTime, endTime, deliveryType, includeSO, profileName, isStandByMode);
	}
	
	public Set<ICrisisManagerBatch> getCrisisMngBatch(Date deliveryDate) throws RoutingServiceException {
		return getService().getCrisisMngBatch(deliveryDate);
	}
	
	public void updateCrisisMngBatchMessage(String orderCrisisBatchId, String message) throws RoutingServiceException {
		getService().updateCrisisMngBatchMessage(orderCrisisBatchId, message);
	}
	
	public void updateCrisisMngBatchStatus(String orderCrisisBatchId, EnumCrisisMngBatchStatus status) throws RoutingServiceException {
		getService().updateCrisisMngBatchStatus(orderCrisisBatchId, status);
	}
	
	public ICrisisManagerBatch getCrisisMngBatchById(String batchId) throws RoutingServiceException {
		return getService().getCrisisMngBatchById(batchId);
	}
	
	
	public void addNewCrisisMngBatchAction(String orderCrisisBatchId, Date actionDateTime, EnumCrisisMngBatchActionType actionType
				, String userId) throws RoutingServiceException {
		getService().addNewCrisisMngBatchAction(orderCrisisBatchId, actionDateTime, actionType, userId);
	}
	
	public void clearCrisisMngBatch(String orderCrisisBatchId) throws RoutingServiceException {
		getService().clearCrisisMngBatch(orderCrisisBatchId);
	}
	
	public List<ICrisisManagerBatchOrder> getOrderByCriteria(Date deliveryDate, Date cutOffDateTime, String[] area, String startTime, String endTime, String[] deliveryType, boolean isSOIncluded) throws RoutingServiceException {
		return getService().getOrderByCriteria(deliveryDate, cutOffDateTime, area, startTime, endTime, deliveryType, isSOIncluded);		
	}
	
	public Map<EnumSaleStatus, Integer> getOrderStatsByDate(Date deliveryDate, String batchId) throws RoutingServiceException {
		return getService().getOrderStatsByDate(deliveryDate, batchId);		
	}
	
	public void addNewCrisisMngBatchOrder(List<ICrisisManagerBatchOrder> batchOrders) throws RoutingServiceException {
		getService().addNewCrisisMngBatchOrder(batchOrders);
	}
	
	public List<ICrisisManagerBatchOrder> getCrisisMngBatchOrders(String batchId, boolean filterException, boolean filterOrder) throws RoutingServiceException {
		return getService().getCrisisMngBatchOrders(batchId, filterException, filterOrder);
	}
	
	public void updateCrisisMngOrderException(String orderCrisisBatchId, List<String> exceptionOrderIds) throws RoutingServiceException {
		getService().updateCrisisMngOrderException(orderCrisisBatchId, exceptionOrderIds);
	}
	
	public List<ICrisisManagerBatchReservation> getReservationByCriteria(Date deliveryDate, Date cutOffDateTime, String[] area, String startTime, String endTime) throws RoutingServiceException {
		return getService().getReservationByCriteria(deliveryDate, cutOffDateTime, area, startTime, endTime);		
	}
	
	public void addNewCrisisMngBatchReservation(List<ICrisisManagerBatchReservation> reservations) throws RoutingServiceException {
		getService().addNewCrisisMngBatchReservation(reservations);
	}
	
	public Map<String, ICrisisManagerBatchReservation> getCrisisMngBatchReservation(String batchId, boolean filterException)throws RoutingServiceException {
		return getService().getCrisisMngBatchReservation(batchId, filterException);
	}
	public void updateCrisisMngReservationException(String crisisMngBatchId, List<String> exceptionRsvIds) throws RoutingServiceException {
		getService().updateCrisisMngReservationException(crisisMngBatchId, exceptionRsvIds);
	}
	
	public void updateCrisisMngOrderStatus(String orderCrisisBatchId, List<String> exceptionOrderIds) throws RoutingServiceException {
		getService().updateCrisisMngOrderStatus(orderCrisisBatchId, exceptionOrderIds);
	}
	public void updateCrisisMngReservationStatus(String orderCrisisBatchId, List<String> exceptionRsvIds) throws RoutingServiceException {
		getService().updateCrisisMngReservationStatus(orderCrisisBatchId, exceptionRsvIds);
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslotByZone(String crisisMngBatchId) throws RoutingServiceException {
		return getService().getCrisisMngBatchTimeslotByZone(crisisMngBatchId);
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getTimeslotByDate(Date deliveryDate) throws RoutingServiceException {
		return getService().getTimeslotByDate(deliveryDate);
	}
	
	public void addCrisisMngBatchDeliveryslot(Map<String, List<ICrisisManagerBatchDeliverySlot>> groupedSlots) throws RoutingServiceException {
		getService().addCrisisMngBatchDeliveryslot(groupedSlots);
	}
	
	public void updateCrisisMngBatchDeliveryslot(List<ICrisisManagerBatchDeliverySlot> groupedSlots) throws RoutingServiceException {
		getService().updateCrisisMngBatchDeliveryslot(groupedSlots);
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslot(String batchId, boolean filterException) throws RoutingServiceException {
		return getService().getCrisisMngBatchTimeslot(batchId, filterException);
	}
	
	public void clearCrisisMngBatchDeliverySlot(String batchId) throws RoutingServiceException {
		getService().clearCrisisMngBatchDeliverySlot(batchId);
	}
	
	public List<IStandingOrderModel> getStandingOrderByCriteria(Date deliveryDate,Date cutOffDateTime, String[] area, String startTime, String endTime, String[] deliveryType, boolean isSOIncluded) throws RoutingServiceException{
		return getService().getStandingOrderByCriteria(deliveryDate, cutOffDateTime, area, startTime, endTime, deliveryType, isSOIncluded);		
	}
	
	public void addNewCrisisMngBatchStandingOrder(List<IStandingOrderModel> soOrders){
		getService().addNewCrisisMngBatchStandingOrder(soOrders);
	}
	
	public List<IStandingOrderModel> getStandingOrderByBatchId(String batchId){
		return getService().getStandingOrderByBatchId(batchId);
	}
	
	public void updateCrisisMngBatchStandingOrder(String batchId, List<IStandingOrderModel> batchStandingOrders) throws RoutingServiceException {
		getService().updateCrisisMngBatchStandingOrder(batchId, batchStandingOrders);
	}
	
	public List<IActiveOrderModel> getActiveOrderByArea(Date deliveryDate) throws RoutingServiceException {
		return getService().getActiveOrderByArea(deliveryDate);
	}
	
	public void updateCrisisMngBatchOrderReservation(String batchId, Map<String, String> batchOrderReservations) throws RoutingServiceException {
		getService().updateCrisisMngBatchOrderReservation(batchId, batchOrderReservations);
	}
	
	public List<ICancelOrderModel> getCancelOrderByArea(String batchId) throws RoutingServiceException {
		return getService().getCancelOrderByArea(batchId);
	}
	
}