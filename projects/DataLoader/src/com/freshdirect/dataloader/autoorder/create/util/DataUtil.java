package com.freshdirect.dataloader.autoorder.create.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.dataloader.autoorder.create.command.IAccept;
import com.freshdirect.dataloader.autoorder.create.command.OrderBean;


public class DataUtil {
	
	private static final String CUSTOMER_CHECK = "select * from cust.customer where user_id like ?";
	
	private static final String CUSTOMER_GET = "select * from cust.address order by TO_NUMBER(ID) desc";
	
		
	private static final String CUSTOMER_FULLGET = "select  c.ID ERPID, fd.ID FDID, c.user_id USID from cust.customer c, cust.fdcustomer fd where c.ID = fd.ERP_CUSTOMER_ID and user_id like ?";
	
	public static void getOrders(Connection conn, String prefix,int totalCount, IAccept accept ) throws Exception {
				
		PreparedStatement ps = conn.prepareStatement(CUSTOMER_FULLGET);
		ps.setString(1, prefix+"%");
		
		ResultSet rs = ps.executeQuery();
		OrderBean orderBean = null;
			
		Map<String, OrderBean> custMap = new HashMap<String, OrderBean>();
		while (rs.next()) {				
			orderBean = new OrderBean();						
			orderBean.setErpCustomerPK(getString(rs.getString("ERPID")));
			orderBean.setFdCustomerPK(getString(rs.getString("FDID")));				
			custMap.put(getString(rs.getString("USID")), orderBean);
			//accept.accept(orderBean);
		}
		System.out.println("totalCount>>"+totalCount);
		for(int intCount=1; intCount<=totalCount; intCount++) {
			accept.accept(custMap.get(prefix+intCount+"@freshdirect.com"));
		}
		rs.close();
		ps.close();			
	}
		

	
		
	public static void getCustomers(Connection conn, Connection devCon, int noOfCust, String prefix, IAccept accept) throws SQLException {
			
		
			OrderBean orderBean = null;
			PreparedStatement ps = conn.prepareStatement(CUSTOMER_GET);
			
			ResultSet rs = ps.executeQuery();
			int intCount = 1;		
			while (rs.next() && intCount <=noOfCust) {
				
				if(!hasCustomer(devCon, prefix+intCount+"@freshdirect.com")) {
					orderBean = new OrderBean();
								
					orderBean.setAddress1(getString(rs.getString("ADDRESS1")));
					orderBean.setAddress2(getString(rs.getString("ADDRESS2")));
					orderBean.setApt(getString(rs.getString("APARTMENT")));
					orderBean.setCity(getString(rs.getString("CITY")));
					orderBean.setCountry(getString(rs.getString("COUNTRY")));			
					orderBean.setState(getString(rs.getString("STATE")));
					orderBean.setZip(getString(rs.getString("ZIP")));	
					orderBean.setServiceType(rs.getString("SERVICE_TYPE"));
					orderBean.setCustomerId(prefix+intCount);							
					accept.accept(orderBean);
				}
				intCount++;	
			}
			rs.close();
			ps.close();			
			
	}
	
	private static boolean hasCustomer(Connection conn, String prefix) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(CUSTOMER_CHECK);

		ps.setString(1, (prefix+"%"));
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {				
			rs.close();
			ps.close();
			return true;		
		}
						
		return false;
	}
	
	private static String getString(String data) {
		if(data == null || "null".equalsIgnoreCase(data)) {
			return "";
		} else {
			return data;
		}
	}

}
