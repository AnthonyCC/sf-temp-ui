/*
 * DlvTemplateManager.java
 *
 * Created on November 12, 2001, 2:45 PM
 */

package com.freshdirect.delivery.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.EnumReservationStatus;
import com.freshdirect.delivery.EnumTimeslotStatus;
import com.freshdirect.delivery.HibernateUtil;
import com.freshdirect.delivery.admin.DlvHistoricTimeslotData;
import com.freshdirect.delivery.model.DlvRegion;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DlvTemplateManagerSessionBean extends SessionBeanSupport {
    
    private static Category LOGGER = LoggerFactory.getInstance( DlvTemplateManagerSessionBean.class );
	
	private SessionFactory sf = HibernateUtil.getSessionFactory();
	

    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    protected String getResourceCacheKey() {
        return "com.freshdirect.delivery.ejb.DlvTemplateManagerHome";
    }
		
	public DlvRegionModel getRegion(String name) throws DlvResourceException {
		DlvRegionModel region = null;
		Session session = sf.getCurrentSession();
		try{			
			DlvRegion reg = (DlvRegion)session.createQuery("from DlvRegion where name ='"+name+"'" ).list().get(0);
			region = (DlvRegionModel)reg.getModel();
			return region;
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot find region:"+name);
		}
		finally{
			session.close();
		}
		
	}
	
	public DlvRegionModel getRegion(String name, java.util.Date startDate) throws DlvResourceException {
		DlvRegionModel region = null;
		Session session = sf.getCurrentSession();
		try{			
			DlvRegion reg = (DlvRegion)session.createQuery("from DlvRegion where name ='"+name+"'" ).list().get(0);
			region = (DlvRegionModel)reg.getModel(startDate);
			return region;
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot find region:"+name);
		}
		finally{
			session.close();
		}
	
	}
	
	public Collection getRegions() throws DlvResourceException {
		Session session = sf.getCurrentSession();
		try{			
			//return session.createQuery("from DlvRegion" ).list();
			return session.getNamedQuery("getRegions").list();
		}catch(RuntimeException e){
			throw new DlvResourceException(e, "Cannot find regions");
		}
		finally{
			session.close();
		}
	}
	
    private final static String timeslotForDateRangeAndZoneQuery =
        "select ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.status, ts.zone_id, z.zone_code, ts.capacity, ts.ct_capacity, " +
        "(select count(*) from dlv.reservation where zone_id=z.id and ts.id=timeslot_id and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, " +
        "(select count(*) from dlv.reservation where zone_id=z.id and ts.id=timeslot_id and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, " +
        "z.ct_release_time as ct_release_time, " +
        "z.ct_active as ct_active " + 
        "from dlv.zone z, dlv.timeslot ts " +
        "where z.id=ts.zone_id and z.zone_code=? " +
        "and ts.base_date >= ? and ts.base_date < ? " + 
        "order by ts.start_time";
	
	public ArrayList getTimeslotForDateRangeAndZone(java.util.Date startDate, java.util.Date endDate, java.util.Date curTime, String zoneCode) throws DlvResourceException{
		ArrayList timeslots = new ArrayList();
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(timeslotForDateRangeAndZoneQuery);
			ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
			ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
			ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
			ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());
			ps.setString(5, zoneCode);
			ps.setDate(6, new java.sql.Date(startDate.getTime()));
			ps.setDate(7, new java.sql.Date(endDate.getTime()));
			
			ResultSet rs = ps.executeQuery();
						
			while (rs.next()) {
				PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
				java.util.Date baseDate = rs.getDate("BASE_DATE");
				TimeOfDay startTime = new TimeOfDay(rs.getTime("START_TIME"));
				TimeOfDay endTime = new TimeOfDay(rs.getTime("END_TIME"));
				TimeOfDay cutoffTime = new TimeOfDay(rs.getTime("CUTOFF_TIME"));
				EnumTimeslotStatus status = EnumTimeslotStatus.getEnum(rs.getInt("STATUS"));
				int capacity = rs.getInt("CAPACITY");
				int chefsTableCapacity = rs.getInt("CT_CAPACITY");
				int baseAllocation = rs.getInt("base_allocation");
				int ctAllocation = rs.getInt("ct_allocation");
				int ctReleaseTime = rs.getInt("CT_RELEASE_TIME");
				boolean ctActive = "X".equals(rs.getString("CT_ACTIVE"));
				String zoneId = rs.getString("ZONE_ID");
				
				 
				DlvTimeslotModel model = new DlvTimeslotModel(pk, zoneId, baseDate, startTime, endTime, cutoffTime, status, capacity, chefsTableCapacity, baseAllocation, ctAllocation, ctReleaseTime, ctActive,zoneCode);
				timeslots.add(model);
			}
            rs.close();
            ps.close();
			
		}catch(SQLException se){
			throw new DlvResourceException(se.getMessage());
		}finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException sx) {
				LOGGER.debug("Problem while trying to cleanup", sx);
			}
		}
		
		return timeslots;
		
	}
	private static final String TIMESLOT_REGION_QUERY = "select ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.status, ts.zone_id, z.zone_code, ts.capacity, ts.traffic_factor, ts.ct_capacity, "+
		"(select count(*) from dlv.reservation where zone_id=z.id and ts.id=timeslot_id and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, "+
		"(select count(*) from dlv.reservation where zone_id=z.id and ts.id=timeslot_id and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, "+
		"(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "+
		"(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active "+
		"from dlv.timeslot ts, "+ 
		"( "+
 		"select zone.id, zone.zone_code from dlv.zone, "+
 		"( "+
  		"select id from dlv.region_data where region_id= ? "+
  		"and start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = ? ) "+
  		"and start_date <= ? "+
 		") rd "+
 		"where region_data_id=rd.id "+
		") z "+
		"where z.id=ts.zone_id "+
		"and ts.base_date >= ? and ts.base_date < ? "+
		"order by z.zone_code, ts.base_date ";
	
	public DlvHistoricTimeslotData getTimeslotForDateRangeAndRegion(java.util.Date startDate, java.util.Date endDate, String regionId) throws DlvResourceException {

		Connection con = null;
		try{
			DlvHistoricTimeslotData data = new DlvHistoricTimeslotData();
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(TIMESLOT_REGION_QUERY);
			ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
			ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
			ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
			ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());
			ps.setString(5, regionId);
			ps.setDate(6, new java.sql.Date(startDate.getTime()));
			ps.setString(7, regionId);
			ps.setDate(8, new java.sql.Date(endDate.getTime()));
			ps.setDate(9, new java.sql.Date(startDate.getTime()));
			ps.setDate(10, new java.sql.Date(endDate.getTime()));
			
			ResultSet rs = ps.executeQuery();
				
			while (rs.next()) {
				PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
				java.util.Date baseDate = rs.getDate("BASE_DATE");
				TimeOfDay startTime = new TimeOfDay(rs.getTime("START_TIME"));
				TimeOfDay endTime = new TimeOfDay(rs.getTime("END_TIME"));
				TimeOfDay cutoffTime = new TimeOfDay(rs.getTime("CUTOFF_TIME"));
				EnumTimeslotStatus status = EnumTimeslotStatus.getEnum(rs.getInt("STATUS"));
				int capacity = rs.getInt("CAPACITY");
		 		int chefsTableCapacity = rs.getInt("ct_capacity");
				int baseAllocation = rs.getInt("base_allocation");
				int ctAllocation = rs.getInt("ct_allocation");
				int ctReleaseTime = rs.getInt("CT_RELEASE_TIME");
				boolean ctActive = "X".equals(rs.getString("CT_ACTIVE"));
				double trafficFactor = rs.getDouble("TRAFFIC_FACTOR");
				String zoneId = rs.getString("ZONE_ID");
		 		
		 		String zoneCode = rs.getString("ZONE_CODE");
				DlvTimeslotModel model = new DlvTimeslotModel(pk, zoneId, baseDate, startTime, endTime, cutoffTime, status, capacity, chefsTableCapacity, baseAllocation, ctAllocation, ctReleaseTime, ctActive,zoneCode);
				model.setTrafficFactor(trafficFactor);
				data.addTimeslot(zoneCode, model);
			}
			rs.close();
			ps.close();
			
			return data;
	
		}catch(SQLException se){
			throw new DlvResourceException(se.getMessage());
		}finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException sx) {
				LOGGER.debug("Problem while trying to cleanup", sx);
			}
		}
	}

}
