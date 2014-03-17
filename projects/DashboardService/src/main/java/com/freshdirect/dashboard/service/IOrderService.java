package com.freshdirect.dashboard.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.dashboard.exception.FDServiceException;
import com.freshdirect.dashboard.model.DashboardSummary;
import com.freshdirect.dashboard.model.OrderRateVO;
import com.freshdirect.dashboard.model.ProjectedUtilizationBase;
import com.freshdirect.dashboard.model.ProjectedUtilizationVO;
import com.freshdirect.dashboard.model.TimeslotModel;

public interface IOrderService {
	
	List<ProjectedUtilizationVO> getProjectedUtilization(String deliveryDate) throws FDServiceException;
		
	Map<Date, String> getCutoffs() throws FDServiceException;
	 
	List<String> getBaseDates(String deliveryDate) throws FDServiceException;
	
	List<TimeslotModel> getOrderMetricsByTimeslot(final String deliveryDate, final String zone) throws FDServiceException;
	
	List<OrderRateVO> getForecast(String deliveryDate, String zone,
			String prevDay1, String prevDay2) throws FDServiceException;
	
	List<OrderRateVO> getCurrentOrderRateBySnapshot(
			final String currentDate, final String deliveryDate,
			final String zone) throws FDServiceException;

	List<ProjectedUtilizationBase> runCapacitySuggestedActionLogic(
			List<ProjectedUtilizationBase> projectionLst);
	
	DashboardSummary getDashboardSummary(String deliveryDate) throws FDServiceException;
	
	Set<Date> getExceptions() throws FDServiceException;

}
