package com.freshdirect.transadmin.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.freshdirect.transadmin.dao.IDashboardDataDAO;
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
	public Collection getPlantCapacity(String dayOfWeek) {
		try {
			return getDashboardDataDAO().getPlantCapacity(dayOfWeek);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void savePlantCapacity(String dayOfWeek, List<PlantCapacity> capacity) {
		try {
			getDashboardDataDAO().purgePlantCapacity(dayOfWeek);
			getDashboardDataDAO().savePlantCapacity(dayOfWeek, capacity);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Collection getPlantDispatch() {
		try {
			return getDashboardDataDAO().getPlantDispatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void savePlantDispatch(List<PlantDispatch> dispatch) {
		try {
			getDashboardDataDAO().purgePlantDispatch();
			getDashboardDataDAO().savePlantCapacity(dispatch);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
