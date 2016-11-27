package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.crm.CrmCustomerHeaderInfo;

public class CrmCustomerInfoDAO {
	private static final String CRM_CUTOMER_HEADER_QUERY = 
		"select c.id, c.user_id, c.active, ci.first_name, ci.middle_name, ci.last_name, ci.home_phone, ci.business_phone, ci.cell_phone, "
		+ "(select count(1) from cust.customercredit where customer_id = c.id) as credits, "
		+ "(select sum(amount) from cust.customercredit where customer_id = c.id) as remaining_credit, "
		+ "(select count(1) from cust.case where customer_id = c.id) as cases, "
		+ "(select count(1) from cust.customeralert where customer_id = c.id) alerts "
		+ "from cust.customer c, cust.customerinfo ci "
		+ "where c.id = ? and c.id = ci.customer_id ";
	
	public static CrmCustomerHeaderInfo getCrmCustomerHeaderInfo (Connection conn, String customerId) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(CRM_CUTOMER_HEADER_QUERY);
			ps.setString(1, customerId);
			rs = ps.executeQuery();
			if(rs.next()){
				CrmCustomerHeaderInfo info = new CrmCustomerHeaderInfo(rs.getString("ID"), rs.getString("USER_ID"));
				info.setFirstName(rs.getString("FIRST_NAME"));
				info.setActive("1".equals(rs.getString("ACTIVE")));
				info.setMiddleName(rs.getString("MIDDLE_NAME"));
				info.setLastName(rs.getString("LAST_NAME"));
				info.setHomePhone(new PhoneNumber(rs.getString("HOME_PHONE")));
				info.setBusinessPhone(new PhoneNumber(rs.getString("BUSINESS_PHONE")));
				info.setCellPhone(new PhoneNumber(rs.getString("CELL_PHONE")));
				info.setCredits(rs.getInt("CREDITS"));
				info.setRemainingAmount(rs.getDouble("REMAINING_CREDIT"));
				info.setCases(rs.getInt("CASES"));
				info.setOnAlert(rs.getInt("ALERTS") > 0);
				return info;
			}else{
				throw new SQLException("No Customer found for customer id: " + customerId);
			}
		}finally{
			if(rs != null){
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
		}
		
	}
}
