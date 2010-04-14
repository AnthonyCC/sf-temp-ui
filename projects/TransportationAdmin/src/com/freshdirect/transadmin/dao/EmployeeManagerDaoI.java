package com.freshdirect.transadmin.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

public interface EmployeeManagerDaoI {

	Collection getEmployees() throws DataAccessException;

	Collection getTerminatedEmployees() throws DataAccessException;
	
	Collection getSupervisors() throws DataAccessException;
	
	public Collection getActiveInactiveEmployees() ;

}
