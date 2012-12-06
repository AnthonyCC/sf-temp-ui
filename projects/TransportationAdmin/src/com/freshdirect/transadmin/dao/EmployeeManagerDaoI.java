package com.freshdirect.transadmin.dao;

import java.util.Collection;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.EmployeeInfo;

public interface EmployeeManagerDaoI {

	Collection getEmployees() throws DataAccessException;

	Map<String, EmployeeInfo> getTerminatedEmployees() throws DataAccessException;
	
	Collection getSupervisors() throws DataAccessException;
	
	public Collection getActiveInactiveEmployees() ;
	
	public void refresh(final String worktable) throws DataAccessException;
	

}
