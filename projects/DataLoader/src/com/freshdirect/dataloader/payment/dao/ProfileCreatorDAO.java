
package com.freshdirect.dataloader.payment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;



public class ProfileCreatorDAO  {
	private static final Category LOGGER = LoggerFactory.getInstance(ProfileCreatorDAO.class);
	
	public static List<String> getCustomersByBatchId(Connection conn, String batchId) throws SQLException{
		List<String> customerList = new ArrayList<String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
				ps =
					conn.prepareStatement("SELECT CUSTOMER_ID FROM MIS.PAYMENT_MIGRATION_CUSTOMER WHERE BATCH_ID = ? AND IS_IGNORE IS NULL");
				ps.setString(1, batchId);
				rs = ps.executeQuery();
				while (rs.next()) {
					customerList.add(rs.getString("CUSTOMER_ID"));
				}

				
		}finally{
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
		}
		return customerList;
			
	}

	public static boolean getCustomerProfile(Connection conn, String customerId,
			String profileName) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
				ps = conn.prepareStatement("SELECT * FROM CUST.PROFILE WHERE CUSTOMER_ID = ? AND PROFILE_NAME = ?");
				ps.setString(1, customerId);
				ps.setString(2, profileName);
				
				rs = ps.executeQuery();
				if (rs.next()) {
					return true;
				}

				
		}finally{
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
		}
		return false;
			
	}
	

}

