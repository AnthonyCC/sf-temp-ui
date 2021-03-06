/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Provides an efficient way of finding DataSources.
 *
 * @version $Revision$
 * @author $Author$
 */
public class DataSourceLocator {

	/** DataSource objects hashed by home interface names */
	private final static HashMap dataSourceCache = new HashMap();
	static String DEFAULT_DATASOURCE_CACHE_KEY = "";

    /**
     * Retreives a DataSource using information provided by an enterprise
     * bean's deployment descriptor, as described in getDataSource().
     *
     * This method performs caching of DataSource objects, to avoid the expensive
     * JNDI lookups. DataSources are cached by the name provided in the <CODE>cacheKey</CODE>
     * parameter.     
     *
     * @throws NamingException if any problems were encountered while trying to find the DataSource
     */
	public static DataSource getDataSource(String cacheKey) throws NamingException {
		DataSource ds = (DataSource) dataSourceCache.get( cacheKey );
		if (ds==null) {
			ds = DataSourceLocator.getUncachedDataSource();
			synchronized (dataSourceCache) {
				dataSourceCache.put(cacheKey, ds);
			}
		}
		return ds;
	}

	public static DataSource getDataSource() throws NamingException {
		return getDataSource(DEFAULT_DATASOURCE_CACHE_KEY);
	}
	
    /**
     * Retreives a DataSource using information provided
     * by an enterprise bean's deployment descriptor.
     *
     * Bean implementors must create a &lt;resource-ref&gt;
     * in the ejb-jar.xml deployment descriptor that has a standard
     * &lt;res-ref-name&gt; of jdbc/dbpool and in the
     * weblogic-ejb-jar.xml deployment descriptor they must
     * bind that &lt;res-ref-name&gt; name to the correct JNDI name of the
     * TXDataSource as defined in weblogic.properties
     * for every bean they write.
     *
     * Example:
     * <code>
     *      the resource-ref in ejb-jar.xml
     *      ...
     *      &lt;resource-ref&gt;
     *          &lt;res-ref-name&gt;jdbc/dbpool&lt;/res-ref-name&gt;
     *          &lt;res-type&gt;javax.sql.DataSource&lt;/res-type&gt;
     *          &lt;res-auth&gt;Container&lt;/res-ref&gt;
     *      &lt;/resource-ref&gt;
     *      ...
     *
     *      refers to the resource-description in weblogic-ejb-jar.xml
     *      ...
     *     &lt;reference-descriptor&gt;
     *         &lt;resource-description&gt;
     *             &lt;res-ref-name&gt;jdbc/dbpool&lt;/res-ref-name&gt;
     *             &lt;jndi-name&gt;erpservices&lt;/jndi-name&gt;
     *         &lt;/resource-description&gt;
     *     &lt;/reference-descriptor&gt;
     *     ...
     *
     *     which refers to the TXDataSource in weblogic.properties
     *     ...
     *     weblogic.jdbc.TXDataSource.erpservices=erpservices
     *     ...
     *
     *     which refers to the connectionPool in weblogic.properties
     *     ...
     *     weblogic.jdbc.connectionPool.erpservices=\
     *     driver=oracle.jdbc.driver.OracleDriver,\
     *     ...
     * </code>
     *
     * @throws NamingException if any problems were encountered while trying to find the DataSource
     */
	private static DataSource getUncachedDataSource() throws NamingException {
    	InitialContext initCtx = null;
        try {
            initCtx = new InitialContext();
            return (DataSource) initCtx.lookup("java:comp/env/jdbc/dbpool");
        } finally {
        	if (initCtx!=null) try {
        		initCtx.close();
        	} catch (NamingException ne) {}
        }
     }

	/**
     * Retreives a database connection using information provided by
     * an enterprise bean's deployment descriptor. Performs no caching.
	 *
     * @throws SQLException if any problems were encountered while trying
     * to find that DataSource that retreives connections from a pool
     */
	private static Connection getConnection() throws SQLException {
		try {
			return DataSourceLocator.getDataSource(DEFAULT_DATASOURCE_CACHE_KEY).getConnection();
        } catch (NamingException ne) {
            throw new SQLException("Unable to find DataSource to get Connection: " + ne.getMessage());
		}
	}

	/**
     * Retreives a database connection using information provided by
     * an enterprise bean's deployment descriptor, caching the underlying
     * DataSource with the specified <CODE>cacheKey</CODE>. If <CODE>cacheKey</CODE>
     * is null, this method calls getConnection() and performs no caching.
	 *
     * @throws SQLException if any problems were encountered while trying
     * to find that DataSource that retreives connections from a pool
     */
	public static Connection getConnection(String cacheKey) throws SQLException {
		if (cacheKey==null) {
			return DataSourceLocator.getConnection();
		}
		try {
			return DataSourceLocator.getDataSource(cacheKey).getConnection();
        } catch (NamingException ne) {
            throw new SQLException("Unable to find DataSource to get Connection: " + ne.getMessage());
		}
	}

	/** generally this method should not be used as datasource references are injected into the ejbs */
	@Deprecated
	public static Connection getConnectionByDatasource(String JNDIName) throws SQLException {
		try {
			return DataSourceLocator.getDataSourceByJNDIName(JNDIName).getConnection();
        } catch (NamingException ne) {
            throw new SQLException("Unable to find DataSource to get Connection: " + ne.getMessage());
		}
	}

	/** generally this method should not be used as datasource references are injected into the ejbs */
	@Deprecated
	private static DataSource getDataSourceByJNDIName(String JNDIName) throws NamingException {
    	InitialContext initCtx = null;
        try {
            initCtx = new InitialContext();
            return (DataSource) initCtx.lookup(JNDIName);
        } finally {
        	if (initCtx!=null) try {
        		initCtx.close();
        	} catch (NamingException ne) {}
        }
     }
}