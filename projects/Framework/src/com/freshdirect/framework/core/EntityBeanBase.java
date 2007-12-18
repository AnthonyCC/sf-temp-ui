/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

import java.sql.*;
import javax.ejb.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

/**
 * Provides the basic functionality of all entity beans.
 *
 * @version $Revision$
 * @author $Author$
 */ 
abstract class EntityBeanBase extends PersistentBeanBase implements EntityBeanI {

	/** log4j */
	private final static Category LOGGER = LoggerFactory.getInstance( EntityBeanBase.class );
    
    /** the entity context provided by the EJB container */    
    private EntityContext entityCtx;
  
       
    /** default constructor */    
    public EntityBeanBase() {
        super();
    }
    
    /**
     * Sets the entity context for this entity. Called by the EJB container.
     *
     * @param context the entity context
     */    
    public void setEntityContext(EntityContext context)  {
        this.entityCtx = context;
        setModified();
    }
    
    /**
     * Removes the context from this entity. Called by the EJB container.
     */    
    public void unsetEntityContext() {
        this.entityCtx = null;
    }
    
    /**
     * Cconvenience method for subclasses to get the entity context.
     *
     * @return the entity context set by the EJB container
     */    
    protected EntityContext getEntityContext() {
        return this.entityCtx;
    }
    
    /**
     * Called by the EJB container when an instance of this bean is moved from the passive to the active state.
     */    
    public void ejbActivate() {
    	setModified();
    }
    
    /**
     * Called by the EJB container when the bean is moved from an active to the passivated state.
     */    
    public void ejbPassivate() {
        setModified();
    }
    
    /**
     * Reads an entity instance's data from a persistent store such as an RDBMS.
     */    
    public void ejbLoad() {
    	initialize();
        Connection conn = null;
        try {
   			this.setPK( (PrimaryKey)this.getEntityContext().getPrimaryKey() );
            conn = getConnection();
            load(conn);
            unsetModified();
        } catch (SQLException sqle) {
        	LOGGER.warn("Error in ejbLoad, throwing EJBException", sqle);
            throw new EJBException(sqle);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    LOGGER.warn("Unable to close connection after ejbLoad()");
                }
            }
        }

        if (this.isModified()) {
	        LOGGER.warn("ASSERTION FAILED: ejbLoad finished for "+this+" with isModified true");
        }

    }
    
    /**
     * Writes an instance's data to a persistent store.
     */    
    public void ejbStore() {
		if (!isModified()) {
			return;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ejbStore for "+this);
		}

		if (this.getEntityContext().getRollbackOnly()) {
			LOGGER.warn("ejbStore called after rollback");
			return;
		}

        Connection conn = null;
        try {
            conn = getConnection();
            store(conn);
            unsetModified();
        } catch (SQLException sqle) {
        	LOGGER.warn("Error in ejbStore, setting rollbackOnly, throwing EJBException", sqle);
            throw new EJBException(sqle);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    LOGGER.warn("Unable to close connection after ejbStore()");
                }
            }
        }
    }
    
    /**
     * Removes an instance's data from the persistent store.
     *
     * @throws RemoveException any problems during writing to the store, such as attempting to remove an object that doesn't exist in the persistent store
     */    
    public void ejbRemove() throws RemoveException {
        Connection conn = null;
        try {
            conn = getConnection();
            remove(conn);
            setModified();
        } catch (SQLException sqle) {
        	LOGGER.warn("Error in ejbRemove, setting rollbackOnly, throwing RemoveException", sqle);
            throw new RemoveException("SQLException in ejbRemove()" + sqle.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    LOGGER.warn("Unable to close connection after ejbRemove()");
                }
            }
        }
    }
    
    /**
     * a method that does nothing but throw an exception
     * its purpose is to force the container to discard
     * an instance from its cache and re-instantiate it
     * next time it is requested
     * @throws RemoteException unexpected system level exception
     * @throws EJBException this exception is intentionally thrown by this method to force the
     * container to discard an instance
     */
    public void invalidate() {
        throw new EJBException("Invalidate");
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
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
    	return "EntityBean[" + this.getClass().getName() + ":" + this.getPK() + "]";
	}    
}
