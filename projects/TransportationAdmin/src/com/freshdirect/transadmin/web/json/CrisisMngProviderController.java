package com.freshdirect.transadmin.web.json;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.CrisisManagerBatchDeliverySlot;
import com.freshdirect.routing.model.ICrisisManagerBatch;
import com.freshdirect.routing.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.routing.model.ICrisisManagerBatchOrder;
import com.freshdirect.routing.model.IStandingOrderModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.CrisisManagerServiceProxy;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerCancelAction;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerCreateReservationAction;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerOrderCancelAction;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerOrderInAction;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerPlaceOrderAction;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.CrisisManagerBatchInfo;
import com.freshdirect.transadmin.web.model.CrisisManagerBatchStandingOrderInfo;
import com.freshdirect.transadmin.web.model.CrisisManagerBatchTimeslotInfo;


public class CrisisMngProviderController extends BaseJsonRpcController  implements ICrisisMngProvider {
	
	private static Logger LOGGER = LoggerFactory.getInstance(CrisisMngProviderController.class);
	
	private class OrderCrisisBatchInfoComparator implements Comparator<CrisisManagerBatchInfo> {

		public int compare(CrisisManagerBatchInfo batch1, CrisisManagerBatchInfo batch2) {
			if(batch1.getLastActionDateTime() != null &&  batch2.getLastActionDateTime() != null) {
				return -(batch1.getLastActionDateTime().compareTo(batch2.getLastActionDateTime()));
			}
			return 0;
		}
	}
	
	private class CrisisManagerBatchTimeslotInfoComparator implements Comparator<CrisisManagerBatchTimeslotInfo> {

		public int compare(CrisisManagerBatchTimeslotInfo t1, CrisisManagerBatchTimeslotInfo t2) {
			if(t1.getArea() != null &&  t2.getArea() != null) {
				return -(t2.getArea().compareTo(t1.getArea()));
			}
			return 0;
		}
	}
	
	public CrisisManagerBatchInfo getOrderCrisisBatchById(String batchId) {
		
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
		try {
			ICrisisManagerBatch batch = proxy.getCrisisMngBatchById(batchId);
			batch.setOrder(proxy.getCrisisMngBatchOrders(batchId, true, false));
			batch.setCancelOrder(proxy.getCancelOrderByArea(batchId));
			batch.setActiveOrder(proxy.getActiveOrderByArea(batch.getDeliveryDate()));
			return new CrisisManagerBatchInfo(batch);
			
		} catch (RoutingServiceException e) {
			LOGGER.error("routing service exception", e);
		} 
		return null;
	}
	
	public List<CrisisManagerBatchInfo> getOrderCrisisBatch(String deliveryDate) {
		
		List<CrisisManagerBatchInfo> result = new ArrayList<CrisisManagerBatchInfo>();
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
		try {
		
			Set<ICrisisManagerBatch> lstBatch = null;
			if(TransStringUtil.isEmpty(deliveryDate)) {
				lstBatch = proxy.getCrisisMngBatch(null);
			} else {
				lstBatch = proxy.getCrisisMngBatch(TransStringUtil.getDate(deliveryDate));
			}			
			
			if(lstBatch != null) {
				for(ICrisisManagerBatch batch : lstBatch) {					
					CrisisManagerBatchInfo _info = new CrisisManagerBatchInfo(batch);
					result.add(_info);
				}
			}
		
		} catch (RoutingServiceException e) {
			e.printStackTrace();
			LOGGER.error("Routing service exception", e);
		} catch (ParseException ex) {
			ex.printStackTrace();
			LOGGER.error("parse exception", ex);
		}
		Collections.sort(result, new OrderCrisisBatchInfoComparator());	
		return result;
		
	}
	
	public boolean doOrderCollectionIn(String orderCrisisBatchId) {
		
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
		ICrisisManagerBatch batch;
		try {
			batch = proxy.getCrisisMngBatchById(orderCrisisBatchId);
			CrisisManagerOrderInAction process = new CrisisManagerOrderInAction(batch, userId);
			process.execute();
		} catch (RoutingServiceException e) {
			LOGGER.error("<doOrderCollectionIn:> Routing service exception", e);
		}
		
		return true;
	}
	
	public boolean doCrisisMngBatchCancel(String orderCrisisBatchId) {
		
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
		ICrisisManagerBatch batch;
		try {
			batch = proxy.getCrisisMngBatchById(orderCrisisBatchId);
			CrisisManagerCancelAction process = new CrisisManagerCancelAction(batch, userId);
			process.execute();
		} catch (RoutingServiceException e) {
			LOGGER.error("<doOrderScenarioCancel:> Routing service exception", e);
		}
		
		return true;
	}
	
	public boolean doCancelOrder(String orderCrisisBatchId, String[][] orders){
		
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
		ICrisisManagerBatch batch;
		try {
			batch = proxy.getCrisisMngBatchById(orderCrisisBatchId);
			List<ICrisisManagerBatchOrder> cancelOrders = proxy.getCrisisMngBatchOrders(orderCrisisBatchId, false, true);			
						
			CrisisManagerOrderCancelAction process = new CrisisManagerOrderCancelAction(batch, userId, cancelOrders);
			process.execute();
		} catch (RoutingServiceException e) {
			LOGGER.error("<doOrderCollectionIn:> Routing service exception", e);
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public String doCrisisMngCreateReservation(String batchId, boolean isExceptionCheck, String[][] timeslot){
		
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
		ICrisisManagerBatch batch;
		try {
			batch = proxy.getCrisisMngBatchById(batchId);
			String normalDate = TransStringUtil.getDate(TransStringUtil.getNormalDate());
			List<ICrisisManagerBatchDeliverySlot> exceptionSlots = new ArrayList<ICrisisManagerBatchDeliverySlot>();
			
			if(!isExceptionCheck && timeslot != null){
				Map<String, List<ICrisisManagerBatchDeliverySlot>> deliverySlots 
												= proxy.getTimeslotByDate(batch.getDestinationDate());

				for(int i= 0;i < timeslot.length;i++){					
					ICrisisManagerBatchDeliverySlot _slot = new CrisisManagerBatchDeliverySlot();
					exceptionSlots.add(_slot);
					_slot.setBatchId(batchId);
					_slot.setArea(timeslot[i][0]);
					_slot.setStartTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][1].substring(0, 8)));
					_slot.setEndTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][1].substring(11, 19)));
					_slot.setDestStartTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][2].substring(0, 8)));
					_slot.setDestEndTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][2].substring(11, 19)));
				
					if(deliverySlots != null && deliverySlots.get(timeslot[i][0]) != null){				
						for(ICrisisManagerBatchDeliverySlot _deliverySlot : deliverySlots.get(timeslot[i][0])){
							if(_slot.getDestStartTime().equals(_deliverySlot.getStartTime())
									&& _slot.getDestEndTime().equals(_deliverySlot.getEndTime())){
								_slot.setTimeSlotId(_deliverySlot.getTimeSlotId());
								break;
							}
						}
					}
				}		
				
				proxy.updateCrisisMngBatchDeliveryslot(exceptionSlots);
			}
			
			CrisisManagerCreateReservationAction process = new CrisisManagerCreateReservationAction(batch, userId, isExceptionCheck);
			
			Map<String, Integer> exceptions = (Map<String, Integer>) process.execute();
			boolean hasError = (exceptions != null && exceptions.keySet().size() > 0);
			if(hasError) {
				StringBuffer exceptionMessage = new StringBuffer();
				exceptionMessage.append("Below are the list of Timeslot exceptions: \n\n");
				exceptionMessage.append("Area - No of Timeslots \n");
				for(Map.Entry<String, Integer> exp : exceptions.entrySet()) {					
					exceptionMessage.append("\n").append(exp.getKey()+"  =  "+exp.getValue());
				}
				if(!isExceptionCheck) {
					exceptionMessage.append("\n\n"+"Please handle timeslot exceptions as reservations can't be created for orders cancelled. Do you want to continue?");
				} 
				return exceptionMessage.toString();
			}
		} catch (RoutingServiceException e) {
			e.printStackTrace();
			LOGGER.error("<doCreateReservation:> Routing service exception", e);
		} catch (ParseException e) {
			LOGGER.error("parse exception", e);
		}
		return null;
	}
	
	public List<CrisisManagerBatchTimeslotInfo> getTimeslotExceptions(String batchId) {
		
		List<CrisisManagerBatchTimeslotInfo> result = new ArrayList<CrisisManagerBatchTimeslotInfo>();
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
		Map<String, List<ICrisisManagerBatchDeliverySlot>> exceptionSlots 
							= proxy.getCrisisMngBatchTimeslot(batchId, false);
		if(exceptionSlots != null){
			for(Map.Entry<String, List<ICrisisManagerBatchDeliverySlot>> exceptionEntry : exceptionSlots.entrySet()){
				
				for(ICrisisManagerBatchDeliverySlot _slot : exceptionEntry.getValue()){
					result.add(new CrisisManagerBatchTimeslotInfo(_slot));
				}
			}			
		}	
		Collections.sort(result, new CrisisManagerBatchTimeslotInfoComparator());	
		return result;
	}
	
	public List<String> getTimeslotByZone(String batchId, String zone) {
		List<String> r = new ArrayList<String>();
		List<CrisisManagerBatchTimeslotInfo> result = new ArrayList<CrisisManagerBatchTimeslotInfo>();

		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
		ICrisisManagerBatch batch;
		batch = proxy.getCrisisMngBatchById(batchId);
		
		Map<String, List<ICrisisManagerBatchDeliverySlot>> timeSlots 
							= proxy.getTimeslotByDate(batch.getDestinationDate());
		if(timeSlots != null && timeSlots.get(zone) != null){				
				for(ICrisisManagerBatchDeliverySlot _slot : timeSlots.get(zone)){
					result.add(new CrisisManagerBatchTimeslotInfo(_slot));
				}
		}	
		Collections.sort(result, new CrisisManagerBatchTimeslotInfoComparator());
		
		if(result != null && result.size() > 0){
			Iterator<CrisisManagerBatchTimeslotInfo> itr = result.iterator();
			while(itr.hasNext()){
				CrisisManagerBatchTimeslotInfo _slot = itr.next();
				r.add(_slot.getTimeslot());
			}
		}
		return r;
	}
	
	public List<CrisisManagerBatchStandingOrderInfo> getStandingOrderByBatchId(String batchId){
		
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
		List<CrisisManagerBatchStandingOrderInfo> standingOrders = new ArrayList<CrisisManagerBatchStandingOrderInfo>();
		List<IStandingOrderModel> batchSOList = proxy.getStandingOrderByBatchId(batchId);
		Iterator<IStandingOrderModel> itr =  batchSOList.iterator();
		while(itr.hasNext()){
			IStandingOrderModel model = itr.next();			
			standingOrders.add(new CrisisManagerBatchStandingOrderInfo(model));
		}
		return standingOrders;
	}
	
	public boolean placeStandingOrder(String batchId, String[][] _standingOrderData){
		
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
		List<IStandingOrderModel> standingOrders = new ArrayList<IStandingOrderModel>();
		ICrisisManagerBatch batch;
		
		try {
			batch = proxy.getCrisisMngBatchById(batchId);
			List<IStandingOrderModel> batchSOList = proxy.getStandingOrderByBatchId(batchId);
			
			for(int i= 0;i < _standingOrderData.length;i++){		
				Iterator<IStandingOrderModel> itr =  batchSOList.iterator();
				while(itr.hasNext()){
					IStandingOrderModel model = itr.next();
					if(model != null && model.getId().equalsIgnoreCase(_standingOrderData[i][0])){
						model.setAltDeliveryDate(batch.getDestinationDate());
						standingOrders.add(model);
						break;
					}
				}
			}
			CrisisManagerPlaceOrderAction process = new CrisisManagerPlaceOrderAction(batch, userId, standingOrders);
			process.execute();
		} catch (RoutingServiceException e) {
			e.printStackTrace();
			LOGGER.error("<placeStandingOrder:> Routing service exception", e);
			return false;
		} 	
		
		return true;
	}
}
