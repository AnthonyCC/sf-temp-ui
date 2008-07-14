package com.freshdirect.transadmin.dao.hibernate;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.dao.DispatchManagerDaoI;
import com.freshdirect.transadmin.model.TrnDispatch;
import com.freshdirect.transadmin.model.TrnDispatchPlan;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DispatchManagerDaoHibernateImpl extends BaseManagerDaoHibernateImpl implements DispatchManagerDaoI {

	public Collection getDrivers() throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnEmployee te");
		strBuf.append(" WHERE te.obsolete IS NULL AND te.trnEmployeeJobType.jobTypeId in ('1','2','9')  Order By  LAST_NAME");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public Collection getHelpers() throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnEmployee te WHERE te.obsolete IS NULL ");
		strBuf.append("  Order By  LAST_NAME");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}


	public Collection getPlan() throws DataAccessException {

		return getDataList("TrnDispatchPlan Order By  PLAN_DATE");
	}
	
	public Collection getPlan(String dateRange, String zoneLst) throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("TrnDispatchPlan p ");
		boolean hasDate = false;
		if(dateRange != null) {
			strBuf.append("where p.planDate ").append(dateRange);
			hasDate = true;
		}
		
		if(zoneLst != null) {
			if(!hasDate) {
				strBuf.append("where ");
			} else {
				strBuf.append(" and ");
			}
			strBuf.append("p.trnZone.zoneNumber ").append(zoneLst);
		}
		
		strBuf.append(" ORDER BY  PLAN_DATE");
		return getDataList(strBuf.toString());
	}

	public Collection getPlan(String day, String zone, String date) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnDispatchPlan tp");
		strBuf.append(" where tp.planDate='").append(date).append("'");

		if (!TransStringUtil.isEmpty(zone) && !zone.equals("0") && !zone.equals("null")) {
			strBuf.append(" and tp.trnZone.zoneId='").append(zone).append("'");
		}

		strBuf.append(" and tp.planId").append(" not in(")
			.append("select id.planId from TrnDispatch where id.dispatchDate='").append(date).append("')");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getPlanList(String date) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnDispatchPlan tp");
		strBuf.append(" where tp.planDate='").append(date).append("'");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public Collection getDispatchList(String date, String zone) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnDispatch tp");
		strBuf.append(" where tp.id.dispatchDate='").append(date).append("'");

		if (zone != null && !zone.equals("0") && !zone.equals("null")) {
			strBuf.append(" and tp.trnZone.zoneId='").append(zone).append("'");
		}

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public TrnDispatch getDispatch(String planId, String date) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnDispatch tp");
		strBuf.append(" where tp.id.dispatchDate='").append(date).append("'");
		strBuf.append(" and tp.id.planId=").append(planId);

		Collection collection = getHibernateTemplate().find(strBuf.toString());
		if(collection==null || collection.size()==0) return null;
		Iterator iterator = collection.iterator();
		return (TrnDispatch)iterator.next();
	}

	public TrnDispatchPlan getPlan(String id) throws DataAccessException  {
		return (TrnDispatchPlan)getEntityById("TrnDispatchPlan","id.planId",id);
	}
}
