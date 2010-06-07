package com.freshdirect.transadmin.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

public interface BaseManagerDaoI {

	Object saveEntity(Object entity) throws DataAccessException;

	void removeEntity(Collection employees) throws DataAccessException;
	
	void removeEntityEx(Object entity) throws DataAccessException;

	void saveEntityList(Collection entity) throws DataAccessException;
	
	Collection getDataList(String dataTable) throws DataAccessException;
	
	Object getEntityById(String table, String keyCol, String id) throws DataAccessException;
	
	void saveEntityEx(Object entity) throws DataAccessException;
}
