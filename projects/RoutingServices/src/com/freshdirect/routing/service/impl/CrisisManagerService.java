package com.freshdirect.routing.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.routing.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.routing.dao.ICrisisManagerDAO;
import com.freshdirect.routing.manager.IProcessMessage;
import com.freshdirect.routing.model.IActiveOrderModel;
import com.freshdirect.routing.model.ICancelOrderModel;
import com.freshdirect.routing.model.ICrisisManagerBatch;
import com.freshdirect.routing.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.routing.model.ICrisisManagerBatchOrder;
import com.freshdirect.routing.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.model.IStandingOrderModel;
import com.freshdirect.routing.model.TriggerCrisisManagerResult;
import com.freshdirect.routing.service.ICrisisManagerService;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class CrisisManagerService extends BaseService implements ICrisisManagerService {
	
	private ICrisisManagerDAO crisisManagerDAOImpl;

	public ICrisisManagerDAO getCrisisManagerDAOImpl() {
		return crisisManagerDAOImpl;
	}

	public void setCrisisManagerDAOImpl(ICrisisManagerDAO crisisManagerDAOImpl) {
		this.crisisManagerDAOImpl = crisisManagerDAOImpl;
	}

	public TriggerCrisisManagerResult createNewCrisisMngBatch(Date deliveryDate, Date destinationDate, String userId, String[] zone, 
			Date cutOffDateTime, Date startTime, Date endTime, String[] deliveryType, boolean includeSO, String profileName, boolean isStandByMode) throws RoutingServiceException{
		
		TriggerCrisisManagerResult result = new TriggerCrisisManagerResult();
		String batchId = null;
		try {
			List<String> messages = new ArrayList<String>();			
			
			if(RoutingServicesProperties.getRoutingCutOffStandByEnabled()) {
				messages.add(IProcessMessage.INFO_MESSAGE_STANDBYMODE);
				isStandByMode = true;
			}
			
			batchId = getCrisisManagerDAOImpl().addNewCrisisMngBatch(deliveryDate, destinationDate, zone
													,cutOffDateTime, startTime, endTime, deliveryType, includeSO, profileName, isStandByMode);
			getCrisisManagerDAOImpl().addNewCrisisMngBatchAction(batchId, RoutingDateUtil.getCurrentDateTime()
													, EnumCrisisMngBatchActionType.CREATE, userId);
			
			messages.add(IProcessMessage.INFO_MESSAGE_ORDERSCENARIOBATCHTRIGERRED);
			result.setCrisisMngBatchId(batchId);
			result.setMessages(messages);
			
		}catch (SQLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}catch (ParseException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
		
		return result;
	}
	
	public Set<ICrisisManagerBatch> getCrisisMngBatch(final Date deliveryDate) throws RoutingServiceException {
		try {
			return getCrisisManagerDAOImpl().getCrisisMngBatch(deliveryDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
	}
	
	public void updateCrisisMngBatchMessage(String orderScenarioBatchId, String message) throws RoutingServiceException {
		try {
			getCrisisManagerDAOImpl().updateCrisisMngBatchMessage(orderScenarioBatchId, message);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
	}
	
	public void updateCrisisMngBatchStatus(String crisisMngBatchId, EnumCrisisMngBatchStatus status) throws RoutingServiceException {
		try {
			getCrisisManagerDAOImpl().updateCrisisMngBatchStatus(crisisMngBatchId, status);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
	}
	
	public ICrisisManagerBatch getCrisisMngBatchById(String batchId) throws RoutingServiceException {
		try{
			return getCrisisManagerDAOImpl().getCrisisMngBatchById(batchId);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void addNewCrisisMngBatchAction(String crisisMngBatchId, Date actionDateTime, EnumCrisisMngBatchActionType actionType
			, String userId) throws RoutingServiceException{
		try{
			getCrisisManagerDAOImpl().addNewCrisisMngBatchAction(crisisMngBatchId, actionDateTime, actionType, userId);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void clearCrisisMngBatch(String crisisMngBatchId) throws RoutingServiceException{
		try{
			getCrisisManagerDAOImpl().clearCrisisMngBatchOrder(crisisMngBatchId);
			getCrisisManagerDAOImpl().clearCrisisMngBatchReservation(crisisMngBatchId);
			getCrisisManagerDAOImpl().clearCrisisMngBatchStandingOrder(crisisMngBatchId);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<ICrisisManagerBatchOrder> getOrderByCriteria(Date deliveryDate,Date cutOffDateTime, String[] area, String startTime, String endTime, String[] deliveryType, String profileName,boolean isSOIncluded) throws RoutingServiceException{
		try{
			return getCrisisManagerDAOImpl().getOrderByCriteria(deliveryDate, cutOffDateTime, area, startTime, endTime, deliveryType, profileName, isSOIncluded);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public Map<EnumSaleStatus, Integer> getOrderStatsByDate(Date deliveryDate, String batchId) throws RoutingServiceException {
		try{
			return getCrisisManagerDAOImpl().getOrderStatsByDate(deliveryDate, batchId);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void addNewCrisisMngBatchOrder(List<ICrisisManagerBatchOrder> batchOrders) throws RoutingServiceException {
		try{
			getCrisisManagerDAOImpl().addNewCrisisMngBatchOrder(batchOrders);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<ICrisisManagerBatchOrder> getCrisisMngBatchOrders(String batchId, boolean filterException, boolean filterOrder) throws RoutingServiceException {
		try{
			return getCrisisManagerDAOImpl().getCrisisMngBatchOrders(batchId, filterException, filterOrder);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngOrderException(String orderCrisisBatchId, List<String> exceptionOrderIds) throws RoutingServiceException {
		try{
			getCrisisManagerDAOImpl().updateCrisisMngOrderException(orderCrisisBatchId, exceptionOrderIds);			
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<ICrisisManagerBatchReservation> getReservationByCriteria(Date deliveryDate, Date cutOffDateTime, String[] area, String startTime, String endTime, String profileName) throws RoutingServiceException {
		try{
			return getCrisisManagerDAOImpl().getReservationByCriteria(deliveryDate, cutOffDateTime, area, startTime, endTime, profileName);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void addNewCrisisMngBatchReservation(List<ICrisisManagerBatchReservation> reservations) throws RoutingServiceException {
		try{
			getCrisisManagerDAOImpl().addNewCrisisMngBatchReservation(reservations);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public Map<String, ICrisisManagerBatchReservation> getCrisisMngBatchReservation(String batchId, boolean filterException) throws RoutingServiceException {
		try{
			return getCrisisManagerDAOImpl().getCrisisMngBatchReservation(batchId, filterException);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngReservationException(String crisisMngBatchId, List<String> exceptionRsvIds) throws RoutingServiceException {
		try{
			getCrisisManagerDAOImpl().updateCrisisMngReservationException(crisisMngBatchId, exceptionRsvIds);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngOrderStatus(String crisisMngBatchId, List<String> exceptionOrderIds) throws RoutingServiceException {
		try{
			getCrisisManagerDAOImpl().updateCrisisMngOrderStatus(crisisMngBatchId, exceptionOrderIds);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngReservationStatus(String crisisMngBatchId, List<String> exceptionRsvIds) throws RoutingServiceException {
		try{
			getCrisisManagerDAOImpl().updateCrisisMngReservationStatus(crisisMngBatchId, exceptionRsvIds);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslotByZone(String crisisMngBatchId) throws RoutingServiceException {
		try{
			return getCrisisManagerDAOImpl().getCrisisMngBatchTimeslotByZone(crisisMngBatchId);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getTimeslotByDate(Date deliveryDate) throws RoutingServiceException {
		try{
			return getCrisisManagerDAOImpl().getTimeslotByDate(deliveryDate);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void addCrisisMngBatchDeliveryslot(Map<String, List<ICrisisManagerBatchDeliverySlot>> groupedSlots) throws RoutingServiceException {
		try{
			getCrisisManagerDAOImpl().addCrisisMngBatchDeliveryslot(groupedSlots);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngBatchDeliveryslot(List<ICrisisManagerBatchDeliverySlot> groupedSlots) throws RoutingServiceException {
		try{
			getCrisisManagerDAOImpl().updateCrisisMngBatchDeliveryslot(groupedSlots);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslot(String batchId, boolean filterException) throws RoutingServiceException {
		try{
			return getCrisisManagerDAOImpl().getCrisisMngBatchTimeslot(batchId, filterException);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void clearCrisisMngBatchDeliverySlot(String batchId) throws RoutingServiceException {
		try{
			getCrisisManagerDAOImpl().clearCrisisMngBatchDeliverySlot(batchId);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}	
	
	public List<IStandingOrderModel> getStandingOrderByCriteria(Date deliveryDate,Date cutOffDateTime, String[] area, String startTime, String endTime, String[] deliveryType, String profileName, boolean isSOIncluded) throws RoutingServiceException{
		try{
			return getCrisisManagerDAOImpl().getStandingOrderByCriteria(deliveryDate, cutOffDateTime, area, startTime, endTime, deliveryType, profileName, isSOIncluded);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void addNewCrisisMngBatchStandingOrder(List<IStandingOrderModel> soOrders) throws RoutingServiceException{
		try{
			getCrisisManagerDAOImpl().addNewCrisisMngBatchStandingOrder(soOrders);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<IStandingOrderModel> getStandingOrderByBatchId(String batchId) throws RoutingServiceException{
		try{
			return getCrisisManagerDAOImpl().getStandingOrderByBatchId(batchId);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngBatchStandingOrder(String batchId, List<IStandingOrderModel> batchStandingOrders) throws RoutingServiceException {
		try{
			getCrisisManagerDAOImpl().updateCrisisMngBatchStandingOrder(batchId, batchStandingOrders);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<IActiveOrderModel> getActiveOrderByArea(Date deliveryDate) throws RoutingServiceException {
		try{
			return getCrisisManagerDAOImpl().getActiveOrderByArea(deliveryDate);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngBatchOrderReservation(String batchId, Map<String, String> batchOrderReservations) throws RoutingServiceException {
		try{
			getCrisisManagerDAOImpl().updateCrisisMngBatchOrderReservation(batchId, batchOrderReservations);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<ICancelOrderModel> getCancelOrderByArea(String batchId) throws RoutingServiceException {
		try{
			return getCrisisManagerDAOImpl().getCancelOrderByArea(batchId);
		}catch(SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
}
