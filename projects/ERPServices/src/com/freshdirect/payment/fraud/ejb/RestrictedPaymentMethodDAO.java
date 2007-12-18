/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.payment.fraud.ejb;

import java.sql.*;

import com.freshdirect.framework.core.*;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.payment.fraud.EnumRestrictedPatternType;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.EnumRestrictionReason;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodCriteria;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodModel;
import java.util.List;
import java.util.ArrayList;

/**
 * RestrictedPaymentMethodDAO.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class RestrictedPaymentMethodDAO {
	

	static String createSQL = "INSERT INTO CUST.RESTRICTEDPAYMENTMETHOD " +
			 "(ID, CUSTOMER_ID, PAYMENT_METHOD_ID, FIRST_NAME, LAST_NAME, PAYMENT_METHOD_TYPE, CARD_TYPE, EXPIRATION_DATE, BANK_NAME, ABA_ROUTE_PATTERN_TYPE, " +
			 "ABA_ROUTE_NUMBER, ACCOUNT_PATTERN_TYPE, BANK_ACCOUNT_TYPE, ACCOUNT_NUMBER, STATUS, SOURCE, CREATE_USER, CREATE_DATE, CASE_ID, REASON_CODE, NOTE) " +
			 "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static PrimaryKey createRestrictedPaymentMethod(Connection conn, RestrictedPaymentMethodModel m) throws SQLException {
	
		PreparedStatement ps = conn.prepareStatement(createSQL);
		String id = SequenceGenerator.getNextId(conn, "CUST");
		int index = 1;
		ps.setString(index++, id);
		ps.setString(index++, m.getCustomerId());
		ps.setString(index++, m.getPaymentMethodId());
		ps.setString(index++, m.getFirstName());
		ps.setString(index++, m.getLastName());
		ps.setString(index++, m.getPaymentMethodType().getName());
		if (m.getCardType() != null) {
			ps.setString(index++, m.getCardType().getFdName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}
		if (m.getExpirationDate() != null) {
			ps.setDate(index++, new java.sql.Date(DateUtil.truncate(m.getExpirationDate()).getTime()));
		} else {
			ps.setNull(index++, Types.DATE);			
		}
		ps.setString(index++, m.getBankName());			
		
		if (m.getAbaRoutePatternType() != null) {
			ps.setString(index++, m.getAbaRoutePatternType().getName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}		
		ps.setString(index++, m.getAbaRouteNumber());
		if (m.getAccountPatternType() != null) {
			ps.setString(index++, m.getAccountPatternType().getName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}
		if (m.getBankAccountType() != null) {
			ps.setString(index++, m.getBankAccountType().getName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}
		ps.setString(index++, m.getAccountNumber());
		ps.setString(index++, m.getStatus().getName());		
		ps.setString(index++, m.getSource().getCode());
		ps.setString(index++, m.getCreateUser());
		ps.setDate(index++, new java.sql.Date(DateUtil.truncate(m.getCreateDate()).getTime()));
		ps.setString(index++, m.getCaseId());
		if (m.getReason() != null) {
			ps.setString(index++, m.getReason().getName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}		
		ps.setString(index++, m.getNote());

		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
			m.setPK(new PrimaryKey(id));
			m.setId(id);
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			if (ps != null) ps.close();
			ps = null;
		}
	
		return m.getPK();
		
	}

	static String loadSQL = "SELECT " +
				 "ID, CUSTOMER_ID, PAYMENT_METHOD_ID, FIRST_NAME, LAST_NAME, PAYMENT_METHOD_TYPE, CARD_TYPE, EXPIRATION_DATE, BANK_NAME, ABA_ROUTE_PATTERN_TYPE, ABA_ROUTE_NUMBER, " +
				 "ACCOUNT_PATTERN_TYPE, ACCOUNT_NUMBER, BANK_ACCOUNT_TYPE, STATUS, SOURCE, CREATE_USER, LAST_MODIFY_USER, CREATE_DATE, LAST_MODIFY_DATE, CASE_ID, REASON_CODE, NOTE " +
				 "FROM CUST.RESTRICTEDPAYMENTMETHOD";

	public static RestrictedPaymentMethodModel findRestrictedPaymentMethodByPrimaryKey(Connection conn, PrimaryKey pk) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		RestrictedPaymentMethodModel m = null;
		
		try {
			String sql = loadSQL + " WHERE ID = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, pk.getId());
			rs = ps.executeQuery();
			if  (rs.next()) {
				m = loadResult(rs);
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}				
		return m;
		
	}
	
	public static List findRestrictedPaymentMethodByCustomerId(Connection conn, String customerId, EnumRestrictedPaymentMethodStatus status) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		RestrictedPaymentMethodModel m = null;
		ArrayList list = null;
		
		try {
			String sql = loadSQL + " WHERE CUSTOMER_ID = ?";
			if (status != null) {
				sql += " AND STATUS = ?";
			}
			sql += " ORDER BY ACCOUNT_NUMBER, ABA_ROUTE_NUMBER"; // default order by
			ps = conn.prepareStatement(sql);
			int index = 1;
			ps.setString(index++, customerId);
			if (status != null) {
				ps.setString(index++, status.getName());
			}			
			rs = ps.executeQuery();
			list = new ArrayList();
			while  (rs.next()) {
				m = loadResult(rs);
				list.add(m);
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}				
		return list;
				
	}
	
	public static RestrictedPaymentMethodModel findRestrictedPaymentMethodByPaymentMethodId(Connection conn, String paymentMethodId, EnumRestrictedPaymentMethodStatus status) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		RestrictedPaymentMethodModel m = null;
		ArrayList list = null;
		
		try {
			String sql = loadSQL + " WHERE PAYMENT_METHOD_ID = ?";
			if (status != null) {
				sql += " AND STATUS = ?";
			}
			sql += " ORDER BY ACCOUNT_NUMBER, ABA_ROUTE_NUMBER"; // default order by
			ps = conn.prepareStatement(sql);
			int index = 1;
			ps.setString(index++, paymentMethodId);
			if (status != null) {
				ps.setString(index++, status.getName());
			}			
			rs = ps.executeQuery();
			list = new ArrayList();
			if  (rs.next()) {
				m = loadResult(rs);
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}				
		return m;
				
	}

	public static List findRestrictedPaymentMethods(Connection conn, RestrictedPaymentMethodCriteria criteria) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		RestrictedPaymentMethodModel m = null;
		ArrayList list = null;

		CriteriaBuilder builder = new CriteriaBuilder();
		
		try {
			String sql = loadSQL;
			
			builder.addString("ACCOUNT_NUMBER", criteria.getAccountNumber());
			if (criteria.getAbaRouteNumber() != null) {
				builder.addString("ABA_ROUTE_NUMBER", criteria.getAbaRouteNumber());				
			}
			if (criteria.getBankAccountType() != null) {
				builder.addObject("BANK_ACCOUNT_TYPE", criteria.getBankAccountType().getName());
			}
			if (criteria.getFirstName() != null) {
				builder.addSql("UPPER(FIRST_NAME) = UPPER(?)", new Object[] {criteria.getFirstName()});
			}
			if (criteria.getLastName() != null) {
				builder.addSql("UPPER(LAST_NAME) = UPPER(?)", new Object[] {criteria.getLastName()});
			}
			if (criteria.getCreateDate() != null) {
				builder.addObject("CREATE_DATE", new java.sql.Date(criteria.getCreateDate().getTime()));
			}
			if (criteria.getReason() != null) {
				builder.addString("REASON_CODE", criteria.getReason().getName());
			}
			if (criteria.getStatus() != null) {
				builder.addString("STATUS", criteria.getStatus().getName());
			}
			String criteriaStr = builder.getCriteria(); 
			if (criteriaStr != null && !"".equals(criteriaStr)) {
				sql +=  " WHERE " + criteriaStr;
			}
			ps = conn.prepareStatement(sql);
			Object[] par = builder.getParams();
			for (int i = 0; i < par.length; i++) {
				ps.setObject(i + 1, par[i]);
			}
			rs = ps.executeQuery();
			list = new ArrayList();
			while  (rs.next()) {
				m = loadResult(rs);
				list.add(m);
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}				
		return list;
				
	}

	public static List loadAll(Connection conn) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		RestrictedPaymentMethodModel m = null;
		ArrayList list = null;
		
		try {
			ps = conn.prepareStatement(loadSQL);
			rs = ps.executeQuery();
			list = new ArrayList();
			while (rs.next()) {
				m = loadResult(rs);
				list.add(m);
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}		
		
		return list;
	
	}
	
	public static List loadAllBadPaymentMethods(Connection conn) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		RestrictedPaymentMethodModel m = null;
		ArrayList list = null;
		
		try {
			ps = conn.prepareStatement(loadSQL + " WHERE STATUS = 'BAD'");
			rs = ps.executeQuery();
			list = new ArrayList();
			while (rs.next()) {
				m = loadResult(rs);
				list.add(m);
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}		
		
		return list;
	
	}

	// load all rows where ABA_ROUTE_PATTERN_TYPE are STARTS_WITH, ENDS_WITH or CONTAINS
	// AND
	// load all rows where ACCOUNT_PATTERN_TYPE are STARTS_WITH, ENDS_WITH or CONTAINS
	static String loadAllPatternsSQL = loadSQL + " WHERE ABA_ROUTE_PATTERN_TYPE IN ('SW', 'EW', 'CT') " +
										"UNION " +
										loadSQL + " WHERE ACCOUNT_PATTERN_TYPE IN ('SW', 'EW', 'CT')"; 

	public static List loadAllPatterns(Connection conn) throws SQLException {
	
		PreparedStatement ps = null;
		ResultSet rs = null;
		RestrictedPaymentMethodModel m = null;
		ArrayList list = null;
		
		try {
			ps = conn.prepareStatement(loadAllPatternsSQL);
			rs = ps.executeQuery();
			list = new ArrayList();
			while (rs.next()) {
				m = loadResult(rs);
				list.add(m);
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}		
		
		return list;
	
	}
	
	private static RestrictedPaymentMethodModel loadResult(ResultSet rs) throws SQLException {

		String id = rs.getString("ID");
		RestrictedPaymentMethodModel m = new RestrictedPaymentMethodModel(new PrimaryKey(id));
		m.setCustomerId(rs.getString("CUSTOMER_ID"));
		m.setPaymentMethodId(rs.getString("PAYMENT_METHOD_ID"));
		m.setFirstName(rs.getString("FIRST_NAME"));
		m.setLastName(rs.getString("LAST_NAME"));
		m.setPaymentMethodType(EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE")));
		String cardType = rs.getString("CARD_TYPE");
		if (cardType != null) {
			m.setCardType(EnumCardType.getCardType(cardType));
		}
		java.sql.Date expirationDate = rs.getDate("EXPIRATION_DATE");
		if (expirationDate != null) {
			m.setExpirationDate(new java.util.Date(expirationDate.getTime()));
		}
		m.setBankName(rs.getString("BANK_NAME"));
		String abaRoutePatternType = rs.getString("ABA_ROUTE_PATTERN_TYPE");
		if (abaRoutePatternType != null) {
			m.setAbaRoutePatternType(EnumRestrictedPatternType.getEnum(abaRoutePatternType));
		}
		m.setAbaRouteNumber(rs.getString("ABA_ROUTE_NUMBER"));
		String accountPatternType = rs.getString("ACCOUNT_PATTERN_TYPE");
		if (accountPatternType != null) {
			m.setAccountPatternType(EnumRestrictedPatternType.getEnum(accountPatternType));
		}
		m.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
		m.setBankAccountType(EnumBankAccountType.getEnum(rs.getString("BANK_ACCOUNT_TYPE")));
		m.setStatus(EnumRestrictedPaymentMethodStatus.getEnum(rs.getString("STATUS")));
		m.setSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
		m.setCreateUser(rs.getString("CREATE_USER"));
		m.setLastModifyUser(rs.getString("LAST_MODIFY_USER"));
		java.sql.Date createDate = rs.getDate("CREATE_DATE");
		if (createDate != null) {
			m.setCreateDate(new java.util.Date(createDate.getTime()));
		}
		java.sql.Date lastModifyDate = rs.getDate("LAST_MODIFY_DATE");
		if (lastModifyDate != null) {
			m.setLastModifyDate(new java.util.Date(lastModifyDate.getTime()));
		}
		m.setCaseId(rs.getString("CASE_ID"));
		String reasonCode = rs.getString("REASON_CODE");
		if (reasonCode != null) {
			m.setReason(EnumRestrictionReason.getEnum(reasonCode));
		}
		m.setNote(rs.getString("NOTE"));
		return m;
		
	}

	static String storeSQL = 
					"UPDATE CUST.RESTRICTEDPAYMENTMETHOD SET " +
					 "CUSTOMER_ID = ?, PAYMENT_METHOD_ID = ?, FIRST_NAME = ?, LAST_NAME = ?, PAYMENT_METHOD_TYPE = ?, CARD_TYPE = ?, EXPIRATION_DATE = ?, BANK_NAME = ?, " +
					 "ABA_ROUTE_PATTERN_TYPE = ?, ABA_ROUTE_NUMBER = ?, ACCOUNT_PATTERN_TYPE = ?, ACCOUNT_NUMBER = ?, BANK_ACCOUNT_TYPE = ?, STATUS = ?, " +
					 "SOURCE = ?, CREATE_USER = ?, CREATE_DATE = ?, LAST_MODIFY_USER = ?, LAST_MODIFY_DATE = ?, CASE_ID = ?, REASON_CODE = ?, NOTE = ? " +
					 "WHERE ID = ?";
		
	public static void storeRestrictedPaymentMethod(Connection conn, RestrictedPaymentMethodModel m) throws SQLException {

		ErpPaymentMethodModel paymentMethod = (ErpPaymentMethodModel)findPaymentMethodByAccountInfo(conn, m);
		if (paymentMethod != null) { 
			if (paymentMethod.getPK() != null) {
				m.setPaymentMethodId(paymentMethod.getPK().getId());
			}
			m.setCustomerId(paymentMethod.getCustomerId());
		}

		PreparedStatement ps = conn.prepareStatement(storeSQL);
		
		int index = 1;
		ps.setString(index++, m.getCustomerId());
		ps.setString(index++, m.getPaymentMethodId());
		ps.setString(index++, m.getFirstName());
		ps.setString(index++, m.getLastName());
		ps.setString(index++, m.getPaymentMethodType().getName());
		if (m.getCardType() != null) {
			ps.setString(index++, m.getCardType().getFdName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}
		if (m.getExpirationDate() != null) {
			ps.setDate(index++, new java.sql.Date(DateUtil.truncate(m.getExpirationDate()).getTime()));
		} else {
			ps.setNull(index++, Types.DATE);			
		}		
		ps.setString(index++, m.getBankName());
		if (m.getAbaRoutePatternType() != null) {
			ps.setString(index++, m.getAbaRoutePatternType().getName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}
		ps.setString(index++, m.getAbaRouteNumber());
		if (m.getAccountPatternType() != null) {
			ps.setString(index++, m.getAccountPatternType().getName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}
		ps.setString(index++, m.getAccountNumber());
		if (m.getBankAccountType() != null) {
			ps.setString(index++, m.getBankAccountType().getName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}
		ps.setString(index++, m.getStatus().getName());		
		ps.setString(index++, m.getSource().getCode());
		ps.setString(index++, m.getCreateUser());
		if (m.getCreateDate() != null) {
			ps.setDate(index++, new java.sql.Date(DateUtil.truncate(m.getCreateDate()).getTime()));
		} else {
			ps.setDate(index++, new java.sql.Date((new java.util.Date()).getTime()));  //create date can't be null
		}
		ps.setString(index++, m.getLastModifyUser());
		if (m.getLastModifyDate() != null) {
			ps.setDate(index++, new java.sql.Date(DateUtil.truncate(m.getLastModifyDate()).getTime()));
		} else {
			ps.setNull(index++, Types.DATE);			
		}
		ps.setString(index++, m.getCaseId());
		if (m.getReason() != null) {
			ps.setString(index++, m.getReason().getName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}		
		ps.setString(index++, m.getNote());
		ps.setString(index++, m.getId());
		
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();
		ps = null;

		
	}

	public static void removeRestrictedPaymentMethod(Connection conn, PrimaryKey pk, String lastModifyUser) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE CUST.RESTRICTEDPAYMENTMETHOD SET STATUS = 'DEL', LAST_MODIFY_USER=?, LAST_MODIFY_DATE=TO_CHAR(SYSDATE, 'DD-MON-YYYY') WHERE ID=?");
			int index = 1;
			ps.setString(index++, lastModifyUser);
			ps.setString(index++, pk.getId());
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not deleted");
			}
		} finally {
			if (ps != null) ps.close();
			ps = null;
		}
	}

	private static String findPaymentMethodByAccountInfoQuery = "SELECT ID, NAME, ACCOUNT_NUMBER, EXPIRATION_DATE, CARD_TYPE, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_NAME, BANK_ACCOUNT_TYPE, ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIP_CODE, COUNTRY, CUSTOMER_ID FROM CUST.PAYMENTMETHOD";
	
	public static ErpPaymentMethodI findPaymentMethodByAccountInfo(Connection conn, RestrictedPaymentMethodModel m) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ErpPaymentMethodModel model = null;
		
		try {
			CriteriaBuilder builder = new CriteriaBuilder();

			String sql = findPaymentMethodByAccountInfoQuery;
			
			builder.addString("ACCOUNT_NUMBER", m.getAccountNumber());

			if (m.getPaymentMethodType() != null) {
				builder.addString("PAYMENT_METHOD_TYPE", m.getPaymentMethodType().getName());
			}
			if (m.getAbaRouteNumber() != null) {
				builder.addString("ABA_ROUTE_NUMBER", m.getAbaRouteNumber());
			}			
			if (m.getBankAccountType() != null) {
				builder.addString("BANK_ACCOUNT_TYPE", m.getBankAccountType().getName());
			}
			if (m.getCardType() != null) {
				builder.addString("CARD_TYPE", m.getCardType().getFdName());
			}
			if (m.getExpirationDate() != null) {
				builder.addObject("EXPIRATION_DATE", new java.sql.Date(m.getExpirationDate().getTime()));
			}
			String criteriaStr = builder.getCriteria();
			if (criteriaStr == null || "".equals(criteriaStr)) {
				return model;	// don't return all payment methods if there are no criterias
			}
			if (criteriaStr != null && !"".equals(criteriaStr)) {
				sql +=  " WHERE " + criteriaStr;
			}
			ps = conn.prepareStatement(sql);
			Object[] par = builder.getParams();
			for (int i = 0; i < par.length; i++) {
				ps.setObject(i + 1, par[i]);
			}

			rs = ps.executeQuery();

			if (rs.next()) {
				EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE"));
				model = (ErpPaymentMethodModel)PaymentManager.createInstance(paymentMethodType);
				model.setPK(new PrimaryKey(rs.getString("ID")));
				model.setName(rs.getString("NAME"));
				model.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
				model.setExpirationDate(rs.getDate("EXPIRATION_DATE"));
				model.setCardType(EnumCardType.getCardType(rs.getString("CARD_TYPE")));
				model.setAbaRouteNumber(rs.getString("ABA_ROUTE_NUMBER"));
				model.setBankName(rs.getString("BANK_NAME"));
				model.setBankAccountType(EnumBankAccountType.getEnum(rs.getString("BANK_ACCOUNT_TYPE")));
				model.setAddress1(rs.getString("ADDRESS1"));
				model.setAddress2(NVL.apply(rs.getString("ADDRESS2"), ""));
				model.setApartment(NVL.apply(rs.getString("APARTMENT"), ""));
				model.setCity(rs.getString("CITY"));
				model.setState(rs.getString("STATE"));
				model.setZipCode(rs.getString("ZIP_CODE"));
				model.setCountry(rs.getString("COUNTRY"));
				model.setCustomerId(rs.getString("CUSTOMER_ID"));
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}
	
		return model;
	}
}
