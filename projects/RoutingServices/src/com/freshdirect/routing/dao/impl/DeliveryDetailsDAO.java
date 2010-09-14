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

import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;
import com.freshdirect.routing.dao.IDeliveryDetailsDAO;
import com.freshdirect.routing.model.AreaModel;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.DeliveryWindowMetrics;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IUnassignedModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.PackagingModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.model.ServiceTimeTypeModel;
import com.freshdirect.routing.model.UnassignedModel;
import com.freshdirect.routing.model.ZoneModel;
import com.freshdirect.routing.util.RoutingUtil;

public class DeliveryDetailsDAO extends BaseDAO implements IDeliveryDetailsDAO {
		
	
	private static final String GET_DELIVERYINFO_QRY = "select di.SCRUBBED_ADDRESS SCRUBBED_ADDRESS, di.DELIVERY_TYPE DELIVERY_TYPE, "+
			"di.APARTMENT APARTMENT,di.CITY CITY, di.STATE STATE, di.COUNTRY COUNTRY, " +
			"di.ZIP ZIP, di.ZONE ZONE "+			
			"from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "+
			"where s.id = ? and s.id = sa.sale_id and s.customer_id = sa.customer_id and sa.id = di.salesaction_id "+ 
			"and sa.action_type in ('CRO','MOD') and sa.action_date=s.CROMOD_DATE and s.type='REG'";
	
	/*private static final String GET_SERVICETIME_QRY = "select st.FIXED_SERVICE_TIME  FIXED_SERVICE_TIME , st.VARIABLE_SERVICE_TIME VARIABLE_SERVICE_TIME "+			
			"from dlv.SERVICETIME st "+
			"where st.SERVICETIME_TYPE = ? and st.ZONE_TYPE = ? ";*/
	
	private static final String GET_DELIVERYTYPE_QRY = "select r.SERVICE_TYPE SERVICE_TYPE from dlv.region r, dlv.region_data rd, dlv.zone z "+
			"where rd.id = z.region_data_id and rd.region_id = r.id and z.zone_code = ? "  +
			"and rd.start_date = (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id=r.id)";
	
	private static final String GET_DELIVERYZONETYPE_QRY = "select z.ZONE_TYPE SERVICE_TYPE from transp.trn_zone z where z.zone_number = ? ";
	
	private static final String GET_DELIVERYZONEDETAILS_QRY = "select z.SERVICETIME_TYPE SERVICETIME_TYPE, z.ZONE_CODE ZONE_NUMBER, z.ZONE_TYPE ZONE_TYPE, a.CODE AREACODE, a.ACTIVE ACTIVE, " +
			"a.BALANCE_BY BALANCE_BY, a.LOADBALANCE_FACTOR LOADBALANCE_FACTOR, a.NEEDS_LOADBALANCE NEEDS_LOADBALANCE,a.IS_DEPOT IS_DEPOT  from transp.zone z, transp.trn_area a  " +
			" where z.area = a.code and (z.OBSOLETE <> 'X' or z.OBSOLETE IS NULL)";
	
	private static final String GET_TIMESLOTSBYDATE_QRY = "select t.ID REF_ID, ta.AREA, ta.STEM_FROM_TIME  STEM_FROM, ta.STEM_TO_TIME STEM_TO, ta.STEM_MAX_TIME STEM_MAX, z.ZONE_CODE, z.NAME, t.START_TIME , t.END_TIME" +
			", TO_CHAR(t.CUTOFF_TIME, 'HH_MI_PM') wavecode, t.IS_DYNAMIC IS_DYNAMIC, t.IS_CLOSED IS_CLOSED, a.IS_DEPOT IS_DEPOT   " +
			" from dlv.timeslot t , dlv.zone z, transp.zone ta, transp.trn_area a" +
			" where t.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE" +
			" and t.base_date = ?";
	
	private static final String GET_UNASSIGNED_QRY = "select r.ID RID, r.STATUS_CODE STATUS, r.CUSTOMER_ID CID, r.TYPE RTYPE, r.ORDER_ID OID," +
			" r.UNASSIGNED_DATETIME UDATETIME, r.UNASSIGNED_ACTION UACTION, r.IS_FORCED FORCEFLAG, r.CHEFSTABLE CTFLAG, t.BASE_DATE BDATE, t.START_TIME STIME, " +
			"t.END_TIME ETIME, s.CROMOD_DATE SCROMODDATE, z.ZONE_CODE ZCODE," +
			"R.NUM_CARTONS NCARTONS, R.NUM_FREEZERS NFREEZERS, R.NUM_CASES NCASES, " +
			" r.ORDER_SIZE OSIZE, r.SERVICE_TIME SRTIME, R.RESERVED_ORDER_SIZE ROSIZE, R.RESERVED_SERVICE_TIME RSRTIME, r.UPDATE_STATUS UPDSTATUS, t.IS_DYNAMIC IS_DYNAMIC, t.IS_CLOSED IS_CLOSED " +
			"from dlv.reservation r, dlv.timeslot t, cust.sale s, dlv.zone z " +
			"where t.BASE_DATE = ?  AND (unassigned_datetime IS NOT NULL OR (UPDATE_STATUS IS NOT NULL AND UPDATE_STATUS in ('FLD','OVD'))) and r.TIMESLOT_ID = t.ID and r.ORDER_ID = s.ID(+) and t.ZONE_ID = z.ID ORDER BY z.ZONE_CODE, t.START_TIME";
	
	private static final String GET_UNASSIGNEDRESERVATION_QRY = "select r.ID RID, r.STATUS_CODE STATUS, r.CUSTOMER_ID CID, r.TYPE RTYPE, r.ORDER_ID OID," +
			" r.UNASSIGNED_DATETIME UDATETIME, r.UNASSIGNED_ACTION UACTION, r.IS_FORCED FORCEFLAG, r.CHEFSTABLE CTFLAG, t.BASE_DATE BDATE, t.START_TIME STIME, " +
			"t.END_TIME ETIME, s.CROMOD_DATE SCROMODDATE, z.ZONE_CODE ZCODE, " +
			"R.NUM_CARTONS NCARTONS, R.NUM_FREEZERS NFREEZERS, R.NUM_CASES NCASES, " +
			"r.ORDER_SIZE OSIZE,r.SERVICE_TIME SRTIME, R.RESERVED_ORDER_SIZE ROSIZE, R.RESERVED_SERVICE_TIME RSRTIME, r.UPDATE_STATUS UPDSTATUS " +
			"from dlv.reservation r, dlv.timeslot t, cust.sale s, dlv.zone z " +
			"where r.ID = ? and r.TIMESLOT_ID = t.ID and r.ORDER_ID = s.ID(+) and t.ZONE_ID = z.ID";
	
	private static final String GET_RESERVATIONBYDATE_QRY = "select r.ID RID, r.STATUS_CODE STATUS, r.CUSTOMER_ID CID, r.TYPE RTYPE, r.ORDER_ID OID," +
	" r.UNASSIGNED_DATETIME UDATETIME, r.UNASSIGNED_ACTION UACTION, r.IS_FORCED FORCEFLAG, r.CHEFSTABLE CTFLAG, t.BASE_DATE BDATE, t.START_TIME STIME, " +
	"t.END_TIME ETIME, z.ZONE_CODE ZCODE, R.NUM_CARTONS NCARTONS, R.NUM_FREEZERS NFREEZERS, R.NUM_CASES NCASES, " +
	"r.ORDER_SIZE OSIZE, r.SERVICE_TIME SRTIME, R.RESERVED_ORDER_SIZE ROSIZE, R.RESERVED_SERVICE_TIME RSRTIME, r.UPDATE_STATUS UPDSTATUS " +
	"from dlv.reservation r, dlv.timeslot t, dlv.zone z " +
	"where t.BASE_DATE = ? and r.TIMESLOT_ID = t.ID and t.ZONE_ID = z.ID and z.ZONE_CODE = ?";
	
	private static final String UPDATE_UNASSIGNEDRESERVATION_QRY = "update dlv.reservation r " +
											"set R.ORDER_SIZE = ? , R.SERVICE_TIME = ? , r.UPDATE_STATUS = ? where r.ID = ?  ";
	
	private static final String UPDATE_TIMESLOTFORSTATUS_QRY = "update dlv.timeslot t set t.IS_CLOSED = ? where t.ID=? ";
	
	private static final String UPDATE_TIMESLOTFORSTATUSBYZONE_QRY = "update dlv.timeslot tx set tx.IS_CLOSED = ? where tx.ID in (select t.id from dlv.timeslot t, dlv.zone z  where t.base_date=? and z.zone_code = ? and t.zone_id = z.id)";	
	
	private static final String UPDATE_TIMESLOTFORSTATUSBYREGION_QRY = "update dlv.timeslot tx set tx.IS_CLOSED = ? where tx.ID in (select t.id from dlv.timeslot t, dlv.zone z, transp.zone zt where t.base_date=? and zt.region = ? and t.zone_id = z.id and z.zone_code=zt.zone_code)";
	
	private static final String UPDATE_TIMESLOTFORDYNAMICSTATUS_QRY = "update dlv.timeslot t set t.IS_DYNAMIC = ? where t.ID=? ";
	
	private static final String UPDATE_TIMESLOTFORDYNAMICSTATUSBYZONE_QRY = "update dlv.timeslot tx set tx.IS_DYNAMIC = ? where tx.ID in (select t.id from dlv.timeslot t, dlv.zone z  where t.base_date=? and z.zone_code = ? and t.zone_id = z.id)";	
	
	private static final String UPDATE_TIMESLOTFORDYNAMICSTATUSBYREGION_QRY = "update dlv.timeslot tx set tx.IS_DYNAMIC = ? where tx.ID in (select t.id from dlv.timeslot t, dlv.zone z, transp.zone zt where t.base_date=? and zt.region = ? and t.zone_id = z.id and z.zone_code=zt.zone_code)";
	
	private static final String UPDATE_TIMESLOTCUTUFF_QRYAPPEND = " and tx.cutoff_time = (select cutoff_time from transp.trn_cutoff c where c.id = ?) ";
	
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
			+ "where ts.zone_id=z.id and ts.capacity<>0 and ts.base_date = ? "//and ts.CUTOFF_TIME =  ? "
			//+ ") group by code, name, st, et order by code "
			;
			
	private static final String ORDERSIZE_ESTIMATION_QUERY = "select Ceil(Avg(tbl.NUM_REGULAR_CARTONS)) CCOUNT, " +
			"Ceil(Avg(tbl.NUM_FREEZER_CARTONS)) FCOUNT, Ceil(Avg(tbl.NUM_ALCOHOL_CARTONS)) ACOUNT " +
			"from (select NUM_REGULAR_CARTONS, NUM_FREEZER_CARTONS, NUM_ALCOHOL_CARTONS from cust.sale s " +
			"where s.CUSTOMER_ID = ? and s.STATUS = 'STL' and s.TYPE = 'REG' order by s.CROMOD_DATE desc) tbl where rownum <= ?";
	
		
	public IPackagingModel getHistoricOrderSize(final String customerId, final int range) throws SQLException {
		
		final IPackagingModel model = new PackagingModel();
		
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(ORDERSIZE_ESTIMATION_QUERY);
                ps.setString(1, customerId);
                ps.setInt(2, range);
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	
       		    	do {
       		    		model.setNoOfCartons(rs.getInt("CCOUNT")+ rs.getInt("ACOUNT"));
       		    		model.setNoOfFreezers(rs.getInt("FCOUNT"));       		    		
       		    	}  while(rs.next());		        		    	
       		      }
       		  }
       	); 
        
		return model;
	}
	
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
        		    		
        		    		IBuildingModel building = new BuildingModel();
        		    		
        		    		building.setSrubbedStreet(rs.getString("SCRUBBED_ADDRESS"));
        		    		building.setStreetAddress1(building.getSrubbedStreet());        		    		
        		    		building.setZipCode(rs.getString("ZIP"));
        		    		building.setState(rs.getString("STATE"));
        		    		building.setCity(rs.getString("CITY"));
        		    		building.setCountry(rs.getString("COUNTRY"));
        		    		
        		    		ILocationModel locModel = new LocationModel(building);
        		    		locModel.setApartmentNumber(rs.getString("APARTMENT"));
        		    		model.setDeliveryLocation(locModel);
        		    		        		    		
        		    		
        		    	}  while(rs.next());		        		    	
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
				    		tmpAreaModel.setActive("X".equalsIgnoreCase(rs.getString("ACTIVE")));
				    		
				    		IServiceTimeTypeModel serviceTimeType = new ServiceTimeTypeModel();
        		    		serviceTimeType.setCode( rs.getString("SERVICETIME_TYPE"));
        		    		tmpModel.setServiceTimeType(serviceTimeType);
        		    		
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
				    		tmpModel.setDynamicActive("X".equalsIgnoreCase(rs.getString("IS_DYNAMIC")) ? true : false);
				    		tmpModel.setManuallyClosed("X".equalsIgnoreCase(rs.getString("IS_CLOSED")) ? true : false);
				    		tmpModel.setReferenceId(rs.getString("REF_ID"));
				    		
				    		if(!timeslotByArea.containsKey(zCode)) {
				    			timeslotByArea.put(zCode, new ArrayList<IDeliverySlot>());
				    		}
				    		timeslotByArea.get(zCode).add(tmpModel);
				    		
				    		IRoutingSchedulerIdentity _schId = new RoutingSchedulerIdentity();
							_schId.setDeliveryDate(deliveryDate);
							
							IAreaModel _aModel = new AreaModel();
							_aModel.setAreaCode(rs.getString("AREA"));
							_aModel.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")) ? true : false);
							_aModel.setStemFromTime(rs.getInt("STEM_FROM"));
							_aModel.setStemToTime(rs.getInt("STEM_TO"));	
							_aModel.setMaxStemTime(rs.getInt("STEM_MAX"));
							
							_schId.setRegionId(RoutingUtil.getRegion(_aModel));
							_schId.setArea(_aModel);
							
							tmpModel.setSchedulerId(_schId);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return timeslotByArea;
	}
	
	public Map<String, List<IDeliveryWindowMetrics>> getTimeslotsByDateEx(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws SQLException {
		
		final Map<String, List<IDeliveryWindowMetrics>> timeslotByZone = new TreeMap<String, List<IDeliveryWindowMetrics>>();
		
		final StringBuffer query = new StringBuffer();
		query.append(EARLY_WARNING_QUERY);
		
		if(cutOffTime != null) {
			query.append(" and ts.CUTOFF_TIME =  ? ");
		}
		query.append(") group by code, name, st, et order by code");
		
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
				    		IDeliveryWindowMetrics metrics = new DeliveryWindowMetrics();				    		
				    		String zCode = rs.getString("code");
				    		
				    		metrics.setDeliveryStartTime(rs.getTimestamp("st"));
				    		metrics.setDeliveryEndTime(rs.getTimestamp("et"));
				    		metrics.setOrderCapacity(rs.getInt("capacity"));
				    		metrics.setTotalConfirmedOrders(rs.getInt("total_order"));
				    		metrics.setTotalAllocatedOrders(rs.getInt("total_alloc"));
				    		
				    		if(!timeslotByZone.containsKey(zCode)) {
				    			timeslotByZone.put(zCode, new ArrayList<IDeliveryWindowMetrics>());
				    		}
				    		timeslotByZone.get(zCode).add(metrics);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return timeslotByZone;
	}
	
	public List<IUnassignedModel> getUnassigned(final Date deliveryDate, final Date cutOffTime
													, final String zoneCode) throws SQLException {
		
		final List<IUnassignedModel> orders = new ArrayList<IUnassignedModel>();
				
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
				    		order.setUnassignedAction(rs.getString("UACTION"));
				    		
				    		order.setOverrideOrderSize(rs.getDouble("OSIZE"));
				    		order.setOverrideServiceTime(rs.getDouble("SRTIME"));
				    		
				    		order.setReservedOrderSize(rs.getDouble("ROSIZE"));
				    		order.setReservedServiceTime(rs.getDouble("RSRTIME"));
							
				    		EnumRoutingUpdateStatus _status = EnumRoutingUpdateStatus.getEnum(rs.getString("UPDSTATUS"));
				    		if(_status != null) {
				    			order.setUpdateStatus(_status.value());
				    		}
				    						    						    						    		
				    		IDeliveryModel dModel = new DeliveryModel();
				    		dModel.setDeliveryDate(rs.getDate("BDATE"));
				    		dModel.setDeliveryStartTime(rs.getTimestamp("STIME"));
				    		dModel.setDeliveryEndTime(rs.getTimestamp("ETIME"));
				    		dModel.setReservationId(rs.getString("RID"));
				    						    		
				    		IPackagingModel pModel = new PackagingModel();
				    		pModel.setNoOfCartons(rs.getLong("NCARTONS"));
				    		pModel.setNoOfCases(rs.getLong("NCASES"));
				    		pModel.setNoOfFreezers(rs.getLong("NFREEZERS"));
				    		dModel.setPackagingDetail(pModel);
				    		
				    		IZoneModel zModel = new ZoneModel();
				    		zModel.setZoneNumber(rs.getString("ZCODE"));
				    		
				    		dModel.setDeliveryZone(zModel);
				    		
				    		order.setDeliveryInfo(dModel);
				    		
				    		IDeliverySlot slot = new DeliverySlot();
				    		slot.setDynamicActive("X".equalsIgnoreCase(rs.getString("IS_DYNAMIC")) ? true : false);
				    		slot.setManuallyClosed("X".equalsIgnoreCase(rs.getString("IS_CLOSED")) ? true : false);
				    		
				    		IUnassignedModel root = new UnassignedModel();
				    		root.setOrder(order);
				    		root.setSlot(slot);
				    		root.setIsChefsTable(rs.getString("CTFLAG"));
				    		root.setIsForced(rs.getString("FORCEFLAG"));
				    					    		
				    		
				    		orders.add(root);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return orders;
	}
	
	public IOrderModel getRoutingOrderByReservation(final String reservationId) throws SQLException {

		final IOrderModel order = new OrderModel();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_UNASSIGNEDRESERVATION_QRY);
				ps.setString(1, reservationId);

				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do { 
						
					order.setOrderNumber(rs.getString("OID"));
					order.setCustomerNumber(rs.getString("CID"));
					order.setCreateModifyTime(rs.getTimestamp("SCROMODDATE"));
					order.setUnassignedTime(rs.getTimestamp("UDATETIME"));
					order.setUnassignedAction(rs.getString("UACTION"));
					
					order.setOverrideOrderSize(rs.getDouble("OSIZE"));
		    		order.setOverrideServiceTime(rs.getDouble("SRTIME"));
		    		
		    		order.setReservedOrderSize(rs.getDouble("ROSIZE"));
		    		order.setReservedServiceTime(rs.getDouble("RSRTIME"));
		    		
					EnumRoutingUpdateStatus _status = EnumRoutingUpdateStatus.getEnum(rs.getString("UPDSTATUS"));
		    		if(_status != null) {
		    			order.setUpdateStatus(_status.value());
		    		}
					
					IDeliveryModel dModel = new DeliveryModel();
					dModel.setDeliveryDate(rs.getDate("BDATE"));
					dModel.setDeliveryStartTime(rs.getTimestamp("STIME"));
					dModel.setDeliveryEndTime(rs.getTimestamp("ETIME"));
					dModel.setReservationId(rs.getString("RID"));
					
					IPackagingModel pModel = new PackagingModel();
					pModel.setNoOfCartons(rs.getLong("NCARTONS"));
					pModel.setNoOfCases(rs.getLong("NCASES"));
					pModel.setNoOfFreezers(rs.getLong("NFREEZERS"));
					dModel.setPackagingDetail(pModel);
					
					IZoneModel zModel = new ZoneModel();
					zModel.setZoneNumber(rs.getString("ZCODE"));

					dModel.setDeliveryZone(zModel);

					order.setDeliveryInfo(dModel);

				} while(rs.next());		        		    	
			}
		}
		);
		return order;
	}
	
	public List<IOrderModel> getRoutingOrderByDate(final Date deliveryDate
															, final String zoneCode, final boolean filterExpiredCancelled) throws SQLException {

		final List<IOrderModel> result = new ArrayList<IOrderModel>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_RESERVATIONBYDATE_QRY);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setString(2, zoneCode);
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do { 
					int statusCode = rs.getInt("STATUS");
					if(filterExpiredCancelled && (statusCode == 15 || statusCode == 20)) {
						continue;
					}
					IOrderModel order = new OrderModel();	
					result.add(order);
					
					order.setOrderNumber(rs.getString("OID"));
					order.setCustomerNumber(rs.getString("CID"));
					
					order.setUnassignedTime(rs.getTimestamp("UDATETIME"));
					order.setUnassignedAction(rs.getString("UACTION"));
					
					order.setOverrideOrderSize(rs.getDouble("OSIZE"));
		    		order.setOverrideServiceTime(rs.getDouble("SRTIME"));
		    		
		    		order.setReservedOrderSize(rs.getDouble("ROSIZE"));
		    		order.setReservedServiceTime(rs.getDouble("RSRTIME"));
		    		
					EnumRoutingUpdateStatus _status = EnumRoutingUpdateStatus.getEnum(rs.getString("UPDSTATUS"));
		    		if(_status != null) {
		    			order.setUpdateStatus(_status.value());
		    		}
					
					IDeliveryModel dModel = new DeliveryModel();
					dModel.setDeliveryDate(rs.getDate("BDATE"));
					dModel.setDeliveryStartTime(rs.getTimestamp("STIME"));
					dModel.setDeliveryEndTime(rs.getTimestamp("ETIME"));
					dModel.setReservationId(rs.getString("RID"));
					
					IPackagingModel pModel = new PackagingModel();
					pModel.setNoOfCartons(rs.getLong("NCARTONS"));
					pModel.setNoOfCases(rs.getLong("NCASES"));
					pModel.setNoOfFreezers(rs.getLong("NFREEZERS"));
					dModel.setPackagingDetail(pModel);
					
					IZoneModel zModel = new ZoneModel();
					zModel.setZoneNumber(rs.getString("ZCODE"));

					dModel.setDeliveryZone(zModel);

					order.setDeliveryInfo(dModel);

				} while(rs.next());		        		    	
			}
		}
		);
		return result;
	}
	
	
	public int updateRoutingOrderByReservation(final String reservationId
												, final double overrideOrderSize
												, final double overriderServiceTime) throws SQLException {

		Connection connection=null;
		int result = 0;
		try{
			result = this.jdbcTemplate.update(UPDATE_UNASSIGNEDRESERVATION_QRY, new Object[] {new Double(overrideOrderSize), new Double(overriderServiceTime)
												, EnumRoutingUpdateStatus.OVERRIDDEN.value(), reservationId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public int updateTimeslotForStatus(final String timeslotId, final boolean isClosed) throws SQLException {

		Connection connection=null;
		int result = 0;
		try{
			result = this.jdbcTemplate.update(UPDATE_TIMESLOTFORSTATUS_QRY, new Object[] {(isClosed ? "X" : null), timeslotId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public int updateTimeslotForStatusByZone(final Date baseDate, final String zoneCode, final String cutOff, final boolean isClosed) throws SQLException {

		Connection connection=null;
		int result = 0;
		StringBuffer strUpdateQuery = new StringBuffer();
		strUpdateQuery.append(UPDATE_TIMESLOTFORSTATUSBYZONE_QRY);
		try{			
			if(cutOff == null || cutOff.trim().length() == 0) {
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isClosed ? "X" : null), baseDate, zoneCode});
			} else {
				strUpdateQuery.append(UPDATE_TIMESLOTCUTUFF_QRYAPPEND);
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isClosed ? "X" : null), baseDate, zoneCode, cutOff});
			}
			
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public int updateTimeslotForStatusByRegion(final Date baseDate, final String regionCode, final String cutOff, final boolean isClosed) throws SQLException {

		Connection connection=null;
		int result = 0;
		StringBuffer strUpdateQuery = new StringBuffer();
		strUpdateQuery.append(UPDATE_TIMESLOTFORSTATUSBYREGION_QRY);
		try{
			if(cutOff == null || cutOff.trim().length() == 0) {
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isClosed ? "X" : null), baseDate, regionCode});
			} else {
				strUpdateQuery.append(UPDATE_TIMESLOTCUTUFF_QRYAPPEND);
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isClosed ? "X" : null), baseDate, regionCode, cutOff});
			}			
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public int updateTimeslotForDynamicStatus(final String timeslotId, final boolean isDynamic) throws SQLException {

		Connection connection=null;
		int result = 0;
		try{
			result = this.jdbcTemplate.update(UPDATE_TIMESLOTFORDYNAMICSTATUS_QRY, new Object[] {(isDynamic ? "X" : null), timeslotId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public int updateTimeslotForDynamicStatusByZone(final Date baseDate, final String zoneCode,final String cutOff, final boolean isDynamic) throws SQLException {

		Connection connection=null;
		int result = 0;
		StringBuffer strUpdateQuery = new StringBuffer();
		strUpdateQuery.append(UPDATE_TIMESLOTFORDYNAMICSTATUSBYZONE_QRY);
		try{
			if(cutOff == null || cutOff.trim().length() == 0) {
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isDynamic ? "X" : null), baseDate, zoneCode});
			} else {
				strUpdateQuery.append(UPDATE_TIMESLOTCUTUFF_QRYAPPEND);
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isDynamic ? "X" : null), baseDate, zoneCode, cutOff});
			}			
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public int updateTimeslotForDynamicStatusByRegion(final Date baseDate, final String regionCode,final String cutOff, final boolean isDynamic) throws SQLException {

		Connection connection=null;
		int result = 0;
		StringBuffer strUpdateQuery = new StringBuffer();
		strUpdateQuery.append(UPDATE_TIMESLOTFORDYNAMICSTATUSBYREGION_QRY);
		try{
			if(cutOff == null || cutOff.trim().length() == 0) {
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isDynamic ? "X" : null), baseDate, regionCode});
			} else {
				strUpdateQuery.append(UPDATE_TIMESLOTCUTUFF_QRYAPPEND);
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isDynamic ? "X" : null), baseDate, regionCode, cutOff});
			}
			
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}

}
