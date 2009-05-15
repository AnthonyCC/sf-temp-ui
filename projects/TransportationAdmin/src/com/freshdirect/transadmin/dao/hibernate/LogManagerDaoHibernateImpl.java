package com.freshdirect.transadmin.dao.hibernate;


import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import com.freshdirect.transadmin.dao.LogManagerDaoI;
import com.freshdirect.transadmin.model.ActivityLog;

public class LogManagerDaoHibernateImpl extends BaseManagerDaoHibernateImpl implements LogManagerDaoI{

	public void log(String userId, int type,Object obj) 
	{
		Object[] array=(Object[])obj;		
		ActivityLog log=new ActivityLog();
		log.setUserId(userId);
		log.setType(type);
		log.setDate(new Timestamp(System.currentTimeMillis()));
		log.setId(array[0].toString());
		log.setFieldName(array[1]==null?"":array[1].toString());
		log.setOldValue(array[2]==null?"":array[2].toString());
		log.setNewValue(array[3]==null?"":array[3].toString());
		getHibernateTemplate().save(log);
	}

	public Collection getLogs(Date fromDate, Date toDate) 
	{		
		String query="from ActivityLog a where a.date between ? and ? order by a.logId";
		
		return (Collection) getHibernateTemplate().find(query,new Object[]{fromDate,toDate});
	}

}
