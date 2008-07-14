package com.freshdirect.transadmin.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.TrnDispatch;
import com.freshdirect.transadmin.model.TrnDispatchPlan;

public interface DispatchManagerDaoI extends BaseManagerDaoI {

	Collection getPlan(String day, String zone, String date) throws DataAccessException;
	
	Collection getPlanList(String date) throws DataAccessException;

	Collection getDispatchList(String date, String zone) throws DataAccessException;

	TrnDispatch getDispatch(String planId, String date) throws DataAccessException;

	Collection getPlan() throws DataAccessException;
	
	Collection getPlan(String dateRange, String zoneLst) throws DataAccessException;

	TrnDispatchPlan getPlan(String id) throws DataAccessException;

	Collection getDrivers() throws DataAccessException;

	Collection getHelpers() throws DataAccessException;
}
