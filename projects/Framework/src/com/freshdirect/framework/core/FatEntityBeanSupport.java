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
 *
 * @version    $Revision$
 * @author     $Author$
 */
public abstract class FatEntityBeanSupport extends FatEntityBeanBase {
    
    private static Category LOGGER = LoggerFactory.getInstance( FatEntityBeanSupport.class );
    
    public FatEntityBeanSupport() {
        super();
    }
    
    public FatPrimaryKey ejbFindByPrimaryKey(FatPrimaryKey fpk) throws ObjectNotFoundException, FinderException {
        Connection conn = null;
        try {
        	conn = this.getConnection();
            // found, create payload for the fat key
            PayloadI payload = this.loadRowPayload( conn, fpk );
            if (payload==null) {
                throw new ObjectNotFoundException("Unable to find PK " + fpk);
            }
            // return key
            return constructPrimaryKey(fpk, payload);
        } catch (SQLException sqle) {
            LOGGER.warn("An error occured in ejbFindByPrimaryKey", sqle);
            throw new FinderException(sqle.getMessage());
        } finally {
            try {
                if (conn!=null) {
                    conn.close();
                }
            } catch (SQLException sqle2) {
                // eat it
            }
        }
    }
    
    /**
     * Factory template method for creating primary keys.
     *
     * @param pk original pk
     * @param payload payload object
     *
     * @return a subclass of PrimaryKey, based on the required information in pk and the payload
     */
    public FatPrimaryKey constructPrimaryKey(FatPrimaryKey fpk, PayloadI payload) {
        return new FatPrimaryKey(fpk.getId(), payload);
    }
    
    /**
     * Optimized ejbLoad, using fat PK.
     * If payload is set, calls setFromPayload(payload), otherwise calls load(connection).
     */
    public void ejbLoad() {
        FatPrimaryKey fatpk = (FatPrimaryKey)this.getEntityContext().getPrimaryKey();
        PayloadI payload = fatpk.getPayload();
        if (payload==null) {
            super.ejbLoad();
        } else {
            this.setPK( fatpk );
            this.setFromPayload( payload );
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
    public FatPrimaryKey ejbCreate(ModelI model) throws CreateException {
        Connection conn = null;
        try {
            initialize();
            setFromModel(model);
            conn = getConnection();
            FatPrimaryKey fpk = (FatPrimaryKey) create(conn);
            return fpk;
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
     * Perform any additional tasks after this object's data has been created in the persistent store.
     *
     * @param model the model that was used in the corresponding create method
     */
    public void ejbPostCreate(ModelI model) {
        this.unsetModified();
    }
    
}
