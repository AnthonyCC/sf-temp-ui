package com.freshdirect.framework.conf;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @author htai
 *
 * TODO: DON'T USE THIS IN PRODUCTION...
 */
public class JndiDataSource implements DataSource {

	private final String jndiName;

	private final DataSource dataSource;

	public JndiDataSource(String jndiName) throws NamingException {
		this.jndiName = jndiName;
		this.dataSource = (DataSource) lookup(this.jndiName);
	}

	public int getLoginTimeout() throws SQLException {
		return getDataSource().getLoginTimeout();
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		this.getDataSource().setLoginTimeout(seconds);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return getDataSource().getLogWriter();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		getDataSource().setLogWriter(out);
	}

	public Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return getDataSource().getConnection(username, password);
	}

	private DataSource getDataSource() {
		return this.dataSource;
	}

	private static Object lookup(String jndiName) throws NamingException {
		InitialContext ctx = null;
		try {
			ctx = new InitialContext();
			return ctx.lookup(jndiName);
		} finally {
			if (ctx != null) {
				ctx.close();
			}
		}
	}

	public boolean isWrapperFor(Class arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public Object unwrap(Class arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
