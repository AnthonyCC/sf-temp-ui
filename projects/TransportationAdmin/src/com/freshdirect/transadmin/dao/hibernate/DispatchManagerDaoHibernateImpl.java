package com.freshdirect.transadmin.dao.hibernate;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.freshdirect.transadmin.dao.DispatchManagerDaoI;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ScribLabel;
import com.freshdirect.transadmin.model.WaveInstance;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DispatchManagerDaoHibernateImpl extends
		BaseManagerDaoHibernateImpl implements DispatchManagerDaoI {

	public Collection getPlan() throws DataAccessException {

		return getDataList("TrnDispatchPlan Order By  PLAN_DATE");
	}

	public Collection getPlan(String planDate, String zoneLst)
			throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("Plan p ");
		boolean hasDate = false;
		if (planDate != null) {
			strBuf.append(" where p.planDate ").append(planDate);
			hasDate = true;
		}

		if (zoneLst != null) {
			if (!hasDate) {
				strBuf.append("where ");
			} else {
				strBuf.append(" and ");
			}
			strBuf.append("p.zone.zoneCode ").append(zoneLst);
		}

		strBuf
				.append(" order by p.planDate, p.firstDeliveryTime ,p.region, p.sequence");
		return getDataList(strBuf.toString());
	}

	public Collection getPlan(String day, String zone, String date)
			throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Plan p");
		strBuf.append(" where p.planDate='").append(date).append("'");

		if (!TransStringUtil.isEmpty(zone) && !zone.equals("0")
				&& !zone.equals("null")) {
			strBuf.append(" and p.zone.zoneCode='").append(zone).append("'");
		}

		strBuf.append(" and p.planId").append(" not in(").append(
				"select id.planId from Dispatch where id.dispatchDate='")
				.append(date).append("')");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getPlan(Date planDate, Date startTime, boolean isBullPen)
			throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Plan p where p.planDate = ? and p.startTime = ? ");
		if (isBullPen) {
			strBuf.append(" and p.isBullpen = 'Y'");
		}
		strBuf.append(" order by p.planDate, p.zone.zoneCode, p.startTime, p.sequence");
		
		return (Collection) getHibernateTemplate().find(strBuf.toString(),	new Object[] { planDate, startTime });		
	}

	public Collection<Plan> getPlanList(String date) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Plan p");
		strBuf
				.append(" where p.planDate='")
				.append(date)
				.append(
						"' order by p.planDate, p.zone.zoneCode, p.startTime, p.sequence");

		return (Collection<Plan>) getHibernateTemplate().find(strBuf.toString());
	}

	public Collection getPlanList(String date, String region) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Plan p");
		strBuf
				.append(" where p.planDate='")
				.append(date);
			if(region!=null)	
				strBuf.append("' and p.region.code='").append(region);
			
				strBuf.append("' order by p.planDate, p.zone.zoneCode, p.startTime, p.sequence");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getPlanForResource(String date, String resourceId)
			throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Plan p");
		strBuf
				.append(" where p.planDate='")
				.append(date)
				.append("'")
				.append(" and '")
				.append(resourceId)
				.append("' in (select r.id.resourceId from p.planResources r)")
				.append(
						" order by p.planDate, p.zone.zoneCode, p.startTime, p.sequence");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getDispatchForResource(String date, String resourceId)
			throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Dispatch p");
		strBuf
				.append(" where p.dispatchDate='")
				.append(date)
				.append("'")
				.append(" and '")
				.append(resourceId)
				.append("' in (select r.id.resourceId from p.dispatchResources r)")
				.append(
						" order by p.dispatchDate, p.zone.zoneCode, p.startTime");

		return (Collection) getHibernateTemplate().find(strBuf.toString());		
	}

	public Dispatch getDispatch(String dispatchId) throws DataAccessException {

		return (Dispatch) getEntityById("Dispatch", "dispatchId", dispatchId);

	}
	
	public Collection getDispatchForGPS(Date dispatchDate, String assetId)
		throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Dispatch p where p.dispatchDate = ? and p.gpsNumber = ? ");
		
		strBuf.append(" order by p.dispatchDate, p.zone.zoneCode, p.startTime");
		
		return (Collection) getHibernateTemplate().find(strBuf.toString(),
				new Object[] { dispatchDate, assetId });
	}
	
	public Collection getDispatchForEZPass(Date dispatchDate, String assetId)
	throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Dispatch p where p.dispatchDate = ? and p.ezpassNumber = ? ");

		strBuf.append(" order by p.dispatchDate, p.zone.zoneCode, p.startTime");

		return (Collection) getHibernateTemplate().find(strBuf.toString(),
				new Object[] { dispatchDate, assetId });
	}

	public Collection getDispatchForMotKit(Date dispatchDate, String assetId)
	throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Dispatch p where p.dispatchDate = ? and p.motKitNumber = ? ");

		strBuf.append(" order by p.dispatchDate, p.zone.zoneCode, p.startTime");

		return (Collection) getHibernateTemplate().find(strBuf.toString(),
				new Object[] { dispatchDate, assetId });
	}
	
	
	
	public Collection getDispatch(Date dispatchDate, Date startTime, boolean isBullPen)
			throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Dispatch p where p.dispatchDate = ? and p.startTime = ? ");
		if (isBullPen) {
			strBuf.append(" and p.bullPen = 'Y'");
		}
		strBuf.append(" order by p.dispatchDate, p.zone.zoneCode, p.startTime");
		
		return (Collection) getHibernateTemplate().find(strBuf.toString(),
				new Object[] { dispatchDate, startTime });
	}

	public void evictDispatch(Dispatch d) throws DataAccessException {
		getSession().evict(d);
	}
	
	public Plan getPlan(String id) throws DataAccessException {
		return (Plan) getEntityById("Plan", "id.planId", id);
	}

	public Collection getDispatchList(String date, String zone, String region)
			throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Dispatch dp");
		strBuf.append(" where dp.dispatchDate='").append(date).append("'");

		if (zone != null && zone.length() > 0 && !zone.equals("0")
				&& !zone.equals("null")) {
			strBuf.append(" and dp.zone.zoneCode='").append(zone).append("'");
		}
		if (region != null && region.length() > 0 && !region.equals("0")
				&& !region.equals("null")) {
			strBuf.append(" and dp.region='").append(region).append("'");
		}
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public void saveDispatch(Dispatch dispatch) throws DataAccessException {

		if (TransStringUtil.isEmpty(dispatch.getDispatchId())) {
			Set resources = dispatch.getDispatchResources();
			dispatch.setDispatchResources(null);
			getHibernateTemplate().saveOrUpdate(dispatch);
			if (resources != null && resources.size() > 0) {
				Iterator it = resources.iterator();
				while (it.hasNext()) {
					DispatchResource dr = (DispatchResource) it.next();
					dr.getId().setContextId(dispatch.getDispatchId());
				}
			}
			dispatch.setDispatchResources(resources);
			saveEntityList(dispatch.getDispatchResources());

		} else {
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

	public Collection getAssignedRoutes(String date) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("Select dp.route from Dispatch dp");
		strBuf.append(" where dp.dispatchDate='").append(date).append("'");
		strBuf.append(" and dp.route is not null");
		return (Collection) getHibernateTemplate().find(strBuf.toString());

	}

	public void savePlan(Plan plan) throws DataAccessException {

		if (plan.getPlanId() == null || "".equals(plan.getPlanId())) {
			Set resources = plan.getPlanResources();
			plan.setPlanResources(null);
			getHibernateTemplate().save(plan);
			if (resources != null && resources.size() > 0) {
				Iterator it = resources.iterator();
				while (it.hasNext()) {
					PlanResource dr = (PlanResource) it.next();
					dr.getId().setContextId(plan.getPlanId());
				}
			}
			plan.setPlanResources(resources);
			saveEntityList(plan.getPlanResources());

		} else {
			saveEntity(plan);
		}
	}

	public Collection getScribList(String date) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Scrib s");
		strBuf
				.append(" where s.scribDate='")
				.append(date)
				.append(
						"' order by s.scribDate,s.region,s.firstDeliveryTime,s.zone,s.startTime");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getScribList(String date, String region) {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Scrib s");
		strBuf
				.append(" where s.scribDate='")
				.append(date)
				.append("' ");
			if(region!=null)	
				strBuf.append("and s.region.code='").append(region+"' ");
			
				strBuf.append("order by s.scribDate,s.region,s.firstDeliveryTime,s.zone,s.startTime");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	
	public Scrib getScrib(String id) {
		return (Scrib) getEntityById("Scrib", "scribId", id);
	}

	public Collection getUserPref(String userId) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from UserPref s");
		strBuf.append(" where s.userId='").append(userId).append("'");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public Collection getDispatchReasons(boolean active) {
		if (active)
			return getDataList("DispatchReason ds where ds.active is null or ds.active='Y' order by ds.code");
		else
			return getDataList("DispatchReason ds where ds.active='N' order by ds.code");
	}
	
	public int addScenarioDayMapping(DlvScenarioDay scenarioDay) throws DataIntegrityViolationException{
		if (scenarioDay!=null){
			try{
				saveEntity(scenarioDay);
			}catch(DataIntegrityViolationException ex){
				return 2;
			}
			return 1;
		}
		else
			return 0;
	}
	
	public void deleteDefaultScenarioDay(String sDate, String sDay){
		
		this.getHibernateTemplate().execute(new HibernateCallback() {
			 public Object doInHibernate(Session session) throws HibernateException, SQLException {
			 		 
				 Query query = session.createQuery("delete from DlvScenarioDay where dayOfWeek is null and normalDate is null");
				 query.executeUpdate();
			     return null;
			 }
		});
	}
	
	public ScribLabel getScribLabelByDate(String date) throws DataAccessException {
		return (ScribLabel) getEntityById("ScribLabel", "date", date);
	}
	
	public Collection getDatesByScribLabel(String slabel) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from ScribLabel sl");
		strBuf
				.append(" where sl.scribLabel='")
				.append(slabel)
				.append("'");
			
		strBuf.append("order by sl.date");

		return (Collection) getHibernateTemplate().find(strBuf.toString());		
	}
	
	public Collection getPlan(Date planDate, String zone) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Plan p where p.planDate = ? ");
		if (zone != null) {
			strBuf.append(" and p.zone.zoneCode = '").append(zone).append("'");
		}
		strBuf.append(" order by p.planDate, p.zone.zoneCode, p.startTime, p.firstDeliveryTime, p.cutOffTime, p.sequence");

		return (Collection) getHibernateTemplate().find(strBuf.toString(),	new Object[] { planDate});		
	}
	
	public Collection getScrib(Date scribDate, String zone) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from Scrib p where p.scribDate = ? ");
		if (zone != null) {
			strBuf.append(" and p.zone.zoneCode = '").append(zone).append("'");
		}
		strBuf.append(" order by p.scribDate, p.zone.zoneCode, p.startTime, p.firstDeliveryTime, p.cutOffTime");

		return (Collection) getHibernateTemplate().find(strBuf.toString(),	new Object[] { scribDate});		
	}
	
	public Collection getWaveInstancePublish(Date deliveryDate) throws DataAccessException {
		return (Collection) getHibernateTemplate().find("from WaveInstancePublish p where p.deliveryDate = ? "
															, new Object[] { deliveryDate});	
	}
	
	public Collection getWaveInstance(Date deliveryDate, String area) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from WaveInstance p where p.deliveryDate = ? ");
		if (area != null) {
			strBuf.append(" and p.area = '").append(area).append("'");
		}
		strBuf.append(" order by p.deliveryDate, p.area, p.dispatchTime, p.firstDeliveryTime, p.cutOffTime");	
		return (Collection) getHibernateTemplate().find(strBuf.toString(),	new Object[] { deliveryDate });		
	}
	
	public Collection getWaveInstance(Date deliveryDate, Date cutOff) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from WaveInstance p where p.deliveryDate = ? ");
		strBuf.append(" and p.cutOffTime = ?");
		strBuf.append(" order by p.deliveryDate, p.area, p.dispatchTime, p.firstDeliveryTime, p.cutOffTime");	
		return (Collection) getHibernateTemplate().find(strBuf.toString(),	new Object[] { deliveryDate,  cutOff});		
	}
	
	public Collection getWaveInstance(Date cutOff) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from WaveInstance p where p.deliveryDate > sysdate ");
		if(cutOff != null) {
			strBuf.append(" and p.cutOffTime = ?");
		}
		strBuf.append(" order by p.deliveryDate, p.area, p.dispatchTime, p.firstDeliveryTime, p.cutOffTime");
		if(cutOff != null) {
			return (Collection) getHibernateTemplate().find(strBuf.toString(),	new Object[] { cutOff});
		} else {
			return (Collection) getHibernateTemplate().find(strBuf.toString(),	new Object[] { });
		}
	}
	
	public WaveInstance getWaveInstance(String waveInstanceId) throws DataAccessException {

		return (WaveInstance) getEntityById("WaveInstance", "waveInstanceId", waveInstanceId);

	}
	
	public void deleteWaveInstance(final Date deliveryDate) throws DataAccessException {
		
		this.getHibernateTemplate().execute(new HibernateCallback() {
			 public Object doInHibernate(Session session) throws HibernateException, SQLException {
				 
				 Query query = session.createQuery("delete from WaveInstance where DELIVERY_DATE = :rdate");
				 query.setDate("rdate", deliveryDate);
				 				 
				 int rowCount = query.executeUpdate();
			     
				 return null;
			 }
		});
	}
}
