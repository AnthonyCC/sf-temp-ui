package com.freshdirect.routing.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.routing.dao.IDeliveryDetailsDAO;
import com.freshdirect.routing.model.AreaModel;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.DeliveryWindowMetrics;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IServiceTimeModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.ServiceTimeModel;
import com.freshdirect.routing.model.ZoneModel;

public class DeliveryDetailsDAO extends BaseDAO implements IDeliveryDetailsDAO {
		
	
	private static final String GET_DELIVERYINFO_QRY = "select di.SCRUBBED_ADDRESS SCRUBBED_ADDRESS, di.DELIVERY_TYPE DELIVERY_TYPE, "+
			"di.APARTMENT APARTMENT,di.CITY CITY, di.STATE STATE, di.COUNTRY COUNTRY, " +
			"di.ZIP ZIP, di.ZONE ZONE "+			
			"from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "+
			"where s.id = ? and s.id = sa.sale_id and s.customer_id = sa.customer_id and sa.id = di.salesaction_id "+ 
			"and sa.action_type in ('CRO','MOD') and sa.action_date=s.CROMOD_DATE and s.type='REG'";
	
	private static final String GET_SERVICETIME_QRY = "select st.FIXED_SERVICE_TIME  FIXED_SERVICE_TIME , st.VARIABLE_SERVICE_TIME VARIABLE_SERVICE_TIME "+			
			"from dlv.SERVICETIME st "+
			"where st.SERVICETIME_TYPE = ? and st.ZONE_TYPE = ? ";
	
	private static final String GET_DELIVERYTYPE_QRY = "select r.SERVICE_TYPE SERVICE_TYPE from dlv.region r, dlv.region_data rd, dlv.zone z "+
			"where rd.id = z.region_data_id and rd.region_id = r.id and z.zone_code = ? "  +
			"and rd.start_date = (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id=r.id)";
	
	private static final String GET_DELIVERYZONETYPE_QRY = "select z.ZONE_TYPE SERVICE_TYPE from transp.trn_zone z where z.zone_number = ? ";
	
	private static final String GET_DELIVERYZONEDETAILS_QRY = "select z.ZONE_CODE ZONE_NUMBER, z.ZONE_TYPE ZONE_TYPE, a.CODE AREACODE," +
			"a.BALANCE_BY BALANCE_BY, a.LOADBALANCE_FACTOR LOADBALANCE_FACTOR, a.NEEDS_LOADBALANCE NEEDS_LOADBALANCE,a.IS_DEPOT IS_DEPOT  from transp.zone z, transp.trn_area a  " +
			" where z.area = a.code and (z.OBSOLETE <> 'X' or z.OBSOLETE IS NULL)";
	
	private static final String GET_TIMESLOTSBYDATE_QRY = " select ta.AREA, z.ZONE_CODE, z.NAME, t.START_TIME , t.END_TIME, TO_CHAR(t.CUTOFF_TIME, 'HH_PM') wavecode  from dlv.timeslot t" +
			", dlv.zone z, transp.zone ta where t.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE" +
			" and t.base_date = ?";
	
	private static final String GET_UNASSIGNED_QRY = "select r.ID RID, r.STATUS_CODE STATUS, r.CUSTOMER_ID CID, r.TYPE RTYPE, r.ORDER_ID OID," +
			" r.UNASSIGNED_DATETIME UDATETIME, r.UNASSIGNED_ACTION UACTION, t.BASE_DATE BDATE, t.START_TIME STIME, " +
			"t.END_TIME ETIME, s.CROMOD_DATE SCROMODDATE, z.ZONE_CODE ZCODE   " +
			"from dlv.reservation r, dlv.timeslot t, cust.sale s, dlv.zone z " +
			"where t.BASE_DATE = ? and r.STATUS_CODE in ('10','15') and r.TIMESLOT_ID = t.ID and r.ORDER_ID = s.ID(+) and t.ZONE_ID = z.ID";
	
		
	private static final String EARLY_WARNING_QUERY =
		"select code, name, st, et, sum(orders) as total_order, sum(capacity) as capacity, "
			+ "sum(total_alloc) as total_alloc, "
			+ "sum(base_orders) as base_orders, "
			+ "sum(base_alloc) as base_alloc, "
			+ "sum(ct_capacity) as ct_capacity, "
			+ "sum(ct_alloc) as ct_alloc, "
			+ "sum(ct_orders) as ct_orders "			
			+ "from "
			+ "( "
			+ "select z.zone_code as code, z.name, ts.capacity, ts.ct_capacity, ts.START_TIME st, ts.END_TIME et, z.ct_active as ct_active, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = '10' ) as orders, "
			+ "decode((sysdate-(TO_DATE(TO_CHAR(ts.base_date - 1, 'YYYY-MM-DD')||' '||to_char(ts.cutoff_time - (z.ct_release_time/60/24), 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS'))- abs(sysdate-(TO_DATE(TO_CHAR(ts.base_date - 1, 'YYYY-MM-DD')||' '||to_char(ts.cutoff_time - (z.ct_release_time/60/24), 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS')))),0,(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> '15' and status_code <> '20' ),(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> '15' and status_code <> '20' and chefstable = ' ')+ts.ct_capacity) as total_alloc, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = '10' and chefstable = ' ') as base_orders, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> '15' and status_code <> '20' and chefstable = ' ') as base_alloc, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> '15' and status_code <> '20' and chefstable = 'X') as ct_alloc, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = '10' and chefstable = 'X') as ct_orders "
			+ "from dlv.timeslot ts, dlv.zone z "
			+ "where ts.zone_id=z.id and ts.capacity<>0 and ts.base_date = ? and ts.CUTOFF_TIME =  ? "
			+ ") group by code, name, st, et order by code ";
			
	
	public IDeliveryModel getDeliveryInfo(final String saleId) throws SQLException {
		 final IDeliveryModel model = new DeliveryModel();	         
         PreparedStatementCreator creator=new PreparedStatementCreator() {
             public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                 PreparedStatement ps =
                     connection.prepareStatement(GET_DELIVERYINFO_QRY);
                 ps.setString(1,saleId);
                 return ps;
             }  
         };
         jdbcTemplate.query(creator, 
        		  new RowCallbackHandler() { 
        		      public void processRow(ResultSet rs) throws SQLException {
        		    	
        		    	do { 
        		    		IZoneModel tmpModel = new ZoneModel();
				    		tmpModel.setZoneNumber(rs.getString("ZONE"));
        		    		model.setDeliveryZone(tmpModel);
        		    		//model.setDeliveryType(rs.getString("DELIVERY_TYPE"));
        		    		ILocationModel locModel = new LocationModel();
        		    		locModel.setStreetAddress1(rs.getString("SCRUBBED_ADDRESS"));
        		    		locModel.setApartmentNumber(rs.getString("APARTMENT"));
        		    		locModel.setZipCode(rs.getString("ZIP"));
        		    		locModel.setState(rs.getString("STATE"));
        		    		locModel.setCity(rs.getString("CITY"));
        		    		locModel.setCountry(rs.getString("COUNTRY"));
        		    		
        		    		model.setDeliveryLocation(locModel);        		    		
        		    	   }
        		    	   while(rs.next());		        		    	
        		      }
        		  }
        	); 
         
		return model;
	}
	
	public IServiceTimeModel getServiceTime(final String serviceTimeType, final String zoneType) throws SQLException {
		final IServiceTimeModel model = new ServiceTimeModel();  
		model.setNew(true);
		
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(GET_SERVICETIME_QRY);
                ps.setString(1,serviceTimeType);
                ps.setString(2,zoneType);
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	
       		    	do {        		    		
       		    		model.setFixedServiceTime(rs.getDouble("FIXED_SERVICE_TIME"));
       		    		model.setVariableServiceTime(rs.getDouble("VARIABLE_SERVICE_TIME")); 
       		    		model.setNew(false);
       		    	   }
       		    	   while(rs.next());		        		    	
       		      }
       		  }
       	); 
        
		return model;
	}
	public String getDeliveryType(final String zoneCode) throws SQLException {
		
		return (String)jdbcTemplate.queryForObject(GET_DELIVERYTYPE_QRY, new Object[]{zoneCode}, String.class);	
	}	
	
	public String getDeliveryZoneType(final String zoneCode) throws SQLException {
				
		return (String)jdbcTemplate.queryForObject(GET_DELIVERYZONETYPE_QRY, new Object[]{zoneCode}, String.class);    		
	}	
	
	public Map getDeliveryZoneDetails() throws SQLException {
		final Map zoneDetailsMap = new HashMap();
				
		jdbcTemplate.query(GET_DELIVERYZONEDETAILS_QRY, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		String zoneCode = rs.getString("ZONE_NUMBER");
				    		IZoneModel tmpModel = new ZoneModel();
				    		tmpModel.setZoneNumber(zoneCode);
				    		
				    		
				    		tmpModel.setZoneType(rs.getString("ZONE_TYPE"));
				    		
				    		IAreaModel tmpAreaModel = new AreaModel();
				    		tmpAreaModel.setAreaCode(rs.getString("AREACODE"));
				    		tmpAreaModel.setBalanceBy(rs.getString("BALANCE_BY"));
				    		tmpAreaModel.setLoadBalanceFactor(rs.getDouble("LOADBALANCE_FACTOR"));
				    		tmpAreaModel.setNeedsLoadBalance("X".equalsIgnoreCase(rs.getString("NEEDS_LOADBALANCE")));
				    		
				    		tmpAreaModel.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")));
				    		
				    		tmpModel.setArea(tmpAreaModel);
				    		zoneDetailsMap.put(zoneCode, tmpModel);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return zoneDetailsMap;
	}
	
	public List getLateDeliveryOrders(String query) throws SQLException {
		final List lateDeliveryOrders = new ArrayList();
		
		jdbcTemplate.query(query, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		lateDeliveryOrders.add(rs.getString("ERPID"));
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return lateDeliveryOrders;
	}
	
	public Map<String, List<IDeliverySlot>> getTimeslotsByDate(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws SQLException {
		
		final Map<String, List<IDeliverySlot>> timeslotByArea = new TreeMap<String, List<IDeliverySlot>>();
		
		final StringBuffer query = new StringBuffer();
		query.append(GET_TIMESLOTSBYDATE_QRY);
		
		if(cutOffTime != null) {
			query.append(" and t.cutoff_time = ?");
		}
		query.append(" order by z.ZONE_CODE, t.START_TIME");
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(query.toString());
                ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
                
                if(cutOffTime != null) {
                	ps.setTimestamp(2, new java.sql.Timestamp(cutOffTime.getTime()));
                }
                return ps;
            }  
        };
        
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		String zCode = rs.getString("ZONE_CODE");
				    		IDeliverySlot tmpModel = new DeliverySlot();
				    		tmpModel.setStartTime(rs.getTimestamp("START_TIME"));
				    		tmpModel.setStopTime(rs.getTimestamp("END_TIME"));
				    		tmpModel.setWaveCode(rs.getString("wavecode"));	
				    		
				    		if(!timeslotByArea.containsKey(zCode)) {
				    			timeslotByArea.put(zCode, new ArrayList<IDeliverySlot>());
				    		}
				    		timeslotByArea.get(zCode).add(tmpModel);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return timeslotByArea;
	}
	
	public Map<String, List<IDeliveryWindowMetrics>> getTimeslotsByDateEx(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws SQLException {
		
		final Map<String, List<IDeliveryWindowMetrics>> timeslotByArea = new TreeMap<String, List<IDeliveryWindowMetrics>>();
				
		PreparedStatementCreator creator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(EARLY_WARNING_QUERY);
                ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
                
                ps.setTimestamp(2, new java.sql.Timestamp(cutOffTime.getTime()));
                
                return ps;
            }  
        };
        
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do { 
				    		IDeliveryWindowMetrics metrics = new DeliveryWindowMetrics();				    		
				    		String zCode = rs.getString("code");
				    		
				    		metrics.setDeliveryStartTime(rs.getTimestamp("st"));
				    		metrics.setDeliveryEndTime(rs.getTimestamp("et"));
				    		metrics.setOrderCapacity(rs.getInt("capacity"));
				    		metrics.setTotalConfirmedOrders(rs.getInt("total_order"));
				    		metrics.setTotalAllocatedOrders(rs.getInt("total_alloc"));
				    		
				    		if(!timeslotByArea.containsKey(zCode)) {
				    			timeslotByArea.put(zCode, new ArrayList<IDeliveryWindowMetrics>());
				    		}
				    		timeslotByArea.get(zCode).add(metrics);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return timeslotByArea;
	}
	
	public List<IOrderModel> getUnassigned(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws SQLException {
		
		final List<IOrderModel> orders = new ArrayList<IOrderModel>();
				
		PreparedStatementCreator creator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(GET_UNASSIGNED_QRY);
                ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
                               
                return ps;
            }  
        };
        
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do { 
				    		IOrderModel order = new OrderModel();	
				    		order.setOrderNumber(rs.getString("OID"));
				    		order.setCustomerNumber(rs.getString("CID"));
				    		order.setCreateModifyTime(rs.getTimestamp("SCROMODDATE"));
				    		order.setUnassignedTime(rs.getTimestamp("UDATETIME"));
				    						    		
				    		IDeliveryModel dModel = new DeliveryModel();
				    		dModel.setDeliveryDate(rs.getDate("BDATE"));
				    		dModel.setDeliveryStartTime(rs.getTimestamp("STIME"));
				    		dModel.setDeliveryEndTime(rs.getTimestamp("ETIME"));
				    		
				    		IZoneModel zModel = new ZoneModel();
				    		zModel.setZoneNumber(rs.getString("ZCODE"));
				    		
				    		dModel.setDeliveryZone(zModel);
				    		
				    		order.setDeliveryInfo(dModel);
				    						    		
				    		orders.add(order);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return orders;
	}
}
