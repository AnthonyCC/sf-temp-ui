package com.freshdirect.transadmin.service.impl;

import java.util.Collection;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.DispatchManagerDaoI;
import com.freshdirect.transadmin.model.TrnDispatch;
import com.freshdirect.transadmin.model.TrnDispatchPlan;
import com.freshdirect.transadmin.service.DispatchManagerI;

public class DispatchManagerImpl extends BaseManagerImpl implements DispatchManagerI {
	
	private DispatchManagerDaoI dispatchManagerDao = null;
	
	public DispatchManagerDaoI getDispatchManagerDao() {
		return dispatchManagerDao;
	}

	public void setDispatchManagerDao(DispatchManagerDaoI dispatchManagerDao) {
		this.dispatchManagerDao = dispatchManagerDao;
	}
	
	protected BaseManagerDaoI getBaseManageDao() {
		return getDispatchManagerDao();
	}

	public Collection getDrivers() {
					
		return getDispatchManagerDao().getDrivers(); 
	}
	
	public Collection getHelpers() {
		
		return getDispatchManagerDao().getHelpers();
	}	
		
	public Collection getPlan() {
		
		return getDispatchManagerDao().getPlan();
	}
	
	public Collection getPlan(String dateRange, String zoneLst) {
		return getDispatchManagerDao().getPlan(dateRange, zoneLst);
	}
	
	public Collection getPlanList(String date) {
		return getDispatchManagerDao().getPlanList(date);
	}
	
	public Collection getPlan(String day, String zone, String date) {
		
		return getDispatchManagerDao().getPlan(day, zone, date);
	}
	
	public Collection getDispatchList(String date, String zone) {
		
		return getDispatchManagerDao().getDispatchList(date, zone);
	}
	
	public TrnDispatch getDispatch(String planId, String date) {
		
		return getDispatchManagerDao().getDispatch(planId, date);
	}
	
	public TrnDispatchPlan getPlan(String id)  {
		
		return getDispatchManagerDao().getPlan(id);	
	}
	
	public void copyPlan(Collection addPlanList, Collection removePlanList) {
		this.removeEntity(removePlanList);
		this.saveEntityList(addPlanList);
	}
}
