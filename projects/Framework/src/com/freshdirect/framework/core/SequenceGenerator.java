package com.freshdirect.framework.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SequenceGenerator {

	private SequenceGenerator() {
	}

	public static String getNextId(Connection conn) throws SQLException {
		return query(conn, "SELECT SYSTEM_SEQ.nextval FROM DUAL");
	}

	public static String getNextId(Connection conn, String schema) throws SQLException {
		return query(conn, "SELECT "+schema+".SYSTEM_SEQ.nextval FROM DUAL");
	}

	public static String getNextIdFromSequence(Connection conn, String sequence) throws SQLException {
		return query(conn, "SELECT "+sequence+".nextval FROM DUAL");
	}
	
	public static String getNextId(Connection conn, String schema, String sequence) throws SQLException {
		return query(conn, "SELECT "+schema+"."+sequence+".nextval FROM DUAL");
	}

	private static String query(Connection conn, String query) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			throw new SQLException("Unable to get next sequence number from Oracle sequence");
		}
        String response= String.valueOf(rs.getObject(1));   
		rs.close();
		ps.close();
        
		return response;
	}
	

}
