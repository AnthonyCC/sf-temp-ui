package com.freshdirect.routing.truckassignment.analysis;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

public class ConnectionInfo {

	private String JDBCUrl;
	private String JDBCUser;
	private String JDBCPassword;

	protected ConnectionInfo(String jdbcurl, String jdbcuser,
			String jdbcpassword) {
		this.JDBCUrl = jdbcurl;
		this.JDBCUser = jdbcuser;
		this.JDBCPassword = jdbcpassword;
	}

	public String getUrl() {
		return JDBCUrl;
	}

	public String getUser() {
		return JDBCUser;
	}

	public String getPassword() {
		return JDBCPassword;
	}

	public Connection getNewConnection() throws SQLException {
		OracleConnectionPoolDataSource dataSource = new OracleConnectionPoolDataSource();
		dataSource.setURL(JDBCUrl);
		dataSource.setUser(JDBCUser);
		dataSource.setPassword(JDBCPassword);
		return dataSource.getConnection();
	}
	
	public OracleConnectionPoolDataSource getDataSource() throws SQLException {
		OracleConnectionPoolDataSource dataSource = new OracleConnectionPoolDataSource();
		dataSource.setURL(JDBCUrl);
		dataSource.setUser(JDBCUser);
		dataSource.setPassword(JDBCPassword);
		return dataSource;
	}

	public static final ConnectionInfo EUEDGE = new ConnectionInfo(
			"jdbc:oracle:thin:@zetor:1521:DBEU01", "fdstore", "fdstore");

	public static final ConnectionInfo PROD = new ConnectionInfo(
			"jdbc:oracle:thin:@(DESCRIPTION=("
					+ "ADDRESS=(PROTOCOL=TCP)(HOST=nyc2stdb01-vip.nyc2.freshdirect.com)(PORT=1521))"
					+ "(ADDRESS=(PROTOCOL=TCP)(HOST=nyc2stdb02-vip.nyc2.freshdirect.com)(PORT = 1521))"
					+ "(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=dbsto_prod)(failover_mode=(type=session)(method=basic)(retries=20))))",
			"APPDEV", "readn0wrt");

	public static final ConnectionInfo DWDEV = new ConnectionInfo(
			"jdbc:oracle:thin:@(DESCRIPTION=("
					+ "ADDRESS=(PROTOCOL=TCP)(HOST=nyc1dbcl01-vip01.nyc1.freshdirect.com)(PORT=1521))"
					+ "(ADDRESS=(PROTOCOL=TCP)(HOST=nyc1dbcl01-vip01.nyc1.freshdirect.com)(PORT = 1521))"
					+ "(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=devint)(failover_mode=(type=session)(method=basic)(retries=20))))",
			"FDTRN_PRDA", "FDTRN_PRDA");

	public static final ConnectionInfo MKTDW = new ConnectionInfo(
			"jdbc:oracle:thin:@(DESCRIPTION=("
					+ "ADDRESS_LIST=(ADDRESS = (PROTOCOL= TCP)(HOST=nyc1fddb05.nyc1.freshdirect.com)(PORT=1521)))"
					+ "(CONNECT_DATA = (SERVICE_NAME = MKTDW)))", "victor",
			"victor999");

	public static final ConnectionInfo STANDBY = new ConnectionInfo(
			"jdbc:oracle:thin:@( DESCRIPTION =("
					+ "ADDRESS_LIST =(ADDRESS =(PROTOCOL = TCP)(HOST =nyc1fddb04-vip.nyc1.freshdirect.com)(PORT = 1521)))"
					+ "(CONNECT_DATA=(SERVICE_NAME=dbsto_readonly)(INSTANCE_NAME = DBSTOSBY)(SERVER=DEDICATED)))",
			"APPDEV", "readn0wrt");
}