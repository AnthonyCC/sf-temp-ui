package com.freshdirect.transadmin.dao.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.dao.DispatchManagerDaoI;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.ZonetypeResource;
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
	
	public Collection getPlan(String planDate, String zoneLst) throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("Plan p ");
		boolean hasDate = false;
		if(planDate != null) {
			strBuf.append(" where p.planDate ").append(planDate);
			hasDate = true;
		}
		
		if(zoneLst != null) {
			if(!hasDate) {
				strBuf.append("where ");
			} else {
				strBuf.append(" and ");
			}
			strBuf.append("p.zone.zoneCode ").append(zoneLst);
		}
		
		strBuf.append(" ORDER BY  PLAN_DATE");
		return getDataList(strBuf.toString());
	}

	
	/*public Collection getPlan(String day, String zone, String date) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnDispatchPlan tp");
		strBuf.append(" where tp.planDate='").append(date).append("'");

		if (!TransStringUtil.isEmpty(zone) && !zone.equals("0") && !zone.equals("null")) {
			strBuf.append(" and tp.trnZone.zoneId='").append(zone).append("'");
		}

		strBuf.append(" and tp.planId").append(" not in(")
			.append("select id.planId from TrnDispatch where id.dispatchDate='").append(date).append("')");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}*/
	
	public Collection getPlan(String day, String zone, String date) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Plan p");
		strBuf.append(" where p.planDate='").append(date).append("'");

		if (!TransStringUtil.isEmpty(zone) && !zone.equals("0") && !zone.equals("null")) {
			strBuf.append(" and p.zone.zoneCode='").append(zone).append("'");
		}

		strBuf.append(" and p.planId").append(" not in(")
			.append("select id.planId from Dispatch where id.dispatchDate='").append(date).append("')");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getPlanList(String date) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Plan p");
		strBuf.append(" where p.planDate='").append(date).append("'");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public Dispatch getDispatch(String dispatchId) throws DataAccessException {
		return (Dispatch)getEntityById("Dispatch","dispatchId",dispatchId);
	
	}

	public Plan getPlan(String id) throws DataAccessException  {
		return (Plan)getEntityById("Plan","id.planId",id);
	}

	
	public Collection getDispatchList(String date, String zone, String region) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Dispatch dp");
		strBuf.append(" where dp.dispatchDate='").append(date).append("'");

		if (zone != null && zone.length() > 0 && !zone.equals("0") && !zone.equals("null")) {
			strBuf.append(" and dp.zone.zoneCode='").append(zone).append("'");
		}
		if (region != null && region.length() > 0 && !region.equals("0") && !region.equals("null")) {
			strBuf.append(" and dp.zone.region.code='").append(region).append("'");
		}
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public void saveDispatch(Dispatch dispatch) throws DataAccessException {
		
		if(dispatch.getDispatchId()==null ||"".equals(dispatch.getDispatchId())) {
			Set resources=dispatch.getDispatchResources();
			dispatch.setDispatchResources(null);
			getHibernateTemplate().saveOrUpdate(dispatch);
			if(resources!=null && resources.size()>0) {
				Iterator it=resources.iterator();
				while(it.hasNext()) {
					DispatchResource dr=(DispatchResource)it.next();
					dr.getId().setContextId(dispatch.getDispatchId());
				}
			}
			dispatch.setDispatchResources(resources);
			saveEntityList(dispatch.getDispatchResources());
			
		}
		else {
			saveEntity(dispatch);
		}
	}
	public Collection getAssignedTrucks(String date) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("Select dp.truck from Dispatch dp");
		strBuf.append(" where dp.dispatchDate='").append(date).append("'");
		strBuf.append(" and dp.truck is not null");
		return (Collection) getHibernateTemplate().find(strBuf.toString());

	}

	public void savePlan(Plan plan) throws DataAccessException {
		
		if(plan.getPlanId()==null ||"".equals(plan.getPlanId())) {
			Set resources=plan.getPlanResources();
			System.out.println("Resources to save :"+resources.size());
			plan.setPlanResources(null);
			getHibernateTemplate().save(plan);
			if(resources!=null && resources.size()>0) {
				Iterator it=resources.iterator();
				while(it.hasNext()) {
					PlanResource dr=(PlanResource)it.next();
					System.out.println("Resource ID:"+dr.getId().getResourceId());
					dr.getId().setContextId(plan.getPlanId());
					System.out.println("Plan ID:"+dr.getId().getContextId());
				}
			}
			plan.setPlanResources(resources);
			saveEntityList(plan.getPlanResources());
			
		}
		else {
			saveEntity(plan);
		}

		
	}
}
