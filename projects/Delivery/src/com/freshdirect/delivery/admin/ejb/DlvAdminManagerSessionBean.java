package com.freshdirect.delivery.admin.ejb;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.EnumDayCode;
import com.freshdirect.delivery.EnumReservationStatus;
import com.freshdirect.delivery.EnumTruckType;
import com.freshdirect.delivery.HibernateUtil;
import com.freshdirect.delivery.admin.EarlyWarningDataI;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.planning.DlvPlanModel;
import com.freshdirect.delivery.planning.DlvResourceModel;
import com.freshdirect.delivery.planning.DlvShiftModel;
import com.freshdirect.delivery.planning.DlvShiftTimeslotModel;
import com.freshdirect.delivery.planning.DlvTruckAllocation;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.hibernate.BooleanType;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DlvAdminManagerSessionBean extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(DlvAdminManagerSessionBean.class);

	private SessionFactory sf = HibernateUtil.getSessionFactory();

	protected String getResourceCacheKey() {
		return "com.freshdirect.delivery.admin.ejb.DlvAdminManagerHome";
	}

	public void addPlan(DlvPlanModel planModel) throws DlvResourceException {
		Session session = sf.getCurrentSession();
		try{			
			session.save(planModel);
			session.flush();
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "can't save plan");
		}
		finally{
			session.close();
		}
		
	}
	public String addPlan(DlvPlanModel planModel, boolean returnId) throws DlvResourceException {
		Session session = sf.getCurrentSession();
		try{			
			String generatedId = (String)session.save(planModel);
			session.flush();
			return generatedId;
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "can't save plan");
		}
		finally{
			session.close();
		}
		
	}

	public void deletePlan(String planId) throws DlvResourceException {
		Session session = sf.getCurrentSession();
		try{			
			DlvPlanModel plan = (DlvPlanModel)session.load(DlvPlanModel.class, planId);
			session.delete(plan);
			session.flush();
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot delete plan for id: " + planId);
		}
		finally{
			session.close();
		}
	}

	public List getPlansForRegion(String regionId) throws DlvResourceException {
/*
 		//HQL, load objects fully initialized
		Session session = sf.getCurrentSession();
		try{			
			return session.createQuery("from DlvPlanModel where regionId = '" + regionId+"'").list();
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot find plans for region id: " + regionId);
		}
		finally{
			session.close();
		}
*/
		
		// SQL, load light weight objects
		Session session = sf.getCurrentSession();
		try{	
			return session.getNamedQuery("getPlanForRegion").setString("regionId", regionId).list();
			
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot find plans for region id: " + regionId);
		}
		finally{
			session.close();
		}
				
	}

	public DlvPlanModel getPlan(String planId) throws DlvResourceException {
		Session session = sf.getCurrentSession();
		try{			
			return (DlvPlanModel)session.load(DlvPlanModel.class, planId);
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot find plan for id: " + planId);
		}
		finally{
			session.close();
		}
	}

	public void updatePlan(DlvPlanModel planModel) throws DlvResourceException {
		Session session = sf.getCurrentSession();
		try{			
			session.saveOrUpdate(planModel);
			session.flush();
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot update plan for id: " + planModel.getPK().getId());
		}
		finally{
			session.close();
		}

	}

	public List getResourcesForDateRange(String regionName, String zoneCode, Date[] days) throws DlvResourceException {
		List resources = new ArrayList();
		for (int i = 0; i < days.length; i++) {
			resources.add(this.getResourcesForDate(regionName, zoneCode, days[i]));
		}
		return resources;
	}

	public Map getResourcesForRegionAndDateRange(String regionId, Date[] days) throws DlvResourceException {

			Map resourceMap = new HashMap();
			Session session = sf.getCurrentSession();
			try{
			for (int i = 0; i < days.length; i++) {
				Date day = days[i];
				Query q = session.getNamedQuery("findByRegionAndDate");
				q.setString("region_id", regionId);
				q.setDate("start_date", day);
				q.setDate("day", day);
				Collection resources = q.list();
				
				for (Iterator itr = resources.iterator(); itr.hasNext();) {
					DlvResourceModel resource = (DlvResourceModel)session.load(DlvResourceModel.class,(String)itr.next());

					Map dayHashMap = (Map) resourceMap.get(resource.getZoneCode());
					if (dayHashMap == null) {
						dayHashMap = new HashMap();
						resourceMap.put(resource.getZoneCode(), dayHashMap);
					}
					List resourceList = (List) dayHashMap.get(day);
					if (resourceList == null) {
						resourceList = new ArrayList();
						resourceList.add(resource);
						dayHashMap.put(day, resourceList);
					} else {
						resourceList.add(resource);
					}
				}
			}
			}catch(RuntimeException e){
				throw new DlvResourceException(e, "Cannot find resource for region id: " + regionId);
			}
			finally{
				session.close();
			}
			return resourceMap;
	}

	public List getResourcesForDate(String regionName, String zoneCode, Date day) throws DlvResourceException {

		Session session = sf.getCurrentSession();
		try {

			Query q = session.getNamedQuery("findResourcesByZoneCodeAndDay");
			q.setString("zoneCode", zoneCode);
			q.setDate("day", day);
			List dlvResources = q.list();
			
			if (!dlvResources.isEmpty()) {
				return dlvResources;
			}

			// fallback - no resources found, generate them now
			
			String planId = "";
			List l= new ArrayList();
			q = session.getNamedQuery("zoneIdQuery");
			q.setString("region_name", regionName);
			q.setDate("start_date", day);
			q.setString("region_name", regionName);
			q.setString("zone_code", zoneCode);
			l = q.list();
			if (l.isEmpty()) {
				// no query - can't make resources
				return dlvResources;
			}
			
			Object[] result = (Object[])l.get(0);
			
			planId = (String)result[1];
			if (planId == null) {
				// no plan - can't make resources
				return dlvResources;
			}
			
			// construct resources based on plan
			DlvPlanModel planModel = (DlvPlanModel)session.load(DlvPlanModel.class, planId);
			Calendar dayCal = Calendar.getInstance();
			dayCal.setTime(day);
			for (Iterator i = planModel.getShiftsForDay(EnumDayCode.getEnum(dayCal.get(Calendar.DAY_OF_WEEK))).iterator();
				i.hasNext();
				) {
				DlvShiftModel shift = (DlvShiftModel) i.next();
				List dlvTimeslots = new ArrayList();
				DlvResourceModel resource = new DlvResourceModel();
				resource.setName(shift.getName());
				resource.setZoneCode(zoneCode);
				resource.setStart(shift.getStart());
				resource.setEnd(shift.getEnd());
				resource.setDeliveryRate(0);
				resource.setDay(day);
				resource.setPeople(0);

				DlvTruckAllocation truck = new DlvTruckAllocation(EnumTruckType.TYPE_A);
				truck.setCapacity(0);
				truck.setCount(0);

				resource.setTruckAllocation(truck);

				for (Iterator j = shift.getTimeslots().iterator(); j.hasNext();) {
					DlvShiftTimeslotModel shiftTimeslot = (DlvShiftTimeslotModel) j.next();
					DlvTimeslotModel dlvTimeslot = new DlvTimeslotModel(shiftTimeslot);
					dlvTimeslot.setBaseDate(day);
					dlvTimeslot.setTrafficFactor(100);

					dlvTimeslots.add(dlvTimeslot);
				}
				resource.setTimeslots(dlvTimeslots);
				resource.calculateCapacities();
				dlvResources.add(resource);
			}

			return dlvResources;
		} catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot find resource for region: " + regionName);
		}
		finally{
			session.close();
		}

	}

	public DlvResourceModel getResourceById(String resourceId) throws DlvResourceException {
		Session session = sf.getCurrentSession();
		try{			
			return (DlvResourceModel)session.load(DlvResourceModel.class, resourceId);
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot find resource for id: " + resourceId);
		}
		finally{
			session.close();
		}
	}

	public PrimaryKey updatePlanningResource(String regionName, String zoneCode, DlvResourceModel resourceModel)
		throws DlvResourceException {

		Session session = sf.getCurrentSession();
		try {
			if (resourceModel.isAnonymous()) {
				// resolve timeslot zone IDs before saving
				String zoneId = "";
				List l= new ArrayList();
				Query q = session.getNamedQuery("zoneIdQuery");
				q.setString("region_name", regionName);
				q.setDate("start_date", resourceModel.getDay());
				q.setString("region_name", regionName);
				q.setString("zone_code", zoneCode);
				l = q.list();
				Object[] result = (Object[])l.get(0);
				zoneId = (String)result[0];
				for (Iterator i = resourceModel.getTimeslots().iterator(); i.hasNext();) {
					DlvTimeslotModel timeslot = (DlvTimeslotModel) i.next();
					timeslot.setZoneId(zoneId);
				}
			}
			session.saveOrUpdate(resourceModel);
			session.flush();
			return resourceModel.getPK();
		} catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot update resource for region: " + regionName);
		}
		finally{
			session.close();
		}
	}

	public void updateZones(String regionName, List zones, Date startDate) throws DlvResourceException {
		Session session = sf.getCurrentSession();
		try{
			for(Iterator i= zones.iterator();i.hasNext();){
				DlvZoneModel zone = (DlvZoneModel)i.next();
				session.saveOrUpdate(zone);
			}
			session.flush();
			
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot update region: " + regionName);
		}
		finally{
			session.close();
		}

	}
	
	public void updateZone(DlvZoneModel zone) throws DlvResourceException {
		Session session = sf.getCurrentSession();
		try{
			session.saveOrUpdate(zone);
			session.flush();
			
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot update zone: " + zone.getName());
		}
		finally{
			session.close();
		}

	}

	public void updateRegionData(String regionDataId, Date startDate, double dlvCharge) throws DlvResourceException {
		Connection conn = null;
		CallableStatement cs = null;
		try {
			conn = this.getConnection();
			cs = conn.prepareCall("{ ? = call dlv.template_manager_sb.update_version(?, ?, ?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, regionDataId);
			cs.setDate(3, new java.sql.Date(startDate.getTime()));
			cs.setDouble(4, dlvCharge);

			cs.execute();
			int ret = cs.getInt(1);
			if (ret != 1) {
				throw new DlvResourceException("A problem occurred while updating the version");
			}
		} catch (SQLException se) {
			throw new DlvResourceException(se);
		} finally {
			try {
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException sx) {
				LOGGER.debug("There was a problem while trying to cleanup", sx);
			}
		}
	}
	public void updateChefsTableZone(String zoneCode, boolean ctActive, int ctReleaseTime) throws DlvResourceException {
		Session session = sf.getCurrentSession();
		try {
			if (!ctActive) {
				ctReleaseTime = 0;
			}
			Query q = session.getNamedQuery("updateChefsTableZone");
			q.setString("zoneCode", zoneCode);
			q.setParameter("ctActive", new Boolean(ctActive), Hibernate.custom(BooleanType.class));
			q.setInteger("ctReleaseTime", ctReleaseTime);
			q.executeUpdate();
			
			if (!ctActive) {
				q = session.getNamedQuery("deactivateCtTimeslots");
				q.setString("zoneCode", zoneCode);
				q.executeUpdate();
			}
			
			session.flush();
			
		} catch (RuntimeException e) {
			throw new DlvResourceException(e, "Cannot deactivate zone: " + zoneCode);
		} finally {
			session.close();
		}
	}

	public void updateZoneUnattendedDeliveryStatus(String zoneCode, boolean unattended) throws DlvResourceException {
		Session session = sf.getCurrentSession();
		try {
			
			Query q = session.getNamedQuery("updateZoneUnattendedDeliveryStatus");
			q.setString("zoneCode", zoneCode);
			q.setParameter("unattended", new Boolean(unattended), Hibernate.custom(BooleanType.class));
			q.executeUpdate();
			
			session.flush();
			
		} catch (RuntimeException e) {
			throw new DlvResourceException(e, "Cannot update unattended delivery status for: " + zoneCode);
		} finally {
			session.close();
		}
	}
	

	private static final String EARLY_WARNING_QUERY =
		"select code, name, sum(orders) as total_order, sum(capacity) as capacity, "
			+ "sum(total_alloc) as total_alloc, "
			+ "sum(base_orders) as base_orders, "
			+ "sum(base_alloc) as base_alloc, "
			+ "sum(ct_capacity) as ct_capacity, "
			+ "sum(ct_alloc) as ct_alloc, "
			+ "sum(ct_orders) as ct_orders, "
			+ "ct_active "
			+ "from "
			+ "( "
			+ "select z.zone_code as code, z.name, ts.capacity, ts.ct_capacity, z.ct_active as ct_active, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = ? ) as orders, "
			+ "decode((sysdate-(TO_DATE(TO_CHAR(ts.base_date - 1, 'YYYY-MM-DD')||' '||to_char(ts.cutoff_time - (z.ct_release_time/60/24), 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS'))- abs(sysdate-(TO_DATE(TO_CHAR(ts.base_date - 1, 'YYYY-MM-DD')||' '||to_char(ts.cutoff_time - (z.ct_release_time/60/24), 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS')))),0,(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? ),(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> ? and status_code <> ? and chefstable = ' ')+ts.ct_capacity) as total_alloc, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = ? and chefstable = ' ') as base_orders, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> ? and status_code <> ? and chefstable = ' ') as base_alloc, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> ? and status_code <> ? and chefstable = 'X') as ct_alloc, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = ? and chefstable = 'X') as ct_orders "
			+ "from dlv.timeslot ts, dlv.zone z "
			+ "where ts.zone_id=z.id and ts.capacity<>0 and ts.base_date = ? "
			+ ") group by code, name, ct_active order by code ";

	public List getEarlyWarningData(Date day) throws DlvResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(EARLY_WARNING_QUERY);
			ps.setInt(1, EnumReservationStatus.COMMITTED.getCode());
			ps.setInt(2, EnumReservationStatus.CANCELED.getCode());
			ps.setInt(3, EnumReservationStatus.EXPIRED.getCode());
			ps.setInt(4, EnumReservationStatus.CANCELED.getCode());
			ps.setInt(5, EnumReservationStatus.EXPIRED.getCode());
			
			ps.setInt(6, EnumReservationStatus.COMMITTED.getCode());
			ps.setInt(7, EnumReservationStatus.CANCELED.getCode());
			ps.setInt(8, EnumReservationStatus.EXPIRED.getCode());
			ps.setInt(9, EnumReservationStatus.CANCELED.getCode());
			ps.setInt(10, EnumReservationStatus.EXPIRED.getCode());
			ps.setInt(11, EnumReservationStatus.COMMITTED.getCode());
			ps.setDate(12, new java.sql.Date(day.getTime()));

			ResultSet rs = ps.executeQuery();
			List data = new ArrayList();
			while (rs.next()) {
				data.add(
					new EarlyWarningData(
						rs.getString("CODE"),
						rs.getString("NAME"),
						
						rs.getInt("TOTAL_ORDER"),
						rs.getInt("CAPACITY"),
						rs.getInt("TOTAL_ALLOC"),
												
						rs.getInt("BASE_ORDERS"),
						rs.getInt("BASE_ALLOC"),
						
						rs.getInt("CT_CAPACITY"),
						rs.getInt("CT_ALLOC"),
						rs.getInt("CT_ORDERS"),
						rs.getString("CT_ACTIVE")
					));
			}

			rs.close();
			ps.close();

			return data;
		} catch (SQLException e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while cleanup", e);
			}
		}
	}

	public static class EarlyWarningData implements EarlyWarningDataI {

		private final String zoneCode;
		private final String zoneName;
		private final int order;
		private final int capacity;
		private final double percentOrders;
		private final int totalAlloc;
		private final double percentAlloc;

		private final int baseOrder;
		private final int baseCapacity;
		private final double percentbaseOrders;
		private final int baseAlloc;
		private final double percentbaseAlloc;

		private final int ctCapacity;
		private final int ctAlloc;
		private final int ctOrder;
		private final double percentCTAlloc;
		private final double percentCTOrders;
		private final boolean ctActive;

		public EarlyWarningData(
			String zoneCode,
			String zoneName,
			int order,
			int capacity,
			int totalAlloc,
			int baseOrder,
			int baseAlloc,
			int ctCapacity,
			int ctAlloc,
			int ctOrder,
			String ctActive
			) {
			this.zoneCode = zoneCode;
			this.zoneName = zoneName;
			this.order = order;
			this.capacity = capacity;
			this.percentOrders = (double) order / (double) capacity;
			
			this.totalAlloc = totalAlloc;
			this.percentAlloc = (double) totalAlloc / (double) capacity;
			this.baseOrder = baseOrder;
			this.baseCapacity = capacity - ctCapacity;
			this.percentbaseOrders = (double) baseOrder / (double) (capacity - ctCapacity);
			this.baseAlloc = baseAlloc;
			this.percentbaseAlloc = (double) baseAlloc / (double) (capacity - ctCapacity);
			this.ctCapacity = ctCapacity;
			this.ctAlloc = ctAlloc;
			this.ctOrder = ctOrder;
			this.percentCTAlloc = ctCapacity == 0 ? 0 : (double) ctAlloc / (double) ctCapacity;
			this.percentCTOrders = ctCapacity == 0 ? 0 : (double) ctOrder / (double) ctCapacity;
			this.ctActive = "X".equals(ctActive) ? true : false ;
		}

		public String getZoneCode() {
			return this.zoneCode;
		}

		public String getZoneName() {
			return this.zoneName;
		}

		public int getCapacity() {
			return this.capacity;
		}

		public int getOrder() {
			return this.order;
		}

		public double getPercentOrders() {
			return this.percentOrders;
		}

		public int getTotalAllocation() {
			return this.totalAlloc;
		}

		public double getPercentAllocation() {
			return this.percentAlloc;
		}
		public int getBaseCapacity(){
			return this.baseCapacity;
		}
		public int getBaseOrder(){
			return this.baseOrder;
		}
		public int getBaseAllocation(){
			return this.baseAlloc;
		}
		public double getPercentBaseOrders(){
			return this.percentbaseOrders;
		}
		public double getPercentBaseAllocation(){
			return this.percentbaseAlloc;
		}

		public int getCTCapacity(){
			return this.ctCapacity;
		}
		public int getCTAllocation(){
			return this.ctAlloc;
		}
		public double getPercentCTAllocation(){
			return this.percentCTAlloc;
		}
		public int getCTOrder(){
			return this.ctOrder;
		}
		public double getPercentCTOrders(){
			return this.percentCTOrders;
		}		
		public boolean getCTActive(){
			return this.ctActive;
		}		
	}

}
