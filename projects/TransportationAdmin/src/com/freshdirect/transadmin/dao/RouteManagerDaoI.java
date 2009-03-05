package com.freshdirect.transadmin.dao;

import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface RouteManagerDaoI {
	
	Map getRouteNumberGroup(String date, String cutOff, String groupCode) throws DataAccessException;
}
