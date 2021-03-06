package com.freshdirect.transadmin.dao.hibernate;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.freshdirect.transadmin.dao.LogManagerDaoI;
import com.freshdirect.transadmin.model.ActivityLog;
import com.freshdirect.transadmin.model.TimeslotLog;

public class LogManagerDaoHibernateImpl extends BaseManagerDaoHibernateImpl
		implements LogManagerDaoI {

	public void log(String userId, int type, Object obj) {
		Object[] array = (Object[]) obj;
		if (array[0] == null)
			return;
		ActivityLog log = new ActivityLog();
		log.setUserId(userId);
		log.setType(type);
		log.setDate(new Timestamp(System.currentTimeMillis()));
		log.setId(array[0].toString());
		log.setFieldName(array[1] == null ? "" : array[1].toString());
		log.setOldValue(array[2] == null ? "" : array[2].toString());
		log.setNewValue(array[3] == null ? "" : array[3].toString());
		getHibernateTemplate().save(log);
	}

	public Collection getLogs(Date fromDate, Date toDate, String view) {
		String query = "";
		if("P".equalsIgnoreCase(view)) {
			query = "from ActivityLog a where a.type in ('1','2') and a.date between ? and ? order by a.date desc";
		} else {
			query = "from ActivityLog a where a.type not in ('1','2') and a.date between ? and ? order by a.date desc";
		}
		return (Collection) getHibernateTemplate().find(query, new Object[] { fromDate, toDate });
	}

	public Collection getTimeSlotLogs(Date fromDate, Date toDate) {
		
		
		String query = "from TimeslotLog a where a.eventDtm between ? and ? order by a.eventDtm desc ";
		
		return (Collection) getHibernateTemplate().find(query,
				new Object[] { fromDate, toDate });
	}
	/*public Collection getTimeSlotLogs(Date fromDate, Date toDate) {
		return (Collection)getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(TimeslotLog.class)
			.add(Expression.between("eventDtm", fromDate, toDate))
			.addOrder( Order.desc("eventDtm") )
		   //.setFetchMode("timeslotLogDtls",FetchMode.JOIN)
		   .uniqueResult();

	}*/

}
