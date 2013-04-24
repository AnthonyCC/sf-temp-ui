package com.freshdirect.transadmin.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.freshdirect.framework.service.ServiceException;
import com.freshdirect.transadmin.dao.IDashboardDataDAO;
import com.freshdirect.transadmin.model.OrderRateException;
import com.freshdirect.transadmin.model.PlantCapacity;
import com.freshdirect.transadmin.model.PlantDispatch;
import com.freshdirect.transadmin.service.IDashboardManager;

public class DashboardManagerImpl implements IDashboardManager {
	
	private IDashboardDataDAO dashboardDataDAO;

	public IDashboardDataDAO getDashboardDataDAO() {
		return dashboardDataDAO;
	}

	public void setDashboardDataDAO(IDashboardDataDAO dashboardDataDAO) {
		this.dashboardDataDAO = dashboardDataDAO;
	}


	@Override
	public Collection getPlantCapacity(String dayOfWeek) throws ServiceException{
		try {
			return getDashboardDataDAO().getPlantCapacity(dayOfWeek);
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void savePlantCapacity(String dayOfWeek, List<PlantCapacity> capacity) throws ServiceException{
		try {
			getDashboardDataDAO().purgePlantCapacity(dayOfWeek);
			getDashboardDataDAO().savePlantCapacity(dayOfWeek, capacity);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Collection getPlantDispatch() throws ServiceException {
		try {
			return getDashboardDataDAO().getPlantDispatch();
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void savePlantDispatch(List<PlantDispatch> dispatch) throws ServiceException{
		try {
			getDashboardDataDAO().purgePlantDispatch();
			getDashboardDataDAO().savePlantCapacity(dispatch);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Collection getOrderRateExceptions() throws ServiceException {
		try {
			return getDashboardDataDAO().getOrderRateExceptions();
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveOrderRateExceptions(List<OrderRateException> exceptions)
			throws ServiceException {
		try {
			getDashboardDataDAO().purgeOrderRateExceptions();
			getDashboardDataDAO().saveOrderRateExceptions(exceptions);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
