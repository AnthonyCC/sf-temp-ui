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

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.ErpTruckInfo;
import com.freshdirect.customer.RedeliverySaleInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.fdstore.FDConfiguredProduct;
import com.freshdirect.fdstore.FDConfiguredProductFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
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

	private final static Category LOGGER = LoggerFactory.getInstance(ErpSaleInfoDAO.class);

	private static final String QUERY_ORDER_HISTORY =
		"select s.customer_id,s.id, s.dlv_pass_id,del.requested_date, s.status, del.payment_method_type, sa.action_date as create_date, sa.source as create_source, sa.initiator as create_by, "
		+ "del.action_date as mod_date, del.source as mod_source, del.initiator as mod_by, del.starttime, del.endtime, del.cutofftime, del.delivery_type, del.zone,  "
		+ "NVL(( select sum(c.amount) as amount from cust.complaint c where c.sale_id=s.id and c.status='APP'), 0) as credit_approved, "
		+ "NVL(( select sum(c.amount) as amount from cust.complaint c where c.sale_id=s.id and c.status='PEN'), 0) as credit_pending, "
		+ "( select max(sa.amount) from cust.salesaction sa where sale_id=s.id and sa.action_type in ('CRO','MOD','INV') "
		+ "and sa.action_date=(select max(action_date) from cust.salesaction where action_type in ('CRO','MOD','INV') and sale_id=s.id)) as amount " 
		+ "from cust.sale s, cust.salesaction sa, "
		+ "( select s.id, sa.requested_date, sa.action_date, sa.source, sa.initiator, di.starttime, di.endtime, di.cutofftime, di.delivery_type, di.zone, "
		+ "(select pi.payment_method_type from cust.paymentinfo pi where pi.salesaction_id = sa.id) as payment_method_type "
		+ "from cust.sale s, cust.sale_cro_mod_date sac, cust.salesaction sa, cust.deliveryinfo di "
		+ "where s.id = sac.sale_id and s.id=sa.sale_id and sa.id=di.salesaction_id and sa.action_type in ('CRO','MOD') " 
		+ "and sa.action_date=sac.max_date "
		+ "and s.customer_id=?) del "
		+ "where sale_id=s.id and sa.action_type = 'CRO' and s.customer_id=? " 
		+ "and s.id=del.id order by to_number(s.id) desc";

	public static Collection getOrderHistoryInfo(Connection conn, String erpCustomerId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(QUERY_ORDER_HISTORY);
		ps.setString(1, erpCustomerId);
		ps.setString(2, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		List extendedInfos = new ArrayList();
		while (rs.next()) {
			extendedInfos.add(
				new ErpSaleInfo(
					rs.getString("ID"),
					rs.getString("CUSTOMER_ID"),
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					rs.getDouble("AMOUNT"),
					rs.getDate("REQUESTED_DATE"),
					EnumTransactionSource.getTransactionSource(rs.getString("CREATE_SOURCE")),
					rs.getTimestamp("CREATE_DATE"),
					rs.getString("CREATE_BY"),
					EnumTransactionSource.getTransactionSource(rs.getString("MOD_SOURCE")),
					rs.getTimestamp("MOD_DATE"),
					rs.getString("MOD_BY"),
					rs.getTimestamp("STARTTIME"),
					rs.getTimestamp("ENDTIME"),
					rs.getTimestamp("CUTOFFTIME"),
					EnumDeliveryType.getDeliveryType(rs.getString("DELIVERY_TYPE")),
					rs.getDouble("CREDIT_PENDING"),
					rs.getDouble("CREDIT_APPROVED"),
					rs.getString("ZONE"),
					EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE")),
					rs.getString("DLV_PASS_ID")
					));
		}
		rs.close();
		ps.close();
		LOGGER.info("*****run get order history info query");
		return extendedInfos;
	}
	
	private static final String ORDERS_BY_DLV_PASS =
		"select s.customer_id,s.id, s.dlv_pass_id,del.requested_date, s.status, del.payment_method_type, sa.action_date as create_date, sa.source as create_source, sa.initiator as create_by, "
		+ "del.action_date as mod_date, del.source as mod_source, del.initiator as mod_by, del.starttime, del.endtime, del.cutofftime, del.delivery_type, del.zone, "
		+ "NVL(( select sum(c.amount) as amount from cust.complaint c where c.sale_id=s.id and c.status='APP'), 0) as credit_approved, "
		+ "NVL(( select sum(c.amount) as amount from cust.complaint c where c.sale_id=s.id and c.status='PEN'), 0) as credit_pending, "
		+ "( select sa.amount from cust.salesaction sa where sale_id=s.id and sa.action_type in ('CRO','MOD','INV') "
		+ "and sa.action_date=(select max(action_date) from cust.salesaction where action_type in ('CRO','MOD','INV') and sale_id=s.id)) as amount " 
		+ "from cust.sale s, cust.salesaction sa, "
		+ "( select s.id, sa.requested_date, sa.action_date, sa.source, sa.initiator, di.starttime, di.endtime, di.cutofftime, di.delivery_type, di.zone, "
		+ "(select pi.payment_method_type from cust.paymentinfo pi where pi.salesaction_id = sa.id) as payment_method_type "
		+ "from cust.sale s, cust.sale_cro_mod_date sac, cust.salesaction sa, cust.deliveryinfo di "
		+ "where s.id = sac.sale_id and s.id=sa.sale_id and sa.id=di.salesaction_id and sa.action_type in ('CRO','MOD') " 
		+ "and sa.action_date=sac.max_date "
		+ "and s.customer_id=?) del "
		+ "where sale_id=s.id and sa.action_type = 'CRO' and s.customer_id=? and s.dlv_pass_id =? and s.id=del.id order by to_number(s.id) desc";

	public static Collection getOrdersByDlvPassId(Connection conn, String erpCustomerId, String dlvPassId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(ORDERS_BY_DLV_PASS);
		ps.setString(1, erpCustomerId);
		ps.setString(2, erpCustomerId);
		ps.setString(3, dlvPassId);
		ResultSet rs = ps.executeQuery();
		List extendedInfos = new ArrayList();
		while (rs.next()) {
			extendedInfos.add(
				new ErpSaleInfo(
					rs.getString("ID"),
					rs.getString("CUSTOMER_ID"),
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					rs.getDouble("AMOUNT"),
					rs.getDate("REQUESTED_DATE"),
					EnumTransactionSource.getTransactionSource(rs.getString("CREATE_SOURCE")),
					rs.getTimestamp("CREATE_DATE"),
					rs.getString("CREATE_BY"),
					EnumTransactionSource.getTransactionSource(rs.getString("MOD_SOURCE")),
					rs.getTimestamp("MOD_DATE"),
					rs.getString("MOD_BY"),
					rs.getTimestamp("STARTTIME"),
					rs.getTimestamp("ENDTIME"),
					rs.getTimestamp("CUTOFFTIME"),
					EnumDeliveryType.getDeliveryType(rs.getString("DELIVERY_TYPE")),
					rs.getDouble("CREDIT_PENDING"),
					rs.getDouble("CREDIT_APPROVED"),
					rs.getString("ZONE"),
					EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE")),
					rs.getString("DLV_PASS_ID")
					));
		}
		rs.close();
		ps.close();
		return extendedInfos;
	}
	
	private static final String GET_ORDERS_USED_DLV_PASS ="select s.dlv_pass_id, s.id sale_id, sa.id saleaction_id, sa.requested_date, s.status "
			+ "from cust.sale s, cust.salesaction sa where s.id = sa.sale_id and sa.action_type in ('CRO','MOD') "
			+ "and s.status <> 'CAN' and sa.action_date=(select max(action_date) from cust.salesaction where action_type in ('CRO','MOD') and sale_id=s.id) "
			+ "and s.customer_id = ? and s.dlv_pass_id in (select id from cust.delivery_pass where customer_id = ?) order by s.dlv_pass_id";

	public static Collection getOrdersUsingDlvPass(Connection conn, String erpCustomerId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(GET_ORDERS_USED_DLV_PASS);
		ps.setString(1, erpCustomerId);
		ps.setString(2, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		List usageList = new ArrayList();
		while (rs.next()) {
			String note = null;
			usageList.add(
				new DlvPassUsageLine(
					rs.getString("DLV_PASS_ID"),
					rs.getString("SALE_ID"),
					rs.getDate("REQUESTED_DATE"),
					 EnumSaleStatus.getSaleStatus(rs.getString("STATUS"))));
		}
		rs.close();
		ps.close();
		return usageList;
	}
	
	//The following query returns the sale id along with the sale status of those orders that used a specific delivery pass
	//and are delivered and delivery date is within 7 days from today.
	
//	private static final String GET_RECENT_ORDERS_BY_DLV_PASS ="select s.id sale_id, s.status , sa.requested_date from cust.sale s, cust.salesaction sa "
//			+ "where s.id = sa.sale_id and s.status in ('PPG', 'STL') and sa.action_type in ('CRO','MOD','INV') and sa.action_date = "
//			+ "(select max(action_date) from cust.salesaction where action_type in ('CRO','MOD','INV') and sale_id=s.id) and SYSDATE >= sa.requested_date "
//			+ "and SYSDATE <= (sa.requested_date + ?) and s.customer_id = ? and s.dlv_pass_id = ?";
	
	//The following query returns the sale id along with the sale status of those orders that used a specific delivery pass
	//and are delivered or settled. 
	
	private static final String GET_RECENT_ORDERS_BY_DLV_PASS ="select s.id sale_id, s.status , sa.requested_date from cust.sale s, cust.salesaction sa "
		+ "where s.id = sa.sale_id and s.status in ('PPG', 'STL','ENR','CPG') and sa.action_type in ('CRO','MOD') and sa.action_date = "
		+ "(select max(action_date) from cust.salesaction where action_type in ('CRO','MOD') and sale_id=s.id) "
		+ "and s.customer_id = ? and s.dlv_pass_id = ?";

public static Collection getRecentOrdersByDlvPassId(Connection conn, String erpCustomerId, String dlvPassId, int noOfDaysOld) throws SQLException {
	PreparedStatement ps = conn.prepareStatement(GET_RECENT_ORDERS_BY_DLV_PASS);
	//ps.setInt(1, noOfDaysOld);
	ps.setString(1, erpCustomerId);
	ps.setString(2, dlvPassId);

	ResultSet rs = ps.executeQuery();
	List usageList = new ArrayList();
	while (rs.next()) {
		usageList.add(
			new DlvPassUsageLine(
				dlvPassId,
				rs.getString("SALE_ID"),
				rs.getDate("REQUESTED_DATE"),
				EnumSaleStatus.getSaleStatus(rs.getString("STATUS"))));
	}
	rs.close();
	ps.close();
	return usageList;
}
	private static final String TRUCK_NUMBER_QUERY =
		"select s.truck_number, sum(decode(s.status, 'ENR', 1, 0)) as has_enroute, count(*) as order_total "
			+ "from cust.salesaction sa, cust.sale s,  cust.deliveryinfo di "
			+ "where  sa.action_type in ('CRO', 'MOD')"
			+ "and sa.action_date = (select max(action_date) from cust.salesaction where action_type in ('CRO', 'MOD') and sale_id = sa.sale_id) "
			+ "and sa.requested_date = ? and s.id = sa.sale_id and sa.id=di.salesaction_id "
			+ "and s.status <> 'CAN' and di.starttime >= ? and di.starttime < ? "
			+ "and s.status in ('ENR', 'STL', 'PPG', 'CPG', 'REF', 'RET') "
			+ "group by s.truck_number "
			+ "order by s.truck_number ";

	public static List getTruckNumbersForDate(Connection conn, java.util.Date deliveryDate) throws SQLException {
		List ret = new ArrayList();
		Calendar dlvCal = DateUtil.truncate(DateUtil.toCalendar(deliveryDate));

		PreparedStatement ps = conn.prepareStatement(TRUCK_NUMBER_QUERY);
		//set requested date
		ps.setDate(1, new java.sql.Date(dlvCal.getTime().getTime()));

		ps.setTimestamp(2, new java.sql.Timestamp(dlvCal.getTime().getTime()));

		dlvCal.set(Calendar.HOUR_OF_DAY, 23);
		dlvCal.set(Calendar.MINUTE, 59);
		dlvCal.set(Calendar.SECOND, 59);

		ps.setTimestamp(3, new java.sql.Timestamp(dlvCal.getTime().getTime()));
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			ret.add(new ErpTruckInfo(rs.getString("TRUCK_NUMBER"), rs.getInt("HAS_ENROUTE"), rs.getInt("ORDER_TOTAL")));
		}

		rs.close();
		ps.close();
		return ret;
	}
	private static final String ORDER_BY_TRUCK_QUERY =
		"select s.id, s.customer_id,s.stop_sequence, s.status, di.first_name, di.last_name "
			+ "from  cust.salesaction sa ,cust.sale s, cust.deliveryinfo di "
			+ "where  sa.action_type in ('CRO', 'MOD') "
			+ "and sa.action_date = (select max(action_date) from cust.salesaction "
			+ " where action_type in ('CRO', 'MOD') and sale_id = sa.sale_id) "
			+ "and sa.requested_date = ? and sa.sale_id = s.id and s.status <> 'CAN' "
			+ " and sa.id=di.salesaction_id  "
			+ "and di.starttime >= ? and di.starttime < ? and truck_number = ? "
			+ " and s.status in ('ENR', 'STL', 'PPG', 'CPG', 'REF', 'RET') "
			+ "order by s.stop_sequence ";

	public static List getOrdersByTruckNumber(Connection conn, String truckNumber, java.util.Date deliveryDate)
		throws SQLException {
		List saleInfos = new ArrayList();

		PreparedStatement ps = conn.prepareStatement(ORDER_BY_TRUCK_QUERY);
		Calendar dlvCal = DateUtil.truncate(DateUtil.toCalendar(deliveryDate));
		
		//set requested date
		ps.setDate(1, new java.sql.Date(dlvCal.getTime().getTime()));
		
		ps.setTimestamp(2, new java.sql.Timestamp(dlvCal.getTime().getTime()));

		dlvCal.set(Calendar.HOUR_OF_DAY, 23);
		dlvCal.set(Calendar.MINUTE, 59);
		dlvCal.set(Calendar.SECOND, 59);

		ps.setTimestamp(3, new java.sql.Timestamp(dlvCal.getTime().getTime()));
		ps.setString(4, truckNumber);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			saleInfos.add(
				new DlvSaleInfo(
					rs.getString("ID"),
					rs.getString("CUSTOMER_ID"),
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					rs.getString("STOP_SEQUENCE"),
					rs.getString("FIRST_NAME"),
					rs.getString("LAST_NAME")));

		}

		rs.close();
		ps.close();

		return saleInfos;
	}

	private static final String dlvSaleInfoQuery =
		"select s.id,s.customer_id, s.stop_sequence, s.status, di.first_name, di.last_name from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ "where s.id = sa.sale_id and sa.action_type in ('CRO', 'MOD') "
			+ "and sa.action_date = (select max(action_date) from cust.salesaction sa1 where sa1.action_type in ('CRO', 'MOD') and sa1.sale_id = s.id) "
			+ "and sa.id = di.salesaction_id and s.id = ? ";

	public static DlvSaleInfo getDlvSaleInfo(Connection conn, String saleId) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(dlvSaleInfoQuery);
		ps.setString(1, saleId);
		ResultSet rs = ps.executeQuery();
		DlvSaleInfo dlvSaleInfo = null;
		while (rs.next()) {
			dlvSaleInfo =
				new DlvSaleInfo(
					rs.getString("ID"),
					rs.getString("CUSTOMER_ID"),
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					rs.getString("STOP_SEQUENCE"),
					rs.getString("FIRST_NAME"),
					rs.getString("LAST_NAME"));
		}
		rs.close();
		ps.close();

		return dlvSaleInfo;
	}

	private static final String ORD_SEARCH_QUERY =
		"select s.id,s.customer_id, s.stop_sequence, s.status, di.first_name, di.last_name, di.address1, di.apartment, di.zip "
			+ "from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ "where s.id=sa.sale_id and sa.id=di.salesaction_id and s.status<>'CAN' and sa.requested_date = ? "
			+ "and sa.action_type in ('CRO','MOD') and sa.action_date=(select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD')) "
			+ "and upper(di.address1) like upper(?) and di.zip = ? ";

	public static List getOrdersForDateAndAddress(Connection conn, Date date, String address, String zipcode) throws SQLException {
		List lst = new ArrayList();
		PreparedStatement ps = conn.prepareStatement(ORD_SEARCH_QUERY);
		ps.setDate(1, new java.sql.Date(date.getTime()));
		ps.setString(2, "%" + address);
		ps.setString(3, zipcode);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			DlvSaleInfo info =
				new DlvSaleInfo(
					rs.getString("ID"),
					rs.getString("CUSTOMER_ID"),
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					rs.getString("STOP_SEQUENCE"),
					rs.getString("FIRST_NAME"),
					rs.getString("LAST_NAME"));
			info.setAddress(rs.getString("ADDRESS1"));
			info.setApartment(rs.getString("APARTMENT"));
			info.setZipcode(rs.getString("ZIP"));

			lst.add(info);
		}

		return lst;
	}

	private static final String redeliveryQuery =
		"select old.customer_id,old.id, new.status, old.truck_number, old.stop_sequence, old.first_name || ' ' || old.last_name as name, "
			+ "old.address1 || ' ' || old.apartment as address, new.starttime, new.endtime "
			+ "from "
			+ "(select s.customer_id,s.id, s.truck_number, s.stop_sequence, di.first_name, di.last_name, di.address1, di.apartment "
			+ "from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ "where s.id=sa.sale_id and sa.id=di.salesaction_id  and sa.action_type in ('CRO','MOD') "
			+ "and sa.action_date=(select max(action_date) from cust.salesaction where action_type in ('CRO','MOD') and sale_id=s.id) "
			+ "and trunc(di.starttime) = trunc(?-1)) old, "
			+ "(select s.id, s.status, di.starttime, di.endtime "
			+ "from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ "where s.id=sa.sale_id and sa.id=di.salesaction_id and sa.action_type='RED' and s.status='RED' "
			+ "and sa.action_date=(select max(action_date) from cust.salesaction where action_type='RED' and sale_id=s.id) "
			+ "and trunc(di.starttime) = trunc(?)) new "
			+ "where new.id=old.id order by truck_number, stop_sequence ";

	public static List getRedeliveries(Connection conn, java.util.Date date) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(redeliveryQuery);
		ps.setDate(1, new java.sql.Date(date.getTime()));
		ps.setDate(2, new java.sql.Date(date.getTime()));
		ResultSet rs = ps.executeQuery();
		List saleInfos = new ArrayList();
		RedeliverySaleInfo saleInfo = null;
		while (rs.next()) {
			saleInfo =
				new RedeliverySaleInfo(
					rs.getString("ID"),
					rs.getString("CUSTOMER_ID"),
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					rs.getString("TRUCK_NUMBER"),
					rs.getString("STOP_SEQUENCE"),
					rs.getString("NAME"),
					rs.getString("ADDRESS"),
					rs.getTimestamp("STARTTIME"),
					rs.getTimestamp("ENDTIME"));
			saleInfos.add(saleInfo);
		}

		rs.close();
		ps.close();

		return saleInfos;
	}

	private static final String everyItemOrderedQuery =
		"select ol.sku_code, ol.sales_unit, quantity as quantity, ol.configuration, sa.action_date "
			+ "from cust.sale s, cust.salesaction sa, cust.orderline ol "
			+ "where s.id=sa.sale_id and sa.id=ol.salesaction_id and sa.action_type in ('CRO','MOD') "
			+ "and sa.action_date=(select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD')) "
			+ "and s.customer_id=? "
			+ "and NVL(ol.promotion_type,0)<>"
			+ EnumDiscountType.SAMPLE.getId()
			+ " and NVL(ol.delivery_grp, 0) = 0 ";

	public static List getEveryItemEverOrdered(Connection conn, String erpCustomerId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(everyItemOrderedQuery);
		ps.setString(1, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		List products = new ArrayList();
		FDConfiguredProductFactory factory = FDConfiguredProductFactory.getInstance();
		
		while (rs.next()) {
			try {
				FDConfiguredProduct prod = factory.getConfiguration(
					rs.getString("SKU_CODE"),
					rs.getDouble("QUANTITY"),
					rs.getString("SALES_UNIT"),
					ErpOrderLineUtil.convertStringToHashMap(rs.getString("CONFIGURATION")));
				
				products.add(prod);
			} catch (FDResourceException e) {
				LOGGER.warn("Unable to create a configured product", e);
			} catch (FDSkuNotFoundException e) {
				LOGGER.warn("Unable to create a configured product", e);
			}
		}
		rs.close();
		ps.close();

		return products;
	}
	
	// get order number for a customer except cancelled orders.
	private static final String validOrderCountQuery = "select count(*) from cust.sale where customer_id =? and status <>'CAN'";
	
	public static int getValidOrderCount(Connection conn, String erpCustomerId) throws SQLException {
		int orderCount =0;
		PreparedStatement ps = conn.prepareStatement(validOrderCountQuery);
		ps.setString(1, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			orderCount = rs.getInt(1);	
		}
		rs.close();
		ps.close();

		return orderCount;
	}
	
	// get last order's ID for a customer.
	private static final String lastOrderIDQuery = "select id from (select s.id from cust.sale s, cust.salesaction sa where s.customer_id =? and s.id= sa.sale_id and sa.action_type='CRO' order by action_date desc) where rownum =1";
	
	public static String getLastOrderID(Connection conn, String erpCustomerId) throws SQLException {
		String lastOrderID = null;
		PreparedStatement ps = conn.prepareStatement(lastOrderIDQuery);
		ps.setString(1, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			lastOrderID = rs.getString("ID");	
		}
		rs.close();
		ps.close();
		
		return lastOrderID;
	}
	
	// get order number for a customer during specific past time.
	private static final String orderCountPastQuery = "select count(*) from cust.sale s, cust.salesaction sa where s.customer_id =? and s.id= sa.sale_id and sa.action_type='CRO' and sa.action_date> ?";
	
	public static int getOrderCountPast(Connection conn, String erpCustomerId, Date day) throws SQLException {
		int orderCount =0;
		PreparedStatement ps = conn.prepareStatement(orderCountPastQuery);
		ps.setString(1, erpCustomerId);
		ps.setDate(2, new java.sql.Date(day.getTime()));
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			orderCount = rs.getInt(1);	
		}
		rs.close();
		ps.close();
		
		return orderCount;
	}
	
	// get lastest order amount for a customer's specific order.

	private static final String orderAmountQuery ="select sa.amount from cust.sale s, cust.salesaction sa where s.id = ? and s.customer_id = ? and s.id = sa.sale_id and sa.action_type in ('CRO', 'MOD', 'INV') and sa.action_date = (select max(action_date) from cust.salesaction where sale_id = s.id and action_type in ('CRO', 'MOD', 'INV'))";
	
	public static double getOrderAmount(Connection conn, String erpCustomerId, String sale_id) throws SQLException {
		double orderAmount =Double.MAX_VALUE;
		PreparedStatement ps = conn.prepareStatement(orderAmountQuery);
		ps.setString(1, sale_id);
		ps.setString(2, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			orderAmount = rs.getDouble(1);	
		}
		rs.close();
		ps.close();

		return orderAmount;
	}
	// Check if this order belongs to the user. This method has been added to replace the
	// existing logic which loops through the order history info to get the same information. 
	
	private static final String orderBelongsToUserQuery = "select count(*) from cust.sale where id = ? and customer_id = ?";
	
	public static boolean isOrderBelongsToUser(Connection conn, String erpCustomerId, String saleId) throws SQLException {
		int orderCount =0;
		PreparedStatement ps = conn.prepareStatement(orderBelongsToUserQuery);
		ps.setString(1, saleId);
		ps.setString(2, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			orderCount = rs.getInt(1);	
		}
		rs.close();
		ps.close();
		if(orderCount > 0)
			return true;
		else
			return false;
	}
	
	private static final String QUERY_WEB_ORDER_HISTORY =
		"select s.id, s.status, sa.action_date as mod_date, sa.requested_date, sa.action_type, sa.source as mod_source, "
		+ "(select action_date from cust.salesaction where sale_id = s.id and action_type = 'CRO') as create_date, "
		+ "(select source from cust.salesaction where sale_id = s.id and action_type = 'CRO') as create_source, "
		+ "di.delivery_type, di.zone, pi.payment_method_type "
		+ "from cust.sale s, cust.salesaction sa, " 
		+ "cust.deliveryinfo di, cust.paymentinfo pi "
		+ "where s.id = sa.sale_id and s.customer_id = ? " 
		+ "and sa.action_date = (select max_date from cust.sale_cro_mod_date sco where sco.sale_id = s.id) " 
		+ "and sa.action_type in ('CRO', 'MOD') "
		+ "and sa.id = di.salesaction_id and sa.id = pi.salesaction_id";
		
	public static Collection getWebOrderHistoryInfo(Connection conn, String erpCustomerId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(QUERY_WEB_ORDER_HISTORY);
		ps.setString(1, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		List extendedInfos = new ArrayList();
		while (rs.next()) {
			extendedInfos.add(
				new ErpSaleInfo(
					rs.getString("ID"),
					"",
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					0.0,
					rs.getDate("REQUESTED_DATE"),
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
					""
					));
		}
		rs.close();
		ps.close();
		LOGGER.info("*****run get WEB order history info query");
		return extendedInfos;
	}
}