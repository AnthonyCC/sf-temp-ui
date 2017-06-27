/*
 * $Workfile:FDCustomerOrderInfoDAO.java$
 *
 * $Date:7/8/2003 11:24:32 AM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer.ejb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import weblogic.descriptor.annotation.AnnotationDefinition.UnSetValueAnnotation;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDCustomerSearchCriteria;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderSearchCriteria;
import com.freshdirect.fdstore.customer.PendingOrder;
import com.freshdirect.fdstore.customer.SilverPopupDetails;
import com.freshdirect.fdstore.customer.UnsettledOrdersInfo;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGiftCardUtil;
/**
 *
 *
 * @version $Revision:15$
 * @author $Author:Mike Rose$
 */
class FDCustomerOrderInfoDAO {
	
	@SuppressWarnings( "unused" )
	private static Category LOGGER = LoggerFactory.getInstance(FDCustomerOrderInfoDAO.class);
	
	/*private static final String ORDER_SEARCH_QUERY = "select  s.id, sa.requested_date, s.status, sa.amount, di.last_name, di.first_name, c.user_id, di.delivery_type, "
		+ "di.phone, di.alt_phone, s.stop_sequence, s.truck_number, s.wave_number, di.cutofftime, c.id as erp_id, fc.id as fd_id, " 
		+ "(select on_fd_account from cust.paymentinfo where salesaction_id = sa.id) as on_fd_account, "
		+ "(select p.profile_value from  cust.profile p where p.customer_id = fc.id and p.profile_name='VIPCustomer') as vip_cust, " 
		+ "(select p.profile_value from  cust.profile p where p.customer_id = fc.id and p.profile_name='ChefsTable') as chefs_table "
		+ "from cust.sale s,  cust.salesaction sa, cust.deliveryinfo di, cust.customer c, cust.fdcustomer fc "   
		+ "where s.id=sa.sale_id and SA.ACTION_TYPE IN ('CRO','MOD') and s.CROMOD_DATE = sa.action_date and sa.id=di.salesaction_id " 
		+ "and s.customer_id = c.id and c.id = fc.erp_customer_id "; */


	private static final String ORDER_SEARCH_QUERY = "select  s.id, sa.requested_date, s.status, sa.amount, di.last_name, di.first_name, c.user_id, di.delivery_type, "
		+ "di.phone, di.alt_phone, s.stop_sequence, s.truck_number, di.cutofftime, c.id as erp_id, fc.id as fd_id, " 
		+ "(select on_fd_account from cust.paymentinfo where salesaction_id = sa.id) as on_fd_account, "
		+ "(select p.profile_value from  cust.profile p where p.customer_id = fc.id and p.profile_name='VIPCustomer') as vip_cust, " 
		+ "(select p.profile_value from  cust.profile p where p.customer_id = fc.id and p.profile_name='ChefsTable') as chefs_table "
		+ "from cust.sale s,  cust.salesaction sa, cust.deliveryinfo di, cust.customer c, cust.fdcustomer fc "   
		+ "where s.id=sa.sale_id and SA.ACTION_TYPE IN ('CRO','MOD') and s.CROMOD_DATE = sa.action_date and sa.id=di.salesaction_id " 
		+ "and s.customer_id = c.id and c.id = fc.erp_customer_id ";
	
	public static final String GIFT_CARD_ORDER_SERACH_QUERY=" select  /*+use_nl(di,sa,scm,s)*/ s.id, sa.requested_date, s.status, sa.amount, di.last_name, di.first_name, c.user_id, di.delivery_type, "+ 
	" di.phone, di.alt_phone, s.stop_sequence, s.truck_number, di.cutofftime, c.id as erp_id, fc.id as fd_id, "+  
	"(select on_fd_account from cust.paymentinfo where salesaction_id = sa.id) as on_fd_account, "+ 
	"(select p.profile_value from  cust.profile p where p.customer_id = fc.id and p.profile_name='VIPCustomer') as vip_cust, "+  
	"(select p.profile_value from  cust.profile p where p.customer_id = fc.id and p.profile_name='ChefsTable') as chefs_table "+ 
	"from cust.sale s,  cust.salesaction sa, cust.deliveryinfo di, "+ 
	" cust.customer c, cust.fdcustomer fc, CUST.APPLIED_GIFT_CARD agc "+     
	" where s.id=sa.sale_id and sa.id=di.salesaction_id "+   
	" and s.customer_id = c.id and c.id = fc.erp_customer_id "+ 
	//" and scm.max_date = sa.action_date "+
	" and agc.SALESACTION_ID = sa.id "+ 
	" and SA.ACTION_TYPE IN ('CRO','MOD') "+
	" and S.CROMOD_DATE=SA.ACTION_DATE ";
	

	public static List<FDCustomerOrderInfo> findOrdersByCriteria(Connection conn, FDOrderSearchCriteria criteria) throws SQLException {
		
		CriteriaBuilder builder = new CriteriaBuilder();
		boolean isGiftCardSearch=false;
		
		String value = criteria.getOrderNumber();
		if (value != null) {
			builder.addString("s.id", value);
			return runOrderQuery(conn, builder,isGiftCardSearch);
		}

		java.util.Date d = criteria.getDeliveryDate();
		if (d != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal = DateUtil.truncate(cal);
			builder.addSql("sa.requested_date = ?", new Object[] { new java.sql.Date(cal.getTime().getTime())});
			builder.addSql("di.starttime >= ?", new Object[] { new java.sql.Date(cal.getTime().getTime())});
			cal.add(Calendar.DATE, 1);
			builder.addSql("di.endtime < ?", new Object[] { new java.sql.Date(cal.getTime().getTime())});
		}

		value = criteria.getFirstName();
		if (value != null) {
			builder.addString("lower(di.first_name)", value.toLowerCase());
		}
		value = criteria.getLastName();
		if (value != null) {
			builder.addString("lower(di.last_name)", value.toLowerCase());
		}
		value = criteria.getPhone();
		if (value != null) {
			builder.addString("di.phone", value);
		}
		String[] status = criteria.getStatus();
		if (status != null) {
			builder.addInString("s.status", status);
		}
		value = criteria.getEmail();
		if (value != null) {
			builder.addString("c.user_id", value.toLowerCase());
		}

		Set<String> values = criteria.getDepotLocationIds();
		if (!values.isEmpty()) {
			builder.addInString("di.depotlocation_id", (String[]) values.toArray(new String[values.size()]));
		}
		
		if(criteria.isCorporate()){
			builder.addSql("di.delivery_type = ?", new Object[] {EnumDeliveryType.CORPORATE.getCode()});
		}
		
		if(criteria.isChefsTable()){
			builder.addSql("(SELECT profile_value FROM cust.PROFILE WHERE customer_id=fc.id AND profile_name='ChefsTable') = ?", new Object[]{"1"});
		}
		
		String gc_num=criteria.getGiftCardNumber();
		//String cert_num=criteria.getCertificateNumber();
		
		
		if(gc_num!=null && gc_num.trim().length()>0){
			isGiftCardSearch=true;			
			
			if(gc_num.length()<16)
				builder.addString("agc.certificate_num", gc_num);
			else
			    builder.addSql("agc.certificate_num=(select certificate_num from cust.gift_card where givex_num=?)", new Object[]{ErpGiftCardUtil.encryptGivexNum(gc_num)});
					
		}
		/*else if(cert_num!=null && cert_num.trim().length()>0){
			isGiftCardSearch=true;
			builder.addString("agc.certificate_num", cert_num);			
		} */
		  
		
				
		return runOrderQuery(conn, builder,isGiftCardSearch);
	}

	private static List<FDCustomerOrderInfo> runOrderQuery(Connection conn, CriteriaBuilder builder,boolean isGiftCardSearch) throws SQLException {
		PreparedStatement ps=null;
		System.out.println("query :"+GIFT_CARD_ORDER_SERACH_QUERY + " and " + builder.getCriteria());
		if(isGiftCardSearch)
		   ps = conn.prepareStatement(GIFT_CARD_ORDER_SERACH_QUERY + " and " + builder.getCriteria());
		else
			ps = conn.prepareStatement(ORDER_SEARCH_QUERY + " and " + builder.getCriteria());
		
		Object[] obj = builder.getParams();
		for(int i = 0; i < obj.length; i++) {
			if(obj[i] instanceof java.util.Date){
				ps.setDate(i+1, (Date)obj[i]);
			}
			else{
				ps.setObject(i+1, obj[i]);
			}
		}
		ResultSet rs = ps.executeQuery();
		
		List<FDCustomerOrderInfo> lst = new ArrayList<FDCustomerOrderInfo>();
		while (rs.next()) {
			FDCustomerOrderInfo oInfo = new FDCustomerOrderInfo();
			
			oInfo.setSaleId(rs.getString("ID"));
			oInfo.setDeliveryDate((java.util.Date) rs.getDate("REQUESTED_DATE"));
			oInfo.setOrderStatus(EnumSaleStatus.getSaleStatus(rs.getString("STATUS")));
			oInfo.setAmount(rs.getDouble("AMOUNT"));
			oInfo.setLastName(rs.getString("LAST_NAME"));
			oInfo.setFirstName(rs.getString("FIRST_NAME"));
			oInfo.setPhone(new PhoneNumber(rs.getString("PHONE")).getPhone());
			oInfo.setAltPhone(new PhoneNumber(rs.getString("ALT_PHONE")).getPhone());
			//oInfo.setWaveNum(rs.getString("WAVE_NUMBER"));
			oInfo.setRouteNum(rs.getString("TRUCK_NUMBER"));
			oInfo.setStopSequence(rs.getString("STOP_SEQUENCE"));
			oInfo.setCutoffTime((java.util.Date) rs.getTimestamp("CUTOFFTIME"));
			oInfo.setPaymentType(EnumPaymentType.getEnum(rs.getString("ON_FD_ACCOUNT")));
			oInfo.setEmail(rs.getString("USER_ID"));
			oInfo.setDeliveryType(rs.getString("DELIVERY_TYPE"));
			oInfo.setVip("true".equals(rs.getString("VIP_CUST")));
			oInfo.setChefsTable("1".equals(rs.getString("CHEFS_TABLE")));
			oInfo.setIdentity(new FDIdentity(rs.getString("ERP_ID"), rs.getString("FD_ID")));
			
			lst.add(oInfo);
		}
		rs.close();
		ps.close();

		return lst;
	}
	
	public static List<FDCustomerOrderInfo> findCustomersByCriteria(Connection conn, FDCustomerSearchCriteria criteria) throws SQLException {
		CriteriaBuilder builder = new CriteriaBuilder();
		
		if(criteria.getOrderNumber() != null && !"".equals(criteria.getOrderNumber())) {
			return customerSearchByOrderNumber(conn, criteria.getOrderNumber().trim());
		}
		
		String value = NVL.apply(criteria.getEmail(), "").trim().toLowerCase();
		if(!"".equals(value)){
			builder.addString("c.user_id", value);
		}
		
		value = NVL.apply(criteria.getCustomerId(), "").trim();
		if(!"".equals(value)) {
			builder.addString("c.id", value);
		}
		
		value = NVL.apply(criteria.getFirstName(), "").trim();
		if(!"".equals(value)) {
			builder.addSql("lower(ci.first_name) like lower(?)", new Object[] { value.replace('*', '%')});
		}
		value = NVL.apply(criteria.getLastName(), "").trim();
		if(!"".equals(value)) {
			builder.addSql("lower(ci.last_name) like lower(?)", new Object[] { value.replace('*', '%')});
		}
		
		value = NVL.apply(criteria.getPhone(), "").trim();
		if(!"".equals(value)) {
			builder.addSql("ci.home_phone like ?", new Object[] { value.replace('*', '%')});
		}
		
		value = NVL.apply(criteria.getAddress(), "").trim();
		if(!"".equals(value)){
			builder.addSql("a.scrubbed_address like upper(?)", new Object[] { value.replace('*', '%')});
		}
		
		value = NVL.apply(criteria.getApartment(), "").trim();
		if(!"".equals(value)){
			builder.addString("a.apartment", value);
		}
		
		value = NVL.apply(criteria.getZipCode(), "").trim();
		if(!"".equals(value)){
			builder.addString("a.zip", value);
		}
		
		value = NVL.apply(criteria.getDepotCode(), "").trim();
		if(!"".equals(value)){
			builder.addString("fc.depot_code", value);
		}
		
		value = NVL.apply(criteria.getSapId(), "").trim();
		if(!"".equals(value)){
			builder.addString("c.sap_id", value);
		}
		return customerSearch(conn, builder);
	}
	
	private static final String CUST_SEARCH_QUERY = 
		/*
		"select distinct c.id, ci.first_name, ci.last_name, c.user_id, ci.home_phone, ci.business_phone, ci.cell_phone, "
		+ "(select p.profile_value from cust.profile p where p.customer_id = fc.id and p.profile_name='VIPCustomer') VIP_CUST, "
		+ "(select p.profile_value from cust.profile p where p.customer_id = fc.id and p.profile_name='ChefsTable') CHEFS_TABLE "
		+ "from cust.customer c, cust.customerinfo ci, cust.fdcustomer fc, cust.address a "
		+ "where c.id = ci.customer_id and c.id = fc.erp_customer_id and c.id = a.customer_id";
		*/
		//Removed  address to look up for GC customers also who doesn't have delivery address. 
		
		"select distinct c.id, ci.first_name, ci.last_name, c.user_id, ci.home_phone, ci.business_phone, ci.cell_phone, "
		+ "(select p.profile_value from cust.profile p where p.customer_id = fc.id and p.profile_name='VIPCustomer') VIP_CUST, "
		+ "(select p.profile_value from cust.profile p where p.customer_id = fc.id and p.profile_name='ChefsTable') CHEFS_TABLE "
		+ "from cust.customer c, cust.customerinfo ci, cust.fdcustomer fc, cust.address a "
		+ "where c.id = ci.customer_id and c.id = fc.erp_customer_id and c.id = a.customer_id (+)";
	
	private static List<FDCustomerOrderInfo> customerSearch(Connection conn, CriteriaBuilder builder) throws SQLException {
		String query = CUST_SEARCH_QUERY + " and " + builder.getCriteria();
		PreparedStatement ps = conn.prepareStatement(query);
		Object[] obj = builder.getParams();
		for(int i = 0; i < obj.length; i++) {
			ps.setObject(i+1, obj[i]);
		}
		ResultSet rs = ps.executeQuery();
		List<FDCustomerOrderInfo> lst = processCustomerResultSet(rs);
		rs.close();
		ps.close();
		return lst;
	}

	private static final String CUST_BY_ORDER_QUERY =
		"select c.id, ci.first_name, ci.last_name, c.user_id, ci.home_phone, ci.business_phone, ci.cell_phone, "
		 + "(select p.profile_value from cust.profile p where p.customer_id = fc.id and p.profile_name='VIPCustomer') VIP_CUST, "
		 + "(select p.profile_value from cust.profile p where p.customer_id = fc.id and p.profile_name='ChefsTable') CHEFS_TABLE "
		 + "from cust.sale s, cust.customer c, cust.customerinfo ci, cust.fdcustomer fc "
		 + "where s.id = ? and c.id = ci.customer_id and s.customer_id = c.id " 
		 + "and c.id = fc.erp_customer_id";

	private static List<FDCustomerOrderInfo> customerSearchByOrderNumber(Connection conn, String orderNumber) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(CUST_BY_ORDER_QUERY);
		ps.setString(1, orderNumber);
		ResultSet rs = ps.executeQuery();
		List<FDCustomerOrderInfo> lst = processCustomerResultSet(rs);

		rs.close();
		ps.close();

		return lst;
	}

	private static List<FDCustomerOrderInfo> processCustomerResultSet(ResultSet rs) throws SQLException {
		List<FDCustomerOrderInfo> lst = new ArrayList<FDCustomerOrderInfo>();
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
	
	private static final String DELIVERY_PASS_SAVINGS =
		"select (sum(CL.AMOUNT+CL.AMOUNT*tax_rate)-(select sum(dp.amount+decode(s.type, 'SUB',sa.tax,'REG',dp.amount*cl.tax_rate,0)) from cust.delivery_pass dp, cust.sale s, cust.salesaction sa,CUST.CHARGELINE CL" 
		+ " where DP.ID=? and DP.PURCHASE_ORDER_ID=s.id"
		+ " and s.id=sa.sale_id and S.CROMOD_DATE=SA.ACTION_DATE and SA.ACTION_TYPE in ('CRO','MOD')  and cl.type(+)='DLV' and CL.SALESACTION_ID(+)=sa.id)) as SAVINGS from CUST.CHARGELINE CL, cust.salesaction sa, cust.sale s"
		+" where cl.type='DLV' and CL.SALESACTION_ID=sa.id and s.DLV_PASS_ID= ? and s.status<>'CAN' and s.id=sa.sale_id and sa.ACTION_TYPE in ('CRO','MOD') and"
		+" s.CROMOD_DATE=sa.action_date and s.customer_id =? group by s.CUSTOMER_ID";
		
	public static String getActiveDeliveryPassSavings(Connection conn, String customerPK,String dpNumber) throws SQLException {
		String savings="0";
		PreparedStatement ps = conn.prepareStatement(DELIVERY_PASS_SAVINGS);
		ps.setString(1, dpNumber);
		ps.setString(2, dpNumber);
		ps.setString(3, customerPK);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			savings = rs.getString("SAVINGS");
		}
		rs.close();
		ps.close();

		return savings;
	}
	
	private static final String UNSETTLED_ORDERS = "select \"Delivery Date\","+
			" MAX(DECODE(\"Status\",'STL',\"Order Count\")) \"Settled\", "+
			" MAX(DECODE(\"Status\",'CBK',\"Order Count\")) \"Charge Back\","+
			" MAX(DECODE(\"Status\",'STF',\"Order Count\")) \"Settlement Failed\","+
			" MAX(DECODE(\"Status\",'ENR',\"Order Count\")) \"Enroute\","+
			" MAX(DECODE(\"Status\",'PPG',\"Order Count\")) \"Payment Pending\","+
			" MAX(DECODE(\"Status\",'CPG',\"Order Count\")) \"Capture Pending\","+
			" MAX(DECODE(\"Status\",'STP',\"Order Count\")) \"GC Settlement Pending\","+
			" MAX(DECODE(\"Status\",'REF',\"Order Count\")) \"Pending Refusal\","+
			" MAX(DECODE(\"Status\",'POG',\"Order Count\")) \"GC Payment Pending\","+
			" MAX(DECODE(\"Status\",'RPG',\"Order Count\")) \"GC Registration Pending\""+
			" FROM ("+
			" select count(s.id) \"Order Count\", sa.requested_Date \"Delivery Date\", s.status \"Status\""+
			" from cust.sale s, cust.salesaction sa where s.id=sa.sale_id  and sa.action_type in ('CRO','MOD') and S.CROMOD_DATE=sa.action_date"+
			" and sa.requested_date between trunc(sysdate-15) and trunc(sysdate-1) and s.status!='CAN' "+
			" Group BY sa.requested_Date,s.status"+
			" Order BY sa.requested_Date"+
			" )"+
			" Group BY \"Delivery Date\" Order BY \"Delivery Date\"";

	public static List<UnsettledOrdersInfo> getUnsettledOrders(Connection conn, java.util.Date date) throws SQLException {
		Statement statement =  conn.createStatement();
		ResultSet rs = statement.executeQuery(UNSETTLED_ORDERS);
		List<UnsettledOrdersInfo> unsettledOrders = new ArrayList<UnsettledOrdersInfo>();
		while(rs.next()){
			UnsettledOrdersInfo order = new UnsettledOrdersInfo();
			order.setDeliveryDate(rs.getDate("Delivery Date"));
			order.setSettled(rs.getString("Settled"));
			order.setChargeBack(rs.getString("Charge Back"));
			order.setSettlementFailed(rs.getString("Settlement Failed"));
			order.setEnroute(rs.getString("Enroute"));
			order.setPaymentPending(rs.getString("Payment Pending"));
			order.setCapturePending(rs.getString("Capture Pending"));
			order.setPendingRefusal(rs.getString("Pending Refusal"));
			order.setGCSettlementPending(rs.getString("GC Settlement Pending"));
			order.setGCPaymentPending(rs.getString("GC Payment Pending"));
			order.setGCRegistrationPending(rs.getString("GC Registration Pending"));
			
			unsettledOrders.add(order);
		}
		return unsettledOrders;
		
	}
	
	private static final String Pending_Deliveries_Freshdirect = "SELECT COUNT(1) \"Order Count\", SA.REQUESTED_DATE \"Delivery Date\" FROM CUST.SALE S, " +
			"CUST.SALESACTION SA WHERE S.STATUS='ENR' AND SA.REQUESTED_DATE BETWEEN SYSDATE-3 AND SYSDATE-1 AND S.CROMOD_DATE=SA.ACTION_DATE " +
			"AND SA.ACTION_TYPE IN ('CRO','MOD') AND S.ID=SA.SALE_ID AND S.TYPE='REG'"+
			"AND S.E_STORE='FreshDirect'"+
			"GROUP BY SA.REQUESTED_DATE ORDER BY SA.REQUESTED_DATE";
	private static final String Pending_Deliveries_fdx = "SELECT COUNT(1) \"Order Count\", SA.REQUESTED_DATE \"Delivery Date\" FROM CUST.SALE S, " +
			"CUST.SALESACTION SA WHERE S.STATUS='ENR' AND SA.REQUESTED_DATE BETWEEN SYSDATE-3 AND SYSDATE-1 AND S.CROMOD_DATE=SA.ACTION_DATE " +
			"AND SA.ACTION_TYPE IN ('CRO','MOD') AND S.ID=SA.SALE_ID AND S.TYPE='REG'"+
			"AND S.E_STORE='FDX'"+
			"GROUP BY SA.REQUESTED_DATE ORDER BY SA.REQUESTED_DATE";

	public static Map<String, List<PendingOrder>> getPendingDeliveries(Connection conn) throws SQLException {
		Statement statement =  conn.createStatement();
		ResultSet rs1 = statement.executeQuery(Pending_Deliveries_Freshdirect);
		List<PendingOrder> freshDirectOrders = new ArrayList<PendingOrder>();
		List<PendingOrder> fdx = new ArrayList<PendingOrder>();
		while(rs1.next()){
			PendingOrder order = new PendingOrder();
			order.setOrderCount(rs1.getString("Order Count"));
			order.setDeliveryDate(rs1.getString("Delivery Date"));
			freshDirectOrders.add(order);
		}
		ResultSet rs2 = statement.executeQuery(Pending_Deliveries_fdx);
		while(rs2.next()){
			PendingOrder order = new PendingOrder();
			order.setOrderCount(rs2.getString("Order Count"));
			order.setDeliveryDate(rs2.getString("Delivery Date"));
			fdx.add(order);
		}
		Map<String, List<PendingOrder>> totalOrders = new HashMap<String, List<PendingOrder>>();
		totalOrders.put("freshdirect", freshDirectOrders);
		totalOrders.put("fdx", fdx);
		return totalOrders;
	}
	
	private static final String retrieve_silverpopupdetails = "SELECT customer_id, qualifier, destination from cust.customer_pushnotification where trunc(UPDATE_TIMESTAMP) = trunc(sysdate)";
	public static List<SilverPopupDetails> getSilverPopupDetails(Connection conn) throws SQLException {
		Statement statement =  conn.createStatement();
		ResultSet rs = statement.executeQuery(retrieve_silverpopupdetails);
		List<SilverPopupDetails> details = new ArrayList<SilverPopupDetails>();
		while(rs.next()){
			SilverPopupDetails silverPopup = new SilverPopupDetails();
			silverPopup.setCustomerId(rs.getString("customer_id"));
			silverPopup.setQualifier(rs.getString("qualifier"));
			silverPopup.setDestination(rs.getString("destination"));
			details.add(silverPopup);
		}
		
		return details;
	}	
}