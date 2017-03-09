package com.freshdirect.cms.publish.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;

/**
 * DataSource configuration for Content Publish service
 * 
 * @author segabor
 *
 */
public final class DatabaseConfig {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class);

    /**
     * CMS DataSource
     */
    private static DataSource dataSource = null;

    public static void setDataSource(DataSource dataSource) {
        DatabaseConfig.dataSource = dataSource;
    }

    /**
     * Returns data source for CMS DB access The default data source is the one set in CMSServiceConfig.xml
     * However, it can be overridden by explicit value.
     * 
     * NOTE! Don't use it directly! Instead use {@link #getConnection()} to obtain JDBC connection.
     * 
     * @return
     */
    public static DataSource getDataSource() {
        if (DatabaseConfig.dataSource == null) {
            dataSource = lookupDataSourceInJNDI();
        }

        return dataSource;
    }

    /**
     * Return JDbC connection for CMS DB
     * 
     * @return
     * @throws SQLException 
     */
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    /**
     * Construct JNDI lookup context
     * 
     * @return
     * @throws NamingException
     */
    private static Context getInitialContext() throws NamingException {
        Hashtable<String, String> h = new Hashtable<String, String>();
        h.put(Context.INITIAL_CONTEXT_FACTORY, FDStoreProperties.getInitialContextFactory());
        h.put(Context.PROVIDER_URL, FDStoreProperties.getProviderURL());
        return new InitialContext(h);
    }

    private static DataSource lookupDataSourceInJNDI() {
        DataSource jndiDataSource = null;
        
        try {
            final Context ctx = DatabaseConfig.getInitialContext();
            jndiDataSource = (DataSource) ctx.lookup("fddatasource");
        } catch (NamingException e) {
            LOGGER.error("fddatasource data source not found in JNDI", e);
        }
        
        return jndiDataSource;
    }
}
