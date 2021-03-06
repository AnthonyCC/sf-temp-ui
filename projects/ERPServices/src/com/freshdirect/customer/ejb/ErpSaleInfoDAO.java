/*
 * $Workfile:ErpSaleInfoDAO.java$
 *
 * $Date:6/3/2003 7:18:15 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTruckInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;

/**
 *
 *
 * @version $Revision:24$
 * @author $Author:Viktor Szathmary$
 */
public class ErpSaleInfoDAO {
//
	private final static Category LOGGER = LoggerFactory.getInstance(ErpSaleInfoDAO.class);
	
	//The following query returns the sale id along with the sale status of those orders that used a specific delivery pass
	//and are delivered and delivery date is within 7 days from today.
	
//	private static final String GET_RECENT_ORDERS_BY_DLV_PASS ="select s.id sale_id, s.status , sa.requested_date from cust.sale s, cust.salesaction sa "
//			+ "where s.id = sa.sale_id and s.status in ('PPG', 'STL') and sa.action_type in ('CRO','MOD','INV') and sa.action_date = "
//			+ "(select max(action_date) from cust.salesaction where action_type in ('CRO','MOD','INV') and sale_id=s.id) and SYSDATE >= sa.requested_date "
//			+ "and SYSDATE <= (sa.requested_date + ?) and s.customer_id = ? and s.dlv_pass_id = ?";
	
	//The following query returns the sale id along with the sale status of those orders that used a specific delivery pass
	//and are delivered or settled. 
	
	

	private static final String TRUCK_NUMBER_QUERY =
		"select s.truck_number, sum(decode(s.status, 'ENR', 1, 0)) as has_enroute, count(*) as order_total "
			+ "from cust.salesaction sa, cust.sale s,  cust.deliveryinfo di "
			+ "where  sa.action_type in ('CRO', 'MOD')"
			+ "and sa.action_date = (select max(action_date) from cust.salesaction where action_type in ('CRO', 'MOD') and sale_id = sa.sale_id) "
			+ "and sa.requested_date = ? and s.id = sa.sale_id and s.type='REG' and sa.id=di.salesaction_id "
			+ "and s.status <> 'CAN' and di.starttime >= ? and di.starttime < ? "
			+ "and s.status in ('ENR', 'STL', 'PPG', 'CPG', 'REF', 'RET') "
			+ "group by s.truck_number "
			+ "order by s.truck_number ";

	public static List<ErpTruckInfo> getTruckNumbersForDate(Connection conn, java.util.Date deliveryDate) throws SQLException {
		List<ErpTruckInfo> ret = new ArrayList<ErpTruckInfo>();
		Calendar dlvCal = DateUtil.truncate(DateUtil.toCalendar(deliveryDate));

        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {

			ps = conn.prepareStatement(TRUCK_NUMBER_QUERY);
			//set requested date
			ps.setDate(1, new java.sql.Date(dlvCal.getTime().getTime()));
			ps.setTimestamp(2, new java.sql.Timestamp(dlvCal.getTime().getTime()));
	
			dlvCal.set(Calendar.HOUR_OF_DAY, 23);
			dlvCal.set(Calendar.MINUTE, 59);
			dlvCal.set(Calendar.SECOND, 59);
	
			ps.setTimestamp(3, new java.sql.Timestamp(dlvCal.getTime().getTime()));
			rs = ps.executeQuery();
	
			while (rs.next()) {
				ret.add(new ErpTruckInfo(rs.getString("TRUCK_NUMBER"), rs.getInt("HAS_ENROUTE"), rs.getInt("ORDER_TOTAL")));
			}
	
			return ret;
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }
	}
	
	private static final String dlvSaleInfoQuery =
		"select s.id,s.customer_id, s.stop_sequence, s.status, di.first_name, di.last_name, " +
		"(SELECT 'X' FROM cust.paymentinfo pi, dlv.sale_signature sig where pi.salesaction_id = sa.id and sa.sale_id = sig.sale_id and pi.card_type = 'EBT') IS_EBT " +
		"from cust.sale s, cust.salesaction sa, cust.deliveryinfo di " +
		"where s.id = sa.sale_id and sa.action_type in ('CRO', 'MOD') " +
		"and sa.action_date = (select max(action_date) from cust.salesaction sa1 where sa1.action_type in ('CRO', 'MOD') and sa1.sale_id = s.id) " +
		"and sa.id = di.salesaction_id and s.id = ? and s.type='REG'";

	public static DlvSaleInfo getDlvSaleInfo(Connection conn, String saleId) throws SQLException {
        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {
			ps = conn.prepareStatement(dlvSaleInfoQuery);
			ps.setString(1, saleId);
			rs = ps.executeQuery();
			DlvSaleInfo dlvSaleInfo = null;
			while (rs.next()) {
				dlvSaleInfo =
					new DlvSaleInfo(
						rs.getString("ID"),
						rs.getString("CUSTOMER_ID"),
						EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
						rs.getString("STOP_SEQUENCE"),
						rs.getString("FIRST_NAME"),
						rs.getString("LAST_NAME"),
						"X".equalsIgnoreCase(rs.getString("IS_EBT")));
			}
	
			return dlvSaleInfo;
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }


	}

	private static final String ORD_SEARCH_QUERY =
		"select s.id,s.customer_id, s.stop_sequence, s.status, di.first_name, di.last_name, di.address1, di.apartment, di.zip "
			+ "from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ "where s.id=sa.sale_id and sa.id=di.salesaction_id and s.status<>'CAN' and sa.requested_date = ? "
			+ "and sa.action_type in ('CRO','MOD') and sa.action_date=(select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD')) "
			+ "and upper(di.address1) like upper(?) and di.zip = ? and s.type='REG'";

	public static List<DlvSaleInfo> getOrdersForDateAndAddress(Connection conn, Date date, String address, String zipcode) throws SQLException {
        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {	
			ps = conn.prepareStatement(ORD_SEARCH_QUERY);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ps.setString(2, "%" + address);
			ps.setString(3, zipcode);
	
			return collectDlvSaleInfo(ps);
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }
	}

    private static List<DlvSaleInfo> collectDlvSaleInfo(PreparedStatement ps) throws SQLException {
    	ResultSet rs = null;
    	try {
            List<DlvSaleInfo> lst = new ArrayList<DlvSaleInfo>();
            rs = ps.executeQuery();
            while (rs.next()) {
                DlvSaleInfo info = new DlvSaleInfo(rs.getString("ID"), rs.getString("CUSTOMER_ID"), EnumSaleStatus.getSaleStatus(rs.getString("STATUS")), rs
                        .getString("STOP_SEQUENCE"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"));
                info.setAddress(rs.getString("ADDRESS1"));
                info.setApartment(rs.getString("APARTMENT"));
                info.setZipcode(rs.getString("ZIP"));

                lst.add(info);
            }
            return lst;
        } finally {
            DaoUtil.closePreserveException(rs, null);
        }
    }

	// get order number for a customer except cancelled orders.
	private static final String validOrderCountQuery = "select count(*) from cust.sale where customer_id =? and status <>'CAN'";
	
	public static int getValidOrderCount(Connection conn, String erpCustomerId) throws SQLException {
		int orderCount =0;
        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {
			ps = conn.prepareStatement(validOrderCountQuery);
			ps.setString(1, erpCustomerId);
			rs = ps.executeQuery();
			while(rs.next()){
				orderCount = rs.getInt(1);	
			}
	
			return orderCount;
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }

	}
	
	// get last order's ID for a customer.
	private static final String lastOrderIDQuery = "select id from (select s.id from cust.sale s, cust.salesaction sa where s.customer_id =? and s.id= sa.sale_id and s.type='REG' and sa.action_type='CRO' order by action_date desc) where rownum =1";
	
	public static String getLastOrderID(Connection conn, String erpCustomerId) throws SQLException {
		String lastOrderID = null;
        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {
	
			ps = conn.prepareStatement(lastOrderIDQuery);
			ps.setString(1, erpCustomerId);
			rs = ps.executeQuery();
			while(rs.next()){
				lastOrderID = rs.getString("ID");	
			}
			
			return lastOrderID;
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }
	}
	
	// get order number for a customer during specific past time.
	private static final String orderCountPastQuery = "select count(*) from cust.sale s, cust.salesaction sa where s.customer_id =? and s.id= sa.sale_id and s.type=? and sa.action_type='CRO' and sa.action_date> ?";
	
	public static int getOrderCountPast(Connection conn, String erpCustomerId, Date day, EnumSaleType type)
			throws SQLException {
		int orderCount = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(orderCountPastQuery);
			ps.setString(1, erpCustomerId);
			ps.setString(2, type.getSaleType());
			if(null !=type && EnumSaleType.GIFTCARD.equals(type)){
				ps.setTimestamp(3, new java.sql.Timestamp(day.getTime()));
			}else{
				ps.setDate(3, new java.sql.Date(day.getTime()));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				orderCount = rs.getInt(1);
			}
		} finally {
			DaoUtil.close(rs);
			DaoUtil.close(ps);
		}
		return orderCount;
	}
	
	// get lastest order amount for a customer's specific order.

	private static final String orderAmountQuery ="select sa.amount from cust.sale s, cust.salesaction sa where s.id = ? and s.customer_id = ? and s.id = sa.sale_id and sa.action_type in ('CRO', 'MOD', 'INV') and sa.action_date = (select max(action_date) from cust.salesaction where sale_id = s.id and action_type in ('CRO', 'MOD', 'INV'))";
	
	public static double getOrderAmount(Connection conn, String erpCustomerId, String sale_id) throws SQLException {
		double orderAmount =Double.MAX_VALUE;
        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {

			ps = conn.prepareStatement(orderAmountQuery);
			ps.setString(1, sale_id);
			ps.setString(2, erpCustomerId);
			rs = ps.executeQuery();
			while(rs.next()){
				orderAmount = rs.getDouble(1);	
			}
	
			return orderAmount;
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }
	}
	
	private static final String QUERY_WEB_ORDER_HISTORY = 
		"select	s.id, s.status, s.truck_number, s.stop_sequence, s.standingorder_id as standingorder_id, sa.action_date as mod_date, sa.requested_date, sa.action_type, sa.source as mod_source,"
		+ "		(select action_date from cust.salesaction where sale_id = s.id and customer_id=s.customer_id and action_type = 'CRO') as create_date, "
		+ "		(select source from cust.salesaction where sale_id = s.id and customer_id=s.customer_id and action_type = 'CRO') as create_source,"
		+ "		di.delivery_type, di.zone, pi.payment_method_type "
		+" NVL(E_STORE,'FreshDirect') E_STORE, NVL(SALES_ORG,'1000') SALES_ORG, NVL(DISTRIBUTION_CHANNEL,'1000') DISTRIBUTION_CHANNEL, NVL(PLANT_ID,'1000') PLANT_ID "
		+ "	from cust.sale s, cust.salesaction sa, cust.deliveryinfo di, cust.paymentinfo pi "
		+ "	where s.id = sa.sale_id"
		+ "		and s.customer_id=sa.customer_id"
		+ "		and s.customer_id = ?"
		+ "		and sa.action_date = s.cromod_date"
		+ "		and sa.action_type in ('CRO', 'MOD') "
		+ "		and sa.id = di.salesaction_id and sa.id = pi.salesaction_id";
		

	public static Collection<ErpSaleInfo> getWebOrderHistoryInfo(Connection conn, String erpCustomerId) throws SQLException {
        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {
			ps = conn.prepareStatement(QUERY_WEB_ORDER_HISTORY);
			ps.setString(1, erpCustomerId);
			rs = ps.executeQuery();
			List<ErpSaleInfo> extendedInfos = new ArrayList<ErpSaleInfo>();
			while (rs.next()) {
				extendedInfos.add(
					new ErpSaleInfo(
						rs.getString("ID"),
						"",
						EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
						0.0,0.0,
						rs.getDate("REQUESTED_DATE"),
						null,
						EnumTransactionSource.getTransactionSource(rs.getString("CREATE_SOURCE")),
						rs.getTimestamp("CREATE_DATE"),
						"",
						EnumTransactionSource.getTransactionSource(rs.getString("MOD_SOURCE")),
						rs.getTimestamp("MOD_DATE"),
						"",
						null,
						null,
						null,
						EnumDeliveryType.getDeliveryType(rs.getString("DELIVERY_TYPE")),
						0.0,
						0.0,
						rs.getString("ZONE"),
						EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE")),
						"",
						EnumSaleType.REGULAR,
						rs.getString("TRUCK_NUMBER"),
						rs.getString("STOP_SEQUENCE"),
						false,
						rs.getString("STANDINGORDER_ID"),
						false,
						EnumEStoreId.valueOfContentId(rs.getString("E_STORE")),
						rs.getString("PLANT_ID"),
						rs.getString("SALES_ORG"),
						rs.getString("DISTRIBUTION_CHANNEL"),
						null
						));
			}
			LOGGER.info("*****run get WEB order history info query");
			return extendedInfos;
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }

	}
	
	private static final String QUERY_ORDER_HISTORY_FOR_CUST="select count(*) from cust.sale s  where s.customer_id =? and s.status ='STL'";
	
	public static int getPreviousOrderHistory(Connection conn, String erpCustomerId) throws SQLException{
		int orderCount =0;
        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {
			ps = conn.prepareStatement(QUERY_ORDER_HISTORY_FOR_CUST);
			ps.setString(1, erpCustomerId);
			rs = ps.executeQuery();
			while(rs.next()){
				orderCount = rs.getInt(1);	
			}
			
			return orderCount;
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }

	}
	
	private static final String QUERY_SETTLED_ORDER_HISTORY_FOR_CUST="select count(*) from cust.sale s  where s.customer_id =? and s.status='STL'";
	
	public static int getPreviousSettledOrderHistory(Connection conn, String erpCustomerId) throws SQLException{
		int orderCount =0;
        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {
			ps = conn.prepareStatement(QUERY_SETTLED_ORDER_HISTORY_FOR_CUST);
			ps.setString(1, erpCustomerId);
			rs = ps.executeQuery();
			while(rs.next()){
				orderCount = rs.getInt(1);	
			}
			
			return orderCount;
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }

	}
	
	private static final String QUERY_SAP_CUSTOMER_ID="select sap_id from cust.customer c  where c.id =?";
	
	public static String getSapCustomerId(Connection conn, String erpCustomerId) throws SQLException {
		String sapcustomerId ="";
        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {
	
			ps = conn.prepareStatement(QUERY_SAP_CUSTOMER_ID);
			ps.setString(1, erpCustomerId);
			rs = ps.executeQuery();
			while(rs.next()){
				sapcustomerId = rs.getString("sap_id");	
			}
			
			return sapcustomerId;
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }
	}
	
	private static final String GC_NSM_ORD_SEARCH_QUERY =
		"select s.id,s.customer_id,s.status,sa.action_date,NVL(E_STORE,'FreshDirect') E_STORE "
			+ "from cust.sale s, cust.salesaction sa "
			+ "where s.id=sa.sale_id and s.status = 'NEW' "
			+ "and s.type='GCD' and sa.action_type in ('AUT')";
	//NVL(E_STORE,'FreshDirect') E_STORE +" NVL(E_STORE,'FreshDirect') E_STORE, NVL(SALES_ORG,'1000') SALES_ORG, NVL(DISTRIBUTION_CHANNEL,'1000') DISTRIBUTION_CHANNEL, NVL(PLANT_ID,'1000') PLANT_ID "
	public static List<ErpSaleInfo> getNSMOrdersForGC(Connection conn) throws SQLException{
		List<ErpSaleInfo> list = new ArrayList<ErpSaleInfo>();
        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {
			ps = conn.prepareStatement(GC_NSM_ORD_SEARCH_QUERY);
			rs = ps.executeQuery();
			while (rs.next()) {
			//	Date actionDate = rs.getTimestamp("action_date").;			
				Long currentTime = System.currentTimeMillis()-(FDStoreProperties.getNSMAuthSkipSecsForGC()*1000);
				Date currentDate = new Date(currentTime);
				if(null != rs.getTimestamp("action_date") && rs.getTimestamp("action_date").before(currentDate)){
					ErpSaleInfo erpSaleInfo = new ErpSaleInfo(
							rs.getString("ID"),
							rs.getString("CUSTOMER_ID"),
							EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
							0.0,
							0.0,
							null,
							null,
							null,
							null,
							"",
							null,
							null,
							"",
							null,
							null,
							null,
							null,
							0.0,
							0.0,
							"",
							null,
							"",
							EnumSaleType.GIFTCARD,
							"",
							"",
							false,
							null,
							false,
							EnumEStoreId.valueOfContentId(rs.getString("E_STORE")),
							"1000",
							"1000",
							"1000",
							null);
							list.add(erpSaleInfo);
				}
			}
			return list;
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }

	}
	
	// get last order's ID for a customer.
	private static final String lastOrderIDQueryByEStore = "select id from (select s.id from cust.sale s, cust.salesaction sa where s.customer_id =? and s.e_store=? and s.id= sa.sale_id and s.type='REG' and sa.action_type='CRO' order by action_date desc) where rownum =1";
	
	public static String getLastOrderID(Connection conn, String erpCustomerId, EnumEStoreId eStore) throws SQLException {
		String lastOrderID = null;
        PreparedStatement ps = null; 
        ResultSet rs = null;

        try {

			ps = conn.prepareStatement(lastOrderIDQueryByEStore);
			ps.setString(1, erpCustomerId);
			ps.setString(2, eStore.getContentId());
			rs = ps.executeQuery();
			while(rs.next()){
				lastOrderID = rs.getString("ID");	
			}
			
			return lastOrderID;
        } finally {
            DaoUtil.closePreserveException(rs,ps);
        }
	}

	private static final String UPDATE_SALE_SHIIPING_DETAIL = " UPDATE CUST.SALE SA  SET SA.TRUCK_NUMBER=?, SA.STOP_SEQUENCE=? ,SA.NUM_REGULAR_CARTONS=?"
			+ ",SA.NUM_FREEZER_CARTONS=?,NUM_ALCOHOL_CARTONS=? WHERE SA.ID=?";

	public static boolean updateSalesShippingInfo(Connection conn,
			Map<String, ErpShippingInfo> erpShippingMap) throws SQLException {
		PreparedStatement pst = null;
		ErpShippingInfo shippingInfo = null;
		try {
			pst = conn.prepareStatement(UPDATE_SALE_SHIIPING_DETAIL);

			for (Map.Entry<String, ErpShippingInfo> shipping : erpShippingMap.entrySet()) {
				
				shippingInfo = shipping.getValue();
				
				if(null!=shippingInfo.getTruckNumber()){
					pst.setString(1, shippingInfo.getTruckNumber());
				}else{
					pst.setNull(1,java.sql.Types.NULL);

				}if(null!=shippingInfo.getStopSequence()){
					pst.setString(2, shippingInfo.getStopSequence());
				}else{
					pst.setNull(2,java.sql.Types.NULL);

				}
				pst.setInt(3,
						shippingInfo.getRegularCartons() > 0 ? shippingInfo.getRegularCartons() : 0);
				pst.setInt(4,
						shippingInfo.getFreezerCartons() > 0 ? shippingInfo.getFreezerCartons() : 0);
				pst.setInt(5,
						shippingInfo.getAlcoholCartons() > 0 ? shippingInfo.getAlcoholCartons() : 0);
				pst.setString(6, shipping.getKey());
				
				pst.addBatch();

			}
			if(!erpShippingMap.isEmpty()){
				 pst.executeBatch();
			}
			
		} finally {
			DaoUtil.closePreserveException(null, pst);
		}
		return true;
	}
	
}
