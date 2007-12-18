package com.freshdirect.framework.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.InvalidNameException;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Just a little wrapper to keep all the jndi stuff in one place
 */
public class JndiWrapper {
	public static Connection getConnection(String name) throws SQLException, NamingException {
		Context ctx = new InitialContext();
		
		if(ctx == null) {
			System.err.println("No context.  We're doomed");
			return null;
		}
		
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/" + name);
		
		if (ds == null) {
			System.err.println("No datasource.  Abandon all hope");
		}
	
		
		getDatasources();
		
		return ds.getConnection();
	}

	public static DataSource getDataSource(String name) throws SQLException, NamingException {
		Context ctx = new InitialContext();
		
		if(ctx == null) {
			System.err.println("No context.  We're doomed");
			return null;
		}
		
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/" + name);
		
		if (ds == null) {
			System.err.println("No datasource.  Abandon all hope");
		}

		return ds;
	}
	
	public static List getDatasources() throws InvalidNameException, NamingException {
		Context ctx = new InitialContext();
		List ret    = new ArrayList();
		
		// Worry about mapping later.
		if(1 == 1) {
			ret.add("fddatasource");
			return ret;
		}
		
		
		if(ctx == null) {
			System.err.println("No context.  We're doomed");
			return null;
		}
		
		NamingEnumeration ne = ctx.list(new CompositeName("java:comp/env/jdbc"));
		
		while(ne.hasMore()) {
			NameClassPair ncp = (NameClassPair) ne.next();
			ret.add(ncp.getName());
		}
		
		return ret;
	}
}
