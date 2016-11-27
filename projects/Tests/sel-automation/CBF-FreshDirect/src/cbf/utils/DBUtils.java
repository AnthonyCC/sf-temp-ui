/******************************************************************************
$Id : DBUtils.java 10/21/2014 4:05:28 PM
Copyright 2014-2016 IGATE GROUP OF COMPANIES. All rights reserved
(Subject to Limited Distribution and Restricted Disclosure Only.)
THIS SOURCE FILE MAY CONTAIN INFORMATION WHICH IS THE PROPRIETARY
INFORMATION OF IGATE GROUP OF COMPANIES AND IS INTENDED FOR USE
ONLY BY THE ENTITY WHO IS ENTITLED TO AND MAY CONTAIN
INFORMATION THAT IS PRIVILEGED, CONFIDENTIAL, OR EXEMPT FROM
DISCLOSURE UNDER APPLICABLE LAW.
YOUR ACCESS TO THIS SOURCE FILE IS GOVERNED BY THE TERMS AND
CONDITIONS OF AN AGREEMENT BETWEEN YOU AND IGATE GROUP OF COMPANIES.
The USE, DISCLOSURE REPRODUCTION OR TRANSFER OF THIS PROGRAM IS
RESTRICTED AS SET FORTH THEREIN.
******************************************************************************/

package cbf.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cbf.engine.BaseModuleDriver;

/**
 * Utility to handle connection to Microsoft Access Database 
 *
 */
public class DBUtils {
	
	/**
	 * Constructor to make connection to database
	 * @param dbName name of database
	 */
	public DBUtils(String dbName){
		connection = getConnection(dbName);
	}
	
	public static Connection connection;
	/**
	 * Checks if the database exists or not
	 * 
	 * @param dbName
	 * 			contains name of the database
	 * @return boolean data
	 */

	public boolean checkExists(String dbName) {
		boolean result = false;
		try {
			ResultSet resultSet = connection.getMetaData().getCatalogs();

			while (resultSet.next()) {
				String databaseName = resultSet.getString(1);
				if (databaseName.equals(dbName)) {
					result = true;
				}
			}
			resultSet.close();
			 return result;
		} catch (Exception e) {
			logger.handleError("Exception caught : ", e);
			return result;
		}

	}
	
	/**
	 * 
	 * Executes the query string and returns a list of Map
	 * @param dbName
	 * 			contains name of the database
	 * @param queryString
	 * 			contains the SQL query
	 * @param params
	 * @return list of Map
	 */
	public List<Map> runQuery(String dbName,
			String queryString, ArrayList<Object> params) {
		ResultSet resultSet = null;
		resultSet = execute(connection, queryString, params);
		List<Map> list = null;
		list = rs2Map(resultSet);
		return list;
	}
	private ResultSet execute1(Connection connection, String queryString) {
		PreparedStatement preStmt;
		ResultSet resultSet = null;

		try {
			preStmt = connection.prepareStatement(queryString);
			resultSet = preStmt.executeQuery();
		} catch (SQLException e) {
			logger.handleError("Error in executing query : ", e);
		}

		return resultSet;
	}
	public List<Map> runQueryOr(String dbName,
			String queryString) {
		List<Map> list = null;
		try
		{
			ResultSet resultSet = null;
			resultSet = execute1(connection, queryString);
			list = rs2Map(resultSet);
			return list;
		}
		catch(Exception e)
		{
			logger.warning("Run query for query "+queryString+ " caught exception and exception is "+e.getMessage());
			return list;
		}
		
	}
	
	
	/**
	 * Makes connection to the database
	 * @param dbName
	 * 			contains name of the database
	 * @return Connection object or null depending upon whether connection is established or not
	 */
	public Connection getConnection(String dbName) {
        String url=null,userID=null,password=null;
        userID = "FDSTORE_PRDA";
        password = "FDSTORE_PRDA";

	url="jdbc:oracle:thin:@scan-dev.dev.nyc1.freshdirect.com:1521:devint4";
        try {
//			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
//        	DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            connection = DriverManager.getConnection(url,userID,password);
			BaseModuleDriver.RESULT.passed("Connection successful","Should able to connect Database","Successfully connected to Database");
			return connection;
		} catch (Exception e) {
			BaseModuleDriver.RESULT.failed("Connection unsuccessful","Should able to connect Database","Failure in Database connection");
			return null;
		}

	}
	
	/**
	 * Disconnects from the established connection
	 */
	public void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			logger.handleError("Error in disconnecting : ", e);
		}
	}
	
	/**
	 * Overriding toString() method and returning DBUtils format string
	 */
	public String toString() {
		return "DBUtils()";
	}
	
	private ResultSet execute(Connection connection, String queryString,
			ArrayList<Object> params) {
		PreparedStatement preStmt;
		ResultSet resultSet = null;

		try {
			preStmt = connection.prepareStatement(queryString);

			if (params != null) {
				for (Object temp : params) {
					preStmt.setObject(params.indexOf(temp) + 1, temp);
				}
			}
			resultSet = preStmt.executeQuery();
		} catch (SQLException e) {
			logger.handleError("Error in executing query : ", e);
		}

		return resultSet;
	}
	
	private List<Map> rs2Map(ResultSet resultSet) {
		List<Map> list = new ArrayList<Map>();
		ResultSetMetaData meta = null;
		try {
			meta = resultSet.getMetaData();
		} catch (SQLException e1) {
			logger.handleError("Error in mapping metadata : ", e1);
		}

		try {
			while (resultSet.next()) {
				Map<String, Object> hashMap = new HashMap<String, Object>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
	                String key = meta.getColumnName(i);
	                String value = resultSet.getString(key);
	                hashMap.put(key, value);
				}
				list.add(hashMap);
			}
		} catch (SQLException e) {
			logger.handleError("Exception caught : ", e);
		}
		return list;
	}
	
	private LogUtils logger = new LogUtils(this);
}
