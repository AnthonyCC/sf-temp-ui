package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;

public interface LogManagerI  extends BaseManagerI {
	
	void log(String userId,int type,Object obj);
	
	Collection getLogs(Date fromDate,Date toDate);
	
	Collection getTimeSlotLogs(Date fromDate, Date toDate);
}
