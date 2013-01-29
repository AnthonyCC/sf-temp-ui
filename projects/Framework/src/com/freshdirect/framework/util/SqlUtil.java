package com.freshdirect.framework.util;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class SqlUtil {

	public static void setInt(PreparedStatement ps, int pos, Integer value) throws SQLException{
		if(value == null){
			ps.setNull(pos, java.sql.Types.INTEGER);
		} else {
			ps.setInt(pos, value);
		}
	}

	public static void setString(PreparedStatement ps, int pos, String value) throws SQLException{
		if(value == null){
			ps.setNull(pos, java.sql.Types.VARCHAR);
		} else {
			ps.setString(pos, value);
		}
	}

	
	public static void setTimestamp(PreparedStatement ps, int pos, Long value) throws SQLException{
		if (value==null){
			ps.setNull(pos, java.sql.Types.TIMESTAMP);
		} else {
			ps.setTimestamp(pos, new java.sql.Timestamp(value));
		}
	}

	public static void setBigDecimal(PreparedStatement ps, int pos, BigDecimal value) throws SQLException{
		if(value == null){
			ps.setNull(pos, java.sql.Types.DOUBLE);
		} else {
			ps.setBigDecimal(pos, value);
		}
	}

	public static void setBoolean(PreparedStatement ps, int pos, Boolean value) throws SQLException{
		if(value == null){
			ps.setNull(pos, java.sql.Types.BOOLEAN);
		} else {
			ps.setBoolean(pos, value);
		}
	}


}
