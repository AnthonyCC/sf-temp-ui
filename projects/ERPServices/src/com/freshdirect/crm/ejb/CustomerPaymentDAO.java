package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerPaymentDAO {
	private static final String GET_USERID_BY_ACCOUNT_NUMBER = "SELECT DISTINCT(c.USER_ID) userid FROM cust.paymentmethod p, cust.customer c  " +
															   "WHERE c.ID = p.CUSTOMER_ID AND account_number = ?";

	
	public static String getUserIDByAccountNumber(Connection conn, String accountNum) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String userId = null;
		try{
			ps = conn.prepareStatement(GET_USERID_BY_ACCOUNT_NUMBER);
			ps.setString(1, accountNum);
			rs = ps.executeQuery();
			if(rs.next()){
				userId = rs.getString("USERID");
				
			}
		}catch(SQLException sexp){
			throw sexp;
		}finally{
			if(rs != null){
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
		}
		return userId;
	}
	
	private static final String GET_ORDERS_BY_ACCOUNT_NUMBER = "SELECT /*+ USE_NL(s, sa, p) */DISTINCT(s.id) saleid FROM " +
															   "cust.sale s,cust.salesaction sa,cust.paymentinfo p " +
															   "WHERE s.id = sa.sale_id AND sa.id = p.salesaction_id AND p.ACCOUNT_NUMBER =  ?";
	public static List<String> getOrdersByAccountNumber(Connection conn, String accountNum) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> orderIds = new ArrayList<String>();
		try{
			ps = conn.prepareStatement(GET_ORDERS_BY_ACCOUNT_NUMBER);
			ps.setString(1, accountNum);
			rs = ps.executeQuery();
			while(rs.next()){
				orderIds.add(rs.getString("SALEID"));
			}
		}catch(SQLException sexp){
			throw sexp;
		}finally{
			if(rs != null){
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
		}
		return orderIds;
	}
	
	
}
