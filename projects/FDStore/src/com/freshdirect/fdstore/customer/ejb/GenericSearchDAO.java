package com.freshdirect.fdstore.customer.ejb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.fdstore.customer.FDBrokenAccountInfo;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDCustomerReservationInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.EnumSearchType;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.SettlementBatchInfo;

public class GenericSearchDAO {
	
	//private static Map queryMap = new HashMap();
	//.put(EnumSearchType.COMPANY_SEARCH.getName(), CUSTOMER_QUERY);
	private static Category LOGGER = LoggerFactory.getInstance(GenericSearchDAO.class);
	
	public static List genericSearch(Connection conn, GenericSearchCriteria criteria) throws SQLException {
		List searchResults = null;
		if(criteria == null && criteria.isBlank()){
			return Collections.EMPTY_LIST;
		} else if(EnumSearchType.COMPANY_SEARCH.equals(criteria.getSearchType())){
			CriteriaBuilder builder = buildSQLFromCriteria(criteria);
			searchResults = findCustomersByCriteria(conn, criteria, builder);
		} else if(EnumSearchType.EXEC_SUMMARY_SEARCH.equals(criteria.getSearchType())){
			searchResults = orderSummaryByDate(conn, criteria);
		} else if(EnumSearchType.RESERVATION_SEARCH.equals(criteria.getSearchType())){
			CriteriaBuilder builder = buildSQLFromCriteria(criteria);
			//Fetch uncommitted pre reservations based on given criteria.
			searchResults= findReservationsByCriteria(conn, criteria, builder);
		} else if(EnumSearchType.ORDERS_BY_RESV_SEARCH.equals(criteria.getSearchType())){			
			//Fetch the orders that has standard and pre reservations based on the given criteria.
			CriteriaBuilder builder = buildOrderSearchForResv(criteria);
			searchResults = findOrderForResvByCriteria(conn,criteria, builder);
		}else if(EnumSearchType.BROKEN_ACCOUNT_SEARCH.equals(criteria.getSearchType())){
			searchResults = findBrokenAccounts(conn);
		}else if(EnumSearchType.CANCEL_ORDER_SEARCH.equals(criteria.getSearchType())){
			CriteriaBuilder builder = buildOrderSearchForResv(criteria);
			//Fetch the orders for cancellations based on the given criteria.
			searchResults = findOrderForResvByCriteria(conn,criteria, builder);
		}else if(EnumSearchType.RETURN_ORDER_SEARCH.equals(criteria.getSearchType())){
			CriteriaBuilder builder = buildSQLFromCriteria(criteria);
			searchResults = findOrderForReturnsByCriteria(conn, criteria, builder);
		}else if(EnumSearchType.SETTLEMENT_BATCH_SEARCH.equals(criteria.getSearchType())){
			searchResults = findFailedSettlementBatch(conn);
		}
		else if(EnumSearchType.DEL_RESTRICTION_SEARCH.equals(criteria.getSearchType())){
			CriteriaBuilder builder = buildSQLFromCriteria(criteria);
			searchResults = processDeliveryRestriction(conn, criteria, builder);
		}
		else if(EnumSearchType.ADDR_RESTRICTION_SEARCH.equals(criteria.getSearchType())){
			CriteriaBuilder builder = buildSQLFromCriteria(criteria);
			searchResults = processAddressRestriction(conn, criteria, builder);
		}
		return searchResults;
		
	}

	private static CriteriaBuilder buildSQLFromCriteria(GenericSearchCriteria criteria) {
		CriteriaBuilder builder = new CriteriaBuilder();
		if(criteria.getCriteriaMap()!=null && !criteria.getCriteriaMap().isEmpty()){
			if(EnumSearchType.COMPANY_SEARCH.equals(criteria.getSearchType())){
				builder.addSql(" lower(a.company_name) = ?", 
						new Object[] { 
							criteria.getCriteriaMap().get("companyName").toString() });
			}
			else if(EnumSearchType.RESERVATION_SEARCH.equals(criteria.getSearchType())){
				buildReservationSearch(criteria, builder);
			} else if(EnumSearchType.RETURN_ORDER_SEARCH.equals(criteria.getSearchType())){
				buildOrderSearchForReturns(criteria, builder);
			}
			else if(EnumSearchType.DEL_RESTRICTION_SEARCH.equals(criteria.getSearchType())){
				buildDeliveryRestrictionDays(criteria, builder);
			}
			else if(EnumSearchType.ADDR_RESTRICTION_SEARCH.equals(criteria.getSearchType())){
				buildAddressRestrictionCriteria(criteria, builder);
			}
		}
		return builder;
	}

	private static void buildReservationSearch(GenericSearchCriteria criteria, CriteriaBuilder builder) {
		java.util.Date baseDate = (java.util.Date) criteria.getCriteriaMap().get("baseDate");
		builder.addSql("ts.base_date = ?", 
					new Object[] {new Date(baseDate.getTime())});
		Object cutoffTime = criteria.getCriteriaMap().get("cutoffTime");
		if(cutoffTime != null){
			builder.addSql("to_date(to_char(ts.cutoff_time, 'HH:MI AM'),'HH:MI AM') = to_date(?,'HH:MI AM')", 
					new Object[] { 
					cutoffTime.toString() });
		}
		Object zoneArray = criteria.getCriteriaMap().get("zoneArray");
		if(zoneArray != null){
			builder.addInString("ze.zone_code", (String[])zoneArray);	
		}
		Object startTime = criteria.getCriteriaMap().get("startTime");
		if(startTime != null){
			builder.addSql("to_date(to_char(ts.start_time, 'HH:MI AM'),'HH:MI AM') >= to_date(?,'HH:MI AM')", 
					new Object[] {startTime.toString() });
		}
		Object endTime = criteria.getCriteriaMap().get("endTime");
		if(endTime != null){
			builder.addSql("to_date(to_char(ts.end_time, 'HH:MI AM'),'HH:MI AM') <= to_date(?,'HH:MI AM')", 
					new Object[] {endTime.toString() });
		}
	}

	private static CriteriaBuilder buildOrderSearchForResv(GenericSearchCriteria criteria) {
		CriteriaBuilder builder = new CriteriaBuilder();
		java.util.Date baseDate = (java.util.Date) criteria.getCriteriaMap().get("baseDate");
		builder.addSql("sa.requested_date = ?", 
					new Object[] {new Date(baseDate.getTime())});
		Object cutoffTime = criteria.getCriteriaMap().get("cutoffTime");
		if(cutoffTime != null){
			builder.addSql("to_date(to_char(di.cutofftime, 'HH:MI AM'),'HH:MI AM') = to_date(?,'HH:MI AM')", 
					new Object[] { 
					cutoffTime.toString() });
		}
		Object zoneArray = criteria.getCriteriaMap().get("zoneArray");
		if(zoneArray != null){
			builder.addInString("di.zone", (String[])zoneArray);	
		}
		Object startTime = criteria.getCriteriaMap().get("startTime");
		if(startTime != null){
			builder.addSql("to_date(to_char(di.starttime, 'HH:MI AM'),'HH:MI AM') >= to_date(?,'HH:MI AM')", 
					new Object[] {startTime.toString() });
		}
		Object endTime = criteria.getCriteriaMap().get("endTime");
		if(endTime != null){
			builder.addSql("to_date(to_char(di.endtime, 'HH:MI AM'),'HH:MI AM') <= to_date(?,'HH:MI AM')", 
					new Object[] {endTime.toString() });
		}
		return builder;
	}
	
	private static CriteriaBuilder buildOrderSearchForReturns(GenericSearchCriteria criteria, CriteriaBuilder builder) {
		java.util.Date baseDate = (java.util.Date) criteria.getCriteriaMap().get("baseDate");
		builder.addSql("sa.requested_date = ?", 
					new Object[] {new Date(baseDate.getTime())});
		Object zoneArray = criteria.getCriteriaMap().get("zoneArray");
		if(zoneArray != null){
			builder.addInString("di.zone", (String[])zoneArray);	
		}
		Object startTime = criteria.getCriteriaMap().get("startTime");
		if(startTime != null){
			
			builder.addSql("to_date(to_char(di.starttime, 'HH:MI AM'),'HH:MI AM') >= to_date(?,'HH:MI AM')", 
					new Object[] {startTime.toString() });
		}
		Object endTime = criteria.getCriteriaMap().get("endTime");
		if(endTime != null){
			builder.addSql("to_date(to_char(di.endtime, 'HH:MI AM'),'HH:MI AM') <= to_date(?,'HH:MI AM')", 
					new Object[] {endTime.toString() });
		}
		Object fromWaveNum = criteria.getCriteriaMap().get("fromWaveNumber");
		Object toWaveNum = criteria.getCriteriaMap().get("toWaveNumber");
		if(fromWaveNum != null && toWaveNum != null){
			//Searching for a range of wave numbers.
			builder.addSql("s.wave_number between to_number(?) and to_number(?)", 
					new Object[] {fromWaveNum.toString(), toWaveNum.toString()});
		}else if(fromWaveNum != null && toWaveNum == null){
			//Searching for a specific wave number.
			builder.addSql("s.wave_number = to_number(?)", 
					new Object[] {fromWaveNum.toString()});

		}
		Object fromTruckNum = criteria.getCriteriaMap().get("fromTruckNumber");
		Object toTruckNum = criteria.getCriteriaMap().get("toTruckNumber");
		if(fromTruckNum != null && toTruckNum != null){
			//Searching for a range of truck numbers.
			builder.addSql("s.truck_number between to_number(?) and to_number(?)", 
					new Object[] {fromTruckNum.toString(), toTruckNum.toString()});
		}else if(fromTruckNum != null && toTruckNum == null){
			//Searching for a specific truck number.
			builder.addSql("s.truck_number = to_number(?)", 
					new Object[] {fromTruckNum.toString()});

		}
		return builder;
	}
	
	private static String CUSTOMER_COMP_QUERY = 
		"select "
		+ " c.id, ci.first_name, ci.last_name, c.user_id, ci.home_phone, ci.business_phone, ci.cell_phone, " 
		+ " (select p.profile_value from cust.profile p where p.customer_id = fc.id and p.profile_name='VIPCustomer') VIP_CUST, " 
		+ " (select p.profile_value from cust.profile p where p.customer_id = fc.id and p.profile_name='ChefsTable') CHEFS_TABLE "
		+ " from cust.customer c, cust.customerinfo ci, cust.fdcustomer fc, cust.address a "  
		+ " where c.id = ci.customer_id and ci.customer_id = fc.erp_customer_id and " 
		+ " fc.erp_customer_id = a.customer_id "; 
	
	public static List findCustomersByCriteria(Connection conn, GenericSearchCriteria criteria, CriteriaBuilder builder) throws SQLException {
		String query = CUSTOMER_COMP_QUERY + " and " + builder.getCriteria();
		PreparedStatement ps = conn.prepareStatement(query);
		Object[] obj = builder.getParams();
		for(int i = 0; i < obj.length; i++) {
			ps.setObject(i+1, obj[i]);
		}
		ResultSet rs = ps.executeQuery();
		List lst = processCustomerResultSet(rs);
		rs.close();
		ps.close();
		return lst;
	}

	private static List processCustomerResultSet(ResultSet rs) throws SQLException {
		List lst = new ArrayList();
		while (rs.next()) {
			FDCustomerOrderInfo oInfo = new FDCustomerOrderInfo();
			oInfo.setIdentity(new FDIdentity(rs.getString("ID")));
			oInfo.setFirstName(rs.getString("FIRST_NAME"));
			oInfo.setLastName(rs.getString("LAST_NAME"));
			oInfo.setEmail(rs.getString("USER_ID"));
			
			oInfo.setPhone(new PhoneNumber(rs.getString("HOME_PHONE")).getPhone());
			String bizPhone = new PhoneNumber(rs.getString("BUSINESS_PHONE")).getPhone();
			String cellPhone = new PhoneNumber(rs.getString("CELL_PHONE")).getPhone();
			
			oInfo.setAltPhone(NVL.apply(cellPhone, ""));
			if("".equals(cellPhone)) {
				oInfo.setAltPhone(NVL.apply(bizPhone, ""));
			}
			
			oInfo.setVip("true".equals(rs.getString("VIP_CUST")));
			oInfo.setChefsTable("1".equals(rs.getString("CHEFS_TABLE")));
						
			lst.add(oInfo);
		}
		return lst;
	}

	
	private static String EXEC_SUMMARY_QUERY = 
		"select "
		+ " sales, total_promotions, total_orders, avg_order_size, avg_promotion, "
		+ " promotion_count,(promotion_count/total_orders) as promotion_percentage " 
		+ " from ( select "
		+ " status, sum(amount) as sales, sum(promotion_amt) as total_promotions, count(*) as total_orders, " 
		+ " sum(amount)/count(*) as avg_order_size, sum(promotion_amt)/count(*) as Avg_promotion, "
		+ " (select count(*) from cust.sale s, cust.salesaction sa, cust.deliveryinfo di, cust.discountline dl "
		+ " where s.id = sa.sale_id and sa.id = dl.salesaction_id and di.salesaction_id = dl.salesaction_id and " 
		+ " sa.action_date = (select /*USE_NL (salesaction) */ max(action_date) from cust.salesaction where action_type in ('CRO', 'MOD') and sale_id = sa.sale_id) and " 
		+ " sa.requested_date = ? and "
		+ " di.starttime >= ? and di.endtime < ? and "
		+ " s.status not in ('NSM','AUF','CAN') "
		+ " ) as promotion_count from (  " 
		+ " select /*+ INDEX (s pk_sale) INDEX (sa pk_salesaction) USE_NL (s sa) */ "
		+ " 'OK' as status,sa.action_date, sa.amount, (select sum(PROMOTION_AMT) from cust.discountline dl where dl.salesaction_id = sa.id) as promotion_amt " 
		+ " from cust.sale s, cust.salesaction sa, cust.deliveryinfo di " 
		+ " where s.id = sa.sale_id and sa.id = di.salesaction_id and " 
		+ " sa.action_date = (select /*+ USE_NL (salesaction) */ max(action_date) from cust.salesaction where action_type in ('CRO', 'MOD') and sale_id = sa.sale_id) and " 
		+ " sa.requested_date = ? AND "
		+ " di.starttime >= ? AND di.endtime < ? and s.status not in ('NSM','AUF','CAN') "
		+ " ) group by status )";
	
	public static List orderSummaryByDate(Connection conn, GenericSearchCriteria criteria) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(EXEC_SUMMARY_QUERY);
		Date startDate = new Date(((java.util.Date)criteria.getCriteriaMap().get("summaryDate")).getTime());
		Date endDate = new Date(DateUtil.addDays(startDate, 1).getTime());

		ps.setDate(1, startDate);
		ps.setDate(2, startDate);
		ps.setDate(3, endDate);
		ps.setDate(4, startDate);
		ps.setDate(5, startDate);
		ps.setDate(6, endDate);
		
		ResultSet rs = ps.executeQuery();
		List lst = processOrderSummaryResultSet(rs);
		rs.close();
		ps.close();
		return lst;
		
	}
	
	public static List processOrderSummaryResultSet(ResultSet rs) throws SQLException {
		List lst =  new ArrayList();
		while(rs.next()) {
			Map m = new HashMap();
			m.put("sales", new Double(rs.getDouble("sales")));
			m.put("Total Orders", new Integer(rs.getInt("TOTAL_ORDERS")));
			m.put("Average Order Size", new Double(rs.getDouble("AVG_ORDER_SIZE")));
			m.put("Total Promotions", new Double(rs.getDouble("TOTAL_PROMOTIONS")));
			m.put("Average Promotion", new Double(rs.getDouble("AVG_PROMOTION")));
			m.put("Promotion Count", new Integer(rs.getInt("PROMOTION_COUNT")));
			m.put("Promotion Percentage", new Double(rs.getDouble("PROMOTION_PERCENTAGE")));
			lst.add(m);
			
		}
		return lst;
	}
	
	private static String RESERVATION_SEARCH_QUERY = 
			"SELECT "
			+ "ci.customer_id, ci.first_name, ci.last_name, c.user_id, ci.home_phone, ci.business_phone, "
			+ "ci.cell_phone, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ze.zone_code, rs.id  "
			+ "from dlv.reservation rs, dlv.timeslot ts, dlv.zone ze, cust.customerinfo ci, cust.customer c "
			+ "where ts.id = rs.timeslot_id and ze.id = ts.zone_id and rs.customer_id = c.id and ci.customer_id = c.id and rs.status_code = 5 "
			+ "and rs.type in ('WRR','OTR')";
	
	public static List findReservationsByCriteria(Connection conn, GenericSearchCriteria criteria, CriteriaBuilder builder) throws SQLException {
		String query = RESERVATION_SEARCH_QUERY + " and " + builder.getCriteria();
		PreparedStatement ps = conn.prepareStatement(query);
		Object[] obj = builder.getParams();
		for(int i = 0; i < obj.length; i++) {
			ps.setObject(i+1, obj[i]);
		}
		ResultSet rs = ps.executeQuery();
		List lst = processReservationResultSet(rs);
		rs.close();
		ps.close();
		return lst;

	}
	
	private static List processReservationResultSet(ResultSet rs) throws SQLException {
		List lst = new ArrayList();
		while (rs.next()) {
			String id = rs.getString("ID");
			Date baseDate = rs.getDate("BASE_DATE");
			java.util.Date cutoffTime = rs.getTimestamp("CUTOFF_TIME");
			String firstName = rs.getString("FIRST_NAME");
			String lastName = rs.getString("LAST_NAME");
			FDIdentity identity  = new FDIdentity(rs.getString("CUSTOMER_ID"));
			String email = rs.getString("USER_ID");
			String phone =  new PhoneNumber(rs.getString("HOME_PHONE")).getPhone();
			String cellPhone = new PhoneNumber(rs.getString("CELL_PHONE")).getPhone();
			String altPhone = NVL.apply(cellPhone, "");
			String bizPhone = NVL.apply(new PhoneNumber(rs.getString("BUSINESS_PHONE")).getPhone(), "");
			if("".equals(cellPhone)) {
				altPhone = NVL.apply(bizPhone, "");
			}

			java.util.Date startTime = rs.getTimestamp("START_TIME");
			java.util.Date endTime = rs.getTimestamp("END_TIME");
			String zone = rs.getString("ZONE_CODE");
			
			FDCustomerReservationInfo rInfo = new FDCustomerReservationInfo(id, 
																			baseDate, 
																			cutoffTime, 
																			firstName, 
																			lastName, 
																			identity,
																			email,
																			phone,
																			altPhone,
																			bizPhone,
																			startTime, 
																			endTime,
																			zone);
			lst.add(rInfo);
		}
		return lst;
	}
	
	private static String ORDER_SEARCH_FOR_RESERVATION = 
		"SELECT "
		+ "c.id customer_id, fdc.id fdc_id, ci.first_name, ci.last_name, c.user_id, ci.home_phone, ci.business_phone, "
		+ "ci.cell_phone, s.id, sa.requested_date, s.status, sa.amount, di.starttime, di.endtime, "
		+ "di.cutofftime, rs.type "
		+ "from cust.customer c, cust.fdcustomer fdc, cust.customerinfo ci, cust.sale s, cust.salesaction sa, cust.deliveryinfo di, dlv.reservation rs "
		+ "where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id and s.id = sa.sale_id and sa.action_type IN ('CRO', 'MOD') "
		+ "and s.status in('SUB','AVE','AUT','AUF') and sa.action_date = "
		+ "(SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')) "
		+ "and sa.id = di.salesaction_id and rs.id = di.reservation_id";
	
	
	public static List findOrderForResvByCriteria(Connection conn, GenericSearchCriteria criteria, CriteriaBuilder builder) throws SQLException {
		String query = ORDER_SEARCH_FOR_RESERVATION + " and " + builder.getCriteria();
		PreparedStatement ps = conn.prepareStatement(query);
		Object[] obj = builder.getParams();
		for(int i = 0; i < obj.length; i++) {
			ps.setObject(i+1, obj[i]);
		}
		ResultSet rs = ps.executeQuery();
		List lst = processOrderForResvResultSet(rs);
		rs.close();
		ps.close();
		return lst;

	}
	
	private static List processOrderForResvResultSet(ResultSet rs) throws SQLException {
		List lst = new ArrayList();
		while (rs.next()) {
			FDCustomerOrderInfo oInfo = new FDCustomerOrderInfo();
			oInfo.setIdentity(new FDIdentity(rs.getString("CUSTOMER_ID"), rs.getString("FDC_ID")));
			oInfo.setFirstName(rs.getString("FIRST_NAME"));
			oInfo.setLastName(rs.getString("LAST_NAME"));
			oInfo.setEmail(rs.getString("USER_ID"));
			
			oInfo.setPhone(new PhoneNumber(rs.getString("HOME_PHONE")).getPhone());
			String bizPhone = new PhoneNumber(rs.getString("BUSINESS_PHONE")).getPhone();
			String cellPhone = new PhoneNumber(rs.getString("CELL_PHONE")).getPhone();
			
			oInfo.setAltPhone(NVL.apply(cellPhone, ""));
			if("".equals(cellPhone)) {
				oInfo.setAltPhone(NVL.apply(bizPhone, ""));
			}
			oInfo.setSaleId(rs.getString("ID"));
			oInfo.setDeliveryDate(rs.getDate("REQUESTED_DATE"));
			oInfo.setOrderStatus(EnumSaleStatus.getSaleStatus(rs.getString("STATUS")));
			oInfo.setAmount(rs.getDouble("AMOUNT"));
			oInfo.setStartTime(rs.getTimestamp("STARTTIME"));
			oInfo.setEndTime(rs.getTimestamp("ENDTIME"));
			oInfo.setCutoffTime(rs.getTimestamp("CUTOFFTIME"));
			oInfo.setRsvType(EnumReservationType.getEnum(rs.getString("TYPE")));
			
			lst.add(oInfo);
		}
		return lst;
	}
	private static final String BROKEN_ACCOUNT_QUERY = "select "
			+ "user_id, c.id as erp_id, fdc.fdcustid as fd_id, a.zip, fdc.depot_code "
			+ "from cust.customer c, cust.customerinfo ci, cust.address a, "
			+ "(select id as fdcustid, erp_customer_id, depot_code "
			+ "from cust.fdcustomer where id not in "
			+ "(select fdcustomer_id from cust.fduser where fdcustomer_id is not null)) fdc "
			+ "where c.id=fdc.erp_customer_id and c.id=ci.customer_id and c.id=a.customer_id";
	
	
	public static List findBrokenAccounts(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(BROKEN_ACCOUNT_QUERY);
		ResultSet rs = ps.executeQuery();
		List lst = processBrokenAccountResultSet(rs);
		rs.close();
		ps.close();
		return lst;

	}	

	private static List processBrokenAccountResultSet(ResultSet rs) throws SQLException {
		List lst = new ArrayList();
		while (rs.next()) {
			String userId = rs.getString("USER_ID");
			String customerId = rs.getString("ERP_ID");
			String fdCustomerId = rs.getString("FD_ID");
			String zipCode = rs.getString("ZIP");
			String depotCode = rs.getString("DEPOT_CODE");
			FDBrokenAccountInfo baInfo = new FDBrokenAccountInfo(userId, customerId, fdCustomerId, zipCode, depotCode);
			lst.add(baInfo);
		}
		return lst;
	}
	
	private static String ORDER_SEARCH_FOR_RETURNS = 
		"SELECT "
		+ "c.id customer_id, fdc.id fdc_id, ci.first_name, ci.last_name, c.user_id, ci.home_phone, ci.business_phone, "
		+ "ci.cell_phone, decode(ci.email_plain_text, 'X', 'TEXT', 'HTML') email_type, s.id, sa.requested_date, s.status, sa.amount, di.starttime, di.endtime, s.wave_number, s.truck_number "
		+ "from cust.customer c, cust.fdcustomer fdc, cust.customerinfo ci, cust.sale s, cust.salesaction sa, cust.deliveryinfo di, dlv.reservation rs "
		+ "where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id and s.id = sa.sale_id and sa.action_type IN ('CRO', 'MOD') "
		+ "and s.status = 'REF' and sa.action_date = "
		+ "(SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')) "
		+ "and sa.id = di.salesaction_id and rs.id = di.reservation_id";
	
	public static List findOrderForReturnsByCriteria(Connection conn, GenericSearchCriteria criteria, CriteriaBuilder builder) throws SQLException {
		String query = ORDER_SEARCH_FOR_RETURNS + " and " + builder.getCriteria();
		PreparedStatement ps = conn.prepareStatement(query);
		Object[] obj = builder.getParams();
		for(int i = 0; i < obj.length; i++) {
			ps.setObject(i+1, obj[i]);
		}
		ResultSet rs = ps.executeQuery();
		List lst = processOrderForReturnsResultSet(rs);
		rs.close();
		ps.close();
		return lst;
	}
	
	private static List processOrderForReturnsResultSet(ResultSet rs) throws SQLException {
		List lst = new ArrayList();
		while (rs.next()) {
			FDCustomerOrderInfo oInfo = new FDCustomerOrderInfo();
			oInfo.setIdentity(new FDIdentity(rs.getString("CUSTOMER_ID"), rs.getString("FDC_ID")));
			oInfo.setFirstName(rs.getString("FIRST_NAME"));
			oInfo.setLastName(rs.getString("LAST_NAME"));
			oInfo.setEmail(rs.getString("USER_ID"));
			oInfo.setEmailType(rs.getString("EMAIL_TYPE"));
			oInfo.setPhone(new PhoneNumber(rs.getString("HOME_PHONE")).getPhone());
			String bizPhone = new PhoneNumber(rs.getString("BUSINESS_PHONE")).getPhone();
			String cellPhone = new PhoneNumber(rs.getString("CELL_PHONE")).getPhone();
			
			oInfo.setAltPhone(NVL.apply(cellPhone, ""));
			if("".equals(cellPhone)) {
				oInfo.setAltPhone(NVL.apply(bizPhone, ""));
			}
			oInfo.setSaleId(rs.getString("ID"));
			oInfo.setDeliveryDate(rs.getDate("REQUESTED_DATE"));
			oInfo.setOrderStatus(EnumSaleStatus.getSaleStatus(rs.getString("STATUS")));
			oInfo.setAmount(rs.getDouble("AMOUNT"));
			oInfo.setStartTime(rs.getTimestamp("STARTTIME"));
			oInfo.setEndTime(rs.getTimestamp("ENDTIME"));
			oInfo.setWaveNum(rs.getString("WAVE_NUMBER"));
			oInfo.setRouteNum(rs.getString("TRUCK_NUMBER"));
			
			lst.add(oInfo);
		}
		return lst;
	}

	private static CriteriaBuilder buildAddressRestrictionCriteria(GenericSearchCriteria criteria, CriteriaBuilder builder) {
		
		String address1 = (String)criteria.getCriteriaMap().get("address1");
		if(address1 != null  && address1.trim().length()>0){
			builder.addSql(" scrubbed_address like ? ", new Object[]{"%"+address1+"%"});	
		}
		
		String apartment = (String)criteria.getCriteriaMap().get("apartment");
		if(apartment != null && apartment.trim().length()>0){
			builder.addSql(" apartment like ? ", new Object[]{"%"+apartment+"%"});	
		}

		String zipCode = (String)criteria.getCriteriaMap().get("zipCode");
		if(zipCode != null && zipCode.trim().length()>0){
			builder.addSql(" zipcode like ? ", new Object[]{"%"+zipCode+"%"});	
		}

		
		EnumRestrictedAddressReason reason = (EnumRestrictedAddressReason)criteria.getCriteriaMap().get("reason");
		if(reason!=null && !"N".equalsIgnoreCase(reason.getCode())){
			builder.addSql(" reason = ? ", new String[]{reason.getCode()});
		}
		
		
		//builder.addOrderBy(sortColumn,true);
		
		return builder;
	}

	
	private static CriteriaBuilder buildDeliveryRestrictionDays(GenericSearchCriteria criteria, CriteriaBuilder builder) {
		java.util.Date startDate = (java.util.Date) criteria.getCriteriaMap().get("startDate");
		if(startDate!=null){
		  builder.addSql("start_time > ?", 
			 		new Date(startDate.getTime()));
		}
		Object message = criteria.getCriteriaMap().get("message");
		if(message != null){
			builder.addSql(" message like ? ", new Object[]{"%"+message+"%"});	
		}
		
		EnumDlvRestrictionReason reason = (EnumDlvRestrictionReason)criteria.getCriteriaMap().get("reason");
		if(reason!=null && !"All".equalsIgnoreCase(reason.getName())){
			builder.addSql(" reason = ? ", new String[]{reason.getName()});
		}
		EnumDlvRestrictionType type = (EnumDlvRestrictionType)criteria.getCriteriaMap().get("type");
		if(type!=null){
			builder.addSql(" type = ?", new String[]{type.getName()});
		}
		
		return builder;
	}
	
	private static String DELIVERY_RESTRICTIONS_RETURN = 
		"select ID,TYPE,NAME,DAY_OF_WEEK,START_TIME,END_TIME,REASON,MESSAGE,CRITERION FROM dlv.restricted_days";

	private static List processDeliveryRestriction(Connection conn, GenericSearchCriteria criteria, CriteriaBuilder builder) throws SQLException {
		List restrictions=new ArrayList();		
		
		String query ="";
		String sortColumn = (String)criteria.getCriteriaMap().get("sortColumn");
		String ascending = (String)criteria.getCriteriaMap().get("ascending");
		
		if(sortColumn == null  || sortColumn.trim().length()==0){
			sortColumn="start_time";			
		}
		if(ascending == null  || ascending.trim().length()==0){
			ascending="asc";			
		}
		
		if(builder.getParams()!=null && builder.getParams().length>0){
		    query = new StringBuffer(DELIVERY_RESTRICTIONS_RETURN).append(" where ").append(builder.getCriteria()).append(" order by ").append(sortColumn).append(" "+ascending).toString();
		}else{
			query = new StringBuffer(DELIVERY_RESTRICTIONS_RETURN).append(builder.getCriteria()).append(" order by ").append(sortColumn).append(" "+ascending).toString();
		}
		
		
		LOGGER.debug("query :"+query);
		PreparedStatement ps = conn.prepareStatement(query);
		
		Object[] obj = builder.getParams();
		for(int i = 0; i < obj.length; i++) {
			LOGGER.debug("i:"+i+":"+obj[i]);
			LOGGER.debug(obj[i].getClass().getName());
			ps.setObject(i+1, obj[i]);
		}
		
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			String id = rs.getString("ID");
			String name = rs.getString("NAME");
			String msg = rs.getString("MESSAGE");
			EnumDlvRestrictionCriterion criterion = EnumDlvRestrictionCriterion.getEnum(rs.getString("CRITERION"));
			if (criterion == null) {
				// skip unknown criteria
				continue;
			}

			EnumDlvRestrictionReason reason = EnumDlvRestrictionReason.getEnum(rs.getString("REASON"));
			if (reason == null) {
				// skip unknown reasons
				continue;
			}

			java.util.Date startDate = new java.util.Date(rs.getTimestamp("START_TIME").getTime());
			java.util.Date endDate = new java.util.Date(rs.getTimestamp("END_TIME").getTime());
			int dayOfWeek = rs.getInt("DAY_OF_WEEK");

			String typeCode = rs.getString("TYPE");
			EnumDlvRestrictionType type = EnumDlvRestrictionType.getEnum(typeCode);
			if (type == null && "PTR".equals(typeCode)) {
				type = EnumDlvRestrictionType.RECURRING_RESTRICTION;
			}

			if (EnumDlvRestrictionType.ONE_TIME_RESTRICTION.equals(type)) {

				endDate = DateUtil.roundUp(endDate);

				// FIXME one-time reverse restrictions should have a different EnumDlvRestrictionType 
				if (reason.isSpecialHoliday()) {
					restrictions.add(new OneTimeReverseRestriction(id,criterion, reason, name, msg, startDate, endDate));
				} else {
					restrictions.add(new OneTimeRestriction(id,criterion, reason, name, msg, startDate, endDate));
				}

			} else if (EnumDlvRestrictionType.RECURRING_RESTRICTION.equals(type)) {

				TimeOfDay startTime = new TimeOfDay(startDate);
				TimeOfDay endTime = new TimeOfDay(endDate);
				// round up 11:59 to next midnight
				if (JUST_BEFORE_MIDNIGHT.equals(endTime)) {
					endTime = TimeOfDay.NEXT_MIDNIGHT;
				}
				restrictions.add(new RecurringRestriction(id,criterion, reason, name, msg, dayOfWeek, startTime, endTime));

			} else {
				// ignore	
			}



		}

		LOGGER.debug("restrictions size :"+restrictions.size());
		rs.close();
		ps.close();

		return restrictions;
	}
	
	
	private static String ADDRESS_RESTRICTIONS_RETURN = 
		"select scrubbed_address, apartment, zipcode, reason, date_modified, modified_by from dlv.restricted_address ";

	
	private static List processAddressRestriction(Connection conn, GenericSearchCriteria criteria, CriteriaBuilder builder) throws SQLException {
		List restrictions=new ArrayList();
		String query="";
		
		String sortColumn = (String)criteria.getCriteriaMap().get("sortColumn");
		String ascending = (String)criteria.getCriteriaMap().get("ascending");
		
		if(sortColumn == null  || sortColumn.trim().length()==0){
			sortColumn="date_modified";			
		}
		if(ascending == null  || ascending.trim().length()==0){
			ascending="asc";			
		}
		
		if(builder.getParams()!=null && builder.getParams().length>0){
		    query = new StringBuffer(ADDRESS_RESTRICTIONS_RETURN).append(" where ").append(builder.getCriteria()).append(" order by ").append(sortColumn).append(" "+ascending).toString();
		}else{
			query = new StringBuffer(ADDRESS_RESTRICTIONS_RETURN).append(" order by ").append(sortColumn).append(" "+ascending).toString() ;
		}
		
		LOGGER.debug("query2 :"+query);
		PreparedStatement ps = conn.prepareStatement(query);
		
		Object[] obj = builder.getParams();
		for(int i = 0; i < obj.length; i++) {
			LOGGER.debug("i:"+i+":"+obj[i]);
			LOGGER.debug(obj[i].getClass().getName());
			ps.setObject(i+1, obj[i]);
		}
		
		RestrictedAddressModel restriction=null;
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			restriction= new  RestrictedAddressModel();
			restriction.setAddress1(rs.getString("scrubbed_address"));
			restriction.setApartment(rs.getString("apartment"));
			restriction.setZipCode(rs.getString("zipCode"));
			EnumRestrictedAddressReason reason = EnumRestrictedAddressReason.getRestrictionReason(rs.getString("reason"));
			restriction.setReason(reason);	
			java.util.Date dateModified = new java.util.Date(rs.getTimestamp("date_modified").getTime());			
			restriction.setLastModified(dateModified);
			restriction.setModifiedBy(rs.getString("modified_by"));
			
			restrictions.add(restriction);
		
		}

		LOGGER.debug("address restrictions size :"+restrictions.size());
		rs.close();
		ps.close();

		return restrictions;
	}

	
	private final static TimeOfDay JUST_BEFORE_MIDNIGHT = new TimeOfDay("11:59 PM");

	private static final String SETTLEMENT_BATCH_QUERY = "select merchant_id, batch_id, settle_date_time, batch_status, " +
			"batch_response_msg, processor_batch_id, submission_id, sales_transactions,sales_amount, return_transactions, return_amount " +
			"from paylinx.cc_settlement where batch_status<>'00'";


	public static List findFailedSettlementBatch(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(SETTLEMENT_BATCH_QUERY);
		ResultSet rs = ps.executeQuery();
		List lst = processSettlementBatchResultSet(rs);
		rs.close();
		ps.close();
		return lst;

	}	

	private static List processSettlementBatchResultSet(ResultSet rs) throws SQLException {
		List lst = new ArrayList();
		while (rs.next()) {
			SettlementBatchInfo sbInfo = new SettlementBatchInfo();
			sbInfo.setMerchant_id(rs.getString("MERCHANT_ID"));
			sbInfo.setBatch_id(rs.getString("BATCH_ID"));
			sbInfo.setSettle_date_time(rs.getTimestamp("SETTLE_DATE_TIME"));
			sbInfo.setBatch_status(rs.getString("BATCH_STATUS"));
			sbInfo.setBatch_response_msg(rs.getString("BATCH_RESPONSE_MSG"));
			sbInfo.setProcessor_batch_id(rs.getString("PROCESSOR_BATCH_ID"));
			sbInfo.setSubmission_id(rs.getString("SUBMISSION_ID"));
			sbInfo.setSales_transactions(rs.getInt("SALES_TRANSACTIONS"));
			sbInfo.setSales_amount(rs.getDouble("SALES_AMOUNT"));
			sbInfo.setReturn_transactions(rs.getInt("RETURN_TRANSACTIONS"));
			sbInfo.setReturn_amount(rs.getDouble("RETURN_AMOUNT"));
			lst.add(sbInfo);
		}
		return lst;
	}


}
