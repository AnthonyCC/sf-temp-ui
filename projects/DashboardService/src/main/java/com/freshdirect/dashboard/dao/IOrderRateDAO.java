package com.freshdirect.dashboard.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.dashboard.model.DashboardSummary;
import com.freshdirect.dashboard.model.OrderRateVO;
import com.freshdirect.dashboard.model.ProjectedUtilizationVO;
import com.freshdirect.dashboard.model.TimeslotModel;

public interface IOrderRateDAO {

	
	List<ProjectedUtilizationVO> getProjectedUtilization(final String deliveryDate) throws SQLException;
	
	public Set<Date> getExceptions() throws SQLException;

	public List<OrderRateVO> getForecastData(final String deliveryDate,
			final Date baseDate, final String zone, Date day1, Date day2)  throws SQLException, ParseException;
	
	Map<Date, String> getCutoffs() throws SQLException;
	
	List<TimeslotModel> getOrderMetricsByTimeslot(final String deliveryDate, final String zone) throws SQLException;
	
	List<OrderRateVO> getCurrentOrderRateBySnapshot(
			final String currentDate, final String deliveryDate,
			final String zone) throws ParseException, SQLException;
	
	DashboardSummary getDashboardSummary(final String deliveryDate) throws SQLException;
	
	List<OrderRateVO> getPlannedCapacityByZone(final Date deliveryDate) throws SQLException;
}