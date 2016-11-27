/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

import javax.ejb.*;
import java.sql.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

/**
 * Abstract base class for EntityBean implementation classes.
 *
 * Application needs to implement the following methods:
 * <UL>
 * <LI>load()
 * <LI>store()
 * <LI>create()
 * <LI>remove()
 * <LI>getModel(), that can call decorateModel()
 * <LI>decorateModel, that can call super.decorateModel()
 * <LI>setFromModel()
 * </UL>
 *
 * @version     $Revision$
 * @author      $Author$
 */
public abstract class EntityBeanSupport extends EntityBeanBase {
    
    /** log4j */
	private static Category LOGGER = LoggerFactory.getInstance( EntityBeanSupport.class );
    
    /** default constructor
     */
    public EntityBeanSupport() {
        super();
    }
    
    /**
     * Default creation method for entities.
     * Writes a new instance of this entity to the persistent store.
     *
     * @throws CreateException any problems during create, such as an inability to insert a row in a database table
     *
     * @return the primary key of the newly created entity
     */
    public PrimaryKey ejbCreate() throws CreateException {
        Connection conn = null;
        try {
            initialize();
            conn = getConnection();
            PrimaryKey pk = create(conn);
            return pk;
        } catch (SQLException sqle) {
            LOGGER.warn("Error in ejbCreate(), setting rollbackOnly, throwing CreateException", sqle);
            throw new CreateException("SQLException in ejbCreate()" + sqle.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    LOGGER.warn("Unable to close connection after ejbCreate()");
                }
            }
        }
    }
    
    /**
     * Creates an entity from data provided by a model object.
     * Writes a new instance of this entity to the persistent store.
     *
     * @param model the data to use to create the new entity
     * @throws CreateException any problems during create, such as an inability to insert a row in a database table
     *
     * @return the primary key of the newly created entity
     */
    public PrimaryKey ejbCreate(ModelI model) throws CreateException {
        Connection conn = null;
        try {
            initialize();
            setFromModel(model);
            conn = getConnection();
            PrimaryKey pk = create(conn);
            return pk;
        } catch (SQLException sqle) {
            LOGGER.warn("Error in ejbCreate(model), setting rollbackOnly, throwing CreateException", sqle);
            throw new CreateException("SQLException in ejbCreate(ModelI) "+sqle.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    LOGGER.warn("Unable to close connection after ejbCreate(ModelI)");
                }
            }
        }
    }
    
    /**
     * Perform any additional tasks after a default object has been created in the persistent store.
     */
    public void ejbPostCreate() {
        this.unsetModified();
    }
    
    /**
     * Perform any additional tasks after this object's data has been created in the persistent store.
     *
     * @param model the model that was used in the corresponding create method
     */
    public void ejbPostCreate(ModelI model) {
        this.unsetModified();
    }
    
}