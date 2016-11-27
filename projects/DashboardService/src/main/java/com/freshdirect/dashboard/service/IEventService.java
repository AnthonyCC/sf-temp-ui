package com.freshdirect.dashboard.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.dashboard.exception.FDServiceException;
import com.freshdirect.dashboard.model.BounceData;
import com.freshdirect.dashboard.model.RollData;

public interface IEventService {

	Map<String, Map<Date, Integer>> getRealTimeBounceByZone(final String deliveryDate) throws FDServiceException;
	
	Map<String, Map<Date, Integer>> getCustomerVisitCnt(String deliveryDate) throws FDServiceException;

	Map<String, Map<Date, Integer>> getCustomerSoldOutWindowCnt(String deliveryDate) throws FDServiceException;
	
	Map<String, Map<Date, Integer>> getSOWByZone(String deliveryDate) throws FDServiceException;
	
	List<BounceData> getBounceByZone(String deliveryDate, String zone);

	List<RollData> getRollByZone(String deliveryDate, String zone);	
}