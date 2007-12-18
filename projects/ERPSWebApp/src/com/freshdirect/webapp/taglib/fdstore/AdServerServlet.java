package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

		Connection conn = null;
		try {
			conn = this.getConnection();
			PreparedStatement  ps = conn.prepareStatement("Insert into CreativeUpdate (ProductId, Available, Price) values (?,?,?)");
			for (int counter = 0; counter < prodId.length; counter++) {
				ps.setString(1, prodId[counter]);
				ps.setString(2, avail[counter]);
				ps.setString(3, price[counter]);
				ps.addBatch();
			}
			
			int[] result = ps.executeBatch();
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
		if (ctx == null)
			throw new NamingException("No Context found");

		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/AdServer");

		Connection conn = null;
		if (ds != null) {
			conn = ds.getConnection();
		}
		return conn;
	}

}
