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

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * an abstract support class providing the functionality that all
 * SessionBeans must at least implement
 * @version $Revision$
 * @author $Author$
 */ 
public abstract class SessionBeanSupport implements SessionBean {
    
    final static Logger LOGGER = LoggerFactory.getInstance(SessionBeanSupport.class);
    
    /** The session context provided by the EJB container */    
    private SessionContext sessionCtx = null;

    /** default constructor
     * @link dependency
     */
    /*#SessionBeanI lnkSessionBeanI;*/

    public SessionBeanSupport() {
        super();
    }
    
    /** sets the session context for this bean. called by the EJB container.
     * @param context the session context provided by the EJB container
     */    
    public void setSessionContext(SessionContext context) {
        this.sessionCtx = context;
    }
    
    /** gets the session context provided by the EJB container
     * @return the session context for this bean
     */    
    protected SessionContext getSessionContext() {
        return this.sessionCtx;
    }
    
    /**
     * called by the EJB container when a new instance of this bean is required
     */
    public void ejbCreate() throws CreateException {
        // nothing required
    }
    
    /**
     * called by the EJB container when an instance of this bean is no longer needed
     */
    public void ejbRemove() {
        // nothing required
    }
    
    /**
     * Called by the EJB container when an instance of this bean is moved from the passive to the active state.
     */ 
    public void ejbActivate() {
    }
    
    /**
     * Called by the EJB container when the bean is moved from an active to the passivated state.
     */    
    public void ejbPassivate() {
    }
    
    /**
     * Retreives a database connection. Caches the underlying DataSource
     * (using the key from template method <CODE>getResourceCacheKey()</CODE>).
     *
     * @see DataSourceLocator
     *
     * @throws SQLException if any problems were encountered while trying
     * to find that DataSource that retreives connections from a pool
     */
    public Connection getConnection() throws SQLException {
   		return DataSourceLocator.getConnection( this.getResourceCacheKey() );
    }

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return null if caching is not supported by this bean (default implementation).
	 */
    protected String getResourceCacheKey() {
    	return null;
    }

    /**
     * Gets the next unique Id to use when writing a persistent object
     * to a persistent store for the first time.
     *
     * @param conn a SQLConnection to use to find an Id
     * @throws SQLException any problems while getting a unique id
     * @return an id that uniquely identifies a persistent object
     */    
    protected String getNextId(Connection conn, String schema) throws SQLException {
    	return SequenceGenerator.getNextId(conn, schema);
    }

    protected void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqle) {
            LOGGER.error("problem in closing connection : " + this.getClass().getName() + " err: " + sqle.getMessage(), sqle);
        }
    }

}
