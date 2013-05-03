package com.freshdirect.webservices;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

import com.freshdirect.analytics.model.*;

public interface EventService {

	public List<BounceData> getBounce(String deliveryDate, String zone);

	public List<RollData> getRoll(String deliveryDate, String zone);

	public List<BounceData> getBounceByZone(String deliveryDate);

	public List<RollData> getRollByZone(String deliveryDate);

	public List<OrderRateVO> getCurrentOrdersByZoneCutoff(String deliveryDate);

	public List<OrderRateVO> getResourcesByZoneCutoff(String deliveryDate);

	public List<OrderRateVO> getCurrentOrdersByZoneTimeslot(
			String deliveryDate, String zone);

	public List<OrderRateVO> getCapacityByZoneTimeslot(String deliveryDate,
			String zone);

	public List<OrderRateVO> getCapacityByZoneCutoff(String deliveryDate);

	public List<OrderRateVO> getProjectedOrdersByZoneCutoff(String deliveryDate);

	public List<OrderRateVO> getCurrentOrderRateBySnapshot(String currentDate,
			String deliveryDate, String zone) throws ParseException;

	public List<OrderData> getOrderCount(String currentDate,
			String deliveryDate, String zone) throws ParseException;

	public List<PlantDispatchData> getPlantDispatchData(String deliveryDate);

	public String getRefreshTime();
	
	public List<String> getBaseDates(String deliveryDate) throws ParseException;

	public List<OrderRateVO> getForecast(String deliveryDate, String zone,
			String prevDay1, String prevDay2) throws ParseException;
	String getConnection() throws Exception;
}