package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;



/**@author ekracoff on Jul 20, 2004*/
public class AdServerServlet extends HttpServlet{

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] prodId = request.getParameterValues("product_id");
		String[] price = request.getParameterValues("price");
		String[] avail = request.getParameterValues("available");
		String zonePricingEnabled = request.getParameter("zonePricingEnabled");

		Connection conn = null;
		try {
			conn = this.getConnection();
			int[] result = {};
			PreparedStatement  ps = null;
			System.out.println("Zone Pricing Enabled:"+zonePricingEnabled);
			if(null ==zonePricingEnabled || "false".equalsIgnoreCase(zonePricingEnabled)){
				ps = conn.prepareStatement("Insert into CreativeUpdate (ProductId, Available, Price) values (?,?,?)");
				for (int counter = 0; counter < prodId.length; counter++) {
					ps.setString(1, prodId[counter]);
					ps.setString(2, avail[counter]);
					ps.setString(3, price[counter]);
					ps.addBatch();
				}
				result = ps.executeBatch();
								
			}else {
				String[] zoneId = request.getParameterValues("zoneId");
				String[] zoneType = request.getParameterValues("zoneType");
				ps = conn.prepareStatement("select count(*) from CreativeUpdate_Zone");
				ResultSet rs =ps.executeQuery();
				int count = 0;
				if(rs.next()){
					count = rs.getInt(1);
				}
				System.out.println("Count from CreativeUpdate_Zone table:"+count);
				if(count <= 0){
					ps = conn.prepareStatement("Insert into CreativeUpdate_Zone (ProductId, Available, Price, ZoneId, ZoneType) values (?,?,?,?,?)");
					for (int counter = 0; counter < prodId.length; counter++) {
						ps.setString(1, prodId[counter]);
						ps.setString(2, avail[counter]);
						ps.setString(3, price[counter]);
						ps.setString(4, zoneId[counter]);
						ps.setString(5, zoneType[counter]);
						ps.addBatch();
					}
					result = ps.executeBatch();
				}
			}
//			int[] result = ps.executeBatch();
			ps.close();
			
			System.out.println("-=Batch update complete=- Inserted " + result.length + " records into CreativeUpdate table");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

	private Connection getConnection() throws SQLException, NamingException {

		Context ctx = new InitialContext();

		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/AdServer");

		Connection conn = null;
		if (ds != null) {
			conn = ds.getConnection();
		}
		return conn;
	}

}
