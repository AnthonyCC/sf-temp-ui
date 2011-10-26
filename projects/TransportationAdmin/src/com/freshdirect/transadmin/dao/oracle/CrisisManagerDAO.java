package com.freshdirect.transadmin.dao.oracle;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.routing.constants.EnumDeliveryType;
import com.freshdirect.routing.constants.EnumProfileList;
import com.freshdirect.routing.constants.EnumReservationType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.routing.constants.EnumReservationStatus;
import com.freshdirect.transadmin.dao.ICrisisManagerDAO;
import com.freshdirect.transadmin.model.ActiveOrderModel;
import com.freshdirect.transadmin.model.CancelOrderModel;
import com.freshdirect.transadmin.model.CrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.model.CrisisManagerBatchReservation;
import com.freshdirect.routing.model.CustomerModel;
import com.freshdirect.transadmin.model.IActiveOrderModel;
import com.freshdirect.transadmin.model.ICancelOrderModel;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchAction;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.model.ICrisisManagerBatchOrder;
import com.freshdirect.transadmin.model.CrisisManagerBatch;
import com.freshdirect.transadmin.model.CrisisManagerBatchAction;
import com.freshdirect.transadmin.model.CrisisManagerBatchOrder;
import com.freshdirect.transadmin.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.model.ICustomerModel;
import com.freshdirect.routing.model.IStandingOrderModel;
import com.freshdirect.routing.model.StandingOrderModel;


public class CrisisManagerDAO implements ICrisisManagerDAO   {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	private static final String GET_CRISISMNGBATCHNEXTSEQ_QRY = "SELECT TRANSP.CRISISMNGBATCHSEQ.nextval FROM DUAL";
	
	private static final String INSERT_CRISISMNGBATCH = "INSERT INTO TRANSP.CRISISMNG_BATCH ( BATCH_ID,"+
			"DELIVERY_DATE, BATCH_STATUS, SYS_MESSAGE, IS_CANCEL_ELIGIBLE, DESTINATION_DATE, AREA, CUTOFF_DATETIME, WINDOW_STARTTIME, WINDOW_ENDTIME, DELIVERY_TYPE, INCLUDE_STANDINGORDER, PROFILE_NAME)" +
			" VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,? )";
	
	private static final String INSERT_CRISISMNGBATCHACTION = "INSERT INTO TRANSP.CRISISMNG_BATCHACTION ( BATCH_ID,"+
			"ACTION_DATETIME, ACTION_TYPE, ACTION_BY ) VALUES ( ?,?,?,?)";
	
	private static final String GET_CRISISMNGBATCH_DELIVERYDATE = "SELECT b.BATCH_ID, b.DELIVERY_DATE, B.BATCH_STATUS, B.SYS_MESSAGE, B.IS_CANCEL_ELIGIBLE, "+ 
	    " BA.ACTION_BY, BA.ACTION_DATETIME, BA.ACTION_TYPE , B.DESTINATION_DATE, B.CUTOFF_DATETIME, B.AREA ZONE_NO, B.WINDOW_STARTTIME, B.WINDOW_ENDTIME, B.DELIVERY_TYPE, B.INCLUDE_STANDINGORDER, B.PROFILE_NAME, "+             
	    " BO.WEBORDER_ID, BO.ERPORDER_ID,  BO.AREA,  BO.RESERVATION_TYPE, BO.WINDOW_STARTTIME ORDER_STARTTIME, BO.WINDOW_ENDTIME ORDER_ENDTIME, "+
	    " (select count(1) from transp.CRISISMNG_BATCHORDER xO where xO.BATCH_ID = B.BATCH_ID) ORDERCOUNT, "+
	    " (select count(1) from transp.CRISISMNG_BATCHORDER xO where xO.BATCH_ID = B.BATCH_ID and xO.ORDER_STATUS='CAN') ORDERCANCELCOUNT, "+
	    " (select count(1) from transp.CRISISMNG_BATCHRESERVATION yO where yO.BATCH_ID = B.BATCH_ID) RSVCOUNT, "+
	    " (select count(1) from transp.CRISISMNG_BATCHRESERVATION yO where yO.BATCH_ID = B.BATCH_ID and yO.STATUS_CODE=25) RSVCANCELCOUNT "+
	    " from transp.CRISISMNG_BATCH b, transp.CRISISMNG_BATCHACTION ba, transp.CRISISMNG_BATCHORDER bo "+  
	    " where B.DELIVERY_DATE = ? and B.BATCH_ID = BA.BATCH_ID  and B.BATCH_ID = BO.BATCH_ID(+) "+  
	    " order by B.BATCH_ID, B.DELIVERY_DATE, BA.ACTION_DATETIME"; 
	
	private static final String GET_CRISISMNGBATCH_DATERANGE = "SELECT b.BATCH_ID, b.DELIVERY_DATE, B.BATCH_STATUS, B.SYS_MESSAGE, B.IS_CANCEL_ELIGIBLE, "+ 
        " BA.ACTION_BY, BA.ACTION_DATETIME, BA.ACTION_TYPE , B.DESTINATION_DATE, B.CUTOFF_DATETIME, B.AREA ZONE_NO, B.WINDOW_STARTTIME, B.WINDOW_ENDTIME, B.DELIVERY_TYPE, B.INCLUDE_STANDINGORDER,B.PROFILE_NAME, "+             
        " BO.WEBORDER_ID, BO.ERPORDER_ID,  BO.AREA,  BO.RESERVATION_TYPE, BO.WINDOW_STARTTIME ORDER_STARTTIME, BO.WINDOW_ENDTIME ORDER_ENDTIME, "+
        " (select count(1) from transp.CRISISMNG_BATCHORDER xO where xO.BATCH_ID = B.BATCH_ID) ORDERCOUNT, "+
        " (select count(1) from transp.CRISISMNG_BATCHORDER xO where xO.BATCH_ID = B.BATCH_ID and xO.ORDER_STATUS='CAN') ORDERCANCELCOUNT, "+
	    " (select count(1) from transp.CRISISMNG_BATCHRESERVATION yO where yO.BATCH_ID = B.BATCH_ID) RSVCOUNT, "+
	    " (select count(1) from transp.CRISISMNG_BATCHRESERVATION yO where yO.BATCH_ID = B.BATCH_ID and yO.STATUS_CODE=25) RSVCANCELCOUNT "+
	    " from transp.CRISISMNG_BATCH b, transp.CRISISMNG_BATCHACTION ba, transp.CRISISMNG_BATCHORDER bo "+  
        " where B.DELIVERY_DATE > (sysdate - ?) and B.BATCH_ID = BA.BATCH_ID  and B.BATCH_ID = BI.BATCH_ID(+) and B.BATCH_ID = BO.BATCH_ID(+) "+  
        " order by b.BATCH_ID, b.DELIVERY_DATE, BA.ACTION_DATETIME"; 
		
	private static String GET_ORDER_BYCRITERIA = 
		"SELECT "
		+ " c.id CUSTOMERID, ci.first_name FIRSTNAME, ci.last_name LASTNAME, c.user_id EMAIL, ci.home_phone, ci.business_phone, ci.business_ext, "
		+ " ci.cell_phone, s.id WEBORDER_ID, s.sap_number ERPORDER_ID, sa.requested_date DELIVERY_DATE, s.status STATUS, sa.amount, di.starttime, di.endtime, "
		+ " di.cutofftime CUTOFFTIME, rs.type RSVTYPE, di.zone AREA, di.delivery_type, di.company_name, di.charity_name, s.standingorder_id, rs.address_id "
		+ " from cust.customer c, cust.customerinfo ci, cust.sale s, cust.salesaction sa, cust.deliveryinfo di, dlv.reservation rs "
		+ " where c.id = ci.customer_id and c.id = s.customer_id and s.id = sa.sale_id and sa.action_type IN ('CRO', 'MOD') "
		+ " and sa.customer_id = s.customer_id and sa.action_date = s.cromod_date and sa.id = di.salesaction_id and rs.id = di.reservation_id "
		+ " and s.type ='REG' and s.status <> 'CAN'  "		
		+ " and sa.requested_date = ? ";
	
	private static String GET_ORDERSTATSBY_DATE_BATCH = "SELECT s.status, count(*) as order_count "+
		" from cust.sale s, cust.salesaction sa, cust.deliveryinfo di, dlv.reservation rs "+
        " where s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date = ? and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "+ 
        " and s.type ='REG' and sa.id = di.salesaction_id and rs.id = di.reservation_id "+
        " and s.id in (select weborder_id from transp.CRISISMNG_BATCHORDER where batch_id = ?) "+  
        " group by s.status ";
	
	private static String GET_STANDINGORDER_BYCRITERIA = "select c.id CUSTOMERID, ci.first_name FIRSTNAME, ci.last_name LASTNAME, s.id WEBORDER_ID, SO.ID STANDINGORDER_ID, di.zone AREA, "+
		" (select count(*) from CUST.ORDERLINE o where O.SALESACTION_ID=sa.id) SALE_LINEITEMCOUNT, "+        
		" (select count(*) from CUST.CUSTOMERLIST_DETAILS x where X.LIST_ID=CL.ID) TEMPLATE_LINEITEMCOUNT "+       
		" from cust.customer c, cust.customerinfo ci, cust.sale s, cust.salesaction sa, cust.standing_order so, cust.customerlist cl, cust.deliveryinfo di "+
		" where c.id = ci.customer_id and c.id = s.customer_id and s.id = sa.sale_id and sa.action_type IN ('CRO', 'MOD') and s.type='REG' and s.status <> 'CAN' "+
		" and sa.action_date = s.cromod_date and sa.id = di.salesaction_id and s.standingorder_id = so.id and so.customerlist_id = cl.id and sa.requested_date = ? ";
	
	private static final String GET_RESERVATION_BYCRITERIA = 
        "SELECT " + 
        " c.id CUSTOMERID, ci.first_name FIRSTNAME , ci.last_name LASTNAME, c.user_id EMAIL, ci.home_phone, ci.business_phone, "+ 
        " ci.cell_phone, ts.base_date DELIVERY_DATE, ts.start_time STARTTIME, ts.end_time ENDTIME, ts.cutoff_time CUTOFFTIME, ze.zone_code AREA, rs.id, rs.type, "+
        " rs.address_id ,rs.status_code, rs.expiration_datetime EXPDATETIME "+
        " from cust.customer c, cust.customerinfo ci, dlv.reservation rs, dlv.timeslot ts, dlv.zone ze "+ 
        " where ts.id = rs.timeslot_id and ze.id = ts.zone_id and rs.customer_id = c.id and ci.customer_id = c.id and rs.status_code = ? "+ 
        " and rs.type in ('WRR','OTR','STD')  "+
        " and ts.base_date = ? ";	
	
	private static final String UPDATE_CRISISMNGBATCH_MESSAGE = "UPDATE TRANSP.CRISISMNG_BATCH SET SYS_MESSAGE = ? where BATCH_ID = ?";
	
	private static final String UPDATE_CRISISMNGBATCH_STATUS = "UPDATE TRANSP.CRISISMNG_BATCH SET BATCH_STATUS = ? where BATCH_ID = ?";
			
	private static final String GET_CRISISMNGBATCH_BYID = "SELECT b.BATCH_ID, b.DELIVERY_DATE, B.BATCH_STATUS, B.SYS_MESSAGE, B.IS_CANCEL_ELIGIBLE, "+ 
	    " BA.ACTION_BY, BA.ACTION_DATETIME, BA.ACTION_TYPE , B.DESTINATION_DATE, B.CUTOFF_DATETIME, B.AREA ZONE_NO, B.WINDOW_STARTTIME, B.WINDOW_ENDTIME, B.DELIVERY_TYPE, B.INCLUDE_STANDINGORDER, B.PROFILE_NAME, "+             
	    " BO.WEBORDER_ID, BO.ERPORDER_ID,  BO.AREA,  BO.RESERVATION_TYPE, BO.WINDOW_STARTTIME ORDER_STARTTIME, BO.WINDOW_ENDTIME ORDER_ENDTIME, "+
	    " (select count(1) from transp.CRISISMNG_BATCHORDER xO where xO.BATCH_ID = B.BATCH_ID) ORDERCOUNT, "+
        " (select count(1) from transp.CRISISMNG_BATCHORDER xO where xO.BATCH_ID = B.BATCH_ID and xO.ORDER_STATUS='CAN') ORDERCANCELCOUNT, "+
	    " (select count(1) from transp.CRISISMNG_BATCHRESERVATION yO where yO.BATCH_ID = B.BATCH_ID) RSVCOUNT, "+
	    " (select count(1) from transp.CRISISMNG_BATCHRESERVATION yO where yO.BATCH_ID = B.BATCH_ID and yO.STATUS_CODE=25) RSVCANCELCOUNT "+
	    " from transp.CRISISMNG_BATCH b, transp.CRISISMNG_BATCHACTION ba, transp.CRISISMNG_BATCHORDER bo "+  
	    " where B.BATCH_ID = ? and B.BATCH_ID = BA.BATCH_ID  and B.BATCH_ID = BO.BATCH_ID(+) "+  
	    " order by B.BATCH_ID, B.DELIVERY_DATE, BA.ACTION_DATETIME";
	
	private static final String CLEAR_CRISISMNGBATCH_ORDERS = "DELETE FROM TRANSP.CRISISMNG_BATCHORDER X WHERE X.BATCH_ID = ?";
	
	private static final String INSERT_CRISISMNGBATCH_ORDER = "INSERT INTO TRANSP.CRISISMNG_BATCHORDER ( BATCH_ID,"+
		" DELIVERY_DATE, WEBORDER_ID, ERPORDER_ID, AREA, WINDOW_STARTTIME, WINDOW_ENDTIME, ORDER_STATUS, CUTOFFTIME, CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL, " +
		" HOME_PHONE, BUSINESS_PHONE, CELL_PHONE, RESERVATION_TYPE, AMOUNT, DELIVERY_TYPE, STANDINGORDER_ID, ADDRESS_ID, COMPANY_NAME, BUSINESS_EXT ) "+
		" VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
	
	private static final String GET_CRISISMNGBATCH_ORDERS = "SELECT * FROM TRANSP.CRISISMNG_BATCHORDER X WHERE X.BATCH_ID = ? ";
	
	private static final String UPDATE_CRISISMNGBATCH_ORDEREXCEPTION = "UPDATE TRANSP.CRISISMNG_BATCHORDER SET IS_EXCEPTION = 'X' " +
				" where BATCH_ID = ? AND WEBORDER_ID in (";
	
	private static final String CLEAR_CRISISMNGBATCH_RESERVATION = "DELETE FROM TRANSP.CRISISMNG_BATCHRESERVATION X WHERE X.BATCH_ID = ?";
	
	private static final String INSERT_CRISISMNGBATCH_RESERVATION = "INSERT INTO TRANSP.CRISISMNG_BATCHRESERVATION ( BATCH_ID,"+
		" CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL, HOME_PHONE, BUSINESS_PHONE, CELL_PHONE, RESERVATION_ID, DELIVERY_DATE, AREA, WINDOW_STARTTIME, WINDOW_ENDTIME, STATUS_CODE, CUTOFFTIME, RESERVATION_TYPE, ADDRESS_ID ) "+
		" VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

	private static final String GET_CRISISMNGBATCH_RESERVATION = "SELECT * FROM TRANSP.CRISISMNG_BATCHRESERVATION X WHERE X.BATCH_ID = ? and X.STATUS_CODE = 5";
	
	private static final String UPDATE_CRISISMNGBATCH_RSVEXCEPTION = "UPDATE TRANSP.CRISISMNG_BATCHRESERVATION SET IS_EXCEPTION = 'X' " +
		" where BATCH_ID = ? AND RESERVATION_ID in (";
	
	private static final String UPDATE_CRISISMNGBATCH_ORDERSTATUS = "UPDATE TRANSP.CRISISMNG_BATCHORDER SET ORDER_STATUS = 'CAN' where BATCH_ID = ? ";
	
	private static final String UPDATE_CRISISMNGBATCH_RSVSTATUS = "UPDATE TRANSP.CRISISMNG_BATCHRESERVATION SET STATUS_CODE = 25 where BATCH_ID = ? ";
	
	private static final String GETCRISISMNGBATCH_TIMESLOTBYZONEQRY = "select b.batch_id, o.area, o.window_starttime, o.window_endtime "+
		" from TRANSP.CRISISMNG_BATCHORDER o, TRANSP.CRISISMNG_BATCH b "+
		" where b.batch_id = o.batch_id and b.batch_id = ? "+
		" group by b.batch_id, o.area, o.window_starttime, o.window_endtime order by o.area, o.window_starttime asc";
	
	private static final String GET_TIMESLOT_BYDATEQRY = "select z.ZONE_CODE AREA, t.START_TIME, t.END_TIME, t.ID "+
        " from dlv.timeslot t, dlv.zone z, transp.zone ta "+
        " where t.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and t.base_date = ? and T.CAPACITY <> '0' "+
        " group by z.ZONE_CODE, t.START_TIME, t.END_TIME, t.ID order by area, t.start_time asc";
		
	private static final String INSERT_CRISISMNGBATCH_DELIVERYSLOT = "INSERT INTO TRANSP.CRISISMNG_BATCHTIMESLOT (BATCH_ID, AREA,WINDOW_STARTTIME, WINDOW_ENDTIME, NEW_WINDOWSTARTTIME, NEW_WINDOWENDTIME, TIMESLOT_ID) "+
		"VALUES (?,?,?,?,?,?,?) ";
	
	private static final String UPDATE_CRISISMNGBATCH_DELIVERYSLOT = 
		" UPDATE TRANSP.CRISISMNG_BATCHTIMESLOT BT SET NEW_WINDOWSTARTTIME = ?, NEW_WINDOWENDTIME = ?, TIMESLOT_ID = ? "+
    	" where BT.BATCH_ID = ? and BT.AREA = ? and to_char(BT.WINDOW_STARTTIME, 'HH:MI AM') = to_char(?, 'HH:MI AM') and to_char(BT.WINDOW_ENDTIME, 'HH:MI AM') = to_char(?, 'HH:MI AM') ";
	
	private static final String CLEAR_CRISISMNGBATCH_DELIVERYSLOT = "DELETE FROM TRANSP.CRISISMNG_BATCHTIMESLOT X WHERE X.BATCH_ID = ?";
	
	private static final String GET_CRISISMNGBATCH_TIMESLOT = "select ts.area, ts.window_starttime, ts.window_endtime, ts.new_windowstarttime, ts.new_windowendtime, ts.timeslot_id "+
		" from transp.CRISISMNG_BATCHTIMESLOT ts, transp.CRISISMNG_BATCH b "+
		" where  b.batch_id = ts.batch_id and b.batch_id = ?  ";
	
	private static final String INSERT_CRISISMNGBATCH_STANDINGORDER = "INSERT INTO TRANSP.CRISISMNG_BATCHSTANDINGORDER ( BATCH_ID,"+
		" STANDINGORDER_ID, SALE_ID, SALE_LINEITEMCOUNT, TEMPLATE_LINEITEMCOUNT, AREA, CUSTOMER_ID ) VALUES ( ?,?,?,?,?,?,? )";
	
	private static final String CLEAR_CRISISMNGBATCH_STANDINGORDER = "DELETE FROM TRANSP.CRISISMNG_BATCHSTANDINGORDER X WHERE X.BATCH_ID = ? ";
	
	private static final String GET_CRISISMNGBATCH_STANDINGORDER = "SELECT * FROM TRANSP.CRISISMNG_BATCHSTANDINGORDER X WHERE X.BATCH_ID = ? ";
	
	private static final String UPDATE_CRISISMNGBATCH_STANDINGORDEREXCEPTION = "UPDATE TRANSP.CRISISMNG_BATCHSTANDINGORDER SET ERROR_HEADER = ?, STATUS = ? " +
					" where BATCH_ID = ? AND STANDINGORDER_ID = ? ";
	
	private static final String UPDATE_CRISISMNGBATCH_ORDERRESERVATION = "UPDATE TRANSP.CRISISMNG_BATCHORDER SET NEWRESERVATION_ID = ? " +
	" where BATCH_ID = ? AND WEBORDER_ID = ? ";

	private static final String GET_ACTIVEORDERS_BYAREA = "SELECT  di.zone AREA, di.starttime, di.endtime, count(di.zone) as ORDERCOUNT "+
        " from cust.customer c, cust.fdcustomer fdc, cust.customerinfo ci, cust.sale s, cust.salesaction sa, cust.deliveryinfo di, dlv.reservation rs "+
        " where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id and s.id = sa.sale_id and sa.action_type IN ('CRO', 'MOD') "+ 
        " and s.type ='REG' and s.status <> 'CAN' and sa.action_date = s.cromod_date "+         
        " and sa.id = di.salesaction_id and rs.id = di.reservation_id and sa.requested_date = ? "+ 
        " group by di.zone, di.starttime, di.endtime order by  di.zone, di.starttime asc ";
	
	private static final String GET_CANCELORDER_BYAREA = "SELECT "+
        " BO.AREA, BO.WINDOW_STARTTIME STARTTIME, BO.WINDOW_ENDTIME ENDTIME, count(BO.AREA) as ORDERCOUNT, count(BO.NEWRESERVATION_ID) as RSVCOUNT "+
        " from transp.CRISISMNG_BATCH b, transp.CRISISMNG_BATCHORDER bo "+ 
        " where B.BATCH_ID = ? and B.BATCH_ID = BO.BATCH_ID(+) and BO.ORDER_STATUS = 'CAN' "+
        " group by BO.AREA, BO.WINDOW_STARTTIME,  BO.WINDOW_ENDTIME order by area, BO.WINDOW_STARTTIME asc ";
	
	public String getNewCrisisManagerBatchId() throws SQLException {
		return ""+jdbcTemplate.queryForLong(GET_CRISISMNGBATCHNEXTSEQ_QRY);
	}
	
	public String addNewCrisisMngBatch(Date deliveryDate, Date destinationDate, String[] zoneArray, 
			Date cutOffDateTime, Date startTime, Date endTime, String[] deliveryType, boolean includeSO, String profileName, boolean isStandByMode) throws SQLException {
		
		Connection connection = null;
		String orderCrisisBatchId = null;
		try {
			orderCrisisBatchId = this.getNewCrisisManagerBatchId();
			String isCancelEligible = (isStandByMode ? null : "X");
			String isSOIncluded = (includeSO ? "X" : null);
			
			Date cutOff = null;
			if(cutOffDateTime != null)
				cutOff = new Timestamp(cutOffDateTime.getTime());
			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			ArrayDescriptor desc = ArrayDescriptor.createDescriptor("TRANSP.CRISISMANAGER_ZONE_NO", connection);
			ArrayDescriptor desc1 = ArrayDescriptor.createDescriptor("TRANSP.CRISISMANAGER_DELIVERYTYPE", connection);
			
			ARRAY zoneId = new ARRAY(desc, connection, zoneArray);
			ARRAY deliveryTypeId = new ARRAY(desc1, connection, deliveryType);
			
			this.jdbcTemplate.update(INSERT_CRISISMNGBATCH
											, new Object[] {orderCrisisBatchId
											, deliveryDate
											, EnumCrisisMngBatchStatus.NEW.value() 
											, "New Batch Created"
											, isCancelEligible
											, destinationDate
											, zoneId
											, cutOff
											, startTime
											, endTime
											, deliveryTypeId
											, isSOIncluded
											, profileName});
			
			
			
		} finally {
			if(connection!=null) connection.close();
		}
		return orderCrisisBatchId;
	}
	
	
	public void addNewCrisisMngBatchAction(String orderCrisisBatchId, Date actionDateTime
			, EnumCrisisMngBatchActionType actionType, String userId) throws SQLException {
		
		Connection connection = null;
		
		try {
			this.jdbcTemplate.update(INSERT_CRISISMNGBATCHACTION
											, new Object[] {orderCrisisBatchId
											, new Timestamp(actionDateTime.getTime())
											, actionType.value()
											, userId});
			
			connection = this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}	
		
	}
	
	public Set<ICrisisManagerBatch> getCrisisMngBatch(final Date deliveryDate) throws SQLException {
		final Set<ICrisisManagerBatch> result = new HashSet<ICrisisManagerBatch>();
		final Map<String, ICrisisManagerBatch> batchMapping = new HashMap<String, ICrisisManagerBatch>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				if(deliveryDate == null) {
					ps = connection.prepareStatement(GET_CRISISMNGBATCH_DATERANGE);
					ps.setInt(1, 7);
				} else {
					ps = connection.prepareStatement(GET_CRISISMNGBATCH_DELIVERYDATE);
					ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				}
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						processBatchFromResultset(rs, batchMapping);
					} while(rs.next());		        		    	
				}
		}
		);
		result.addAll(batchMapping.values());
		return result;
	}
	
	private void processBatchFromResultset(ResultSet rs, Map<String, ICrisisManagerBatch> batchMapping) throws SQLException {
		
		String _batchId = rs.getString("BATCH_ID");		
		String _actionBy = rs.getString("ACTION_BY");
		int orderCount = rs.getInt("ORDERCOUNT");
		int orderCancelCount = rs.getInt("ORDERCANCELCOUNT");
		int rsvCount = rs.getInt("RSVCOUNT");
		int rsvCancelCount = rs.getInt("RSVCANCELCOUNT");
		
		if(!batchMapping.containsKey(_batchId)) {
			ICrisisManagerBatch _batch = new CrisisManagerBatch();
			_batch.setBatchId(_batchId);
			
			_batch.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
			_batch.setDestinationDate(rs.getDate("DESTINATION_DATE"));
			_batch.setCutOffDateTime(rs.getTimestamp("CUTOFF_DATETIME"));
			_batch.setStartTime(rs.getTimestamp("WINDOW_STARTTIME"));
			_batch.setEndTime(rs.getTimestamp("WINDOW_ENDTIME"));
			Array array = rs.getArray("ZONE_NO");
			if(array != null) {							 							
				_batch.setArea((String[]) array.getArray());
			}
			Array array1 = rs.getArray("DELIVERY_TYPE");
			if(array1 != null) {							 							
				_batch.setDeliveryType((String[]) array1.getArray());
			}
			_batch.setStandingOrderIncluded("X".equalsIgnoreCase(rs.getString("INCLUDE_STANDINGORDER")));
			_batch.setStatus(EnumCrisisMngBatchStatus.getEnum(rs.getString("BATCH_STATUS")));
			_batch.setSystemMessage(rs.getString("SYS_MESSAGE"));
			_batch.setEligibleForCancel("X".equalsIgnoreCase(rs.getString("IS_CANCEL_ELIGIBLE")));
			_batch.setNoOfOrders(orderCount);
			_batch.setNoOfOrdersCancelled(orderCancelCount);
			_batch.setNoOfReservations(rsvCount);
			_batch.setNoOfReservationsCancelled(rsvCancelCount);
			_batch.setProfile(rs.getString("PROFILE_NAME"));
			
			_batch.setAction(new TreeSet<ICrisisManagerBatchAction>());
			_batch.setOrder(new ArrayList<ICrisisManagerBatchOrder>());
			batchMapping.put(_batchId, _batch);
		}
		
		if(_actionBy != null) {
			ICrisisManagerBatchAction action = new CrisisManagerBatchAction();
			action.setActionBy(_actionBy);
			action.setActionDateTime(rs.getTimestamp("ACTION_DATETIME"));
			action.setActionType(EnumCrisisMngBatchActionType.getEnum(rs.getString("ACTION_TYPE")));
			action.setBatchId(_batchId);
			batchMapping.get(_batchId).getAction().add(action);
		}		
	}
	
	public void updateCrisisMngBatchMessage(String OrderCrisisBatchId, String message) throws SQLException {
		
		Connection connection = null;		
		try {
			if(message != null) {
				message = message.substring(0 , Math.min(message.length(), 1024));
			}
			this.jdbcTemplate.update(UPDATE_CRISISMNGBATCH_MESSAGE
											, new Object[] {message, OrderCrisisBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void updateCrisisMngBatchStatus(String OrderCrisisBatchId, EnumCrisisMngBatchStatus status) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(UPDATE_CRISISMNGBATCH_STATUS
											, new Object[] {status.value(), OrderCrisisBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public ICrisisManagerBatch getCrisisMngBatchById(final String batchId) throws SQLException {

		final Map<String, ICrisisManagerBatch> batchMapping = new HashMap<String, ICrisisManagerBatch>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				ps = connection.prepareStatement(GET_CRISISMNGBATCH_BYID);
				ps.setString(1, batchId);
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						processBatchFromResultset(rs, batchMapping);
					} while(rs.next());		        		    	
				}
		});
		
		Collection result = batchMapping.values();
		if(result != null && result.size() > 0) {
			return (ICrisisManagerBatch) result.iterator().next();
		}
		return null;
	}
	
	public void clearCrisisMngBatchOrder(String orderCrisisBatchId) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_CRISISMNGBATCH_ORDERS, new Object[] {orderCrisisBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public List<ICrisisManagerBatchOrder> getOrderByCriteria(final Date deliveryDate, final Date cutOffDateTime
			, final String[] area, final String startTime, final String endTime, final String[] deliveryType, String profileName, boolean isSOIncluded) throws SQLException {
		
		final List<ICrisisManagerBatchOrder> result = new ArrayList<ICrisisManagerBatchOrder>();
		
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_ORDER_BYCRITERIA);
		
		orderSearchCriteria(cutOffDateTime, area, startTime, endTime,
				deliveryType, profileName, isSOIncluded, updateQ);
		
		Connection connection = null;
		try{
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
					PreparedStatement ps =
						connection.prepareStatement(updateQ.toString());
					ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
					int paramIndex = 2;
					if(cutOffDateTime != null)
							ps.setTimestamp(paramIndex++, new Timestamp(cutOffDateTime.getTime()));					
					if(startTime != null)				
							ps.setString(paramIndex++, startTime);						
					if(endTime != null) 
							ps.setString(paramIndex++, endTime);
						
					return ps;
				}  
			};
			
			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do { 
								ICrisisManagerBatchOrder orderModel = new CrisisManagerBatchOrder();	
								result.add(orderModel);
																
								orderModel.setErpCustomerPK(rs.getString("CUSTOMERID"));								
								orderModel.setFirstName(rs.getString("FIRSTNAME"));
								orderModel.setLastName(rs.getString("LASTNAME"));								
								orderModel.setAmount(rs.getString("AMOUNT"));
								orderModel.setArea(rs.getString("AREA"));
								orderModel.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
								orderModel.setCutOffTime(rs.getTimestamp("CUTOFFTIME"));
								orderModel.setStartTime(rs.getTimestamp("STARTTIME"));
								orderModel.setEndTime(rs.getTimestamp("ENDTIME"));
								orderModel.setEmail(rs.getString("EMAIL"));
								orderModel.setErpOrderNumber(rs.getString("ERPORDER_ID"));
								orderModel.setOrderNumber(rs.getString("WEBORDER_ID"));
								orderModel.setReservationType(EnumReservationType.getEnum(rs.getString("RSVTYPE")));
								orderModel.setOrderStatus(EnumSaleStatus.getSaleStatus(rs.getString("STATUS")));
								orderModel.setDeliveryType(EnumDeliveryType.getEnum(rs.getString("DELIVERY_TYPE")));
								orderModel.setStandingOrderId(rs.getString("STANDINGORDER_ID"));
								orderModel.setHomePhone(rs.getString("HOME_PHONE"));
								orderModel.setBusinessPhone(rs.getString("BUSINESS_PHONE"));
								orderModel.setCellPhone(rs.getString("CELL_PHONE"));
								orderModel.setAddressId(rs.getString("ADDRESS_ID"));
								orderModel.setBusinessExt(rs.getString("BUSINESS_EXT"));
								orderModel.setCompanyName(rs.getString("COMPANY_NAME") == null ? rs.getString("CHARITY_NAME") : rs.getString("COMPANY_NAME"));
								
							} while(rs.next());		 
							
						}
					}
			);
			
			return result;
			
		} finally {
			if(connection!=null) connection.close();
		}	
		
	}
	
	public Map<EnumSaleStatus, Integer> getOrderStatsByDate(final Date deliveryDate, final String batchId) throws SQLException {

		final Map<EnumSaleStatus, Integer> result = new HashMap<EnumSaleStatus, Integer>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {	
				String query = GET_ORDERSTATSBY_DATE_BATCH;
				
				PreparedStatement ps =
					connection.prepareStatement(query);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setString(2, batchId);
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						result.put(EnumSaleStatus.getSaleStatus(rs.getString("status")), rs.getInt("order_count"));						
	
					} while(rs.next());		        		    	
				}
		}
		);
		return result;
	}
	
	public void addNewCrisisMngBatchOrder(List<ICrisisManagerBatchOrder> batchOrders) throws SQLException {
		
		Connection connection = null;
		if(batchOrders != null && batchOrders.size() > 0) {
			try {			
				
				BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_CRISISMNGBATCH_ORDER);
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));		
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));	
				
				batchUpdater.compile();
				
				connection = this.jdbcTemplate.getDataSource().getConnection();
				
				for(ICrisisManagerBatchOrder _order : batchOrders){
					batchUpdater.update(new Object[]{_order.getBatchId(),
										_order.getDeliveryDate(),_order.getOrderNumber(),_order.getErpOrderNumber(),
										_order.getArea(),new TimeOfDay(_order.getStartTime()).getAsDate(), new TimeOfDay(_order.getEndTime()).getAsDate(),
										_order.getOrderStatus().getStatusCode(),_order.getCutOffTime(),_order.getErpCustomerPK(),
										_order.getFirstName(),_order.getLastName(), _order.getEmail(),_order.getHomePhone(),
										_order.getBusinessPhone(), _order.getCellPhone(),_order.getReservationType().getName(),
										_order.getAmount(),_order.getDeliveryType().getName(),_order.getStandingOrderId(),
										_order.getAddressId(),_order.getCompanyName(),_order.getBusinessExt()
									});
					
				}
				batchUpdater.flush();		
			} finally {
				if(connection!=null) connection.close();
			}
		}
	}
	
	public List<ICrisisManagerBatchOrder> getCrisisMngBatchOrders(final String batchId, final boolean filterException, final boolean filterOrder) throws SQLException {

		final List<ICrisisManagerBatchOrder> result = new ArrayList<ICrisisManagerBatchOrder>();
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_CRISISMNGBATCH_ORDERS);
		if(filterOrder){
			updateQ.append(" and x.ORDER_STATUS <> 'CAN' ");
		}
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(updateQ.toString());
				ps.setString(1, batchId);				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
								new RowCallbackHandler() { 
					public void processRow(ResultSet rs) throws SQLException {				    	
						do {
							
							ICrisisManagerBatchOrder model = new CrisisManagerBatchOrder();						
							
							model.setBatchId(rs.getString("BATCH_ID"));
							model.setArea(rs.getString("AREA"));
							model.setFirstName(rs.getString("FIRST_NAME"));
							model.setLastName(rs.getString("LAST_NAME"));
							model.setErpCustomerPK(rs.getString("CUSTOMER_ID"));
							model.setFdCustomerPK(rs.getString("FDCUSTOMER_ID"));
							model.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
							model.setCutOffTime(rs.getTimestamp("CUTOFFTIME"));
							model.setStartTime(rs.getTimestamp("WINDOW_STARTTIME"));
							model.setEndTime(rs.getTimestamp("WINDOW_ENDTIME"));
							model.setOrderNumber(rs.getString("WEBORDER_ID"));
							model.setErpOrderNumber(rs.getString("ERPORDER_ID"));						
							model.setEmail(rs.getString("EMAIL"));
							model.setAmount(rs.getString("AMOUNT"));
							model.setReservationType(EnumReservationType.getEnum(rs.getString("RESERVATION_TYPE")));
							model.setOrderStatus(EnumSaleStatus.getSaleStatus(rs.getString("ORDER_STATUS")));
							model.setDeliveryType(EnumDeliveryType.getEnum(rs.getString("DELIVERY_TYPE")));
							model.setHomePhone(rs.getString("HOME_PHONE"));
							model.setBusinessPhone(rs.getString("BUSINESS_PHONE"));
							model.setBusinessExt(rs.getString("BUSINESS_EXT"));
							model.setCellPhone(rs.getString("CELL_PHONE"));
							model.setStandingOrderId(rs.getString("STANDINGORDER_ID"));
							model.setException("X".equalsIgnoreCase(rs.getString("IS_EXCEPTION")));
							model.setCompanyName(rs.getString("COMPANY_NAME"));	
							model.setAddressId(rs.getString("ADDRESS_ID"));
							model.setReservationId(rs.getString("NEWRESERVATION_ID"));
							
							if(!model.isException() || !filterException) {
								result.add(model);
							}
						} while(rs.next());		        		    	
					}
			}
		);
		return result;
	}
	
	public void updateCrisisMngOrderException(String orderCrisisBatchId, List<String> exceptionOrderIds) throws SQLException {
		
		Connection connection = null;		
		try {
			if(exceptionOrderIds != null && exceptionOrderIds.size() > 0) {
				StringBuffer updateQ = new StringBuffer();
				updateQ.append(UPDATE_CRISISMNGBATCH_ORDEREXCEPTION);
				int intCount = 0;
				for(String expOrder : exceptionOrderIds) {
					updateQ.append("'").append(expOrder).append("'");
					intCount++;
					if(intCount != exceptionOrderIds.size()) {
						updateQ.append(",");
					}
				}
				updateQ.append(")");
				this.jdbcTemplate.update(updateQ.toString()
												, new Object[] {orderCrisisBatchId});
				
				connection=this.jdbcTemplate.getDataSource().getConnection();
			}
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void updateCrisisMngOrderStatus(String orderCrisisBatchId, List<String> exceptionOrderIds) throws SQLException {
		
		Connection connection = null;		
		try {
			StringBuffer updateQ = new StringBuffer();
			updateQ.append(UPDATE_CRISISMNGBATCH_ORDERSTATUS);
			if(exceptionOrderIds != null && exceptionOrderIds.size() > 0) {
				updateQ.append(" AND WEBORDER_ID not in (");
				int intCount = 0;
				for(String expOrder : exceptionOrderIds) {
					updateQ.append("'").append(expOrder).append("'");
					intCount++;
					if(intCount != exceptionOrderIds.size()) {
						updateQ.append(",");
					}
				}
				updateQ.append(")");				
			}
			this.jdbcTemplate.update(updateQ.toString()
											, new Object[] {orderCrisisBatchId});

			connection=this.jdbcTemplate.getDataSource().getConnection();
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public List<ICrisisManagerBatchReservation> getReservationByCriteria(final Date deliveryDate, final Date cutOffDateTime
			, String[] area, final String startTime, final String endTime, final String profileName) throws SQLException{
		
		final List<ICrisisManagerBatchReservation> result = new ArrayList<ICrisisManagerBatchReservation>();
		
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_RESERVATION_BYCRITERIA);
		
		if(area != null && area.length > 0){
			updateQ.append(" and ze.zone_code in (");
			for(int intCount = 0; intCount < area.length; intCount++ ) {
				updateQ.append("'").append(area[intCount]).append("'");				
				if(intCount < area.length-1) {
					updateQ.append(",");
				}
			}
			updateQ.append(")");
		}
		
		if(cutOffDateTime != null){
			updateQ.append(" and to_char(ts.cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM')");
		}
		if(startTime != null){
			updateQ.append(" and to_date(to_char(ts.start_time, 'HH:MI AM'), 'HH:MI AM') >= to_date(?, 'HH:MI AM')");
		}
		if(endTime != null){
			updateQ.append(" and to_date(to_char(ts.end_time, 'HH:MI AM'), 'HH:MI AM') <= to_date(?, 'HH:MI AM')");
		}
		if(EnumProfileList.CHEFSTABLE.getName().equals(profileName)){
			updateQ.append(" and exists (select 1 from cust.profile p, cust.fdcustomer fdc, cust.customer c "
							+" where p.customer_id=fdc.ID and c.id=fdc.erp_customer_id"
							+" and p.profile_name='ChefsTable' and p.profile_value='1' and c.id=ci.customer_id)");
		} else if(EnumProfileList.NONCHEFSTABLE.getName().equals(profileName)){
			updateQ.append(" and not exists (select 1 from cust.profile p, cust.fdcustomer fdc, cust.customer c "
							+" where p.customer_id=fdc.ID and c.id=fdc.erp_customer_id"
							+" and p.profile_name='ChefsTable' and p.profile_value='1' and c.id=ci.customer_id)");
		}
	
		Connection connection = null;
		try{
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
					PreparedStatement ps =
						connection.prepareStatement(updateQ.toString());					
					ps.setInt(1, EnumReservationStatus.RESERVED.getCode());
					ps.setDate(2, new java.sql.Date(deliveryDate.getTime()));
					int paramIndex = 3;
					if(cutOffDateTime != null){ 
						ps.setTimestamp(paramIndex++, new Timestamp(cutOffDateTime.getTime()));
					}
					if(startTime != null) {
						ps.setString(paramIndex++, startTime);
					}					
					if(endTime != null) { 
						ps.setString(paramIndex++, endTime); 
					}
						
					return ps;
				}  
			};
			
			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do { 
								ICrisisManagerBatchReservation reservationModel = new CrisisManagerBatchReservation();	
								result.add(reservationModel);
																
								reservationModel.setId(rs.getString("ID"));
								reservationModel.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
								reservationModel.setCutOffTime(rs.getTimestamp("CUTOFFTIME"));
								reservationModel.setArea(rs.getString("AREA"));
								reservationModel.setStartTime(rs.getTimestamp("STARTTIME"));
								reservationModel.setEndTime(rs.getTimestamp("ENDTIME"));					
								reservationModel.setType(EnumReservationType.getEnum(rs.getString("TYPE")));
								reservationModel.setAddressId(rs.getString("ADDRESS_ID"));
								reservationModel.setStatusCode(rs.getInt("STATUS_CODE"));
								reservationModel.setExpirationDateTime(rs.getTimestamp("EXPDATETIME"));
								
								ICustomerModel custModel = new CustomerModel();
								reservationModel.setCustomerModel(custModel);
								
								custModel.setErpCustomerPK(rs.getString("CUSTOMERID"));								
								custModel.setFirstName(rs.getString("FIRSTNAME"));
								custModel.setLastName(rs.getString("LASTNAME"));
								custModel.setEmail(rs.getString("EMAIL"));																
								custModel.setHomePhone(rs.getString("HOME_PHONE"));
								custModel.setBusinessPhone(rs.getString("BUSINESS_PHONE"));
								custModel.setCellPhone(rs.getString("CELL_PHONE"));
							
							} while(rs.next());
						}
					}
			);
			
			return result;
			
		} finally {
			if(connection!=null) connection.close();
		}		
	}
	
	public void clearCrisisMngBatchReservation(String orderCrisisBatchId) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_CRISISMNGBATCH_RESERVATION, new Object[] {orderCrisisBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void addNewCrisisMngBatchReservation(List<ICrisisManagerBatchReservation> batchReservations) throws SQLException {
		
		Connection connection = null;
		if(batchReservations != null && batchReservations.size() > 0) {
			try {			
				
				BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_CRISISMNGBATCH_RESERVATION);

				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));				
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.INTEGER));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.compile();
				
				connection = this.jdbcTemplate.getDataSource().getConnection();
				
				for(ICrisisManagerBatchReservation _reservation : batchReservations){
					batchUpdater.update(new Object[]{_reservation.getBatchId(),_reservation.getCustomerModel().getErpCustomerPK(),
										_reservation.getCustomerModel().getFirstName(),_reservation.getCustomerModel().getLastName(), _reservation.getCustomerModel().getEmail(),
										_reservation.getCustomerModel().getHomePhone(),_reservation.getCustomerModel().getBusinessPhone(), _reservation.getCustomerModel().getCellPhone(),
										_reservation.getId(),_reservation.getDeliveryDate(),_reservation.getArea(),_reservation.getStartTime(),_reservation.getEndTime(),
										_reservation.getStatusCode(),_reservation.getCutOffTime(),
										_reservation.getType().getName(), _reservation.getAddressId()
									});
					
				}
				batchUpdater.flush();		
			} finally {
				if(connection!=null) connection.close();
			}
		}
	}
	
	public Map<String, ICrisisManagerBatchReservation> getCrisisMngBatchReservation(final String batchId, final boolean filterException) throws SQLException {

		final Map<String, ICrisisManagerBatchReservation> result = new HashMap<String, ICrisisManagerBatchReservation>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_CRISISMNGBATCH_RESERVATION);
				ps.setString(1, batchId);				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
					public void processRow(ResultSet rs) throws SQLException {				    	
						do {
							
							ICrisisManagerBatchReservation infoModel = new CrisisManagerBatchReservation();						
							ICustomerModel custModel = new CustomerModel();
							
							infoModel.setBatchId(rs.getString("BATCH_ID"));
							infoModel.setId(rs.getString("RESERVATION_ID"));
							infoModel.setArea(rs.getString("AREA"));
							infoModel.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
							infoModel.setCutOffTime(rs.getTimestamp("CUTOFFTIME"));
							infoModel.setStartTime(rs.getTimestamp("WINDOW_STARTTIME"));
							infoModel.setEndTime(rs.getTimestamp("WINDOW_ENDTIME"));
							infoModel.setType(EnumReservationType.getEnum(rs.getString("RESERVATION_TYPE")));
							infoModel.setAddressId(rs.getString("ADDRESS_ID"));
							infoModel.setStatusCode(rs.getInt("STATUS_CODE"));
							infoModel.setException("X".equalsIgnoreCase(rs.getString("IS_EXCEPTION")));
							
							infoModel.setCustomerModel(custModel);
							custModel.setErpCustomerPK(rs.getString("CUSTOMER_ID"));
							custModel.setFirstName(rs.getString("FIRST_NAME"));
							custModel.setLastName(rs.getString("LAST_NAME"));							
							custModel.setEmail(rs.getString("EMAIL"));
							custModel.setHomePhone(rs.getString("HOME_PHONE"));
							custModel.setBusinessPhone(rs.getString("BUSINESS_PHONE"));
							custModel.setCellPhone(rs.getString("CELL_PHONE"));
				
							if(!infoModel.isException() || !filterException) {
								result.put(infoModel.getId(), infoModel);
							}
						} while(rs.next());		        		    	
					}
			}
		);
		return result;
	}

	public void updateCrisisMngReservationException(String crisisMngBatchId, List<String> exceptionRsvIds) throws  SQLException {

		Connection connection = null;		
		try {
			if(exceptionRsvIds != null && exceptionRsvIds.size() > 0) {
				StringBuffer updateQ = new StringBuffer();
				updateQ.append(UPDATE_CRISISMNGBATCH_RSVEXCEPTION);
				int intCount = 0;
				for(String expOrder : exceptionRsvIds) {
					updateQ.append("'").append(expOrder).append("'");
					intCount++;
					if(intCount != exceptionRsvIds.size()) {
						updateQ.append(",");
					}
				}
				updateQ.append(")");
				this.jdbcTemplate.update(updateQ.toString()
												, new Object[] {crisisMngBatchId});
				
				connection = this.jdbcTemplate.getDataSource().getConnection();
			}
			
		} finally {
			if (connection != null)
				connection.close();
		}
	}
	
	
	public void updateCrisisMngReservationStatus(String orderCrisisBatchId, List<String> exceptionRsvIds) throws SQLException {
		
		Connection connection = null;		
		try {
			StringBuffer updateQ = new StringBuffer();
			updateQ.append(UPDATE_CRISISMNGBATCH_RSVSTATUS);
			if(exceptionRsvIds != null && exceptionRsvIds.size() > 0) {
				updateQ.append(" AND RESERVATION_ID not in (");
				int intCount = 0;
				for(String expRsv : exceptionRsvIds) {
					updateQ.append("'").append(expRsv).append("'");
					intCount++;
					if(intCount != exceptionRsvIds.size()) {
						updateQ.append(",");
					}
				}
				updateQ.append(")");				
			}
			this.jdbcTemplate.update(updateQ.toString()
											, new Object[] {orderCrisisBatchId});

			connection=this.jdbcTemplate.getDataSource().getConnection();
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslotByZone(final String batchId) throws SQLException {
		
		final Map<String, List<ICrisisManagerBatchDeliverySlot>> slotMapping = new HashMap<String, List<ICrisisManagerBatchDeliverySlot>>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GETCRISISMNGBATCH_TIMESLOTBYZONEQRY);
				ps.setString(1, batchId);				
				return ps;
			}  
		};
		
		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do {
						processBatchDeliverySlotFromResultset(rs, slotMapping);
					} while(rs.next());
				}
		});
	
		
		return slotMapping;
	}
	
	private void processBatchDeliverySlotFromResultset(ResultSet rs, Map<String, List<ICrisisManagerBatchDeliverySlot>> slotMapping) throws SQLException {
		String _batchId = rs.getString("BATCH_ID");		
		String _area = rs.getString("AREA");
				
		if(!slotMapping.containsKey(_area)) {
			slotMapping.put(_area, new ArrayList<ICrisisManagerBatchDeliverySlot>());
		}
		ICrisisManagerBatchDeliverySlot model = new CrisisManagerBatchDeliverySlot();		
		model.setBatchId(_batchId);
		model.setArea(_area);
		model.setStartTime(rs.getTimestamp("WINDOW_STARTTIME"));
		model.setEndTime(rs.getTimestamp("WINDOW_ENDTIME"));
		slotMapping.get(_area).add(model);
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getTimeslotByDate(final Date deliveryDate) throws SQLException {
		
		final Map<String, List<ICrisisManagerBatchDeliverySlot>> slotMapping = new HashMap<String, List<ICrisisManagerBatchDeliverySlot>>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_TIMESLOT_BYDATEQRY);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));				
				return ps;
			}  
		};
		
		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do {
						String _area = rs.getString("AREA");						
						if(!slotMapping.containsKey(_area)) {
							slotMapping.put(_area, new ArrayList<ICrisisManagerBatchDeliverySlot>());
						}
						ICrisisManagerBatchDeliverySlot model = new CrisisManagerBatchDeliverySlot();		
						model.setArea(_area);
						model.setStartTime(rs.getTimestamp("START_TIME"));
						model.setEndTime(rs.getTimestamp("END_TIME"));
						model.setTimeSlotId(rs.getString("ID"));
						slotMapping.get(_area).add(model);
					} while(rs.next());
				}
		});
		
		return slotMapping;
	}
	
	public void addCrisisMngBatchDeliveryslot(Map<String, List<ICrisisManagerBatchDeliverySlot>> batchGroupedSlots) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_CRISISMNGBATCH_DELIVERYSLOT);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));	
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			for(Map.Entry<String, List<ICrisisManagerBatchDeliverySlot>> slotEntry : batchGroupedSlots.entrySet()){
				Iterator<ICrisisManagerBatchDeliverySlot> _slotItr = slotEntry.getValue().iterator();
				ICrisisManagerBatchDeliverySlot model = null;
				while(_slotItr.hasNext()) {
					model = _slotItr.next();
					batchUpdater.update(new Object[]{ model.getBatchId()
							, model.getArea()
							, model.getStartTime()
							, model.getEndTime()
							, model.getDestStartTime()
							, model.getDestEndTime()
							, model.getTimeSlotId()
					});
				}
			}
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}
	
	public void updateCrisisMngBatchDeliveryslot(List<ICrisisManagerBatchDeliverySlot> timeSlotList) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), UPDATE_CRISISMNGBATCH_DELIVERYSLOT);
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));	
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			
			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(ICrisisManagerBatchDeliverySlot model : timeSlotList){				
				batchUpdater.update(new Object[]{ model.getDestStartTime()
												, model.getDestEndTime()
												, model.getTimeSlotId()
												, model.getBatchId()
												, model.getArea()
												, model.getStartTime()
												, model.getEndTime()
											});
			}
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslot(final String batchId, boolean filterException) throws SQLException {
		
		final Map<String, List<ICrisisManagerBatchDeliverySlot>> slotMapping = new HashMap<String, List<ICrisisManagerBatchDeliverySlot>>();
		
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_CRISISMNGBATCH_TIMESLOT);
		if(!filterException){
			updateQ.append(" and ts.new_windowstarttime is null and ts.new_windowendtime is null");
		}
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(updateQ.toString());
				ps.setString(1, batchId);				
				return ps;
			}  
		};
		
		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do {
						String _area = rs.getString("AREA");						
						if(!slotMapping.containsKey(_area)) {
							slotMapping.put(_area, new ArrayList<ICrisisManagerBatchDeliverySlot>());
						}
						ICrisisManagerBatchDeliverySlot model = new CrisisManagerBatchDeliverySlot();		
						model.setArea(_area);
						model.setStartTime(rs.getTimestamp("WINDOW_STARTTIME"));
						model.setEndTime(rs.getTimestamp("WINDOW_ENDTIME"));
						model.setDestStartTime(rs.getTimestamp("NEW_WINDOWSTARTTIME"));
						model.setDestEndTime(rs.getTimestamp("NEW_WINDOWENDTIME"));
						model.setTimeSlotId(rs.getString("TIMESLOT_ID"));
						slotMapping.get(_area).add(model);
					} while(rs.next());
				}
		});
		
		return slotMapping;
	}
	
	public void clearCrisisMngBatchDeliverySlot(String batchId) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_CRISISMNGBATCH_DELIVERYSLOT, new Object[] {batchId});
			
			connection = this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public List<IStandingOrderModel> getStandingOrderByCriteria(final Date deliveryDate, final Date cutOffDateTime
			, final String[] area, final String startTime, final String endTime, final String[] deliveryType, String profileName, boolean isSOIncluded) throws SQLException {
		
		final List<IStandingOrderModel> result = new ArrayList<IStandingOrderModel>();
		
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_STANDINGORDER_BYCRITERIA);
		
		orderSearchCriteria(cutOffDateTime, area, startTime, endTime,
				deliveryType, profileName, isSOIncluded, updateQ);
		
		Connection connection = null;
		try{
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
					PreparedStatement ps =
						connection.prepareStatement(updateQ.toString());
					ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
					int paramIndex = 2;
					if(cutOffDateTime != null) ps.setTimestamp(paramIndex++, new Timestamp(cutOffDateTime.getTime()));					
					if(startTime != null) ps.setString(paramIndex++, startTime);
					if(endTime != null) ps.setString(paramIndex++, endTime);
						
					return ps;
				}  
			};
			
			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do { 
								IStandingOrderModel soModel = new StandingOrderModel();	
								result.add(soModel);
								
								ICustomerModel custModel = new CustomerModel();								
								custModel.setErpCustomerPK(rs.getString("CUSTOMERID"));
								//custModel.setFdCustomerPK(rs.getString("FDCUSTOMERID"));
								custModel.setFirstName(rs.getString("FIRSTNAME"));
								custModel.setLastName(rs.getString("LASTNAME"));								
								
								soModel.setId(rs.getString("STANDINGORDER_ID"));
								soModel.setSaleId(rs.getString("WEBORDER_ID"));
								soModel.setLineItemCount(rs.getInt("SALE_LINEITEMCOUNT"));
								soModel.setTempLineItemCount(rs.getInt("TEMPLATE_LINEITEMCOUNT"));
								soModel.setArea(rs.getString("AREA"));
								soModel.setCustomerModel(custModel);
							
							} while(rs.next());		 
							
						}
					}
			);
			
			return result;
			
		} finally {
			if(connection!=null) connection.close();
		}		
	}

	private void orderSearchCriteria(final Date cutOffDateTime,
			final String[] area, final String startTime, final String endTime,
			final String[] deliveryType, String profileName,
			boolean isSOIncluded, final StringBuffer updateQ) {
		if(area != null && area.length > 0){
			updateQ.append(" and di.zone in (");
			for(int intCount = 0; intCount < area.length; intCount++ ) {
				updateQ.append("'").append(area[intCount]).append("'");				
				if(intCount < area.length-1) {
					updateQ.append(",");
				}
			}
			updateQ.append(")");
		}
		
		if(deliveryType != null && deliveryType.length > 0){
			updateQ.append(" and di.delivery_type in (");
			for(int intCount = 0; intCount < deliveryType.length; intCount++ ) {
				updateQ.append("'").append(deliveryType[intCount]).append("'");				
				if(intCount < deliveryType.length-1) {
					updateQ.append(",");
				}
			}
			updateQ.append(")");
		}
		
		if(cutOffDateTime != null){
			updateQ.append(" and to_char(di.cutofftime, 'HH:MI AM') = to_char(?, 'HH:MI AM')");
		}
		if(startTime != null){
			updateQ.append(" and to_date(to_char(di.starttime, 'HH:MI AM'), 'HH:MI AM') >= to_date(?, 'HH:MI AM')");
		}
		if(endTime != null){
			updateQ.append(" and to_date(to_char(di.endtime, 'HH:MI AM'), 'HH:MI AM') <= to_date(?, 'HH:MI AM')");
		}
		if(!isSOIncluded){
			updateQ.append(" and s.standingorder_id is null ");
		}
		if(EnumProfileList.CHEFSTABLE.getName().equals(profileName)){
			updateQ.append(" and exists (select 1 from cust.profile p, cust.fdcustomer fdc, cust.customer c "
							+" where p.customer_id=fdc.ID and c.id=fdc.erp_customer_id "
							+" and p.profile_name='ChefsTable' and p.profile_value='1' and c.id=ci.customer_id)");
		} else if(EnumProfileList.NONCHEFSTABLE.getName().equals(profileName)){
			updateQ.append(" and not exists (select 1 from cust.profile p, cust.fdcustomer fdc, cust.customer c "
							+" where p.customer_id=fdc.ID and c.id=fdc.erp_customer_id "
							+" and p.profile_name='ChefsTable' and p.profile_value='1' and c.id=ci.customer_id)");
		}
	}
	
	public void addNewCrisisMngBatchStandingOrder(List<IStandingOrderModel> batchSOs) throws SQLException {
		
		Connection connection = null;
		if(batchSOs != null && batchSOs.size() > 0) {
			try {			
				
				BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_CRISISMNGBATCH_STANDINGORDER);

				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.compile();
				
				connection = this.jdbcTemplate.getDataSource().getConnection();
				
				for(IStandingOrderModel _SOModel : batchSOs){
					batchUpdater.update(new Object[]{_SOModel.getBatchId()
										,_SOModel.getId()
										,_SOModel.getSaleId()
										,_SOModel.getLineItemCount()
										,_SOModel.getTempLineItemCount()
										,_SOModel.getArea()
										,_SOModel.getCustomerModel().getErpCustomerPK()
									});
					
				}
				batchUpdater.flush();		
			} finally {
				if(connection!=null) connection.close();
			}
		}
	}
	
	public void clearCrisisMngBatchStandingOrder(String orderCrisisBatchId) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_CRISISMNGBATCH_STANDINGORDER, new Object[] {orderCrisisBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public List<IStandingOrderModel> getStandingOrderByBatchId(final String batchId) throws SQLException {
		
		final List<IStandingOrderModel> result = new ArrayList<IStandingOrderModel>();
		
		Connection connection = null;
		try{
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
					PreparedStatement ps =
						connection.prepareStatement(GET_CRISISMNGBATCH_STANDINGORDER);
					ps.setString(1, batchId);									
					return ps;
				}  
			};
			
			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do { 
								IStandingOrderModel soModel = new StandingOrderModel();	
								result.add(soModel);
								
								ICustomerModel custModel = new CustomerModel();								
								custModel.setErpCustomerPK(rs.getString("CUSTOMER_ID"));
								
								soModel.setId(rs.getString("STANDINGORDER_ID"));
								soModel.setSaleId(rs.getString("SALE_ID"));
								soModel.setLineItemCount(rs.getInt("SALE_LINEITEMCOUNT"));
								soModel.setTempLineItemCount(rs.getInt("TEMPLATE_LINEITEMCOUNT"));
								soModel.setArea(rs.getString("AREA"));
								soModel.setErrorHeader(rs.getString("ERROR_HEADER"));
								soModel.setStatus(rs.getString("STATUS"));
								soModel.setCustomerModel(custModel);
							
							} while(rs.next());		 
							
						}
					}
			);
			
			return result;
			
		} finally {
			if(connection!=null) connection.close();
		}		
	}

	public void updateCrisisMngBatchStandingOrder(String batchId, List<IStandingOrderModel> batchStandingOrders) throws SQLException {
		
		Connection connection = null;
		if(batchStandingOrders != null && batchStandingOrders.size() > 0) {
			try {			
				
				BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), UPDATE_CRISISMNGBATCH_STANDINGORDEREXCEPTION);

				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.compile();
				
				connection = this.jdbcTemplate.getDataSource().getConnection();
				
				for(IStandingOrderModel _soEntry : batchStandingOrders){
					batchUpdater.update(new Object[]{_soEntry.getErrorHeader(),_soEntry.getStatus(), batchId, _soEntry.getId()});			
				}	
			
				batchUpdater.flush();		
			} finally {
				if(connection!=null) connection.close();
			}
		}
	}
	
	public List<IActiveOrderModel> getActiveOrderByArea(final Date deliveryDate) throws SQLException {
	
		final List<IActiveOrderModel> result = new ArrayList<IActiveOrderModel>();
		Connection connection = null;
		try{
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
					PreparedStatement ps =
						connection.prepareStatement(GET_ACTIVEORDERS_BYAREA);
					ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));		
					return ps;
				}  
			};
			
			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do { 
								IActiveOrderModel model = new ActiveOrderModel();	
								result.add(model);
																
								model.setArea(rs.getString("AREA"));
								model.setStartTime(rs.getTimestamp("STARTTIME"));
								model.setEndTime(rs.getTimestamp("ENDTIME"));
								model.setOrderCount(rs.getInt("ORDERCOUNT"));
							} while(rs.next());
						}
					}
			);
			
			return result;
			
		} finally {
			if(connection!=null) connection.close();
		}	
	}
	
	public void updateCrisisMngBatchOrderReservation(String batchId, Map<String, String> batchOrderReservations) throws SQLException {
		
		Connection connection = null;
		if(batchOrderReservations != null && batchOrderReservations.size() > 0) {
			try {			
				
				BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), UPDATE_CRISISMNGBATCH_ORDERRESERVATION);

				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.compile();
				
				connection = this.jdbcTemplate.getDataSource().getConnection();
				
				for(Map.Entry<String, String> _orEntry : batchOrderReservations.entrySet()){
					batchUpdater.update(new Object[]{_orEntry.getValue(), batchId, _orEntry.getKey()});
				}	
			
				batchUpdater.flush();		
			} finally {
				if(connection!=null) connection.close();
			}
		}
	}
	
	public List<ICancelOrderModel> getCancelOrderByArea(final String batchId) throws SQLException {
		
		final List<ICancelOrderModel> result = new ArrayList<ICancelOrderModel>();
		Connection connection = null;
		try{
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
					PreparedStatement ps =
						connection.prepareStatement(GET_CANCELORDER_BYAREA);
					ps.setString(1, batchId);		
					return ps;
				}  
			};
			
			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do { 
								ICancelOrderModel model = new CancelOrderModel();	
								result.add(model);
																
								model.setArea(rs.getString("AREA"));
								model.setStartTime(rs.getTimestamp("STARTTIME"));
								model.setEndTime(rs.getTimestamp("ENDTIME"));
								model.setOrderCount(rs.getInt("ORDERCOUNT"));
								model.setReservationCount(rs.getInt("RSVCOUNT"));
							} while(rs.next());
						}
					}
			);
			
			return result;
			
		} finally {
			if(connection!=null) connection.close();
		}	
	}
	
}
