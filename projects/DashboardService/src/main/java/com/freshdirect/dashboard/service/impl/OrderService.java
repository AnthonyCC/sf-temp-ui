package com.freshdirect.dashboard.service.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.dashboard.dao.IOrderRateDAO;
import com.freshdirect.dashboard.drools.decisiontable.ProjectedUtilizationRuleDT;
import com.freshdirect.dashboard.exception.FDServiceException;
import com.freshdirect.dashboard.exception.IIssue;
import com.freshdirect.dashboard.model.DashboardSummary;
import com.freshdirect.dashboard.model.OrderRateVO;
import com.freshdirect.dashboard.model.ProjectedUtilizationBase;
import com.freshdirect.dashboard.model.ProjectedUtilizationVO;
import com.freshdirect.dashboard.model.TimeslotModel;
import com.freshdirect.dashboard.service.IOrderService;
import com.freshdirect.dashboard.util.DateUtil;
import com.freshdirect.dashboard.util.OrderRateUtil;

@Service
public class OrderService implements IOrderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
			
	@Autowired
	private IOrderRateDAO orderRateDAO;
	
	@Autowired
	ProjectedUtilizationRuleDT ruleRunner;
	
	@Override
	public List<ProjectedUtilizationBase> runCapacitySuggestedActionLogic(List<ProjectedUtilizationBase> projectionLst) {
	  LOGGER.debug("Running projection rule logic - projection Route");
	  try {
		ruleRunner.executeSuggestedActionRule(projectionLst);
		if(projectionLst != null) {
			for(ProjectedUtilizationBase _pu : projectionLst){
				if(_pu.getResourceCnt() <= 0)
					_pu.setSuggestedAction(null);
			}
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
	}
	  return projectionLst;
	}
	
	public List<ProjectedUtilizationVO> getProjectedUtilization(String deliveryDate) throws FDServiceException {
		
		try {
			return orderRateDAO.getProjectedUtilization(deliveryDate);
		} catch(SQLException e) {
			throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
		}		
	}
	
	public List<String> getBaseDates(String deliveryDate) throws FDServiceException {
		List<String> baseDates = new ArrayList<String>(2);
		try {
			Set<Date> exceptions = orderRateDAO.getExceptions();
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(DateUtil.getDate(deliveryDate));
			
			baseDates.add(DateUtil.getDate(OrderRateUtil.getSample(cal, exceptions)));
			baseDates.add(DateUtil.getDate(OrderRateUtil.getSample(cal, exceptions)));
			Collections.sort(baseDates);
		} catch(SQLException e) {
			throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
		}	
		return baseDates;
	}
	
	public Map<Date, String> getCutoffs() throws FDServiceException {
		try {
			return orderRateDAO.getCutoffs();
		} catch(SQLException e) {
			throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
		}
	}
	
	public List<TimeslotModel> getOrderMetricsByTimeslot(String deliveryDate, String zone) throws FDServiceException {
		
		try {
			return orderRateDAO.getOrderMetricsByTimeslot(deliveryDate, zone);
		} catch(SQLException e) {
			throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
		}		
	}
	
	public Set<Date> getExceptions() throws FDServiceException {
		try {
			return orderRateDAO.getExceptions();
		} catch(SQLException e) {
			throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
		}
	}
	
	public List<OrderRateVO> getForecast(String deliveryDate, String zone,
			String prevDay1, String prevDay2) throws FDServiceException {
		
		List<OrderRateVO> dataList = null;
		try {
			LOGGER.info("Start getForecast. ");
			long start_time = System.currentTimeMillis();

			Calendar cal = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

			Date date1, date2;
			final Date date;

			date = df.parse(deliveryDate);

			cal.setTime(df.parse(deliveryDate));

			Set<Date> exceptions = orderRateDAO.getExceptions();

			if (prevDay1 == null || prevDay2 == null || "".equals(prevDay2)
					|| "".equals(prevDay1)) {
				date1 = OrderRateUtil.getSample(cal, exceptions);
				date2 = OrderRateUtil.getSample(cal, exceptions);
			} else {
				date1 = df.parse(prevDay1);
				date2 = df.parse(prevDay2);
			}

			LOGGER.info("Day1 " + date1 + " Day2 " + date2 + " Delivery Date "
					+ deliveryDate + " Zone " + zone + " "
					+ Long.toString(System.currentTimeMillis() - start_time));

			dataList = orderRateDAO.getForecastData(deliveryDate, date, zone, date1, date2);
			
			long end_time = System.currentTimeMillis();

			LOGGER.info("End getForecast. Day1 " + date1 + " Day2 " + date2
					+ " Delivery Date " + deliveryDate + " Zone " + zone
					+ " Total Time " + Long.toString(end_time - start_time));

		} catch (SQLException e) {
			throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
		}
		return dataList;
	}	

	public List<OrderRateVO> getCurrentOrderRateBySnapshot(
			final String currentDate, final String deliveryDate,
			final String zone) throws FDServiceException {
		
		List<OrderRateVO> dataList = null;
		try {
			LOGGER.info("Start CurrentOrderRateBySnapshot. ");
			long start_time = System.currentTimeMillis();
			
			dataList = orderRateDAO.getCurrentOrderRateBySnapshot(currentDate, deliveryDate, zone);
			
			long end_time = System.currentTimeMillis();
			
			LOGGER.info("End CurrentOrderRateBySnapshot. currentDate " + currentDate + " deliveryDate " + deliveryDate
					+ " Zone " + zone
					+ " Total Time " + Long.toString(end_time - start_time));
		} catch (SQLException e) {
			throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
		}
		return dataList;
	}
	
	public String getRefreshTime() {
		DateFormat df = new SimpleDateFormat("hh:mm a");
		return df.format(new Date());
	}

	@Override
	public DashboardSummary getDashboardSummary(String deliveryDate)
			throws FDServiceException {
		try{
			return orderRateDAO.getDashboardSummary(deliveryDate);
		} catch (SQLException e) {
			throw new FDServiceException(e, IIssue.ORDERRATE_DATA_ERROR);
		}
	}
	
}
