package com.freshdirect.transadmin.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

public interface PunchInfoDaoI {
	Collection getPunchInfo(String date) throws DataAccessException;
	public Collection getPunchInfoPayCode(final String date) throws DataAccessException;
}
