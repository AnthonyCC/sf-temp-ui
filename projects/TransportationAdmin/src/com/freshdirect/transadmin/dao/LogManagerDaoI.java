package com.freshdirect.transadmin.dao;

import java.util.Collection;
import java.util.Date;

public interface LogManagerDaoI extends BaseManagerDaoI
{
	public void log(String userId,int type,Object obj);
	
	public Collection getLogs(Date fromDate,Date toDate);
	
}
