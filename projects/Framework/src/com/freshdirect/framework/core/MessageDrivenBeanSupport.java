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
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.jms.MessageListener;
import javax.ejb.*;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public abstract class MessageDrivenBeanSupport implements MessageDrivenBean, MessageListener {
	
	final static Logger LOGGER = LoggerFactory.getInstance(MessageDrivenBeanSupport.class);
	   
	private MessageDrivenContext messageDrivenCtx;

	public void ejbRemove() {
	}

	public void ejbCreate() {
	}

	public void setMessageDrivenContext(MessageDrivenContext ctx) {
		this.messageDrivenCtx = ctx;
	}
	
	public MessageDrivenContext getMessageDrivenContext() {
		return this.messageDrivenCtx;	
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
    protected final void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqle) {
            LOGGER.error("problem in closing connection : " + this.getClass().getName() + " err: " + sqle.getMessage(), sqle);
        }
    }
    
    protected final void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqle) {
                LOGGER.error("problem in closing result set: " + this.getClass().getName() + " err: " + sqle.getMessage(), sqle);
            }
        }
    }

}