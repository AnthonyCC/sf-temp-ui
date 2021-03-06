package com.freshdirect.transadmin.service.impl;

import java.util.Collection;
import java.util.Date;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.LogManagerDaoI;
import com.freshdirect.transadmin.service.LogManagerI;

public class LogManagerImpl extends BaseManagerImpl implements LogManagerI {
	private LogManagerDaoI logManagerDao;
	
	
	public void setLogManagerDao(LogManagerDaoI logManagerDao) {
		this.logManagerDao = logManagerDao;
	}


	public void log(String userId, int type,Object obj) {
		logManagerDao.log(userId, type,obj);
		
	}


	protected BaseManagerDaoI getBaseManageDao() 	{
		return logManagerDao;
	}


	public Collection getLogs(Date fromDate, Date toDate, String view) 	{
		return logManagerDao.getLogs(fromDate, toDate, view);
	}
	
	public Collection getTimeSlotLogs(Date fromDate, Date toDate) 	{
		return logManagerDao.getTimeSlotLogs(fromDate, toDate);
	}

}
