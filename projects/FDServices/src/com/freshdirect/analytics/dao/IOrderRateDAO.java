package com.freshdirect.analytics.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.analytics.model.OrderData;
import com.freshdirect.analytics.model.OrderRateVO;
import com.freshdirect.analytics.model.PlantDispatchData;

public interface IOrderRateDAO {

	public List<OrderRateVO> getCurrentOrdersByZoneCutoff(
			final String deliveryDate);

	public List<OrderRateVO> getResourcesByZoneCutoff(final String deliveryDate);

	public List<OrderRateVO> getCurrentOrdersByZoneTimeslot(
			final String deliveryDate, final String zone);

	public List<OrderRateVO> getCapacityByZoneCutoff(final String deliveryDate);

	public List<OrderRateVO> getCapacityByZoneTimeslot(
			final String deliveryDate, final String zone);

	public List<OrderRateVO> getProjectedOrdersByZoneCutoff(
			final String deliveryDate);

	public Set<Date> getExceptions();

	public List<OrderRateVO> getForecastData(final String deliveryDate,
			final Date baseDate, final String zone, Date day1, Date day2);

	public List<OrderRateVO> getCurrentOrderRateBySnapshot(
			final String currentDate, final String deliveryDate,
			final String zone) throws ParseException;

	public List<OrderData> getOrderCount(final String currentDate,
			final String deliveryDate, final String zone) throws ParseException;

	public List<PlantDispatchData> getPlantDispatchData(String deliveryDate);

}