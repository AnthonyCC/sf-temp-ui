package com.freshdirect.framework.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class PropertyDao {

	protected static DataSource dataSource;
	protected static Connection conn;

	public static final Category LOGGER = LoggerFactory
			.getInstance(PropertyDao.class);

	public static void setDataSource(DataSource dataSource) {
		try {
			dataSource = DataSourceLocator.getDataSource();
		} catch (NamingException e) {
			LOGGER.error("error while setting datasource resources");
		}
	}
	public static Connection getConnection(String cacheKey) {
	
			try {
				conn = DataSourceLocator.getConnection(cacheKey);
			} catch (SQLException e) {
				LOGGER.error("error while retriving the db connection");
			}
		return conn;
	}

	public static boolean getBoolean(String value) {
		return (value != null && "1".equalsIgnoreCase(value));
	}

	

	public  static void close(ResultSet rs, PreparedStatement ps, Connection conn) {
		DaoUtil.close(rs, ps, conn);
	}

	public static void close(PreparedStatement ps, Connection conn) {
		DaoUtil.close(null, ps, conn);
	}

	public void close(PreparedStatement ps) {
		DaoUtil.close(ps);
	}
	
	private static final String LOAD_PROPERTIES_QRY01 = "select pm.name, pm.default_value from mis.property_master pm where pm.type = ? and pm.property_enabled = 'X' ";

	private static final String LOAD_PROPERTIES_QRY02 = " select property_name,overriden_value from MIS.PROPERTY_OVERRIDE where CLUSTER_NAME = ?  ";

	public static Properties loadProperties(Connection conn2, final String type, final String cluster,
			final String node) throws SQLException {
		final Properties p = new Properties();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn=conn2;
		try{
			
			ps = conn.prepareStatement(LOAD_PROPERTIES_QRY01);
			ps.setString(1, type);
			rs = ps.executeQuery();
	
			while (rs.next()) {
				p.setProperty(rs.getString("name").trim(),
						rs.getString("default_value"));
			}
	
			ps.close();
			rs.close();
			
			ps = conn.prepareStatement(LOAD_PROPERTIES_QRY02);
			ps.setString(1, cluster.trim());
			rs = ps.executeQuery();
			
			while (rs.next()) {
				
				if (p.containsKey(rs.getString("property_name").trim())) {
					p.setProperty(rs.getString("property_name").trim(),
							rs.getString("overriden_value"));
				}
			} 

		}catch (SQLException e) {
		    System.err.println("Error: " + e);
		}finally {
			close(rs, ps, conn);
		}
		return p;
	}
	
}
