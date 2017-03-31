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

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTruckInfo;
import com.freshdirect.customer.RedeliverySaleInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDConfiguredProduct;
import com.freshdirect.fdstore.FDConfiguredProductFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
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
	private static String QUERY_ORDER_HISTORY=" select cre.customer_id, cre.id, cre.truck_number, cre.stop_sequence, cre.standingorder_id, cre.so_holiday_movement, cre.dlv_pass_id,cre.type, "+
	"  del.requested_date, cre.requested_date as create_requested_date, cre.status, sac.amount,sac.sub_total,del.payment_method_type,del.referenced_order,  "+
	"  cre.action_date as create_date, cre.source as create_source, cre.initiator as create_by, del.action_date as mod_date,   "+
	"  del.source as mod_source, del.initiator as mod_by, del.starttime, del.endtime, del.cutofftime, del.delivery_type,   "+
	"  NVL((select /*+ use_nl(c s) */sum(c.amount) from cust.complaint c where c.status='APP' and c.sale_id=cre.id),0) as credit_approved,  "+
	"  NVL((select /*+ use_nl(c s) */sum(c.amount) from cust.complaint c where c.status='PEN' and c.sale_id=cre.id),0) as credit_pending, del.zone,NVL(E_STORE,'FreshDirect') E_STORE,  "+
	"  NVL(SALES_ORG,'1000') SALES_ORG, NVL(DISTRIBUTION_CHANNEL,'1000') DISTRIBUTION_CHANNEL, NVL(PLANT_ID,'1000') PLANT_ID, del.ewallet_id "+
	"  from  "+
	"    ( select  s.customer_id, s.id, s.status, s.dlv_pass_id,s.type, s.truck_number, s.stop_sequence, s.standingorder_id, s.so_holiday_movement, "+
	"     sa.action_date, sa.source, sa.initiator, sa.requested_date,s.E_STORE         from    "+
	"         cust.sale s, cust.salesaction sa          "+
	"             where  sale_id = s.id         and    sa.action_type = 'CRO'         and  "+
	"                        sa.customer_id = s.customer_id         and    s.customer_id =  ?  "+
	"     ) cre,       "+
	"      "+
	"     ( "+
	"      select  s.id, sa.amount,sa.sub_total         from   cust.sale s, cust.salesaction sa         where   "+
	"             sale_id = s.id         and    sa.action_type in ('CRO', 'MOD', 'INV')         and    "+
	"             sa.customer_id = s.customer_id         and    "+
	"             sa.action_date = (select max(action_date)   from   cust.salesaction  where  action_type in ('CRO', 'MOD','INV')  "+
	"                                              and  customer_id = s.customer_id   and    sale_id = s.id)  "+
	"             and    s.customer_id =  ? and rownum > 0"+
	"     ) sac,  "+
	"      "+
	"      "+
	"     ( "+
	"     select s.id, sa.requested_date, sa.action_date, sa.source, sa.initiator, di.starttime, di.endtime, di.cutofftime,  "+
	"      di.delivery_type, di.zone,pi.payment_method_type,pi.ewallet_id,pi.referenced_order,DI.SALES_ORG, DI.DISTRIBUTION_CHANNEL, DI.PLANT_ID  from  "+
	"       cust.sale s, cust.salesaction sa, cust.deliveryinfo di, cust.paymentinfo pi        where  "+
	"        pi.salesaction_id=sa.id and s.id = sa.sale_id  and    sa.id = di.salesaction_id        "+
	"        and    sa.action_type in ('CRO', 'MOD')         and    sa.customer_id = s.customer_id  "+
	"        and    sa.action_date = s.cromod_date         and    s.customer_id = ? "+
	"      ) del "+
	"          "+
	"         where  cre.id = sac.id(+) and     "+
	"         cre.id = del.id     "+
	"          order by del.requested_date desc";

	public static Collection<ErpSaleInfo> getOrderHistoryInfo(Connection conn, String erpCustomerId) throws SQLException {
		long startTime=System.currentTimeMillis();
		PreparedStatement ps =conn.prepareStatement(QUERY_ORDER_HISTORY);
			ps.setString(1, erpCustomerId);
			ps.setString(2, erpCustomerId);
			//Added As part of PERF-27 task.
			ps.setString(3, erpCustomerId);
		
		
		ResultSet rs = ps.executeQuery();
		List<ErpSaleInfo> extendedInfos = new ArrayList<ErpSaleInfo>();
		
		while (rs.next()) {
			
			String referencedOrder=rs.getString("REFERENCED_ORDER");
			extendedInfos.add(
				new ErpSaleInfo(
					rs.getString("ID"),
					rs.getString("CUSTOMER_ID"),
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					rs.getDouble("AMOUNT"),
					rs.getDouble("SUB_TOTAL"),
					rs.getDate("REQUESTED_DATE"),
					rs.getDate("CREATE_REQUESTED_DATE"),
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
					rs.getString("DLV_PASS_ID"),
					EnumSaleType.getSaleType(rs.getString("TYPE")),
					rs.getString("TRUCK_NUMBER"),
					rs.getString("STOP_SEQUENCE"),
				    (referencedOrder==null || "".equals(referencedOrder))?false:true,
					rs.getString("STANDINGORDER_ID"),
					"Y".equalsIgnoreCase(rs.getString("SO_HOLIDAY_MOVEMENT")),
					EnumEStoreId.valueOfContentId(rs.getString("E_STORE")),
					rs.getString("PLANT_ID"),
					rs.getString("SALES_ORG"),
					rs.getString("DISTRIBUTION_CHANNEL"),
					null !=rs.getString("EWALLET_ID")?EnumEwalletType.getEnum(Integer.parseInt(rs.getString("EWALLET_ID"))):null
				));
		}
		rs.close();
		ps.close();
		LOGGER.info(new StringBuilder("*****run get order history info query ").append(" for customer ").append(erpCustomerId).append(" completed in ").append(System.currentTimeMillis()-startTime).append(" milliseconds, and returned ").append(extendedInfos.size()).append(" records").toString());
		return extendedInfos;
	}
	
	private static final String ORDERS_BY_DLV_PASS =
		"select s.customer_id,s.id, s.dlv_pass_id,del.requested_date, s.status,s.type, s.truck_number, s.stop_sequence, s.standingorder_id, del.payment_method_type, sa.action_date as create_date, sa.source as create_source, sa.initiator as create_by, "
		+ "del.action_date as mod_date, del.source as mod_source, del.initiator as mod_by, del.starttime, del.endtime, del.cutofftime, del.delivery_type, del.zone, "
		+ "NVL(( select sum(c.amount) as amount from cust.complaint c where c.sale_id=s.id and c.status='APP'), 0) as credit_approved, "
		+ "NVL(( select sum(c.amount) as amount from cust.complaint c where c.sale_id=s.id and c.status='PEN'), 0) as credit_pending, "
		+ "( select sa.amount from cust.salesaction sa where sale_id=s.id and sa.action_type in ('CRO','MOD','INV') "
		+ "and sa.action_date=(select max(action_date) from cust.salesaction where action_type in ('CRO','MOD','INV') and sale_id=s.id)) as amount, " 
		+" NVL(E_STORE,'FreshDirect') E_STORE, NVL(SALES_ORG,'1000') SALES_ORG, NVL(DISTRIBUTION_CHANNEL,'1000') DISTRIBUTION_CHANNEL, NVL(PLANT_ID,'1000') PLANT_ID "
		+ "from cust.sale s, cust.salesaction sa, "
		+ "( select s.id, sa.requested_date, sa.action_date, sa.source, sa.initiator, di.starttime, di.endtime, di.cutofftime, di.delivery_type, di.zone,di.SALES_ORG,di.DISTRIBUTION_CHANNEL,di.PLANT_ID, "
		+ "(select pi.payment_method_type from cust.paymentinfo pi where pi.salesaction_id = sa.id) as payment_method_type "
		+ "from cust.sale s, cust.sale_cro_mod_date sac, cust.salesaction sa, cust.deliveryinfo di "
		+ "where s.id = sac.sale_id and s.id=sa.sale_id and s.type='REG' and sa.id=di.salesaction_id and sa.action_type in ('CRO','MOD') " 
		+ "and sa.action_date=sac.max_date "
		+ "and s.customer_id=?) del "
		+ "where sale_id=s.id and sa.action_type = 'CRO' and s.customer_id=? and s.dlv_pass_id =? and s.id=del.id order by to_number(s.id) desc";

	public static Collection<ErpSaleInfo> getOrdersByDlvPassId(Connection conn, String erpCustomerId, String dlvPassId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(ORDERS_BY_DLV_PASS);
		ps.setString(1, erpCustomerId);
		ps.setString(2, erpCustomerId);
		ps.setString(3, dlvPassId);
		ResultSet rs = ps.executeQuery();
		List<ErpSaleInfo> extendedInfos = new ArrayList<ErpSaleInfo>();
		while (rs.next()) {
			extendedInfos.add(
				new ErpSaleInfo(
					rs.getString("ID"),
					rs.getString("CUSTOMER_ID"),
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					rs.getDouble("AMOUNT"),0.0,
					rs.getDate("REQUESTED_DATE"),
					null,
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
					rs.getString("DLV_PASS_ID"),
					EnumSaleType.getSaleType(rs.getString("TYPE")),
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
		rs.close();
		ps.close();
		return extendedInfos;
	}
	
	private static final String GET_ORDERS_USED_DLV_PASS ="select s.dlv_pass_id, s.id sale_id, sa.id saleaction_id, sa.requested_date, s.status "
			+ "from cust.sale s, cust.salesaction sa where s.id = sa.sale_id and sa.action_type in ('CRO','MOD') and s.type='REG' "
			+ "and s.status <> 'CAN' and sa.action_date=(select max(action_date) from cust.salesaction where action_type in ('CRO','MOD') and sale_id=s.id) "
			+ "and s.customer_id = ? and s.dlv_pass_id in (select id from cust.delivery_pass where customer_id = ?) order by s.dlv_pass_id";

	public static Collection<DlvPassUsageLine> getOrdersUsingDlvPass(Connection conn, String erpCustomerId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(GET_ORDERS_USED_DLV_PASS);
		ps.setString(1, erpCustomerId);
		ps.setString(2, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		List<DlvPassUsageLine> usageList = new ArrayList<DlvPassUsageLine>();
		while (rs.next()) {
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
		+ "and s.customer_id = ? and s.type='REG' and s.dlv_pass_id = ?";

	public static List<DlvPassUsageLine> getRecentOrdersByDlvPassId(Connection conn, String erpCustomerId, String dlvPassId, int noOfDaysOld) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(GET_RECENT_ORDERS_BY_DLV_PASS);
		//ps.setInt(1, noOfDaysOld);
		ps.setString(1, erpCustomerId);
		ps.setString(2, dlvPassId);
	
		ResultSet rs = ps.executeQuery();
		List<DlvPassUsageLine> usageList = new ArrayList<DlvPassUsageLine>();
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
			+ "and sa.requested_date = ? and s.id = sa.sale_id and s.type='REG' and sa.id=di.salesaction_id "
			+ "and s.status <> 'CAN' and di.starttime >= ? and di.starttime < ? "
			+ "and s.status in ('ENR', 'STL', 'PPG', 'CPG', 'REF', 'RET') "
			+ "group by s.truck_number "
			+ "order by s.truck_number ";

	public static List<ErpTruckInfo> getTruckNumbersForDate(Connection conn, java.util.Date deliveryDate) throws SQLException {
		List<ErpTruckInfo> ret = new ArrayList<ErpTruckInfo>();
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
			+ "and sa.requested_date = ? and sa.sale_id = s.id and s.type='REG' and s.status <> 'CAN' "
			+ " and sa.id=di.salesaction_id  "
			+ "and di.starttime >= ? and di.starttime < ? and truck_number = ? "
			+ " and s.status in ('ENR', 'STL', 'PPG', 'CPG', 'REF', 'RET') "
			+ "order by s.stop_sequence ";

	public static List<DlvSaleInfo> getOrdersByTruckNumber(Connection conn, String truckNumber, java.util.Date deliveryDate)
		throws SQLException {
		List<DlvSaleInfo> saleInfos = new ArrayList<DlvSaleInfo>();

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
		"select s.id,s.customer_id, s.stop_sequence, s.status, di.first_name, di.last_name, " +
		"(SELECT 'X' FROM cust.paymentinfo pi, dlv.sale_signature sig where pi.salesaction_id = sa.id and sa.sale_id = sig.sale_id and pi.card_type = 'EBT') IS_EBT " +
		"from cust.sale s, cust.salesaction sa, cust.deliveryinfo di " +
		"where s.id = sa.sale_id and sa.action_type in ('CRO', 'MOD') " +
		"and sa.action_date = (select max(action_date) from cust.salesaction sa1 where sa1.action_type in ('CRO', 'MOD') and sa1.sale_id = s.id) " +
		"and sa.id = di.salesaction_id and s.id = ? and s.type='REG'";

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
					rs.getString("LAST_NAME"),
					"X".equalsIgnoreCase(rs.getString("IS_EBT")));
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
			+ "and upper(di.address1) like upper(?) and di.zip = ? and s.type='REG'";

	public static List<DlvSaleInfo> getOrdersForDateAndAddress(Connection conn, Date date, String address, String zipcode) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(ORD_SEARCH_QUERY);
		ps.setDate(1, new java.sql.Date(date.getTime()));
		ps.setString(2, "%" + address);
		ps.setString(3, zipcode);

		return collectDlvSaleInfo(ps);
	}

    private static List<DlvSaleInfo> collectDlvSaleInfo(PreparedStatement ps) throws SQLException {
        try {
            List<DlvSaleInfo> lst = new ArrayList<DlvSaleInfo>();
            ResultSet rs = ps.executeQuery();
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
            ps.close();
        }
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

	public static List<RedeliverySaleInfo> getRedeliveries(Connection conn, java.util.Date date) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(redeliveryQuery);
		ps.setDate(1, new java.sql.Date(date.getTime()));
		ps.setDate(2, new java.sql.Date(date.getTime()));
		ResultSet rs = ps.executeQuery();
		List<RedeliverySaleInfo> saleInfos = new ArrayList<RedeliverySaleInfo>();
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

	public static List<FDConfiguredProduct> getEveryItemEverOrdered(Connection conn, String erpCustomerId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(everyItemOrderedQuery);
		ps.setString(1, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		List<FDConfiguredProduct> products = new ArrayList<FDConfiguredProduct>();
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
	private static final String lastOrderIDQuery = "select id from (select s.id from cust.sale s, cust.salesaction sa where s.customer_id =? and s.id= sa.sale_id and s.type='REG' and sa.action_type='CRO' order by action_date desc) where rownum =1";
	
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
	private static final String orderCountPastQuery = "select count(*) from cust.sale s, cust.salesaction sa where s.customer_id =? and s.id= sa.sale_id and s.type=? and sa.action_type='CRO' and sa.action_date> ?";
	
	public static int getOrderCountPast(Connection conn, String erpCustomerId, Date day,EnumSaleType type) throws SQLException {
		int orderCount =0;
		PreparedStatement ps = conn.prepareStatement(orderCountPastQuery);
		ps.setString(1, erpCustomerId);
		ps.setString(2, type.getName());
		ps.setDate(3, new java.sql.Date(day.getTime()));
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
		PreparedStatement ps = conn.prepareStatement(QUERY_WEB_ORDER_HISTORY);
		ps.setString(1, erpCustomerId);
		ResultSet rs = ps.executeQuery();
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
		rs.close();
		ps.close();
		LOGGER.info("*****run get WEB order history info query");
		return extendedInfos;
	}
	
	private static final String QUERY_ORDER_HISTORY_FOR_CUST="select count(*) from cust.sale s  where s.customer_id =? and s.status ='STL'";
	
	public static int getPreviousOrderHistory(Connection conn, String erpCustomerId) throws SQLException{
		int orderCount =0;
		PreparedStatement ps = conn.prepareStatement(QUERY_ORDER_HISTORY_FOR_CUST);
		ps.setString(1, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			orderCount = rs.getInt(1);	
		}
		rs.close();
		ps.close();
		
		return orderCount;
	}
	
	private static final String QUERY_SETTLED_ORDER_HISTORY_FOR_CUST="select count(*) from cust.sale s  where s.customer_id =? and s.status='STL'";
	
	public static int getPreviousSettledOrderHistory(Connection conn, String erpCustomerId) throws SQLException{
		int orderCount =0;
		PreparedStatement ps = conn.prepareStatement(QUERY_SETTLED_ORDER_HISTORY_FOR_CUST);
		ps.setString(1, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			orderCount = rs.getInt(1);	
		}
		rs.close();
		ps.close();
		
		return orderCount;
	}
	private static final String QUERY_SAP_CUSTOMER_ID="select sap_id from cust.customer c  where c.id =?";
	
	public static String getSapCustomerId(Connection conn, String erpCustomerId) throws SQLException {
		String sapcustomerId ="";
		PreparedStatement ps = conn.prepareStatement(QUERY_SAP_CUSTOMER_ID);
		ps.setString(1, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			sapcustomerId = rs.getString("sap_id");	
		}
		rs.close();
		ps.close();
		
		return sapcustomerId;
	}
	
	private static final String LAST_ORD_SEARCH_QUERY =
		"select s.id,s.customer_id, s.stop_sequence, s.status, di.first_name, di.last_name, di.address1, di.apartment, di.zip "
			+ "from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ "where s.id=sa.sale_id and sa.id=di.salesaction_id and s.status<>'CAN' "
			+ "and sa.action_type in ('STL') and sa.action_date=(select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('STL')) "
			+ "and upper(di.address1) like upper(?) and upper(di.apartment) = UPPER(?) and di.zip = ? and s.type='REG'";

	public static List<DlvSaleInfo> getLastOrderForAddress(Connection conn, AddressModel address) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LAST_ORD_SEARCH_QUERY);
//		ps.setDate(1, new java.sql.Date(date.getTime()));
		ps.setString(1, "%" + address.getAddress1());
		ps.setString(2, address.getApartment());
		ps.setString(3, address.getZipCode());

		return collectDlvSaleInfo(ps);
	}
	
	private static final String GC_NSM_ORD_SEARCH_QUERY =
		"select s.id,s.customer_id,s.status,sa.action_date,NVL(E_STORE,'FreshDirect') E_STORE "
			+ "from cust.sale s, cust.salesaction sa "
			+ "where s.id=sa.sale_id and s.status = 'NEW' "
			+ "and s.type='GCD' and sa.action_type in ('AUT')";
	//NVL(E_STORE,'FreshDirect') E_STORE +" NVL(E_STORE,'FreshDirect') E_STORE, NVL(SALES_ORG,'1000') SALES_ORG, NVL(DISTRIBUTION_CHANNEL,'1000') DISTRIBUTION_CHANNEL, NVL(PLANT_ID,'1000') PLANT_ID "
	public static List<ErpSaleInfo> getNSMOrdersForGC(Connection conn) throws SQLException{
		List<ErpSaleInfo> list = new ArrayList<ErpSaleInfo>();
		PreparedStatement ps = conn.prepareStatement(GC_NSM_ORD_SEARCH_QUERY);
		ResultSet rs = ps.executeQuery();
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
	}
	
	// get last order's ID for a customer.
			private static final String lastOrderIDQueryByEStore = "select id from (select s.id from cust.sale s, cust.salesaction sa where s.customer_id =? and s.e_store=? and s.id= sa.sale_id and s.type='REG' and sa.action_type='CRO' order by action_date desc) where rownum =1";
			
			public static String getLastOrderID(Connection conn, String erpCustomerId, EnumEStoreId eStore) throws SQLException {
				String lastOrderID = null;
				PreparedStatement ps = conn.prepareStatement(lastOrderIDQueryByEStore);
				ps.setString(1, erpCustomerId);
				ps.setString(2, eStore.getContentId());
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					lastOrderID = rs.getString("ID");	
				}
				rs.close();
				ps.close();
				
				return lastOrderID;
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
             if(pst!=null){
            	 pst.close();
             }
		}
		return true;
	}

}
