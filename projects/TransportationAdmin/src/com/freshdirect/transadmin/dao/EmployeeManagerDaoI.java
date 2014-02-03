package com.freshdirect.transadmin.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.KronosEmployeeInfo;

public interface EmployeeManagerDaoI {

	List<EmployeeInfo> getAllEmployees() throws DataAccessException;
	
	Collection getEmployees() throws DataAccessException;

	Map<String, EmployeeInfo> getTerminatedEmployees() throws DataAccessException;
	
	Collection getSupervisors() throws DataAccessException;
	
	public Collection getActiveInactiveEmployees() ;
	
	public void refresh(final String worktable) throws DataAccessException;
	
	List<KronosEmployeeInfo> getAllKronosEmployees() throws DataAccessException;

	void syncEmployees(List<KronosEmployeeInfo> inserts,
			List<KronosEmployeeInfo> updates, List<KronosEmployeeInfo> deletes) throws SQLException;

}
