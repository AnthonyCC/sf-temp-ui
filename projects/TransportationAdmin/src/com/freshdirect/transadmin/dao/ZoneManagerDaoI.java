package com.freshdirect.transadmin.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

public interface ZoneManagerDaoI {
	
	Collection getActiveZoneCodes() throws DataAccessException;
}
