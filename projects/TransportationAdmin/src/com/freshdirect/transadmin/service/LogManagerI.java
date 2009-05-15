package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;

public interface LogManagerI  extends BaseManagerI 
{
	public void log(String userId,int type,Object obj);
	public Collection getLogs(Date fromDate,Date toDate);
}
