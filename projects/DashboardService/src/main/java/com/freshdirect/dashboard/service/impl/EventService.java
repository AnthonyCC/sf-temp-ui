package com.freshdirect.dashboard.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.dashboard.dao.IBounceDAO;
import com.freshdirect.dashboard.dao.IRollDAO;
import com.freshdirect.dashboard.exception.FDServiceException;
import com.freshdirect.dashboard.exception.IIssue;
import com.freshdirect.dashboard.model.BounceData;
import com.freshdirect.dashboard.model.RollData;
import com.freshdirect.dashboard.service.IEventService;


@Service
public class EventService implements IEventService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);
	
	@Autowired
	private IBounceDAO bounceDAO;
	
	@Autowired 
	private IRollDAO rollDAO;
	
	
	public Map<String, Map<Date, Integer>> getRealTimeBounceByZone(String deliveryDate) throws FDServiceException {
		
		try {
			return bounceDAO.getRealTimeBounceByZone(deliveryDate);
		} catch(SQLException e) {
			throw new FDServiceException(e, IIssue.BOUNCE_DATA_ERROR);
		}
	}
	
	public Map<String, Map<Date, Integer>> getCustomerVisitCnt(String deliveryDate) throws FDServiceException {
		
		try {
			return bounceDAO.getCustomerVisitCnt(deliveryDate);
		} catch(SQLException e) {
			throw new FDServiceException(e, IIssue.BOUNCE_DATA_ERROR);
		}
	}

	public Map<String, Map<Date, Integer>> getCustomerSoldOutWindowCnt(String deliveryDate) throws FDServiceException {
		
		try {
			return bounceDAO.getCustomerSoldOutWindowCnt(deliveryDate);
		} catch(SQLException e) {
			throw new FDServiceException(e, IIssue.BOUNCE_DATA_ERROR);
		}
	}
	
	public Map<String, Map<Date, Integer>> getSOWByZone(String deliveryDate) throws FDServiceException {
		
		try {
			return bounceDAO.getSOWByZone(deliveryDate);
		} catch(SQLException e) {
			throw new FDServiceException(e, IIssue.BOUNCE_DATA_ERROR);
		}
	}
		
	public List<BounceData> getBounceByZone(String deliveryDate,String zone) {
		
		LOGGER.info("Begin getBounce. Delivery Date "+deliveryDate+" Zone "+zone);
		long start_time = System.currentTimeMillis();

		List<BounceData> dataList = null;
		dataList = bounceDAO.getBounceByZone(deliveryDate,zone);
				
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getBounce. Delivery Date "+deliveryDate+" Zone "+zone+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}
	
	public List<RollData> getRollByZone(String deliveryDate, String zone) {
		
		List<RollData> dataList = null;
		LOGGER.info("Begin getRoll. Delivery Date "+deliveryDate+" Zone "+zone);
		long start_time = System.currentTimeMillis();

		dataList = rollDAO.getRollByZone(deliveryDate,zone);
		
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getRoll. Delivery Date "+deliveryDate+" Zone "+zone+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}

}
