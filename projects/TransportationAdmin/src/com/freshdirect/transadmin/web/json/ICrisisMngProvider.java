package com.freshdirect.transadmin.web.json;

import java.util.List;

import com.freshdirect.transadmin.web.model.CrisisManagerBatchInfo;
import com.freshdirect.transadmin.web.model.CrisisManagerBatchStandingOrderInfo;
import com.freshdirect.transadmin.web.model.CrisisManagerBatchTimeslotInfo;


public interface ICrisisMngProvider {
		
	List<CrisisManagerBatchInfo> getOrderCrisisBatch(String deliveryDate);
	
	CrisisManagerBatchInfo getOrderCrisisBatchById(String batchId);
	
	boolean doOrderCollectionIn(String orderCrisisBatchId);
	
	boolean doCrisisMngBatchCancel(String orderCrisisBatchId);
	
	boolean doCancelOrder(String orderCrisisBatchId, String[][] orders);
	
	String doCrisisMngCreateReservation(String batchId, String[][] timeslots);
	
	List<CrisisManagerBatchTimeslotInfo> getTimeslotExceptions(String batchId);
	
	List<String> getTimeslotByZone(String batchId, String zone); 
	
	List<CrisisManagerBatchStandingOrderInfo> getStandingOrderByBatchId(String batchId);
	
	String placeStandingOrder(String batchId, String[][] _standingOrderData, String[][] timeslot);
	
	boolean doCrisisMngBatchComplete(String batchId);
}
