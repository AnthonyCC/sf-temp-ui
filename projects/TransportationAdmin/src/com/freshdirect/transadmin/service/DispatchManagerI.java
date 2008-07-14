package com.freshdirect.transadmin.service;

import java.util.Collection;

import com.freshdirect.transadmin.model.TrnDispatch;
import com.freshdirect.transadmin.model.TrnDispatchPlan;

public interface DispatchManagerI extends BaseManagerI {
	
	Collection getPlan(String day, String zone, String date);
	
	Collection getPlanList(String date);
	
	Collection getDispatchList(String date, String zone);
	
	TrnDispatch getDispatch(String planId, String date);
	
	Collection getPlan(String dateRange, String zoneLst);
	
	Collection getPlan();
	
	TrnDispatchPlan getPlan(String id);
	
	Collection getDrivers();
	
	Collection getHelpers();
	
	void copyPlan(Collection addPlanList, Collection removePlanList);
}
