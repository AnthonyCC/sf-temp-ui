/*
 * ComplaintDAO.java
 *
 * Created on November 15, 2001, 10:38 PM
 */

package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Data Access Object for fraud prevention
 *
 * @version $Revision$
 * @author $Author$
 */
public class FraudDAO implements java.io.Serializable {

	private static Category LOGGER = LoggerFactory.getInstance(FraudDAO.class);

	private final static String QUERY_DUPLICATE_ACCOUNT =
		"SELECT COUNT(PM.ID) FROM CUST.PAYMENTMETHOD pm, CUST.CUSTOMERINFO CI"
			+ " WHERE PM.ACCOUNT_NUMBER = ?"
			+ " AND PM.CUSTOMER_ID <> ?"
			+ " AND CI.CUSTOMER_ID=PM.CUSTOMER_ID"
			+ " AND 'X'<>CI.CORP_CUSTOMER";

	public boolean isDuplicateAccountNumber(Connection conn, String accountNumber, String erpCustomerId) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(QUERY_DUPLICATE_ACCOUNT);

		ps.setString(1, accountNumber);
		ps.setString(2, erpCustomerId);

		ResultSet rs = ps.executeQuery();

		int count = 0;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		rs.close();
		ps.close();

		if (count > 0) {
			LOGGER.debug("FRAUD RULE TRIPPED: Duplicate Payment Method Account Number");
			return true;
		} else {
			return false;
		}
	} 

	private final static String QUERY_DUPLICATE_ABA_ROUTE_ACCOUNT =
		"SELECT COUNT(PM.ID) FROM CUST.PAYMENTMETHOD pm, CUST.CUSTOMERINFO CI"
			+ " WHERE PM.ACCOUNT_NUMBER = ?"
			+ " AND PM.ABA_ROUTE_NUMBER = ?"
			+ " AND PM.CUSTOMER_ID <> ?"
			+ " AND CI.CUSTOMER_ID=PM.CUSTOMER_ID"
			+ " AND 'X'<>CI.CORP_CUSTOMER";

	public boolean isDuplicateAbaRouteAccountNumber(Connection conn, String accountNumber, String abaRouteNumber, String erpCustomerId) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(QUERY_DUPLICATE_ABA_ROUTE_ACCOUNT);

		ps.setString(1, accountNumber);
		ps.setString(2, abaRouteNumber);
		ps.setString(3, erpCustomerId);

		ResultSet rs = ps.executeQuery();

		int count = 0;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		rs.close();
		ps.close();

		if (count > 0) {
			LOGGER.debug("FRAUD RULE TRIPPED: Duplicate Payment Method ABA Route/Account Number");
			return true;
		} else {
			return false;
		}
	} 

	public boolean isDuplicateShipToAddress(Connection conn, ErpAddressModel address, String erpCustomerId) throws SQLException {
		//
		// Build the query
		//
		StringBuffer dlvQ = new StringBuffer();
		boolean hasApartment = address.getApartment()!=null && !"".equals(address.getApartment().trim());

		dlvQ.append("SELECT SUM(cnt) as COUNT FROM ( ");
		dlvQ.append("SELECT count(d.salesaction_id) AS cnt FROM CUST.DELIVERYINFO D ");
//		if (erpCustomerId != null)
			dlvQ.append(", CUST.SALESACTION SA, CUST.SALE S ");
		dlvQ.append("WHERE D.SCRUBBED_ADDRESS = ? ");
		if (hasApartment) {
			dlvQ.append("AND D.APARTMENT = REPLACE(REPLACE(UPPER(?),'-'),' ') ");
		} else {
			dlvQ.append("AND D.APARTMENT IS NULL ");
		}
		dlvQ.append("AND D.ZIP = ? ");
		dlvQ.append("AND D.SALESACTION_ID = SA.ID ");
		dlvQ.append("AND SA.SALE_ID = S.ID ");
		dlvQ.append("AND S.STATUS= ? ");
		dlvQ.append("AND S.TYPE = ? ");
		//Orders settled 6 months before.
		if (erpCustomerId != null) {
			dlvQ.append("AND S.CUSTOMER_ID <> ? ");
		}
		dlvQ.append("AND SA.ACTION_DATE < ? ");
		/*dlvQ.append("UNION ALL ");
		dlvQ.append("SELECT count(ID) AS CNT FROM CUST.ADDRESS A ");
		dlvQ.append("WHERE A.SCRUBBED_ADDRESS = ? ");
		if (hasApartment) {
			dlvQ.append("AND A.APARTMENT = REPLACE(REPLACE(UPPER(?),'-'),' ') ");
		} else {
			dlvQ.append("AND A.APARTMENT IS NULL ");
		}
		dlvQ.append("AND A.ZIP = ? ");
		if (erpCustomerId != null)
			dlvQ.append("AND A.CUSTOMER_ID <> ? ");*/
		dlvQ.append(") ");
		//
		// Populate the query list
		//
		List sqlParams = new ArrayList();
//		for (int i = 0; i < 2; i++) {
			sqlParams.add(address.getScrubbedStreet());
			if (hasApartment) {
				sqlParams.add(address.getApartment());
			}
			sqlParams.add(address.getZipCode());			
			sqlParams.add(EnumSaleStatus.SETTLED.getStatusCode());
			sqlParams.add(EnumSaleType.REGULAR.getSaleType());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, -(ErpServicesProperties.getSignupPromoDeliveryDaysLimit()));
			Timestamp time = new Timestamp(calendar.getTime().getTime());
//			sqlParams.add(time);
			if (erpCustomerId != null)
				sqlParams.add(erpCustomerId);
//		}
		//
		// Execute query
		//
		int count = runQuery(conn, dlvQ.toString(), sqlParams, time);
		if (count > 0) {
			LOGGER.debug("FRAUD RULE TRIPPED: Duplicate Delivery Address");
			return true;
		} else {
			return false;
		}
	}

	public boolean isDuplicateBillToAddress(Connection conn, AddressModel address, String erpCustomerId) throws SQLException {
		//
		// Build the query
		//
		StringBuffer dlvQ = new StringBuffer();

		dlvQ.append("SELECT SUM(cnt) as COUNT FROM ( ");
		dlvQ.append("SELECT count(p.salesaction_id) AS cnt FROM CUST.PAYMENTINFO P ");
		if (erpCustomerId != null)
			dlvQ.append(", CUST.SALESACTION SA, CUST.SALE S ");
		//dlvQ.append("WHERE P.SCRUBBED_ADDRESS = ? ");
		dlvQ.append("WHERE P.ADDRESS1 = ? ");
		dlvQ.append("AND P.APARTMENT = REPLACE(REPLACE(UPPER(?),'-'),' ') ");
		dlvQ.append("AND P.ZIP_CODE = ? ");
		if (erpCustomerId != null) {
			dlvQ.append("AND P.SALESACTION_ID = SA.ID ");
			dlvQ.append("AND SA.SALE_ID = S.ID ");
			dlvQ.append("AND S.CUSTOMER_ID <> ? ");
		}
		dlvQ.append("UNION ALL ");
		dlvQ.append("SELECT count(ID) AS CNT FROM CUST.PAYMENTMETHOD PM ");
		//dlvQ.append("WHERE A.SCRUBBED_ADDRESS = ? ");
		dlvQ.append("WHERE PM.ADDRESS1 = ? ");
		dlvQ.append("AND PM.APARTMENT = REPLACE(REPLACE(UPPER(?),'-'),' ') ");
		dlvQ.append("AND PM.ZIP_CODE = ? ");
		if (erpCustomerId != null)
			dlvQ.append("AND PM.CUSTOMER_ID <> ? ");
		dlvQ.append(") ");
		//
		// Populate the query list
		//
		List sqlParams = new ArrayList();
		for (int i = 0; i < 2; i++) {
			sqlParams.add(address.getAddress1());
			sqlParams.add(address.getApartment());
			sqlParams.add(address.getZipCode());
			if (erpCustomerId != null)
				sqlParams.add(erpCustomerId);
		}
		//
		// Execute query
		//
		int count = runQuery(conn, dlvQ.toString(), sqlParams);
		if (count > 0) {
			LOGGER.debug("FRAUD RULE TRIPPED: Duplicate Billing Address");
			return true;
		} else {
			return false;
		}
	}

	public boolean isDuplicatePhone(Connection conn, PhoneNumber phone) throws SQLException {
		return this.isDuplicatePhone(conn, phone, null);
	}

	public boolean isDuplicatePhone(Connection conn, PhoneNumber phone, String erpCustomerId) throws SQLException {
		//
		// Build the query
		//
		boolean hasExtension = (phone.getExtension() != null && !"".equals(phone.getExtension().trim()));
		StringBuffer sql = new StringBuffer("SELECT SUM(cnt) as COUNT FROM ( ");
		sql.append("SELECT count(*) AS cnt FROM CUST.CUSTOMERINFO CI ");
		sql.append("WHERE ((CI.HOME_PHONE = replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.') ");
		if (hasExtension) {
			sql.append("AND CI.HOME_EXT = ?) ");
		} else {
			sql.append("AND CI.HOME_EXT is null) ");
		}
		sql.append("OR (CI.BUSINESS_PHONE = replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.') ");
		if (hasExtension) {
			sql.append("AND CI.BUSINESS_EXT = ?) ");
		} else {
			sql.append("AND CI.BUSINESS_EXT is null) ");
		}
		sql.append("OR (CI.CELL_PHONE = replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.') ");
		if (hasExtension) {
			sql.append("AND CI.CELL_EXT = ?)) ");
		} else {
			sql.append("AND CI.CELL_EXT is null)) ");
		}
		if (erpCustomerId != null)
			sql.append("AND CI.CUSTOMER_ID <> ? ");
		sql.append("UNION ALL ");
		sql.append("SELECT count(*) AS CNT FROM CUST.ADDRESS A ");
		sql.append("WHERE A.PHONE = replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.') ");
		if (hasExtension) {
			sql.append("AND A.PHONE_EXT = ? ");
		} else {
			sql.append("AND A.PHONE_EXT is null ");
		}
		if (erpCustomerId != null)
			sql.append("AND A.CUSTOMER_ID <> ? ");
		sql.append(")");
		//
		// Populate the query list
		//
		List sqlParams = new ArrayList();
		for (int i = 0; i < 3; i++) { // loop once for each: HOME, BIZ, CELL
			sqlParams.add(phone.getPhone());
			if (hasExtension)
				sqlParams.add(phone.getExtension());
		}
		if (erpCustomerId != null)
			sqlParams.add(erpCustomerId);
		sqlParams.add(phone.getPhone()); // ADDRESS PHONE
		if (hasExtension)
			sqlParams.add(phone.getExtension()); // ADDRESS EXTENSION
		if (erpCustomerId != null)
			sqlParams.add(erpCustomerId);
		//
		// Execute query
		//
		int count = runQuery(conn, sql.toString(), sqlParams);
		if (count > 0) {
			LOGGER.debug("FRAUD RULE TRIPPED: Duplicate Phone Number");
			return true;
		} else {
			return false;
		}
	}

	private int runQuery(Connection conn, String sql, List sqlParams) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		//
		// Populate PreparedStatement
		//
		ps = conn.prepareStatement(sql);
		int j = 0;
		Iterator it = sqlParams.iterator();
		while (it.hasNext()) {
			j++;
			String next = (String) it.next();
			ps.setString(j, next);
		}
		//
		// Execute the query
		//
		rs = ps.executeQuery();
		if (rs.next()) {
			count = rs.getInt(1);
		}
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
		} catch (SQLException sef) {
			//eat it for the time being
		}
		return (count);
	}
	
	private int runQuery(Connection conn, String sql, List sqlParams, Timestamp time) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		//
		// Populate PreparedStatement
		//
		ps = conn.prepareStatement(sql);
		int j = 0;
		Iterator it = sqlParams.iterator();
		while (it.hasNext()) {
			j++;
			String next = (String) it.next();
			ps.setString(j, next);
		}
		j++;
		ps.setTimestamp(j, time);
		//
		// Execute the query
		//
		rs = ps.executeQuery();
		if (rs.next()) {
			count = rs.getInt(1);
		}
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
		} catch (SQLException sef) {
			//eat it for the time being
		}
		return (count);
	}

}