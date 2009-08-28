package com.freshdirect.transadmin.dao;

import java.util.Collection;
import java.util.Date;

public interface LogManagerDaoI extends BaseManagerDaoI {
	
	void log(String userId,int type,Object obj);
	
	Collection getLogs(Date fromDate,Date toDate);
	
	Collection getTimeSlotLogs(Date fromDate, Date toDate);
	
}
