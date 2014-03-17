package com.freshdirect.dashboard.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.dashboard.model.BounceData;

public interface IBounceDAO {
	
	Map<String, Map<Date, Integer>> getRealTimeBounceByZone(final String deliveryDate) throws SQLException;
	
	Map<String, Map<Date, Integer>> getCustomerVisitCnt(final String deliveryDate) throws SQLException;
	
	Map<String, Map<Date, Integer>> getCustomerSoldOutWindowCnt(final String deliveryDate) throws SQLException;	
	
	Map<String, Map<Date, Integer>> getSOWByZone(final String deliveryDate) throws SQLException;
	
	public List<BounceData> getBounceByZone(final String deliveryDate, final String zone);	
	

}